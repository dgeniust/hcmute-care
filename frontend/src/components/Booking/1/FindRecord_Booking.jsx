import React, {useState, useEffect} from "react";
import { Button, Input, message } from 'antd';
import {InfoCircleTwoTone, ArrowRightOutlined, BarcodeOutlined, PhoneOutlined, RightOutlined   } from '@ant-design/icons';
import {handleHttpStatusCode, notifyErrorWithCustomMessage, notifySuccessWithCustomMessage } from '../../../utils/notificationHelper';

const FindRecord_Booking = () => {
    
    const [visible, setVisible] = useState(false);
    const [messageApi, contextHolder] = message.useMessage();
    const [barcode, setBarcode] = useState('');
    const [medicalRecords, setMedicalRecords] = useState([]);
    const showImgSuggest = () => {
        setVisible(prevState => !prevState);
    }
    const handleChangeBarcode = (e) => {
        const value = e.target.value;
        if (value.length < 10) {
            notifyErrorWithCustomMessage('Số hồ sơ không hợp lệ!', messageApi);
            return;
        }
        setBarcode(value);
    }
    const customerId = localStorage.getItem('customerId');
    const handeSearchRecord = async () => {
        if (!customerId) {
            notifyErrorWithCustomMessage("Customer ID not found in localStorage", messageApi);
            return;
        }
        if (!barcode || barcode.length < 10) {
            notifyErrorWithCustomMessage("Please enter a valid barcode", messageApi);
            return;
        }
        try{
            const response = await fetch(`http://localhost:8080/api/v1/medical-records/${barcode}?customerId=${customerId}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
            });
            if(!response.ok){
                const errorText = await response.text();
                handleHttpStatusCode(response.status, '','Không tìm thấy hồ sơ bệnh án', messageApi);
                console.error('Error fetching medical records:', errorText);
                return;
            }

            const data = await response.json();
            console.log('Raw API response medical record:', data);
            // Handle single object or null response
            let records = [];
            if (data && data.data) {
                records = [data.data]; // Convert single object to array
            } else if (data && !data.data) {
                records = [data]; // Handle case where response is a single object
            }

            if (records.length > 0) {
                const filteredRecords = records
                .filter((record) => {
                    const matches = String(record.customerId) === String(customerId);
                    console.log(
                    `Record customerId: ${record.customerId} (type: ${typeof record.customerId}), ` +
                    `Input customerId: ${customerId} (type: ${typeof customerId}), ` +
                    `Matches: ${matches}`
                    );
                    return matches;
                })
                .map((record) => ({
                    id: record.id,
                    barcode: record.barcode,
                    patient: record.patient || { name: "Unknown", phone: "N/A" }, // Fallback for missing patient data
                }));

                if (filteredRecords.length > 0) {
                    setMedicalRecords(filteredRecords);
                    console.log("Updated medicalRecords state find record:", filteredRecords);
                    notifySuccessWithCustomMessage("Lấy thông tin hồ sơ thành công", messageApi);
                } else {
                    setMedicalRecords([]);
                    notifyErrorWithCustomMessage("Không có dữ liệu hồ sơ bệnh án", messageApi);
                    console.log("No matching medical records found for customerId:", customerId);
                }
            } else {
                setMedicalRecords([]);
                notifyErrorWithCustomMessage("Không tìm thấy hồ sơ nào", messageApi);
            }
        }
        catch(e){
            notifyErrorWithCustomMessage('Lỗi kết nối khi cập nhật hồ sơ', messageApi);
            console.error('Error updating customer:', e);
        }
    }
    useEffect(() => {
        console.log('Barcode:', barcode);
    },[barcode])
    return (
        <div className="w-full h-fit min-h-[460px] p-8 flex flex-col gap-4">
                        <div className="flex flex-col">
                            <p className="text-black">Vui lòng nhập chính xác số hồ sơ</p>
                            <div className="w-full h-fit flex flex-row gap-4 items-center mt-4">
                                <Input style={{fontWeight: 'bold', width: '100%', height: '50px'}} placeholder="N18-000XXXX" onChange={handleChangeBarcode}/>
                                <Button style={{backgroundColor: '#273c75', color: 'white', fontWeight: 'bold', width: '200px', height: '50px'}} onClick={handeSearchRecord}>Tìm kiếm</Button>
                            </div>
                            <div className="space-y-1 mt-1">
                                {
                                    medicalRecords && medicalRecords.length > 0 ? (
                                        medicalRecords.map((record) => (
                                            <Button
                                            key={record.id}
                                            style={{
                                                display: 'flex',
                                                flexDirection: 'column',
                                                height: '70px',
                                                width: '100%',
                                                padding: '16px',
                                                backgroundColor: 'transparent',
                                                color: '#273c75',
                                                border: '1px solid #273c75',
                                            }}
                                            type="primary"
                                            size="large"
                                        >
                                            <div className='flex flex-row justify-between w-full'>
                                                <div className='space-x-1'>
                                                    <InfoCircleTwoTone />
                                                    <span className='font-bold text-sm tracking-normal'>{record.patient.name}</span>
                                                </div>
                                                <div><RightOutlined /></div>
                                            </div>
                                            <div className='flex flex-row justify-between w-full'>
                                                <div className='flex flex-row w-full text-sm'>
                                                    <div className='mr-18 flex flex-row h-fit space-x-1'>
                                                        <BarcodeOutlined />
                                                        <span>{record.barcode}</span>
                                                    </div>
                                                    <div className='flex flex-row h-fit space-x-1'>
                                                        <PhoneOutlined />
                                                        <span>{record.patient.phone}</span>
                                                    </div>
                                                </div>
                                            </div>
                                        </Button>
                                        ))
                                    ) : 
                                    <div className="flex flex-row gap-4 items-center">
                                        <InfoCircleTwoTone />
                                        <p className="text-[#273c75] items-center text-center font-bold text-sm cursor-pointer hover:text-blue-500">Không tìm thấy hồ sơ nào</p>
                                    </div>
                                }
                                
                            </div>
                        </div>
                        <div className="text-black flex flex-col gap-4 text-base">
                            <div className="flex flex-row gap-4 items-center">
                            <InfoCircleTwoTone />
                            <p>Số hồ sơ được in trên toa thuốc, phiếu chỉ định hoặc phiếu trả kết quả cận lâm sàng </p>
                            </div>
                            <p className="text-[#273c75] items-center text-center font-bold text-sm cursor-pointer hover:text-blue-500" onClick={showImgSuggest}>Xem gợi ý <ArrowRightOutlined /></p>
                        </div>
                        {
                            visible ? (<div className="img-suggest w-full h-[360px] border border-black">
        
                                </div>) : null
                        }
                        {contextHolder}
        </div>
    )
}
export default FindRecord_Booking;
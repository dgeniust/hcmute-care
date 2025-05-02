import React, { useEffect, useState } from 'react';
import {Button, Modal, message} from 'antd';
import { InfoCircleTwoTone, RightOutlined, BarcodeOutlined, PhoneOutlined } from '@ant-design/icons';
import UserDetails from '../../components/Personal/UserDetails';
import { handleHttpStatusCode, notifySuccessWithCustomMessage, notifyErrorWithCustomMessage } from '../../utils/notificationHelper';
const MedicalRecord = () => {
    const [isModalInfoOpen, setIsModalInfoOpen] = useState(false);
    const [modalContent, setModalContent] = useState(null);
    const [modalButton, setmodalButton] = useState(false);
    const [medicalRecords, setMedicalRecords] = useState([]); // State to store medical records
    const handleModalButtonClose = () => {
        setmodalButton(false);
    }
    const showButtonButtonModal = (userData) => {
        setModalContent(userData);
        setmodalButton(true);
    };

    const handleInfoCancel = () => {
        setIsModalInfoOpen(false);
        setModalContent(null);
    };
    const [messageApi, contextHolder] = message.useMessage();
    const customerId = localStorage.getItem('customerId');
    useEffect(() => {
        const handleDataMedicalRecord = async () => {
            try{
                const response = await fetch(`http://localhost:8080/api/v1/customers/${customerId}/medicalRecords?page=1&size=10&sort=id&direction=asc`, {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json',
                        Authorization: `Bearer ${localStorage.getItem('accessToken')}`,
                    },
                });
                if(!response.ok) {
                    const errorText = await response.text();
                    console.error(`Error fetching data: ${errorText || response.statusText}`);
                    handleHttpStatusCode(response.status, '',`Lấy hồ sơ bệnh án thất bại: ${errorText || response.statusText}`);
                    return;
                }
                const data = await response.json();
                console.log('Raw API response:', data);
                if(data && data.data.content.length > 0) {
                    const medicalRecords = data.data.content
                    .filter((record) => {
                        // Convert both to strings to avoid type mismatch
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
                        patient: record.patient,
                    }));
                    if(medicalRecords.length > 0){
                        setMedicalRecords(medicalRecords);
                        console.log('Updated medicalRecords state:', medicalRecords);
                        notifySuccessWithCustomMessage('Lấy thông tin hồ sơ thành công', messageApi);
                    }
                    else{
                        notifyErrorWithCustomMessage('Không có dữ liệu hồ sơ bệnh án', messageApi);
                        console.log('No matching medical records found for customerId:', customerId);
                    } 
                }
            }
            catch (e) {
                notifyErrorWithCustomMessage('Lỗi kết nối khi cập nhật hồ sơ', messageApi);
                console.error('Error updating customer:', e);
            }
        }
        handleDataMedicalRecord();
    }, [])
    return (
        <div className='w-full h-full p-8 border border-black'>
            <h1 className='text-black font-bold text-xl text-center w-full '>Hồ sơ người bệnh</h1>
            <div className='w-[70%] h-full rounded-lg flex items-center justify-center mx-auto my-4 flex flex-col space-y-2'>
                {  
                    medicalRecords && medicalRecords.length > 0 ? (
                        medicalRecords.map((user) => (
                            <Button
                                key={user.id} // Thêm key để React có thể theo dõi các phần tử trong danh sách
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
                                onClick={() => showButtonButtonModal(user)}
                            >
                                <div className='flex flex-row justify-between w-full'>
                                    <div className='space-x-1'>
                                        <InfoCircleTwoTone />
                                        <span className='font-bold text-sm tracking-normal'>{user.patient.name}</span>
                                    </div>
                                    <div><RightOutlined /></div>
                                </div>
                                <div className='flex flex-row justify-between w-full'>
                                    <div className='flex flex-row w-full text-sm'>
                                        <div className='mr-18 flex flex-row h-fit space-x-1'>
                                            <BarcodeOutlined />
                                            <span>{user.barcode}</span>
                                        </div>
                                        <div className='flex flex-row h-fit space-x-1'>
                                            <PhoneOutlined />
                                            <span>{user.patient.phone}</span>
                                        </div>
                                    </div>
                                </div>
                            </Button>
                        ))
                    ) : 
                        <div className='w-full h-full flex items-center justify-center'>
                            <h1 className='text-black font-bold text-base'>Không có dữ liệu hồ sơ bệnh án</h1>
                        </div>
                }
            </div>
            <Modal title="Thông tin người bệnh 👨‍🦰" open={isModalInfoOpen} onCancel={handleInfoCancel} footer={null}>
                {modalContent && <UserDetails modalContent={modalContent} />}
            </Modal>
            <Modal
                centered
                open={modalButton}
                onCancel={handleModalButtonClose}
                footer={null}
            >
                <div className='text-center'>
                    <h1 className='text-lg mb-4 font-bold'>
                       CHỌN CHỨC NĂNG
                    </h1>
                </div>
                <div className='flex flex-col gap-3'>
                    <Button style={{ color: '#273c75', border: '1px solid #273c75' }}
                    onClick={() => {
                        setIsModalInfoOpen(true);
                        setmodalButton(false);
                    }}
                    >
                        THÔNG TIN HỒ SƠ
                    </Button>
                    <Button style={{ color: '#273c75', border: '1px solid #273c75' }}
                    onClick={() => {
                        setmodalButton(false);
                    }}
                    >
                        XEM HỒ SƠ SỨC KHỎE
                    </Button>
                    <Button style={{ color: '#273c75', border: '1px solid #273c75' }}
                    onClick={() => {
                        setmodalButton(false);
                    }}
                    >
                        XEM KẾT QUẢ CẬN LÂM SÀNG NGOẠI TRÚ
                    </Button>
                    <Button style={{ color: '#273c75', border: '1px solid #273c75' }}
                    onClick={() => {
                        setmodalButton(false);
                    }}
                    >
                        XEM HÌNH ẢNH CHỤP
                    </Button>
                    <Button style={{ color: '#273c75', border: '1px solid #273c75' }}
                    onClick={() => {
                        setmodalButton(false);
                    }}
                    >
                        XEM PHIẾU ĐĂNG KÝ KHÁM
                    </Button>
                </div>
            </Modal>
            {contextHolder}
        </div>
    );
}
export default MedicalRecord;
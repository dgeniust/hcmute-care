import React, {useEffect, useState} from 'react';
import { Button, message} from 'antd';
import {RightOutlined, InfoCircleTwoTone} from '@ant-design/icons';
import { handleHttpStatusCode, notifySuccessWithCustomMessage, notifyErrorWithCustomMessage} from '../../../utils/notificationHelper';
const Specialty_Booking = ({setSpecialty, setSpecialtyId, setPrice, setChoosedSpecialty, setStep, ref}) => {
    
    const [messageApi, contextHolder] = message.useMessage();
    const [specialtyData, setSpecialtyData] = useState([]);
    const handleSpecialty = (specialtyId, specialtyName, price) => {
        console.log('Selected specialty id:', specialtyId);
        localStorage.setItem('specialtyId', specialtyId);
    
        // Cập nhật state và chuyển bước
        setSpecialtyId(specialtyId); // Truyền specialtyId lên parent
        setSpecialty(specialtyName);
        setPrice(price);
        setChoosedSpecialty(true);
        setStep(2);
      };
    useEffect(() => {
        const handleDataSpecialty = async () => {
            try {
                const response = await fetch('http://localhost:8080/api/v1/medical-specialties?page=1&size=20&sort=id&direction=asc', {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json',
                        Authorization: `Bearer ${localStorage.getItem('accessToken')}`,
                    },
                });
                if(!response.ok) {
                    const errorText = await response.text();
                    handleHttpStatusCode(response.status, '',`Lấy thông tin chuyên khoa thất bại: ${errorText || response.statusText}`);
                    return;
                }
                const data = await response.json();
                console.log('Raw API response:', data);
                if (data && data.data.content.length > 0) {
                    const specialties = data.data.content.map((specialty) => ({
                        id: specialty.id,
                        name: specialty.name,
                        price: new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(specialty.price).replace('₫', 'đ'),
                        note: specialty.note,
                    }));
                    console.log('Specialty data:', specialties);
                    setSpecialtyData(specialties);
                    notifySuccessWithCustomMessage('Chọn chuyên khoa thành công', messageApi);
                } else {
                    notifyErrorWithCustomMessage('Không có dữ liệu chuyên khoa', messageApi);
                }
            }
            catch (e) {
                notifyErrorWithCustomMessage('Lỗi kết nối khi cập nhật chuyên khoa', messageApi);
                console.error('Error updating customer:', e);
            }
        }
        handleDataSpecialty();
    },[])

    return ( 
        <div className='w-3/4 h-fit flex flex-col' ref={ref}>
            <div className='flex w-full justify-center'>
                <h1 className='text-black font-bold text-base'>Chọn chuyên khoa</h1>
            </div>
            <div className='w-full max-h-[460px] h-[460px] flex flex-col border border-black rounded-xl overflow-y-auto space-y-2'>
                {
                    specialtyData && specialtyData.length > 0 ? (
                        specialtyData.map((item) => (
                            <Button key={item.id} style={{width:'100%', height:'70px', padding: '25px', display: 'flex', justifyContent: 'center',flexDirection:'column', alignItems:'center', border: '1px solid black', borderRadius: '5px'}}
                            >
                            <div className='w-full h-fit flex flex-row items-center justify-between' onClick = {() => handleSpecialty(item.id, item.name, item.price)}>
                                <div className='flex flex-row items-center space-x-4 justify-start'>
                                    <InfoCircleTwoTone style={{fontSize: '20px'}}/>
                                    <p className='text-[#273c75] text-sm font-bold'>{item.name}</p>
                                </div>
                                <div className='flex flex-row items-center'>
                                    <p className='text-black text-sm space-x-4'>
                                        <span>{item.price}</span><RightOutlined />
                                    </p>
                                </div>
                            </div>
                            <div className='w-full h-fit flex items-center'>
                                <p className='text-black text-center '>({item.note})</p>
                            </div>
                        </Button>
                        ))
                    ) : 
                    <div className='flex w-full h-full justify-center items-center'>
                        <h1 className='text-black font-bold text-base'>Không có dữ liệu chuyên khoa</h1>
                    </div>
                }
            </div>
        {contextHolder}
        </div>
    )
}
export default Specialty_Booking;
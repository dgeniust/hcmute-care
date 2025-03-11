import React, { useState, useEffect } from 'react';
import { Button, Collapse, theme, notification } from 'antd';
import {ArrowLeftOutlined, UserAddOutlined, CaretRightOutlined, RestOutlined, ForkOutlined } from '@ant-design/icons';

const ConfirmInfo_Booking = () => {
    const { token } = theme.useToken();
    
    const panelStyle = {
        marginBottom: 5,
        background: token.colorFillAlter,
        borderRadius: token.borderRadiusLG,
        border: 'none',
    };
    const getItems = (panelStyle) => [
        {
            key: '1',
            label: (
                <div className='flex flex-col items-center w-full space-y-2'>
                    <div className='flex flex-row justify-between items-center w-full'>
                        <div className='space-x-4'>
                            <UserAddOutlined />
                            <span>Hồ sơ đăng ký khám bệnh</span>
                        </div>
                    </div>
                </div>
            ),
            children: (
                <div className='w-full h-fit'>
                    <div className='grid grid-flow-row grid-cols-2 gap-4'>
                        <div className='grid grid-flow-row grid-cols-[80px_1fr]'>
                            <p>Họ tên:</p>
                            <p className='text-[#273c75] font-bold'>NGUYỄN THÀNH ĐẠT</p>
                        </div>
                        <div className='grid grid-flow-row grid-cols-[80px_1fr]'>
                            <p>Giới tính:</p>
                            <p>Nam</p>
                        </div>
                        <div className='grid grid-flow-row grid-cols-[80px_1fr]'>
                            <p>Điện thoại:</p>
                            <p>0387731823</p>
                        </div>
                        <div className='grid grid-flow-row grid-cols-[80px_1fr]'>
                            <p>Địa chỉ:</p>
                            <p>1392/10 đường 30/4, Phường 12, Thành phố Vũng Tàu</p>
                        </div>
                    </div>
                </div>
            ),
            style: panelStyle,
    }
    ];
    return (
        <div className='w-full h-fit min-h-[460px] border border-red-600 p-8'>
            <div className='flex flex-row gap-4 w-full h-full items-center'>
                <Button icon={<ArrowLeftOutlined />} style={{backgroundColor:'transparent', border: 'none', boxShadow: 'none'}}></Button>
                <h1 className='text-black font-bold text-lg'>Thông tin đặt khám</h1>
            </div>
            <div className='flex flex-col space-y-4'>
                <p className='text-black'>Vui lòng kiểm tra thông tin đặt khám bên dưới. Hoặc "Thêm chuyên khoa" mới</p>
                <div className='w-full max-h-[460px] h-fit flex flex-col border border-red-600 rounded-xl overflow-y-auto space-y-2'>
                    <Collapse
                    bordered={false}
                    defaultActiveKey={['0']}
                    expandIcon={({ isActive }) => <CaretRightOutlined rotate={isActive ? 90 : 0} 
                    iconPosition='end'/>}
                    expandIconPosition='end'
                    style={{
                        background: token.colorBgContainer,
                        overflow: 'auto'
                    }}
                    items={getItems(panelStyle)}
                    />
                </div>
                <h1 className='text-black text-lg'>Chuyên khoa đã đặt (1)</h1>
                <div className='w-full h-fit flex flex-row justify-between items-center text-black rounded-xl shadow-lg'>
                    <div className="w-[95%] h-fit grid grid-cols-2 grid-flow-row gap-3 text-black p-4">
                        <div className='grid grid-flow-row grid-cols-[150px_1fr]'>
                            <p>Chuyên khoa:</p>
                            <p className='text-[#273c75] font-bold'>SUY TIM</p>
                        </div>
                        <div className='grid grid-flow-row grid-cols-[150px_1fr]'>
                            <p>Ngày khám:</p>
                            <p>10/3/2025</p>
                        </div>
                        <div className='grid grid-flow-row grid-cols-[150px_1fr]'>
                            <p>Phòng - Giờ khám:</p>
                            <p>13:30 - 14:30, Suy Tim - Lầu 14 Khu A</p>
                        </div>
                        <div className='grid grid-flow-row grid-cols-[150px_1fr]'>
                            <p>Phí khám:</p>
                            <p>150.000đ</p>
                        </div>
                        <div className='grid grid-flow-row grid-cols-[150px_1fr]'>
                            <p>BHYT:</p>
                            <p>Không</p>
                        </div>
                        <div className='grid grid-flow-row grid-cols-[150px_1fr]'>
                            <p>BLVP:</p>
                            <p>Không</p>
                        </div>
                    </div>
                    <div className='w-[5%]'>
                        <RestOutlined style={{color: 'red', border: '1px solid red', borderRadius:'50%', width:'20px', height:'20px', display: 'flex', justifyContent:"center", backgroundColor:'transparent', cursor:'pointer'}}/>
                    </div>
                </div>
                <div className='flex flex-row justify-center items-center w-full h-fit space-x-4'>
                    <Button type="primary" className='w-full h-fit mt-4' icon={<ForkOutlined/>} style={{width:'300px', height:'40px', fontSize:'15px', fontWeight:'bold', backgroundColor:'white', color:'blue', border:'1px solid blue'}}>Thêm chuyên khoa</Button>
                    <Button type="primary" className='w-full h-fit mt-4' style={{width:'200px', height:'40px', fontSize:'15px', fontWeight:'bold', backgroundColor:'blue'}}>Tiếp tục</Button>
                </div>
                
            </div>
        </div>
    )
}

export default ConfirmInfo_Booking;
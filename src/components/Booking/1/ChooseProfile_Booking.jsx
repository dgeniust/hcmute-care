import React, { useState } from 'react';
import { Button, Modal, Tag } from 'antd';
import { PlusOutlined, InfoCircleTwoTone, RightOutlined, BarcodeOutlined, PhoneOutlined, HomeOutlined } from '@ant-design/icons';
// import '../css/BookingContent.css';

const ChooseProfile_Booking = ({ setStatus }) => {
    
    const [modalOpen, setModalOpen] = useState(false); // Modal open/close state
    // Function to handle the modal state
    const handleModalClose = () => {
        setModalOpen(false);
    };
    const handleSetStatus = (value) => {
        setStatus(value);
    }
    return (
        <div className='w-full h-full space-y-4'>
            <h1 className='text-black font-bold text-lg'>Chọn hồ sơ đặt khám</h1>
            <div className='flex flex-col gap-4 w-full h-full min-h-[460px] justify-between'>
                <div className='flex flex-row gap-4 w-full h-full'>
                    <div className='grid grid-flow-row grid-cols-3 gap-2 w-1/3'>
                        {/* Add a new profile button */}
                        <Button
                            style={{
                                display: 'flex',
                                flexDirection: 'column',
                                alignItems: 'center',
                                height: '90px',
                                width: '90px',
                                padding: '8px',
                                backgroundColor: 'transparent',
                                color: '#273c75',
                                border: '1px solid #273c75',
                            }}
                            type="primary"
                            size="large"
                            onClick={() => setModalOpen(true)}
                        >
                            <PlusOutlined style={{ fontSize: '30px' }} />
                            <span>Thêm</span>
                        </Button>
                        {/* Existing profiles */}
                        <Button
                            style={{
                                display: 'flex',
                                flexDirection: 'column',
                                alignItems: 'center',
                                textAlign: 'center',
                                height: '90px',
                                width: '90px',
                                padding: '4px',
                                backgroundColor: 'transparent',
                                color: '#273c75',
                                border: '1px solid #273c75',
                            }}
                            type="primary"
                            size="large"
                        >
                            <img
                                width="48"
                                height="48"
                                src="https://img.icons8.com/color/48/businessman.png"
                                alt="businessman"
                            />
                            <span>
                                <Tag color="magenta" style={{ width: '100%', textAlign: 'center' }}>
                                    Đạt
                                </Tag>
                            </span>
                        </Button>
                        <Button
                            style={{
                                display: 'flex',
                                flexDirection: 'column',
                                alignItems: 'center',
                                textAlign: 'center',
                                height: '90px',
                                width: '90px',
                                padding: '4px',
                                backgroundColor: 'transparent',
                                color: '#273c75',
                                border: '1px solid #273c75',
                            }}
                            type="primary"
                            size="large"
                        >
                            <img
                                width="48"
                                height="48"
                                src="https://img.icons8.com/color/48/businesswoman.png"
                                alt="businesswoman"
                            />
                            <span>
                                <Tag color="purple" style={{ width: '100%', textAlign: 'center' }}>
                                    Quỳnh
                                </Tag>
                            </span>
                        </Button>
                    </div>
                    {/* Profile details section */}
                    <div className='flex flex-col space-y-2 w-2/3 h-fit'>
                        <Button
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
                                    <span className='font-bold text-sm tracking-normal'>NGUYỄN THÀNH ĐẠT</span>
                                </div>
                                <div><RightOutlined /></div>
                            </div>
                            <div className='flex flex-row justify-between w-full'>
                                <div className='flex flex-row w-full text-sm'>
                                    <div className='mr-18 flex flex-row h-fit space-x-1'>
                                        <BarcodeOutlined />
                                        <span>W24-0068373</span>
                                    </div>
                                    <div className='flex flex-row h-fit space-x-1'>
                                        <PhoneOutlined />
                                        <span>0387***823</span>
                                    </div>
                                </div>
                            </div>
                        </Button>
                        {/* Other profiles */}
                    </div>
                </div>
                <div className='h-full w-fit'>
                    <Button
                        style={{ backgroundColor: 'transparent' }}
                        icon={<HomeOutlined />}
                        onClick={() => handleSetStatus('records')} // Go back to records view
                    >
                        Quay lại
                    </Button>
                </div>
            </div>
            <Modal
                centered
                open={modalOpen}
                onCancel={handleModalClose}
                footer={null}
            >
                <div className='text-center'>
                    <h1 className='text-lg mb-4 font-bold'>
                        Bạn đã từng khám tại <span className='text-[#273c75]'>Bệnh viện Đại học Y Dược TP.HCM?</span>
                    </h1>
                </div>
                <div className='flex flex-col gap-1'>
                    <Button style={{ color: 'white', backgroundColor: '#273c75' }} onClick={() => {
                        setModalOpen(false);
                        handleSetStatus('hasUser');
                    }}>
                        ĐÃ TỪNG KHÁM, NHẬP HỒ SƠ
                    </Button>
                    <Button style={{ color: '#273c75', border: '1px solid #273c75' }}
                    onClick={() => {
                        setModalOpen(false);
                        handleSetStatus('new');
                    }}
                    >
                        CHƯA TỪNG KHÁM, TẠO HỒ SƠ MỚI
                    </Button>
                </div>
            </Modal>
        </div>
    )
}
export default ChooseProfile_Booking;
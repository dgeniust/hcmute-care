import React, { useState } from 'react';
import {Button, Modal} from 'antd';
import { InfoCircleTwoTone, RightOutlined, BarcodeOutlined, PhoneOutlined } from '@ant-design/icons';
import UserDetails from './UserDetails';
const MedicalRecord = () => {
    const [isModalInfoOpen, setIsModalInfoOpen] = useState(false);
    const [modalContent, setModalContent] = useState(null);
    const [modalButton, setmodalButton] = useState(false);
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
    const users = [
        {
            id: 1,
            name: 'NGUYỄN THÀNH ĐẠT',
            barcode: 'W24-0068373',
            phone: '0387***823',
            dob: '14/01/2004',
            gender: 'Nam',
            cmnd: '077204000553',
            nation: 'Việt Nam',
            job: 'Không',
            email: 'dathiichan141@gmail.com',
            address: '1392/10 đường 30/4 Phường 12 TP.Vũng Tàu'
        }, 
        {
            id: 2,
            name: 'Hàng Diễm Quỳnh',
            barcode: 'W24-0022070',
            phone: '0916***519',
            dob: '22/07/2004',
            gender: 'Nữ',
            cmnd: '************',
            nation: 'Việt Nam',
            job: 'Không',
            email: 'ha.diemquynh22@gmail.com',
            address: 'Ninh Thuận'
        }
    ]
    return (
        <div className='w-full h-full p-8 border border-black'>
            <h1 className='text-black font-bold text-xl text-center w-full '>Hồ sơ người bệnh</h1>
            <div className='w-[70%] h-full rounded-lg flex items-center justify-center mx-auto my-4 flex flex-col space-y-2'>
                {users.map((user) => (
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
                                <span className='font-bold text-sm tracking-normal'>{user.name}</span>
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
                                    <span>{user.phone}</span>
                                </div>
                            </div>
                        </div>
                    </Button>
                ))}
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
        </div>
    );
}
export default MedicalRecord;
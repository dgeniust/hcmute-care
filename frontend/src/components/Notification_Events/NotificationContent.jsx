import React, {useState} from 'react';
import notificationData from './notificationData';
import {FireTwoTone, NotificationTwoTone} from '@ant-design/icons';
import { Modal, Button } from 'antd';
const NotificationContent = () => {
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [data, setData] = useState([]);
    const openNotification = (content) => {
        setData(content);
    }
    const showModal = () => {
        setIsModalOpen(true);
    };
    const handleOk = () => {
        setIsModalOpen(false);
    };
    const handleCancel = () => {
        setIsModalOpen(false);
    };
    return (
        <div className='w-full h-fit flex flex-col text-black space-y-2'>
            {
                notificationData.map((item, index) => (
                    <div className="border border-black p-4 rounded-lg" onClick={()=> {
                        openNotification(item);
                        showModal();
                        }
                    
                    }>
                        <div className='flex flex-row space-x-4' key = {index} >
                            <FireTwoTone twoToneColor="#e74c3c"/>
                            <h1 className='font-bold'>{item.title}</h1>
                        </div>
                        <div>
                            {item.children}
                        </div>
                        <div className='text-end text-xs'>
                            <p>{item.date}</p>
                        </div>
                    </div>
                ))
            }
            <Modal title={<NotificationTwoTone twoToneColor="#e74c3c"/>} open={isModalOpen} onOk={handleOk} onCancel={handleCancel}
            footer={[
                <Button key="back" onClick={handleCancel} style={{width: '100%'}}>
                  Đóng
                </Button>,
              ]}
            >
                    <div className='flex flex-row space-x-4'>
                        <h1 className='font-bold text-[#273c75]'>{data.title}</h1>
                    </div>
                    <div>
                        {data.children}
                    </div>
            </Modal>
        </div>
    )
}

export default NotificationContent;
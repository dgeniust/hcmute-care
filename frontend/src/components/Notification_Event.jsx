import React from 'react';
import { Tabs } from 'antd';
import { DatePicker } from 'antd';
import NotificationContent from './Notification_Events/NotificationContent';
const Notification_Event = () => {
    const onChange = (key) => {
        console.log(key);
    };
    const items = [
        {
          key: '1',
          label: 'Tất cả',
          children: (
            <div className="w-full h-full">
                {/* <div className='w-full h-full min-h-[460px] flex flex-col items-center justify-center'>
                    <img src="" alt="" />
                    <h1 className='font-bold'>Không có dữ liệu!</h1>
                    <p>Các phiếu khám sẽ được hiển thị ở đây, bạn quay lại sau nhé.</p>
                </div> */}
                <NotificationContent/>
            </div>
          ),
        },
        {
          key: '2',
          label: 'Chưa đọc',
          children: (
            <div className="w-full h-full p-4">
                <div className='w-full h-full min-h-[460px] flex flex-col items-center justify-center'>
                    <img src="" alt="" />
                    <h1 className='font-bold'>Không có dữ liệu!</h1>
                    <p>Các phiếu khám sẽ được hiển thị ở đây, bạn quay lại sau nhé.</p>
                </div>
            </div>
          ),
        },
    ];
    return (
        <div className='w-full h-full p-8 border border-black'>
            <h1 className='text-black font-bold text-xl text-center w-full mb-8'>Thông báo</h1>
            <Tabs defaultActiveKey="1" items={items} onChange={onChange} type="card"/>;
        </div>
    );
}
export default Notification_Event;
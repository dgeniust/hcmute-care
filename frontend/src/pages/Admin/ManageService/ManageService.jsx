import React from 'react';
import {DatePicker, Tabs  } from 'antd';
import dayjs from 'dayjs';
import MedicalService from './MedicalService';
import VaccineService from './VaccineService';
const ManageService = () => {
    const today = dayjs()
    const dateFormat = 'DD/MM/YYYY';

    const items = [
        {
          key: '1',
          label: 'Dịch vụ y tế',
          children: <MedicalService/>,
        },
        {
          key: '2',
          label: 'Dịch vụ tiêm phòng',
          children: <VaccineService/>,
        },
        {
          key: '3',
          label: 'Dụng cụ y tế',
          children: 'Content of Tab Pane 3',
        },
    ];
    return (
        <div className="w-full h-full py-8 px-4 text-black bg-gray-50 space-y-10">
            <div className="w-full h-fit flex flex-row justify-between items-center mb-4">
                <h1 className="font-bold text-2xl">Quản lý người dùng</h1>
                <DatePicker prefix="Today" defaultValue={today} format={dateFormat} style={{width: '250px'}}/>
            </div>
            <div className='w-full h-full border p-4'>
                <Tabs defaultActiveKey="1" items={items}/>
            </div>
        </div>
    )
}

export default ManageService;
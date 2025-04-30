import React from 'react';
import dayjs from 'dayjs';
import {Tag, DatePicker  } from 'antd';
import {RiseOutlined, TeamOutlined, FallOutlined} from '@ant-design/icons';
import EmployeeAccounts from './EmployeeAccounts';
const ManageEmployee =() => {
    const today = dayjs()
    const dateFormat = 'DD/MM/YYYY';
    return (
        <div className="w-full h-full py-8 px-4 text-black bg-gray-50 space-y-10">
            <div className="w-full h-fit flex flex-row justify-between items-center mb-4">
                <h1 className="font-bold text-2xl">Quản lý nhân sự</h1>
                <DatePicker prefix="Today" defaultValue={today} format={dateFormat} style={{width: '250px'}}/>
            </div>
            <div className="grid grid-cols-4 gap-4">
                <div className="rounded-lg shadow-md p-4 space-y-6">
                    <p className="font-bold text-gray-400">Tổng số tài khoản</p>
                    <div className="flex flex-col h-full space-y-2">
                    <div className="flex flex-row items-center justify-between w-full">
                        <p className="font-bold text-lg">$2,840,000</p>
                        <Tag color="green" bordered={false}>
                        <RiseOutlined />+20%
                        </Tag>
                    </div>
                    <p className="text-xs text-gray-400">
                        Your income increased <span className="text-green-500 font-bold">$580,000</span> by this month
                    </p>
                    </div>
                </div>

                <div className="rounded-lg shadow-md p-4 space-y-6">
                    <p className="font-bold text-gray-400">Tổng số hồ sơ bệnh án</p>
                    <div className="flex flex-col h-full space-y-2">
                    <div className="flex flex-row items-center justify-between w-full">
                        <p className="font-bold text-lg">
                        <TeamOutlined style={{ fontWeight: 'bold' }} />5,000
                        </p>
                        <Tag color="green" bordered={false}>
                        <RiseOutlined />+50%
                        </Tag>
                    </div>
                    <p className="text-xs text-gray-400">
                        Your customer increased <span className="text-green-500 font-bold">
                        <TeamOutlined style={{ fontWeight: 'bold' }} />2270
                        </span> by this month
                    </p>
                    </div>
                </div>

                <div className="rounded-lg shadow-md p-4 space-y-6">
                    <p className="font-bold text-gray-400">Số người dùng mới</p>
                    <div className="flex flex-col h-full space-y-2">
                    <div className="flex flex-row items-center justify-between w-full">
                        <p className="font-bold text-lg">7999</p>
                        <Tag color="green" bordered={false}>
                        <RiseOutlined />+1%
                        </Tag>
                    </div>
                    <p className="text-xs text-gray-400">
                        Your customer increased <span className="text-green-500 font-bold">120</span> by this month
                    </p>
                    </div>
                </div>

                <div className="rounded-lg shadow-md p-4 space-y-6">
                    <p className="font-bold text-gray-400">Số hồ sơ bệnh án mới</p>
                    <div className="flex flex-col h-full space-y-2">
                    <div className="flex flex-row items-center justify-between w-full">
                        <p className="font-bold text-lg">20</p>
                        <Tag color="red" bordered={false}>
                        <FallOutlined />-15%
                        </Tag>
                    </div>
                    <p className="text-xs text-gray-400">
                        Your customer decreased <span className="text-red-500 font-bold">94</span> by this month
                    </p>
                    </div>
                </div>
            </div>
            <div className='w-full h-fit '>
                <EmployeeAccounts/>
            </div>
        </div>
    )
}

export default ManageEmployee;
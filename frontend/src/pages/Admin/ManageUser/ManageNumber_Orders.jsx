import React from "react";
import {Tag, DatePicker  } from 'antd';
import {RiseOutlined} from '@ant-design/icons';
import dayjs from 'dayjs';
import ChartNumber_Orders from "../../../components/AdminComponents/ManageUser/ChartNumber_Orders";
import NumberOrders from "../../../components/AdminComponents/ManageUser/NumberOrders";
const ManageNumber_Orders = () => {
    const today = dayjs()
    const dateFormat = 'DD/MM/YYYY';
    return (
        <div className="w-full h-full py-8 px-4 text-black bg-gray-50 space-y-10">
            <div className="w-full h-fit flex flex-row justify-between items-center mb-4">
                <h1 className="font-bold text-2xl">Quản lý số phiếu khám</h1>
                <DatePicker prefix="Today" defaultValue={today} format={dateFormat} style={{width: '250px'}}/>
            </div>
            <div className="grid grid-cols-4 gap-4">
                <div className="rounded-lg shadow-md p-4 space-y-6">
                    <p className="font-bold text-gray-400">Tổng số hồ sơ bệnh án</p>
                    <div className="flex flex-col h-full space-y-2">
                    <div className="flex flex-row items-center justify-between w-full">
                        <p className="font-bold text-lg">84000</p>
                        <Tag color="green" bordered={false}>
                        <RiseOutlined />+20%
                        </Tag>
                    </div>
                    <p className="text-xs text-gray-400">
                        Your medical records increased <span className="text-green-500 font-bold">58,000</span> by this month
                    </p>
                    </div>
                </div>

                <div className="rounded-lg shadow-md p-4 space-y-6">
                    <p className="font-bold text-gray-400">Số phiếu khám bệnh hôm nay</p>
                    <div className="flex flex-col h-full space-y-2">
                    <div className="flex flex-row items-center justify-between w-full">
                        <p className="font-bold text-lg">10000</p>
                        <Tag color="green" bordered={false}>
                        <RiseOutlined />+10%
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
                        <p className="font-bold text-lg">5800</p>
                        <Tag color="green" bordered={false}>
                        <RiseOutlined />+15%
                        </Tag>
                    </div>
                    <p className="text-xs text-gray-400">
                        Your customer increased <span className="text-green-500 font-bold">94</span> by this month
                    </p>
                    </div>
                </div>
            </div>
            <div className="w-full h-fit">
                <NumberOrders />
            </div>
            <div className="w-full h-fit bg-white rounded-lg shadow-md">
                <ChartNumber_Orders/>
            </div>
        </div>
    );
}
export default ManageNumber_Orders;
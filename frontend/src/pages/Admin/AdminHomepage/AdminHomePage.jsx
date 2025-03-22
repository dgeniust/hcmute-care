import React, {useState} from "react";
import {Tag, Select, DatePicker  } from 'antd';
import {RiseOutlined, TeamOutlined, FallOutlined, RightOutlined} from '@ant-design/icons';
import LineChart from "../../../components/AdminComponents/AdminHomepage/LineChart";
import DonutChart from "../../../components/AdminComponents/AdminHomepage/DonutChart";
import TransactionTable from "../../../components/AdminComponents/AdminHomepage/TransactionTable";
import HorizontalChart from "../../../components/AdminComponents/AdminHomepage/HorizontalChart";
import dayjs from 'dayjs';
const AdminHomePage = () => {
    const dateFormat = 'DD/MM/YYYY';
    const today = dayjs(); // Lấy ngày hiện tại

    const [typeChart, setTypeChart] = useState('week')
    const salesDataWeek = [
        24000, 5000, 4000, 5000, 3000, 3450, 15000,
      ];
    const salesLabelsWeek = [
        'Thứ 2', 'Thứ 3', 'Thứ 4', 'Thứ 5', 'Thứ 6', 'Thứ 7', 'Chủ nhật',
    ];
    const salesDataYear = [
        100000, 150000, 220000, 200000, 180000, 194560, 160000, 140000, 130000, 120000, 150000, 180000,
    ];
    const salesLabelsYear = [
        'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec', 'Jan',
    ];
    const salesDonutData = [15.20, 28.3, 14.16, 30, 8, 4.34];
    const salesDonutLabels = ['Cồn 70º', 'Băng gạc', 'Nước rửa tay', 'Khẩu trang', 'Povidone iod', 'Oxy Già'];
    const handleChange = (value) => {
        setTypeChart(value);
    };
    const specialtyData = [3450, 2548, 2203, 1850, 500, 100];
    const specialtyLabels = [
        'Nội khoa',
        'Ngoại khoa',
        'Nhi khoa',
        'Sản khoa',
        'Da liễu',
        'Mắt',
    ];
    const [isModalOpen, setIsModalOpen] = useState(false);
    const showModal = () => {
        setIsModalOpen(true);
    };
    return (
        <div className="w-full h-full py-8 px-4 text-black bg-gray-50 space-y-4">
            <div className="w-full h-fit flex flex-row justify-between items-center mb-4">
                <h1 className="font-bold text-2xl">Bảng điều khiển</h1>
                <DatePicker prefix="Today" defaultValue={today} format={dateFormat} style={{width: '250px'}}/>

            </div>
            <div className="grid grid-cols-4 gap-4">
                <div className="rounded-lg shadow-md p-4 space-y-6">
                    <p className="font-bold text-gray-400">Doanh thu thường ngày</p>
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
                    <p className="font-bold text-gray-400">Người dùng mới</p>
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
                    <p className="font-bold text-gray-400">Số phiếu hôm nay</p>
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
                    <p className="font-bold text-gray-400">Số vật dụng trả lại</p>
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
            <div className="grid grid-cols-3 gap-2">
            <div className="col-span-2 rounded-lg shadow-md p-4 bg-white">
                <div className="flex flex-row items-center justify-between mb-4">
                <h1 className="font-bold text-base">Doanh thu</h1>
                <Select
                    defaultValue="week"
                    style={{ width: 120 }}
                    onChange={handleChange}
                    options={[
                    { value: 'week', label: '1 Tuần' },
                    { value: 'year', label: '12 Tháng' },
                    ]}
                />
                </div>
                <div className="h-[300px]">
                <LineChart
                    data={typeChart === 'week' ? salesDataWeek : salesDataYear}
                    labels={typeChart === 'week' ? salesLabelsWeek : salesLabelsYear}
                />
                </div>
            </div>

            <div className="col-span-1 rounded-lg shadow-md p-4 bg-white">
                <div className="mb-4">
                <h2 className="font-bold text-base">Sản phẩm y tế bán chạy</h2>
                </div>
                <div className="h-[305px]">
                <DonutChart data={salesDonutData} labels={salesDonutLabels} />
                </div>
            </div>

            <div className="col-span-2 rounded-lg shadow-md p-4 bg-white">
                <div className="flex flex-row items-center justify-between mb-4">
                <h1 className="font-bold text-base">Giao dịch</h1>
                <div className="text-blue-500 items-center flex flex-row space-x-2 cursor-pointer" onClick={showModal}>
                    <p>Xem hết</p>
                    <RightOutlined />
                </div>
                </div>
                <div className="relative h-full">
                <TransactionTable className="absolute" isModalOpen={isModalOpen}
                setIsModalOpen={setIsModalOpen}/>
                </div>
            </div>

            <div className="col-span-1 rounded-lg shadow-md p-4 bg-white">
                <h2 className="text-lg font-semibold mb-4">Lượt Khám Theo Chuyên Khoa</h2>
                <div style={{ height: '235px' }}>
                    <HorizontalChart data={specialtyData} labels={specialtyLabels} />
                </div>
            </div>
            </div>
        </div>
    )
}

export default AdminHomePage;
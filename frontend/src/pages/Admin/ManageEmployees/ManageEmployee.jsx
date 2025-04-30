import React, {useState, useEffect} from 'react';
import dayjs from 'dayjs';
import {Tag, DatePicker, message} from 'antd';
import {RiseOutlined, TeamOutlined, FallOutlined} from '@ant-design/icons';
import EmployeeAccounts from './EmployeeAccounts';
import { handleHttpStatusCode, notifyErrorWithCustomMessage, notifySuccessWithCustomMessage } from "../../../utils/notificationHelper";

const ManageEmployee =() => {
    const today = dayjs()
    const dateFormat = 'DD/MM/YYYY';
    const [messageApi, contextHolder] = message.useMessage();
    const [totalDoctors, setTotalDoctors] = useState(0);
    const [totalNurses, setTotalNurses] = useState(0);
    const [totalStaffs, setTotalStaffs] = useState(0);
    useEffect(() => {
        const fetchRevenue = async () => {
          try {
            const accessToken = localStorage.getItem("accessToken");
            const response = await fetch("http://localhost:8080/api/v1/revenue/employees", {
              method: "GET",
              headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${accessToken}`, // Added token for consistency
              },
            });
    
            if (!response.ok) {
              const errorText = await response.text();
              notifyErrorWithCustomMessage(`Lỗi khi lấy thống kê nhân viên: ${errorText || response.statusText}`, messageApi);
              return;
            }
    
            const data = await response.json();
            console.log("API response revenue:", data);
    
            // Handle nested response structure
            const statsData = data.data || data;
            setTotalDoctors(statsData.totalDoctor || 0);
            setTotalNurses(statsData.totalNurse || 0);
            setTotalStaffs(statsData.totalStaff || 0);
    
            
            handleHttpStatusCode(response.status, "Thống kê nhân viên thành công", "", messageApi);
          } catch (e) {
            notifyErrorWithCustomMessage("Lỗi kết nối khi lấy thống kê người dùng", messageApi);
            console.error("Error fetching users:", e);
          }
        };
    
        fetchRevenue();
      }, []);
    return (
        <div className="w-full h-full py-8 px-4 text-black bg-gray-50 space-y-10">
            <div className="w-full h-fit flex flex-row justify-between items-center mb-4">
                <h1 className="font-bold text-2xl">Quản lý nhân sự</h1>
                <DatePicker prefix="Today" defaultValue={today} format={dateFormat} style={{width: '250px'}}/>
            </div>
            <div className="grid grid-cols-4 gap-4">
                <div className="rounded-lg shadow-md p-4 space-y-6">
                    <p className="font-bold text-gray-400">Tổng số bác sĩ</p>
                    <div className="flex flex-col h-full space-y-2">
                    <div className="flex flex-row items-center justify-between w-full">
                        <p className="font-bold text-lg">{totalDoctors.toLocaleString()}</p>
                    </div>
                    </div>
                </div>

                <div className="rounded-lg shadow-md p-4 space-y-6">
                    <p className="font-bold text-gray-400">Tổng số y tá</p>
                    <div className="flex flex-col h-full space-y-2">
                    <div className="flex flex-row items-center justify-between w-full">
                        <p className="font-bold text-lg">{totalNurses.toLocaleString()}</p>
                    </div>
                    </div>
                </div>

                <div className="rounded-lg shadow-md p-4 space-y-6">
                    <p className="font-bold text-gray-400">Tổng số nhân viên (Staff)</p>
                    <div className="flex flex-col h-full space-y-2">
                    <div className="flex flex-row items-center justify-between w-full">
                        <p className="font-bold text-lg">{totalStaffs.toLocaleString()}</p>
                    </div>
                    </div>
                </div>
                <div className="rounded-lg shadow-md p-4 space-y-6">
                    <p className="font-bold text-gray-400">Tổng số admin</p>
                    <div className="flex flex-col h-full space-y-2">
                    <div className="flex flex-row items-center justify-between w-full">
                        <p className="font-bold text-lg">4</p>
                    </div>
                    </div>
                </div>
            </div>
            <div className='w-full h-fit '>
                <EmployeeAccounts/>
            </div>
            {contextHolder}
        </div>
    )
}

export default ManageEmployee;
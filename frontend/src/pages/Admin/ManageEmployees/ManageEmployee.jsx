import React, { useState, useEffect } from "react";
import dayjs from "dayjs";
import { Tag, DatePicker, message } from "antd";
import { TeamOutlined } from "@ant-design/icons";
import EmployeeAccounts from "./EmployeeAccounts";
import {
  handleHttpStatusCode,
  notifyErrorWithCustomMessage,
  notifySuccessWithCustomMessage,
} from "../../../utils/notificationHelper";

const ManageEmployee = () => {
  const today = dayjs();
  const dateFormat = "DD/MM/YYYY";
  const apiUrl = import.meta.env.VITE_API_BASE_URL;
  const [messageApi, contextHolder] = message.useMessage();
  const [totalDoctors, setTotalDoctors] = useState(0);
  const [totalNurses, setTotalNurses] = useState(0);
  const [totalStaffs, setTotalStaffs] = useState(0);
  const [totalAdmins, setTotalAdmins] = useState(0);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    fetchEmployeeStats();
  }, []);

  const fetchEmployeeStats = async () => {
    setLoading(true);
    try {
      const accessToken = localStorage.getItem("accessToken");
      const response = await fetch(`${apiUrl}v1/revenue/employees`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${accessToken}`,
        },
      });

      if (!response.ok) {
        const errorText = await response.text();
        notifyErrorWithCustomMessage(
          `Lỗi khi lấy thống kê nhân viên: ${errorText || response.statusText}`,
          messageApi
        );
        return;
      }

      const data = await response.json();
      
      // Handle nested response structure
      const statsData = data.data || data;
      setTotalDoctors(statsData.totalDoctor || 0);
      setTotalNurses(statsData.totalNurse || 0);
      setTotalStaffs(statsData.totalStaff || 0);
      setTotalAdmins(statsData.totalAdmin || 4);

      handleHttpStatusCode(
        response.status,
        "Thống kê nhân viên thành công",
        "",
        messageApi
      );
    } catch (e) {
      notifyErrorWithCustomMessage(
        "Lỗi kết nối khi lấy thống kê người dùng",
        messageApi
      );
      console.error("Error fetching users:", e);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="w-full h-full py-8 px-4 text-black bg-gray-50 space-y-10">
      {contextHolder}
      
      <div className="flex justify-between items-center">
        <h1 className="text-2xl font-bold flex items-center">
          <TeamOutlined className="mr-2 text-blue-500" /> Quản lý nhân sự
        </h1>
        <DatePicker
          prefix="Today"
          defaultValue={today}
          format={dateFormat}
          className="w-48"
        />
      </div>
      
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
        <StatsCard 
          title="Tổng số bác sĩ" 
          value={totalDoctors} 
          loading={loading}
          color="blue"
        />
        <StatsCard 
          title="Tổng số y tá" 
          value={totalNurses} 
          loading={loading}
          color="pink"
        />
        <StatsCard 
          title="Tổng số nhân viên" 
          value={totalStaffs} 
          loading={loading}
          color="green"
        />
        <StatsCard 
          title="Tổng số admin" 
          value={totalAdmins} 
          loading={loading}
          color="purple"
        />
      </div>
      
      <div className="w-full">
        <EmployeeAccounts />
      </div>
    </div>
  );
};

const StatsCard = ({ title, value, loading, color }) => {
  const colorMap = {
    blue: "bg-blue-500",
    green: "bg-green-500",
    pink: "bg-pink-500",
    purple: "bg-purple-500"
  };
  
  return (
    <div className="bg-white rounded-lg shadow-md p-6 transition-all hover:shadow-lg">
      <div className="flex justify-between items-center mb-4">
        <h3 className="text-gray-500 font-medium">{title}</h3>
        <div className={`w-10 h-10 rounded-full ${colorMap[color]} flex items-center justify-center`}>
          <TeamOutlined className="text-white text-lg" />
        </div>
      </div>
      <div className="flex items-end">
        <span className="text-3xl font-bold">
          {loading ? "..." : value.toLocaleString()}
        </span>
      </div>
    </div>
  );
};

export default ManageEmployee;
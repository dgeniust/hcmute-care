import React, { useState, useEffect } from "react";
import { Tag, DatePicker, message } from "antd";
import { TeamOutlined } from "@ant-design/icons";
import dayjs from "dayjs";
import UserAccounts from "../../../components/AdminComponents/ManageUser/UserAccounts";
import { handleHttpStatusCode, notifyErrorWithCustomMessage, notifySuccessWithCustomMessage } from "../../../utils/notificationHelper";

const ManageUser = () => {
  const today = dayjs();
  const dateFormat = "DD/MM/YYYY";
  const [messageApi, contextHolder] = message.useMessage();
  const [totalUsers, setTotalUsers] = useState(0);
  const [totalMedicalRecords, setTotalMedicalRecords] = useState(0);

  useEffect(() => {
    const fetchRevenue = async () => {
      try {
        const accessToken = localStorage.getItem("accessToken");
        const response = await fetch("http://localhost:8080/api/v1/revenue/customers", {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${accessToken}`, // Added token for consistency
          },
        });

        if (!response.ok) {
          const errorText = await response.text();
          notifyErrorWithCustomMessage(`Lỗi khi lấy thống kê người dùng: ${errorText || response.statusText}`, messageApi);
          return;
        }

        const data = await response.json();
        console.log("API response revenue:", data);

        // Handle nested response structure
        const statsData = data.data || data;
        setTotalUsers(statsData.totalAccounts || 0);
        setTotalMedicalRecords(statsData.totalMedicalRecords || 0);

        // Pass a string message to handleHttpStatusCode instead of the raw data object
        const successMessage = `Tổng số tài khoản: ${statsData.totalAccounts || 0}, Tổng số hồ sơ bệnh án: ${
          statsData.totalMedicalRecords || 0
        }`;
        handleHttpStatusCode(response.status, successMessage, "Thống kê người dùng", messageApi);
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
        <h1 className="font-bold text-2xl">Quản lý người dùng</h1>
        <DatePicker prefix="Today" defaultValue={today} format={dateFormat} style={{ width: "250px" }} />
      </div>
        <div className="grid grid-cols-4 gap-4">
          <div className="rounded-lg shadow-md p-4 space-y-6">
            <p className="font-bold text-gray-400">Tổng số tài khoản</p>
            <div className="flex flex-col h-full space-y-2">
              <div className="flex flex-row items-center justify-between w-full">
                <p className="font-bold text-lg">{totalUsers.toLocaleString()}</p>
              </div>
            </div>
          </div>
          <div className="rounded-lg shadow-md p-4 space-y-6">
            <p className="font-bold text-gray-400">Tổng số hồ sơ bệnh án</p>
            <div className="flex flex-col h-full space-y-2">
              <div className="flex flex-row items-center justify-between w-full">
                <p className="font-bold text-lg">
                  <TeamOutlined style={{ fontWeight: "bold", marginRight: "4px" }} />
                  {totalMedicalRecords.toLocaleString()}
                </p>
              </div>
            </div>
          </div>
        </div>
      <div className="w-full h-fit">
        <UserAccounts />
      </div>
      {contextHolder}
    </div>
  );
};

export default ManageUser;
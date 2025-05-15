import React, { useState, useEffect } from "react";
import { Tag, DatePicker } from "antd";
import { RiseOutlined } from "@ant-design/icons";
import dayjs from "dayjs";
import ChartNumber_Orders from "../../../components/AdminComponents/ManageUser/ChartNumber_Orders";
import NumberOrders from "../../../components/AdminComponents/ManageUser/NumberOrders";

const ManageNumber_Orders = () => {
  const apiUrl = import.meta.env.VITE_API_BASE_URL;

  const [tickets, setTickets] = useState([]);
  const [loading, setLoading] = useState(true);
  const today = dayjs();
  const dateFormat = "YYYY-MM-DD";

  useEffect(() => {
    const fetchTickets = async () => {
      try {
        const response = await fetch(
          `${apiUrl}v1/tickets?page=1&size=20&sort=scheduleSlot.schedule.date&direction=asc`,
          {
            method: "GET",
            headers: {
              "Content-Type": "application/json",
            },
          }
        );
        if (!response.ok) {
          const errorText = await response.text();
          throw new Error(errorText);
        }
        const data = await response.json();
        if (data && data.data.content.length > 0) {
          setTickets(data.data.content);
        }
        setLoading(false);
      } catch (error) {
        console.error("Error fetching tickets:", error);
        setLoading(false);
      }
    };
    fetchTickets();
  }, []);

  // Calculate metrics
  const totalMedicalRecords = tickets.length;
  const todayTickets = tickets.filter((ticket) =>
    dayjs(ticket.schedule.date).isSame(today, "day")
  ).length;
  const newMedicalRecords = tickets.filter(
    (ticket) => ticket.status === "CONFIRMED"
  ).length;

  return (
    <div className="w-full h-full py-8 px-4 text-black bg-gray-50 space-y-10">
      <div className="w-full h-fit flex flex-row justify-between items-center mb-4">
        <h1 className="font-bold text-2xl">Quản lý số phiếu khám</h1>
        <DatePicker
          prefix="Today"
          defaultValue={today}
          format={dateFormat}
          style={{ width: "250px" }}
        />
      </div>
      <div className="grid grid-cols-4 gap-4">
        <div className="rounded-lg shadow-md p-4 space-y-6">
          <p className="font-bold text-gray-400">Tổng số hồ sơ bệnh án</p>
          <div className="flex flex-col h-full space-y-2">
            <div className="flex flex-row items-center justify-between w-full">
              <p className="font-bold text-lg">{totalMedicalRecords}</p>
              <Tag color="green" bordered={false}>
                <RiseOutlined /> +20%
              </Tag>
            </div>
            <p className="text-xs text-gray-400">
              Your medical records increased{" "}
              <span className="text-green-500 font-bold">
                {totalMedicalRecords}
              </span>{" "}
              by this month
            </p>
          </div>
        </div>

        <div className="rounded-lg shadow-md p-4 space-y-6">
          <p className="font-bold text-gray-400">Số phiếu khám bệnh hôm nay</p>
          <div className="flex flex-col h-full space-y-2">
            <div className="flex flex-row items-center justify-between w-full">
              <p className="font-bold text-lg">{todayTickets}</p>
              <Tag color="green" bordered={false}>
                <RiseOutlined /> +10%
              </Tag>
            </div>
            <p className="text-xs text-gray-400">
              Your customer increased{" "}
              <span className="text-green-500 font-bold">{todayTickets}</span>{" "}
              by this month
            </p>
          </div>
        </div>

        <div className="rounded-lg shadow-md p-4 space-y-6">
          <p className="font-bold text-gray-400">Số hồ sơ bệnh án mới</p>
          <div className="flex flex-col h-full space-y-2">
            <div className="flex flex-row items-center justify-between w-full">
              <p className="font-bold text-lg">{newMedicalRecords}</p>
              <Tag color="green" bordered={false}>
                <RiseOutlined /> +15%
              </Tag>
            </div>
            <p className="text-xs text-gray-400">
              Your customer increased{" "}
              <span className="text-green-500 font-bold">
                {newMedicalRecords}
              </span>{" "}
              by this month
            </p>
          </div>
        </div>
      </div>
      <div className="w-full h-fit">
        <NumberOrders tickets={tickets} />
      </div>
      {/* <div className="w-full h-fit bg-white rounded-lg shadow-md">
        <ChartNumber_Orders tickets={tickets} />
      </div> */}
    </div>
  );
};

export default ManageNumber_Orders;

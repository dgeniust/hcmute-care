import React, { useState, useEffect } from "react";
import { Button, theme, Collapse, message } from "antd";
import {
  ForkOutlined,
  CaretRightOutlined,
  FileSearchOutlined,
} from "@ant-design/icons";
import {
  handleHttpStatusCode,
  notifyErrorWithCustomMessage,
} from "../../../utils/notificationHelper";

const TimeADoctor_Booking = ({ handleSlotClick }) => {
  const apiUrl = import.meta.env.VITE_API_BASE_URL;
  const specialtyId = localStorage.getItem("specialtyId");
  const dateBooking = localStorage.getItem("dateBooking");
  const [schedule, setSchedule] = useState([]);
  const [messageApi, contextHolder] = message.useMessage();

  useEffect(() => {
    const handleDataTimeADoctor = async () => {
      try {
        const response = await fetch(
          `${apiUrl}v1/schedules/available?medicalSpecialtyId=${specialtyId}&date=${dateBooking}`,
          {
            method: "GET",
            headers: {
              "Content-Type": "application/json",
              Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
            },
          }
        );
        if (!response.ok) {
          const errorText = await response.text();
          handleHttpStatusCode(
            response.status,
            "",
            `Lấy lịch khám thất bại: ${errorText || response.statusText}`,
            messageApi
          );
          return;
        }
        const data = await response.json();
        console.log("Raw data time and doctor:", data);
        if (data && data.data.length > 0) {
          const scheduleData = data.data.map((item) => ({
            id: item.id,
            doctor: item.doctor,
            roomDetail: item.roomDetail,
            timeSlots: item.scheduleSlots.map((slot) => ({
              id: slot.id,
              timeString: `${slot.timeSlot.startTime.slice(
                0,
                5
              )} - ${slot.timeSlot.endTime.slice(0, 5)}`,
              bookedSlots: slot.bookedSlots,
              startTime: slot.timeSlot.startTime, // Keep startTime for comparison
              originalSlot: slot,
            })).sort((a, b) => a.id - b.id),
          }));
          setSchedule(scheduleData);
          console.log("Data time and doctor:", scheduleData);
        }
      } catch (e) {
        console.error("Error in time and doctor:", e);
        notifyErrorWithCustomMessage(
          "Có lỗi xảy ra trong quá trình lấy lịch khám. Vui lòng thử lại sau.",
          messageApi
        );
      }
    };
    handleDataTimeADoctor();
  }, [specialtyId, dateBooking, messageApi]);

  const { token } = theme.useToken();

  const panelStyle = {
    marginBottom: 5,
    background: token.colorFillAlter,
    borderRadius: token.borderRadiusLG,
    border: "1px solid black",
  };

  // Function to check if a time slot is in the past
  const isTimeSlotPast = (startTime, bookingDate) => {
    const currentDate = new Date();
    const currentDateString = currentDate.toISOString().split("T")[0]; // e.g., "2025-05-14"
    const slotDateTime = new Date(`${bookingDate}T${startTime}`); // e.g., "2025-05-14T09:00"

    // If the booking date is today, check if the start time has passed
    if (bookingDate === currentDateString) {
      return slotDateTime < currentDate;
    }
    // If the booking date is in the past, disable all slots
    return new Date(bookingDate) < new Date(currentDateString);
  };

  const getItems = (panelStyle) => {
    return schedule.map((item, index) => ({
      key: index,
      label: (
        <div className="flex flex-col items-center w-full space-y-2">
          <div className="flex flex-row justify-between items-center w-full">
            <div className="space-x-4">
              <ForkOutlined
                style={{
                  border: "1px solid transparent",
                  borderRadius: "50%",
                  padding: "3px",
                  backgroundColor: "#dfe6e9",
                }}
              />
              <span>{item.doctor.fullName}</span>
            </div>
          </div>
          <div className="flex flex-row justify-between items-center w-full">
            <div className="space-x-4 text-sm font-normal">
              <FileSearchOutlined
                style={{
                  border: "1px solid transparent",
                  borderRadius: "50%",
                  padding: "3px",
                  backgroundColor: "#dfe6e9",
                }}
              />
              <span>{item.roomDetail.name}</span>
            </div>
          </div>
        </div>
      ),
      children: (
        <div className="w-full">
          {item.timeSlots.map((slot) => {
            const isPast = isTimeSlotPast(slot.startTime, dateBooking);
            return (
              <Button
                key={slot.id}
                onClick={() => {
                  handleSlotClick(
                    slot.timeString,
                    item.doctor.fullName,
                    item.roomDetail.name,
                    slot.id
                  );
                }}
                //disabled={slot.bookedSlots > 6 || isPast} // Disable if booked or past
                style={{
                  padding: "10px 20px",
                  marginRight: "4px",
                  borderRadius: "8px",
                  border: "1px solid #273c75",
                  backgroundColor:
                    slot.bookedSlots > 6 || isPast ? "#f5f5f5" : "white",
                  cursor:
                    slot.bookedSlots > 6 || isPast ? "not-allowed" : "pointer",
                  color: slot.bookedSlots > 6 || isPast ? "#999" : "#273c75",
                }}
              >
                {slot.timeString}
              </Button>
            );
          })}
        </div>
      ),
      style: panelStyle,
    }));
  };

  return (
    <div className="w-3/4 h-fit flex flex-col">
      <div className="flex w-full justify-center">
        <h1 className="text-black font-bold text-base">Chọn giờ khám</h1>
      </div>
      <div className="w-full max-h-[460px] h-fit flex flex-col border rounded-xl overflow-y-auto space-y-2">
        <Collapse
          bordered={false}
          defaultActiveKey={["0"]}
          expandIcon={({ isActive }) => (
            <CaretRightOutlined rotate={isActive ? 90 : 0} />
          )}
          expandIconPosition="end"
          style={{
            background: token.colorBgContainer,
            overflow: "auto",
          }}
          items={getItems(panelStyle)}
        />
      </div>
      {contextHolder}
    </div>
  );
};

export default TimeADoctor_Booking;
import React, { useState, useEffect, useRef } from "react";
import { Button, Collapse, theme, message } from "antd";
import {
  ArrowLeftOutlined,
  UserAddOutlined,
  CaretRightOutlined,
  RestOutlined,
  ForkOutlined,
} from "@ant-design/icons";
import {
  handleHttpStatusCode,
  notifyErrorWithCustomMessage,
  notifySuccessWithCustomMessage,
} from "../../../utils/notificationHelper";

const ConfirmInfo_Booking = ({ bookingList, setBookingList, setCurrent }) => {
  const apiUrl = import.meta.env.VITE_API_BASE_URL;
  const { token } = theme.useToken();
  const ref1 = useRef(null);
  const [messageApi, contextHolder] = message.useMessage();
  const medicalRecordId = localStorage.getItem("medicalRecordId");
  const [medicalRecordData, setMedicalRecordData] = useState([]);

  useEffect(() => {
    const handleMedicalRecord = async () => {
      try {
        const response = await fetch(
          `${apiUrl}v1/medical-records/${medicalRecordId}`,
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
            `Lấy hồ sơ bệnh án thất bại: ${errorText || response.statusText}`,
            messageApi
          );
          return;
        }
        const data = await response.json();
        console.log("Medical Records:", data);
        if (data && data.data) {
          const medicalRecordData = {
            id: data.data.id,
            patient: data.data.patient,
            barcode: data.data.barcode,
          };
          setMedicalRecordData([medicalRecordData]);
          console.log("Medical Records Data:", medicalRecordData);
        }
      } catch (error) {
        console.error("Error fetching medical records:", error);
      }
    };
    handleMedicalRecord();
  }, []);

  const resetBooking = () => {
    localStorage.removeItem("dateBooking");
    localStorage.removeItem("specialtyId");
    setCurrent(1); // Navigate back to CureInfo_Booking
  };

  const panelStyle = {
    marginBottom: 5,
    background: token.colorFillAlter,
    borderRadius: token.borderRadiusLG,
    border: "none",
  };

  const getItems = (panelStyle) => [
    {
      key: "1",
      label: (
        <div className="flex flex-col items-center w-full space-y-2">
          <div className="flex flex-row justify-between items-center w-full">
            <div className="space-x-4">
              <UserAddOutlined />
              <span>Hồ sơ đăng ký khám bệnh</span>
            </div>
          </div>
        </div>
      ),
      children: (
        <div className="w-full h-fit">
          {medicalRecordData &&
            medicalRecordData.map((item) => (
              <div
                className="grid grid-flow-row grid-cols-2 gap-4"
                key={item.patient.id}
              >
                <div className="grid grid-flow-row grid-cols-[80px_1fr]">
                  <p>Họ tên:</p>
                  <p className="text-[#273c75] font-bold">
                    {item.patient.name}
                  </p>
                </div>
                <div className="grid grid-flow-row grid-cols-[80px_1fr]">
                  <p>Giới tính:</p>
                  <p>{item.patient.gender === "MALE" ? "Nam" : "Nữ"}</p>
                </div>
                <div className="grid grid-flow-row grid-cols-[80px_1fr]">
                  <p>Điện thoại:</p>
                  <p>{item.patient.phone}</p>
                </div>
                <div className="grid grid-flow-row grid-cols-[80px_1fr]">
                  <p>Địa chỉ:</p>
                  <p>{item.patient.address}</p>
                </div>
              </div>
            ))}
        </div>
      ),
      style: panelStyle,
    },
  ];

  const deleteBooking = (index) => {
    const updatedBookingList = bookingList.filter((_, i) => i !== index);
    setBookingList(updatedBookingList);
    // Cập nhật bookings trong localStorage
    let bookings = JSON.parse(localStorage.getItem("bookings") || "[]");
    bookings = bookings.filter((_, i) => i !== index);
    localStorage.setItem("bookings", JSON.stringify(bookings));
  };

  const handleNextStep = async () => {
    try {
      const scheduleSlotIds = bookingList.map(
        (booking) => booking.scheduleSlotId
      );
      if (!scheduleSlotIds || scheduleSlotIds.length === 0) {
        notifyErrorWithCustomMessage(
          "Không có lịch khám nào được chọn.",
          messageApi
        );
        return;
      }
      const payload = {
        medicalRecordId: medicalRecordId,
        scheduleSlotIds: scheduleSlotIds,
      };
      console.log("Payload in appointment", payload);
      const response = await fetch(`${apiUrl}v1/appointments`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
        },
        body: JSON.stringify(payload),
      });
      console.log(
        "Response in appointment -------------------------:",
        response
      );
      if (!response.ok) {
        const errorText = await response.text();
        handleHttpStatusCode(
          response.status,
          "",
          `Đặt khám thất bại: ${errorText || response.statusText}`,
          messageApi
        );
        return;
      }
      const data = await response.json();
      console.log("Appointment Data:", data);
      notifySuccessWithCustomMessage("Đặt khám thành công", messageApi);
      if (data) {
        const appointmentId = data.data.id;
        localStorage.setItem("appointmentId", appointmentId);
      }
      setCurrent(4); // Navigate to Payment_Booking
    } catch (e) {
      console.error("Error:", e);
      notifyErrorWithCustomMessage(
        "Có lỗi xảy ra trong quá trình xử lý yêu cầu.",
        messageApi
      );
    }
    setCurrent(3);
    localStorage.removeItem("bookings");
    localStorage.removeItem("dateBooking");
    localStorage.removeItem("specialtyId");
  };

  return (
    <div className="w-full h-fit min-h-[460px] border border-red-600 p-8">
      <div className="flex flex-row gap-4 w-full h-full items-center">
        <Button
          icon={<ArrowLeftOutlined />}
          style={{
            backgroundColor: "transparent",
            border: "none",
            boxShadow: "none",
          }}
        ></Button>
        <h1 className="text-black font-bold text-lg">Thông tin đặt khám</h1>
      </div>
      <div className="flex flex-col space-y-4" ref={ref1}>
        <p className="text-black">
          Vui lòng kiểm tra thông tin đặt khám bên dưới. Hoặc "Thêm chuyên khoa"
          mới
        </p>
        <div className="w-full max-h-[460px] h-fit flex flex-col border border-red-600 rounded-xl overflow-y-auto space-y-2">
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
        <h1 className="text-black text-lg">
          Chuyên khoa đã đặt ({bookingList.length})
        </h1>
        {bookingList.map((item, index) => (
          <div
            className="w-full h-fit flex flex-row justify-between items-center text-black rounded-xl shadow-lg"
            key={index}
          >
            <div className="w-[95%] h-fit grid grid-cols-2 grid-flow-row gap-3 text-black p-4">
              <div className="grid grid-flow-row grid-cols-[150px_1fr]">
                <p>Chuyên khoa:</p>
                <p className="text-[#273c75] font-bold">{item.specialty}</p>
              </div>
              <div className="grid grid-flow-row grid-cols-[150px_1fr]">
                <p>Phí khám:</p>
                <p className="text-[#273c75] font-bold">{item.price}</p>
              </div>
              <div className="grid grid-flow-row grid-cols-[150px_1fr]">
                <p>Ngày khám:</p>
                <p className="text-[#273c75] font-bold">{item.date}</p>
              </div>
              <div className="grid grid-flow-row grid-cols-[150px_1fr]">
                <p>Phòng - Giờ khám:</p>
                <p className="text-[#273c75] font-bold">
                  {item.room}, {item.time}
                </p>
              </div>
              <div className="grid grid-flow-row grid-cols-[150px_1fr]">
                <p>Bác sĩ:</p>
                <p className="text-[#273c75] font-bold">{item.doctor}</p>
              </div>
              <div className="grid grid-flow-row grid-cols-[150px_1fr]">
                <p>BHYT:</p>
                <p className="text-[#273c75] font-bold">{item.insurance}</p>
              </div>
              <div className="grid grid-flow-row grid-cols-[150px_1fr]">
                <p>BLVP:</p>
                <p className="text-[#273c75] font-bold">{item.guarantee}</p>
              </div>
            </div>
            <div className="w-[5%]">
              <RestOutlined
                style={{
                  color: "red",
                  border: "1px solid red",
                  borderRadius: "50%",
                  width: "20px",
                  height: "20px",
                  display: "flex",
                  justifyContent: "center",
                  backgroundColor: "transparent",
                  cursor: "pointer",
                }}
                onClick={() => deleteBooking(index)}
              />
            </div>
          </div>
        ))}
        <div className="flex flex-row justify-center items-center w-full h-fit space-x-4">
          <Button
            type="primary"
            className="w-full h-fit mt-4"
            icon={<ForkOutlined />}
            style={{
              width: "300px",
              height: "40px",
              fontSize: "15px",
              fontWeight: "bold",
              backgroundColor: "white",
              color: "blue",
              border: "1px solid blue",
            }}
            onClick={resetBooking}
          >
            Thêm chuyên khoa
          </Button>
          <Button
            type="primary"
            className="w-full h-fit mt-4"
            style={{
              width: "200px",
              height: "40px",
              fontSize: "15px",
              fontWeight: "bold",
              backgroundColor: "blue",
            }}
            onClick={handleNextStep}
          >
            Tiếp tục
          </Button>
        </div>
      </div>
      {contextHolder}
    </div>
  );
};

export default ConfirmInfo_Booking;

import React, { useState, useEffect } from "react";
import { message, Typography } from "antd";
import {
  notifyErrorWithCustomMessage,
  notifySuccessWithCustomMessage,
} from "../../../utils/notificationHelper";
const { Text } = Typography;

const ConfirmBill_Booking = () => {
  const [paymentData, setPaymentData] = useState(null); // Initialize as null for a single object
  const [messageApi, contextHolder] = message.useMessage();

  useEffect(() => {
    try {
      const storedData = localStorage.getItem("paymentData");
      if (storedData) {
        const parsedData = JSON.parse(storedData);
        setPaymentData(parsedData); // Set directly as a single object
        console.log("Parsed paymentData:", parsedData);
        notifySuccessWithCustomMessage(
          "Vui lòng kiểm tra thông tin đặt lịch khám của bạn tại đây!",
          messageApi
        );
      }
    } catch (error) {
      console.error("Error parsing paymentData from localStorage:", error);
      notifyErrorWithCustomMessage("Lỗi khi tải dữ liệu đặt lịch!", messageApi);
    }
  }, [messageApi]);

  return (
    <>
      {contextHolder}
      <div className="flex flex-col items-center justify-center min-h-screen my-6 space-y-6 mx-auto">
        {paymentData ? (
          // Handle single paymentData object
          (paymentData?.appointment?.tickets || []).map((ticket, ticketIndex) => (
            <div
              key={ticketIndex}
              className="w-fit h-fit min-h-[460px] border border-sky-600 px-8 py-4 space-y-8 rounded-lg flex flex-col m-auto mb-4"
            >
              <div className="flex flex-col text-black justify-center items-center space-y-4 text-center">
                <div className="flex flex-col">
                  <h1 className="font-bold text-base">
                    Bệnh viện đại học Y Dược TPHCM
                  </h1>
                  <span>215 Hồng Bàng, P.11, Q5, TPHCM</span>
                </div>
                <div className="flex flex-col">
                  <h1 className="text-xl text-[#273c75] font-bold">PHIẾU KHÁM BỆNH</h1>
                  <span>(Mã phiếu: {ticket.ticketCode || "Đang tải..."})</span>
                  <span>
                    (Mã giao dịch: {paymentData?.transactionId || "Đang tải..."})
                  </span>
                </div>
              </div>
              <div className="flex flex-col space-y-4 items-center justify-center text-center">
                <div className="flex flex-col space-y-1 text-black items-center justify-center text-center">
                  <h1 className="text-lg font-bold">TỔNG QUÁT</h1>
                  <p className="font-bold">
                    {ticket.schedule?.roomDetail?.name ||
                      "Phòng 33 - Khám Nội Lầu 1 Khu A"}
                  </p>
                  <div className="flex justify-center mb-4">
                    <div className="text-center p-4 bg-blue-50 border border-blue-100 rounded-xl">
                      <Text type="secondary">Số thứ tự</Text>
                      <div className="font-bold text-4xl text-blue-600">
                        {ticket.waitingNumber || "--"}
                      </div>
                    </div>
                  </div>
                </div>
                <div className="flex flex-row items-center space-x-20 justify-center w-full text-center text-black">
                  <div className="space-y-4">
                    <div className="flex flex-row space-x-4">
                      <h1>Họ tên:</h1>
                      <p className="font-bold">
                        {paymentData?.appointment?.medicalRecord?.patientName ||
                          "Đang tải..."}
                      </p>
                    </div>
                    <div className="flex flex-row space-x-4">
                      <h1>Ngày sinh:</h1>
                      <p className="font-bold">
                        {paymentData?.appointment?.medicalRecord?.dob || "--"}
                      </p>
                    </div>
                    <div className="flex flex-row space-x-4">
                      <h1>Giới tính:</h1>
                      <p className="font-bold">
                        {paymentData?.appointment?.medicalRecord?.gender === "MALE"
                          ? "Nam"
                          : paymentData?.appointment?.medicalRecord?.gender ===
                            "FEMALE"
                          ? "Nữ"
                          : "--"}
                      </p>
                    </div>
                  </div>
                  <div className="space-y-4">
                    <div className="flex flex-row space-x-4">
                      <h1>Giờ khám dự kiến:</h1>
                      <p className="font-bold">
                        {ticket.timeSlot?.startTime && ticket.timeSlot?.endTime
                          ? `${ticket.timeSlot?.startTime} - ${ticket.timeSlot?.endTime}`
                          : "-:00 - -:00"}
                      </p>
                    </div>
                    <div className="flex flex-row space-x-4 items-center">
                      <h1>Ngày khám:</h1>
                      <p className="font-bold text-[#009432] ">
                        {ticket.schedule?.date || "2025-05-17"}
                      </p>
                    </div>
                    <div className="flex flex-row space-x-4">
                      <h1>Đối tượng:</h1>
                      <p className="font-bold">Đã thu phí</p>
                    </div>
                  </div>
                </div>
              </div>
              <div className="text-black space-y-4 flex justify-center flex-col items-center text-center">
                <p>
                  Vui lòng đến trực tiếp phòng khám trước hẹn 15-30 phút để khám bệnh
                </p>
                <div>
                  <p>
                    Số hồ sơ (Mã bệnh nhân):{" "}
                    <span className="font-bold">
                      {paymentData?.appointment?.medicalRecord?.barcode || "--"}
                    </span>
                  </p>
                  <span className="text-xs">
                    Ghi chú: Số thứ tự này chỉ có giá trị trong ngày khám
                  </span>
                </div>
              </div>
            </div>
          ))
        ) : null}
      </div>
    </>
  );
};

export default ConfirmBill_Booking;
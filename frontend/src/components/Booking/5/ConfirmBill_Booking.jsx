import React, { useState, useEffect } from "react";
import { message, Typography } from "antd";
import {
  notifyErrorWithCustomMessage,
  notifySuccessWithCustomMessage,
} from "../../../utils/notificationHelper";
const { Text } = Typography;

const ConfirmBill_Booking = () => {
  const [paymentData, setPaymentData] = useState([]);
  const [messageApi, contextHolder] = message.useMessage();

  useEffect(() => {
    try {
      const storedData = localStorage.getItem("paymentData");
      if (storedData) {
        const parsedData = JSON.parse(storedData);
        setPaymentData(Array.isArray(parsedData) ? parsedData : [parsedData]);
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
      {paymentData.length > 0 ? (
        paymentData.map((paymentItem, index) => {
          const patientInfo = paymentItem?.appointment?.medicalRecord || {};
          const ticket = paymentItem?.appointment?.tickets?.[0] || {};
          return (
            <div
              key={index}
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
                    (Mã giao dịch: {paymentItem?.transactionId || "Đang tải..."})
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
                  <div className="flex flex-row space-x-1">
                    <h1>Ngày khám:</h1>
                    <span className="font-bold">
                      {ticket.schedule?.date || "Phòng 33 - Khám Nội Lầu 1 Khu A"}
                    </span>
                  </div>
                  <div className="flex justify-center mb-4">
                    <div className="text-center p-4 bg-blue-50 border border-blue-200 rounded-lg">
                      <Text type="secondary">Số thứ tự</Text>
                      <div className="font-bold text-3xl text-blue-600">
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
                        {patientInfo.patientName || "Đang tải..."}
                      </p>
                    </div>
                    <div className="flex flex-row space-x-4">
                      <h1>Ngày sinh:</h1>
                      <p className="font-bold">{patientInfo.dob || "--"}</p>
                    </div>
                    <div className="flex flex-row space-x-4">
                      <h1>Giới tính:</h1>
                      <p className="font-bold">
                        {patientInfo.gender === "MALE"
                          ? "Nam"
                          : patientInfo.gender === "FEMALE"
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
                      <h1>Tiền khám:</h1>
                      <p className="font-bold text-[#009432] text-lg">
                        {paymentItem?.amount
                          ? `${paymentItem.amount.toLocaleString()}đ`
                          : "---.000đ"}
                      </p>
                    </div>
                    <div className="flex flex-row space-x-4">
                      <h1>Đối tượng:</h1>
                      <p className="font-bold">Thu phí</p>
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
                    <span className="font-bold">{patientInfo.barcode || "--"}</span>
                  </p>
                  <span className="text-xs">
                    Ghi chú: Số thứ tự này chỉ có giá trị trong ngày khám
                  </span>
                </div>
              </div>
            </div>
          );
        })
      ) : null}
    </>
  );
};

export default ConfirmBill_Booking;
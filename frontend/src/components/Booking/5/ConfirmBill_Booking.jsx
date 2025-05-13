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
        console.log("Stored paymentData -----------:", parsedData);
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

  useEffect(() => {
    console.log("Payment data -----------------------:", paymentData);
  }, [paymentData]);

  return (
    <>
      {contextHolder}
      {paymentData && paymentData.length > 0 ? (
        paymentData.map((paymentItem, index) => {
          const patientInfo = paymentItem?.appointment?.medicalRecord || {};
          const tickets = paymentItem?.appointment?.tickets || [];
          // Limit to first two tickets
          const displayTickets = tickets.slice(0, 2);

          return (
            <div
              key={index}
              className="w-full max-w-5xl mx-auto my-6"
            >
              <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                {displayTickets.map((ticket, ticketIndex) => (
                  <div
                    key={ticketIndex}
                    className="bg-white border border-gray-200 rounded-xl shadow-lg p-6"
                  >
                    {/* Header Section */}
                    <div className="text-center space-y-2">
                      <h1 className="text-2xl font-bold text-gray-800">
                        Bệnh viện Đại học Y Dược TPHCM
                      </h1>
                      <p className="text-sm text-gray-600">
                        215 Hồng Bàng, P.11, Q5, TPHCM
                      </p>
                      <h2 className="text-xl font-semibold text-blue-700">
                        PHIẾU KHÁM BỆNH
                      </h2>
                      <p className="text-sm text-gray-500">
                        Mã phiếu: {ticket.ticketCode || "Đang tải..."}
                      </p>
                      <p className="text-sm text-gray-500">
                        Booking ID: {paymentItem?.transactionId || "Đang tải..."}
                      </p>
                    </div>

                    {/* General Info Section */}
                    <div className="text-center space-y-4 mt-4">
                      <h3 className="text-lg font-semibold text-gray-800">TỔNG QUÁT</h3>
                      <p className="font-medium text-gray-700">
                        {ticket.schedule?.roomDetail?.name ||
                          "Phòng 33 - Khám Nội Lầu 1 Khu A"}
                      </p>
                      <p className="text-gray-600">
                        Ngày khám: <span className="font-semibold">{ticket.schedule?.date || "Đang tải..."}</span>
                      </p>
                      <div className="inline-block bg-blue-50 border border-blue-200 rounded-lg px-6 py-3">
                        <Text type="secondary" className="text-sm">Số thứ tự</Text>
                        <div className="text-3xl font-bold text-blue-600">
                          {ticket.waitingNumber || "--"}
                        </div>
                      </div>
                    </div>

                    {/* Patient and Appointment Details */}
                    <div className="grid grid-cols-1 md:grid-cols-2 gap-6 text-gray-700 mt-4">
                      <div className="space-y-2">
                        <p>
                          <span className="font-semibold">Họ tên:</span>{" "}
                          {patientInfo.patientName || "Đang tải..."}
                        </p>
                        <p>
                          <span className="font-semibold">Ngày sinh:</span>{" "}
                          {patientInfo.dob || "--"}
                        </p>
                        <p>
                          <span className="font-semibold">Giới tính:</span>{" "}
                          {patientInfo.gender === "MALE"
                            ? "Nam"
                            : patientInfo.gender === "FEMALE"
                            ? "Nữ"
                            : "--"}
                        </p>
                      </div>
                      <div className="space-y-2">
                        <p>
                          <span className="font-semibold">Giờ khám dự kiến:</span>{" "}
                          {ticket.timeSlot?.startTime && ticket.timeSlot?.endTime
                            ? `${ticket.timeSlot.startTime} - ${ticket.timeSlot.endTime}`
                            : "-:00 - -:00"}
                        </p>
                        <p>
                          <span className="font-semibold">Tiền khám:</span>{" "}
                          <span className="text-green-600 font-semibold">
                            {paymentItem?.amount
                              ? `${paymentItem.amount.toLocaleString()}đ`
                              : "---.000đ"}
                          </span>
                        </p>
                        <p>
                          <span className="font-semibold">Đối tượng:</span> Thu phí
                        </p>
                      </div>
                    </div>

                    {/* Footer Section */}
                    <div className="text-center space-y-2 text-gray-600 mt-4">
                      <p>
                        Vui lòng đến phòng khám trước giờ hẹn 15-30 phút để khám bệnh.
                      </p>
                      <p>
                        Số hồ sơ (Mã bệnh nhân):{" "}
                        <span className="font-semibold">{patientInfo.barcode || "--"}</span>
                      </p>
                      <p className="text-xs text-gray-500">
                        Ghi chú: Số thứ tự chỉ có giá trị trong ngày khám.
                      </p>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          );
        })
      ) : null}
    </>
  );
};

export default ConfirmBill_Booking;
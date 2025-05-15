import React, { useState, forwardRef, useEffect } from "react";
import { Button, Modal, Tag, message } from "antd";
import {
  PlusOutlined,
  InfoCircleTwoTone,
  RightOutlined,
  BarcodeOutlined,
  PhoneOutlined,
  HomeOutlined,
} from "@ant-design/icons";
// import '../css/BookingContent.css';
import {
  handleHttpStatusCode,
  notifyErrorWithCustomMessage,
  notifySuccessWithCustomMessage,
} from "../../../utils/notificationHelper";
const ChooseProfile_Booking = forwardRef(
  ({ setStatus, refs, setCurrent }, ref) => {
    const [modalOpen, setModalOpen] = useState(false); // Modal open/close state
    const [medicalRecords, setMedicalRecords] = useState([]); // State to store medical records
    const [messageApi, contextHolder] = message.useMessage(); // Message API for notifications
    // Function to handle the modal state
    const handleModalClose = () => {
      setModalOpen(false);
    };
    const handleSetStatus = (value) => {
      setStatus(value);
    };
    const customerId = localStorage.getItem("customerId");
    const apiUrl = import.meta.env.VITE_API_BASE_URL;

    const handleMedicalRecord = (value) => {
      console.log("Selected medical record:", value);
      localStorage.setItem("medicalRecordId", value);
      setCurrent(1); // Move to the next step
    };

    useEffect(() => {
      console.log("Customer ID at Medical Record:", customerId);
      const handleShowMedicalRecord = async () => {
        try {
          const response = await fetch(
            `${apiUrl}v1/customers/${customerId}/medicalRecords?page=1&size=10&sort=id&direction=asc`,
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
              `Lấy thông tin hồ sơ thất bại: ${
                errorText || response.statusText
              }`
            );
            return;
          }
          const data = await response.json();
          console.log("Raw API response medical record:", data);
          if (data && data.data.content.length > 0) {
            const medicalRecords = data.data.content
              .filter((record) => {
                // Convert both to strings to avoid type mismatch
                const matches =
                  String(record.customerId) === String(customerId);
                return matches;
              })
              .map((record) => ({
                id: record.id,
                barcode: record.barcode,
                patient: record.patient,
              }));
            if (medicalRecords.length > 0) {
              setMedicalRecords(medicalRecords);
              console.log("Updated medicalRecords state:", medicalRecords);
              notifySuccessWithCustomMessage(
                "Lấy thông tin hồ sơ thành công",
                messageApi
              );
            } else {
              notifyErrorWithCustomMessage(
                "Không có dữ liệu hồ sơ bệnh án",
                messageApi
              );
              console.log(
                "No matching medical records found for customerId:",
                customerId
              );
            }
          }
        } catch (e) {
          notifyErrorWithCustomMessage(
            "Lỗi kết nối khi cập nhật hồ sơ",
            messageApi
          );
          console.error("Error updating customer:", e);
        }
      };
      handleShowMedicalRecord();
    }, []);

    return (
      <div className="w-full h-full space-y-4">
        <h1 className="text-black font-bold text-lg">Chọn hồ sơ đặt khám</h1>
        <div className="flex flex-col gap-4 w-full h-full min-h-[460px] justify-between">
          <div className="flex flex-row gap-4 w-full h-full">
            <div className="grid grid-flow-row grid-cols-3 gap-2 w-1/3">
              {/* Add a new profile button */}
              <Button
                ref={refs.ref3}
                style={{
                  display: "flex",
                  flexDirection: "column",
                  alignItems: "center",
                  height: "90px",
                  width: "90px",
                  padding: "8px",
                  backgroundColor: "transparent",
                  color: "#273c75",
                  border: "1px solid #273c75",
                }}
                type="primary"
                size="large"
                onClick={() => setModalOpen(true)}
              >
                <PlusOutlined style={{ fontSize: "30px" }} />
                <span>Thêm</span>
              </Button>
              {/* Existing profiles */}
              {medicalRecords && medicalRecords.length > 0 ? (
                medicalRecords.map((record) => (
                  <Button
                    key={record.id}
                    style={{
                      display: "flex",
                      flexDirection: "column",
                      alignItems: "center",
                      textAlign: "center",
                      height: "90px",
                      width: "90px",
                      padding: "4px",
                      backgroundColor: "transparent",
                      color: "#273c75",
                      border: "1px solid #273c75",
                    }}
                    type="primary"
                    size="large"
                  >
                    <img
                      width="48"
                      height="48"
                      src={
                        record.patient.gender === "MALE"
                          ? "https://api.dicebear.com/7.x/miniavs/svg?seed=8"
                          : "https://api.dicebear.com/9.x/miniavs/svg?seed=Liliana"
                      }
                      alt="businessman"
                    />
                    <span>
                      <Tag
                        color="purple"
                        style={{ width: "100%", textAlign: "center" }}
                      >
                        {record.patient.name.split(" ").pop()}
                      </Tag>
                    </span>
                  </Button>
                ))
              ) : (
                <div className="flex w-full h-full justify-center items-center">
                  <h1 className="text-black font-bold text-base">
                    Chưa có hồ sơ bệnh án
                  </h1>
                </div>
              )}
            </div>
            {/* Profile details section */}
            <div className="flex flex-col w-2/3 h-fit">
              {medicalRecords && medicalRecords.length > 0 ? (
                medicalRecords.map((record) => (
                  <Button
                    key={record.id}
                    style={{
                      display: "flex",
                      flexDirection: "column",
                      height: "70px",
                      width: "100%",
                      padding: "16px",
                      backgroundColor: "transparent",
                      color: "#273c75",
                      border: "1px solid #273c75",
                      marginBottom: "8px",
                    }}
                    type="primary"
                    size="large"
                    ref={refs.ref4}
                    onClick={() => handleMedicalRecord(record.id)}
                  >
                    <div className="flex flex-row justify-between w-full">
                      <div className="space-x-1">
                        <InfoCircleTwoTone />
                        <span className="font-bold text-sm tracking-normal">
                          {record.patient.name}
                        </span>
                      </div>
                      <div>
                        <RightOutlined />
                      </div>
                    </div>
                    <div className="flex flex-row justify-between w-full">
                      <div className="flex flex-row w-full text-sm">
                        <div className="mr-18 flex flex-row h-fit space-x-1">
                          <BarcodeOutlined />
                          <span>{record.barcode}</span>
                        </div>
                        <div className="flex flex-row h-fit space-x-1">
                          <PhoneOutlined />
                          <span>{record.patient.phone}</span>
                        </div>
                      </div>
                    </div>
                  </Button>
                ))
              ) : (
                <div className="flex w-full h-full justify-center items-center">
                  <h1 className="text-black font-bold text-base">
                    Chưa có hồ sơ bệnh án
                  </h1>
                </div>
              )}
              {/* Other profiles */}
            </div>
          </div>
          <div className="h-full w-fit">
            <Button
              style={{ backgroundColor: "transparent" }}
              icon={<HomeOutlined />}
              onClick={() => handleSetStatus("records")} // Go back to records view
            >
              Quay lại
            </Button>
          </div>
        </div>
        <Modal
          centered
          open={modalOpen}
          onCancel={handleModalClose}
          footer={null}
        >
          <div className="text-center">
            <h1 className="text-lg mb-4 font-bold">
              Bạn đã từng khám tại{" "}
              <span className="text-[#273c75]">
                Bệnh viện Đại học Y Dược TP.HCM?
              </span>
            </h1>
          </div>
          <div className="flex flex-col gap-1">
            <Button
              style={{ color: "white", backgroundColor: "#273c75" }}
              onClick={() => {
                setModalOpen(false);
                handleSetStatus("hasUser");
              }}
            >
              ĐÃ TỪNG KHÁM, NHẬP HỒ SƠ
            </Button>
            <Button
              style={{ color: "#273c75", border: "1px solid #273c75" }}
              onClick={() => {
                setModalOpen(false);
                handleSetStatus("new");
              }}
            >
              CHƯA TỪNG KHÁM, TẠO HỒ SƠ MỚI
            </Button>
          </div>
        </Modal>
        {contextHolder}
      </div>
    );
  }
);
export default ChooseProfile_Booking;

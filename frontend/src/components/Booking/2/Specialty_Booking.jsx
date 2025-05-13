import React, { useEffect, useState } from "react";
import { 
  Card, 
  Row, 
  Col, 
  Button, 
  message, 
  Spin, 
  Typography, 
  Divider, 
  Tooltip, 
  Empty,
  Space
} from "antd";
import { 
  RightOutlined, 
  InfoCircleOutlined, 
  MedicineBoxOutlined,
  HeartOutlined 
} from "@ant-design/icons";
import {
  handleHttpStatusCode,
  notifySuccessWithCustomMessage,
  notifyErrorWithCustomMessage,
} from "../../../utils/notificationHelper";

const { Title, Text } = Typography;

const Specialty_Booking = ({
  setSpecialty,
  setSpecialtyId,
  setPrice,
  setChoosedSpecialty,
  setStep,
  ref,
}) => {
  const apiUrl = import.meta.env.VITE_API_BASE_URL;
  const [messageApi, contextHolder] = message.useMessage();
  const [specialtyData, setSpecialtyData] = useState([]);
  const [loading, setLoading] = useState(true);
  const [selectedId, setSelectedId] = useState(null);

  const handleSpecialty = (specialtyId, specialtyName, price) => {
    console.log("Selected specialty id:", specialtyId);
    localStorage.setItem("specialtyId", specialtyId);
    setSelectedId(specialtyId);

    // Cập nhật state và chuyển bước
    setSpecialtyId(specialtyId); // Truyền specialtyId lên parent
    setSpecialty(specialtyName);
    setPrice(price);
    setChoosedSpecialty(true);
    
    // Thêm hiệu ứng chuyển sau 500ms
    setTimeout(() => {
      setStep(2);
    }, 300);
  };

  useEffect(() => {
    const handleDataSpecialty = async () => {
      setLoading(true);
      try {
        const response = await fetch(
          `${apiUrl}v1/medical-specialties?page=1&size=20&sort=id&direction=asc`,
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
            `Lấy thông tin chuyên khoa thất bại: ${
              errorText || response.statusText
            }`
          );
          return;
        }
        const data = await response.json();
        console.log("Raw API response:", data);
        if (data && data.data.content.length > 0) {
          const specialties = data.data.content.map((specialty) => ({
            id: specialty.id,
            name: specialty.name,
            price: new Intl.NumberFormat("vi-VN", {
              style: "currency",
              currency: "VND",
            })
              .format(specialty.price)
              .replace("₫", "đ"),
            rawPrice: specialty.price,
            note: specialty.note,
          }));
          console.log("Specialty data:", specialties);
          setSpecialtyData(specialties);
          notifySuccessWithCustomMessage(
            "Lấy danh sách chuyên khoa thành công",
            messageApi
          );
        } else {
          notifyErrorWithCustomMessage(
            "Không có dữ liệu chuyên khoa",
            messageApi
          );
        }
      } catch (e) {
        notifyErrorWithCustomMessage(
          "Lỗi kết nối khi tải dữ liệu chuyên khoa",
          messageApi
        );
        console.error("Error fetching specialties:", e);
      } finally {
        setLoading(false);
      }
    };
    handleDataSpecialty();
  }, []);

  return (
    <div className="w-full md:w-4/5 lg:w-3/4 mx-auto" ref={ref}>
      {contextHolder}
      
      <div className="mb-6 text-center">
        <Space align="center">
          <MedicineBoxOutlined className="text-blue-500 text-2xl" />
          <Title level={3} className="m-0">Chọn Chuyên Khoa</Title>
        </Space>
        <Text type="secondary" className="mt-2 block">
          Vui lòng chọn chuyên khoa phù hợp với nhu cầu khám bệnh của bạn
        </Text>
      </div>

      <Card 
        className="shadow-md rounded-xl overflow-hidden"
        bodyStyle={{ padding: 0 }}
      >
        {loading ? (
          <div className="flex justify-center items-center h-96">
            <Spin size="large" tip="Đang tải danh sách chuyên khoa..." />
          </div>
        ) : specialtyData && specialtyData.length > 0 ? (
          <div className="max-h-[500px] overflow-y-auto p-2">
            <Row gutter={[16, 16]} className="p-4">
              {specialtyData.map((item) => (
                <Col xs={24} sm={12} lg={8} key={item.id}>
                  <Card
                    hoverable
                    className={`h-full transition-all duration-300 ${
                      selectedId === item.id
                        ? "border-blue-500 border-2 bg-blue-50"
                        : "border border-gray-200"
                    }`}
                    onClick={() => handleSpecialty(item.id, item.name, item.price)}
                  >
                    <div className="flex items-center mb-2">
                      <HeartOutlined className={`mr-2 text-xl ${
                        selectedId === item.id ? "text-blue-500" : "text-gray-400"
                      }`} />
                      <Text strong className="text-base text-blue-700 flex-1">
                        {item.name}
                      </Text>
                    </div>
                    
                    <Divider className="my-2" />
                    
                    <div className="flex justify-between items-center">
                      <Text className="font-medium text-red-500">
                        {item.price}
                      </Text>
                      <Tooltip title="Chọn chuyên khoa này">
                        <Button 
                          type="primary" 
                          shape="circle" 
                          size="small"
                          icon={<RightOutlined />} 
                          className={selectedId === item.id ? "bg-blue-600" : ""}
                        />
                      </Tooltip>
                    </div>
                    
                    {item.note && (
                      <div className="mt-3 flex items-start">
                        <InfoCircleOutlined className="mr-1 mt-1 text-gray-400" />
                        <Text type="secondary" className="text-xs">
                          {item.note}
                        </Text>
                      </div>
                    )}
                  </Card>
                </Col>
              ))}
            </Row>
          </div>
        ) : (
          <Empty
            description="Không có dữ liệu chuyên khoa"
            className="py-16"
            image={Empty.PRESENTED_IMAGE_SIMPLE}
          />
        )}
      </Card>
    </div>
  );
};

export default Specialty_Booking;
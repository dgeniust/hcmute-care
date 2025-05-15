import React, { useState, useEffect } from "react";
import {
  List,
  Space,
  Spin,
  Typography,
  Empty,
  Card,
  Tag,
  Tabs,
  Descriptions,
  Table,
  Divider,
} from "antd";
import {
  UserOutlined,
  PhoneOutlined,
  CalendarOutlined,
  FileTextOutlined,
  MedicineBoxOutlined,
} from "@ant-design/icons";
const { Title, Text } = Typography;
const { TabPane } = Tabs;

const MH_3 = ({ loading = false }) => {
  const apiUrl = import.meta.env.VITE_API_BASE_URL;

  const [detailEncounters, setDetailEncounters] = useState([]);
  const [medicalRecords, setMedicalRecords] = useState([]);
  const [selectedMedicalRecord, setSelectedMedicalRecord] = useState(null);
  const [encountersLoading, setEncountersLoading] = useState(false);
  const customerId = localStorage.getItem("customerId");

  useEffect(() => {
    const fetchMedicalRecords = async () => {
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
        if (!response.ok) throw new Error("Lỗi khi lấy hồ sơ y tế");
        const data = await response.json();
        console.log("Dữ liệu API:", data);
        if (data?.data?.content?.length > 0) {
          setMedicalRecords(data.data.content);
        }
      } catch (error) {
        console.error("Lỗi khi lấy hồ sơ y tế:", error);
      }
    };
    if (customerId) fetchMedicalRecords();
  }, [customerId]);

  const handleMedicalRecordSelect = async (recordId) => {
    try {
      setEncountersLoading(true);
      setSelectedMedicalRecord(
        medicalRecords.find((record) => record.id === recordId)
      );
      const response = await fetch(
        `${apiUrl}v1/medical-records/${recordId}/encounters`,
        {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
          },
        }
      );
      if (!response.ok) throw new Error("Lỗi khi lấy danh sách cuộc gặp");
      const data = await response.json();
      console.log("Dữ liệu cuộc gặp:", data);
      setDetailEncounters(data.data || []);
    } catch (error) {
      console.error("Lỗi khi chọn hồ sơ y tế:", error);
      setDetailEncounters([]);
    } finally {
      setEncountersLoading(false);
    }
  };

  const formatDate = (dateString) => {
    if (!dateString) return "N/A";
    const date = new Date(dateString);
    return new Intl.DateTimeFormat("vi-VN", {
      year: "numeric",
      month: "2-digit",
      day: "2-digit",
      hour: "2-digit",
      minute: "2-digit",
    }).format(date);
  };

  // Hiển thị badge trạng thái
  const renderStatusBadge = (status) => {
    let color = "default";
    switch (status) {
      case "RECEIVED":
        status = "Đã nhận";
        color = "green";
        break;
      case "PENDING":
        status = "Đang chờ";
        color = "blue";
        break;
      case "CANCELLED":
        status = "Đã hủy";
        color = "red";
        break;
      default:
        color = "default";
    }
    return <Tag color={color}>{status || "Không xác định"}</Tag>;
  };

  // Hiển thị chi tiết đơn thuốc
  const renderPrescriptions = (prescriptions) => {
    if (!prescriptions || prescriptions.length === 0) {
      return <Empty description="Không có đơn thuốc" />;
    }

    const columns = [
      {
        title: "Tên thuốc",
        dataIndex: "name",
        key: "name",
      },
      {
        title: "Liều lượng",
        dataIndex: "dosage",
        key: "dosage",
      },
      {
        title: "Số lượng",
        dataIndex: "quantity",
        key: "quantity",
        render: (quantity, record) => `${quantity} ${record.unit || ""}`,
      },
    ];

    return (
      <div>
        {prescriptions.map((prescription, index) => (
          <div key={prescription.id} className="mb-6">
            <div className="flex justify-between items-center mb-2">
              <Title level={5}>
                Đơn thuốc #{prescription.id} - Ngày kê đơn:{" "}
                {formatDate(prescription.issueDate)}
              </Title>
              {renderStatusBadge(prescription.status)}
            </div>
            {prescription.prescriptionItems &&
            prescription.prescriptionItems.length > 0 ? (
              <Table
                dataSource={prescription.prescriptionItems.map((item, idx) => ({
                  ...item,
                  key: idx,
                }))}
                columns={columns}
                pagination={false}
                size="small"
                className="shadow-sm"
              />
            ) : (
              <Empty description="Không có mục thuốc trong đơn này" />
            )}
            {index < prescriptions.length - 1 && <Divider />}
          </div>
        ))}
      </div>
    );
  };

  // Hiển thị thẻ cuộc gặp
  const renderEncounterCard = (encounter) => {
    return (
      <Card
        key={encounter.id}
        className="mb-4 shadow-sm"
        title={
          <div className="flex justify-between items-center">
            <span>Lần khám ngày: {formatDate(encounter.visitDate)}</span>
          </div>
        }
      >
        <Tabs defaultActiveKey="1">
          <TabPane
            tab={
              <span>
                <FileTextOutlined /> Thông tin chung
              </span>
            }
            key="1"
          >
            <Descriptions bordered size="small" column={1} className="mb-4">
              <Descriptions.Item label="Chẩn đoán">
                {encounter.diagnosis || "Không có thông tin"}
              </Descriptions.Item>
              <Descriptions.Item label="Phương pháp điều trị">
                {encounter.treatment || "Không có thông tin"}
              </Descriptions.Item>
              <Descriptions.Item label="Ghi chú">
                {encounter.notes || "Không có thông tin"}
              </Descriptions.Item>
            </Descriptions>
          </TabPane>
          <TabPane
            tab={
              <span>
                <MedicineBoxOutlined /> Đơn thuốc
              </span>
            }
            key="2"
          >
            {renderPrescriptions(encounter.prescriptions)}
          </TabPane>
        </Tabs>
      </Card>
    );
  };

  if (loading || !customerId) {
    return (
      <div className="w-full h-full flex items-center justify-center min-h-[460px]">
        <Spin size="large" tip="Đang tải..." />
      </div>
    );
  }

  return (
    <div className="flex flex-col md:flex-row w-full h-full">
      {/* Cột trái - Danh sách hồ sơ y tế */}
      <div className="w-full md:w-1/4 border-r border-gray-200 p-2 bg-white">
        <div className="mb-4">
          <Title level={4} className="flex items-center text-blue-600">
            <UserOutlined className="mr-2" />
            Hồ sơ y tế
          </Title>
        </div>
        {medicalRecords.length === 0 ? (
          <Empty
            image={Empty.PRESENTED_IMAGE_SIMPLE}
            description="Không có hồ sơ y tế"
            className="mt-8"
          />
        ) : (
          <List
            className="w-full bg-white rounded shadow-sm h-full"
            itemLayout="horizontal"
            dataSource={medicalRecords}
            renderItem={(record) => (
              <List.Item
                key={record.id}
                className={`cursor-pointer hover:bg-gray-50 ${
                  selectedMedicalRecord?.id === record.id ? "bg-blue-50" : ""
                }`}
                onClick={() => handleMedicalRecordSelect(record.id)}
              >
                <List.Item.Meta
                  className="px-4 py-2"
                  title={
                    <div className="flex justify-between items-center">
                      <Text strong className="text-base">
                        {record.patient?.name || "Không xác định"}
                      </Text>
                      <Space>
                        {record.patient?.gender === "MALE" ? (
                          <Text className="text-sm text-gray-700">Nam</Text>
                        ) : (
                          <Text className="text-sm text-gray-700">Nữ</Text>
                        )}
                      </Space>
                    </div>
                  }
                  description={
                    <Space>
                      <PhoneOutlined className="text-gray-500" />
                      <Text className="text-xs text-gray-500">
                        {record.patient?.phone || "Không có số điện thoại"}
                      </Text>
                    </Space>
                  }
                />
              </List.Item>
            )}
          />
        )}
      </div>

      {/* Cột phải - Danh sách cuộc gặp */}
      <div className="w-full md:w-3/4 p-4 bg-gray-50">
        {encountersLoading ? (
          <div className="flex items-center justify-center h-full min-h-[460px]">
            <Spin size="large" tip="Đang tải dữ liệu..." />
          </div>
        ) : selectedMedicalRecord ? (
          <div>
            <div className="bg-white p-4 rounded shadow-sm mb-4">
              <div className="flex justify-between items-center mb-4">
                <Title level={4} className="m-0">
                  <UserOutlined className="mr-2 text-blue-500" />
                  {selectedMedicalRecord.patient?.name || "Không có tên"}
                </Title>
                <Space>
                  <Tag color="blue">
                    {selectedMedicalRecord.patient?.gender === "MALE"
                      ? "Nam"
                      : "Nữ"}
                  </Tag>
                  <Tag color="green">
                    <CalendarOutlined className="mr-1" />
                    {selectedMedicalRecord.patient?.dob
                      ? new Date(
                          selectedMedicalRecord.patient.dob
                        ).toLocaleDateString("vi-VN")
                      : "Không có ngày sinh"}
                  </Tag>
                </Space>
              </div>
              <Descriptions
                bordered
                size="small"
                column={{ xxl: 4, xl: 3, lg: 3, md: 2, sm: 1, xs: 1 }}
              >
                <Descriptions.Item label="SĐT">
                  {selectedMedicalRecord.patient?.phone || "Không có"}
                </Descriptions.Item>
                <Descriptions.Item label="Email">
                  {selectedMedicalRecord.patient?.email || "Không có"}
                </Descriptions.Item>
                <Descriptions.Item label="Địa chỉ">
                  {selectedMedicalRecord.patient?.address || "Không có"}
                </Descriptions.Item>
              </Descriptions>
            </div>

            <Title level={5} className="mb-4">
              <FileTextOutlined className="mr-2" />
              Lịch sử khám bệnh ({detailEncounters.length})
            </Title>

            {detailEncounters.length === 0 ? (
              <Empty
                description="Không có lịch sử khám bệnh"
                image={Empty.PRESENTED_IMAGE_SIMPLE}
                className="bg-white p-8 rounded shadow-sm"
              />
            ) : (
              <div className="space-y-4">
                {detailEncounters.map((encounter) =>
                  renderEncounterCard(encounter)
                )}
              </div>
            )}
          </div>
        ) : (
          <div className="flex flex-col items-center justify-center h-full min-h-[460px] text-center bg-white rounded shadow-sm">
            <UserOutlined className="text-6xl text-gray-300 mb-4" />
            <Title level={4}>Vui lòng chọn một hồ sơ y tế</Title>
            <Text className="text-gray-500">
              Chọn một hồ sơ y tế từ danh sách bên trái để xem chi tiết
            </Text>
          </div>
        )}
      </div>
    </div>
  );
};

export default MH_3;

import React, { useEffect, useState } from "react";
import { Button, Modal, message, Card, Typography, Empty, Spin, Badge, Tabs, Avatar, List, Form, Input, Select, DatePicker } from "antd";
import {
  InfoCircleTwoTone,
  RightOutlined,
  BarcodeOutlined,
  PhoneOutlined,
  FileTextOutlined,
  MedicineBoxOutlined,
  PictureOutlined,
  FormOutlined,
  HeartOutlined,
  UserOutlined,
  FileSearchOutlined,
  CalendarOutlined,
} from "@ant-design/icons";
import UserDetails from "../../components/Personal/UserDetails";
import {
  handleHttpStatusCode,
  notifySuccessWithCustomMessage,
  notifyErrorWithCustomMessage,
} from "../../utils/notificationHelper";
import { useNavigate } from "react-router-dom";
const { Title, Text } = Typography;
const { TabPane } = Tabs;
const { Option } = Select;

const MedicalRecord = () => {
  const apiUrl = import.meta.env.VITE_API_BASE_URL;
  const [isModalInfoOpen, setIsModalInfoOpen] = useState(false);
  const [modalContent, setModalContent] = useState(null);
  const [modalButton, setModalButton] = useState(false);
  const [isCreateModalOpen, setIsCreateModalOpen] = useState(false);
  const [medicalRecords, setMedicalRecords] = useState([]);
  const [loading, setLoading] = useState(true);
  const [form] = Form.useForm();
  const [messageApi, contextHolder] = message.useMessage();
  const customerId = localStorage.getItem("customerId");
  const navigate = useNavigate();
  const handleModalButtonClose = () => {
    setModalButton(false);
  };

  const showButtonModal = (userData) => {
    setModalContent(userData);
    setModalButton(true);
  };

  const handleInfoCancel = () => {
    setIsModalInfoOpen(false);
    setModalContent(null);
  };

  const handleCreateModalOpen = () => {
    setIsCreateModalOpen(true);
  };

  const handleCreateModalClose = () => {
    setIsCreateModalOpen(false);
    form.resetFields();
  };

  const handleCreateMedicalRecord = async (values) => {
    const payload = {
      patient: {
        name: `${values.firstName} ${values.lastName}`.trim(),
        cccd: values.cccd,
        dob: values.dob.format("YYYY-MM-DD"),
        gender: values.gender.toUpperCase(),
        address: values.address,
        phone: values.phone,
        email: values.email,
        nation: values.nation,
        career: values.career,
      },
      customerId: customerId,
    };

    try {
      const response = await fetch(`${apiUrl}v1/medical-records`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
        },
        body: JSON.stringify(payload),
      });

      if (!response.ok) {
        const errorText = await response.text();
        handleHttpStatusCode(
          response.status,
          "",
          `Tạo hồ sơ khám bệnh thất bại: ${errorText || response.statusText}`
        );
        return;
      }

      const data = await response.json();
      if (data && data.data) {
        notifySuccessWithCustomMessage("Tạo hồ sơ khám bệnh thành công", messageApi);
        setMedicalRecords([...medicalRecords, data.data]);
        handleCreateModalClose();
      } else {
        notifyErrorWithCustomMessage("Tạo hồ sơ khám bệnh thất bại", messageApi);
      }
    } catch (error) {
      console.error("Error submitting form:", error);
      notifyErrorWithCustomMessage("Lỗi khi gửi thông tin hồ sơ", messageApi);
    }
  };

  useEffect(() => {
    const handleDataMedicalRecord = async () => {
      try {
        setLoading(true);
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
          handleHttpStatusCode(response.status, "", `Lấy hồ sơ bệnh án thất bại: ${errorText || response.statusText}`);
          return;
        }
        const data = await response.json();
        if (data && data.data.content.length > 0) {
          const medicalRecords = data.data.content
            .filter((record) => String(record.customerId) === String(customerId))
            .map((record) => ({
              id: record.id,
              barcode: record.barcode,
              patient: record.patient,
            }));
          if (medicalRecords.length > 0) {
            setMedicalRecords(medicalRecords);
            notifySuccessWithCustomMessage("Lấy thông tin hồ sơ thành công", messageApi);
          } else {
            notifyErrorWithCustomMessage("Không có dữ liệu hồ sơ bệnh án", messageApi);
          }
        }
      } catch (e) {
        notifyErrorWithCustomMessage("Lỗi kết nối khi cập nhật hồ sơ", messageApi);
      } finally {
        setLoading(false);
      }
    };
    handleDataMedicalRecord();
  }, []);

  const functionButtons = [
    {
      title: "THÔNG TIN HỒ SƠ",
      icon: <FileTextOutlined />,
      onClick: () => {
        setIsModalInfoOpen(true);
        setModalButton(false);
      },
    },
    {
      title: "XEM HỒ SƠ SỨC KHỎE",
      icon: <HeartOutlined />,
      onClick: () => setModalButton(false),
    },
    {
      title: "XEM KẾT QUẢ CẬN LÂM SÀNG NGOẠI TRÚ",
      icon: <FileSearchOutlined />,
      onClick: () => setModalButton(false),
    },
    {
      title: "XEM HÌNH ẢNH CHỤP",
      icon: <PictureOutlined />,
      onClick: () => setModalButton(false),
    },
    {
      title: "XEM PHIẾU ĐĂNG KÝ KHÁM",
      icon: <FormOutlined />,
      onClick: () => setModalButton(false),
    },
  ];

  return (
    <>
      <div className="flex w-full h-full">
      <div className="w-[70%] pr-4">
        <Card className="w-full shadow-md h-full" style={{ padding: "0px" }}>
          <div className="bg-gradient-to-r from-blue-600 to-blue-800 p-6 rounded-t-lg text-white">
            <div className="flex items-center justify-between">
              <div className="flex items-center">
                <MedicineBoxOutlined className="text-3xl text-white mr-4" />
                <Title level={2} style={{ color: "white", margin: 0 }}>
                  Hồ sơ người bệnh
                </Title>
              </div>
              <Badge count={medicalRecords.length} overflowCount={99} className="mr-2">
                <Avatar size="large" icon={<UserOutlined />} className="bg-blue-300" />
              </Badge>
            </div>
            <Text style={{ color: "white" }}>Quản lý thông tin y tế và lịch sử bệnh án</Text>
          </div>
          <Tabs defaultActiveKey="1" className="px-6 pt- Johanna 4 px-6 pt-4" type="card">
            <TabPane
              tab={
                <span>
                  <FileTextOutlined /> Hồ sơ bệnh án
                </span>
              }
              key="1"
            >
              <div className="p-4">
                {loading ? (
                  <div className="flex justify-center items-center h-64">
                    <Spin size="large" tip="Đang tải dữ liệu..." />
                  </div>
                ) : medicalRecords && medicalRecords.length > 0 ? (
                  <List
                    itemLayout="vertical"
                    dataSource={medicalRecords}
                    renderItem={(user) => (
                      <List.Item className="mb-4 p-0" key={user.id}>
                        <Card
                          hoverable
                          className="w-full transform transition-all duration-300 hover:scale-[1.01] border-l-4 border-l-blue-600"
                          onClick={() => showButtonModal(user)}
                        >
                          <div className="flex items-center justify-between">
                            <div className="flex items-center">
                              <Avatar size={48} icon={<UserOutlined />} className="bg-blue-600 mr-4" />
                              <div>
                                <Text strong className="text-lg block mb-1">
                                  {user.patient.name}
                                </Text>
                                <div className="flex flex-wrap gap-4">
                                  <div className="flex items-center">
                                    <BarcodeOutlined className="text-blue-600 mr-1" />
                                    <Text type="secondary">{user.barcode}</Text>
                                  </div>
                                  <div className="flex items-center">
                                    <PhoneOutlined className="text-blue-600 mr-1" />
                                    <Text type="secondary">{user.patient.phone}</Text>
                                  </div>
                                  <div className="flex items-center">
                                    <CalendarOutlined className="text-blue-600 mr-1" />
                                    <Text type="secondary">Hồ sơ #{user.id}</Text>
                                  </div>
                                </div>
                              </div>
                            </div>
                            <Button
                              type="primary"
                              shape="circle"
                              icon={<RightOutlined />}
                              size="large"
                              className="bg-blue-600 hover:bg-blue-700"
                            />
                          </div>
                        </Card>
                      </List.Item>
                    )}
                  />
                ) : (
                  <Empty
                    description="Không có dữ liệu hồ sơ bệnh án"
                    image={Empty.PRESENTED_IMAGE_SIMPLE}
                    className="my-16"
                  />
                )}
              </div>
            </TabPane>
            <TabPane
              tab={
                <span>
                  <FormOutlined /> Phiếu đăng ký
                </span>
              }
              key="2"
            >
              <Empty description="Chưa có phiếu đăng ký khám" className="py-16" />
            </TabPane>
          </Tabs>
          <div className="p-4 bg-gray-50 mt-4 border-t border-gray-200">
            <Text type="secondary" className="flex items-center justify-center">
              <InfoCircleTwoTone className="mr-2" />
              Nhấn vào hồ sơ để xem chi tiết và sử dụng các chức năng khác
            </Text>
          </div>
        </Card>
      </div>
      <div className="w-[30%]">
        <Card className="w-full shadow-md h-full">
          <div className="text-center mb-6">
            <div className="inline-flex items-center justify-center w-16 h-16 rounded-full bg-blue-100 mb-4">
              <HeartOutlined className="text-3xl text-blue-600" />
            </div>
            <Title level={3} className="m-0">
              Thông tin sức khỏe
            </Title>
            <Text type="secondary">Tổng quan hồ sơ y tế của bạn</Text>
          </div>
          <div className="mb-6">
            <Card className="bg-gray-50 mb-4">
              <div className="flex justify-between items-center">
                <Text strong>Tổng số hồ sơ</Text>
                <Badge count={medicalRecords.length} className="bg-blue-600" />
              </div>
            </Card>
            <Card className="bg-gray-50 mb-4">
              <div className="flex justify-between items-center">
                <Text strong>Lần khám gần nhất</Text>
                <Text type="secondary">{medicalRecords.length > 0 ? "15/04/2023" : "Chưa có"}</Text>
              </div>
            </Card>
            <Card className="bg-gray-50">
              <div className="flex justify-between items-center">
                <Text strong>Trạng thái</Text>
                <Badge status="processing" text="Đang theo dõi" />
              </div>
            </Card>
          </div>
          <div className="bg-blue-50 p-4 rounded-lg mb-6">
            <div className="flex items-start mb-2">
              <InfoCircleTwoTone className="text-xl mr-2 mt-1" />
              <Title level={5} className="m-0">
                Lời khuyên sức khỏe
              </Title>
            </div>
            <Text className="text-gray-600">
              Uống đủ nước, tập thể dục đều đặn và khám sức khỏe định kỳ 6 tháng/lần để duy trì sức khỏe tốt.
            </Text>
          </div>
          <div>
            <Title level={5}>Thao tác nhanh</Title>
            <div className="grid grid-cols-2 gap-2">
              <Button icon={<FormOutlined />} className="text-left h-auto py-2" onClick={() => navigate("/booking")}>
                Đặt lịch khám
              </Button>
              <Button icon={<FileSearchOutlined />} className="text-left h-auto py-2" onClick={() => navigate("/medical-history")}>
                Xem lịch sử giao dịch
              </Button>
              <Button icon={<MedicineBoxOutlined />} className="text-left h-auto py-2" onClick={handleCreateModalOpen}>
                Tạo hồ sơ bệnh án
              </Button>
              <Button icon={<PhoneOutlined />} className="text-left h-auto py-2" onClick={() => navigate("/contact-service")}>
                Liên hệ hỗ trợ
              </Button>
            </div>
          </div>
        </Card>
      </div>
      <Modal
        title={
          <div className="flex items-center">
            <UserOutlined className="text-xl text-blue-600 mr-2" />
            <span>Thông tin người bệnh</span>
          </div>
        }
        open={isModalInfoOpen}
        onCancel={handleInfoCancel}
        width={600}
        footer={null}
        className="custom-modal"
      >
        {modalContent && <UserDetails modalContent={modalContent} />}
      </Modal>
      <Modal
        centered
        open={modalButton}
        onCancel={handleModalButtonClose}
        footer={null}
        width={500}
        className="function-modal"
        style={{ padding: "0" }}
        title={
          <div className="bg-gradient-to-r from-blue-600 to-blue-800 p-4 rounded-t-lg text-center">
            <Title level={4} style={{ color: "white", margin: 0 }}>
              <MedicineBoxOutlined className="mr-2" />
              CHỌN CHỨC NĂNG
            </Title>
          </div>
        }
        closeIcon={<Button type="text" icon={<RightOutlined />} style={{ color: "white", top: "22px", left: "-50px", position: "absolute" }} />}
      >
        <div className="p-2">
          <div className="grid grid-cols-1 gap-4">
            {functionButtons.map((btn, index) => (
              <Button
                key={index}
                onClick={btn.onClick}
                size="large"
                className="h-16 flex items-center justify-start hover:bg-blue-50 transition-all duration-300 border-0 shadow-sm rounded-lg"
              >
                <div className="flex items-center w-full">
                  <div className="p-3 rounded-lg mr-4 flex items-center justify-center">
                    {React.cloneElement(btn.icon, { className: "text-xl text-white" })}
                  </div>
                  <span className="text-gray-700 font-medium text-base">{btn.title}</span>
                  <RightOutlined className="ml-auto text-red-600" />
                </div>
              </Button>
            ))}
          </div>
        </div>
      </Modal>
      <Modal
        title={
          <div className="flex items-center">
            <MedicineBoxOutlined className="text-xl text-blue-600 mr-2" />
            <span>Tạo hồ sơ bệnh án</span>
          </div>
        }
        open={isCreateModalOpen}
        onCancel={handleCreateModalClose}
        width={600}
        footer={null}
        className="custom-modal"
      >
        <Form
          form={form}
          layout="vertical"
          onFinish={handleCreateMedicalRecord}
          className="p-4"
        >
          <div className="grid grid-cols-2 gap-4">
            <Form.Item
              name="firstName"
              label="Họ"
              rules={[{ required: true, message: "Vui lòng nhập họ" }]}
            >
              <Input placeholder="Nhập họ" />
            </Form.Item>
            <Form.Item
              name="lastName"
              label="Tên"
              rules={[{ required: true, message: "Vui lòng nhập tên" }]}
            >
              <Input placeholder="Nhập tên" />
            </Form.Item>
          </div>
          <Form.Item
            name="cccd"
            label="CCCD"
            rules={[{ required: true, message: "Vui lòng nhập CCCD" }]}
          >
            <Input placeholder="Nhập CCCD" />
          </Form.Item>
          <div className="grid grid-cols-2 gap-4">
            <Form.Item
              name="dob"
              label="Ngày sinh"
              rules={[{ required: true, message: "Vui lòng chọn ngày sinh" }]}
            >
              <DatePicker format="DD/MM/YYYY" style={{ width: "100%" }} />
            </Form.Item>
            <Form.Item
              name="gender"
              label="Giới tính"
              rules={[{ required: true, message: "Vui lòng chọn giới tính" }]}
            >
              <Select placeholder="Chọn giới tính">
                <Option value="MALE">Nam</Option>
                <Option value="FEMALE">Nữ</Option>
              </Select>
            </Form.Item>
          </div>
          <Form.Item
            name="address"
            label="Địa chỉ"
            rules={[{ required: true, message: "Vui lòng nhập địa chỉ" }]}
          >
            <Input placeholder="Nhập địa chỉ" />
          </Form.Item>
          <div className="grid grid-cols-2 gap-4">
            <Form.Item
              name="phone"
              label="Số điện thoại"
              rules={[{ required: true, message: "Vui lòng nhập số điện thoại" }]}
            >
              <Input placeholder="Nhập số điện thoại" />
            </Form.Item>
            <Form.Item
              name="email"
              label="Email"
              rules={[
                { required: true, message: "Vui lòng nhập email" },
                { type: "email", message: "Email không hợp lệ" },
              ]}
            >
              <Input placeholder="Nhập email" />
            </Form.Item>
          </div>
          <div className="grid grid-cols-2 gap-4">
            <Form.Item
              name="nation"
              label="Quốc tịch"
              rules={[{ required: true, message: "Vui lòng nhập quốc tịch" }]}
            >
              <Input placeholder="Nhập quốc tịch" />
            </Form.Item>
            <Form.Item
              name="career"
              label="Nghề nghiệp"
              rules={[{ required: true, message: "Vui lòng nhập nghề nghiệp" }]}
            >
              <Input placeholder="Nhập nghề nghiệp" />
            </Form.Item>
          </div>
          <Form.Item>
            <Button
              type="primary"
              htmlType="submit"
              className="w-full bg-blue-600 hover:bg-blue-700"
            >
              Tạo hồ sơ
            </Button>
          </Form.Item>
        </Form>
      </Modal>
      {contextHolder}
    </div>
    </>
  );
};

export default MedicalRecord;
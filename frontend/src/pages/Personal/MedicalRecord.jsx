
import React, { useEffect, useState } from "react"
import { Button, Modal, message, Card, Typography, Empty, Spin, Badge, Tabs, Avatar, List } from "antd"

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
} from "@ant-design/icons"
import UserDetails from "../../components/Personal/UserDetails"

import {
  handleHttpStatusCode,
  notifySuccessWithCustomMessage,
  notifyErrorWithCustomMessage,

} from "../../utils/notificationHelper"

// Thêm một số icon và component để trang trí
const { Title, Text } = Typography
const { TabPane } = Tabs


const MedicalRecord = () => {
  const apiUrl = import.meta.env.VITE_API_BASE_URL;
  const [isModalInfoOpen, setIsModalInfoOpen] = useState(false)
  const [modalContent, setModalContent] = useState(null)
  const [modalButton, setmodalButton] = useState(false)
  const [medicalRecords, setMedicalRecords] = useState([]) // State to store medical records
  const [loading, setLoading] = useState(true) // Thêm state loading để hiển thị spinner

  const handleModalButtonClose = () => {
    setmodalButton(false)
  }

  const showButtonButtonModal = (userData) => {
    setModalContent(userData)
    setmodalButton(true)
  }

  const handleInfoCancel = () => {
    setIsModalInfoOpen(false)
    setModalContent(null)
  }

  const [messageApi, contextHolder] = message.useMessage()
  const customerId = localStorage.getItem("customerId")

  useEffect(() => {
    const handleDataMedicalRecord = async () => {
      try {
        setLoading(true) // Bắt đầu loading
        const response = await fetch(
          `${apiUrl}v1/customers/${customerId}/medicalRecords?page=1&size=10&sort=id&direction=asc`,

          {
            method: "GET",
            headers: {
              "Content-Type": "application/json",
              Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
            },

          },
        )
        if (!response.ok) {
          const errorText = await response.text()
          console.error(`Error fetching data: ${errorText || response.statusText}`)
          handleHttpStatusCode(response.status, "", `Lấy hồ sơ bệnh án thất bại: ${errorText || response.statusText}`)
          return
        }
        const data = await response.json()
        console.log("Raw API response:", data)
        if (data && data.data.content.length > 0) {
          const medicalRecords = data.data.content
            .filter((record) => {
              // Convert both to strings to avoid type mismatch
              const matches = String(record.customerId) === String(customerId)
              console.log(
                `Record customerId: ${record.customerId} (type: ${typeof record.customerId}), ` +
                  `Input customerId: ${customerId} (type: ${typeof customerId}), ` +
                  `Matches: ${matches}`,
              )
              return matches
            })
            .map((record) => ({
              id: record.id,
              barcode: record.barcode,
              patient: record.patient,
            }))
          if (medicalRecords.length > 0) {
            setMedicalRecords(medicalRecords)
            console.log("Updated medicalRecords state:", medicalRecords)
            notifySuccessWithCustomMessage("Lấy thông tin hồ sơ thành công", messageApi)
          } else {
            notifyErrorWithCustomMessage("Không có dữ liệu hồ sơ bệnh án", messageApi)
            console.log("No matching medical records found for customerId:", customerId)
          }
        }
      } catch (e) {
        notifyErrorWithCustomMessage("Lỗi kết nối khi cập nhật hồ sơ", messageApi)
        console.error("Error updating customer:", e)
      } finally {
        setLoading(false) // Kết thúc loading
      }
    }
    handleDataMedicalRecord()
  }, [])

  // Các nút chức năng cho modal
  const functionButtons = [
    {
      title: "THÔNG TIN HỒ SƠ",
      icon: <FileTextOutlined/>,
      onClick: () => {
        setIsModalInfoOpen(true)
        setmodalButton(false)
      },
    },
    {
      title: "XEM HỒ SƠ SỨC KHỎE",
      icon: <HeartOutlined />,
      onClick: () => setmodalButton(false),
    },
    {
      title: "XEM KẾT QUẢ CẬN LÂM SÀNG NGOẠI TRÚ",
      icon: <FileSearchOutlined />,
      onClick: () => setmodalButton(false),
    },
    {
      title: "XEM HÌNH ẢNH CHỤP",
      icon: <PictureOutlined />,
      onClick: () => setmodalButton(false),
    },
    {
      title: "XEM PHIẾU ĐĂNG KÝ KHÁM",
      icon: <FormOutlined />,
      onClick: () => setmodalButton(false),
    },
  ]

  return (
    <div className="flex w-full h-full">
      {/* Left section - 70% - Medical Records */}
      <div className="w-[70%] pr-4">
        <Card className="w-full shadow-md h-full" style={{ padding: "0px" }}>
          {/* Header with gradient */}
          <div className="bg-gradient-to-r from-blue-600 to-blue-800 p-6 rounded-t-lg text-white">
            <div className="flex items-center justify-between">
              <div className="flex items-center">
                <MedicineBoxOutlined className="text-3xl text-white mr-4" />
                <Title level={2} style={{color: "white", margin: 0}}>
                  Hồ sơ người bệnh
                </Title>
              </div>
              <Badge count={medicalRecords.length} overflowCount={99} className="mr-2">
                <Avatar size="large" icon={<UserOutlined />} className="bg-blue-300" />
              </Badge>
            </div>
            <Text style={{color: "white"}}>Quản lý thông tin y tế và lịch sử bệnh án</Text>
          </div>

          {/* IMPROVED: Better tab design with icons */}
          <Tabs defaultActiveKey="1" className="px-6 pt-4" type="card">
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
                        {/* IMPROVED: Better card design with left border accent */}
                        <Card
                          hoverable
                          className="w-full transform transition-all duration-300 hover:scale-[1.01] border-l-4 border-l-blue-600"
                          onClick={() => showButtonButtonModal(user)}
                        >
                          <div className="flex items-center justify-between">
                            <div className="flex items-center">
                              <Avatar size={48} icon={<UserOutlined />} className="bg-blue-600 mr-4" />
                              <div>
                                {/* IMPROVED: Better typography hierarchy */}
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
                            {/* IMPROVED: Better action button */}
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

          {/* IMPROVED: Better footer with helpful text */}
          <div className="p-4 bg-gray-50 mt-4 border-t border-gray-200">
            <Text type="secondary" className="flex items-center justify-center">
              <InfoCircleTwoTone className="mr-2" />
              Nhấn vào hồ sơ để xem chi tiết và sử dụng các chức năng khác
            </Text>
          </div>
        </Card>
      </div>

      {/* ADDED: Right section - 30% - Health Stats & Info */}
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

          {/* Health stats summary */}
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

          {/* Health tips */}
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

          {/* Quick actions */}
          <div>
            <Title level={5}>Thao tác nhanh</Title>
            <div className="grid grid-cols-2 gap-2">
              <Button icon={<FormOutlined />} className="text-left h-auto py-2">
                Đặt lịch khám
              </Button>
              <Button icon={<FileSearchOutlined />} className="text-left h-auto py-2">
                Xem kết quả
              </Button>
              <Button icon={<MedicineBoxOutlined />} className="text-left h-auto py-2">
                Đơn thuốc
              </Button>
              <Button icon={<PhoneOutlined />} className="text-left h-auto py-2">
                Liên hệ hỗ trợ
              </Button>
            </div>
          </div>
        </Card>
      </div>

      {/* Modal thông tin chi tiết */}
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

      {/* IMPROVED: Better function modal design */}
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
        closeIcon={<Button type="text" icon={<RightOutlined />} style={{color: "white", top:"22px", left :"-50px", position:"absolute"}} />}
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
      {contextHolder}
    </div>

  )
}

export default MedicalRecord


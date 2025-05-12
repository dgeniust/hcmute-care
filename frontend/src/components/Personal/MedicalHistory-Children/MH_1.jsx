import React, { useState, useEffect } from "react";
import {
  Button,
  Modal,
  Card,
  Empty,
  Spin,
  Tag,
  Skeleton,
  Divider,
  Timeline,
  Typography,
  message,
} from "antd";
import {
  CheckCircleOutlined,
  ClockCircleOutlined,
  CloseCircleOutlined,
  DollarOutlined,
  CalendarOutlined,
  UserOutlined,
  PhoneOutlined,
  MedicineBoxOutlined,
  NumberOutlined,
  EnvironmentOutlined,

  SearchOutlined
} from '@ant-design/icons';
import { handleHttpStatusCode, notifyErrorWithCustomMessage, notifySuccessWithCustomMessage } from '../../../utils/notificationHelper';

  SearchOutlined,
} from "@ant-design/icons";


const { Title, Text } = Typography;

/**
 * Transaction History Component
 * Displays a list of customer payment transactions and their details
 */
const TransactionHistory = () => {
  // State management
  const [transactions, setTransactions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [appointmentDetails, setAppointmentDetails] = useState(null);
  const [modalVisible, setModalVisible] = useState(false);
  const [selectedTransaction, setSelectedTransaction] = useState(null);
  const [modalLoading, setModalLoading] = useState(false);

  const [messageApi, contextHolder] = message.useMessage();
  
  // Get customer ID from local storage
  const customerId = localStorage.getItem('customerId');
  
  // Constants
  const API_BASE_URL = 'http://localhost:8080/api/';

  // Constants
  const apiUrl = import.meta.env.VITE_API_BASE_URL;
  const API_BASE_URL = `${apiUrl}v1`;
  // Fetch transactions data
  useEffect(() => {
    fetchTransactions();
  }, []);

  /**
   * Fetch transactions from API
   */
  const fetchTransactions = async () => {
    try {
      setLoading(true);
      const response = await fetch(
        `${API_BASE_URL}v1/customers/${customerId}/payments?paymentStatus=COMPLETED&page=1&size=10&sort=paymentDate&direction=desc`, 

        {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
          },
        }
      );

      if (!response.ok) {
        const errorText = await response.text();

        console.error('Error fetching transaction data:', errorText);
        notifyErrorWithCustomMessage('Không thể tải dữ liệu giao dịch', messageApi);

        setLoading(false);
        return;
      }

      const data = await response.json();

      if (data && data.data.content.length > 0) {
        setTransactions(data.data.content);
      } else {

        notifyErrorWithCustomMessage('No transaction data found', messageApi);
      }
    } catch (error) {
      console.error('Error fetching transaction data:', error);
      notifyErrorWithCustomMessage('Đã xảy ra lỗi khi tải dữ liệu giao dịch', messageApi);
    } finally {
      setLoading(false);
    }
  };

  /**
   * Fetch appointment details for a specific transaction
   * @param {string} appointmentId - The ID of the appointment
   */
  const fetchAppointmentDetails = async (appointmentId) => {
    console.log('Fetching appointment details for ID:', appointmentId);
    localStorage.setItem('appointmentId', appointmentId);
    try {
      setModalLoading(true);

      const response = await fetch(`${API_BASE_URL}v1/appointments/${appointmentId}`, {
        method: 'GET',                  
        headers: {
          'Content-Type': 'application/json',
        }
      );

      if (!response.ok) {
        const errorText = await response.text();

        console.error('Error fetching appointment data:', errorText);
        notifyErrorWithCustomMessage('Không thể tải thông tin lịch khám', messageApi);
        setModalLoading(false);
        return;
      }

      const data = await response.json();

      if (data && data.data) {
        setAppointmentDetails(data.data);
        console.log("Appointment details:", data.data);
        setModalVisible(true);
      } else {
        message.info("Không tìm thấy thông tin lịch khám");
      }
    } catch (error) {

      console.error('Error fetching appointment data:', error);
      notifyErrorWithCustomMessage('Đã xảy ra lỗi khi tải thông tin lịch khám', messageApi);
    } finally {
      setModalLoading(false);
    }
  };
  /**
   * Handle transaction selection
   * @param {Object} transaction - The selected transaction
   */
  const handleTransactionClick = (transaction) => {
    setSelectedTransaction(transaction);
    fetchAppointmentDetails(transaction.appointmentId);
  };

  /**
   * Format date to Vietnamese format
   * @param {string} dateString - The date string to format
   * @returns {string} - Formatted date
   */
  const formatDate = (dateString) => {
    if (!dateString) return "";

    const date = new Date(dateString);
    const day = date.getDate().toString().padStart(2, "0");
    const month = (date.getMonth() + 1).toString().padStart(2, "0");
    const year = date.getFullYear();
    const hours = date.getHours().toString().padStart(2, "0");
    const minutes = date.getMinutes().toString().padStart(2, "0");

    return `${day}/${month}/${year} ${hours}:${minutes}`;
  };

  /**
   * Format amount to Vietnamese currency
   * @param {number} amount - The amount to format
   * @returns {string} - Formatted amount
   */
  const formatAmount = (amount) => {
    return new Intl.NumberFormat("vi-VN", {
      style: "currency",
      currency: "VND",
      maximumFractionDigits: 0,
    }).format(amount);
  };

  /**
   * Get tag for payment status
   * @param {string} status - The payment status
   * @returns {JSX.Element} - Status tag
   */
  const getStatusTag = (status) => {
    switch (status) {
      case "COMPLETED":
        return (
          <Tag icon={<CheckCircleOutlined />} color="success">
            Hoàn thành
          </Tag>
        );
      case "PENDING":
        return (
          <Tag icon={<ClockCircleOutlined />} color="processing">
            Đang xử lý
          </Tag>
        );
      case "CANCELLED":
        return (
          <Tag icon={<CloseCircleOutlined />} color="error">
            Đã hủy
          </Tag>
        );
      case "CONFIRMED":
        return (
          <Tag icon={<CheckCircleOutlined />} color="blue">
            Đã xác nhận
          </Tag>
        );
      default:
        return <Tag color="default">{status}</Tag>;
    }
  };

  /**
   * Get color style for transaction card based on status
   * @param {string} status - The payment status
   * @returns {string} - CSS class names
   */
  const getStatusColor = (status) => {
    switch (status) {
      case "COMPLETED":
        return "bg-green-50 hover:bg-green-100 border-l-4 border-l-green-500 border-t border-r border-b border-green-200";
      case "PENDING":
        return "bg-blue-50 hover:bg-blue-100 border-l-4 border-l-blue-500 border-t border-r border-b border-blue-200";
      case "CANCELLED":
        return "bg-red-50 hover:bg-red-100 border-l-4 border-l-red-500 border-t border-r border-b border-red-200";
      default:
        return "bg-gray-50 hover:bg-gray-100 border-l-4 border-l-gray-500 border-t border-r border-b border-gray-200";
    }
  };

  /**
   * Get payment method display name
   * @param {string} method - The payment method
   * @returns {string} - Display name
   */
  const getPaymentMethod = (method) => {
    switch (method) {
      case "CREDIT_CARD":
        return "Thẻ tín dụng";
      case "BANK_TRANSFER":
        return "Chuyển khoản";
      case "CASH":
        return "Tiền mặt";
      case "MOMO":
        return "Ví MoMo";
      case "VNPAY":
        return "VNPay";
      default:
        return method;
    }
  };

  /**
   * Render loading state
   */
  if (loading && transactions.length === 0) {
    return (
      <div className="w-full h-full p-6">
        <Card title="Lịch sử giao dịch" className="w-full shadow-md">
          <div className="space-y-4">
            {[...Array(3)].map((_, index) => (
              <Skeleton key={index} active avatar paragraph={{ rows: 2 }} />
            ))}
          </div>
        </Card>
      </div>
    );
  }

  /**
   * Render empty state
   */
  if (transactions.length === 0) {
    return (
      <div className="w-full h-full p-6">
        <Card title="Lịch sử giao dịch" className="w-full shadow-md">
          <div className="w-full min-h-[400px] flex flex-col items-center justify-center">
            <Empty
              image={Empty.PRESENTED_IMAGE_SIMPLE}
              description={
                <div className="text-center">
                  <Text strong>Không có giao dịch nào</Text>
                  <p className="text-gray-500 mt-2">
                    Các giao dịch của bạn sẽ được hiển thị ở đây sau khi thanh
                    toán
                  </p>
                </div>
              }
            />
            <Button
              type="primary"
              icon={<SearchOutlined />}
              className="mt-4"
              onClick={() => message.info("Tính năng đang phát triển")}
            >
              Đặt lịch khám ngay
            </Button>
          </div>
        </Card>
      </div>
    );
  }

  return (
    <div className="w-full h-full p-6">
      <Card
        title={
          <div className="flex items-center">
            <DollarOutlined className="mr-2 text-blue-500" />
            <span>Lịch sử giao dịch</span>
          </div>
        }
        className="w-full shadow-md"
        extra={
          <Button
            type="primary"
            icon={<SearchOutlined />}
            onClick={() => fetchTransactions()}
          >
            Làm mới
          </Button>
        }
      >
        <div className="grid grid-cols-1 gap-4">
          {transactions.map((transaction) => (
            <div
              key={transaction.id}
              className={`p-4 rounded-lg cursor-pointer transition-all ${getStatusColor(
                transaction.paymentStatus
              )}`}
              onClick={() => handleTransactionClick(transaction)}
            >
              <div className="flex justify-between items-center">
                <div className="flex-1">
                  <div className="font-semibold text-lg flex items-center">
                    <DollarOutlined className="mr-2 text-blue-600" />
                    {formatAmount(transaction.amount)}
                  </div>
                  <div className="text-sm text-gray-600 mt-1">
                    <span className="font-medium">Mã GD:</span>{" "}
                    {transaction.transactionId || "N/A"}
                  </div>
                  {transaction.paymentMethod && (
                    <div className="text-sm text-gray-600 mt-1">
                      <span className="font-medium">Phương thức:</span>{" "}
                      {getPaymentMethod(transaction.paymentMethod)}
                    </div>
                  )}
                </div>
                <div className="text-right">
                  <div>{getStatusTag(transaction.paymentStatus)}</div>
                  <div className="text-sm text-gray-600 mt-1">
                    <CalendarOutlined className="mr-1" />
                    {formatDate(transaction.paymentDate)}
                  </div>
                </div>
              </div>
            </div>
          ))}
        </div>
      </Card>

      <Modal
        title={
          <div className="flex items-center">
            <MedicineBoxOutlined className="mr-2 text-blue-500" />
            <span>Chi tiết lịch khám</span>
          </div>
        }
        open={modalVisible}
        onCancel={() => setModalVisible(false)}
        footer={[
          <Button key="close" onClick={() => setModalVisible(false)}>
            Đóng
          </Button>,
        ]}
        width={700}
        styles={{ maxHeight: "80vh", overflow: "auto" }}
        destroyOnClose
      >
        {modalLoading ? (
          <Skeleton active paragraph={{ rows: 10 }} />
        ) : appointmentDetails ? (
          <div className="space-y-6">
            {/* Transaction Summary */}
            {selectedTransaction && (
              <Card className="bg-blue-50 border border-blue-200">
                <div className="flex justify-between items-center">
                  <Title level={4} className="m-0 text-blue-700">
                    Chi tiết thanh toán
                  </Title>
                  {getStatusTag(selectedTransaction.paymentStatus)}
                </div>
                <Divider className="my-3" />
                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <Text type="secondary">Số tiền:</Text>
                    <div className="font-bold text-xl text-blue-700">
                      {formatAmount(selectedTransaction.amount)}
                    </div>
                  </div>
                  <div>
                    <Text type="secondary">Ngày thanh toán:</Text>
                    <div>{formatDate(selectedTransaction.paymentDate)}</div>
                  </div>
                  <div>
                    <Text type="secondary">Mã giao dịch:</Text>
                    <div className="font-medium">
                      {selectedTransaction.transactionId || "N/A"}
                    </div>
                  </div>
                  <div>
                    <Text type="secondary">Phương thức:</Text>
                    <div>
                      {getPaymentMethod(selectedTransaction.paymentMethod)}
                    </div>
                  </div>
                </div>
              </Card>
            )}

            {/* Patient Information */}
            <Card
              title={
                <div className="flex items-center">
                  <UserOutlined className="mr-2 text-blue-500" />
                  <span>Thông tin bệnh nhân</span>
                </div>
              }
              className="border border-blue-200"
            >
              <div className="grid grid-cols-2 gap-4">
                <div>
                  <Text type="secondary">Họ và tên:</Text>
                  <div className="font-semibold">
                    {appointmentDetails.medicalRecord.patientName}
                  </div>
                </div>
                <div>
                  <Text type="secondary">Ngày sinh:</Text>
                  <div>{appointmentDetails.medicalRecord.dob}</div>
                </div>
                <div>
                  <Text type="secondary">Giới tính:</Text>
                  <div>
                    {appointmentDetails.medicalRecord.gender === "MALE"
                      ? "Nam"
                      : "Nữ"}
                  </div>
                </div>
                <div>
                  <Text type="secondary">Số điện thoại:</Text>
                  <div className="flex items-center">
                    <PhoneOutlined className="mr-1 text-blue-500" />
                    {appointmentDetails.medicalRecord.phone}
                  </div>
                </div>
              </div>
            </Card>

            {/* Appointment Information */}
            {appointmentDetails.tickets &&
              appointmentDetails.tickets.length > 0 && (
                <Card
                  title={
                    <div className="flex items-center">
                      <CalendarOutlined className="mr-2 text-green-500" />
                      <span>Thông tin lịch khám</span>
                    </div>
                  }
                  className="border border-green-200"
                >
                  {appointmentDetails.tickets.map((ticket) => (
                    <div className="space-y-4" key={ticket.id}>
                      <div className="flex justify-center mb-4">
                        <div className="text-center p-4 bg-blue-50 border border-blue-200 rounded-lg">
                          <Text type="secondary">Số thứ tự</Text>
                          <div className="font-bold text-3xl text-blue-600">
                            {ticket.waitingNumber}
                          </div>
                        </div>
                      </div>

                      <div className="grid grid-cols-2 gap-4">
                        <div>
                          <Text type="secondary">Mã phiếu khám:</Text>
                          <div className="font-semibold">
                            {ticket.ticketCode}
                          </div>
                        </div>
                        <div>
                          <Text type="secondary">Ngày khám:</Text>
                          <div className="font-semibold text-green-700">
                            {ticket.schedule.date}
                          </div>
                        </div>
                      </div>

                      <Divider className="my-3" />

                      <div>
                        <div className="flex items-center mb-1">
                          <MedicineBoxOutlined className="mr-2 text-blue-600" />
                          <Text strong>Bác sĩ phụ trách:</Text>
                        </div>
                        <div className="ml-6">
                          <div className="font-semibold">
                            {ticket.schedule.doctor.fullName}
                          </div>
                          <div className="text-gray-600 text-sm">
                            {ticket.schedule.doctor.qualification}
                          </div>
                        </div>
                      </div>

                      <div>
                        <div className="flex items-center mb-1">
                          <EnvironmentOutlined className="mr-2 text-red-500" />
                          <Text strong>Phòng khám:</Text>
                        </div>
                        <div className="ml-6 font-medium">
                          {ticket.schedule.roomDetail.name}
                        </div>
                      </div>
                    </div>
                  ))}
                </Card>
              )}

            {/* Appointment Timeline */}
            <Card
              title={
                <div className="flex items-center">
                  <ClockCircleOutlined className="mr-2 text-purple-500" />
                  <span>Trạng thái lịch khám</span>
                </div>
              }
              className="border border-purple-200"
            >
              <Timeline
                items={[
                  {
                    color:
                      appointmentDetails.tickets[0].status === "CONFIRMED" ||
                      appointmentDetails.tickets[0].status === "COMPLETED"
                        ? "blue"
                        : appointmentDetails.tickets[0].status === "PENDING"
                        ? "green"
                        : "red",
                    children: (
                      <div>
                        {appointmentDetails.tickets[0].status === "CONFIRMED" ||
                        appointmentDetails.tickets[0].status === "COMPLETED" ? (
                          <div className="font-semibold">
                            Đặt lịch thành công
                          </div>
                        ) : appointmentDetails.tickets[0].status ===
                          "PENDING" ? (
                          <div className="font-semibold">Chờ thanh toán</div>
                        ) : (
                          <div className="font-semibold">Hủy đặt lịch</div>
                        )}
                        <div className="text-gray-500 text-sm">
                          {formatDate(appointmentDetails.tickets[0].date)}
                        </div>
                      </div>
                    ),
                  },
                  {
                    color:
                      appointmentDetails.tickets[0].status === "CONFIRMED" ||
                      appointmentDetails.tickets[0].status === "COMPLETED"
                        ? "blue"
                        : "gray",
                    children: (
                      <div>
                        {appointmentDetails.tickets[0].status === "CONFIRMED" ||
                        appointmentDetails.tickets[0].status === "COMPLETED" ? (
                          <div className="font-semibold">
                            Thanh toán thành công
                          </div>
                        ) : (
                          <div className="font-semibold">Chưa thanh toán</div>
                        )}
                        <div className="text-gray-500 text-sm">
                          {selectedTransaction &&
                            formatDate(selectedTransaction.paymentDate)}
                        </div>
                      </div>
                    ),
                  },
                  {
                    color:
                      appointmentDetails.tickets[0].status === "COMPLETED"
                        ? "blue"
                        : appointmentDetails.tickets[0].status === "CONFIRMED"
                        ? "green"
                        : "gray",
                    children: (
                      <div>
                        <div className="font-semibold">
                          {appointmentDetails.tickets[0].status === "COMPLETED"
                            ? "Đã hoàn thành khám"
                            : appointmentDetails.tickets[0].status ===
                              "CONFIRMED"
                            ? "Chờ khám"
                            : "Hủy khám"}
                        </div>
                        {appointmentDetails.tickets[0].status ===
                          "COMPLETED" && (
                          <div className="text-gray-500 text-sm">
                            {formatDate(
                              appointmentDetails.tickets[0].schedule.date
                            )}
                          </div>
                        )}
                      </div>
                    ),
                  },
                ]}
              />
            </Card>
          </div>
        ) : (
          <Empty
            description="Không tìm thấy thông tin lịch khám"
            image={Empty.PRESENTED_IMAGE_SIMPLE}
          />
        )}
      </Modal>
      {contextHolder}
    </div>
  );
};

export default TransactionHistory;

import React, { useState, useEffect } from "react";
import { Modal, Tag, Pagination } from "antd";
import { formatDateTime } from "../../../utils/formatDate";

const TransactionTable = ({ isModalOpen, setIsModalOpen }) => {
  const apiUrl = import.meta.env.VITE_API_BASE_URL;
  const [payments, setPayments] = useState([]);
  const [currentPage, setCurrentPage] = useState(1);
  const [totalElements, setTotalElements] = useState(0);
  const [pageSize] = useState(10);

  const fetchPayments = async (page) => {
    try {
      const response = await fetch(
        `${apiUrl}v1/payments/all?page=${page}&size=${pageSize}&sort=id&direction=asc`,
        {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
          },
        }
      );
      const data = await response.json(); // Parse JSON
      setPayments(data.data.content);
      setTotalElements(data.data.totalElements);
    } catch (error) {
      console.error("Error fetching payments:", error);
    }
  };

  useEffect(() => {
    fetchPayments(currentPage);
  }, [currentPage]);

  const handleOk = () => {
    setIsModalOpen(false);
  };

  const handleCancel = () => {
    setIsModalOpen(false);
  };

  const handlePageChange = (page) => {
    setCurrentPage(page);
  };

  const headersModal = [
    "Số Hiệu",
    "Phương Thức",
    "Thời Gian",
    "Trạng Thái",
    "Tổng Tiền",
    "Mã Giao Dịch",
    "Mã Cuộc Hẹn",
  ];

  return (
    <>
      <table className="w-full divide-y divide-gray-200">
        <thead className="bg-gray-50">
          <tr>
            {headersModal.map((header) => (
              <th
                key={header}
                className="p-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
              >
                {header}
              </th>
            ))}
          </tr>
        </thead>
        <tbody className="bg-white divide-y divide-gray-200">
          {payments.slice(0, 4).map((transaction) => (
            <tr
              key={transaction.id}
              className={`${
                transaction.paymentStatus === "COMPLETED"
                  ? "bg-green-200"
                  : transaction.paymentStatus === "PENDING"
                  ? "bg-blue-200"
                  : transaction.paymentStatus === "CANCELLED"
                  ? "bg-yellow-200"
                  : "bg-red-200"
              }`}
            >
              <td className="p-3 whitespace-nowrap">{transaction.id}</td>
              <td className="p-3 whitespace-nowrap">
                {transaction.paymentMethod}
              </td>
              <td className="p-3 whitespace-nowrap">
                {formatDateTime(transaction.paymentDate)}
              </td>
              <td className="p-3 whitespace-nowrap">
                {transaction.paymentStatus === "COMPLETED" ? (
                  <Tag color="green">Hoàn Thành</Tag>
                ) : transaction.paymentStatus === "PENDING" ? (
                  <Tag color="blue">Đang Xử Lý</Tag>
                ) : transaction.paymentStatus === "CANCELLED" ? (
                  <Tag color="gold">Hủy</Tag>
                ) : (
                  <Tag color="red">Thất Bại</Tag>
                )}
              </td>
              <td className="p-3 whitespace-nowrap">
                {transaction.amount.toLocaleString()} VND
              </td>
              <td className="p-3 whitespace-nowrap">
                {transaction.transactionId}
              </td>
              <td className="p-3 whitespace-nowrap">
                {transaction.appointmentId}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
      <Modal
        title="Danh Sách Thanh Toán"
        open={isModalOpen}
        onOk={handleOk}
        onCancel={handleCancel}
        width={1000}
        style={{ top: 20 }}
      >
        <table className="min-w-full divide-y divide-gray-200">
          <thead className="bg-gray-50">
            <tr>
              {headersModal.map((header) => (
                <th
                  key={header}
                  className="p-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                >
                  {header}
                </th>
              ))}
            </tr>
          </thead>
          <tbody className="bg-white divide-y divide-gray-200">
            {payments.map((payment) => (
              <tr key={payment.id}>
                <td className="p-3 whitespace-nowrap">{payment.id}</td>
                <td className="p-3 whitespace-nowrap">
                  {payment.paymentMethod}
                </td>
                <td className="p-3 whitespace-nowrap">
                  {formatDateTime(payment.paymentDate)}
                </td>
                <td className="p-3 whitespace-nowrap">
                  {payment.paymentStatus === "COMPLETED" ? (
                    <Tag color="green">Hoàn Thành</Tag>
                  ) : payment.paymentStatus === "PENDING" ? (
                    <Tag color="blue">Đang Xử Lý</Tag>
                  ) : payment.paymentStatus === "CANCELLED" ? (
                    <Tag color="gold">Hủy</Tag>
                  ) : (
                    <Tag color="red">Thất Bại</Tag>
                  )}
                </td>
                <td className="p-3 whitespace-nowrap">
                  {payment.amount.toLocaleString()} VND
                </td>
                <td className="p-3 whitespace-nowrap">
                  {payment.transactionId}
                </td>
                <td className="p-3 whitespace-nowrap">
                  {payment.appointmentId}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
        <div className="mt-4 flex justify-end">
          <Pagination
            current={currentPage}
            total={totalElements}
            pageSize={pageSize}
            onChange={handlePageChange}
            showSizeChanger={false}
          />
        </div>
      </Modal>
    </>
  );
};

export default TransactionTable;

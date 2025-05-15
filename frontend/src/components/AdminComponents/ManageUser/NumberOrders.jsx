import React, { useState, useEffect } from "react";
import { Input, Pagination, Tag, Spin } from "antd";
import dayjs from "dayjs";

const { Search } = Input;

const NumberOrders = ({ tickets }) => {
  const headers = [
    "STT",
    "Barcode",
    "Tên khách hàng",
    "Số phiếu",
    "Ngày sinh",
    "Số khám",
    "Phòng",
  ];

  const getAreaTag = (room) => {
    if (room.includes("Khu A")) return <Tag color="red">{room}</Tag>;
    if (room.includes("Khu B")) return <Tag color="green">{room}</Tag>;
    if (room.includes("Khu C")) return <Tag color="blue">{room}</Tag>;
    return <Tag color="yellow">{room}</Tag>;
  };

  // Map API data to table format
  const tableData = tickets.map((ticket) => ({
    barcode: ticket.medicalRecord.barcode,
    name: ticket.medicalRecord.patient.name,
    ticketNumber: ticket.ticketCode || "N/A",
    dob: dayjs(ticket.medicalRecord.patient.dob).format("DD/MM/YYYY"),
    waitingNumber: ticket.waitingNumber,
    room: ticket.schedule.roomDetail.name,
  }));

  // State for search, pagination, and loading
  const [searchService, setSearchService] = useState("");
  const [filteredUsers, setFilteredUsers] = useState([]);
  const [currentPage, setCurrentPage] = useState(1);
  const [itemsPerPage] = useState(10);
  const [isLoading, setIsLoading] = useState(true);

  // Sync filteredUsers with tableData and add 1s loading delay
  useEffect(() => {
    setIsLoading(true);
    const timer = setTimeout(() => {
      setFilteredUsers(tableData);
      setIsLoading(false);
    }, 1000);
    return () => clearTimeout(timer);
  }, [tickets]);

  // Handle search
  const handleSearch = (value) => {
    setSearchService(value);
    const filtered = tableData.filter((data) =>
      data.name.toLowerCase().includes(value.toLowerCase())
    );
    setFilteredUsers(filtered);
    setCurrentPage(1);
  };

  // Pagination
  const indexOfLastItem = currentPage * itemsPerPage;
  const indexOfFirstItem = indexOfLastItem - itemsPerPage;
  const currentItems = filteredUsers.slice(indexOfFirstItem, indexOfLastItem);

  const handlePageChange = (page) => {
    setCurrentPage(page);
  };

  return (
    <div className="w-full h-fit space-y-4">
      <Search
        placeholder="Tìm kiếm số phiếu 🔍"
        style={{ width: "100%", marginTop: "20px" }}
        value={searchService}
        onChange={(e) => handleSearch(e.target.value)}
        size="large"
      />
      {isLoading ? (
        <div className="flex justify-center items-center h-64">
          <Spin size="large" />
        </div>
      ) : (
        <>
          <table className="min-w-full divide-y divide-gray-200">
            <thead className="bg-gray-50">
              <tr>
                {headers.map((header) => (
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
              {currentItems.map((row, index) => (
                <tr key={index}>
                  <td className="p-3 whitespace-nowrap">{index + 1 + indexOfFirstItem}</td>
                  <td className="p-3 whitespace-nowrap">{row.barcode}</td>
                  <td className="p-3 whitespace-nowrap">{row.name}</td>
                  <td className="p-3 whitespace-nowrap">{row.ticketNumber}</td>
                  <td className="p-3 whitespace-nowrap">{row.dob}</td>
                  <td className="p-3 whitespace-nowrap">{row.waitingNumber}</td>
                  <td className="p-3 whitespace-nowrap">{getAreaTag(row.room)}</td>
                </tr>
              ))}
            </tbody>
          </table>
          <Pagination
            align="center"
            current={currentPage}
            total={filteredUsers.length}
            pageSize={itemsPerPage}
            onChange={handlePageChange}
          />
        </>
      )}
    </div>
  );
};

export default NumberOrders;
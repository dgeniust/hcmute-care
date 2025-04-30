import React, { useState, useEffect } from "react";
import { Pagination, Drawer, Divider, Collapse, theme, Input, message } from "antd";
import { MenuFoldOutlined, MoreOutlined, CaretRightOutlined } from "@ant-design/icons";
import { notifyErrorWithCustomMessage } from "../../../utils/notificationHelper";
import MainLoading from "../../../components/MainLoading"; // Adjust path as needed

const { Search } = Input;

const UserAccounts = () => {
  const [messageApi, contextHolder] = message.useMessage();
  const [users, setUsers] = useState([]);
  const [filteredUsers, setFilteredUsers] = useState([]);
  const [totalItems, setTotalItems] = useState(0);
  const [currentPage, setCurrentPage] = useState(1);
  const [itemsPerPage, setItemsPerPage] = useState(10);
  const [searchService, setSearchService] = useState("");
  const [loading, setLoading] = useState(false);
  const [sortField, setSortField] = useState("id");
  const [sortDirection, setSortDirection] = useState("asc");
  const [dataUser, setDataUser] = useState(null);
  const [open, setOpen] = useState(false);

  // Fetch users from API with 3-second minimum loading
  const fetchUsers = async (page, size, sort, direction, search = "") => {
    setLoading(true);
    const startTime = Date.now();

    try {
      const accessToken = localStorage.getItem("accessToken");
      const url = `http://localhost:8080/api/v1/customers?page=${page}&size=${size}&sort=${sort}&direction=${direction}${
        search ? `&search=${encodeURIComponent(search)}` : ""
      }`;
      const response = await fetch(url, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${accessToken}`,
        },
      });

      if (!response.ok) {
        const errorText = await response.text();
        notifyErrorWithCustomMessage(`Lỗi khi lấy danh sách người dùng: ${errorText || response.statusText}`, messageApi);
        return;
      }

      const data = await response.json();
      console.log("API response:", data);

      const userData = data.data.content || data.data || [];
      const total = data.data.totalElements || userData.length;

      // Ensure minimum 3-second loading
      const elapsedTime = Date.now() - startTime;
      const remainingTime = 500 - elapsedTime;
      if (remainingTime > 0) {
        await new Promise((resolve) => setTimeout(resolve, remainingTime));
      }

      setUsers(userData);
      setFilteredUsers(userData);
      setTotalItems(total);
    } catch (error) {
      notifyErrorWithCustomMessage("Lỗi kết nối khi lấy danh sách người dùng", messageApi);
      console.error("Error fetching users:", error);
    } finally {
      setLoading(false);
    }
  };

  // Fetch users on mount and when pagination/search/sort changes
  useEffect(() => {
    fetchUsers(currentPage, itemsPerPage, sortField, sortDirection, searchService);
  }, [currentPage, itemsPerPage, sortField, sortDirection, searchService]);

  // Search handling
  const handleSearch = (value) => {
    setSearchService(value);
    setCurrentPage(1);
    // Local filtering (uncomment server-side search if API supports it)
    const filtered = users.filter((user) =>
      user.fullName && user.fullName.toLowerCase().includes(value.toLowerCase())
    );
    setFilteredUsers(filtered);
    setTotalItems(filtered.length);
    setCurrentPage(1);
  };

  // Pagination handling
  const handlePageChange = (page, pageSize) => {
    setCurrentPage(page);
    setItemsPerPage(pageSize);
  };

  // Drawer handling
  const showDrawerUser = (data) => {
    setOpen(true);
    setDataUser(data);
  };

  const onClose = () => {
    setOpen(false);
    setDataUser(null);
  };

  // Collapse for medical records
  const { token } = theme.useToken();
  const panelStyle = {
    marginBottom: 12,
    background: token.colorFillAlter,
    borderRadius: token.borderRadiusLG,
    border: "none",
  };

  const getItems = (medicalRecords, panelStyle) => {
    return medicalRecords.map((record, index) => ({
      key: index.toString(),
      label: `Hồ sơ ${index + 1} - ${record.patientName}`,
      children: (
        <div className="grid grid-flow-row grid-cols-2 gap-4">
          <div className="grid grid-flow-row grid-cols-[100px_200px]">
            <p>Mã vạch:</p>
            <p>{record.barcode}</p>
          </div>
          <div className="grid grid-flow-row grid-cols-[100px_200px]">
            <p>Tên bệnh nhân:</p>
            <p>{record.patientName}</p>
          </div>
          <div className="grid grid-flow-row grid-cols-[100px_200px]">
            <p>Ngày sinh:</p>
            <p>{record.patientDOB}</p>
          </div>
          <div className="grid grid-flow-row grid-cols-[100px_200px]">
            <p>Số điện thoại:</p>
            <p>{record.patientPhone}</p>
          </div>
          <div className="grid grid-flow-row grid-cols-[100px_200px]">
            <p>Giới tính:</p>
            <p>{record.patientGender}</p>
          </div>
          <div className="grid grid-flow-row grid-cols-[100px_200px]">
            <p>Địa chỉ:</p>
            <p>{record.patientAddress}</p>
          </div>
        </div>
      ),
      style: panelStyle,
    }));
  };

  const headers = [
    "STT",
    "Tên khách hàng",
    "Email",
    "Số điện thoại",
    "Giới tính",
    "Địa chỉ",
    <MenuFoldOutlined />,
  ];

  return (
    <>
      {contextHolder}
      <Search
        placeholder="Tìm kiếm khách hàng 🔍"
        style={{ width: "100%" }}
        value={searchService}
        onChange={(e) => setSearchService(e.target.value)}
        onSearch={handleSearch}
        size="large"
        disabled={loading} // Disable search during loading
      />
      <table className="w-full divide-y divide-gray-200 mb-4">
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
          {loading ? (
            <tr>
              <td colSpan={headers.length} >
              <div className="flex justify-center items-center text-center max-w-[140px] max-h-full mx-auto my-auto">
                <MainLoading />
              </div>
              </td>
            </tr>
          ) : filteredUsers.length === 0 ? (
            <tr>
              <td colSpan={headers.length} className="p-3 text-center">
                Không tìm thấy người dùng
              </td>
            </tr>
          ) : (
            filteredUsers.map((user) => (
              <tr key={user.id}>
                <td className="p-3 whitespace-nowrap">{user.id}</td>
                <td className="p-3 whitespace-nowrap">{user.fullName}</td>
                <td className="p-3 whitespace-nowrap">{user.email}</td>
                <td className="p-3 whitespace-nowrap">{user.phone}</td>
                <td className="p-3 whitespace-nowrap">{user.gender === "MALE" ? "Nam" : "Nữ"}</td>
                <td className="p-3 whitespace-nowrap">{user.address}</td>
                <td
                  className="p-3 whitespace-nowrap cursor-pointer"
                  onClick={() => showDrawerUser(user)}
                >
                  <MoreOutlined />
                </td>
              </tr>
            ))
          )}
        </tbody>
      </table>
      <Pagination
        align="center"
        current={currentPage}
        defaultCurrent={1}
        total={totalItems}
        pageSize={itemsPerPage}
        onChange={handlePageChange}
        showSizeChanger
        pageSizeOptions={["10", "20", "50"]}
        disabled={loading} // Disable pagination during loading
      />
      <Drawer
        title="Thông tin cá nhân"
        placement="right"
        size="large"
        onClose={onClose}
        open={open}
      >
        <div className="w-full h-full">
          <div className="w-full border border-black h-[150px] personal-bg relative">
            <Divider
              orientation="left"
              plain
              style={{ position: "absolute", bottom: 0, width: "100%" }}
            >
              <div className="w-[150px] h-[150px] rounded-full bg-white absolute border border-black top-1/2 left-1/6 transform -translate-x-1/2 -translate-y-1/3">
                <img
                  src={`${
                    dataUser && dataUser.gender === "MALE"
                      ? "https://api.dicebear.com/7.x/miniavs/svg?seed=8"
                      : "https://api.dicebear.com/9.x/miniavs/svg?seed=Liliana"
                  }`}
                  alt=""
                  className="object-center w-full h-full rounded-full"
                />
                <div className="text-black font-bold text-xl mt-4 text-center w-full">
                  <h1>{dataUser ? dataUser.fullName : ""}</h1>
                </div>
              </div>
            </Divider>
          </div>
          <div className="w-full h-fit mt-[140px] px-8 py-4 rounded-lg shadow-lg">
            {dataUser && (
              <div className="flex flex-col space-y-4">
                <div className="grid grid-flow-row grid-cols-2">
                  <div className="grid grid-flow-row grid-cols-[100px_200px]">
                    <p>Họ và tên: </p>
                    <p>{dataUser.fullName}</p>
                  </div>
                  <div className="grid grid-flow-row grid-cols-[100px_200px]">
                    <p>Giới tính: </p>
                    <p>{dataUser.gender === "MALE" ? "Nam" : "Nữ"}</p>
                  </div>
                </div>
                <div className="grid grid-flow-row grid-cols-[100px_300px]">
                  <p>Địa chỉ: </p>
                  <p>{dataUser.address}</p>
                </div>
                <div className="grid grid-flow-row grid-cols-[100px_200px]">
                  <p>Email: </p>
                  <p>{dataUser.email}</p>
                </div>
                <div className="grid grid-flow-row grid-cols-2">
                  <div className="grid grid-flow-row grid-cols-[100px_200px]">
                    <p>Số điện thoại: </p>
                    <p>{dataUser.phone}</p>
                  </div>
                </div>
                <div className="grid grid-flow-row grid-cols-2">
                  <div className="grid grid-flow-row grid-cols-[100px_200px]">
                    <p>Quốc tịch: </p>
                    <p>{dataUser.nation}</p>
                  </div>
                </div>
              </div>
            )}
          </div>
          {dataUser && Array.isArray(dataUser.medicalRecords) && dataUser.medicalRecords.length > 0 && (
            <div className="w-full h-fit mt-4 p-4 rounded-lg shadow-lg">
              <h1 className="font-bold text-base p-4">Hồ sơ bệnh án</h1>
              <Collapse
                bordered={false}
                expandIcon={({ isActive }) => <CaretRightOutlined rotate={isActive ? 90 : 0} />}
                style={{ background: token.colorBgContainer }}
                items={dataUser && dataUser.medicalRecords ? getItems(dataUser.medicalRecords, panelStyle) : []}
              />
            </div>
          )}
        </div>
      </Drawer>
    </>
  );
};

export default UserAccounts;
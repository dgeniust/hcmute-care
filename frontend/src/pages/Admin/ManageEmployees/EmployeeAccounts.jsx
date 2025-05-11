import React, { useState, useEffect } from "react";
import {
  Pagination,
  Drawer,
  Divider,
  Button,
  Input,
  Tag,
  Select,
  Tabs,
  message,
} from "antd";
import { MoreOutlined } from "@ant-design/icons";
import MedalProfessor, {
  Medal2,
  Medal3,
  Medal4,
  Medal5,
  College,
} from "./SVGEmployee";

const { Search } = Input;
const { TabPane } = Tabs; // For older versions of antd; use Tabs.TabPane if needed

const EmployeeAccounts = () => {
  const apiUrl = import.meta.env.VITE_API_BASE_URL;
  const [messageApi, contextHolder] = message.useMessage();
  const [activeRole, setActiveRole] = useState("DOCTOR"); // Default role
  const [users, setUsers] = useState([]);
  const [filteredUsers, setFilteredUsers] = useState([]);
  const [totalItems, setTotalItems] = useState(0);
  const [currentPage, setCurrentPage] = useState(1);
  const [itemsPerPage, setItemsPerPage] = useState(10);
  const [sortField] = useState("id"); // Fixed as per API
  const [sortDirection] = useState("asc"); // Fixed as per API
  const [loading, setLoading] = useState(false);

  // Search and Filter States
  const [searchService, setSearchService] = useState("");

  // Fetch users from API based on role
  const fetchUsers = async (role, page, size, sort, direction) => {
    setLoading(true);
    try {
      const accessToken = localStorage.getItem("accessToken");
      const url = `${apiUrl}v1/users?role=${role}&page=${page}&size=${size}&sort=${sort}&direction=${direction}`;
      const response = await fetch(url, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${accessToken}`,
        },
      });

      if (!response.ok) {
        const errorText = await response.text();
        messageApi.error(
          `Lỗi khi lấy danh sách nhân sự: ${errorText || response.statusText}`
        );
        return;
      }

      const data = await response.json();
      console.log(`API response for role ${role}:`, data);

      const userData = data.data.content || data.data || [];
      const total = data.data.totalElements || userData.length;

      setUsers(userData);
      setFilteredUsers(userData);
      setTotalItems(total);
    } catch (error) {
      messageApi.error("Lỗi kết nối khi lấy danh sách nhân sự");
      console.error("Error fetching users:", error);
    } finally {
      setLoading(false);
    }
  };

  // Fetch users when role, page, or itemsPerPage changes
  useEffect(() => {
    fetchUsers(activeRole, currentPage, itemsPerPage, sortField, sortDirection);
  }, [activeRole, currentPage, itemsPerPage]);

  // Handle tab change
  const handleTabChange = (key) => {
    setActiveRole(key);
    setCurrentPage(1); // Reset to page 1 when switching tabs
    setSearchService(""); // Reset search
  };

  // Search and Filter Logic
  const handleSearch = (value) => {
    setSearchService(value);
    filterUsers();
  };
  const filterUsers = () => {
    let filtered = users.filter((user) => {
      const searchMatch =
        !searchService ||
        (user.fullName &&
          user.fullName.toLowerCase().includes(searchService.toLowerCase()));

      return searchMatch;
    });

    setFilteredUsers(filtered);
    setCurrentPage(1);
  };

  useEffect(() => {
    filterUsers();
  }, [searchService]);

  // Pagination
  const handlePageChange = (page, pageSize) => {
    setCurrentPage(page);
    setItemsPerPage(pageSize);
  };

  // Drawer
  const [dataUser, setDataUser] = useState(null);
  const [open, setOpen] = useState(false);

  const showDrawerUser = (data) => {
    setOpen(true);
    setDataUser(data);
  };

  const onClose = () => {
    setOpen(false);
    setDataUser(null);
  };

  //   const headers = ["Tên nhân sự", "Học vị", "Vai trò", "Số điện thoại", "Email", "Trạng thái", ""];
  const headers = ["Tên nhân sự", "Số điện thoại", "Email", "Trạng thái"];

  return (
    <div className="flex flex-col space-y-4">
      {contextHolder}
      <Tabs activeKey={activeRole} onChange={handleTabChange}>
        <TabPane tab="Bác sĩ" key="DOCTOR" />
        <TabPane tab="Y tá" key="NURSE" />
        <TabPane tab="Nhân viên" key="STAFF" />
        <TabPane tab="Quản trị viên" key="ADMIN" />
      </Tabs>
      <div className="flex flex-row justify-between items-center w-full">
        <Search
          placeholder="Tìm kiếm nhân sự "
          style={{ width: "100%" }}
          value={searchService}
          onChange={(e) => handleSearch(e.target.value)}
          size="large"
        />
      </div>
      <table className="w-full divide-y divide-gray-200 mb-4 table-fixed border border-slate-200 rounded-xl">
        <thead className="bg-gray-50">
          <tr>
            {headers.map((header, index) => (
              <th
                key={header}
                className={`p-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider ${
                  index === 0 ? "w-1/4" : index === 4 ? "w-1/8" : "w-1/6"
                }`}
              >
                {header}
              </th>
            ))}
          </tr>
        </thead>
        <tbody className="bg-white divide-y divide-gray-200">
          {loading ? (
            <tr>
              <td colSpan={headers.length} className="p-3 text-center">
                Đang tải...
              </td>
            </tr>
          ) : filteredUsers.length === 0 ? (
            <tr>
              <td colSpan={headers.length} className="p-3 text-center">
                Không tìm thấy nhân sự
              </td>
            </tr>
          ) : (
            filteredUsers.map((user) => (
              <tr key={user.id} className="hover:bg-slate-50">
                {user.gender === "MALE" ? (
                  <td className="p-3 whitespace-nowrap">
                    <div className="flex flex-row items-center space-x-2">
                      <img
                        src="https://api.dicebear.com/7.x/miniavs/svg?seed=8"
                        alt="avatar"
                        className="object-center w-[40px] h-[40px] rounded-full border border-slate-200"
                      />
                      <p>{user.fullName}</p>
                    </div>
                  </td>
                ) : (
                  <td className="p-3 whitespace-nowrap">
                    <div className="flex flex-row items-center space-x-2">
                      <img
                        src="https://api.dicebear.com/9.x/miniavs/svg?seed=Liliana"
                        alt="avatar"
                        className="object-center w-[40px] h-[40px] rounded-full border border-slate-200"
                      />
                      <p>{user.fullName}</p>
                    </div>
                  </td>
                )}
                {/* Học vị */}
                {/* <td className="p-3 whitespace-nowrap">
                  <p className="items-center flex flex-row space-x-2">
                    {
                      {
                        "Giáo sư": <MedalProfessor />,
                        "Phó giáo sư": <Medal2 />,
                        "Tiến sĩ": <Medal3 />,
                        "Thạc sĩ": <Medal4 />,
                        "Cử nhân": <Medal5 />,
                        "Đại học": <College />,
                      }[user.education] ?? <span>{user.education}</span>
                    }
                    <span>{user.education}</span>
                  </p>
                </td> */}
                {/* Vị trí trong bệnh viện */}
                {/* {user.career === "Bác sĩ" ? (
                  <td className="p-3 whitespace-nowrap">
                    <div className="w-fit h-fit px-2 rounded-xl bg-[#273c75] text-white">{user.career}</div>
                  </td>
                ) : user.career === "Y tá" ? (
                  <td className="p-3 whitespace-nowrap">
                    <div className="w-fit h-fit px-2 rounded-xl bg-pink-300 text-white">{user.career}</div>
                  </td>
                ) : user.career === "Hậu cần" ? (
                  <td className="p-3 whitespace-nowrap">
                    <div className="w-fit h-fit px-2 rounded-xl bg-yellow-200 text-black">{user.career}</div>
                  </td>
                ) : user.career === "Bảo vệ" ? (
                  <td className="p-3 whitespace-nowrap">
                    <div className="w-fit h-fit px-2 rounded-xl bg-black text-white">{user.career}</div>
                  </td>
                ) : (
                  <td className="p-3 whitespace-nowrap">
                    <div className="w-fit h-fit px-2 rounded-xl bg-white text-black">{user.career}</div>
                  </td>
                )} */}
                <td className="p-3 whitespace-nowrap">{user.phone}</td>
                <td className="p-3 whitespace-nowrap">{user.email}</td>
                <td className="p-3 whitespace-nowrap cursor-pointer space-x-2">
                  <Button
                    style={{ borderRadius: "8px" }}
                    onClick={() => showDrawerUser(user)}
                  >
                    Profile
                  </Button>
                  <MoreOutlined onClick={() => showDrawerUser(user)} />
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
                    <p>{dataUser.gender}</p>
                  </div>
                </div>
                <div className="grid grid-flow-row grid-cols-[100px_300px]">
                  <p>Địa chỉ: </p>
                  <p>{dataUser.specificAddress}</p>
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
                  <div className="grid grid-flow-row grid-cols-[100px_200px]">
                    <p>Quốc gia: </p>
                    <p>{dataUser.country}</p>
                  </div>
                </div>
                <div className="grid grid-flow-row grid-cols-2">
                  <div className="grid grid-flow-row grid-cols-[100px_200px]">
                    <p>Quốc tịch: </p>
                    <p>{dataUser.nation}</p>
                  </div>
                  <div className="grid grid-flow-row grid-cols-[100px_200px]">
                    <p>Nghề nghiệp: </p>
                    <p>{dataUser.career}</p>
                  </div>
                </div>
              </div>
            )}
          </div>
        </div>
      </Drawer>
    </div>
  );
};

export default EmployeeAccounts;

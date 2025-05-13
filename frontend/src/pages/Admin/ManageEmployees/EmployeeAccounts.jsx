import React, { useState, useEffect } from "react";
import {
  Pagination,
  Drawer,
  Button,
  Input,
  Tabs,
  message,
  Table,
  Avatar,
  Badge,
  Space,
  Spin,
  Empty,
  Card,
  Typography,
  Divider,
  Row,
  Col,
  Tag
} from "antd";
import { 
  SearchOutlined,
  UserOutlined,
  PhoneOutlined,
  MailOutlined,
  EyeOutlined,
  IdcardOutlined,
  HomeOutlined,
  GlobalOutlined,
  ManOutlined,
  WomanOutlined
} from "@ant-design/icons";

const { Title, Text } = Typography;

const EmployeeAccounts = () => {
  const apiUrl = import.meta.env.VITE_API_BASE_URL;
  const [messageApi, contextHolder] = message.useMessage();
  const [activeRole, setActiveRole] = useState("DOCTOR");
  const [users, setUsers] = useState([]);
  const [totalItems, setTotalItems] = useState(0);
  const [currentPage, setCurrentPage] = useState(1);
  const [pageSize, setPageSize] = useState(10);
  const [loading, setLoading] = useState(false);
  const [searchValue, setSearchValue] = useState("");
  const [employeeDetail, setEmployeeDetail] = useState(null);
  const [drawerVisible, setDrawerVisible] = useState(false);
  const [loadingDetail, setLoadingDetail] = useState(false);

  // Function to fetch users based on role and pagination
  const fetchUsers = async () => {
    setLoading(true);
    try {
      const accessToken = localStorage.getItem("accessToken");
      const url = `${apiUrl}v1/users?role=${activeRole}&page=${currentPage}&size=${pageSize}&sort=id&direction=asc`;
      
      const response = await fetch(url, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${accessToken}`,
        },
      });

      if (!response.ok) {
        const errorText = await response.text();
        messageApi.error(`Lỗi khi lấy danh sách nhân sự: ${errorText || response.statusText}`);
        return;
      }

      const data = await response.json();
      const userData = data.data.content || data.data || [];
      const total = data.data.totalElements || userData.length;

      setUsers(userData);
      setTotalItems(total);
    } catch (error) {
      messageApi.error("Lỗi kết nối khi lấy danh sách nhân sự");
      console.error("Error fetching users:", error);
    } finally {
      setLoading(false);
    }
  };


  // Load users when component mounts or when dependencies change
  useEffect(() => {
    fetchUsers();
  }, [activeRole, currentPage, pageSize]);

  // Handle tab change
  const handleTabChange = (key) => {
    setActiveRole(key);
    setCurrentPage(1);
    setSearchValue("");
  };

  // Handle search input change
  const handleSearchChange = (e) => {
    setSearchValue(e.target.value);
  };

  // Handle search button click
  const handleSearch = () => {
    setCurrentPage(1);
    fetchUsers();
  };

  // Handle view employee detail
  const handleViewEmployee = (record) => {
    setEmployeeDetail(record);
    setDrawerVisible(true);
  };

  // Handle drawer close
  const handleDrawerClose = () => {
    setDrawerVisible(false);
    setEmployeeDetail(null);
  };

  // Filter users based on search value
  const filteredUsers = users.filter(user => 
    user.fullName?.toLowerCase().includes(searchValue.toLowerCase()) || 
    user.email?.toLowerCase().includes(searchValue.toLowerCase()) ||
    user.phone?.includes(searchValue)
  );

  // Define table columns
  const columns = [
    {
      title: 'Tên nhân sự',
      dataIndex: 'fullName',
      key: 'fullName',
      render: (text, record) => (
        <div className="flex items-center space-x-3">
          <Avatar 
            size={40} 
            src={record.gender === "MALE" 
              ? "https://api.dicebear.com/7.x/miniavs/svg?seed=8" 
              : "https://api.dicebear.com/9.x/miniavs/svg?seed=Liliana"} 
            icon={<UserOutlined />} 
          />
          <div className="ml-2">
            <div className="font-medium">{text}</div>
            <div className="text-xs text-gray-500">{record.roleName || activeRole}</div>
          </div>
        </div>
      ),
    },
    {
      title: 'Số điện thoại',
      dataIndex: 'phone',
      key: 'phone',
      render: (text) => (
        <div className="flex items-center">
          <PhoneOutlined className="mr-2 text-blue-500" />
          <span>{text}</span>
        </div>
      ),
    },
    {
      title: 'Email',
      dataIndex: 'email',
      key: 'email',
      render: (text) => (
        <div className="flex items-center">
          <MailOutlined className="mr-2 text-blue-500" />
          <span>{text}</span>
        </div>
      ),
    },
    {
      title: 'Trạng thái',
      key: 'status',
      render: () => (
        <Badge status="success" text="Đang hoạt động" />
      ),
    },
    {
      title: 'Thao tác',
      key: 'action',
      render: (_, record) => (
        <Button 
          type="primary" 
          icon={<EyeOutlined />} 
          onClick={() => handleViewEmployee(record)}
          className="bg-blue-500 hover:bg-blue-600"
        >
          Xem chi tiết
        </Button>
      ),
    },
  ];

  // Define role labels for tabs
  const roleLabels = {
    DOCTOR: "Bác sĩ",
    NURSE: "Y tá",
    STAFF: "Nhân viên",
    ADMIN: "Quản trị viên"
  };

  // Define employee detail content
  const renderEmployeeDetail = () => {
    if (loadingDetail) {
      return (
        <div className="flex justify-center items-center h-full">
          <Spin size="large" />
        </div>
      );
    }

    if (!employeeDetail) {
      return (
        <Empty description="Không có dữ liệu" />
      );
    }

    return (
      <div className="p-4">
        <div className="bg-gradient-to-r from-blue-500 to-purple-500 h-48 rounded-lg relative mb-20">
          <div className="absolute -bottom-16 left-10">
            <Avatar 
              size={120} 
              src={employeeDetail.gender === "MALE" 
                ? "https://api.dicebear.com/7.x/miniavs/svg?seed=8" 
                : "https://api.dicebear.com/9.x/miniavs/svg?seed=Liliana"} 
              icon={<UserOutlined />}
              className="border-4 border-white shadow-lg" 
            />
          </div>
          <div className="absolute bottom-4 right-10 text-white">
            <Tag color={employeeDetail.gender === "MALE" ? "blue" : "pink"} className="px-3 py-1">
              {employeeDetail.gender === "MALE" ? (
                <><ManOutlined /> Nam</>
              ) : (
                <><WomanOutlined /> Nữ</>
              )}
            </Tag>
          </div>
        </div>

        <Title level={3} className="mt-4">{employeeDetail.fullName}</Title>
        <Tag color="green" className="mb-4">
          {roleLabels[activeRole]}
        </Tag>

        <Card className="mt-4">
          <Title level={4}>Thông tin cá nhân</Title>
          <Divider />
          
          <Row gutter={[16, 16]}>
            <Col span={12}>
              <Space>
                <IdcardOutlined />
                <Text strong>Mã nhân viên:</Text>
                <Text>{employeeDetail.id}</Text>
              </Space>
            </Col>
            <Col span={12}>
              <Space>
                <PhoneOutlined />
                <Text strong>Số điện thoại:</Text>
                <Text>{employeeDetail.phone}</Text>
              </Space>
            </Col>
            <Col span={12}>
              <Space>
                <MailOutlined />
                <Text strong>Email:</Text>
                <Text>{employeeDetail.email}</Text>
              </Space>
            </Col>
            <Col span={12}>
              <Space>
                <UserOutlined />
                <Text strong>Ngày sinh:</Text>
                <Text>{employeeDetail.dob}</Text>
              </Space>
            </Col>
            {employeeDetail.specificAddress && (
              <Col span={24}>
                <Space>
                  <HomeOutlined />
                  <Text strong>Địa chỉ:</Text>
                  <Text>{employeeDetail.specificAddress}</Text>
                </Space>
              </Col>
            )}
            {employeeDetail.country && (
              <Col span={12}>
                <Space>
                  <GlobalOutlined />
                  <Text strong>Quốc gia:</Text>
                  <Text>{employeeDetail.country}</Text>
                </Space>
              </Col>
            )}
            {employeeDetail.nation && (
              <Col span={12}>
                <Space>
                  <GlobalOutlined />
                  <Text strong>Quốc tịch:</Text>
                  <Text>{employeeDetail.nation}</Text>
                </Space>
              </Col>
            )}
            {employeeDetail.career && (
              <Col span={12}>
                <Space>
                  <UserOutlined />
                  <Text strong>Nghề nghiệp:</Text>
                  <Text>{employeeDetail.career}</Text>
                </Space>
              </Col>
            )}
          </Row>
        </Card>
      </div>
    );
  };

  return (
    <div className="bg-white rounded-lg shadow-sm p-6">
      {contextHolder}
      
      <div className="mb-6">
        <Tabs activeKey={activeRole} onChange={handleTabChange}>
          {Object.entries(roleLabels).map(([key, label]) => (
            <Tabs.TabPane tab={label} key={key} />
          ))}
        </Tabs>
      </div>
      
      <div className="mb-6">
        <Input.Search
          placeholder="Tìm kiếm theo tên, email hoặc số điện thoại"
          value={searchValue}
          onChange={handleSearchChange}
          onSearch={handleSearch}
          enterButton
          size="large"
          prefix={<SearchOutlined />}
          className="w-full max-w-xl"
        />
      </div>
      
      <Table
        columns={columns}
        dataSource={filteredUsers}
        loading={loading}
        rowKey="id"
        pagination={{
          current: currentPage,
          pageSize: pageSize,
          total: totalItems,
          onChange: (page, size) => {
            setCurrentPage(page);
            setPageSize(size);
          },
          showSizeChanger: true,
          pageSizeOptions: ['10', '20', '50'],
          showTotal: (total) => `Tổng ${total} nhân sự`,
        }}
        className="shadow-sm rounded-lg"
      />
      
      <Drawer
        title={<span className="text-lg font-semibold">Thông tin chi tiết nhân viên</span>}
        width={800}
        placement="right"
        onClose={handleDrawerClose}
        open={drawerVisible}
        style={{ padding: 0 }}
      >
        {renderEmployeeDetail()}
      </Drawer>
    </div>
  );
};

export default EmployeeAccounts;
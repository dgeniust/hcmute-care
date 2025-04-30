import React, {useEffect, useState} from "react";
import {Layout, Menu, message} from 'antd';

import {
    UserOutlined, LaptopOutlined, NotificationOutlined, UserSwitchOutlined, MessageOutlined, FileTextOutlined, SnippetsOutlined, SafetyCertificateOutlined, LogoutOutlined, AuditOutlined, ReconciliationOutlined, ProfileOutlined, ForkOutlined, PhoneOutlined, BellOutlined, BuildOutlined,
    TableOutlined,UsergroupAddOutlined,ApartmentOutlined,
    ShakeOutlined
} from '@ant-design/icons';
import { useNavigate } from "react-router-dom";
import { notifyErrorWithCustomMessage, handleHttpStatusCode } from '../utils/notificationHelper';
const {Sider} = Layout
const SideBar = () => { 
    const navigate = useNavigate();

    const [userFullName, setUserFullName] = useState(localStorage.getItem('userFullName') || 'User');
    const [userDetails, setUserDetails] = useState(null); // State cho dữ liệu từ API thứ hai
    const [loading, setLoading] = useState(false); // Trạng thái loading
    const [messageApi, contextHolder] = message.useMessage(); // Để dùng notifyErrorWithCustomMessage
    const role = localStorage.getItem('roles') || 'ROLE_CUSTOMER';

    let userDetailsData = null;
    const fetchUserData = async (accId, accessToken) => {
      try{
        const response = await fetch(`http://localhost:8080/api/v1/accounts/${accId}`, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${accessToken}`,
          }
        });
        if (response.status === 400) {
          localStorage.clear();
          notifyErrorWithCustomMessage('Phiên đăng nhập hết hạn, vui lòng đăng nhập lại', messageApi);
          navigate('/login');
          return null;
        }
        console.log('fetchUserData response status:', response.status); // Debug
        if (!response.ok) {
          const errorText = await response.text();
          console.log('fetchUserData error response:', errorText); // Debug
          handleHttpStatusCode(
            response.status,
            '',
            `Lấy thông tin người dùng thất bại: ${errorText || response.statusText}`,
            messageApi
          );
          return null;
        }
        const {data} = await response.json();
        console.log('fetchUserData response data:', data); // Debug
        if (
          !data ||
          !data.userId ||
          !Number.isInteger(Number(data.userId)) ||
          !data.userFullName
        ){
          notifyErrorWithCustomMessage("Dữ liệu người dùng không hợp lệ hoặc thiếu thông tin", messageApi);
          return null;
        }
        return data;
      }
      catch (error) {
        notifyErrorWithCustomMessage('Lỗi kết nối khi lấy thông tin người dùng', messageApi);
        console.error('Error fetching user details:', error);
        return null;
      }
    }
    const fetchUserDetails = async (customerId, accessToken) => {
      try {
        let url;
        if (role === "ROLE_ADMIN") {
          return { gender: "MALE" };
        }
        switch (role) {
          case 'ROLE_DOCTOR':
            url = `http://localhost:8080/api/v1/doctors/${customerId}`;
            break;
          case 'ROLE_NURSE':
            url = `http://localhost:8080/api/v1/nurses/${customerId}`;
            break;
          case 'ROLE_STAFF':
            url = `http://localhost:8080/api/v1/staff/${customerId}`;
            break;
          default:
            url = `http://localhost:8080/api/v1/customers/${customerId}`;
        }

        const response = await fetch(url, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${accessToken}`,
          },
        });
        if (response.status === 400) {
          localStorage.clear();
          notifyErrorWithCustomMessage('Phiên đăng nhập hết hạn, vui lòng đăng nhập lại', messageApi);
          navigate('/login');
          return null;
        }
        console.log('fetchCustomerDetails response status:', response.status); // Debug
        if (!response.ok) {
          const errorText = await response.text();
          console.log('fetchCustomerDetails error response:', errorText); // Debug
          handleHttpStatusCode(
            response.status,
            '',
            `Lấy chi tiết khách hàng thất bại: ${errorText || response.statusText}`,
            messageApi
          );
          return null;
        }
  
        const { data } = await response.json();
        console.log('fetchCustomerDetails response data:', data); // Debug
        if (!data) {
          notifyErrorWithCustomMessage('Không tìm thấy chi tiết khách hàng', messageApi);
          return null;
        }
        return data;
      } catch (error) {
        notifyErrorWithCustomMessage('Lỗi kết nối khi lấy chi tiết người dùng', messageApi);
        console.error('Error fetching user details:', error);
        return null;
      }
    };
    
    useEffect(() => {
      const handleFetchData = async () => {
        setLoading(true);
        const accId = localStorage.getItem('userId');
        const accessToken = localStorage.getItem('accessToken');
        if (!accId || !accessToken) {
          notifyErrorWithCustomMessage('Thiếu thông tin đăng nhập', messageApi);
          setLoading(false);
          return;
        }

        // Gọi API đầu tiên với accId và accessToken
        const userData = await fetchUserData(accId, accessToken);
        if (!userData) {
          setLoading(false);
          return;
        }
        // Cập nhật state và localStorage từ API đầu tiên
        setUserFullName(userData.userFullName);
        localStorage.setItem('customerId', userData.userId);
        localStorage.setItem('userFullName', userData.userFullName);


        const cusId = localStorage.getItem('customerId');
        // Gọi API thứ hai với customerId
        const customerDetailsData = await fetchUserDetails(cusId, accessToken);
        if (customerDetailsData) {
          setUserDetails(customerDetailsData);
          localStorage.setItem('customerDetailsData', JSON.stringify(customerDetailsData));
          localStorage.setItem('gender', customerDetailsData.gender);
          var roles = customerDetailsData.roles;
        }
        setLoading(false);
      }
      handleFetchData();

    }, []);

    const handleLogout = () => {
      localStorage.clear();
    }
    
    const menu_customer = [
      {
        key: 'sub1',
        label: 'Cá nhân',
        icon: <UserOutlined />,
        children: [
          { key: 'personal-profile', label: 'Thông tin cá nhân', icon: <UserSwitchOutlined /> },
          { key: 'medical-records', label: 'Hồ sơ sức khỏe', icon: <AuditOutlined /> },
          { key: 'medical-history', label: 'Lịch sử đặt khám', icon: <ProfileOutlined /> },
          { key: 'regulation-use', label: 'Quy định sử dụng', icon: <SnippetsOutlined /> },
          { key: 'subnav5', label: 'Chính sách bảo mật', icon: <SafetyCertificateOutlined /> },
          {
            key: 'login',
            label: 'Đăng xuất',
            icon: <LogoutOutlined />,
            onClick: ()=>{
              handleLogout();
            }
          }
        ]
      },
      {
        key: 'sub2',
        label: 'Dịch vụ',
        icon: <LaptopOutlined />,
        children: [
          { key: 'booking', label: 'Khám theo chuyên khoa', icon: <UserSwitchOutlined /> },
          { key: 'subnav8', label: 'Khám theo ngày', icon: <MessageOutlined /> },
          { key: 'subnav9', label: 'Khám theo bác sĩ', icon: <FileTextOutlined /> },
          { key: 'subnav10', label: 'Lịch khám bệnh', icon: <SnippetsOutlined /> },
          { key: 'subnav11', label: 'Lịch sử Đặt khám', icon: <SafetyCertificateOutlined /> }
        ]
      },
      { key: 'sub3', label: 'Hỗ trợ', icon: <MessageOutlined /> },
      { key: 'terms-service', label: 'Điều khoản dịch vụ', icon: <ReconciliationOutlined /> },
      { key: 'service-list', label: 'Bảng giá dịch vụ kỹ thuật', icon: <ForkOutlined /> },
      { key: 'sub6', label: 'Hướng dẫn khách hàng', icon: <NotificationOutlined /> },
      { key: 'notification-event', label: 'Tin tức - Sự kiện', icon: <BellOutlined /> },
      { key: 'sub8', label: 'Liên hệ', icon: <PhoneOutlined /> },
      { key: 'calculate', label: 'Tính BMI, BMR', icon: <BuildOutlined /> }
    ];

    const menu_nurse = [
      {
        key: 'nurse',
        label: 'Y tá',
        icon: <UserSwitchOutlined />,
        children: [
          { key: 'nurse-schedule', label: 'Lịch làm việc', icon: <SnippetsOutlined /> },
          { key: 'patient-care', label: 'Chăm sóc bệnh nhân', icon: <AuditOutlined /> },
          {
            key: 'login',
            label: 'Đăng xuất',
            icon: <LogoutOutlined />,
            onClick: ()=>{
              handleLogout();
            }
          }
        ]
      }
    ];
    const menu_doctor = [
      {
        key: '',
        label: 'Bảng điều khiển',
        icon: <UserOutlined />,
      },
      {
        key: 'records',
        label: 'Hồ sơ bệnh án',
        icon: <UsergroupAddOutlined />,
      },
      {
        key: 'patient',
        label: 'Hồ sơ bệnh nhân',
        icon: <UsergroupAddOutlined />,
      },
      {
          key: 'schedule',
          label: 'Lịch làm việc',
          icon: <UsergroupAddOutlined />,
      },
      {
        key: 'list-service',
        label: 'Bảng giá dịch vụ',
        icon: <ForkOutlined />,
      },
      {
        key: 'manage-chat',
        label: 'Hỗ trợ',
        icon: <MessageOutlined />,
      },
      {
        key: 'manage-contact',
        label: 'Liên hệ',
        icon: <PhoneOutlined />,
      },
      {
        key: 'login',
        label: 'Đăng xuất',
        icon: <NotificationOutlined />,
        onClick: ()=>{
          handleLogout();
        }
      },
    ];
    const menu_admin = [
      {
        key: '',
        label: 'Bảng điều khiển',
        icon: <UserOutlined />,
      },
      {
        key: 'sub2',
        label: 'Quản lý người dùng',
        icon: <UsergroupAddOutlined />,
        children: [
          {
            key: 'manage-users',
            label: 'Danh sách người dùng',
            icon: <UserSwitchOutlined />
          },
          {
            key: 'manage-numbers',
            label: 'Quản lý phiếu khám bệnh',
            icon: <ShakeOutlined />
          },
        ]
      },
      {
        key: 'sub3',
        label: 'Quản lý nhân sự',
        icon: <ApartmentOutlined />,
        children: [
          {
            key: 'manage-employee',
            label: 'Danh sách nhân sự',
            icon: <UserSwitchOutlined />
          },
          {
            key: 'manage-schedule',
            label: 'Lịch trình làm việc',
            icon: <TableOutlined />
          },
          {
            key: 'manage-rooms',
            label: 'Quản lý phòng ban',
            icon: <FileTextOutlined />
          },
          {
            key: 'manage-items',
            label: 'Quản lý thiết bị',
            icon: <SnippetsOutlined />
          },
          {
            key: 'manage-support',
            label: 'Điều phối nhân viên hỗ trợ khẩn cấp',
            icon: <SafetyCertificateOutlined />
          }
        ]
      },
      {
        key: 'terms-service',
        label: 'Điều khoản dịch vụ',
        icon: <ReconciliationOutlined />,
      },
      {
        key: 'manage-service',
        label: 'Bảng giá dịch vụ',
        icon: <ForkOutlined />,
      },
      {
        key: 'regulation-use',
        label: 'Quy định sử dụng',
        icon: <SnippetsOutlined />,
      },
      {
        key: 'manage-chat',
        label: 'Hỗ trợ',
        icon: <MessageOutlined />,
      },
      {
        key: 'manage-contact',
        label: 'Liên hệ',
        icon: <PhoneOutlined />,
      },
      {
        key: 'login',
        label: 'Đăng xuất',
        icon: <NotificationOutlined />,
        onClick: ()=>{
          handleLogout();
        }
      },
    ];

    const menu_staff = [
      {
        key: 'staff',
        label: 'Quản lý thông tin, sự kiện',
        icon: <BuildOutlined />,
        children: [
          { key: 'manage-events', label: 'Quản lý sự kiện', icon: <BellOutlined /> },
          { key: 'manage-info', label: 'Quản lý thông tin', icon: <FileTextOutlined /> },
          {
            key: 'login',
            label: 'Đăng xuất',
            icon: <LogoutOutlined />,
            onClick: ()=>{
              handleLogout();
            }
          }
        ]
      }
    ];
    const getMenuItems = () => {
      switch (role) {
        case 'ROLE_CUSTOMER':
          return menu_customer;
        case 'ROLE_ADMIN':
          return menu_admin;
        case 'ROLE_DOCTOR':
          return menu_doctor;
        case 'ROLE_NURSE':
          return menu_nurse;
        case 'ROLE_STAFF':
          return menu_staff;
        default:
          return [];
      }
    };
    const getNavigationPath = (key) => {
      if (key === 'login') {
        return '/login';
      }
      switch (role) {
        case 'ROLE_DOCTOR':
          return `/doctor/${key}`;
        case 'ROLE_ADMIN':
          return `/admin/${key}`;
        case 'ROLE_NURSE':
          return `/nurse/${key}`;
        case 'ROLE_STAFF':
          return `/staff/${key}`;
        case 'ROLE_CUSTOMER':
        default:
          return `/${key}`;
      }
    };
    const gender = localStorage.getItem('gender')
    return <Sider
        style={{
            backgroundColor: 'white',
            height: 'fit-content',
            top: '0',
        }}
        width={250}
        >
        <div className="flex flex-row items-center justify-center space-x-2">
          <div className=" flex items-center justify-center">
            {
              gender && gender === "MALE" ? (
                <img src="https://api.dicebear.com/7.x/miniavs/svg?seed=8" alt="" 
                width={50}
                height={50}
                className="object-center" />
              ) : (
                <img src="https://api.dicebear.com/9.x/miniavs/svg?seed=Liliana" alt=""
                width={50}
                height={50}
                className="object-center" />
              )
            }
          </div>
          <div>
            <h1 className="text-black font-bold">{userFullName}</h1>
            {
              role && role === "ROLE_CUSTOMER" ? (
                <span className="text-red-600">Khách hàng</span>
              ) : role && role === "ROLE_ADMIN" ? (
                <span className="text-red-600">Quản trị viên 🟥</span>
              ) : role && role === "ROLE_DOCTOR" ? (
                <span className="text-red-600">Bác sĩ 🟦</span>
              ) : role && role === "ROLE_STAFF" ? (
                <span className="text-red-600">Nhân viên 🟨</span>
              ) : null
            }
          </div>
        </div>
        <Menu
            mode="inline"
            defaultSelectedKeys={['1']}
            defaultOpenKeys={['sub1']}
            style={{
            height: '100%',
            }}
            items={getMenuItems()}
            onClick={(e) => navigate(getNavigationPath(e.key))}
        />
        {contextHolder}
    </Sider>
};

export default SideBar;
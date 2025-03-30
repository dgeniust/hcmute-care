import React from "react";
import {Layout, Menu} from 'antd';
import {
    UserOutlined, NotificationOutlined, UserSwitchOutlined, MessageOutlined, FileTextOutlined, SnippetsOutlined, SafetyCertificateOutlined, UsergroupAddOutlined, ApartmentOutlined, ReconciliationOutlined, TableOutlined, ForkOutlined, PhoneOutlined, BellOutlined, ShakeOutlined
} from '@ant-design/icons';
import { useNavigate } from "react-router-dom";
const {Sider} = Layout
const AdminSideBar = () => { 
    const navigate = useNavigate();
    
      const menu_items2 = [
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
          key: 'manage-posts',
          label: 'Tin tức - Sự kiện',
          icon: <BellOutlined />,
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
          key: 'logout',
          label: 'Đăng xuất',
          icon: <NotificationOutlined />,
        },
      ]
    return <Sider
        style={{
            backgroundColor: 'white',
            height: 'fit-contetnt',
            top: '0',
        }}
        width={250}
        >
        <div className="flex flex-row items-center justify-center space-x-2">
          <div className=" flex items-center justify-center">
            <img src="https://api.dicebear.com/7.x/miniavs/svg?seed=8" alt="" srcset="" 
            width={50}
            height={50}
            className="object-center" />
          </div>
          <div className="flex flex-col items-center justify-center">
            <h1 className="text-black font-bold">Nguyễn Thành Đạt</h1>
            <span className="text-red-600">ADMIN 🎟️</span>
          </div>
        </div>
        <Menu
            mode="inline"
            defaultSelectedKeys={['1']}
            defaultOpenKeys={['sub1']}
            style={{
            height: '100%',
            }}
            items={menu_items2}
            onClick={(e) => navigate(`/admin/${e.key}`)}
        />
    </Sider>
};

export default AdminSideBar;
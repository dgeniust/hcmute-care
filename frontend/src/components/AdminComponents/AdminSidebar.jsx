import React, { useState } from "react";
import {Layout, Menu} from 'antd';
import {
    UserOutlined, NotificationOutlined, UserSwitchOutlined, MessageOutlined, FileTextOutlined, SnippetsOutlined, SafetyCertificateOutlined, UsergroupAddOutlined, ApartmentOutlined, ReconciliationOutlined, TableOutlined, ForkOutlined, PhoneOutlined, BellOutlined, ShakeOutlined
} from '@ant-design/icons';

const {Sider} = Layout
const AdminSideBar = ({handleChangePage}) => { 

    const onHandleChangePage = (page) => {
      handleChangePage(page.key);
      console.log('click123', page.key);
    }
    
      const menu_items2 = [
        {
          key: 'main',
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
              key: 'subnav2',
              label: 'Lịch trình làm việc',
              icon: <TableOutlined />
            },
            {
              key: 'manage-rooms',
              label: 'Quản lý phòng ban',
              icon: <FileTextOutlined />
            },
            {
              key: 'subnav4',
              label: 'Quản lý thiết bị',
              icon: <SnippetsOutlined />
            },
            {
              key: 'subnav5',
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
          key: 'service-list',
          label: 'Bảng giá dịch vụ',
          icon: <ForkOutlined />,
        },
        
        {
          key: 'manage-posts',
          label: 'Tin tức - Sự kiện',
          icon: <BellOutlined />,
        },
        {
          key: 'sub7',
          label: 'Quy định sử dụng',
          icon: <SnippetsOutlined />,
        },
        {
          key: 'sub8',
          label: 'Hỗ trợ',
          icon: <MessageOutlined />,
        },
        {
          key: 'sub9',
          label: 'Liên hệ',
          icon: <PhoneOutlined />,
        },
        {
          key: 'sub6',
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
            onClick={onHandleChangePage}
        />
    </Sider>
};

export default AdminSideBar;
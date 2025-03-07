import React, { useState } from "react";
import { Link } from "react-router-dom";
import {Layout, Menu, Avatar , theme, Card } from 'antd';
import {
    UserOutlined, LaptopOutlined, NotificationOutlined, UserSwitchOutlined, MessageOutlined, FileTextOutlined, SnippetsOutlined, SafetyCertificateOutlined, LogoutOutlined, AuditOutlined, ReconciliationOutlined, ProfileOutlined, ForkOutlined, PhoneOutlined, BellOutlined
} from '@ant-design/icons';

const {Sider} = Layout
const SideBar = () => { 
    
      const menu_items2 = [
        {
          key: 'sub1',
          label: 'Cá nhân',
          icon: <UserOutlined />,
          children: [
            {
              key: 'subnav1',
              label: 'Thông tin cá nhân',
              icon: <UserSwitchOutlined />
            },
            {
              key: 'subnav2',
              label: 'Hỗ trợ',
              icon: <MessageOutlined />
            },
            {
              key: 'subnav3',
              label: 'Điều khoản dịch vụ',
              icon: <ReconciliationOutlined />
            },
            {
              key: 'subnav4',
              label: 'Quy định sử dụng',
              icon: <SnippetsOutlined />
            },
            {
              key: 'subnav5',
              label: 'Chính sách bảo mật',
              icon: <SafetyCertificateOutlined />
            },
            {
              key: 'subnav6',
              label: 'Đăng xuất',
              icon: <LogoutOutlined />
            }
          ]
        },
        {
          key: 'sub2',
          label: 'Dịch vụ',
          icon: <LaptopOutlined />,
          children: [
            {
              key: 'subnav7',
              label: 'Khám chuyên khoa',
              icon: <UserSwitchOutlined />
            },
            {
              key: 'subnav8',
              label: 'Khám theo ngày',
              icon: <MessageOutlined />
            },
            {
              key: 'subnav9',
              label: 'Khám theo bác sĩ',
              icon: <FileTextOutlined />
            },
            {
              key: 'subnav10',
              label: 'Lịch khám bệnh',
              icon: <SnippetsOutlined />
            },
            {
              key: 'subnav11',
              label: 'Lịch sử Đặt khám',
              icon: <SafetyCertificateOutlined />
            }
          ]
        },
        {
          key: 'sub3',
          label: 'Hồ sơ sức khỏe',
          icon: <AuditOutlined />,
        },
        {
          key: 'sub4',
          label: 'Lịch sử đặt khám',
          icon: <ProfileOutlined />,
        },
        {
          key: 'sub5',
          label: 'Bảng giá dịch vụ kỹ thuật',
          icon: <ForkOutlined />,
        },
        {
          key: 'sub6',
          label: 'Hướng dẫn khách hàng',
          icon: <NotificationOutlined />,
        },
        {
          key: 'sub7',
          label: 'Tin tức - Sự kiện',
          icon: <BellOutlined />,
        },
        {
          key: 'sub8',
          label: 'Liên hệ',
          icon: <PhoneOutlined />,
        }
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
          <div>
            <h1 className="text-black font-bold">Nguyễn Thành Đạt</h1>
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
        />
    </Sider>
};

export default SideBar;
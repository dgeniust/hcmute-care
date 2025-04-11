import React from "react";
import {Layout, Menu} from 'antd';
import {
    UserOutlined, NotificationOutlined, UsergroupAddOutlined, ForkOutlined, PhoneOutlined, MessageOutlined
} from '@ant-design/icons';
import { useNavigate } from "react-router-dom";
const {Sider} = Layout
const DoctorSideBar = () => { 
    const navigate = useNavigate();
    
      const menu_items2 = [
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
            <img src="https://api.dicebear.com/7.x/miniavs/svg?seed=8" alt="" srcSet="" 
            width={50}
            height={50}
            className="object-center" />
          </div>
          <div className="flex flex-col items-center justify-center">
            <h1 className="text-black font-bold">Nguyễn Thành Đạt</h1>
            <span className="text-blue-600">DOCTOR 🟦</span>
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
            onClick={(e) => navigate(`/doctor/${e.key}`)}
        />
    </Sider>
};

export default DoctorSideBar;
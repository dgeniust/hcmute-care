import React, { useState } from "react";
import { Link } from "react-router-dom";
import {Layout, Menu, Dropdown, Space, Button } from 'antd';
import {
  DownOutlined, UpOutlined, MenuUnfoldOutlined, MenuFoldOutlined
} from '@ant-design/icons';
const { Header  } = Layout;
const HeaderNavbar = () => {
  const [isHover, setIsHover] = useState(false);
  const items = [
    {
      key: 'sub1',
      label: 'Đặt khám',
      children: [
        {
          key: '1',
          label: (
            <a target="_blank" rel="noopener noreferrer" href="https://www.antgroup.com">
              Khám chuyên khoa
            </a>
          ),
        },
        {
          key: '2',
          label: (
            <a target="_blank" rel="noopener noreferrer" href="https://www.aliyun.com">
              Khám theo ngày
            </a>
          ),
        },
        {
          key: '3',
          label: (
            <a target="_blank" rel="noopener noreferrer" href="https://www.luohanacademy.com">
              Khám theo bác sĩ
            </a>
          ),
        },
        {
          key: '4',
          label: (
            <a target="_blank" rel="noopener noreferrer" href="https://www.luohanacademy.com">
              Lịch khám bệnh
            </a>
          ),
        },
        {
          key: '5',
          label: (
            <a target="_blank" rel="noopener noreferrer" href="https://www.luohanacademy.com">
              Lịch sử Đặt khám
            </a>
          ),
        },
      ]
    }
    
  ]
  const menu_items = [
    {
      key: 'sub2',
      label: 'Hồ sơ sức khỏe',
    },
    {
      key: 'sub2',
      label: 'Hồ sơ sức khỏe',

    },
    {
      key: 'sub2',
      label: 'Hồ sơ sức khỏe',

    },
    {
      key: 'sub2',
      label: 'Hồ sơ sức khỏe',

    },
  ]
  const handleMouseEnter = () => {
    setIsHover(true);
  };

  const handleMouseLeave = () => {
    setIsHover(false);
  };
  return (
    <Header
          style={{
            display: 'flex',
            alignItems: 'center',
            width: '100%',backgroundColor: 'white',
            height: '7vh',
          }}
        >
          <Button
            type="text"
            icon={collapsed ? <MenuUnfoldOutlined/> : <MenuFoldOutlined />}
            onClick={() => setCollapsed(!collapsed)}
            style={{
              fontSize: '16px',
              width: 64,
              height: 64,
              color: 'white'
            }}
          />
          <Menu
          theme="light"
          mode="horizontal"
          defaultSelectedKeys={['2']}
          items={menu_items}
          style={{  
            minWidth: 0,
            backgroundColor: 'transparent',
          }}
           className="custom-menu"
        />
        <Dropdown
        onMouseEnter={handleMouseEnter}
        onMouseLeave={handleMouseLeave}
          menu={{
            items,
          }}
        >
          <a onClick={(e) => e.preventDefault()}>
            <Space>
              Hover me
              {
                isHover ? <UpOutlined /> : <DownOutlined />
              }
                
            </Space>
          </a>
        </Dropdown>
        </Header>
  );
};

export default HeaderNavbar;

import React, { useState } from "react";
import { Link } from "react-router-dom";
import {Layout, Menu, Dropdown, Button, Modal } from 'antd';
import { Canvas } from "@react-three/fiber";
import { Suspense } from "react";
import { Experience } from "./Sliding Book/Experience";
import { UI } from "./Sliding Book/UI";
import {
  CaretDownOutlined, CaretUpOutlined
} from '@ant-design/icons';
import logo from '../assets/logo_hcmute_care.png';
const { Header  } = Layout;
const HeaderNavbar = () => {
  const [position, setPosition] = useState('end');
  const [isHover, setIsHover] = useState(false);
  const [isModalOpen, setIsModalOpen] = useState(false); // Sử dụng 'open' thay vì 'visible'

  const handleOpenBook = () => {
    setIsModalOpen(true); // Mở modal khi nhấn nút "Hỗ trợ"
  };

  const handleCancel = () => {
    setIsModalOpen(false); // Đóng modal khi nhấn nút "Cancel"
  };
  const items1 = [
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
  const items = [
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
  ];
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
  const handleMouseEnter = (e) => {
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
          justifyContent: 'space-around',
          backgroundColor: 'white',
          boxShadow: 'rgba(100, 100, 111, 0.2) 0px 7px 29px 0px',
        }}
      >
      <div className="w-fit flex items-center justify-center">
        <img src={logo} alt="" width="70px" height="70px" />
      </div>
      <div className="flex flex-row justify-between h-full w-[80vw]">
        <Dropdown
        menu={{
          items,
        }}
        placement="bottom"
        arrow
        >
        <Button onMouseEnter={handleMouseEnter} onMouseLeave={handleMouseLeave} 
        style={{backgroundColor: isHover ?'#f5f6fa' : 'transparent', color: 'black', fontWeight: 'bold', boxShadow: 'none',border: 'none', height: "100%"}} icon={isHover ? <CaretDownOutlined /> : <CaretUpOutlined />} iconPosition={position}>Đặt khám</Button>
        </Dropdown>
        <Button 
        style={{color: 'black', fontWeight: 'bold', boxShadow: 'none',border: 'none', height: "100%", marginRight: '15px'}}>Thanh toán viện phí, đơn thuốc</Button>
        <Button 
        style={{color: 'black', fontWeight: 'bold', boxShadow: 'none',border: 'none', height: "100%", marginRight: '15px'}}>Đặt lịch uống thuốc</Button>
        <Button 
        style={{color: 'black', fontWeight: 'bold', boxShadow: 'none',border: 'none', height: "100%", marginRight: '15px'}} onClick={handleOpenBook}>Hỗ trợ</Button>
        <Button 
        style={{color: 'black', fontWeight: 'bold', boxShadow: 'none',border: 'none', height: "100%", marginRight: '15px'}}>Theo dõi sức khỏe</Button>
      </div>
      </Header>
  );
};

export default HeaderNavbar;

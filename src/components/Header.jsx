import React, { useState } from "react";
import {Layout, Dropdown, Button } from 'antd';
import {
  CaretDownOutlined, CaretUpOutlined
} from '@ant-design/icons';
import logo from '../assets/Logo_2.png';
const { Header  } = Layout;
const HeaderNavbar = () => {
  const [position, setPosition] = useState('end');
  const [isHover, setIsHover] = useState(false);
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
  const buttons = [
    { text: "Thanh toán viện phí, đơn thuốc", onClick: null },
    { text: "Đặt lịch uống thuốc", onClick: null },
    { text: "Hỗ trợ", onClick: null },
    { text: "Theo dõi sức khỏe", onClick: null },
  ];
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
          justifyContent: 'space-between',
          backgroundColor: 'white',
          boxShadow: 'rgba(100, 100, 111, 0.2) 0px 7px 29px 0px',
        }}
      >
      <div className="flex items-center justify-center w-[15%] h-fit">
        <img src={logo} alt="" width="150px" height="150px" />
      </div>
      <div className="flex flex-row justify-between items-center h-full w-[60%]">
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
        {buttons.map((button, index) => (
          <Button
            key={index}
            style={{ color: 'black', fontWeight: 'bold', boxShadow: 'none', border: 'none', height: "100%", marginRight: '15px' }}
            onClick={button.onClick}
          >
            {button.text}
          </Button>
        ))}
      </div>
    </Header>
  );
};

export default HeaderNavbar;

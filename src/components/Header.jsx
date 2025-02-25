import React, { useState } from "react";
import { Link } from "react-router-dom";
import {Layout, Menu } from 'antd';
const { Header  } = Layout;
const HeaderNavbar = () => {
  const items1 = ['1', '2', '3'].map((key) => ({
    key,
    label: `nav ${key}`,
  }));
  return (
    <Header
          style={{
            display: 'flex',
            alignItems: 'center',
          }}
        >
            <div className="demo-logo" />
          <Menu
          theme="dark"
          mode="horizontal"
          defaultSelectedKeys={['2']}
          items={items1}
          style={{
            flex: 1,
            minWidth: 0,
          }}
        />
        </Header>
  );
};

export default HeaderNavbar;

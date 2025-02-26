import React, { useState } from 'react';
import {
  MenuFoldOutlined,
  MenuUnfoldOutlined,
  LaptopOutlined,
  UserOutlined,
  NotificationOutlined,
} from '@ant-design/icons';
import { Button, Layout, Menu, theme, Breadcrumb } from 'antd';
import HeaderNavbar from '../components/Header';
const { Header,Content, Sider  } = Layout;
const HomePage = () => {
  const [collapsed, setCollapsed] = useState(false);
  const {
    token: { borderRadiusLG },
  } = theme.useToken();
  const siderStyle = {
    overflow: 'auto',
    height: '100vh',
    position: 'sticky',
    insetInlineStart: 0,
    backgroundColor: 'transparent',
    top: 0,
    bottom: 0,
    scrollbarWidth: 'thin',
    scrollbarGutter: 'stable',
  };
//   const items1 = ['1', '2', '3'].map((key) => ({
//     key,
//     label: `nav ${key}`,
//   }));
  
  const items2 = [UserOutlined, LaptopOutlined, NotificationOutlined].map((icon, index) => {
    const key = String(index + 1);
    return {
      key: `sub${key}`,
      icon: React.createElement(icon),
      label: `subnav ${key}`,
      children: Array.from({
        length: 4,
      }).map((_, j) => {
        const subKey = index * 4 + j + 1;
        return {
          key: subKey,
          label: `option${subKey}`,
        };
      }),
    };
  });
  return (
    <Layout>
        <HeaderNavbar/>
        {/* <Header
          style={{
            display: 'flex',
            alignItems: 'center',
          }}
        >
            <div className="demo-logo" /> */}
          
          {/* <Menu
          theme="dark"
          mode="horizontal"
          defaultSelectedKeys={['2']}
          items={items1}
          style={{
            flex: 1,
            minWidth: 0,
          }}
        />
        </Header> */}
        
        <Layout>
        <Sider style={siderStyle} trigger={null} onCollapse={(value) => setCollapsed(value)} collapsible collapsed={collapsed}>
        <Menu
            mode="inline"
            defaultSelectedKeys={['1']}
            defaultOpenKeys={['sub1']}
            style={{
              height: '100%',
              borderRight: 0,
            }}
            items={items2}
          />
        </Sider>
        <Layout style={{
            padding: '0 24px 24px',
          }}>
            <Breadcrumb
            items={[
              {
                title: 'Home',
              },
              {
                title: 'List',
              },
              {
                title: 'App',
              },
            ]}
            style={{
              margin: '16px 0',
            }}
          />
            <Content
            style={{
                // margin: '24px 16px',
                // padding: 24,
                minHeight: 280,
                backgroundColor: 'transparent',
                borderRadius: borderRadiusLG,
                overflow: 'initial',
            }}
            >
            <div
                style={{
                padding: 24,
                textAlign: 'center',
                backgroundColor: 'white',
                borderRadius: borderRadiusLG,
                }}
            >
                <p>long content</p>
                {
                // indicates very long content
                Array.from(
                    {
                    length: 100,
                    },
                    (_, index) => (
                    <React.Fragment key={index}>
                        {index % 20 === 0 && index ? 'more' : '...'}
                        <br />
                    </React.Fragment>
                    ),
                )
                }
            </div>
            </Content>
        </Layout>
      </Layout>
    </Layout>
  );
};
export default HomePage;
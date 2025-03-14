//                       _oo0oo_
//                      o8888888o
//                      88" . "88
//                      (| -_- |)
//                      0\  =  /0
//                    ___/`---'\___
//                  .' \\|     |// '.
//                 / \\|||  :  |||// \
//                / _||||| -:- |||||- \
//               |   | \\\  -  /// |   |
//               | \_|  ''\---/''  |_/ |
//               \  .-\__  '-'  ___/-. /
//             ___'. .'  /--.--\  `. .'___
//          ."" '<  `.___\_<|>_/___.' >' "".
//         | | :  `- \`.;`\ _ /`;.`/ - ` : | |
//         \  \ `_.   \_ __\ /__ _/   .-` /  /
//     =====`-.____`.___ \_____/___.-`___.-'=====
//                       `=---='
//
//     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//            Phật phù hộ, không bao giờ BUG
//     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
import React, {useState} from 'react';
import { Breadcrumb, Layout, Menu, theme } from 'antd';
import HeaderNavbar from '../components/Header';
// const {Content, Footer } = Layout;
import SideBar from '../components/Sidebar';
import BookingContent from '../components/BookingContent';
const BookingPage = () => {
  const {
    token: { colorBgContainer, borderRadiusLG },
  } = theme.useToken();
  return (
      <Layout>
      <HeaderNavbar/>
      <div
        style={{
          padding: '0 48px',
        }}
      >
        <Breadcrumb
          style={{
            margin: '16px 0',
          }}
        >
          <Breadcrumb.Item>TRANG CHỦ</Breadcrumb.Item>
          <Breadcrumb.Item>ĐẶT LỊCH KHÁM BỆNH</Breadcrumb.Item>
        </Breadcrumb>
        <Layout
          style={{
            background: colorBgContainer,
            borderRadius: borderRadiusLG,
            minHeight: '300vh',
            display: 'flex',
            flexDirection: 'row',
          }}
        >
          <SideBar/>
            <BookingContent/>
        </Layout>
      </div>
      {/* <Footer
        style={{
          textAlign: 'center',
        }}
      >
        Ant Design ©{new Date().getFullYear()} Created by Ant UED
      </Footer> */}
    </Layout>
  );
};
export default BookingPage;
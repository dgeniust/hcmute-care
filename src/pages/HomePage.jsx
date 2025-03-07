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
import { LaptopOutlined, NotificationOutlined, UserOutlined } from '@ant-design/icons';
import { Breadcrumb, Layout, Menu, theme } from 'antd';
import HeaderNavbar from '../components/Header';
// const {Content, Footer } = Layout;
import SideBar from '../components/Sidebar';
import MainPage from '../components/MainPage';
import BMIPage from './BMIPage';
const HomePage = () => {
  const [currentPage, setCurrentPage] = useState('main');

  const handleChangePage = (page) => {
    setCurrentPage(page);
  }
  // const items2 = [UserOutlined, LaptopOutlined, NotificationOutlined].map((icon, index) => {
  //         const key = String(index + 1);
  //         return {
  //           key: `sub${key}`,
  //           icon: React.createElement(icon),
  //           label: `subnav ${key}`,
  //           children: Array.from({
  //             length: 4,
  //           }).map((_, j) => {
  //             const subKey = index * 4 + j + 1;
  //             return {
  //               key: subKey,
  //               label: `option${subKey}`,
  //             };
  //           }),
  //         };
  //       });
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
          {currentPage === 'main' && <MainPage onPageChange={handleChangePage}/>}
          {currentPage === 'bmi' && <BMIPage/>}
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
export default HomePage;
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
import Personal_Profile from '../pages/Personal_Profile';
import MedicalRecord from '../components/Personal/MedicalRecord';
import MedicalHistory from '../components/Personal/MedicalHistory';
import RegulationUse from '../components/Personal/RegulationUse';
const HomePage = () => {
  const [currentPage, setCurrentPage] = useState('main');

  const handleChangePage = (page) => {
    setCurrentPage(page);
  }
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
          <SideBar handleChangePage={handleChangePage}/>
          {currentPage === 'main' && <MainPage onPageChange={handleChangePage}/>}
          {currentPage === 'bmi' && <BMIPage/>}
          {currentPage === 'personal-profile' && <Personal_Profile/>}
          {currentPage === 'medical-records' && <MedicalRecord/>}
          {currentPage === 'medical-history' && <MedicalHistory/>}
          {currentPage === 'regulation-use' && <RegulationUse/>}
        </Layout>
      </div>
    </Layout>
  );
};
export default HomePage;
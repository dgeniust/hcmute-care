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
import { Breadcrumb, Layout, theme } from 'antd';
import HeaderNavbar from '../components/Header';
// const {Content, Footer } = Layout;
import SideBar from '../components/Sidebar';
import MainPage from '../components/MainPage';
import BMIContent from './BMI_BMR/BMIContent';
import Personal_Profile from '../pages/Personal_Profile';
import MedicalRecord from './Personal/MedicalRecord';
import MedicalHistory from './Personal/MedicalHistory';
import RegulationUse from './Personal/RegulationUse';
import TermsService from './Personal/TermsService';
import ServiceList from './ServiceList';
import Notification_Event from './Personal/Notification_Event';
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
          {currentPage === 'bmi' && <BMIContent/>}
          {currentPage === 'personal-profile' && <Personal_Profile/>}
          {currentPage === 'medical-records' && <MedicalRecord/>}
          {currentPage === 'medical-history' && <MedicalHistory/>}
          {currentPage === 'regulation-use' && <RegulationUse/>}
          {currentPage === 'terms-service' && <TermsService/>}
          {currentPage === 'service-list' && <ServiceList/>}
          {currentPage === 'notification-event' && <Notification_Event/>}
        </Layout>
      </div>
    </Layout>
  );
};
export default HomePage;
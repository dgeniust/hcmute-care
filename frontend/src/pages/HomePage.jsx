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
import React from 'react';
import { Breadcrumb, Layout, theme } from 'antd';
import HeaderNavbar from '../components/Header';
import SideBar from '../components/Sidebar';
import { Outlet, useLocation, Link } from "react-router-dom";

const HomePage = () => {
  const location = useLocation();

  const path = location.pathname.split('/').filter((path) => path);

  const breadcrumbMap = {
    "": "TRANG CHỦ",
    "calculate": "TÍNH TOÁN BMI, BMR. TDEE",
    "personal-profile": "HỒ SƠ CÁ NHÂN",
    "medical-records": "HỒ SƠ Y TẾ",
    "medical-history": "LỊCH SỬ KHÁM BỆNH",
    "regulation-use": "QUY ĐỊNH SỬ DỤNG",
    "terms-service": "ĐIỀU KHOẢN DỊCH VỤ",
    "service-list": "DANH SÁCH DỊCH VỤ",
    "notification-event": "THÔNG BÁO & SỰ KIỆN",
    "booking": "KHÁM THEO CHUYÊN KHOA",
  };

  const breadcumbItems = [
    {
      title: <Link to="/">TRANG CHỦ</Link>,
    },
    ...path.map((item, index) => ({
      title: <Link to={`/${path.slice(0, index + 1).join("/")}`}>{breadcrumbMap[item] || item}</Link>,
    })),
  ]

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
          items={breadcumbItems}
        />
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
            <Outlet />
        </Layout>
      </div>
    </Layout>
  );
};
export default HomePage;
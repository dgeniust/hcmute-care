import React from 'react';
import { Layout, theme, Breadcrumb } from 'antd';
import DoctorSideBar from '../../components/DoctorComponents/DoctorSidebar';
import { Outlet, useLocation, Link } from 'react-router-dom';
import SideBar from '../../components/Sidebar';
const DoctorDashboard = () => {
  const location = useLocation();
  const path = location.pathname.split('/').filter((path) => path);
  // Loại bỏ "doctor" để tránh hiển thị thừa
  const filteredPath = path.filter((item) => item !== "doctor");

  const breadcrumbMap = {
    "" : "BẢNG ĐIỀU KHIỂN",
    "news": "TIN TỨC - SỰ KIỆN",
    "schedule": "LỊCH LÀM VIỆC",
    "records": "HỒ SƠ BỆNH ÁN",
    "patient": "HỒ SƠ BỆNH NHÂN",
    "list-service" : "BẢNG GIÁ DỊCH VỤ"
  };

  const breadcrumbItems = [
    {
      title: <Link to="/doctor">BẢNG ĐIỀU KHIỂN</Link>,
    },
    ...filteredPath.map((item, index) => ({
      title: <Link to={`/doctor/${filteredPath.slice(0, index + 1).join("/")}`}>{breadcrumbMap[item] || item}</Link>,
    })),
  ];


  const {
    token: { colorBgContainer, borderRadiusLG },
  } = theme.useToken();


  return (
      <Layout>
      <div
        style={{
          padding: '12px 24px',
        }}
      >
        <Breadcrumb
          style={{
            margin: '16px 0',
          }}
          items={breadcrumbItems}
        />
        <Layout
          style={{
            background: colorBgContainer,
            borderRadius: borderRadiusLG,
            minHeight: '100vh',
            height: '100%',
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

}
export default DoctorDashboard;
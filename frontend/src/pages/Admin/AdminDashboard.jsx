import React from 'react';
import { Layout, theme, Breadcrumb } from 'antd';
import AdminSideBar from '../../components/AdminComponents/AdminSidebar';
import { Outlet, useLocation, Link } from 'react-router-dom';
const AdminDashboard = () => {
  const location = useLocation();
  const path = location.pathname.split('/').filter((path) => path);
  // Loại bỏ "admin" để tránh hiển thị thừa
  const filteredPath = path.filter((item) => item !== "admin");

  const breadcrumbMap = {
    "": "BẢNG ĐIỀU KHIỂN",
    "manage-users": "QUẢN LÝ NGƯỜI DÙNG",
    "manage-numbers": "QUẢN LÝ PHIẾU KHÁM BỆNH",
    "manage-employee": "DANH SÁCH NHÂN SỰ",
    "manage-rooms": "QUẢN LÝ PHÒNG BAN",
    "manage-schedule": "LỊCH TRÌNH LÀM VIỆC",
    "manage-items": "QUẢN LÝ THIẾT BỊ",
    "manage-support": "ĐIỀU PHỐI HỖ TRỢ",
    "terms-service": "ĐIỀU KHOẢN DỊCH VỤ",
    "manage-service": "BẢNG GIÁ DỊCH VỤ",
    "manage-posts": "QUẢN LÝ TIN TỨC - SỰ KIỆN",
    "manage-chat" : "CHĂM SÓC KHÁCH HÀNG",
    "manage-contact" : "QUẢN LÝ LIÊN HỆ",
    "regulation-use": "QUY ĐỊNH SỬ DỤNG",
  };

  const breadcrumbItems = [
    {
      title: <Link to="/admin">BẢNG ĐIỀU KHIỂN</Link>,
    },
    ...filteredPath.map((item, index) => ({
      title: <Link to={`/admin/${filteredPath.slice(0, index + 1).join("/")}`}>{breadcrumbMap[item] || item}</Link>,
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
            minHeight: '300vh',
            display: 'flex',
            flexDirection: 'row',
          }}
        >
          <AdminSideBar/>
          <Outlet />
        </Layout>
      </div>
    </Layout>
  );

}
export default AdminDashboard;
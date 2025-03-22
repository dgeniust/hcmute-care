import React, {useState} from 'react';
import { Layout, theme } from 'antd';
import AdminSideBar from '../../components/AdminComponents/AdminSidebar';
import AdminHomePage from './AdminHomepage/AdminHomePage';
import ManageUser from './ManageUser/ManageUser';
import ManageNumber_Orders from './ManageUser/ManageNumber_Orders';
import ManageRoom from './ManageEmployees/ManageRoom';
import ManageEmployee from './ManageEmployees/ManageEmployee';
import ManagePost from './ManagePostEvent/ManagePost';
import ManageService from './ManageService/ManageService';
const AdminDashboard = () => {
    const [currentPage, setCurrentPage] = useState('manage-service');

  const handleChangePage = (page) => {
    setCurrentPage(page);
  }
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
        <Layout
          style={{
            background: colorBgContainer,
            borderRadius: borderRadiusLG,
            minHeight: '300vh',
            display: 'flex',
            flexDirection: 'row',
          }}
        >
          <AdminSideBar handleChangePage={handleChangePage}/>
          {currentPage === 'main' && <AdminHomePage />}
          {currentPage === 'manage-users' && <ManageUser/>}
          {currentPage === 'manage-numbers' && <ManageNumber_Orders/>}
          {currentPage === 'manage-rooms' && <ManageRoom/>}
          {currentPage === 'manage-posts' && <ManagePost/>}
          {currentPage === 'manage-employee' && <ManageEmployee/>}
          {currentPage === 'manage-service' && <ManageService/>}
        </Layout>
      </div>
    </Layout>
  );

}
export default AdminDashboard;
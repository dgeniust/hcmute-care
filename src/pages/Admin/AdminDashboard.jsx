import React, {useState} from 'react';
import { Layout, theme } from 'antd';
import AdminSideBar from '../../components/AdminComponents/AdminSidebar';
import AdminHomePage from '../../components/AdminComponents/AdminHomePage';
import ManageUser from '../../components/AdminComponents/ManageUser/ManageUser';
import ManageNumber_Orders from '../../components/AdminComponents/ManageUser/ManageNumber_Orders';
import ManageRoom from '../../components/AdminComponents/ManageEmployee/ManageRoom';
import ManagePost from '../../components/AdminComponents/MangePostEvent/ManagePost';
const AdminDashboard = () => {
    const [currentPage, setCurrentPage] = useState('manage-posts');

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
        </Layout>
      </div>
    </Layout>
  );

}
export default AdminDashboard;
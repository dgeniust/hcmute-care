import React, {useState} from 'react';
import { Layout, theme } from 'antd';
import AdminSideBar from '../../components/AdminComponents/AdminSidebar';
import AdminHomePage from '../../components/AdminComponents/AdminHomePage';
const AdminDashboard = () => {
    const [currentPage, setCurrentPage] = useState('main');

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
          <AdminHomePage/>
        </Layout>
      </div>
    </Layout>
  );

}
export default AdminDashboard;
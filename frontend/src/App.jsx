// import './App.css'
import Signup from './pages/login signup/SignupPage';
import Login from './pages/login signup/LoginPage';
import VerifyOTP from './pages/login signup/VerifyOTP';
import ConfirmPassword from './pages/login signup/ConfirmPassword';
import GetPhone from './pages/login signup/GetPhone';
import GetPassword from './pages/login signup/GetPassword';
import HomePage from './pages/HomePage';
import Book_Review from '../../frontend/src/pages/Book_Review/Book_Review';
import AdminDashboard from './pages/Admin/AdminDashboard';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';

import Notification_Event from './pages/Personal/Notification_Event';
import ServiceList from './pages/ServiceList';
import TermsService from './pages/Personal/TermsService';
import RegulationUse from './pages/Personal/RegulationUse';
import MedicalHistory from './pages/Personal/MedicalHistory';
import MedicalRecord from './pages/Personal/MedicalRecord';
import Personal_Profile from './pages/Personal_Profile';
import CalculateContent from './pages/BMI_BMR/CalculateContent';
import MainPage from './components/MainPage';
import BookingContent from './pages/Booking/BookingContent';
import ManageUser from './pages/Admin/ManageUser/ManageUser';
import ManageService from './pages/Admin/ManageService/ManageService';
import ManageEmployee from './pages/Admin/ManageEmployees/ManageEmployee';
import ManagePost from './pages/Admin/ManagePostEvent/ManagePost';
import ManageRoom from './pages/Admin/ManageEmployees/ManageRoom';
import ManageNumber_Orders from './pages/Admin/ManageUser/ManageNumber_Orders';
import AdminHomePage from './pages/Admin/AdminHomepage/AdminHomePage';
function App() {

  return (
    <Router>
      <div>
        <Routes>
          {/* AUTH ROUTES */}
          <Route path="/login" element={<Login/>} />
          <Route path="/signup" element={<Signup/>} />
          <Route path="/verifyOTP" element={<VerifyOTP/>} />
          <Route path="/confirmPassword" element={<ConfirmPassword/>} />
          <Route path="/getPhone" element={<GetPhone/>} />
          <Route path="/getPassword" element={<GetPassword/>} />
          
          {/* USER ROUTES */}
          <Route path="/" element={<HomePage />}>
            <Route index element={<MainPage />} />
            <Route path="calculate" element={<CalculateContent />} />
            <Route path="personal-profile" element={<Personal_Profile />} />
            <Route path="medical-records" element={<MedicalRecord />} />
            <Route path="medical-history" element={<MedicalHistory />} />
            <Route path="regulation-use" element={<RegulationUse />} />
            <Route path="terms-service" element={<TermsService />} />
            <Route path="service-list" element={<ServiceList />} />
            <Route path="notification-event" element={<Notification_Event />} />
            <Route path="booking" element={<BookingContent />} />
          </Route>

          {/* ADMIN ROUTES */}
          <Route path="/admin" element={<AdminDashboard />} >
            <Route index element={<AdminHomePage />} />
            <Route path="/admin/manage-users" element={<ManageUser />} />
            <Route path="/admin/manage-rooms" element={<ManageRoom />} />
            <Route path="/admin/manage-posts" element={<ManagePost />} />
            <Route path="/admin/manage-service" element={<ManageService />} />
            <Route path="/admin/manage-employee" element={<ManageEmployee />} />
            <Route path="/admin/manage-numbers" element={<ManageNumber_Orders />} />
          </Route>

          {/* ANOTHER ROUTES */}
          <Route path="/book" element={<Book_Review/>}/>
        </Routes>
      </div>
    </Router>
  )
}

export default App

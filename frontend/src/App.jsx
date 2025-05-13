// import './App.css'
import Signup from './pages/login signup/SignupPage';
import Login from './pages/login signup/LoginPage';
import VerifyOTP from './pages/login signup/VerifyOTP';
import ConfirmPassword from './pages/login signup/ConfirmPassword';
import GetPhone from './pages/login signup/GetPhone';
import GetPassword from './pages/login signup/GetPassword';
import HomePage from './pages/HomePage';
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
import DoctorDashboard from './pages/Doctor/DoctorDashboard';
import DoctorHomePage from './pages/Doctor/DoctorHomepage/DoctorHomePage';
import DoctorManageRecords from './pages/Doctor/DoctorManageRecords/DoctorManageRecords';
import PatientRecords from './components/DoctorComponents/PatientRecords/PatientRecords';
import DoctorSchedule from './pages/Doctor/DoctorSchedule/DoctorSchedule';
import ProtectedRoute from './pages/ProtectedRoute';
import ManageSchedule from './pages/Admin/ManageSchedule/ManageSchedule';
import ConfirmBill_Booking from './components/Booking/5/ConfirmBill_Booking';
import PaymentResultPage from './components/Booking/4/PaymentResultPage';
import Book_Review from './pages/Book_Review/Book_Review';
import NurseDashboard from './pages/Nurse/NurseDashboard';
import NurseHomePage from './pages/Nurse/NurseHomePage';
import ChatbotContextMenu from './components/Chatbot/ChatbotContextMenu';
import DiagnoseRecord from './pages/Doctor/DoctorManageRecords/DiagnoseRecord';
import DiagnosePatient from './components/DoctorComponents/DiagnoseRecords/DiagnosePatient';
import PrivacyPolicy from './pages/Personal/PrivacyPolicy';
import ContactService from './pages/Personal/ContactService';
import ChatbotService from './pages/Personal/ChatbotService';
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
          <Route element={<ProtectedRoute allowedRoles={['ROLE_CUSTOMER']} />}>
            <Route path="/" element={<HomePage />}>
              <Route index element={<MainPage />} />
              <Route path="calculate" element={<CalculateContent />} />
              <Route path="personal-profile" element={<Personal_Profile />} />
              <Route path="medical-records" element={<MedicalRecord />} />
              <Route path="medical-history" element={<MedicalHistory />} />
              <Route path="regulation-use" element={<RegulationUse />} />
              <Route path="contact-service" element={<ContactService />} />
              <Route path="chatbot" element={<ChatbotService />} />
              <Route path="terms-service" element={<TermsService />} />
              <Route path="privacy-policy" element={<PrivacyPolicy />} />
              <Route path="service-list" element={<ServiceList />} />
              <Route path="notification-event" element={<Notification_Event />} />
              <Route path="booking" element={<BookingContent />} />
              <Route path="/confirm-booking" element={<PaymentResultPage />} />
              <Route path='/completed-booking' element={<ConfirmBill_Booking/>}/>
            </Route>
          </Route>

          {/* ADMIN ROUTES */}
          <Route element={<ProtectedRoute allowedRoles={['ROLE_ADMIN']} />}>
            <Route path="/admin" element={<AdminDashboard />} >
              <Route index element={<AdminHomePage />} />
              <Route path="/admin/manage-users" element={<ManageUser />} />
              <Route path="/admin/manage-rooms" element={<ManageRoom />} />
              {/* <Route path="/admin/manage-posts" element={<ManagePost />} /> */}
              <Route path="/admin/manage-service" element={<ManageService />} />
              <Route path="/admin/manage-employee" element={<ManageEmployee />} />
              <Route path="/admin/manage-numbers" element={<ManageNumber_Orders />} />
              <Route path="/admin/manage-schedule" element={<ManageSchedule />} />
            </Route>
          </Route>

          {/* DOCTOR ROUTES */}
          <Route element={<ProtectedRoute allowedRoles={['ROLE_DOCTOR']} />}>
            <Route path="/doctor" element={<DoctorDashboard />} >
              <Route index element={<DoctorHomePage />} />
              <Route path="/doctor/records" element={<DoctorManageRecords />} />
              <Route path="/doctor/patient" element={<PatientRecords />} />
              <Route path="/doctor/schedule" element={<DoctorSchedule />} />
              <Route path="/doctor/diagnose-patient" element={<DiagnoseRecord />} />
              <Route path='/doctor/detail-diagnose' element={<DiagnosePatient/>}/>
              {/*<Route path="/doctor/manage-posts" element={<ManagePost />} /> */}
            </Route>
          </Route>
          {/* NURSE ROUTE */}
          <Route element={<ProtectedRoute allowedRoles={['ROLE_NURSE']} />}>
            <Route path="/nurse" element={<NurseDashboard />} >
              <Route index element={<NurseHomePage />} />
            </Route>
          </Route>
          {/* STAFF ROUTES */}
          <Route element={<ProtectedRoute allowedRoles={['ROLE_STAFF']} />}>
            <Route path="/staff" element={<DoctorDashboard />} >
              {/* <Route index element={<DoctorHomePage />} /> */}
              <Route index element={<ManagePost />} />
            </Route>
          </Route>
          {/* ANOTHER ROUTES */}
          <Route path="/book" element={<Book_Review/>}/>
        </Routes>
      </div>
      <ChatbotContextMenu />
    </Router>
  )
}

export default App

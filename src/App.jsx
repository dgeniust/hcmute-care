// import './App.css'
import Signup from './pages/login signup/SignupPage';
import Login from './pages/login signup/LoginPage';
import VerifyOTP from './pages/login signup/VerifyOTP';
import ConfirmPassword from './pages/login signup/ConfirmPassword';
import GetPhone from './pages/login signup/GetPhone';
import GetPassword from './pages/login signup/GetPassword';
import HomePage from './pages/HomePage';
import BMIPage from './pages/BMIPage';
import BookingPage from './pages/BookingPage';
import Book_Review from './components/Book_Review';
import AdminDashboard from './pages/Admin/AdminDashboard';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';

function App() {

  return (
    <Router>
      <div>
        <Routes>
          <Route path="/" element={<Navigate to="/booking" />} />
          <Route path="/login" element={<Login/>} />
          <Route path="/signup" element={<Signup/>} />
          <Route path="/verifyOTP" element={<VerifyOTP/>} />
          <Route path="/confirmPassword" element={<ConfirmPassword/>} />
          <Route path="/getPhone" element={<GetPhone/>} />
          <Route path="/getPassword" element={<GetPassword/>} />
          <Route path="/home" element={<HomePage/>} />
          <Route path="/bmi" element={<BMIPage/>} />
          <Route path="/booking" element={<BookingPage/>} />
          <Route path="/book" element={<Book_Review/>}/>
          <Route path="/admin" element={<AdminDashboard/>}/>
        </Routes>
      </div>
    </Router>
  )
}

export default App

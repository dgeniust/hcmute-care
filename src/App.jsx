// import './App.css'
import Signup from './pages/SignupPage';
import Login from './pages/LoginPage';
import VerifyOTP from './pages/VerifyOTP';
import ConfirmPassword from './pages/ConfirmPassword';
import GetPhone from './pages/GetPhone';
import GetPassword from './pages/GetPassword';
import HomePage from './pages/HomePage';
import BMIPage from './pages/BMIPage';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';

function App() {

  return (
    <Router>
      <div>
        <Routes>
          <Route path="/" element={<Navigate to="/home" />} />
          <Route path="/login" element={<Login/>} />
          <Route path="/signup" element={<Signup/>} />
          <Route path="/verifyOTP" element={<VerifyOTP/>} />
          <Route path="/confirmPassword" element={<ConfirmPassword/>} />
          <Route path="/getPhone" element={<GetPhone/>} />
          <Route path="/getPassword" element={<GetPassword/>} />
          <Route path="/home" element={<HomePage/>} />
          <Route path="/bmi" element={<BMIPage/>} />
        </Routes>
      </div>
    </Router>
  )
}

export default App

import React, { useState } from "react";
import { PhoneOutlined, LockOutlined } from '@ant-design/icons';
import { Input, Checkbox, Button, message, notification  } from 'antd';
import { Link, useNavigate } from 'react-router-dom';
import {notifySuccess, notifyErrorWithCustomMessage, handleHttpStatusCode} from "../../utils/notificationHelper";
const Login = () => {
  const navigate = useNavigate();
  const [phone, setPhone] = useState('');
  const [password, setPassword] = useState('');
  const [agreeTerms, setAgreeTerms] = useState(false);
  const [phoneStatus, setPhoneStatus] = useState('');
  const [passwordStatus, setPasswordStatus] = useState('');
  const [messageApi, contextHolder] = message.useMessage();
  const [notificationApi, contextNotiHolder] = notification.useNotification();

  const handleSignupRedirect = () => {
    // Navigate to the Signup page when the button is clicked
    navigate('/signup');
  };

  const handlePhoneChange = (e) => {
    setPhone(e.target.value);
    console.log('Phone change: ', e.target.value);
  }
  const handlePasswordChange = (e) => {
    setPassword(e.target.value);
    console.log('Password change: ', e.target.value);
  }
  const handleCheckboxChange = (e) => {
    setAgreeTerms(e.target.checked);
  }
  const validateForm = () => {
    let valid = true;
    if (phone.trim() === "" || phone.length < 10 || phone.length > 11) {
      valid = false;
      setPhoneStatus('error');
      notifyErrorWithCustomMessage("Vui lòng nhập đúng số điện thoại của bạn 🫠", messageApi);
    }
    if (password.trim() === "") {
      valid = false;
      setPasswordStatus('error');
      notifyErrorWithCustomMessage("Vui lòng nhập đúng mật khẩu của bạn 🫠", messageApi);
    }
    if (!agreeTerms) {
      valid = false;
      notifyErrorWithCustomMessage("Vui lòng đồng ý với điều khoản sử dụng 🫠", messageApi);
  }
    return valid;
  }
  const handleSubmit = async (e) => {
    e.preventDefault();
    const isValid = validateForm();
    if(!isValid) return;

    const payload = {
      phone: phone,
      password: password,
      platform: "WEB",
      deviceToken: "",
    };

    try {
      const response = await fetch("http://localhost:8080/api/v1/auth/sign-in", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(payload),
      })
      console.log("Raw response:", response);  // Log response trực tiếp
      
      const result = await response.json();
      if(response.ok && result.status === 200) {
        
          const { accessToken, refreshToken, userId } = result.data;

          //Lưu token vào LocalStorage
          localStorage.setItem("accessToken", accessToken);
          localStorage.setItem("refreshToken", refreshToken);
          localStorage.setItem("userId", userId);

          console.log("Login successful:", result.data);
          console.log("id account: " + userId);
          notifySuccess('Đăng nhập thành công', 'Chào mừng bạn đến với HCMUTE CARE',notificationApi, { showProgress: true, pauseOnHover: true });
          setTimeout(() => {
            navigate('/');
          }, 1000); // Wait for 2 seconds before redirecting
        }
      else {
        // Xử lý tất cả mã trạng thái bằng handleHttpStatusCode
        handleHttpStatusCode(
          result.status || response.status,
          'Đăng nhập thành công',
          result.message || 'Đăng nhập thất bại, vui lòng kiểm tra lại thông tin đăng nhập 🫠',
          messageApi
        )
      }
    }
    catch (e) {
      notifyErrorWithCustomMessage("Lỗi kết nối đến server 🫠");
      console.log("Login error:", e);
    }
    
  }
  const isFormEmpty = phone.trim() === "" || password.trim() === "";
  return <div className="flex justify-center items-center h-screen w-screen login-bg bg-cover bg-center">
    <div className="flex flex-col justify-center items-center h-[70vh] w-[60vh] border bg-white border-gray-300 rounded-xl shadow-lg">
    <div className="w-full flex flex-row pt-3 items-center px-6 space-x-8">
        {/* <img src={logo} alt="" width={"100px"} height={"100px"} /> */}
        <div className="text-black text-xl font-bold flex-col text-left"><p>Chào mừng đến với</p> <span className="text-sky-700 text-left">HCMUTE CARE</span></div>
    </div>
    <div className="px-6 pb-6 w-full text-left">
        <p className="text-gray-500">Vui lòng đăng nhập để sử dụng</p>
    </div>
    <form className="flex flex-col justify-center items-center w-full h-fit" onSubmit={handleSubmit}>
      <div className="w-full h-fit pb-3 px-6 flex-col flex text-black">
        <label htmlFor="phone" className="justify-start text-left mb-1 font-normal">Số điện thoại</label>
        <Input status={phoneStatus} onChange={handlePhoneChange} value={phone} size="large" placeholder="Số điện thoại..." prefix={<PhoneOutlined />}/>
      </div>
      <div className="w-full h-fit pb-3 px-6 flex-col flex text-black">
        <label htmlFor="password" className="justify-start text-left mb-1 font-normal">Mật khẩu</label>
        <Input.Password value={password} status={passwordStatus} onChange={handlePasswordChange} size="large" placeholder="Nhập mật khẩu" prefix={<LockOutlined />}/>
      </div>
      <Checkbox checked={agreeTerms} 
                onChange={handleCheckboxChange}>
                  Lưu thông tin đăng nhập
      </Checkbox>
      <div className="w-full py-6 px-6">
        {
          isFormEmpty ? <Button disabled size="large" block htmlType="submit">Đăng nhập</Button> 
          : <Button style={{backgroundColor: '#0958d9', color: 'white' }} size="large" block htmlType="submit">Đăng nhập</Button>
        }
      </div>
    </form>
    <div>
        <Link className="text-sky-600" to={"/getPhone"}>Quên tài khoản hoặc mật khẩu</Link>
    </div>
    <div className="w-full py-6 px-6">
      <Button variant="outlined"color="primary" size="large"block onClick={handleSignupRedirect}>Đăng kí tài khoản mới</Button>
    </div>
    </div>
    {contextHolder}
    {contextNotiHolder}
  </div>
}
export default Login;

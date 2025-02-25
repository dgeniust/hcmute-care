import React, { useState } from "react";
import { PhoneOutlined, LockOutlined, CheckCircleFilled } from '@ant-design/icons';
import { Input, Checkbox, Button, notification, message  } from 'antd';
import { Link, useNavigate } from 'react-router-dom';

const Login = () => {
  const navigate = useNavigate();
  const [phone, setPhone] = useState('');
  const [password, setPassword] = useState('');
  const [agreeTerms, setAgreeTerms] = useState(false);
  const [api, contextNotificationHolder] = notification.useNotification();
  const [phoneStatus, setPhoneStatus] = useState('');
  const [passwordStatus, setPasswordStatus] = useState('');
  const [messageApi, contextHolder] = message.useMessage();
  const handleSignupRedirect = () => {
    // Navigate to the Signup page when the button is clicked
    navigate('/signup');
  };
  function success(){
    messageApi.open({
      type: 'success',
      content: 'Signup success 😙',
    });
  };

  function error(message){
    messageApi.open({
      type: 'error',
      content: message,
    });
  };  
  function openNotification(pauseOnHover) {
        api.open({
          message: 'Đăng nhập thành công',
          description:
            'Xin chào Nguyễn Thành Đạt',
          showProgress: true,
          pauseOnHover,
          icon: (<CheckCircleFilled style={{color: '#2ed573'}}/>)
        });
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
    if (phone.trim() === "") {
      valid = false;
      setPhoneStatus('error');
      error("Vui lòng nhập số điện thoại 🫠");
    }
    if (password.trim() === "") {
      valid = false;
      setPasswordStatus('error');
      error("Vui lòng nhập mật khẩu 🫠");
    }
    if (!agreeTerms) {
      valid = false;
      error("Vui lòng đồng ý với điều khoản sử dụng 🫠");
  }
    return valid;
  }
  const handleSubmit = (e) => {
    e.preventDefault();
    const isValid = validateForm();
    if(isValid && phone === "0387731823" && password === "123") {
      openNotification(true);
      setTimeout(() => {
        navigate('/home'); 
      }, 1000)
    }
    else{
      error("Tài khoản hoặc mật khẩu không chính xác 🫠");
    }
  }
  const isFormEmpty = phone.trim() === "" || password.trim() === "";
  console.log("disabled: "+isFormEmpty);
  return <div className="flex justify-center items-center h-screen w-screen login-bg bg-cover bg-center">
    <div className="flex flex-col justify-center items-center h-[80vh] w-[60vh] border border-black bg-white">
    <div className="w-full flex flex-row pt-3 items-center px-6">
        <img src="/img/logo_hcmute_care.png" alt="" width={"150px"} height={"150px"} />
        <div className="text-black text-xl font-bold flex-col text-left"><p>Chào mừng đến với</p> <span className="text-sky-700 text-left">HCMUTE CARE</span></div>
    </div>
    <div className="px-6 pb-6 w-full text-left">
        <p className="text-gray-500">Vui lòng đăng nhập để sử dụng</p>
    </div>
    <form className="flex flex-col justify-center items-center w-full h-fit" onSubmit={handleSubmit}>
      <div className="w-full h-fit pb-3 px-6 flex-col flex text-black">
        <label htmlFor="phone" className="justify-start text-left mb-1 font-bold">Số điện thoại</label>
        <Input status={phoneStatus} onChange={handlePhoneChange} value={phone} size="large" placeholder="Số điện thoại..." prefix={<PhoneOutlined />}/>
      </div>
      <div className="w-full h-fit pb-3 px-6 flex-col flex text-black">
        <label htmlFor="password" className="justify-start text-left mb-1 font-bold">Mật khẩu</label>
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
        <Link to={"/getPhone"}>Quên tài khoản hoặc mật khẩu</Link>
    </div>
    <div className="w-full py-6 px-6">
      <Button variant="outlined"color="primary" size="large"block onClick={handleSignupRedirect}>Đăng kí tài khoản mới</Button>
    </div>
    </div>
    {contextHolder}
    {contextNotificationHolder}
  </div>
}
export default Login;

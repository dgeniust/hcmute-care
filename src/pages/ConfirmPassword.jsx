import React from "react";
import { ArrowLeftOutlined, LockOutlined } from '@ant-design/icons';
import { Input, Checkbox, Button  } from 'antd';
import { useNavigate } from 'react-router-dom';

const ConfirmPassword = () => {
  const navigate = useNavigate();

  const handleSignupRedirect = () => {
    // Navigate to the Signup page when the button is clicked
    navigate('/login');
  };
  return <div className="flex justify-center items-center h-screen w-screen login-bg bg-cover bg-center">
    <div className="flex flex-col justify-center items-center h-[80vh] w-[60vh] border border-black bg-white">
    <div className="w-full flex flex-row justify-between px-3 text-black font-bold text-xl">
        <Button color="default" variant="link" icon={<ArrowLeftOutlined style={{color:'#3498db'}}/>} style={{color: 'black', fontWeight: 'bold', fontSize: '15px'}} onClick={() => navigate('/login')}>
            Quay lại
        </Button>
    </div>
    <div className="w-full flex flex-row pt-3 items-center px-6">
        <img src="/img/logo_hcmute_care.png" alt="" width={"150px"} height={"150px"} />
        <div className="text-black text-xl font-bold flex-col text-left"><p>Chào mừng đến với</p> <span className="text-sky-700 text-left">HCMUTE CARE</span></div>
    </div>
    <div className="px-6 pb-6 w-full text-left">
        <p className="text-gray-500 font-bold">Nhập mật khẩu của bạn</p>
    </div>
    <form className="flex flex-col justify-center items-center w-full h-fit">
      <div className="w-full h-fit pb-3 px-6 flex-col flex text-black">
        <label htmlFor="phone" className="justify-start text-left mb-1 font-bold">Nhập mật khẩu</label>
        <Input.Password size="large" placeholder="Nhập mật khẩu" prefix={<LockOutlined />}/>
      </div>
      <div className="w-full h-fit pb-3 px-6 flex-col flex text-black">
        <label htmlFor="phone" className="justify-start text-left mb-1 font-bold">Nhập lại mật khẩu</label>
        <Input.Password size="large" placeholder="Nhập mật khẩu" prefix={<LockOutlined />}/>
      </div>
      <div className="w-full py-6 px-6">
        <Button style={{backgroundColor: '#0958d9', color: 'white' }} size="large" block>Xác nhận</Button>
      </div>
    </form>
    </div>
  </div>
}
export default ConfirmPassword;

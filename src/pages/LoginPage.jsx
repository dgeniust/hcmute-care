import React from "react";
import { PhoneOutlined, LockOutlined } from '@ant-design/icons';
import { Input, Checkbox, Button  } from 'antd';

const Login = () => {
    const [passwordVisible, setPasswordVisible] = React.useState(false);

  return <div className="flex justify-center items-center h-screen w-screen login-bg bg-cover bg-center">
    <div className="flex flex-col justify-center items-center h-[80vh] w-[60vh] border border-black bg-white">
    <div className="w-full flex flex-row pt-3 items-center px-6">
        <img src="/img/logo_hcmute_care.png" alt="" width={"150px"} height={"150px"} />
        <div className="text-black text-xl font-bold flex-col text-left"><p>Chào mừng đến với</p> <span className="text-sky-700 text-left">HCMUTE CARE</span></div>
    </div>
    <div className="px-6 pb-6 w-full text-left">
        <p className="text-gray-500">Vui lòng đăng nhập để sử dụng</p>
    </div>
    <form className="flex flex-col justify-center items-center w-full h-fit">
      <div className="w-full h-fit pb-3 px-6 flex-col flex text-black">
        <label htmlFor="phone" className="justify-start text-left mb-1 font-bold">Số điện thoại</label>
        <Input size="large" placeholder="Số điện thoại..." prefix={<PhoneOutlined />}/>
      </div>
      <div className="w-full h-fit pb-3 px-6 flex-col flex text-black">
        <label htmlFor="phone" className="justify-start text-left mb-1 font-bold">Mật khẩu</label>
        <Input.Password size="large" placeholder="Nhập mật khẩu" prefix={<LockOutlined />}/>
      </div>
      <Checkbox>Lưu thông tin đăng nhập</Checkbox>
      <div className="w-full py-6 px-6">
        <Button type="primary" size="large" block>Đăng nhập</Button>
      </div>
    </form>
    <div>
        <a>Quên tài khoản hoặc mật khẩu</a>
    </div>
    <div className="w-full py-6 px-6">
        <Button variant="outlined"color="primary" size="large"block>Đăng kí tài khoản mới</Button>
    </div>
    </div>
  </div>
}
export default Login;
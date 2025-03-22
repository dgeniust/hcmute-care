import React, { useState } from "react";
import { PhoneOutlined, ArrowLeftOutlined } from '@ant-design/icons';
import { Input, Button, message  } from 'antd';
import { useNavigate } from 'react-router-dom';

const Signup = () => {
    const navigate = useNavigate();
    const [phone, setPhone] = useState('')
    const [phoneStatus, setPhoneStatus] = useState(''); // State for phone input status (error, success)
    const [messageApi, contextHolder] = message.useMessage();
    function success(){
        messageApi.open({
          type: 'success',
          content: 'Signup success 😙',
        });
      };
    
      function error(){
        messageApi.open({
          type: 'error',
          content: 'Something went wrong 🫠',
        });
    };
    const handlePhoneChange = (e) => {
        setPhone(e.target.value);
        console.log('Phone changed: ', e.target.value);
    }
    const validateForm = () => {
        console.log('phone: '+ phone)
        let valid = true;
        if(!phone) {
            setPhoneStatus('error');
            valid = false;
        }else {
            setPhoneStatus('');
        }
        const phoneRegex = /^[0-9]{10,11}$/;
        if (!phoneRegex.test(phone)) {
            setPhoneStatus('error');
            valid = false;
        }
        return valid;
    }
    const handleSubmit = (e) => {
        e.preventDefault();
        const isValid = validateForm();
        // Validate the form

        if (isValid) {
        // Redirect to another page if necessary
            success()
            setTimeout(() => {
                navigate('/verifyOTP?type=getPhone');
            }, 1000); // Wait for 1 seconds before redirecting
        }
        else{
            error()
        }
    }
    const isEmptyPhone = phone.trim() === ''

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
            <p className="text-gray-500">Quên mật khẩu</p>
        </div>
        <form className="flex flex-col justify-center items-center w-full h-fit" 
            onSubmit={handleSubmit}>
            <div className="w-full h-fit pb-3 px-6 flex-col flex text-black">
                <label htmlFor="phone" className="justify-start text-left mb-1 font-bold">Số điện thoại</label>
                <Input status={phoneStatus} 
                onChange={handlePhoneChange}  size="large" placeholder="Số điện thoại..." prefix={<PhoneOutlined />} value={phone} />
            </div>
            <div className="w-full py-6 px-6">
                {
                    isEmptyPhone ?
                    <Button htmlType="submit" disabled size="large" block>Xác nhận</Button> : 
                    <Button htmlType="submit" style={{backgroundColor: '#0958d9', color: 'white' }} size="large" block>Xác nhận</Button>
                }
            </div>
        </form>
        </div>
        {contextHolder}
    </div>
}
export default Signup;

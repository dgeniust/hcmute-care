import React, { useState } from "react";
import { PhoneOutlined, ArrowLeftOutlined } from '@ant-design/icons';
import { Input, Checkbox, Button, message  } from 'antd';
import { useNavigate } from 'react-router-dom';

const Signup = () => {
    const navigate = useNavigate();
    const [phone, setPhone] = useState('')
    const [agreeTerms, setAgreeTerms] = useState(false);
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
    const handleCheckboxChange = (e) => {
        setAgreeTerms(e.target.checked);
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
        if (!agreeTerms) {
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
                navigate('/verifyOTP?type=signup');
            }, 1000); // Wait for 2 seconds before redirecting
        }
        else{
            error()
        }
    }
    const handleLoginRedirect = () => {
        navigate('/login');
    }
    const isEmptyInput = phone.trim() === ''

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
            <p className="text-gray-500">Đăng ký tài khoản bằng số điện thoại</p>
        </div>
        <form className="flex flex-col justify-center items-center w-full h-fit" 
            onSubmit={handleSubmit}>
            <div className="w-full h-fit pb-3 px-6 flex-col flex text-black">
                <label htmlFor="phone" className="justify-start text-left mb-1 font-bold">Số điện thoại</label>
                <Input status={phoneStatus} 
                onChange={handlePhoneChange}  size="large" placeholder="Số điện thoại..." prefix={<PhoneOutlined />} value={phone} />
            </div>
            <div className="w-full h-fit px-6 items-start">
            <Checkbox className="text-left" 
                checked={agreeTerms} 
                onChange={handleCheckboxChange}>Bằng việc đăng ký tài khoản tại HCMUTE Care, tôi đã đọc và đồng ý với các <span className="text-sky-700">điều khoản sử dụng</span>
            </Checkbox>
            </div>
            <div className="w-full py-6 px-6">
                {
                    isEmptyInput ? <Button disabled type='primary' htmlType="submit" size="large" block>Đăng ký tài khoản</Button> : <Button htmlType="submit" style={{backgroundColor: '#0958d9', color: 'white' }} size="large" block>Đăng ký tài khoản</Button>
                }
                
            </div>
        </form>
        <div className="w-full py-6 px-6">
            <Button variant="outlined"color="primary" size="large"block onClick={handleLoginRedirect}>Đăng nhập</Button>
        </div>
        </div>
        {contextHolder}
    </div>
}
export default Signup;

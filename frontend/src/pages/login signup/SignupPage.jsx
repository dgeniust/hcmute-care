import React, { useState } from "react";
import { PhoneOutlined, ArrowLeftOutlined } from '@ant-design/icons';
import { Input, Checkbox, Button, message  } from 'antd';
import { useNavigate } from 'react-router-dom';
import {notifySuccessWithCustomMessage, notifyErrorWithCustomMessage} from "../../utils/notificationHelper";
const Signup = () => {
    const navigate = useNavigate();
    const [phone, setPhone] = useState('')
    const [agreeTerms, setAgreeTerms] = useState(false);
    const [phoneStatus, setPhoneStatus] = useState(''); // State for phone input status (error, success)
    const [messageApi, contextHolder] = message.useMessage();
    
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
            notifyErrorWithCustomMessage("Vui lòng đồng ý với điều khoản sử dụng 🫠", messageApi);
        }
        if(phone.length < 10 || phone.length > 11) {
            setPhoneStatus('error');
            valid = false;
            notifyErrorWithCustomMessage("Số điện thoại không hợp lệ, vui lòng kiểm tra lại thông tin 🫠", messageApi);
        }
        return valid;
    }
    const handleSubmit = async (e) => {
        e.preventDefault();
        const isValid = validateForm();

        if(!isValid) return;

        const payload = {
            phone: phone,
        }
        try {
            const response = await fetch("http://localhost:8080/api/v1/auth/register/send-otp", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(payload),
            })
            console.log("Raw response:", response);  // Log response trực tiếp
            if(response.ok ) {
                const result = await response.json();
                if(result.status === 200){
                    const {phone} = result.data;
                    localStorage.setItem("phone", phone);
                    console.log("Data result: ", result.data);
                    notifySuccessWithCustomMessage("Gửi mã OTP thành công, vui lòng kiểm tra tin nhắn SMS của bạn để xác thực tài khoản 🥳",messageApi);
                    setTimeout(() => {
                        navigate('/verifyOTP?type=signup');
                    }, 1000); // Wait for 2 seconds before redirecting
                }
                else {
                    console.log("Error result: ", result.message);
                    notifyErrorWithCustomMessage("Số điện thoại không hợp lệ, vui lòng kiểm tra lại thông tin 🫠",messageApi);
                }
            }
        }
        catch (e) {
            notifyErrorWithCustomMessage("Lỗi kết nối đến server 🫠", messageApi);
            console.error("Login error:", e);
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

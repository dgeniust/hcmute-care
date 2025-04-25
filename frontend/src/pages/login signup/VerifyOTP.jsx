import React, { useState, useEffect } from "react";
import { Input, Button, message, notification } from 'antd';
import { useNavigate, useLocation} from 'react-router-dom';
import { ArrowLeftOutlined, CheckCircleFilled } from '@ant-design/icons';
import {notifySuccess, notifyError, handleHttpStatusCode} from "../../utils/notificationHelper";
const Signup = () => {

    const location = useLocation();
    const queryParams = new URLSearchParams(location.search);
    const type = queryParams.get('type');

    const navigate = useNavigate();
    const [messageApi, contextMessageHolder] = message.useMessage();
    const [seconds, setSeconds] = useState(60);
    const [isTimeUp, setIsTimeUp] = useState(false); // To track if time is up
    const [otp, setOTP] = useState('');
    const [notificationApi, contextNotiHolder] = notification.useNotification();
    
    useEffect(() => {
        if (seconds === 0) {
            setIsTimeUp(true);
            return;
        }; // Stop if the countdown reaches 0
        console.log('otp: ', otp);
        const intervalId = setInterval(() => {
            setSeconds((prevSeconds) => prevSeconds - 1);
        }, 1000);
    
        // Cleanup interval on component unmount
        return () => clearInterval(intervalId);
    }, [seconds]);
    function resetOTP() {
        setSeconds(2);
        setIsTimeUp(false);
        notifySuccess("Gửi lại mã thành công","Gửi lại mã OTP thành công, vui lòng kiểm tra tin nhắn SMS của bạn để xác thực tài khoản 🥳",notificationApi);
    }
    const onChange = (text) => {
        console.log('onChange:', text);
        setOTP(text);
    };
    const onInput = (value) => {
        console.log('onInput:', value);
    };
    const sharedProps = {
        onChange,
        onInput,
    };
    
    const handleSubmit = async (e) => {
        e.preventDefault();
        // Validate the form
        console.log('type: '+ type)
        if(type === 'signup'){
            var phone = localStorage.getItem("phone");
            const payload = {
                phone: phone,
                otp: otp,
            }
            if(!isEmptyOTP) {
                console.log("Payload: ", payload);
                const response = await fetch("http://localhost:8080/api/v1/auth/register/verify-otp", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify(payload),
                })
                console.log("Raw response:", response);  
                if(response.ok ) {
                    const result = await response.json();
                    if(result.status === 200){
                        const {verificationToken} = result.data;
                        localStorage.setItem("verificationToken", verificationToken);
                        console.log("Data result: ", result.data);
                        notifySuccess("Thành công","Xác thực mã OTP thành công 🥳", notificationApi);
                        setTimeout(() => {
                            navigate('/confirmPassword');
                        }, 1000); // Wait for 2 seconds before redirecting
                    }
                    else if(result.status === 400){
                        handleHttpStatusCode(400, "Xác thực mã OTP thất bại", "Mã OTP không chính xác hoặc đã hết hạn. Vui lòng thử lại.");
                        return;
                    }
                    else if(result.status === 500){
                        handleHttpStatusCode(500, "Lỗi hệ thống", "Đã xảy ra lỗi trong quá trình xác thực mã OTP. Vui lòng thử lại sau.");
                        return;
                    }
                    else {
                        notifyErrorWithCustomMessage("Mã OTP không chính xác 🫠", messageApi)
                        return;
                    }
                }
            }
            else {
                notifyErrorWithCustomMessage("Vui lòng nhập mã OTP 🫠",messageApi)
                return;
            }
            
        }
        else if(type === 'getPhone'){
            notifySuccessWithCustomMessage("type get phone",messageApi);
            navigate('/getPassword');
        }
    }
    const isEmptyOTP = otp.trim() === '';
    const formatTime = (seconds) => {
        const minutes = Math.floor(seconds / 60);
        const remainingSeconds = seconds % 60;
        return `${minutes < 10 ? '0' : ''}${minutes}:${remainingSeconds < 10 ? '0' : ''}${remainingSeconds}`;
    };

    return <div className="flex justify-center items-center h-screen w-screen login-bg bg-cover bg-center">
        <div className="flex flex-col justify-center items-center h-[80vh] w-[60vh] border border-black bg-white">
        <div className="w-full flex flex-row justify-between px-3 text-black font-bold text-xl">
            <Button color="default" variant="link" icon={<ArrowLeftOutlined style={{color:'#3498db'}}/>} style={{color: 'black', fontWeight: 'bold', fontSize: '15px'}} onClick={() => navigate('/signup')}>
                Quay lại
            </Button>
        </div>
        <div className="w-full flex flex-row pt-3 items-center px-6">
            <img src="/img/logo_hcmute_care.png" alt="" width={"150px"} height={"150px"} />
            <div className="text-black text-xl font-bold flex-col text-left"><p>Chào mừng đến với</p> <span className="text-sky-700 text-left">HCMUTE CARE</span></div>
        </div>
        <div className="px-6 pb-6 w-full text-left">
            <p className="text-gray-500 font-bold">Nhập mã OTP</p>
        </div>
        <form className="flex flex-col justify-center items-center w-full h-fit" 
            onSubmit={handleSubmit}>
            <div className="w-full h-fit px-8">
                <p className="text-gray-500">Nhập mã OTP được gửi qua số điện thoại của bạn</p>
            </div>
            <div className="w-full h-fit pb-3 px-6 flex-col flex text-black">
                <Input.OTP formatter={(str) => str.toUpperCase()} {...sharedProps} />
            </div>
            <div className="w-full py-6 px-6">
                {
                    isEmptyOTP ? 
                    <Button htmlType="submit" disabled size="large" block>Xác nhận</Button> :
                    <Button htmlType="submit" style={{backgroundColor: '#0958d9', color: 'white' }} size="large" block>Xác nhận</Button>
                }
            </div>
        </form>
        <div className="w-full py-6 px-6 flex flex-col items-center">
            <p className="text-black btn-otp">
                {isTimeUp ? 
                <Button color="primary" variant="link" onClick={resetOTP}>
                    Gửi lại mã
                </Button>
                : "Không nhận được mã?"}</p>
            <p className="text-sky-700 font-bold text-2xl">{formatTime(seconds)}</p>
        </div>
        </div>
        {contextMessageHolder}
        {contextNotiHolder}
    </div>
}
export default Signup;

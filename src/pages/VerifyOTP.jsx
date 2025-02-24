import React, { useState, useEffect } from "react";
import { Input, Button, message, notification } from 'antd';
import { useNavigate, useLocation} from 'react-router-dom';
import { ArrowLeftOutlined, CheckCircleFilled } from '@ant-design/icons';

const Signup = () => {

    const location = useLocation();
    const queryParams = new URLSearchParams(location.search);
    const type = queryParams.get('type');

    const navigate = useNavigate();
    const [messageApi, contextMessageHolder] = message.useMessage();
    const [seconds, setSeconds] = useState(2);
    const [isTimeUp, setIsTimeUp] = useState(false); // To track if time is up
    const [api, contextNotificationHolder] = notification.useNotification();
    const [otp, setOTP] = useState('');
    function openNotification(pauseOnHover) {
      api.open({
        message: 'Gửi lại mã OTP',
        description:
          'Mã xác nhận OTP đã được gửi về tin nhắn điện thoại của bạn.',
        showProgress: true,
        pauseOnHover,
        icon: (<CheckCircleFilled style={{color: '#2ed573'}}/>)
      });
    };
    useEffect(() => {
        if (seconds === 0) {
            setIsTimeUp(true);
            return;
        }; // Stop if the countdown reaches 0
    
        const intervalId = setInterval(() => {
            setSeconds((prevSeconds) => prevSeconds - 1);
        }, 1000);
    
        // Cleanup interval on component unmount
        return () => clearInterval(intervalId);
    }, [seconds]);
    function resetOTP() {
        setSeconds(2);
        setIsTimeUp(false);
        openNotification(true);
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
    const validateForm = () => {
        let fakeOtp = "000000"
        if(otp === fakeOtp) {
            return true;
        }
        return false;
    }
    const handleSubmit = (e) => {
        e.preventDefault();
        const isValid = validateForm();
        // Validate the form
        console.log('type: '+ type)
        if (isValid) {
            if(type === 'signup'){
                success();
                navigate('/confirmPassword');
            }
            else if(type === 'getPhone'){
                success();
                navigate('/getPassword');
            }
        }
        else{
            error()
        }
    }
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
                <Button htmlType="submit" style={{backgroundColor: '#0958d9', color: 'white' }} size="large" block>Xác nhận</Button>
            </div>
        </form>
        <div className="w-full py-6 px-6">
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
        {contextNotificationHolder}
    </div>
}
export default Signup;

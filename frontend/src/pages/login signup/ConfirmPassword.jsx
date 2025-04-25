import React, { useState, useEffect } from "react";
import { ArrowLeftOutlined, LockOutlined, CheckCircleOutlined, CheckCircleFilled } from '@ant-design/icons';
import { Input, Timeline, Button, notification, message } from 'antd';
import { useNavigate } from 'react-router-dom';
import {notifySuccess, notifyError, notifyErrorWithCustomMessage} from "../../utils/notificationHelper";
const ConfirmPassword = () => {
  const navigate = useNavigate();
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [api, contextNotificationHolder] = notification.useNotification();
  const [passwordStatus, setPasswordStatus] = useState('');
  const [messageApi, contextHolder] = message.useMessage();

  const [isValidUppercase, setIsValidUppercase] = useState(false);
  const [isValidPassword, setIsValidPassword] = useState(false);
  const [isValidNumber, setIsValidNumber] = useState(false);
  const [isPasswordMatch, setIsPasswordMatch] = useState(false);
  

  function error(message){
    messageApi.open({
      type: 'error',
      content: message,
    });
  };  
  function openNotification(pauseOnHover) {
        api.open({
          message: 'Đăng kí thành công',
          description:
            'Chúc mừng ! Bạn đã đăng kí thành công.',
          showProgress: true,
          pauseOnHover,
          icon: (<CheckCircleFilled style={{color: '#2ed573'}}/>)
        });
  };
  const handlePasswordChange = (e) => {
    const value = e.target.value;
    setPassword(value);
    validatePassword(value);
  }
  const handleConfirmPasswordChange = (e) => {
    const value = e.target.value;
    setConfirmPassword(value);
    setIsPasswordMatch(value === password);
  };
  const validateForm = () => {
    let valid = true;
    console.log('Password:', password);
    if (password.trim() === "") {
      valid = false;
      setPasswordStatus('error');
      notifyErrorWithCustomMessage("Vui lòng nhập mật khẩu 🫠", messageApi);
    }
    return valid;
  }
  const validatePassword = (password) => {
    const lengthCondition = password.length >= 8;
    const uppercaseCondition = /[A-Z]/.test(password)
    const numberCondition = /\d/.test(password)

    setIsValidNumber(numberCondition);
    setIsValidPassword(lengthCondition);
    setIsValidUppercase(uppercaseCondition);
  }
  const handleSubmit = async (e) => {
    e.preventDefault();
    const isValid = validateForm();
    if(!isValid && !isPasswordMatch) return;
    const payload = {
      password: password,
      confirmPassword: confirmPassword
    }
    try {
      const response = await fetch("http://localhost:8080/api/v1/auth/register/set-password", {
        method: "POST",
        headers: {
          'X-Verification-Token': localStorage.getItem("verificationToken"),
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(payload)
      })
      console.log('Response:', response);
        if(response.ok) {
          const data = await response.json();
          if(data.status === 201) {
            notifySuccess("Thành công", "Đặt mật khẩu thành công 🥳", api);
            setTimeout(() => {
              navigate('/login');
            }, 2000);
          }else {
            notifyErrorWithCustomMessage(data.message || "Mật khẩu không bảo mật 🫠", messageApi);
          }
        }
    } catch (error) {
      console.log('Lỗi server 🫠:', error);
      notifyErrorWithCustomMessage("Lỗi server 🫠", messageApi)
    }

    
  }
  const handleSignupRedirect = () => {
    // Navigate to the Signup page when the button is clicked
    navigate('/login');
  };
  const isButtonDisabled = !isValidPassword || !isValidUppercase || !isValidNumber || !isPasswordMatch;
  return <div className="flex justify-center items-center h-screen w-screen login-bg bg-cover bg-center">
    <div className="flex flex-col justify-center items-center h-[100vh] w-[60vh] border border-black bg-white">
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
    <form className="flex flex-col justify-center items-center w-full h-fit" onSubmit={handleSubmit}>
      <div className="w-full h-fit pb-3 px-6 flex-col flex text-black">
        <label htmlFor="phone" className="justify-start text-left mb-1 font-bold">Nhập mật khẩu</label>
        <Input.Password 
        size="large"
        value={password} 
        status={passwordStatus} 
        onChange={handlePasswordChange}  
        placeholder="Nhập mật khẩu" 
        prefix={<LockOutlined />}/>
      </div>
      <div className="w-full h-fit pb-3 px-6 flex-col flex text-black">
        <label htmlFor="phone" className="justify-start text-left mb-1 font-bold">Nhập lại mật khẩu</label>
        <Input.Password 
        value={confirmPassword} 
        onChange={handleConfirmPasswordChange} 
        status={passwordStatus} size="large" placeholder="Nhập mật khẩu" prefix={<LockOutlined />}/>
      </div>
      <div className="w-full h-fit pt-3 px-6 flex-col flex text-black items-center justify-center">
              <Timeline
                  items={[
                  {
                      dot: isValidPassword ? 
                      <CheckCircleOutlined style={{ color: '#2ed573', fontSize: '20px' }}/> : <CheckCircleOutlined style={{ fontSize: '20px' }}/>,
                      color: isValidPassword ? 'green' : 'gray',
                      fontSize: '22px',
                      children: (
                          <span style={{ color: isValidPassword ? '#2ed573' : 'gray', fontWeight: isValidPassword ? 'bold' : 'normal'  }}>
                            Mật khẩu chứa từ 8 ký tự trở lên
                          </span>
                        ),
                  },
                  {
                      dot: isValidUppercase ?
                      <CheckCircleOutlined style={{ color: '#2ed573', fontSize: '20px' }}/> : <CheckCircleOutlined style={{ fontSize: '20px' }}/>,
                      color: isValidUppercase ? 'green' :'gray',
                      fontSize: '22px',
                      children: (
                          <span style={{ color: isValidUppercase ? '#2ed573' : 'gray', fontWeight: isValidUppercase ? 'bold' : 'normal' }}>
                            Mật khẩu chứa ít nhất 1 chữ hoa
                          </span>
                        ),
                  },
                  {
                      dot: isValidNumber ?
                      <CheckCircleOutlined style={{ color: '#2ed573', fontSize: '20px' }}/> : <CheckCircleOutlined style={{ fontSize: '20px' }}/>,
                      color: isValidNumber ?'green' :'gray',
                      fontSize: '22px',
                      children: (
                          <span style={{ color: isValidNumber ? '#2ed573' : 'gray', fontWeight: isValidNumber ? 'bold' : 'normal'  }}>
                            Mật khẩu chứa ít nhất 1 số
                          </span>
                        ),
                  },
                  {
                      dot: isPasswordMatch ? 
                      <CheckCircleOutlined style={{ color: '#2ed573', fontSize: '20px' }}/> : <CheckCircleOutlined style={{ fontSize: '20px' }}/>,
                      color: isPasswordMatch ?'green' :'gray',
                      fontSize: '22px',
                      children: (
                          <span style={{ color: isPasswordMatch ? '#2ed573' : 'gray', fontWeight: isPasswordMatch ? 'bold' : 'normal'  }}>
                            Xác nhận mật khẩu trùng khớp
                          </span>
                        ),
                  },
                  ]}
              />
            </div>
      <div className="w-full py-6 px-6">
        {
          isButtonDisabled ? 
          <Button disabled style={{backgroundColor: '#bdc3c7', color: 'white' }} size="large" block>Xác nhận</Button> :
          <Button style={{backgroundColor: '#0958d9', color: 'white' }} size="large" block htmlType="submit">Xác nhận</Button>
        }
      </div>
    </form>
    </div>
    {contextHolder}
    {contextNotificationHolder}
  </div>
}
export default ConfirmPassword;

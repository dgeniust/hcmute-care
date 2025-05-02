import React, { useState, useRef } from 'react';
import { Button, Divider, Radio, message } from 'antd';
import {HeartTwoTone, CreditCardTwoTone } from '@ant-design/icons';
import { handleHttpStatusCode, notifyErrorWithCustomMessage, notifySuccessWithCustomMessage } from '../../../utils/notificationHelper';
const style = {
    display: 'flex',
    flexDirection: 'column',
    gap: 8,
};
const MOMO = () => (
    <svg width="20px" height="20px" viewBox="0 0 48 48" xmlns="http://www.w3.org/2000/svg" fill="#D82D8B" stroke="#D82D8B">
      <g strokeWidth="0"></g>
      <g strokeLinecap="round" strokeLinejoin="round" stroke="#CCCCCC" strokeWidth="4.224">
        <circle cx="34.5709" cy="13.4286" r="7.9291" fill="none" stroke="#D82D8B"></circle>
        <path d="m5.5008,21.3573v-11.8915c0-1.9849,1.8504-3.964,3.9644-3.964,2.1186,0,3.9644,1.9783,3.9644,3.964v11.8915" fill="none" stroke="#D82D8B" strokeLinecap="round" strokeLinejoin="round"></path>
        <path d="m13.4288,9.4648c0-1.9849,1.8504-3.964,3.9644-3.964,2.1186,0,3.9644,1.9783,3.9644,3.964v11.8915" fill="none" stroke="#D82D8B" strokeLinecap="round" strokeLinejoin="round"></path>
        <path d="m5.5,42.5v-11.8925c0-1.985,1.8504-3.9642,3.9644-3.9642,2.1186,0,3.9644,1.9784,3.9644,3.9642v11.8925" fill="none" stroke="#D82D8B" strokeLinecap="round" strokeLinejoin="round"></path>
        <path d="m13.4288,30.6075c0-1.985,1.8504-3.9642,3.9644-3.9642,2.1186,0,3.9644,1.9784,3.9644,3.9642v11.8925" fill="none" stroke="#D82D8B" strokeLinecap="round" strokeLinejoin="round"></path>
        <circle cx="34.5709" cy="34.5714" r="7.9291" fill="none" stroke="#D82D8B"></circle>
      </g>
    </svg>
);
const VNPAY = () => (
    <svg width="20px" height="25px" viewBox="0 0 48 48" xmlns="http://www.w3.org/2000/svg" fill="#000000">
      <g strokeWidth="0"></g>
      <g strokeLinecap="round" strokeLinejoin="round">
        <path
          d="m28.6222,37.7222l14.4444-14.4444c.5778-.5778.5778-1.7333,0-2.3111l-8.6667-8.6667c-.5778-.5778-1.7333-.5778-2.3111,0l-6.3556,6.3556-9.2444-9.2444c-.5778-.5778-1.7333-.5778-2.3111,0l-9.2444,9.2444c-.5778.5778-.5778,1.7333,0,2.3111l16.7556,16.7556c1.7333,1.7333,5.2,1.7333,6.9333,0Z"
          fill="none"
          stroke="#ff0000"
          strokeLinejoin="round"
        />
        <path
          d="m25.7333,18.6556l-8.0889,8.0889c-2.3111,2.3111-4.6222,2.3111-6.9333,0"
          fill="none"
          stroke="#ff0000"
          strokeLinecap="round"
          strokeLinejoin="round"
        />
        <path
          d="m18.2222,30.7889c-1.1556,1.1556-2.3111,1.1556-3.4667,0m22.5333-15.6c-1.262-1.1556-2.8889-.5778-4.0444.5778l-15.0222,15.0222"
          fill="none"
          stroke="#ff0000"
          strokeLinecap="round"
          strokeLinejoin="round"
        />
        <path
          d="m18.2222,15.7667c-4.6222-4.6222-10.4,1.1556-5.7778,5.7778l5.2,5.2-5.2-5.2"
          fill="none"
          stroke="#ff0000"
          strokeLinecap="round"
          strokeLinejoin="round"
        />
        <path
          d="m23.4222,20.9667l-4.0444-4.0444"
          fill="none"
          stroke="#ff0000"
          strokeLinecap="round"
          strokeLinejoin="round"
        />
        <path
          d="m21.6889,22.7l-4.6222-4.6222c-.5778-.5778-1.4444-1.4444-2.3111-1.1556"
          fill="none"
          stroke="#ff0000"
          strokeLinecap="round"
          strokeLinejoin="round"
        />
        <path
          d="m14.7556,20.3889c-.5778-.5778-1.4444-1.4444-1.1556-2.3111m5.7778,6.9333l-4.6222-4.6222"
          fill="none"
          stroke="#ff0000"
          strokeLinecap="round"
          strokeLinejoin="round"
        />
      </g>
    </svg>
);
  
const Payment_Booking = ({bookingList, setCurrent}) => {
    const ref1 = useRef(null);
    const ref2 = useRef(null);
    const [valueBanking, setValueBanking] = useState(2);
    const [messageApi, contextHolder] = message.useMessage();
    const appointmentId = localStorage.getItem('appointmentId');
    const onChange = (e) => {
        setValueBanking(e.target.value);
    };
    const handlePayment = async () => {
        try {
            const payload = {
                bankCode: 'NCB',
                appointmentId: appointmentId
            }
            const response = await fetch(`http://localhost:8080/api/v1/payments/vn-pay`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
                },
                body: JSON.stringify(payload)
            });
            console.log("Raw response payment",response);
            if(!response.ok) {
                const errorText = await response.text();
                handleHttpStatusCode(response.status, "", errorText, messageApi );
                return;
            }
            const data = await response.json();
            console.log("Data payment",data);
            // Kiểm tra response và redirect nếu thành công
            if(data) {
                if (data.status === 200 && data.data) {
                    setTimeout(() => {
                        notifySuccessWithCustomMessage('Đặt khám thành công, đang chuyển hướng tới trang thanh toán', messageApi);
                    }, 500);
                    window.location.href = data.data;
                } else {
                    notifyErrorWithCustomMessage('Không tìm thấy URL thanh toán trong response.', messageApi);
                }
            }
        }
        catch (error) {
            console.error('Error during payment:', error);
            notifyErrorWithCustomMessage('Có lỗi xảy ra trong quá trình thanh toán. Vui lòng thử lại sau.', messageApi);
        }

    }
    return (
        <div className='w-full h-fit min-h-[460px] border border-red-600 p-8 bg-gray-100 space-y-4'>
            <div className='rounded-lg space-y-2'>
                <div className='text-black w-full h-fit p-4 text-base'>
                    <p>Vui lòng kiểm tra thông tin đăng ký và chọn phương thức thanh toán.</p>
                    <p className='text-[#273c75] font-bold'>Sau khi thanh toán thành công, bạn vui lòng đợi nhận <span className='italic'>PHIẾU KHÁM BỆNH</span>, không đóng ứng dụng.</p>
                </div>
                <div className='flex flex-col' ref={ref1}>
                    <div className='flex flex-row space-x-4 bg-white p-4'>
                        <HeartTwoTone twoToneColor="#eb2f96" />
                        <h1 className='text-[#273c75] font-bold text-base'>Chuyên khoa đã chọn (1)</h1>
                    </div>
                    {
                        bookingList.map((item, index) => (
                            <div key={index}>
                                <div className='flex flex-row space-x-4 bg-white p-4 justify-between text-black'>
                                    <p className='text-sm'>
                                        {item.specialty}
                                    </p>
                                    <p className='text-[#273c75]'>{item.price}</p>
                                </div>
                                <Divider
                                style={{
                                    borderColor: 'gray', margin: '0'
                                }} dashed
                                >
                                </Divider>
                            </div>
                        ))
                    }
                </div>
            </div>
            <div className='space-y-4 bg-white p-4 rounded-lg' ref={ref2}>
                <div className='flex flex-row space-x-4 bg-white'>
                    <CreditCardTwoTone twoToneColor="#eb2f96"/>
                    <h1 className='text-[#273c75] font-bold text-base'>Phương thức thanh toán</h1>
                </div>
                <div>
                <Radio.Group
                    style={style}
                    onChange={onChange}
                    value={valueBanking}
                    options={[
                        {
                        value: 1,
                        label: (
                            <div className='flex flex-row space-x-2 items-center p-2'>
                                <MOMO />
                                <span className={`text-base ${
                                valueBanking === 1 ? 'text-green-500' : ''
                                }`}>Ví điện tử MoMo</span>
                            </div>
                        ),
                        },
                        {
                        value: 2,
                        label: (
                            <div className='flex flex-row space-x-2 items-center p-2'>
                                <VNPAY />
                                <span className={`text-base ${
                                valueBanking === 2 ? 'text-green-500' : ''
                                }`}>Ví điện tử VNPAY</span>
                            </div>
                        ),
                        },
                    ]}
                    />
                </div>
            </div>
            <div>
                <Button type="primary" className='w-full h-12 bg-[#273c75] hover:bg-[#273c75] text-white font-bold' onClick={handlePayment}>
                    Thanh toán
                </Button>
            </div>
            {contextHolder}
        </div>
    )
}
export default Payment_Booking;
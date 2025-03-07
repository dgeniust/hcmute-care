import React, {useEffect, useState} from 'react';
import { Button, message, Steps, theme } from 'antd';
import { LoadingOutlined, SnippetsOutlined, CreditCardOutlined, UserOutlined, ForkOutlined } from '@ant-design/icons';
import CreateProfile_Booking from './Booking/1/CreateProfile_Booking';
import CureInfo_Booking from './Booking/2/CureInfo_Booking';
import '../css/BookingContent.css';
const steps = [
    {
      title: '1. Hồ sơ',
      content: <CreateProfile_Booking/>,
      icon: <UserOutlined />,
    },
    {
      title: '2. Thông tin khám',
      content: <CureInfo_Booking/>,
      icon: <ForkOutlined />,
    },
    {
      title: '3. Thông tin',
      content: 'Kiểm tra thông tin đặt khám',
      icon: <SnippetsOutlined />,
    },
    {
      title: '4. Thanh toán',
      content: 'Phương thức thanh toán',
      icon: <CreditCardOutlined />,
    },
    {
      title: '5. In bill',
      content: 'Kiểm tra số phiếu',
      icon: <SnippetsOutlined />,
    },
  ];
const BookingContent = () => {
    const { token } = theme.useToken();
    const [current, setCurrent] = useState(0);
    const next = () => {
        setCurrent(current + 1);
    };
    const prev = () => {
        setCurrent(current - 1);
    };
    const items = steps.map((item) => ({
        key: item.title,
        title: item.title,
        icon: item.icon,
    }));
    const contentStyle = {
        marginTop: 16,
        backgroundColor: token.colorFillAlter,
        borderRadius: token.borderRadiusLG,
        border: `1px dashed ${token.colorBorder}`,
        height: 'fit-content',
    };
    return (
        <div className='flex flex-col items-center w-full h-[100vh] gap-4'>
            <h1 className='text-xl mt-4 text-[#273c75] font-bold'>ĐẶT KHÁM</h1>
            <div className='w-full p-8 h-full'>
                <Steps current={current} items={items}/>
                <div style={contentStyle}>{steps[current].content}</div>
                <div
                    style={{
                    marginTop: 24,
                    }}
                >
                    {current < steps.length - 1 && (
                    <Button type="primary" onClick={() => next()}>
                        Next
                    </Button>
                    )}
                    {current === steps.length - 1 && (
                    <Button type="primary" onClick={() => message.success('Processing complete!')}>
                        Done
                    </Button>
                    )}
                    {current > 0 && (
                    <Button
                        style={{
                        margin: '0 8px',
                        }}
                        onClick={() => prev()}
                    >
                        Previous
                    </Button>
                    )}
                </div>
            </div>
        </div>
    );
}

export default BookingContent;
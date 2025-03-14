import React, {useEffect, useState, useRef} from 'react';
import { Button, message, Steps, theme, Tour  } from 'antd';
import { LoadingOutlined, SnippetsOutlined, CreditCardOutlined, UserOutlined, ForkOutlined } from '@ant-design/icons';
import CreateProfile_Booking from './Booking/1/CreateProfile_Booking';
import CureInfo_Booking from './Booking/2/CureInfo_Booking';
import ConfirmInfo_Booking from './Booking/3/ConfirmInfo_Booking';
import Payment_Booking from './Booking/4/Payment_Booking';
import ConfirmBill_Booking from './Booking/5/ConfirmBill_Booking';
import '../css/BookingContent.css';
import logo from '../assets/Logo_2.png'
const BookingContent = () => {
    
    const ref1 = useRef(null);
    const ref2 = useRef(null);
    const ref3 = useRef(null); // Ref cho ChooseProfile_Booking
    const ref4 = useRef(null);
    const refs = {
        ref3, ref4
    }
    const steps = [
        {
          title: '1. Hồ sơ',
          content: <CreateProfile_Booking refs={refs}/>,
          icon: <UserOutlined />,
        },
        {
          title: '2. Thông tin khám',
          content: <CureInfo_Booking/>,
          icon: <ForkOutlined />,
        },
        {
          title: '3. Thông tin',
          content: <ConfirmInfo_Booking/>,
          icon: <SnippetsOutlined />,
        },
        {
          title: '4. Thanh toán',
          content: <Payment_Booking/>,
          icon: <CreditCardOutlined />,
        },
        {
          title: '5. In bill',
          content: <ConfirmBill_Booking/>,
          icon: <SnippetsOutlined />,
        },
      ];
    
    const tour_steps = [
        {
            title: 'Chào mừng đến với hệ thống đặt lịch khám bệnh',
            cover: (
                <img src={logo} width={100} height={100}/>
            ),  
            // description: 'Put your files here.',
            target: () => ref1.current,
        },
        {
            title: 'Các bước cần thực hiện khi đặt khám',
            description: 'Thực hiện theo các bước sau để đặt được phiếu khám bệnh',
            target: () => ref2.current,
        },
        {
            title: 'Tạo hồ sơ của bạn',
            description: 'Bạn có thể thêm hồ sơ mới tại đây.',
            target: () => ref3.current, // Mục tiêu của ChooseProfile_Booking
        },
        {
            title: 'Chọn hồ sơ của bạn',
            description: 'Bạn có thể chọn hồ sơ của bạn tại đây.',
            target: () => ref4.current, // Mục tiêu của ChooseProfile_Booking
        },
    ]

    const { token } = theme.useToken();
    const [current, setCurrent] = useState(0);
    const [open, setOpen] = useState(false);

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
    useEffect(() => {
        setOpen(true); // Mở tour khi component được mount
    }, []);
    return (
        <div className='flex flex-col items-center w-full h-[100vh] gap-4'>
            <h1 className='text-xl mt-4 text-[#273c75] font-bold'>ĐẶT KHÁM</h1>
            <div className='w-full p-8 h-full'>
                <Steps current={current} items={items} ref={ref2}/>
                <div style={contentStyle} ref={ref1}>{steps[current].content}</div>
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
            <Tour open={open} onClose={() => setOpen(false)} steps={tour_steps} />
        </div>
    );
}

export default BookingContent;
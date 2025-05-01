import React, { useEffect, useState, useRef } from 'react';
import { Button, message, Steps, theme, Tour } from 'antd';
import { SnippetsOutlined, CreditCardOutlined, UserOutlined, ForkOutlined } from '@ant-design/icons';
import CreateProfile_Booking from '../../components/Booking/1/CreateProfile_Booking';
import CureInfo_Booking from '../../components/Booking/2/CureInfo_Booking';
import ConfirmInfo_Booking from '../../components/Booking/3/ConfirmInfo_Booking';
import Payment_Booking from '../../components/Booking/4/Payment_Booking';
import ConfirmBill_Booking from '../../components/Booking/5/ConfirmBill_Booking';
import '../../css/bookingcontent.css';
import logo from '../../assets/Logo_2.png';

const BookingContent = () => {
  const ref1 = useRef(null);
  const ref2 = useRef(null);
  const ref3 = useRef(null);
  const ref4 = useRef(null);
  const refs = { ref3, ref4 };

  const [bookingList, setBookingList] = useState([]);
  const [current, setCurrent] = useState(0);
  const [messageApi, contextHolder] = message.useMessage();

  const addBooking = (newBookingElement) => {
    // Check for duplicate specialty
    const isDuplicateSpecialty = bookingList.some(
      (item) => item.specialty.trim().toLowerCase() === newBookingElement.specialty.trim().toLowerCase()
    );

    if (isDuplicateSpecialty) {
      messageApi.error('Chuyên khoa này đã được chọn. Vui lòng chọn chuyên khoa khác.');
      setCurrent(0); // Reset to the first step
      return false; // Indicate failure
    }

    // Check for duplicate time slot
    const isDuplicateTime = bookingList.some(
      (item) => item.date === newBookingElement.date && item.time === newBookingElement.time
    );

    if (isDuplicateTime) {
      messageApi.error('Giờ khám này đã được chọn. Vui lòng chọn giờ khác.');
      return false; // Indicate failure
    }

    // Check for exact duplicate booking
    const isDuplicate = bookingList.some(
      (item) =>
        item.specialty === newBookingElement.specialty &&
        item.date === newBookingElement.date &&
        item.time === newBookingElement.time &&
        item.room === newBookingElement.room &&
        item.doctor === newBookingElement.doctor &&
        item.scheduleSlotId === newBookingElement.scheduleSlotId
    );

    if (isDuplicate) {
      messageApi.success('Thông tin khám đã được lưu lại');
      return true; // Indicate success (no need to add duplicate)
    }

    // Add new booking
    setBookingList([...bookingList, newBookingElement]);
    messageApi.success('Đã thêm thông tin khám');
    return true; // Indicate success
  };

  const steps = [
    {
      title: '1. Hồ sơ',
      content: <CreateProfile_Booking refs={refs} setCurrent={setCurrent} />,
      icon: <UserOutlined />,
    },
    {
      title: '2. Thông tin khám',
      content: (
        <CureInfo_Booking
          bookingList={bookingList}
          addBooking={addBooking} // Pass addBooking function
          setCurrent={setCurrent}
        />
      ),
      icon: <ForkOutlined />,
    },
    {
      title: '3. Thông tin',
      content: (
        <ConfirmInfo_Booking
          bookingList={bookingList}
          setBookingList={setBookingList}
          setCurrent={setCurrent}
        />
      ),
      icon: <SnippetsOutlined />,
    },
    {
      title: '4. Thanh toán',
      content: <Payment_Booking setCurrent={setCurrent} bookingList={bookingList} />,
      icon: <CreditCardOutlined />,
    },
    {
      title: '5. In bill',
      content: <ConfirmBill_Booking setCurrent={setCurrent} bookingList={bookingList} />,
      icon: <SnippetsOutlined />,
    },
  ];

  const { token } = theme.useToken();

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
    <div className="flex flex-col items-center w-full h-[100vh] gap-4">
      {contextHolder}
      <h1 className="text-xl mt-4 text-[#273c75] font-bold">ĐẶT KHÁM</h1>
      <div className="w-full p-8 h-full">
        <Steps current={current} items={items} ref={ref2} />
        <div style={contentStyle} ref={ref1}>
          {steps[current].content}
        </div>
      </div>
    </div>
  );
};

export default BookingContent;
import React, { useState, useEffect, useRef } from 'react';
import { Button, Divider, Radio, message } from 'antd';
import { ArrowLeftOutlined, RestOutlined } from '@ant-design/icons';
import Specialty_Booking from './Specialty_Booking';
import Date_Booking from './Date_Booking';
import TimeADoctor_Booking from './TimeADoctor_Booking';
import Timeline_Booking from './Timeline_Booking';
import { notifyErrorWithCustomMessage } from '../../../utils/notificationHelper';

const CureInfo_Booking = ({ bookingList, addBooking, setCurrent }) => {
  const ref1 = useRef(null);
  const ref2 = useRef(null);

  const [specialty, setSpecialty] = useState('');
  const [price, setPrice] = useState('');
  const [choosedSpecialty, setChoosedSpecialty] = useState(false);
  const [step, setStep] = useState(1);
  const [selectedValue, setSelectedValue] = useState(null);
  const [selectedRoom, setSelectedRoom] = useState(null);
  const [selectedTime, setSelectedTime] = useState(null);
  const [selectedDoctor, setSelectedDoctor] = useState(null);
  const [radio1, setRadio1] = useState();
  const [radio2, setRadio2] = useState();
  const [clickNext, setClickNext] = useState(false);
  const [messageApi, contextHolder] = message.useMessage();
  const [specialtyId, setSpecialtyId] = useState(null); // Lưu specialtyId
  const [scheduleSlotId, setScheduleSlotId] = useState(null); // Lưu scheduleSlotId
  const onSelectDate = (newValue) => {
    setSelectedValue(newValue);
    setStep(3);
  };

  const handleSlotClick = (time, doctorName, room, slotId) => {
    setSelectedTime(time);
    setSelectedDoctor(doctorName);
    setSelectedRoom(room);
    setScheduleSlotId(slotId); // Lưu scheduleSlotId
  };

  const radioOnchange1 = ({ target: { value } }) => {
    setRadio1(value);
  };
  const radioOnchange2 = ({ target: { value } }) => {
    setRadio2(value);
  };

  useEffect(() => {
    console.log('bookingList changed:', bookingList);
  }, [bookingList]);

  const handleClickNext = () => {
    if (radio1 === undefined) {
      notifyErrorWithCustomMessage('Vui lòng chọn thông tin bảo hiểm y tế', messageApi);
      return;
    }
    if (radio2 === undefined) {
      notifyErrorWithCustomMessage('Vui lòng chọn thông tin bảo lãnh viện phí', messageApi);
      return;
    }

    const newBookingElement = {
      specialtyId,
      specialty,
      price,
      date: selectedValue ? selectedValue.format('DD-MM-YYYY') : null,
      time: selectedTime,
      room: selectedRoom,
      doctor: selectedDoctor,
      insurance: radio1 === 1 ? 'Có' : 'Không',
      guarantee: radio2 === 1 ? 'Có' : 'Không',
      scheduleSlotId,
    };

    // Lưu booking vào localStorage
    let bookings = JSON.parse(localStorage.getItem('bookings') || '[]');
    const existingBookingIndex = bookings.findIndex((booking) => booking.specialtyId === specialtyId);

    if (existingBookingIndex !== -1) {
      // Cập nhật booking hiện có
      bookings[existingBookingIndex] = {
        ...bookings[existingBookingIndex],
        specialtyName: specialty,
        dateBooking: localStorage.getItem('dateBooking'),
        scheduleSlotId: scheduleSlotId.toString(),
        timeString: selectedTime,
        doctorName: selectedDoctor,
        roomName: selectedRoom,
      };
    } else {
      // Thêm booking mới
      bookings.push({
        specialtyId,
        specialtyName: specialty,
        dateBooking: localStorage.getItem('dateBooking'),
        scheduleSlotId: scheduleSlotId.toString(),
        timeString: selectedTime,
        doctorName: selectedDoctor,
        roomName: selectedRoom,
      });
    }

    localStorage.setItem('bookings', JSON.stringify(bookings));

    const success = addBooking(newBookingElement);
    if (success) {
      setClickNext(true);
      setCurrent(2);
    }
  };

  const result = selectedValue ? selectedValue.format('YYYY-MM-DD') : null;
  localStorage.setItem('dateBooking', result);

  return (
    <div className="w-full h-fit min-h-[460px] border p-8">
      <div className="flex flex-row gap-4 w-full h-full items-center">
        <Button
          icon={<ArrowLeftOutlined />}
          style={{ backgroundColor: 'transparent', border: 'none', boxShadow: 'none' }}
          onClick={
            () => setCurrent((prev) => Math.max(0, prev - 1))}
        ></Button>
        <h1 className="text-black font-bold text-lg">Chọn thông tin khám</h1>
      </div>
      {!clickNext ? (
        <div className="w-full h-full flex flex-row space-x-4 mt-4 justify-center items-center">
          <div className="flex flex-col w-1/4 h-fit">
            <Timeline_Booking
              choosedSpecialty={choosedSpecialty}
              specialty={specialty}
              step={step}
              result={result}
              selectedValue={selectedValue}
              selectedTime={selectedTime}
              selectedDoctor={selectedDoctor}
              ref={ref1}
            />
            {selectedTime && (
              <div className="w-full h-full p-4">
                <Divider
                  variant="dashed"
                  style={{ borderColor: '#7cb305', width: 'fit-content', height: 'fit-content', margin: '5px' }}
                  dashed
                ></Divider>
                <div className="w-full h-full flex flex-col">
                  <p className="font-bold text-black text-sm tracking-wider">Bảo hiểm Y tế:</p>
                  <div>
                    <Radio.Group
                      name="radiogroup"
                      options={[
                        { value: 1, label: 'Có' },
                        { value: 2, label: 'Không' },
                      ]}
                      onChange={radioOnchange1}
                    />
                  </div>
                </div>
                <Divider
                  variant="dashed"
                  style={{ borderColor: '#7cb305', width: 'fit-content', height: 'fit-content', margin: '5px' }}
                  dashed
                ></Divider>
                <div className="w-full h-full flex flex-col">
                  <p className="font-bold text-black text-sm tracking-wider">Bảo lãnh viện phí:</p>
                  <div>
                    <Radio.Group
                      name="radiogroup"
                      options={[
                        { value: 1, label: 'Có' },
                        { value: 2, label: 'Không' },
                      ]}
                      onChange={radioOnchange2}
                    />
                  </div>
                </div>
              </div>
            )}
          </div>
          {step === 1 && (
            <Specialty_Booking
              setSpecialty={setSpecialty}
              setSpecialtyId={setSpecialtyId} // Thêm prop để set specialtyId
              setPrice={setPrice}
              setChoosedSpecialty={setChoosedSpecialty}
              setStep={setStep}
              ref={ref2}
            />
          )}
          {choosedSpecialty && step === 2 && <Date_Booking selectedValue={selectedValue} onSelectDate={onSelectDate} />}
          {step === 3 && <TimeADoctor_Booking handleSlotClick={handleSlotClick} />}
        </div>
      ) : null}
      {selectedTime && (
        <div className="flex flex-col justify-center items-center w-full h-fit p-8">
          <div className="flex flex-row justify-center items-center w-full h-fit space-x-4">
            <Button
              type="primary"
              className="w-full h-fit mt-4"
              style={{
                width: '200px',
                height: '40px',
                fontSize: '15px',
                fontWeight: 'bold',
                backgroundColor: 'blue',
              }}
              onClick={handleClickNext}
              disabled={clickNext}
            >
              Tiếp tục
            </Button>
          </div>
        </div>
      )}
      {contextHolder}
    </div>
  );
};

export default CureInfo_Booking;
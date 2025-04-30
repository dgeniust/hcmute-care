import React, { useState, useEffect, useRef } from 'react';
import { Button, Divider, Radio, notification, Tour } from 'antd';
import {ArrowLeftOutlined, ForkOutlined, BulbTwoTone, CaretDownOutlined,RestOutlined} from '@ant-design/icons';
import Specialty_Booking from './Specialty_Booking';
import Date_Booking from './Date_Booking';
import TimeADoctor_Booking from './TimeADoctor_Booking';
import Timeline_Booking from './Timeline_Booking';
const CureInfo_Booking = () => {
    const ref1 = useRef(null);
    const ref2 = useRef(null);
    const [open, setOpen] = useState(false);
    const tour_steps = [
        {
            title: 'Bắt đầu đặt khám',
            description: 'Để đặt phiếu khám bệnh, bạn cần hoàn thành 4 bước sau: chọn chuyên khoa, chọn ngày giờ, xác nhận thông tin và thanh toán. Hãy làm theo hướng dẫn để hoàn tất quá trình.',
            target: () => ref1.current,
        },
        {
            title: 'Lựa chọn chuyên khoa và lịch khám',
            description: 'Tại đây, bạn có thể chọn chuyên khoa, bác sĩ và ngày giờ khám phù hợp với nhu cầu của mình. Hãy chọn thông tin một cách cẩn thận để đảm bảo lịch khám được đặt chính xác.',
            target: () => ref2.current,
        },
    ];
    useEffect(() => {
        setOpen(true); // Mở tour khi component được mount
    }, []);
    const [bookingList, setBookingList] = useState([]);
    const [specialty, setSpecialty] = useState('');
    const [price, setPrice] = useState('');
    const [choosedSpecialty, setChoosedSpecialty] = useState(false)

    const [step, setStep] = useState(1); // Different steps (1, 2, 3, 4)


    const [selectedValue, setSelectedValue] = useState(null);
    const [selectedRoom, setSelectedRoom] = useState(null);
    const [selectedTime, setSelectedTime] = useState(null);
    const [selectedDoctor, setSelectedDoctor] = useState(null);

    const [radio1, setRadio1] = useState();
    const [radio2, setRadio2] = useState();

    const [clickNext, setClickNext] = useState(false);
    const [clickContinue, setClickContinue] = useState(false);

    const [api, contextHolder] = notification.useNotification();
    const onSelectDate = (newValue) => {
        setSelectedValue(newValue)
        setStep(3)
    }
    
    const handleSlotClick = (time, doctorName, room) => {
        setSelectedTime(time);
        setSelectedDoctor(doctorName);
        setSelectedRoom(room);
    };

    const radioOnchange1 = ({ target: { value } }) => {
        setRadio1(value)
    }
    const radioOnchange2 = ({ target: { value } }) => {
        setRadio2(value)
    }
    useEffect(() => {
        console.log('bookingList changed:', bookingList);
    }, [bookingList]);

    const newBooking = () => {
        const newBookingElement = {
            specialty: specialty,
            price: price,
            date: selectedValue ? selectedValue.format('DD-MM-YYYY') : null,
            time: selectedTime,
            room: selectedRoom,
            doctor: selectedDoctor,
            insurance: radio1 === 1 ? 'Có' : 'Không',
            guarantee: radio2 === 1 ? 'Có' : 'Không',
        };
        const isDuplicate = bookingList.some(item =>
            item.specialty === newBookingElement.specialty &&
            item.date === newBookingElement.date &&
            item.time === newBookingElement.time &&
            item.room === newBookingElement.room &&
            item.doctor === newBookingElement.doctor
        );
    
        if (isDuplicate) {
            openNotification('Thông tin khám đã được lưu lại');
        } else {
            // Thêm phần tử mới vào bookingList
            setBookingList(prevList => [...prevList, newBookingElement]); // Cập nhật lại state
            openNotification('Đã thêm thông tin khám');
        }
    }
    const handleClickNext = () => {
        if (radio1 === undefined ) {
            openNotification('Vui lòng chọn thông tin bảo hiểm y tế');
        } 
        else if(radio2 === undefined) {
            openNotification('Vui lòng chọn thông tin bảo lãnh viện phí');
        } 
        else {
            setClickNext(true);
            newBooking();
            console.log('bookingList: ', bookingList);
        }
    }
    const continueBooking = () => {
        try {
            if (radio1 === undefined ) {
                openNotification('Vui lòng chọn thông tin bảo hiểm y tế');
            } 
            else if(radio2 === undefined) {
                openNotification('Vui lòng chọn thông tin bảo lãnh viện phí');
            } 
            else {
                newBooking();
                setClickContinue(true);
                setClickNext(false);
                resetBooking();
                console.log('bookingList: ', bookingList);
            }
        } catch (error) {
            console.error('Lỗi trong continueBooking:', error);
        }
    };
    const resetBooking = () => {
        setSpecialty('');
        setPrice('');
        setChoosedSpecialty(false);
        setStep(1);
        setSelectedValue(null);
        setSelectedTime(null);
        setSelectedRoom(null);
        setSelectedDoctor(null);
        setRadio1(undefined);
        setRadio2(undefined);
    }
    
    const openNotification = (message) => {
        try {
            api.open({
                message: 'Thông báo',
                description: message ,
                icon: <BulbTwoTone />,
                duration: 0,
            });
        } catch (error) {
            console.error('Lỗi trong openNotification:', error);
        }
    };
    
    const result = selectedValue ? selectedValue.format('YYYY-MM-DD') : null ; // Ensure selectedValue is dayjs object

    localStorage.setItem('dateBooking', result)

    console.log('step: ', step)

    return (
        <div className='w-full h-fit min-h-[460px] border border-red-600 p-8'>
            <div className='flex flex-row gap-4 w-full h-full items-center' onClick={() => handleSetStatus('records')}>
                <Button icon={<ArrowLeftOutlined />} style={{backgroundColor:'transparent', border: 'none', boxShadow: 'none'}}></Button>
                <h1 className='text-black font-bold text-lg'>Chọn thông tin khám</h1>
            </div>
            {
                !clickNext ? (
                    <div className='w-full h-full flex flex-row space-x-4 mt-4 justify-center items-center'>
                        <div className='flex flex-col w-1/4 h-fit'>
                            <Timeline_Booking choosedSpecialty={choosedSpecialty} specialty={specialty} step={step} result={result} selectedValue={selectedValue} selectedTime={selectedTime} selectedDoctor={selectedDoctor} ref={ref1}/>
                            {
                                selectedTime && (
                                    <div className='w-full h-full p-4'>
                                        <Divider variant="dashed" style={{ borderColor: '#7cb305', width:'fit-content', height: 'fit-content', margin:'5px' }} dashed></Divider>
                                        <div className='w-full h-full flex flex-col'>
                                            <p className='font-bold text-black text-sm tracking-wider'>Bảo hiểm Y tế:</p>
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
                                        <Divider variant="dashed" style={{ borderColor: '#7cb305', width:'fit-content', height: 'fit-content', margin:'5px' }} dashed></Divider>
                                        <div className='w-full h-full flex flex-col'>
                                            <p className='font-bold text-black text-sm tracking-wider'>Bảo lãnh viện phí:</p>
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
                                )
                            }
                        </div>
                        {step === 1 && (
                            <Specialty_Booking setSpecialty = {setSpecialty} setPrice ={setPrice} setChoosedSpecialty = {setChoosedSpecialty} setStep = {setStep} ref={ref2}/>
                        )}
                        {choosedSpecialty && step === 2 && ( 
                            <Date_Booking selectedValue={selectedValue} onSelectDate={onSelectDate}/>
                        
                        )}
                        {step === 3 && (
                            <TimeADoctor_Booking handleSlotClick={handleSlotClick}/>
                        )}
                    </div>
                ) : null
            }
            {
                selectedTime !== null && selectedDoctor !== null && clickNext ?(
                    <div className='w-full h-full flex flex-col space-x-4 mt-4 border border-black rounded-xl min-h-fit'>
                        <div className='w-full h-fit flex flex-row justify-between items-center text-black p-4'>
                            <p className='text-black'>Chuyên khoa đã chọn <span className='text-[#273c75] font-bold'>(2)</span></p>
                            <CaretDownOutlined />
                        </div>
                        <Divider variant="dashed" style={{ borderColor: '#7cb305', margin: '0'}} dashed></Divider>
                        {
                            bookingList.map((item, index) => (
                                <div className='w-full h-fit flex flex-col justify-between items-center text-black' key={index}>
                                    <div className='w-full h-fit flex flex-row justify-between items-center text-black'>
                                        <div className="w-[95%] h-fit grid grid-cols-2 grid-flow-row gap-3 text-black p-4">
                                            <div className='grid grid-flow-row grid-cols-[150px_1fr]'>
                                                <p>Chuyên khoa: </p>
                                                <p className='font-bold text-[#273c75]'>{item.specialty}</p>
                                            </div>
                                            <div className='grid grid-flow-row grid-cols-[150px_1fr]'>
                                                <p>Phí khám: </p>
                                                <p className='font-bold text-[#273c75]'>{item.price}</p>
                                            </div>
                                            <div className='grid grid-flow-row grid-cols-[150px_1fr]'>
                                                <p>Ngày khám: </p>
                                                <p className='font-bold text-[#273c75]'>{item.date}</p>
                                            </div>
                                            <div className='grid grid-flow-row grid-cols-[150px_1fr]'>
                                                <p>Phòng - Giờ khám: </p>
                                                <p className='font-bold text-[#273c75]'>{item.room}</p>
                                            </div>
                                        </div>
                                        <div className='w-[5%]'>
                                            <RestOutlined style={{color: 'red', border: '1px solid red', borderRadius:'50%', width:'20px', height:'20px', display: 'flex', justifyContent:"center", backgroundColor:'transparent', cursor:'pointer'}}/>
                                        </div>
                                    </div>
                                    {/* Only show Divider if it's not the last item */}
                                    {index !== bookingList.length - 1 && (
                                        <Divider variant="dashed" style={{ borderColor: '#7cb305', margin: '0' }} dashed />
                                    )}
                                </div>
                            ))
                        }
                    </div>
                ) : null
            }
            {
                selectedTime && (
                    <div className='flex flex-col justify-center items-center w-full h-fit p-8 '>
                        <div className='flex flex-row justify-center items-center w-full h-fit space-x-4'>
                            <Button type="primary" className='w-full h-fit mt-4' icon={<ForkOutlined/>} style={{width:'300px', height:'40px', fontSize:'15px', fontWeight:'bold', backgroundColor:'white', color:'blue', border:'1px solid blue'}} onClick={continueBooking}>Thêm chuyên khoa</Button>
                            <Button type="primary" className='w-full h-fit mt-4' style={{width:'200px', height:'40px', fontSize:'15px', fontWeight:'bold', backgroundColor:'blue'}} onClick={handleClickNext}>Tiếp tục</Button>
                        </div>
                    </div>
                )
            }
            {contextHolder}
            <Tour open={open} onClose={() => setOpen(false)} steps={tour_steps} />
        </div>
    );
};

export default CureInfo_Booking;

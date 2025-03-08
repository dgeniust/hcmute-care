import React, { useState } from 'react';
import { Button, Divider, Radio, notification } from 'antd';
import {ArrowLeftOutlined, ForkOutlined, BulbTwoTone} from '@ant-design/icons';
import Specialty_Booking from './Specialty_Booking';
import Date_Booking from './Date_Booking';
import TimeADoctor_Booking from './TimeADoctor_Booking';
import Timeline_Booking from './Timeline_Booking';
const CureInfo_Booking = () => {
    const [specialty, setSpecialty] = useState('');
    const [price, setPrice] = useState('');
    const [choosedSpecialty, setChoosedSpecialty] = useState(false)

    const [step, setStep] = useState(1); // Different steps (1, 2, 3, 4)


    const [selectedValue, setSelectedValue] = useState(null);

    const [selectedTime, setSelectedTime] = useState(null);
    const [selectedDoctor, setSelectedDoctor] = useState(null);

    const [radio1, setRadio1] = useState();
    const [radio2, setRadio2] = useState();

    const [api, contextHolder] = notification.useNotification();
    
    const onSelectDate = (newValue) => {
        setSelectedValue(newValue)
        setStep(3)
    }
    
    const handleSlotClick = (time, doctorName) => {
        setSelectedTime(time);
        setSelectedDoctor(doctorName);
    };

    const radioOnchange1 = ({ target: { value } }) => {
        setRadio1(value)
    }
    const radioOnchange2 = ({ target: { value } }) => {
        setRadio2(value)
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
                openNotification('Đã thêm thông tin khám');
            }
        } catch (error) {
            console.error('Lỗi trong continueBooking:', error);
        }
    };
    
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
    // const getItems = (panelStyle) => [
    //     {
    //       key: '1',
    //       label: (
    //         <div className='flex flex-col items-center w-full space-y-2'>
    //             <div className='flex flex-row justify-between items-center w-full'>
    //                 <div className='space-x-4'>
    //                     <ForkOutlined style={{border: '1px solid transparent', borderRadius: '50%', padding: '3px', backgroundColor: '#dfe6e9'}}/>
    //                     <span>{schedule.name}</span>
    //                 </div>
    //             </div>
    //             <div className='flex flex-row justify-between items-center w-full'>
    //                 <div className='space-x-4 text-sm font-normal'>
    //                     <FileSearchOutlined style={{border: '1px solid transparent', borderRadius: '50%', padding: '3px', backgroundColor: '#dfe6e9'}}/>
    //                     <span>{schedule.room}</span>
    //                 </div>
    //             </div>
    //         </div>
    //       ),
    //       children: (
    //         <div className='w-full'>
    //             {
    //                 schedule.timeSlots.map((time, index) => (
    //                     <Button key={index} 
    //                     onClick={() => handleSlotClick(time)}
    //                     style={{ 
    //                       padding: '10px 20px', 
    //                       marginRight: '4px', 
    //                       borderRadius: '8px', 
    //                       border: '1px solid #273c75', 
    //                       backgroundColor: 'white', 
    //                       cursor: 'pointer' 
    //                     }}>
    //                         {time}
    //                     </Button>
    //                 ))
    //             }
    //         </div>
    //       ),
    //       style: panelStyle,
    //     }
    //   ];
    
    const result = selectedValue ? selectedValue.format('DD-MM-YYYY') : null ; // Ensure selectedValue is dayjs object
    console.log('step: ', step)
    return (
        <div className='w-full h-fit min-h-[460px] border border-red-600 p-8'>
            <div className='flex flex-row gap-4 w-full h-full items-center' onClick={() => handleSetStatus('records')}>
                <Button icon={<ArrowLeftOutlined />} style={{backgroundColor:'transparent', border: 'none', boxShadow: 'none'}}></Button>
                <h1 className='text-black font-bold text-lg'>Chọn thông tin khám</h1>
            </div>
            <div className='w-full h-full flex flex-row space-x-4 mt-4 justify-center items-center'>
                <div className='flex flex-col w-1/4 h-fit'>
                    <Timeline_Booking choosedSpecialty={choosedSpecialty} specialty={specialty} step={step} result={result} selectedValue={selectedValue} selectedTime={selectedTime} selectedDoctor={selectedDoctor}/>
                    {
                        selectedTime && (
                            <div className='w-full h-full p-4'>
                                <Divider variant="dashed" style={{ borderColor: '#7cb305', width:'fit-content', height: 'fit-content' }} dashed></Divider>
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
                                <Divider variant="dashed" style={{ borderColor: '#7cb305', width:'fit-content', height: 'fit-content' }} dashed></Divider>
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
                    <Specialty_Booking setSpecialty = {setSpecialty} setPrice ={setPrice} setChoosedSpecialty = {setChoosedSpecialty} setStep = {setStep}/>
                )}
                {choosedSpecialty && step === 2 && ( 
                    <Date_Booking selectedValue={selectedValue} onSelectDate={onSelectDate}/>
                
                )}
                {step === 3 && (
                    <TimeADoctor_Booking handleSlotClick={handleSlotClick}/>
                )}
            </div>
            {
                selectedTime && (
                    <div className='flex flex-col justify-center items-center w-full h-fit p-8 '>
                <div className='flex flex-row justify-between items-center w-[36vw] h-fit'>
                    <p className='text-black text-base font-bold'>Thanh toán tạm tính: </p>
                    <p className='text-[#273c75] font-bold text-xl'>150.000đ</p>
                </div>
                <div className='flex flex-row justify-center items-center w-full h-fit space-x-4'>
                    <Button type="primary" className='w-full h-fit mt-4' icon={<ForkOutlined/>} style={{width:'300px', height:'40px', fontSize:'15px', fontWeight:'bold', backgroundColor:'white', color:'blue', border:'1px solid blue'}} >Thêm chuyên khoa</Button>
                    <Button type="primary" className='w-full h-fit mt-4' style={{width:'200px', height:'40px', fontSize:'15px', fontWeight:'bold', backgroundColor:'blue'}} onClick={continueBooking}>Tiếp tục</Button>
                </div>
            </div>
                )
            }
            {contextHolder}
        </div>
    );
};

export default CureInfo_Booking;

import React, { useState } from 'react';
import { Button} from 'antd';
import {ArrowLeftOutlined} from '@ant-design/icons';
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


    
    // const onPanelChange = (newValue) => {
    //     const result = newValue.format('DD-MM-YYYY')
    //     alert('value onchange: ' + newValue.format('YYYY-MM-DD'))
    //     setValueDate(result);
    //   };
      
    
    const onSelectDate = (newValue) => {
        setSelectedValue(newValue)
        setStep(3)
    }
    
    const handleSlotClick = (time, doctorName) => {
        setSelectedTime(time);
        setSelectedDoctor(doctorName);
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
            <div className='w-full h-full flex flex-row space-x-4 mt-4'>
                <Timeline_Booking choosedSpecialty={choosedSpecialty} specialty={specialty} step={step} result={result} selectedValue={selectedValue} selectedTime={selectedTime} selectedDoctor={selectedDoctor}/>
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
        </div>
    );
};

export default CureInfo_Booking;

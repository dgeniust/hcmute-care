import React, {useState} from "react";
import { Button, Input, Select } from 'antd';
import {ArrowLeftOutlined,HomeOutlined   } from '@ant-design/icons';
// import '../css/BookingContent.css';
import ForgetRecord_Booking from './ForgetRecord_Booking';
import FindRecord_Booking from './FindRecord_Booking';
const HaveRecord_Booking = ({setStatus}) => {
    const [typeForm, setTypeForm] = useState('inputRecords');
    const selectType = (value) =>{
        setTypeForm(value);
    }
    const handleSetStatus = (value) => {
        setStatus(value);
    }
    return (
        <div className='w-full h-full space-y-4'>
            <div className='flex flex-row gap-4 w-full h-full items-center'onClick={() => handleSetStatus('records')} >
                <Button icon={<ArrowLeftOutlined />} style={{backgroundColor:'transparent', border: 'none', boxShadow: 'none'}}></Button>
                <h1 className='text-black font-bold text-lg'>Hồ sơ khám bệnh</h1>
            </div>
            <div className='flex flex-row space-x-8 w-full h-fit items-center justify-center'>
                <Button style={{backgroundColor: typeForm === 'inputRecords' ? '#A1E3F9' : 'transparent', color: '#273c75', fontWeight: 'bold', width: '200px', height: '50px'}}
                onClick={() => selectType('inputRecords')}>Nhập số hồ sơ</Button>
                <Button style={{backgroundColor: typeForm === 'forgetRecords' ? '#A1E3F9' : 'transparent', color: 'black', fontWeight: 'bold', width: '200px', height: '50px'}}
                onClick={() => selectType('forgetRecords')}>Quên hồ sơ</Button>
            </div>
            {
                typeForm === 'inputRecords' ? (
                    <FindRecord_Booking/>
                ) : (
                    <ForgetRecord_Booking/>
                )
            }
            
            <div className='h-full w-fit'>
                <Button
                    style={{ backgroundColor: 'transparent' }}
                    icon={<HomeOutlined />}
                    onClick={() => handleSetStatus('records')} // Go back to records view
                >
                    Quay lại
                </Button>
            </div>                            
        </div>
    );
}
export default HaveRecord_Booking;
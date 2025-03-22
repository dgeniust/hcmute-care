import React from 'react';
import { Button,theme, Collapse  } from 'antd';
import {ForkOutlined, CaretRightOutlined, FileSearchOutlined } from '@ant-design/icons';

const TimeADoctor_Booking = ({handleSlotClick}) => {
    const schedule = [
        {
            name: 'ThS BS. Võ Quang Đỉnh',
            room: 'Phòng 32 - Lầu 1 khu A',
            timeSlots: [
                '06:00 - 07:00',
                '07:00 - 08:00',
                '08:00 - 09:00',
                '09:00 - 10:00',
                '10:00 - 11:00'
            ]
        },
        {
            name: 'TS BS. Nguyễn Văn A',
            room: 'Phòng 33 - Lầu 1 khu A',
            timeSlots: [
                '08:00 - 09:00',
                '09:00 - 10:00',
                '10:00 - 11:00',
                '11:00 - 12:00'
            ]
        },
        {
            name: 'TS BS. Nguyễn Văn A',
            room: 'Phòng 33 - Lầu 1 khu A',
            timeSlots: [
                '08:00 - 09:00',
                '09:00 - 10:00',
                '10:00 - 11:00',
                '11:00 - 12:00'
            ]
        },
        {
            name: 'TS BS. Nguyễn Văn A',
            room: 'Phòng 33 - Lầu 1 khu A',
            timeSlots: [
                '08:00 - 09:00',
                '09:00 - 10:00',
                '10:00 - 11:00',
                '11:00 - 12:00'
            ]
        },
        {
            name: 'TS BS. Nguyễn Văn A',
            room: 'Phòng 33 - Lầu 1 khu A',
            timeSlots: [
                '08:00 - 09:00',
                '09:00 - 10:00',
                '10:00 - 11:00',
                '11:00 - 12:00'
            ]
        },
        {
            name: 'TS BS. Nguyễn Văn A',
            room: 'Phòng 33 - Lầu 1 khu A',
            timeSlots: [
                '08:00 - 09:00',
                '09:00 - 10:00',
                '10:00 - 11:00',
                '11:00 - 12:00'
            ]
        },
        {
            name: 'TS BS. Nguyễn Văn A',
            room: 'Phòng 33 - Lầu 1 khu A',
            timeSlots: [
                '08:00 - 09:00',
                '09:00 - 10:00',
                '10:00 - 11:00',
                '11:00 - 12:00'
            ]
        },
        // Thêm các bác sĩ khác nếu cần
    ];
    const { token } = theme.useToken();
    
    const panelStyle = {
        marginBottom: 5,
        background: token.colorFillAlter,
        borderRadius: token.borderRadiusLG,
        border: 'none',
    };
    const getItems = (panelStyle) => {
        return schedule.map(doctor => ({
            key: schedule.name, // Sử dụng tên bác sĩ làm key duy nhất
            label: (
                <div className='flex flex-col items-center w-full space-y-2'>
                    <div className='flex flex-row justify-between items-center w-full'>
                        <div className='space-x-4'>
                            <ForkOutlined style={{ border: '1px solid transparent', borderRadius: '50%', padding: '3px', backgroundColor: '#dfe6e9' }} />
                            <span>{doctor.name}</span>
                        </div>
                    </div>
                    <div className='flex flex-row justify-between items-center w-full'>
                        <div className='space-x-4 text-sm font-normal'>
                            <FileSearchOutlined style={{ border: '1px solid transparent', borderRadius: '50%', padding: '3px', backgroundColor: '#dfe6e9' }} />
                            <span>{doctor.room}</span>
                        </div>
                    </div>
                </div>
            ),
            children: (
                <div className='w-full'>
                    {
                        doctor.timeSlots.map((time, index) => (
                            <Button
                                key={index}
                                onClick={() => handleSlotClick(time, doctor.name, doctor.room)} // Truyền tên bác sĩ
                                style={{
                                    padding: '10px 20px',
                                    marginRight: '4px',
                                    borderRadius: '8px',
                                    border: '1px solid #273c75',
                                    backgroundColor: 'white',
                                    cursor: 'pointer'
                                }}
                            >
                                {time}
                            </Button>
                        ))
                    }
                </div>
            ),
            style: panelStyle,
        }));
    };

    return ( 
        <div className='w-3/4 h-fit flex flex-col'>
            <div className='flex w-full justify-center'>
                <h1 className='text-black font-bold text-base'>Chọn giờ khám</h1>
            </div>
                <div className='w-full max-h-[460px] h-fit flex flex-col border border-red-600 rounded-xl overflow-y-auto space-y-2'>
                    <Collapse
                    bordered={false}
                    defaultActiveKey={['0']}
                    expandIcon={({ isActive }) => <CaretRightOutlined rotate={isActive ? 90 : 0} 
                    iconPosition='end'/>}
                    expandIconPosition='end'
                    style={{
                        background: token.colorBgContainer,
                        overflow: 'auto'
                    }}
                    items={getItems(panelStyle)}
                    />
                </div>
        </div>
    )
}

export default TimeADoctor_Booking;
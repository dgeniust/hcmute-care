import React, { useState, useRef } from 'react';
import { Button, Timeline , Calendar,theme, Collapse  } from 'antd';
import {ArrowLeftOutlined,ForkOutlined , RightOutlined, ClockCircleOutlined, CalendarOutlined, MedicineBoxOutlined, SyncOutlined, InfoCircleTwoTone, CaretRightOutlined, FileSearchOutlined } from '@ant-design/icons';

const Timeline_Booking = ({choosedSpecialty, specialty, step, result, selectedTime, selectedValue, selectedDoctor, ref}) => {
    return (
        <div className='flex justify-center items-center w-full h-full' ref={ref}>
            <Timeline style={{width: '100%', height: '100%'}}
                items={[
                {
                    dot: (
                        step === 1 ? (<SyncOutlined 
                            style={{
                                fontSize: '16px',
                            }}
                            spin/>) : null
                    ),
                    children: <Button style={{width:'100%', justifyContent: 'start' }} icon={<ForkOutlined/>} iconPosition='start'>
                        {
                            !choosedSpecialty ? (<p className='text-sm space-x-14'>
                                    <span>Chuyên khoa</span>
                                    <RightOutlined />
                                </p>) : (<p className='text-sm space-x-14'>

                                    <span>{specialty}</span>
                                </p>
                                )
                            
                        }
                        </Button>,
                },
                {
                    dot: (
                        step === 2 && result == null ? (<SyncOutlined 
                            style={{
                                fontSize: '16px',
                                color: 'blue'
                            }}
                            spin/>) : null
                    ),
                    color: selectedValue == null ? 'gray' : undefined,
                    children: step !== 2  && step !== 3? (
                        <Button style={{ width: '100%', justifyContent: 'start' }} icon={<CalendarOutlined />} iconPosition='start' disabled>
                            <p className='text-sm space-x-14'>
                                <span>Ngày khám</span>
                            </p>
                        </Button>
                    ) : result ?  (
                        <Button style={{ width: '100%', justifyContent: 'start' }} icon={<CalendarOutlined />} iconPosition='start'>
                            <p className='text-sm space-x-14'>
                                <span>{result}</span>
                            </p>
                        </Button>
                    ) : (<Button style={{ width: '100%', justifyContent: 'start' }} icon={<CalendarOutlined />} iconPosition='start'>
                        <p className='text-sm space-x-14'>
                            <span>Ngày khám</span>
                        </p>
                    </Button>)
                },
                {
                    dot: (
                        !selectedTime && step == 3 ? (<SyncOutlined 
                            style={{
                                fontSize: '16px',
                            }}
                            spin/>) : null
                    ),
                    color: result == null ? 'gray' : undefined,
                    children: step !== 3 ? (
                        <Button style={{width:'100%', justifyContent: 'start'}} icon={<ClockCircleOutlined />} iconPosition='start' disabled>
                        <p className='text-sm space-x-14'>
                            <span>Giờ khám</span>
                        </p>
                        </Button>
                    ) : selectedTime ? (
                        <Button style={{width:'100%', justifyContent: 'start'}} icon={<ClockCircleOutlined />} iconPosition='start'>
                            <p className='text-sm space-x-14'>
                                <span>{selectedTime}</span>
                            </p>
                        </Button>
                    ) : (
                        <Button style={{width:'100%', justifyContent: 'start'}} icon={<ClockCircleOutlined />} iconPosition='start'>
                        <p className='text-sm space-x-14'>
                            <span>Giờ khám</span>
                        </p>
                        </Button>
                    )
                },
                {
                    dot: (
                        !selectedDoctor && step == 3 ? (<SyncOutlined 
                            style={{
                                fontSize: '16px',
                            }}
                            spin/>) : null
                    ),
                    color: result == null ? 'gray' : undefined,
                    children: step !== 3 ? (
                        <Button style={{width:'100%', justifyContent: 'start'}} icon={<MedicineBoxOutlined />} iconPosition='start' disabled>
                        <p className='text-sm space-x-14'>
                            <span>Bác sĩ</span>
                        </p>
                        </Button>
                    ) : selectedDoctor ? (
                        <Button style={{width:'100%', justifyContent: 'start'}} icon={<MedicineBoxOutlined />} iconPosition='start'>
                            <p className='text-sm space-x-14'>
                                <span>{selectedDoctor}</span>
                            </p>
                        </Button>
                    ) : (
                        <Button style={{width:'100%', justifyContent: 'start'}} icon={<MedicineBoxOutlined />} iconPosition='start'>
                        <p className='text-sm space-x-14'>
                            <span>Bác sĩ</span>
                        </p>
                        </Button>
                    )
                },
                ]}
            />
        </div>
    )
}

export default Timeline_Booking;
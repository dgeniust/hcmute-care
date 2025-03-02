import React, { useState } from 'react';
import { Button, Modal, Tag } from 'antd';


import HaveRecord_Booking from './HaveRecord_Booking';
import ChooseProfile_Booking from './ChooseProfile_Booking';
import NoRecord_Booking from './NoRecord_Booking';
const CreateProfile_Booking = () => {
    const [hasAcc, setHasAcc] = useState(true); // To check if the user has a profile
    const [status, setStatus] = useState('records'); // Different user states ('records', 'new', 'hasUser')


    // Conditional content rendering based on `hasAcc` and `status`
    return (
        <div className='w-[970px] h-full min-h-[460px]'>
            {/* Content when there is no account (new user case) */}
            {!hasAcc ? (
                <div className='bg-cp-booking w-full h-[460px] items-center justify-center flex flex-col'>
                    <Button
                        size="large"
                        style={{
                            color: '#273c75',
                            backgroundColor: 'white',
                            border: '1px solid #273c75',
                            fontWeight: 'bold',
                        }}
                    >
                        <span>Tạo hồ sơ</span>
                    </Button>
                </div>
            ) : (
                // Content when there is an account (existing user case)
                <div className='w-full min-h-[460px] flex flex-col p-8 gap-8'>
                    {status === 'records' ? (
                        <ChooseProfile_Booking setStatus={setStatus}/>
                    ) : status === 'hasUser' ? (
                        <HaveRecord_Booking setStatus={setStatus}/>
                    ) : (
                        // Handle other status cases if needed
                        <NoRecord_Booking setStatus={setStatus}/>
                    )}
                </div>
            )}
            {/* Modal for profile creation */}
            
        </div>
    );
};

export default CreateProfile_Booking;

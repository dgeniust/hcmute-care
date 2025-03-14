import React, { useState,forwardRef } from 'react';
import { Button } from 'antd';

import HaveRecord_Booking from './HaveRecord_Booking';
import ChooseProfile_Booking from './ChooseProfile_Booking';
import NoRecord_Booking from './NoRecord_Booking';
const CreateProfile_Booking = forwardRef(({ refs }, ref) => {
    const [hasAcc, setHasAcc] = useState(true);
    const [status, setStatus] = useState('records');
  
    return (
      <div className='w-full h-full min-h-[460px]'>
        {!hasAcc ? (
          <div className='bg-cp-booking w-full h-[460px] items-center justify-center flex flex-col'>
            <Button size="large" style={{ color: '#273c75', backgroundColor: 'white', border: '1px solid #273c75', fontWeight: 'bold' }}>
              <span>Tạo hồ sơ</span>
            </Button>
          </div>
        ) : (
          <div className='w-full min-h-[460px] flex flex-col p-8 gap-8'>
            {status === 'records' ? (
              <ChooseProfile_Booking refs={refs} setStatus={setStatus} /> // Truyền ref xuống ChooseProfile_Booking
            ) : status === 'hasUser' ? (
              <HaveRecord_Booking setStatus={setStatus} />
            ) : (
              <NoRecord_Booking setStatus={setStatus} />
            )}
          </div>
        )}
      </div>
    );
  });
export default CreateProfile_Booking;

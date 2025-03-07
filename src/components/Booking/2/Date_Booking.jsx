import React from 'react';
import {Calendar,theme } from 'antd';


const Date_Booking = ({selectedValue, onSelectDate}) => {

    const { token } = theme.useToken();
    const wrapperStyle = {
        border: `1px solid ${token.colorBorderSecondary}`,
        borderRadius: token.borderRadiusLG,
    };

    return (
        <div className='w-3/4 h-fit flex flex-col'>
            <div className='flex w-full justify-center'>
                <h1 className='text-black font-bold text-base'>Chọn ngày khám</h1>
            </div>
                <div className='w-full min-h-[460px] h-fit flex flex-col border border-black rounded-xl space-y-2'>
                    <div style={wrapperStyle}>
                        <Calendar value={selectedValue} onSelect={onSelectDate}/>
                    </div>
            </div>
        </div>
    )
}

export default Date_Booking;
import React from 'react';
import {Tabs} from 'antd';
import BMIContent from './BMIContent';
import InfoCalculate from './InfoCalculate';
const CalculateContent = () => {
    const onChange = (key) => {
        console.log(key);
    };
    const items = [
        {
          key: '1',
          label: 'Tính BMI, BMR, TDEE',
          children: <BMIContent/>,
        },
        {
          key: '2',
          label: 'Thông tin',
          children: <InfoCalculate/>,
        },
    ];
    
    return (
        <div className='w-full h-full p-4 bg-slate-50'>
            <Tabs defaultActiveKey="1" items={items} onChange={onChange} type="card"/>;
        </div>
    );
}

export default CalculateContent;
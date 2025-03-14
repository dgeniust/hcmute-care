import React from 'react';
import { Tabs } from 'antd';
const RegulationUse = () => {
    const onChange = (key) => {
        console.log(key);
    };
    const items = [
        {
          key: '1',
          label: 'Tab 1',
          children: 'Content of Tab Pane 1',
        },
        {
          key: '2',
          label: 'Tab 2',
          children: 'Content of Tab Pane 2',
        },
        {
          key: '3',
          label: 'Tab 3',
          children: 'Content of Tab Pane 3',
        },
      ];
    return (
        <div className='w-full h-full p-8 border border-black'>
            <h1 className='text-black font-bold text-xl text-center w-full mb-8'>Quy định sử dụng</h1>
            <Tabs defaultActiveKey="1" items={items} onChange={onChange} type="card"/>;
        </div>
    );
}
export default RegulationUse;
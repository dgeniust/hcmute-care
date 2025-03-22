import React from 'react';
import { Tabs } from 'antd';
import regulation from './Data_Personal/regulationData';
const RegulationUse = () => {
    const onChange = (key) => {
        console.log(key);
    };
    const items = regulation.map((regulation, index) => ({
      key: `Trang ${index + 1}`,
      label: `Trang ${index + 1}`, // Sử dụng key làm label
      children: <p>{regulation.children}</p>
  }));
    return (
        <div className='w-full min-h-[460px] p-8 shadow-xl'>
            <h1 className='text-black font-bold text-xl text-center w-full mb-8'>Quy định sử dụng</h1>
            <Tabs defaultActiveKey="1" items={items} onChange={onChange} type="card"/>;
        </div>
    );
}
export default RegulationUse;
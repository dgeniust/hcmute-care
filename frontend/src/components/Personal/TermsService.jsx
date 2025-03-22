import React from 'react';
import { Tabs } from 'antd';
import termsOfService from './Data_Personal/termsOfService';
const TermsService = () => {
    const onChange = (key) => {
        console.log(key);
    };
    const items = termsOfService.map((terms, index) => ({
      key: `Trang ${index + 1}`,
      label: `Trang ${index + 1}`, // Sử dụng key làm label
      children: <p>{terms.children}</p>
  }));
    return (
        <div className='w-full min-h-[460px] p-8 shadow-xl'>
            <h1 className='text-black font-bold text-xl text-center w-full mb-8'>Điều khoản dịch vụ</h1>
            <Tabs defaultActiveKey="1" items={items} onChange={onChange} type="card"/>;
        </div>
    );
}
export default TermsService;
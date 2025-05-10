import React from 'react';
import { Tabs } from 'antd';
import { DatePicker } from 'antd';
import dayjs from 'dayjs';
import MH_1 from '../../components/Personal/MedicalHistory-Children/MH_1';
import MH_2 from '../../components/Personal/MedicalHistory-Children/MH_2';
import MH_3 from '../../components/Personal/MedicalHistory-Children/MH_3';
import MH_4 from '../../components/Personal/MedicalHistory-Children/MH_4';
const { RangePicker } = DatePicker;
const dateFormat = 'YYYY-MM-DD';
const MedicalHistory = () => {
    const onChange = (key) => {
        console.log(key);
    };
    const items = [
        {
          key: '1',
          label: 'Đã giao dịch',
          children: (
            <MH_1/>
          ),
        },
        {
          key: '2',
          label: 'Đã tiếp nhận',
          children: (
            <MH_2/>
          ),
        },
        {
          key: '3',
          label: 'Đã khám',
          children: (
            <MH_3/>
          ),
        },
        {
            key: '4',
            label: 'Đã hủy',
            children: (
                <MH_4/>
              ),
        },
    ];
    return (
        <div className='w-full h-full p-8 border border-black'>
            <h1 className='text-black font-bold text-xl text-center w-full mb-8'>Phiếu khám bệnh</h1>
            <div className="w-full h-fit flex flex-row text-black font-bold">
                <div className="w-1/2">Từ</div>
                <div className="w-1/2">Đến</div>
            </div>
            <RangePicker
            defaultValue={[dayjs('2019-09-03', dateFormat), dayjs('2019-11-22', dateFormat)]}
            disabled={[false, true]} style={{width: '100%', marginBottom: '20px'}}
            />
            <Tabs defaultActiveKey="1" items={items} onChange={onChange} type="card"/>;
        </div>
    );
}
export default MedicalHistory;
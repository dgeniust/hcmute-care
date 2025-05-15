import React, { useState, useEffect } from 'react';
import { Divider } from 'antd';

const PlanCard = ({ name, rate, calo, percent }) => (
  <div className='flex flex-row w-full h-full p-2 border border-sky-600 rounded-xl justify-around items-center'>
    <div className='flex flex-col text-center items-center justify-center w-[45%] h-full'>
      <h1 className='text-lg font-semibold'>{name}</h1>
      <span>({rate})</span>
    </div>
    <div className='h-full w-[10%] flex items-center justify-center'>
      <Divider type="vertical" variant="solid" style={{ borderColor: 'oklch(0.443 0.11 240.79)', height: '40px' }} />
    </div>
    <div className='flex flex-col text-center items-center justify-center w-[45%] h-full'>
      <h1 className='text-xl text-sky-800 font-bold'>{calo}</h1>
      <p className='text-lg font-semibold'>Calories/ngày</p>
      <span className='text-xs'>({percent})</span>
    </div>
  </div>
);

const PlanListCalo = ({ tdee }) => {
  const [g1, setG1] = useState(0);
  const [g2, setG2] = useState(0);
  const [g3, setG3] = useState(0);
  const [t1, setT1] = useState(0);
  const [t2, setT2] = useState(0);
  const [t3, setT3] = useState(0);

  useEffect(() => {
    if (tdee) {
      setG1(Math.round(tdee - 250));
      setG2(Math.round(tdee - 500));
      setG3(Math.round(tdee - 1000));
      setT1(Math.round(tdee + 250));
      setT2(Math.round(tdee + 500));
      setT3(Math.round(tdee + 1000));
    }
  }, [tdee]);

  const plans = [
    { category: 'GIẢM CÂN', options: [
      { name: 'Giảm cân nhẹ', rate: '0.25kg/tuần', calo: g1, percent:'85%' },
      { name: 'Giảm cân', rate: '0.5kg/tuần', calo: g2, percent:'71%' },
      { name: 'Giảm cân nhanh', rate: '1kg/tuần', calo: g3, percent:'42%' }
    ]},
    { category: 'TĂNG CÂN', options: [
      { name: 'Tăng cân nhẹ', rate: '0.25kg/tuần', calo: t1, percent:'115%' },
      { name: 'Tăng cân', rate: '0.5kg/tuần', calo: t2, percent:'129%' },
      { name: 'Tăng cân nhanh', rate: '1kg/tuần', calo: t3, percent:'158%' }
    ]}
  ];

  return (
    <div className='flex w-full'>
      {plans.map(({ category, options }) => (
        <div key={category} className='w-1/2 h-fit p-4 flex flex-col space-y-2'>
          <div className='w-full h-full bg-sky-50'>
            <h1 className='font-bold text-base text-center p-2 text-sky-500'>{category}</h1>
          </div>
          {options.map(plan => <PlanCard key={plan.name} {...plan} />)}
        </div>
      ))}
    </div>
  );
};

export default PlanListCalo;

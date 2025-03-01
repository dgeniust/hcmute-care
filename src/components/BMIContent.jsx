import React, {useState} from 'react';
import {Radio, DatePicker, InputNumber, Button, Divider } from 'antd';
import dayjs from 'dayjs';
import BMIWeight from './bodies';
const dateFormatList = ['DD/MM/YYYY', 'DD/MM/YY', 'DD-MM-YYYY', 'DD-MM-YY'];
const style = {
    display: 'flex',
    flexDirection: 'column',
    gap: 8,
  };
const BMIContent = () => {
    const [value, setValue] = useState(1);
    
    const [bmi, setBMI] = useState(23.7);
    const onChange = (e) => {
        setValue(e.target.value);
        console.log(e.target.value);
    };
    return (
        <div className='flex flex-col items-center w-full h-fit gap-4'>
            <h1 className='text-blue-600 text-xl mt-4'>CÔNG CỤ TÍNH BMI</h1>
            <div className='divider w-[100px] h-[5px] bg-gray-400 mb-4 mt-2'></div>
            <div className='w-fit h-fit border border-blue-600 flex flex-col items-center rounded-3xl'>
              <form action="" method="post" className='flex flex-col items-center p-8 text-blue-600 font-bold'>
                  <div className='grid grid-cols-2 grid-flow-row gap-8 p-8 space-y-2 '>
                      <div className='flex flex-col'>
                          <label htmlFor="">Chiều cao</label>
                          <InputNumber addonAfter="cm" defaultValue={100} />
                      </div>
                      <div className='flex flex-col'>
                          <label htmlFor="">Giới tính</label>
                          <Radio.Group
                              style={style}
                              onChange={onChange}
                              value={value}
                              options={[
                                  {
                                  value: 'Male',
                                  label: 'Nam',
                                  },
                                  {
                                  value: 'Female',
                                  label: 'Nữ',
                                  },
                              ]}
                              />
                      </div>
                      <div className='flex flex-col'>
                          <label htmlFor="">Cân nặng</label>
                          <InputNumber addonAfter="kg" defaultValue={50} />
                      </div>
                      
                      <div className='flex flex-col'>
                          <label htmlFor="">Ngày sinh</label>
                          <DatePicker defaultValue={dayjs('01/01/2015', dateFormatList[0])} format={dateFormatList} />
                      </div>
                  </div>
                  <div className='w-[50%] flex items-center justify-center'>
                      <Button htmlType='submit' style={{width: '100%', height: '40px', color: 'white', backgroundColor: '#273c75', fontWeight: 'bold', fontSize: '15px'}}>
                      Xem kết quả
                      </Button>
                  </div>
              </form>
            </div>
            <div className='w-full h-fit flex flex-col items-center'>
              <div className="flex items-center justify-center">
                <p className="text-3xl font-bold text-blue-600">
                  Your BMI index is{" "}
                  <span className="text-blue-500 font-bold">
                  {bmi} kg/m²
                  </span>
                  <br />
                  <p className="text-center text-base ">
                    Healthy BMI range: 18.5 kg/m² - 25 kg/m².
                  </p>
                </p>
              </div>

              <BMIWeight BMI={bmi}/>
              <div className='w-full h-5'>
                <div className="flex flex-row w-full h-5 rounded-lg">
                  <div className="w-3/6 bg-blue-500 rounded-l-lg"></div>
                  <div className="w-[140px] bg-green-500"></div>
                  <div className="w-[90px] bg-yellow-500"></div>
                  <div className="w-[190px] bg-orange-500"></div>
                  <div className="w-[210px] bg-red-500 rounded-r-lg"></div>
                </div>
                <div className="flex flex-row w-full h-full text-lg font-bold text-slate-500">
                  <div className="w-3/6 ">0</div>
                  <div className="w-[140px]">18.5</div>
                  <div className="w-[90px]">23</div>
                  <div className="w-[190px]">25</div>
                  <div className="w-[210px]">30+</div>
                </div>
              </div>
            </div>
        </div>
    );
}

export default BMIContent;
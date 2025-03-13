import React from 'react';
import { Divider, Input, Select, DatePicker, Button } from 'antd';
import dayjs from 'dayjs';

const dateFormat = 'YYYY/MM/DD';
const Personal_Info = () => {

    const handleChange = (value) => {
        console.log(`selected ${value}`);
      };
    return (
        <div className='w-full h-full shadow-lg rounded-lg'>
            <div className='w-full border border-black h-[150px] personal-bg relative'>
                <Divider orientation="left" plain style={{ position: 'absolute', bottom: 0, width: '100%' }}>
                    <div className='w-[150px] h-[150px] rounded-full bg-white absolute border border-black top-1/2 left-1/6 transform -translate-x-1/2 -translate-y-1/3'>
                        <img src="https://api.dicebear.com/7.x/miniavs/svg?seed=8" alt="" srcset="" 
                        className="object-center w-full h-full rounded-full" />
                        <div className='text-black font-bold text-xl mt-4 text-center w-full'>
                            <h1>Nguyễn Thành Đạt</h1>
                        </div>
                    </div>
                </Divider>
            </div>
            <div className='w-full h-fit mt-[140px] px-8 py-4'>
                <form className='w-full h-full'>
                    <div className='grid grid-flow-row grid-cols-2 gap-4'>
                        <div className='text-black space-y-2 flex flex-col'>
                            <span className='font-bold'>Số điện thoại</span>
                            <Input defaultValue="0387731823" disabled style={{color: 'black'}}/>
                        </div>
                        <div className='text-black space-y-2 flex flex-col'>
                            <span className='font-bold'>Họ và tên lót</span>
                            <Input defaultValue="Nguyễn Thành" />
                        </div>
                        <div className='text-black space-y-2 flex flex-col'>
                            <span className='font-bold'>Tên</span>
                            <Input defaultValue="Đạt" />
                        </div>
                        <div className='flex flex-row w-full space-x-2'>
                            <div className='text-black space-y-2 flex flex-col w-full'>
                                <span className='font-bold'>Ngày sinh</span>
                                <DatePicker defaultValue={dayjs('2025/03/13', dateFormat)} format={dateFormat} />
                            </div>
                            <div className='text-black space-y-2 flex flex-col w-full'>
                                <span className='font-bold'>Giới tính</span>
                                <Select
                                defaultValue="nam"
                                style={{
                                    width: '100%',
                                }}
                                onChange={handleChange}
                                options={[
                                    {
                                    value: 'nam',
                                    label: 'Nam',
                                    },
                                    {
                                    value: 'nu',
                                    label: 'Nữ',
                                    },
                                ]}
                                />
                            </div>
                        </div>
                        <div className='text-black space-y-2 flex flex-col'>
                            <span className='font-bold'>Email</span>
                            <Input defaultValue="dathiichan141@gmail.com" />
                        </div>
                    </div>
                    <div className='w-full flex justify-center mt-8'>
                        <Button type="primary" style={{padding: '20px'}}>Cập nhật thông tin</Button>
                    </div>
                </form>
            </div>
        </div> 
    )
}

export default Personal_Info;
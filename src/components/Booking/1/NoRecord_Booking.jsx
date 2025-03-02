import React, {useState} from "react";
import { Button, Input, Select,DatePicker } from 'antd';
import {ArrowLeftOutlined,HomeOutlined, SearchOutlined,InfoCircleTwoTone   } from '@ant-design/icons';
import dayjs from 'dayjs';
const NoRecord_Booking = ({setStatus}) => {
    const dateFormat = 'YYYY/MM/DD';
    const [typeForm, setTypeForm] = useState('inputRecords');
    const selectType = (value) =>{
        setTypeForm(value);
    }
    const handleSetStatus = (value) => {
        setStatus(value);
    }
    const handleChange = (value) => {
        console.log(`selected ${value}`);
      };
    return (
        <div className='w-full h-full space-y-4'>
            <div className='flex flex-row gap-4 w-full h-full items-center'onClick={() => handleSetStatus('records')} >
                <Button icon={<ArrowLeftOutlined />} style={{backgroundColor:'transparent', border: 'none', boxShadow: 'none'}}></Button>
                <h1 className='text-black font-bold text-lg'>Tạo hồ sơ khám bệnh</h1>
            </div>
            <div className="flex flex-col space-y-4">
                <div className="flex flex-row items-center gap-2">
                    <InfoCircleTwoTone/>
                    <p className="text-black">Vui lòng nhập thông tin bên dưới</p>
                </div>
                <div>
                    <form action="" className="flex flex-col">
                        <div className="grid grid-flow-row grid-cols-2 gap-4 text-black">
                            <div>
                                <label htmlFor="" className="flex flex-row items-center space-x-2">Họ tên lót <span className="ml-1"><img width="10" height="10" src="https://img.icons8.com/forma-bold-filled-sharp/24/FA5252/asterisk.png" alt="asterisk"/></span><span className="text-xs italic">(theo CCCD/CMND/Passpot)</span></label>
                                <Input placeholder="VD: Nguyễn Thành..." />
                            </div>
                            <div>
                                <label htmlFor="" className="flex flex-row items-center space-x-2">Tên<span className="ml-1"><img width="10" height="10" src="https://img.icons8.com/forma-bold-filled-sharp/24/FA5252/asterisk.png" alt="asterisk"/></span><span className="text-xs italic">(theo CCCD/CMND/Passpot)</span></label>
                                <Input placeholder="VD: Đạt..." />
                            </div>
                            <div>
                                <label htmlFor="" className="flex flex-row items-center space-x-2">Số điện thoại<span className="ml-1"><img width="10" height="10" src="https://img.icons8.com/forma-bold-filled-sharp/24/FA5252/asterisk.png" alt="asterisk"/></span></label>
                                <Input placeholder="SĐT: 09xxxxxx..." />
                            </div>
                            <div>
                                <label htmlFor="" className="flex flex-row items-center space-x-2">Email<span className="ml-1"><img width="10" height="10" src="https://img.icons8.com/forma-bold-filled-sharp/24/FA5252/asterisk.png" alt="asterisk"/></span></label>
                                <Input placeholder="Email..." />
                            </div>
                            <div>
                                <label htmlFor="">CCCD/CMND/Passpot</label>
                                <Input placeholder="Email..." />
                            </div>
                            <div>
                                <label htmlFor="" className="flex flex-row items-center space-x-2">Ngày sinh<span className="ml-1"><img width="10" height="10" src="https://img.icons8.com/forma-bold-filled-sharp/24/FA5252/asterisk.png" alt="asterisk"/></span></label>
                                <DatePicker defaultValue={dayjs('2015/01/01', dateFormat)} format={dateFormat} style={{width:'100%'}}/>
                            </div>
                            <div className="flex flex-row space-x-2 w-full">
                                <div className="w-full">
                                <label htmlFor="" className="flex flex-row items-center space-x-2">Giới tính<span className="ml-1"><img width="10" height="10" src="https://img.icons8.com/forma-bold-filled-sharp/24/FA5252/asterisk.png" alt="asterisk"/></span></label>
                                    <Select
                                    defaultValue="Chọn"
                                    style={{
                                        width: 200,
                                    }}
                                    onChange={handleChange}
                                    options={[
                                        {
                                        value: 'male',
                                        label: 'Nam',
                                        },
                                        {
                                        value: 'female',
                                        label: 'Nữ',
                                        },
                                    ]}
                                    />
                                </div>
                                <div>
                                <label htmlFor="" className="flex flex-row items-center space-x-2">Quốc gia<span className="ml-1"><img width="10" height="10" src="https://img.icons8.com/forma-bold-filled-sharp/24/FA5252/asterisk.png" alt="asterisk"/></span></label>
                                    <Select
                                    defaultValue="Chọn"
                                    style={{
                                        width: 200,
                                    }}
                                    onChange={handleChange}
                                    options={[
                                        {
                                            value: 'vietnam',
                                            label: 'Việt Nam',
                                            },
                                            {
                                            value: 'nga',
                                            label: 'Nga',
                                            },
                                            {
                                            value: 'hanquoc',
                                            label: 'Hàn Quốc',
                                            },
                                    ]}
                                    />
                                </div>
                            </div>
                            <div className="flex flex-row space-x-2">
                                <div className="w-full">
                                <label htmlFor="" className="flex flex-row items-center space-x-2">Dân tộc<span className="ml-1"><img width="10" height="10" src="https://img.icons8.com/forma-bold-filled-sharp/24/FA5252/asterisk.png" alt="asterisk"/></span></label>
                                    <Select
                                    defaultValue="Chọn"
                                    style={{
                                        width: 200,
                                    }}
                                    onChange={handleChange}
                                    options={[
                                        {
                                        value: 'male',
                                        label: 'Nam',
                                        },
                                        {
                                        value: 'female',
                                        label: 'Nữ',
                                        },
                                    ]}
                                    />
                                </div>
                                <div>
                                <label htmlFor="" className="flex flex-row items-center space-x-2">Nghề nghiệp<span className="ml-1"><img width="10" height="10" src="https://img.icons8.com/forma-bold-filled-sharp/24/FA5252/asterisk.png" alt="asterisk"/></span></label>
                                    <Select
                                    defaultValue="Chọn"
                                    style={{
                                        width: 200,
                                    }}
                                    onChange={handleChange}
                                    options={[
                                        {
                                            value: 'vietnam',
                                            label: 'Việt Nam',
                                            },
                                            {
                                            value: 'nga',
                                            label: 'Nga',
                                            },
                                            {
                                            value: 'hanquoc',
                                            label: 'Hàn Quốc',
                                            },
                                    ]}
                                    />
                                </div>
                            </div>
                            <div className="flex flex-col">
                                <label htmlFor="">Tỉnh thành</label>
                                <Select
                                    defaultValue="Chọn"
                                    style={{
                                        width: '100%',
                                    }}
                                    onChange={handleChange}
                                    options={[
                                        {
                                        value: 'vietnam',
                                        label: 'Việt Nam',
                                        },
                                        {
                                        value: 'nga',
                                        label: 'Nga',
                                        },
                                        {
                                        value: 'hanquoc',
                                        label: 'Hàn Quốc',
                                        },
                                    ]}
                                />
                            </div>
                            <div className="flex flex-row space-x-2">
                                <div className="w-full">
                                    <label htmlFor="" className="flex flex-row items-center space-x-2">Quận huyện<span className="ml-1"><img width="10" height="10" src="https://img.icons8.com/forma-bold-filled-sharp/24/FA5252/asterisk.png" alt="asterisk"/></span></label>
                                    <Select
                                    defaultValue="Chọn"
                                    style={{
                                        width: 200,
                                    }}
                                    onChange={handleChange}
                                    options={[
                                        {
                                        value: 'male',
                                        label: 'Nam',
                                        },
                                        {
                                        value: 'female',
                                        label: 'Nữ',
                                        },
                                    ]}
                                    />
                                </div>
                                <div>
                                <label htmlFor="" className="flex flex-row items-center space-x-2">Phường xã<span className="ml-1"><img width="10" height="10" src="https://img.icons8.com/forma-bold-filled-sharp/24/FA5252/asterisk.png" alt="asterisk"/></span></label>
                                    <Select
                                    defaultValue="Chọn"
                                    style={{
                                        width: 200,
                                    }}
                                    onChange={handleChange}
                                    options={[
                                        {
                                            value: 'vietnam',
                                            label: 'Việt Nam',
                                            },
                                            {
                                            value: 'nga',
                                            label: 'Nga',
                                            },
                                            {
                                            value: 'hanquoc',
                                            label: 'Hàn Quốc',
                                            },
                                    ]}
                                    />
                                </div>
                            </div>
                            <div>
                            <label htmlFor="" className="flex flex-row items-center space-x-2">Địa chỉ thường trú<span className="ml-1"><img width="10" height="10" src="https://img.icons8.com/forma-bold-filled-sharp/24/FA5252/asterisk.png" alt="asterisk"/></span></label>
                                <Input placeholder="Nhập chính xác địa chỉ..." />
                            </div>
                        </div>
                        <div className="w-full h-fit flex justify-center items-center mt-8"><Button style={{backgroundColor: '#273c75', color: 'white', fontWeight: 'bold', width: '30%', height: '40px'}}>Xác nhận</Button></div>
                    </form>
                </div>
            </div>
            
            <div className='h-full w-fit'>
                <Button
                    style={{ backgroundColor: 'transparent' }}
                    icon={<HomeOutlined />}
                    onClick={() => handleSetStatus('records')} // Go back to records view
                >
                    Quay lại
                </Button>
            </div>                            
        </div>
    );
}
export default NoRecord_Booking;
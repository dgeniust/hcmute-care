import React, {useState} from "react";
import { Button, Input, Select } from 'antd';
import {ArrowLeftOutlined, InfoCircleTwoTone, ArrowRightOutlined, BarcodeOutlined, PhoneOutlined, RightOutlined,SearchOutlined,HomeOutlined   } from '@ant-design/icons';
// import '../css/BookingContent.css';

const ForgetRecord_Booking = () => {
    const handleChange = (value) => {
        console.log(`selected ${value}`);
      };
    return (
        <div className="w-full h-fit min-h-[460px] p-8 flex flex-col gap-4">
            <div className="flex flex-col">
                <p className="text-black">Vui lòng nhập chính xác thông tin</p>
                <div>
                    <form action="" className="flex flex-col">
                        <div className="grid grid-flow-row grid-cols-2 gap-4 text-black">
                            <div>
                                <label htmlFor="">Họ tên lót</label>
                                <Input placeholder="VD: Nguyễn Thành..." />
                            </div>
                            <div>
                                <label htmlFor="">Tên</label>
                                <Input placeholder="VD: Đạt..." />
                            </div>
                            <div>
                                <label htmlFor="">Số điện thoại</label>
                                <Input placeholder="SĐT: 09xxxxxx..." />
                            </div>
                            <div className="flex flex-row space-x-2">
                                <div>
                                    <label htmlFor="">Số điện thoại</label>
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
                                    <label htmlFor="">Năm sinh</label>
                                    <Input placeholder="Năm sinh..." />
                                </div>
                            </div>
                            <div className="flex flex-col">
                                <label htmlFor="">Quốc gia</label>
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
                            <div className="flex flex-col">
                                <label htmlFor="">Quận huyện</label>
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
                            <div className="flex flex-col">
                                <label htmlFor="">Phường xã</label>
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
                        </div>
                        <div className="w-full h-fit flex justify-center items-center mt-8"><Button icon={<SearchOutlined />} style={{backgroundColor: '#273c75', color: 'white', fontWeight: 'bold'}}>Tìm kiếm</Button></div>
                    </form>
                </div>
            </div>
            
        </div>
    )
}
export default ForgetRecord_Booking;
import React, {useState} from "react";
import { Button, Input } from 'antd';
import {InfoCircleTwoTone, ArrowRightOutlined, BarcodeOutlined, PhoneOutlined, RightOutlined   } from '@ant-design/icons';


const FindRecord_Booking = () => {
    
    const [visible, setVisible] = useState(false);
    const showImgSuggest = () => {
        setVisible(prevState => !prevState);
    }
    return (
        <div className="w-full h-fit min-h-[460px] p-8 flex flex-col gap-4">
                        <div className="flex flex-col">
                            <p className="text-black">Vui lòng nhập chính xác số hồ sơ</p>
                            <div className="w-full h-fit flex flex-row gap-4 items-center mt-4">
                                <Input style={{fontWeight: 'bold', width: '100%', height: '50px'}} placeholder="N18-000XXXX" />
                                <Button style={{backgroundColor: '#273c75', color: 'white', fontWeight: 'bold', width: '200px', height: '50px'}}>Tìm kiếm</Button>
                            </div>
                            <div className="space-y-1 mt-1">
                                <Button
                                    style={{
                                        display: 'flex',
                                        flexDirection: 'column',
                                        height: '70px',
                                        width: '100%',
                                        padding: '16px',
                                        backgroundColor: 'transparent',
                                        color: '#273c75',
                                        border: '1px solid #273c75',
                                    }}
                                    type="primary"
                                    size="large"
                                >
                                    <div className='flex flex-row justify-between w-full'>
                                        <div className='space-x-1'>
                                            <InfoCircleTwoTone />
                                            <span className='font-bold text-sm tracking-normal'>NGUYỄN THÀNH ĐẠT</span>
                                        </div>
                                        <div><RightOutlined /></div>
                                    </div>
                                    <div className='flex flex-row justify-between w-full'>
                                        <div className='flex flex-row w-full text-sm'>
                                            <div className='mr-18 flex flex-row h-fit space-x-1'>
                                                <BarcodeOutlined />
                                                <span>W24-0068373</span>
                                            </div>
                                            <div className='flex flex-row h-fit space-x-1'>
                                                <PhoneOutlined />
                                                <span>0387***823</span>
                                            </div>
                                        </div>
                                    </div>
                                </Button>
                                
                            </div>
                        </div>
                        <div className="text-black flex flex-col gap-4 text-base">
                            <div className="flex flex-row gap-4 items-center">
                            <InfoCircleTwoTone />
                            <p>Số hồ sơ được in trên toa thuốc, phiếu chỉ định hoặc phiếu trả kết quả cận lâm sàng </p>
                            </div>
                            <p className="text-[#273c75] items-center text-center font-bold text-sm cursor-pointer hover:text-blue-500" onClick={showImgSuggest}>Xem gợi ý <ArrowRightOutlined /></p>
                        </div>
                        {
                            visible ? (<div className="img-suggest w-full h-[360px] border border-black">
        
                                </div>) : null
                        }
        </div>
    )
}
export default FindRecord_Booking;
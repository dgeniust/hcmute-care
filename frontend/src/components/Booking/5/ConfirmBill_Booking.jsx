import React, { useState, useEffect } from 'react';
import { Button, Collapse, Divider, Radio  } from 'antd';
import {HeartTwoTone, CreditCardTwoTone, RestOutlined, ForkOutlined } from '@ant-design/icons';

const ConfirmBill_Booking = () => {
    return (
        <div className='w-fit h-fit min-h-[460px] border border-sky-600 px-8 py-4 space-y-8 rounded-lg justify-center items-center flex flex-col m-auto'>
            <div className='flex flex-col text-black justify-center items-center space-y-4 text-center'>
                <div className='flex flex-col'>
                    <h1 className='font-bold text-base'>Bệnh viện đại học Y Dược TPHCM</h1>
                    <span>215 Hồng Bàng, P.11, Q5, TPHCM</span>
                </div>
                <div className='flex flex-col'>
                    <h1 className='text-xl text-[#273c75] font-bold'>PHIẾU KHÁM BỆNH</h1>
                    <span>(Mã phiếu: U2408131234)</span>
                    <span>(Mã giao dịch: MOMO_64579989441)</span>
                </div>
            </div>
            <div className='flex flex-col space-y-4 flex items-center justify-center text-center'>
                <div className='flex flex-col space-y-1 text-black items-center justify-center text-center'>
                    <h1 className='text-lg font-bold'>TỔNG QUÁT</h1>
                    <p>Phòng 33 - Khám Nội Lầu 1 Khu A</p>
                    <div className='w-[80px] h-[80px] border border-[#2ecc71] rounded-full flex flex-col justify-center items-center p-4 text-[#2ecc71]'>
                        <p className='text-4xl font-bold text-[#009432]'>42</p>
                        <span>STT</span>
                    </div>
                </div>
            <div className='flex flex-row items-center space-x-20 justify-center w-full text-center text-black'>
                    <div className='space-y-4'>
                        <div className='flex flex-row space-x-4'>
                            <h1>Họ tên:</h1>
                            <p className='font-bold'>NGUYỄN THÀNH ĐẠT</p>
                        </div>
                        <div className='flex flex-row space-x-4'>
                            <h1>Ngày sinh:</h1>
                            <p className='font-bold'>14/01/2004</p>
                        </div>
                        <div className='flex flex-row space-x-4'>
                            <h1>Giới tính:</h1>
                            <p className='font-bold'>Nam</p>
                        </div>
                        <div className='flex flex-row space-x-4'>
                            <h1>Tỉnh/TP:</h1>
                            <p className='font-bold'>Bà Rịa - Vũng Tàu</p>
                        </div>
                    </div>
                    <div className='space-y-4'>
                        <div className='flex flex-row space-x-4'>
                            <h1>Giờ khám dự kiến:</h1>
                            <p className='font-bold'>8:00 - 9:00</p>
                        </div>
                        <div className='flex flex-row space-x-4 items-center'>
                            <h1>Tiền khám:</h1>
                            <p className='font-bold text-[#009432] text-lg '>150.000đ</p>
                        </div>
                        <div className='flex flex-row space-x-4'>
                            <h1>Đối tượng:</h1>
                            <p className='font-bold '>Thu phí</p>
                        </div>
                    </div>
                </div>
            </div>
            <div className='text-black space-y-4 flex justify-center flex-col items-center text-center'>
                <p>Vui lòng đến trực tiếp phòng khám trước hẹn 15-30 phút để khám bệnh</p>
                <div>
                    <p>Số hồ sơ (Mã bệnh nhân): <span className='font-bold'>W24-0068373</span></p>
                    <span className='text-xs'>Ghi chú: Số thứ tự này chỉ có giá trị trong ngày khám</span>
                </div>
            </div>
        </div>
    )
}
export default ConfirmBill_Booking
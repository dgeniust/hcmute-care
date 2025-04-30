import React from 'react';
import { Button} from 'antd';
import {RightOutlined, InfoCircleTwoTone} from '@ant-design/icons';

const Specialty_Booking = ({setSpecialty, setPrice, setChoosedSpecialty, setStep, ref}) => {
    const handleCureInfo = (name, price) => {
        setSpecialty(name);
        setPrice(price);
        setChoosedSpecialty(true)
        setStep(2)
    }
    return ( 
        <div className='w-3/4 h-fit flex flex-col' ref={ref}>
            <div className='flex w-full justify-center'>
                <h1 className='text-black font-bold text-base'>Chọn chuyên khoa</h1>
            </div>
            <div className='w-full max-h-[460px] h-[460px] flex flex-col border border-black rounded-xl overflow-y-auto space-y-2'>
                <Button style={{width:'100%', height:'fit', padding: '25px', display: 'flex', flexDirection:'row', alignItems:'center', border: '1px solid black', borderRadius: '5px', justifyContent: 'space-between',position:'block'}} onClick = {() => handleCureInfo('BỆNH LÝ CỘT SỐNG', '150.000đ')}>
                    <div className='flex flex-row items-center space-x-4'>
                        <InfoCircleTwoTone style={{fontSize: '20px'}}/>
                        <p className='text-[#273c75] text-sm font-bold'>BỆNH LÝ CỘT SỐNG</p>
                    </div>
                    <div className='flex flex-row items-center space-x-4'>
                        <p className='text-black text-sm space-x-4'>
                            <span>150.000đ</span><RightOutlined />
                        </p>
                    </div>
                </Button>
                <Button style={{width:'100%', height:'70px', padding: '25px', display: 'flex', justifyContent: 'center',flexDirection:'column', alignItems:'center', border: '1px solid black', borderRadius: '5px'}}>
                    <div className='w-full h-fit flex flex-row items-center justify-between' onClick = {() => handleCureInfo('CHUYÊN GIA THẦN KINH', '150.000đ')}>
                        <div className='flex flex-row items-center space-x-4 justify-start'>
                            <InfoCircleTwoTone style={{fontSize: '20px'}}/>
                            <p className='text-[#273c75] text-sm font-bold'>CHUYÊN GIA THẦN KINH</p>
                        </div>
                        <div className='flex flex-row items-center'>
                            <p className='text-black text-sm space-x-4'>
                                <span>150.000đ</span><RightOutlined />
                            </p>
                        </div>
                    </div>
                    <div className='w-full h-fit flex items-center'>
                        <p className='text-black text-center '>(Chỉ nhận người bệnh tái khám hoặc được giới thiệu khám bởi BS Chuyên khoa)</p>
                    </div>
                </Button>
            </div>
        
        </div>
    )
}
export default Specialty_Booking;
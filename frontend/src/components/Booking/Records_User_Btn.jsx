import React from "react";
import { Button } from 'antd';
import { UserOutlined, BarcodeOutlined, PhoneOutlined, RightOutlined } from '@ant-design/icons';
import { useNavigate } from "react-router-dom";

const Records_User_Btn = ({...pat}) => {
    const navigate = useNavigate();
    
    const handleOpenRecord = () => {
        navigate('/doctor/patient', { state: { patient: pat } });
    };
    
    return (
        <Button
            style={{
                display: 'flex',
                flexDirection: 'column',
                height: '70px',
                width: '100%',
                padding: '12px',
                backgroundColor: 'white',
                color: '#273c75',
                border: '1px solid #e8e8e8',
                boxShadow: '0 2px 4px rgba(0,0,0,0.05)',
                marginBottom: '8px',
                borderRadius: '6px',
            }}
            type="default"
            size="large"
            onClick={handleOpenRecord}
            className="hover:border-blue-500 transition-all duration-300"
        >
            <div className='flex flex-row justify-between w-full items-center'>
                <div className='flex items-center space-x-2'>
                    <UserOutlined style={{ color: '#1890ff' }} />
                    <span className='font-bold text-sm'>{pat.name}</span>
                </div>
                <RightOutlined style={{ color: '#1890ff' }} />
            </div>
            <div className='flex flex-row justify-between w-full mt-2'>
                <div className='flex items-center space-x-2'>
                    <BarcodeOutlined style={{ color: '#52c41a' }} />
                    <span className='text-xs text-gray-600'>{pat.barcode}</span>
                </div>
                <div className='flex items-center space-x-2'>
                    <PhoneOutlined style={{ color: '#fa8c16' }} />
                    <span className='text-xs text-gray-600'>{pat.phoneNumber}</span>
                </div>
            </div>
        </Button>
    );
};

export default Records_User_Btn;
import React from 'react';
import { Button, Result } from 'antd';
import { useNavigate } from 'react-router-dom';

const ServiceCost = () => {
    const navigate = useNavigate();

    const handleGoBack = () => {
        navigate('/doctor');
    };

    return (
        <Result
            status="404"
            title="404"
            subTitle="Sorry, the page you visited does not exist."
            extra={
                <Button
                    type="primary"
                    onClick={handleGoBack}
                    className="bg-indigo-600 hover:bg-indigo-700"
                >
                    Back Home
                </Button>
            }
            className="bg-white w-screen h-full items-center flex flex-col justify-centerp-8"
        />
    );
};

export default ServiceCost;
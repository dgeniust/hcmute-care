import React from 'react';
import { 
  Button, 
  Timeline, 
  Card, 
  Typography, 
  Badge, 
  Space,
  Divider
} from 'antd';
import {
  ArrowLeftOutlined,
  ForkOutlined,
  RightOutlined, 
  ClockCircleOutlined, 
  CalendarOutlined, 
  MedicineBoxOutlined, 
  SyncOutlined, 
  CheckCircleFilled,
  FileSearchOutlined
} from '@ant-design/icons';

const { Title, Text } = Typography;

const Timeline_Booking = ({
  choosedSpecialty, 
  specialty, 
  step, 
  result, 
  selectedTime, 
  selectedValue, 
  selectedDoctor, 
  ref
}) => {
  
  // Helper function for rendering timeline item content
  const renderTimelineItem = (icon, label, value, isActive, isCompleted) => {
    return (
      <Card 
        className={`w-full border transition-all duration-300 ${
          isActive ? 'border-blue-500 shadow-md' : 
          isCompleted ? 'border-green-400' : 'border-gray-200'
        }`}
        bodyStyle={{ padding: '12px 16px' }}
      >
        <div className="flex items-center justify-between">
          <Space>
            <span className={`flex items-center justify-center h-8 w-8 rounded-full ${
              isActive ? 'bg-blue-100 text-blue-600' : 
              isCompleted ? 'bg-green-100 text-green-600' : 'bg-gray-100 text-gray-500'
            }`}>
              {icon}
            </span>
            <div>
              <Text strong className={
                isActive ? 'text-blue-600' : 
                isCompleted ? 'text-green-700' : 'text-gray-500'
              }>
                {label}
              </Text>
              {value && (
                <>
                  <Divider type="vertical" className="mx-2" />
                  <Text className="font-medium">
                    {value}
                  </Text>
                </>
              )}
            </div>
          </Space>
          
          {isCompleted && (
            <CheckCircleFilled className="text-green-500 text-lg" />
          )}
          {isActive && !isCompleted && (
            <Badge status="processing" className="animate-pulse" />
          )}
        </div>
      </Card>
    );
  };

  return (
    <div className="w-full bg-white p-4 rounded-lg shadow-sm" ref={ref}>
      <div className="mb-4">
        <Title level={4} className="text-gray-700 flex items-center">
          <FileSearchOutlined className="mr-2 text-blue-500" />
          Quá trình đặt lịch
        </Title>
      </div>
      
      <Timeline
        className="px-2"
        items={[
          {
            dot: step === 1 ? (
              <div className="bg-blue-500 flex items-center justify-center rounded-full h-8 w-8">
                <SyncOutlined spin className="text-white" />
              </div>
            ) : choosedSpecialty ? (
              <div className="bg-green-500 flex items-center justify-center rounded-full h-8 w-8">
                <Text className="text-white font-bold">1</Text>
              </div>
            ) : (
              <div className="bg-gray-300 flex items-center justify-center rounded-full h-8 w-8">
                <Text className="text-white font-bold">1</Text>
              </div>
            ),
            children: renderTimelineItem(
              <ForkOutlined />,
              "Chuyên khoa",
              choosedSpecialty ? specialty : null,
              step === 1,
              choosedSpecialty && step !== 1
            )
          },
          {
            dot: step === 2 && result == null ? (
              <div className="bg-blue-500 flex items-center justify-center rounded-full h-8 w-8">
                <SyncOutlined spin className="text-white" />
              </div>
            ) : result ? (
              <div className="bg-green-500 flex items-center justify-center rounded-full h-8 w-8">
                <Text className="text-white font-bold">2</Text>
              </div>
            ) : (
              <div className={`${step >= 2 ? 'bg-gray-500' : 'bg-gray-300'} flex items-center justify-center rounded-full h-8 w-8`}>
                <Text className="text-white font-bold">2</Text>
              </div>
            ),
            children: renderTimelineItem(
              <CalendarOutlined />,
              "Ngày khám",
              result || null,
              step === 2 && !result,
              result != null
            )
          },
          {
            dot: step === 3 && !selectedTime ? (
              <div className="bg-blue-500 flex items-center justify-center rounded-full h-8 w-8">
                <SyncOutlined spin className="text-white" />
              </div>
            ) : selectedTime ? (
              <div className="bg-green-500 flex items-center justify-center rounded-full h-8 w-8">
                <Text className="text-white font-bold">3</Text>
              </div>
            ) : (
              <div className={`${step >= 3 ? 'bg-gray-500' : 'bg-gray-300'} flex items-center justify-center rounded-full h-8 w-8`}>
                <Text className="text-white font-bold">3</Text>
              </div>
            ),
            children: renderTimelineItem(
              <ClockCircleOutlined />,
              "Giờ khám",
              selectedTime || null,
              step === 3 && !selectedTime,
              selectedTime != null
            )
          },
          {
            dot: step === 3 && !selectedDoctor ? (
              <div className="bg-blue-500 flex items-center justify-center rounded-full h-8 w-8">
                <SyncOutlined spin className="text-white" />
              </div>
            ) : selectedDoctor ? (
              <div className="bg-green-500 flex items-center justify-center rounded-full h-8 w-8">
                <Text className="text-white font-bold">4</Text>
              </div>
            ) : (
              <div className={`${step >= 3 ? 'bg-gray-500' : 'bg-gray-300'} flex items-center justify-center rounded-full h-8 w-8`}>
                <Text className="text-white font-bold">4</Text>
              </div>
            ),
            children: renderTimelineItem(
              <MedicineBoxOutlined />,
              "Bác sĩ",
              selectedDoctor || null,
              step === 3 && !selectedDoctor,
              selectedDoctor != null
            )
          },
        ]}
      />
      
      <div className="mt-6 p-3 bg-blue-50 rounded-lg border border-blue-100">
        <Space align="start">
          <SyncOutlined className="text-blue-500 mt-1" />
          <div>
            <Text strong className="text-blue-700">Tiến trình đặt lịch</Text>
            <Text className="block text-gray-600 text-sm mt-1">
              Vui lòng hoàn thành các bước để đặt lịch khám bệnh
            </Text>
          </div>
        </Space>
      </div>
    </div>
  );
};

export default Timeline_Booking;
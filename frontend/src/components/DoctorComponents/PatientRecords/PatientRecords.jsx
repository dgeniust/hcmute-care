import React from 'react';
import { Avatar, Collapse, theme, Card, Badge, Divider, Typography, Space, Tag, Tooltip,Progress } from 'antd';
import { 
  CaretRightOutlined, UserOutlined, PhoneOutlined, 
  MailOutlined, HomeOutlined, GlobalOutlined, IdcardOutlined,
  CalendarOutlined, MedicineBoxOutlined, FileTextOutlined, 
  DollarOutlined, ClockCircleOutlined
} from '@ant-design/icons';
import BloodPresure, { HeartRate, Temperature, BodyHW } from './SVGPatiendRecors';
import { useLocation } from 'react-router-dom';
import ParaclinicalTest from './ParaclinicalTest';
const { Title, Text, Paragraph } = Typography;

const PatientRecords = () => {
  const location = useLocation();
  const patient = location.state?.patient;
  const encounter = patient?.encounter || [];

  const { token } = theme.useToken();
  
  const panelStyle = {
    marginBottom: 16,
    background: token.colorFillAlter,
    borderRadius: token.borderRadiusLG,
    border: 'none',
    // boxShadow: '0 2px 8px rgba(0,0,0,0.05)',
    // overflow: 'hidden',
  };

  const getItems = (panelStyle, encounter) =>
    encounter.map((item) => ({
      key: item.id.toString(),
      label: (
        <div className="flex items-center py-1">
          <Badge 
            status="processing" 
            color={item.prescription.status === 'Hoàn thành' ? 'green' : 'blue'} 
          />
          <Space className="ml-2">
            <Text strong>{`Lần khám ngày ${item.visitDate}`}</Text>
            <Tag 
              color={item.prescription.status === 'Hoàn thành' ? 'success' : 'processing'}
              className="ml-2"
            >
              {item.prescription.status}
            </Tag>
          </Space>
        </div>
      ),
      children: (
        <Card 
          variant={false} 
          className="w-full shadow-sm bg-white"
          style={{ padding: '20px' }}
        >
          {/* Treatment Information */}
          <div className="space-y-5 mb-6">
            <div className="flex flex-col space-y-1">
              <div className="flex items-center mb-1">
                <MedicineBoxOutlined className="text-blue-600 mr-2" />
                <Text strong className="text-gray-800">Phương pháp điều trị</Text>
              </div>
              <Card size="small" className="bg-gray-50 border-gray-100">
                <Text>{item.treatment}</Text>
              </Card>
            </div>
            
            <div className="flex flex-col space-y-1">
              <div className="flex items-center mb-1">
                <FileTextOutlined className="text-green-600 mr-2" />
                <Text strong className="text-gray-800">Chẩn đoán</Text>
              </div>
              <Card size="small" className="bg-gray-50 border-gray-100">
                <Text>{item.diagnosis}</Text>
              </Card>
            </div>
            
            <div className="flex flex-col space-y-1">
              <div className="flex items-center mb-1">
                <FileTextOutlined className="text-amber-600 mr-2" />
                <Text strong className="text-gray-800">Ghi chú</Text>
              </div>
              <Card size="small" className="bg-gray-50 border-gray-100">
                <Paragraph ellipsis={{ rows: 3, expandable: true, symbol: 'Xem thêm' }}>
                  {item.notes}
                </Paragraph>
              </Card>
            </div>
          </div>
          
          <Divider className="my-6">
            <Space>
              <MedicineBoxOutlined />
              <span>Đơn thuốc</span>
            </Space>
          </Divider>
          
          {/* Đơn thuốc */}
          <div className="space-y-4">
            <div className="bg-gradient-to-r from-blue-50 to-indigo-50 p-4 rounded-lg border border-blue-100">
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4 mb-5">
                <div className="flex items-center">
                  <CalendarOutlined className="text-blue-500 mr-2" />
                  <Text type="secondary">Ngày kê đơn:</Text>
                  <Text strong className="ml-2">{item.prescription.issueDate}</Text>
                </div>
                
                <div className="flex items-center">
                  <ClockCircleOutlined className="text-blue-500 mr-2" />
                  <Text type="secondary">Trạng thái:</Text>
                  <Tag 
                    color={item.prescription.status === 'Hoàn thành' ? 'success' : 'processing'}
                    className="ml-2"
                  >
                    {item.prescription.status}
                  </Tag>
                </div>
              </div>

              <Title level={5} className="mb-4 flex items-center">
                <MedicineBoxOutlined className="mr-2" />
                Danh sách thuốc
              </Title>
              
              <Space direction="vertical" size="middle" className="w-full">
                {item.prescription.prescriptionItems.map((presItem) => (
                  <Card 
                    key={presItem.id} 
                    size="small" 
                    className="border border-blue-100 hover:border-blue-300 shadow-sm hover:shadow-md transition-all duration-300"
                    styles={{ body: { padding: 20 } }}
                  >
                    <div className="flex flex-col space-y-4">
                      <div className="flex flex-wrap justify-between items-center">
                        <div className="flex items-center">
                          <div className="w-2 h-10 bg-blue-600 rounded-full mr-3"></div>
                          <div>
                            <Text strong className="text-lg block">{presItem.name}</Text>
                            <Text type="secondary" className="text-sm">{presItem.medicine.strength}</Text>
                          </div>
                        </div>
                        <Tag color="blue" className="text-sm font-medium px-3 py-1">
                          {presItem.quantity} {presItem.unit}
                        </Tag>
                      </div>
                      
                      <div className="bg-blue-50 p-3 rounded-lg">
                        <Text className="block mb-2">{presItem.dosage}</Text>
                        <div className="flex items-center text-sm text-gray-600">
                          <Tooltip title="Cách sử dụng">
                            <span className="flex items-center">
                              <ClockCircleOutlined className="mr-1" />
                              {presItem.medicine.usage}
                            </span>
                          </Tooltip>
                          <Divider type="vertical" className="mx-2" />
                          <Tooltip title="Giá">
                            <span className="flex items-center font-medium text-blue-600">
                              <DollarOutlined className="mr-1" />
                              {presItem.medicine.price}đ
                            </span>
                          </Tooltip>
                        </div>
                      </div>
                    </div>
                  </Card>
                ))}
              </Space>
            </div>
          </div>
        </Card>
      ),
      style: panelStyle,
    }));

  // Giả lập thông số sức khỏe
  const vitalSigns = {
    bloodPressure: { value: "120/80", normal: true, percentage: 80 },
    heartRate: { value: "72", normal: true, percentage: 75 },
    temperature: { value: "38ºC", normal: false, percentage: 65 },
    bodyMetrics: { value: "177/55", normal: true, percentage: 85 }
  };

  const VitalSignCard = ({ icon, title, value, isNormal, percentage, bgClass, progressColor }) => (
    <Card className={`${bgClass} border-0 shadow-sm h-full hover:shadow-md transition-all duration-300`}>
      <div className="flex items-center mb-3">
        {icon}
        <Text strong className="text-base ml-2">{title}</Text>
      </div>
      
      <div className="flex flex-col items-center justify-center pb-1">
        <Text className={`text-2xl font-medium ${isNormal ? 'text-gray-700' : 'text-red-600'}`}>
          {value}
        </Text>
        <Progress 
          percent={percentage} 
          size="small" 
          strokeColor={progressColor}
          className="w-full mt-2" 
          showInfo={false}
        />
      </div>
    </Card>
  );
  
  return (
    <div className="w-full h-full p-4 md:p-6 lg:p-8 text-black bg-gradient-to-br from-white to-blue-50">
      <Card 
        className="w-full h-full rounded-xl overflow-hidden" 
        variant={false}
        styles={{ body: { padding: 0 } }}
      >
        <div className="flex flex-col lg:flex-row w-full h-full">
          {/* Patient Information Section */}
          <div className="w-full lg:w-1/2 h-full p-5 border-r border-gray-100 overflow-auto">
            <Card 
              variant={false} 
              className="mb-6 overflow-hidden bg-gradient-to-r from-blue-50 to-indigo-50"
            >
              <div className="flex flex-col md:flex-row items-center space-y-6 md:space-y-0 md:space-x-6">
                <div className="relative">
                  <div className="absolute inset-0 bg-gradient-to-r from-blue-200 to-indigo-200 rounded-full blur-md opacity-50"></div>
                  <div className="relative w-32 h-32 border-4 border-white rounded-full bg-gradient-to-r from-blue-100 to-white flex items-center justify-center shadow-lg">
                    <Avatar 
                      size={112} 
                      src={<img src={`https://api.dicebear.com/7.x/miniavs/svg?seed=${patient.gender === "Nam" ? "8" : "Brian"}`} alt="avatar" />}
                    />
                  </div>
                </div>
                
                <div className="flex flex-col text-left space-y-3 w-full">
                  <Title level={3} className="mb-0">{patient.name}</Title>
                  
                  <div className="bg-green-100 rounded-lg shadow-sm p-1">
                    <div className="flex items-center justify-center">
                      <p className='text-lg'>Số thứ tự: <span className="font-bold text-green-800">42</span></p>
                    </div>
                  </div>
                  
                  <div className="grid grid-cols-1 md:grid-cols-2 gap-y-3 gap-x-4">
                    <div className="flex items-center space-x-2">
                      <UserOutlined className="text-blue-500" />
                      <Text type="secondary">Giới tính:</Text>
                      <Text strong>{patient.gender}</Text>
                    </div>
                    
                    <div className="flex items-center space-x-2">
                      <CalendarOutlined className="text-blue-500" />
                      <Text type="secondary">Ngày sinh:</Text>
                      <Text strong>{patient.dob}</Text>
                    </div>
                  </div>
                </div>
              </div>
            </Card>
            
            <Card 
              variant={false} 
              className="mb-6 shadow-sm" 
              title={
                <div className="flex items-center">
                  <IdcardOutlined className="mr-2 text-blue-500" />
                  <span>Thông tin cá nhân</span>
                </div>
              }
            >
              <div className="grid grid-cols-1 md:grid-cols-2 gap-y-4 gap-x-6">
                <div className="flex items-center space-x-2">
                  <PhoneOutlined className="text-blue-500" />
                  <Text type="secondary">SĐT:</Text>
                  <Text strong>{patient.phoneNumber}</Text>
                </div>
                
                <div className="flex items-center space-x-2">
                  <MailOutlined className="text-blue-500" />
                  <Text type="secondary">Email:</Text>
                  <Text strong>{patient.email}</Text>
                </div>
                
                <div className="flex items-center space-x-2">
                  <IdcardOutlined className="text-blue-500" />
                  <Text type="secondary">Nghề nghiệp:</Text>
                  <Text strong>{patient.career}</Text>
                </div>
                
                <div className="flex items-center space-x-2">
                  <GlobalOutlined className="text-blue-500" />
                  <Text type="secondary">Quốc tịch:</Text>
                  <Text strong>{patient.nation}</Text>
                </div>
                
                <div className="flex items-center space-x-2 col-span-2">
                  <HomeOutlined className="text-blue-500" />
                  <Text type="secondary">Địa chỉ:</Text>
                  <Text strong>{patient.address}</Text>
                </div>
              </div>
            </Card>
            
            <div className="my-2">
              <Title level={5} className="flex items-center mb-4">
                <MedicineBoxOutlined className="mr-2 text-blue-500" />
                Thông số sức khỏe
              </Title>
            </div>
            
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4 mb-4">
              <VitalSignCard 
                icon={<BloodPresure />}
                title="Huyết áp"
                value={vitalSigns.bloodPressure.value}
                isNormal={vitalSigns.bloodPressure.normal}
                percentage={vitalSigns.bloodPressure.percentage}
                bgClass="bg-gradient-to-br from-white to-blue-100"
                progressColor="#4096ff"
              />
              
              <VitalSignCard 
                icon={<HeartRate />}
                title="Nhịp tim"
                value={vitalSigns.heartRate.value}
                isNormal={vitalSigns.heartRate.normal}
                percentage={vitalSigns.heartRate.percentage}
                bgClass="bg-gradient-to-br from-white to-red-100"
                progressColor="#ff4d4f"
              />
              
              <VitalSignCard 
                icon={<Temperature />}
                title="Nhiệt độ"
                value={vitalSigns.temperature.value}
                isNormal={vitalSigns.temperature.normal}
                percentage={vitalSigns.temperature.percentage}
                bgClass="bg-gradient-to-br from-white to-orange-100"
                progressColor="#fa8c16"
              />
              
              <VitalSignCard 
                icon={<BodyHW />}
                title="Chiều cao/Cân nặng"
                value={vitalSigns.bodyMetrics.value}
                isNormal={vitalSigns.bodyMetrics.normal}
                percentage={vitalSigns.bodyMetrics.percentage}
                bgClass="bg-gradient-to-br from-white to-violet-100"
                progressColor="#722ed1"
              />
            </div>
          </div>
          
          {/* Medical History Section */}
          <div className="w-full lg:w-1/2 h-full p-5 overflow-auto">
            <Card 
              variant={false} 
              className="h-full"
              title={
                <div className="flex items-center justify-center">
                  <FileTextOutlined className="mr-2 text-blue-500" />
                  <span>Lịch sử khám bệnh</span>
                </div>
              }
            >
              <Collapse
                bordered={false}
                // defaultActiveKey={['1']}
                expandIcon={({ isActive }) => (
                  <CaretRightOutlined 
                    rotate={isActive ? 90 : 0} 
                    className="text-blue-600" 
                  />
                )}
                className="bg-transparent"
                items={getItems(panelStyle, encounter)}
              />
            </Card>
          </div>
        </div>
      </Card>
      
      <ParaclinicalTest/>
      
    </div>
  );
};

export default PatientRecords;
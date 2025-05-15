import React, {useEffect, useState} from 'react';
import { Avatar, Collapse, theme, Card, Badge, Divider, Typography, Space, Tag, Tooltip,Progress, message } from 'antd';
import { 
  CaretRightOutlined, UserOutlined, PhoneOutlined, 
  MailOutlined, HomeOutlined, GlobalOutlined, IdcardOutlined,
  CalendarOutlined, MedicineBoxOutlined, FileTextOutlined, 
  DollarOutlined, ClockCircleOutlined
} from '@ant-design/icons';
import BloodPresure, { HeartRate, Temperature, BodyHW } from './SVGPatiendRecors';
import { useLocation } from 'react-router-dom';
import ParaclinicalTest from './ParaclinicalTest';
import { notifyErrorWithCustomMessage, notifySuccessWithCustomMessage, handleHttpStatusCode } from '../../../utils/notificationHelper';
const { Title, Text, Paragraph } = Typography;
import dayjs from 'dayjs';
const PatientRecords = () => {
  const location = useLocation();
  const patient = location.state?.patient;
  const encounter = patient?.encounter || [];
  const [messageApi, contextHolder] = message.useMessage();
  const [patientEncounterInfo, setPatientEncounterInfo] = useState(() => {
    const storedData = localStorage.getItem('patientEncounterInfo');
    return storedData ? JSON.parse(storedData) : null;
  });
  const [encounters, setEncounters] = useState([]);
  const apiUrl = import.meta.env.VITE_API_BASE_URL;
  const { token } = theme.useToken();
  
  const panelStyle = {
    marginBottom: 16,
    background: token.colorFillAlter,
    borderRadius: token.borderRadiusLG,
    border: 'none',
    // boxShadow: '0 2px 8px rgba(0,0,0,0.05)',
    // overflow: 'hidden',
  };
  const medicalRecordPatientId = localStorage.getItem('medicalRecordPatientId');
  useEffect(() => {
    const fetchPatientEncounterInfo = async () => {
      try {
        const response = await fetch(`${apiUrl}v1/medical-records/${medicalRecordPatientId}/encounters`, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
          }
        })
        if(!response.ok) {
          const errorText = await response.text();
          console.log("Error",errorText);
          handleHttpStatusCode(response.status,"", errorText, messageApi);
          return;
        }
        const data = await response.json();
        if(data && data.data.length > 0) {
          console.log("Data: ",data.data);
          notifySuccessWithCustomMessage("Lấy lịch sử khám bệnh thành công", messageApi);
          setEncounters(data.data);
        }
      } 
      catch(e) {
        console.error("Error fetching patient encounter info:", e);
        notifyErrorWithCustomMessage("Không thể lấy thông tin bệnh nhân. Vui lòng thử lại sau.", messageApi);
      }
    }
    fetchPatientEncounterInfo();
  },[])
  
  const waitingNumber = localStorage.getItem('waitingNumber') || null;
  
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
                      src={<img src={`https://api.dicebear.com/7.x/miniavs/svg?seed=${patientEncounterInfo.patient.gender === "MALE" ? "8" : "Liliana"}`} alt="avatar" />}
                    />
                  </div>
                </div>
                
                <div className="flex flex-col text-left space-y-3 w-full">
                  <Title level={3} className="mb-0">{patientEncounterInfo.patient.name}</Title>
                  
                  <div className="bg-green-100 rounded-lg shadow-sm h-[30px]">
                    <div className="flex items-center justify-center m-auto">
                      <p className='text-lg'>Số thứ tự: 
                        <span className="font-bold text-green-800">
                        {waitingNumber}
                        </span>
                      </p>
                    </div>
                  </div>
                  
                  <div className="flex flex-col space-y-2">
                    <div className="flex items-center space-x-2">
                      <UserOutlined className="text-blue-500" />
                      <Text type="secondary">Giới tính:</Text>
                      <Text strong>{patientEncounterInfo.patient.gender === "MALE" ? "Nam" : "Nữ"}</Text>
                    </div>
                    
                    <div className="flex items-center space-x-2">
                      <CalendarOutlined className="text-blue-500" />
                      <Text type="secondary">Ngày sinh:</Text>
                      <Text strong>{patientEncounterInfo.patient.dob ? dayjs(patientEncounterInfo.patient.dob).format('DD-MM-YYYY') : 'Không có dữ liệu'}</Text>
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
                  <Text strong>{patientEncounterInfo.patient.phone}</Text>
                </div>
                
                <div className="flex items-center space-x-2">
                  <MailOutlined className="text-blue-500" />
                  <Text type="secondary">Email:</Text>
                  <Text strong>{patientEncounterInfo.patient.email}</Text>
                </div>
                
                <div className="flex items-center space-x-2">
                  <IdcardOutlined className="text-blue-500" />
                  <Text type="secondary">Nghề nghiệp:</Text>
                  <Text strong>{patientEncounterInfo.patient.career}</Text>
                </div>
                
                <div className="flex items-center space-x-2">
                  <GlobalOutlined className="text-blue-500" />
                  <Text type="secondary">Quốc tịch:</Text>
                  <Text strong>{patientEncounterInfo.patient.nation}</Text>
                </div>
                
                <div className="flex items-center space-x-2 col-span-2">
                  <HomeOutlined className="text-blue-500" />
                  <Text type="secondary">Địa chỉ:</Text>
                  <Text strong>{patientEncounterInfo.patient.address}</Text>
                </div>
              </div>
            </Card>
          </div>
          
          {/* Medical History Section */}
          {/* <div className="w-full lg:w-1/2 h-full p-5 overflow-auto">
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
                items={getItems(panelStyle, encounters)}
              />
            </Card>
          </div> */}
        </div>
      </Card>
      
      <ParaclinicalTest/>
      {contextHolder}
    </div>
  );
};

export default PatientRecords;
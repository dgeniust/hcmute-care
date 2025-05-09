import React, { useState, useEffect } from 'react'; // Thêm useEffect để gọi API
import {message} from 'antd';
import { 
  WomanOutlined, 
  ManOutlined, 
  CalendarOutlined, 
  RightOutlined, 
  FileTextOutlined, 
  UserOutlined, 
  LinkedinOutlined, 
  ExclamationCircleOutlined 
} from '@ant-design/icons';
import { useNavigate } from 'react-router-dom';
import dayjs from 'dayjs'; // Import dayjs
import {handleHttpStatusCode, notifySuccessWithCustomMessage, notifyErrorWithCustomMessage} from '../../../utils/notificationHelper'; // Import hàm xử lý thông báo dựa trên status code
const DiagnoseRecord = () => {
  const [selectedPatient, setSelectedPatient] = useState(null);
  const [scheduleSlots, setScheduleSlots] = useState([]); // State để lưu danh sách time slots
  const [selectedSlotId, setSelectedSlotId] = useState(null); // State để lưu scheduleSlotId khi click
  const [messageApi, contextHolder] = message.useMessage(); // Sử dụng message API từ antd
  const [schedulePatientData, setSchedulePatientData] = useState([]); // State để lưu dữ liệu bệnh nhân từ API


  const doctorId = localStorage.getItem('customerId'); // Lấy doctorId từ localStorage
  //const formatDate = dayjs().format('YYYY-MM-DD'); // Định dạng ngày hiện tại
  // Gọi API khi component mount
  const formatDate = "2025-05-03"
  useEffect(() => {
    const fetchSchedule = async () => {
      try {
        const response = await fetch(`http://localhost:8080/api/v1/doctors/${doctorId}/schedule?date=${formatDate}`, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${localStorage.getItem('accessToken')}`,
          },
        });
        const data = await response.json();
        if (data.status === 200) {
          // Sắp xếp time slot theo thời gian bắt đầu (startTime)
          const sortedSlots = data.data.scheduleSlots.sort((a, b) => 
            a.timeSlot.startTime.localeCompare(b.timeSlot.startTime)
          );
          setScheduleSlots(sortedSlots); // Lưu time slots vào state
        }
      } catch (error) {
        console.error('Error fetching schedule:', error);
      }
    };

    fetchSchedule();
  }, []); // Gọi API khi component mount

  const handleSelectPatient = async (pat) => {
    
    localStorage.setItem('medicalRecordPatientId', pat.medicalRecordId); // Lưu medicalRecordId vào localStorage
    localStorage.setItem('ticketId', pat.id);
    localStorage.setItem('waitingNumber', pat.waitingNumber);
    try{
      const response = await fetch(`http://localhost:8080/api/v1/medical-records/${pat.medicalRecordId}`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${localStorage.getItem('accessToken')}`,
        },
      })
      console.log('Selected patient response:', response);
      if(!response.ok) {
        const errorText = await response.text();
        handleHttpStatusCode(response.status, '', errorText, messageApi); // Gọi hàm xử lý thông báo
        return;
      }
      const data = await response.json();
      if(data.status === 200 && data.data) {
        setSelectedPatient(data.data); // Cập nhật state với thông tin bệnh nhân
        notifySuccessWithCustomMessage('Lấy thông tin bệnh nhân thành công', messageApi); // Thông báo thành công
      }
    }
    catch(e){
      console.error('Error fetching patient data:', e);
      notifyErrorWithCustomMessage('Lỗi khi lấy thông tin bệnh nhân', messageApi); // Thông báo lỗi
    }
  };

  const handleSelectSlot = async (slotId) => {
    setSelectedSlotId(slotId); // Lưu scheduleSlotId khi click vào time slot
    localStorage.setItem('scheduleSlotDoctorId', slotId); // Lưu vào localStorage nếu cần thiết
    console.log('Selected slot ID:', selectedSlotId); // Kiểm tra ID đã được lưu chưa
    try {
      const response = await fetch(`http://localhost:8080/api/v1/schedule-slots/${slotId}/tickets?status=COMPLETED`,{
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${localStorage.getItem('accessToken')}`,
        },
      })
      if(!response.ok) {
        const errorText = await response.text();
        handleHttpStatusCode(response.status, '', errorText, messageApi); // Gọi hàm xử lý thông báo
        return;
      }
      const data = await response.json();
      console.log('Selected slot data:', data);
      if(data && data.data.length > 0) {
        const patients = data.data.map(item => ({
          id: item.id,
          ticketCode: item.ticketCode,
          waitingNumber: item.waitingNumber,
          patientName: item.patientName,
          medicalRecordId: item.medicalRecordId,
          medicalRecordBarcode: item.medicalRecordBarcode,
          dob: item.patientDob,
          gender: item.patientGender,
        })); // Lấy danh sách bệnh nhân từ response
        setSchedulePatientData(patients); // Cập nhật state với danh sách bệnh nhân
        console.log('Schedule patient data:', patients);
        notifySuccessWithCustomMessage('Lấy danh sách bệnh nhân thành công', messageApi); // Thông báo thành công
      }
    }
    catch (error) {
      setSchedulePatientData([]); // Clear patient list if no data
      console.error('Error selecting slot:', error);
      notifyErrorWithCustomMessage('Lỗi khi lấy danh sách bệnh nhân', messageApi); // Thông báo lỗi
    }
  };
  
  const navigate = useNavigate();
  const handleOpenRecord = async (patient) => {
    localStorage.setItem('patientEncounterInfo', JSON.stringify(patient)); // Lưu medicalRecordId vào localStorage
    const medicalRecordPatientId = localStorage.getItem('medicalRecordPatientId'); // Lấy medicalRecordId từ localStorage
    const day = dayjs().format('YYYY-MM-DD'); // Ngày khám bệnh
    const payload = {
      medicalRecordId: medicalRecordPatientId,
      prescriptionId: [0],
      treatment: "string",
      diagnosis: "string",
      visitDate: day, // Ngày khám bệnh
      notes: "string",
    }
    console.log('Payload:', payload); // Kiểm tra payload trước khi gửi
    try {
      const response = await fetch('http://localhost:8080/api/v1/encounters', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${localStorage.getItem('accessToken')}`, // Lấy accessToken từ localStorage
        },
        body: JSON.stringify(payload), // Chuyển đổi payload thành chuỗi JSON
      })
      console.log('Encounter response:', response);
      if(!response.ok) {
        const errorText = await response.text();
        console.error('Encounter error:', errorText);
        handleHttpStatusCode(response.status, '', errorText, messageApi); // Gọi hàm xử lý thông báo
        return;
      }
      const data = await response.json();
      if(data.status === 201 && data.data) {
        console.log('Encounter data:', data.data.id);
        localStorage.setItem('encounterId', data.data.id); // Lưu encounterId vào localStorage
        navigate('/doctor/patient');
        notifySuccessWithCustomMessage('Lấy thông tin bệnh nhân thành công', messageApi); // Thông báo thành công
      }
    }
    catch(e) {
      console.error('Error fetching patient data:', e);
      notifyErrorWithCustomMessage('Lỗi khi lấy thông tin bệnh nhân', messageApi); // Thông báo lỗi
    }
  };

  return (
    <div className="w-full h-full bg-gray-50">
      {/* Header */}
      <div className="bg-white p-6 shadow-sm border-b border-gray-200">
        <h1 className="text-2xl font-bold text-gray-800">Quản lý bệnh án</h1>
        <p className="text-gray-500 mt-1">Xem và quản lý hồ sơ bệnh án của bệnh nhân</p>
      </div>

      <div className="p-6">
        <div className="flex flex-col lg:flex-row gap-6 h-full">
          {/* Danh sách bệnh nhân */}
          <div className="lg:w-1/3 bg-white rounded-xl shadow-sm border border-gray-200 overflow-hidden">
            <div className="p-4 border-b border-gray-100">
              <h2 className="font-bold text-gray-800 flex items-center">
                <FileTextOutlined className="mr-2 text-blue-500" />
                Danh sách bệnh nhân
              </h2>

              {/* Hiển thị danh sách time slots */}
              <div className="mt-3 flex flex-wrap gap-2">
                {scheduleSlots && scheduleSlots.length > 0 ? (
                  scheduleSlots.map((slot) => (
                    <button
                      key={slot.id}
                      className={`px-3 py-1 text-sm rounded-full ${
                        selectedSlotId === slot.id
                          ? 'bg-blue-500 text-white'
                          : 'bg-blue-50 text-blue-600 hover:bg-blue-100'
                      }`}
                      onClick={() => handleSelectSlot(slot.id)}
                    >
                      {slot.timeSlot.startTime} - {slot.timeSlot.endTime}
                    </button>
                  ))
                ) : (
                  <p className="text-sm text-gray-500">Không có lịch trống</p>
                )}
              </div>
            </div>
            
            {/* Patient list */}
            <div className="overflow-y-auto max-h-[600px]">
              {schedulePatientData && schedulePatientData.length > 0 ? (
                schedulePatientData.map((pat, index) => (
                  <div 
                    key={index}
                    className={`p-4 border-b border-gray-100 hover:bg-blue-50 cursor-pointer transition duration-150 ${selectedPatient === pat ? 'bg-blue-50' : ''}`}
                    onClick={() => handleSelectPatient(pat)}
                  >
                    <div className='flex flex-row space-x-2'>
                      <div className='flex flex-col w-full'>
                        <div className="flex justify-between items-center">
                          <h3 className="font-medium text-gray-800">{pat.patientName}</h3>
                          <RightOutlined className="text-gray-400" />
                        </div>
                        <div className="flex mt-2 gap-x-4">
                          <span className="text-xs bg-blue-100 text-blue-800 px-2 py-1 rounded flex items-center">
                            <UserOutlined className="mr-1" />
                            {pat.dob}
                          </span>
                          <span className="text-xs bg-gray-100 text-gray-600 px-2 py-1 rounded">
                            {pat.ticketCode}
                          </span>
                        </div>
                      </div>
                      <div className="flex mt-2 gap-x-4">
                        <div className="w-[40px] h-[40px] rounded-full bg-blue-500 flex items-center justify-center">
                          <span className="text-base font-medium text-white">{pat.waitingNumber}</span>
                        </div>
                      </div>
                    </div>
                  </div>
                ))
              ) : (
                <div className="p-8 text-center text-gray-500">
                  <ExclamationCircleOutlined style={{ fontSize: '40px' }} className="block mx-auto mb-2 text-gray-300" />
                  Không tìm thấy bệnh nhân
                </div>
              )}
            </div>
          </div>
          
          {/* Chi tiết bệnh án */}
          <div className="lg:w-2/3 bg-white rounded-xl shadow-sm border border-gray-200 overflow-hidden text-black">
            {selectedPatient ? (
              <div className="h-full flex flex-col">
                {/* Patient info header */}
                <div className="p-6 border-b border-gray-100 bg-gradient-to-r from-blue-50 to-white">
                  <div className="flex justify-between items-start">
                    <div className='w-full'>
                      <h2 className="text-xl font-bold text-gray-800">{selectedPatient.patient?.name|| 'Không có tên'}</h2>
                      <div className="flex gap-4 mt-2 text-sm text-gray-600 w-full">
                        <span className="flex items-center w-fit">
                          <CalendarOutlined className="mr-1 text-blue-500" />
                          {selectedPatient.patient?.dob ? dayjs(selectedPatient.patient.dob).format('DD-MM-YYYY') : 'Không có dữ liệu'}
                        </span>
                        <span>|</span>
                        <span>
                          {
                            selectedPatient.patient?.gender ==="FEMALE" ? <WomanOutlined className="mr-1 text-blue-500" /> : <ManOutlined className="mr-1 text-blue-500" />
                          }
                          {selectedPatient.patient?.gender === "MALE" ? "Nam" : "Nữ" || 'Không có dữ liệu'}
                        </span>
                        <span>|</span>
                        <span><LinkedinOutlined className="mr-1 text-blue-500"  />{selectedPatient.patient?.career|| 'Không có dữ liệu'}</span>
                      </div>
                    </div>
                    <div className='w-full flex flex-row justify-end space-x-4'>
                        <div className="bg-white text-blue-600 px-4 py-2 rounded-lg shadow-sm cursor-pointer hover:bg-blue-50 transition duration-150" onClick={() => {
                          if (selectedPatient) {
                            handleOpenRecord(selectedPatient);
                          }
                        }}>
                        Xem chi tiết hồ sơ
                        </div>
                        <div className="bg-blue-600 text-white px-4 py-2 rounded-lg shadow-sm cursor-pointer hover:bg-blue-500 transition duration-150">
                        {selectedPatient.barcode|| 'Không có dữ liệu'}
                        </div>
                    </div>
                  </div>
                  
                  <div className="mt-4 grid grid-cols-1 md:grid-cols-2 gap-4 text-sm">
                    <div className="flex flex-col">
                      <span className="text-gray-500">Địa chỉ</span>
                      <span className="font-medium">{selectedPatient.patient?.address|| 'Không có dữ liệu'}</span>
                    </div>
                    <div className="flex flex-col">
                      <span className="text-gray-500">Email</span>
                      <span className="font-medium">{selectedPatient.patient?.email|| 'Không có dữ liệu'}</span>
                    </div>
                    <div className="flex flex-col">
                      <span className="text-gray-500">Số điện thoại</span>
                      <span className="font-medium">{selectedPatient.patient?.phone|| 'Không có dữ liệu'}</span>
                    </div>
                    <div className="flex flex-col">
                      <span className="text-gray-500">Quốc tịch</span>
                      <span className="font-medium">{selectedPatient.patient?.nation|| 'Không có dữ liệu'}</span>
                    </div>
                  </div>
                </div>
                
                {/* Medical records */}
                {/* <div className="p-6 overflow-y-auto flex-1">
                  <h3 className="font-bold text-gray-800 mb-4 flex items-center">
                    <ClockCircleOutlined className="mr-2 text-blue-500" />
                    Lịch sử khám bệnh
                  </h3>
                  
                  {selectedPatient.encounter && selectedPatient.encounter.length > 0 ? (
                    <div className="space-y-4">
                      {selectedPatient.encounter.map((enc, idx) => (
                        <div key={idx} className="border border-gray-200 rounded-lg overflow-hidden">
                          <div className="bg-gray-50 p-4 border-b border-gray-200 flex justify-between items-center">
                            <h4 className="font-medium text-gray-800">Lần khám ngày {enc.visitDate}</h4>
                            <span className={`px-3 py-1 text-xs rounded-full ${
                              enc.prescription.status === 'ĐÃ DUYỆT' ? 'bg-green-100 text-green-800' : 'bg-yellow-100 text-yellow-800'
                            }`}>
                              {enc.prescription.status}
                            </span>
                          </div>
                          
                          <div className="p-4">
                            <div className="grid grid-cols-1 md:grid-cols-2 gap-4 mb-4">
                              <div>
                                <h5 className="text-sm font-medium text-gray-500">Chẩn đoán</h5>
                                <p className="text-gray-800">{enc.diagnosis}</p>
                              </div>
                              <div>
                                <h5 className="text-sm font-medium text-gray-500">Phương pháp điều trị</h5>
                                <p className="text-gray-800">{enc.treatment}</p>
                              </div>
                            </div>
                            
                            <div className="mb-4">
                              <h5 className="text-sm font-medium text-gray-500">Ghi chú</h5>
                              <p className="text-gray-700 bg-gray-50 p-3 rounded mt-1">{enc.notes}</p>
                            </div>
                            
                            <div className="mt-4">
                              <h5 className="text-sm font-medium text-gray-500 mb-2">Đơn thuốc ngày {enc.prescription.issueDate}</h5>
                              <div className="bg-blue-50 rounded-lg p-4">
                                {enc.prescription.prescriptionItems.map((item, i) => (
                                  <div key={i} className="mb-3 last:mb-0 bg-white p-3 rounded-lg border border-blue-100 shadow-sm">
                                    <div className="flex justify-between">
                                      <span className="font-medium text-gray-800">{item.name} ({item.strength})</span>
                                      <span className="text-blue-700 font-medium">{(item.medicine.price).toLocaleString('vi-VN')} VNĐ</span>
                                    </div>
                                    <div className="mt-1 flex flex-wrap gap-3 text-sm">
                                      <span className="text-gray-600">{item.quantity} {item.unit}</span>
                                      <span className="text-gray-600">{item.dosage}</span>
                                      <span className="text-gray-600">{item.medicine.usage}</span>
                                    </div>
                                  </div>
                                ))}
                              </div>
                            </div>
                          </div>
                        </div>
                      ))}
                    </div>
                  ) : (
                    <div className="text-center py-12 text-gray-500">
                      <ExclamationCircleOutlined style={{ fontSize: '40px' }} className="block mx-auto mb-2 text-gray-300" />
                      Bệnh nhân chưa có lần khám nào
                    </div>
                  )}
                </div> */}
              </div>
              ) : (
              <div className="h-full flex items-center justify-center p-8 text-center text-gray-500">
                <div>
                  <FileTextOutlined style={{ fontSize: '64px' }} className="block mx-auto mb-4 text-gray-300" />
                  <h3 className="text-lg font-medium text-gray-700 mb-2">Chọn bệnh nhân để xem chi tiết</h3>
                  <p className="max-w-md mx-auto">Vui lòng chọn một bệnh nhân từ danh sách bên trái để xem thông tin chi tiết và lịch sử khám bệnh</p>
                </div>
              </div>
            )}
          </div>
        </div>
      </div>
      {contextHolder}
    </div>
  );
};

export default DiagnoseRecord;
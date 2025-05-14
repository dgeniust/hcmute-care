
import React, { useState, useEffect } from 'react';
import { message } from 'antd';

import {
  WomanOutlined,
  ManOutlined,
  CalendarOutlined,
  RightOutlined,
  FileTextOutlined,
  UserOutlined,
  LinkedinOutlined,
  ExclamationCircleOutlined,

} from '@ant-design/icons';
import { useNavigate } from 'react-router-dom';
import dayjs from 'dayjs';
import { handleHttpStatusCode, notifySuccessWithCustomMessage, notifyErrorWithCustomMessage } from '../../../utils/notificationHelper';

const DiagnoseRecord = () => {
  const apiUrl = import.meta.env.VITE_API_BASE_URL;
  const [selectedPatient, setSelectedPatient] = useState(null);

  const [scheduleSlots, setScheduleSlots] = useState([]);
  const [selectedSlotId, setSelectedSlotId] = useState(null);
  const [messageApi, contextHolder] = message.useMessage();
  const [schedulePatientData, setSchedulePatientData] = useState([]);
  const doctorId = localStorage.getItem('customerId');
  const formatDate = dayjs().format('YYYY-MM-DD');
  const navigate = useNavigate();

  // Gọi API lấy danh sách lịch
  useEffect(() => {
    const fetchSchedule = async () => {
      try {
        const response = await fetch(`${apiUrl}v1/doctors/${doctorId}/schedule?date=${formatDate}`, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${localStorage.getItem('accessToken')}`,
          },
        });
        if(!response.ok) {
          handleHttpStatusCode(response.status, '', "Hôm nay bác sĩ không có lịch làm việc", messageApi);
          return;
        }
        const data = await response.json();
        if (data.status === 200) {
          const sortedSlots = data.data.scheduleSlots.sort((a, b) =>
            a.timeSlot.startTime.localeCompare(b.timeSlot.startTime)
          );
          setScheduleSlots(sortedSlots);
        }
      } catch (error) {
        console.error("Error fetching schedule:", error);
      }
    };
    fetchSchedule();

  }, []);
  // Lấy danh sách bệnh nhân có trạng thái COMPLETED
  const handleSelectSlot = async (slotId) => {
    setSelectedSlotId(slotId);
    localStorage.setItem('scheduleSlotDoctorId', slotId);
    try {
      const response = await fetch(`${apiUrl}v1/schedule-slots/${slotId}/tickets?status=COMPLETED`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${localStorage.getItem('accessToken')}`,
        },
      });
      if (!response.ok) {
        const errorText = await response.text();
        handleHttpStatusCode(response.status, '', errorText, messageApi);
        return;
      }
      const data = await response.json();

      if (data && data.data.length > 0) {
        const patients = data.data.map((item) => ({
          id: item.id,
          ticketCode: item.ticketCode,
          waitingNumber: item.waitingNumber,
          patientName: item.patientName,
          medicalRecordId: item.medicalRecordId,
          medicalRecordBarcode: item.medicalRecordBarcode,
          dob: item.patientDob,
          gender: item.patientGender,

        }));
        setSchedulePatientData(patients);
        notifySuccessWithCustomMessage('Lấy danh sách bệnh nhân thành công', messageApi);
      } else {
        setSchedulePatientData([]);
      }
    } catch (error) {
      setSchedulePatientData([]);
      console.error('Error selecting slot:', error);
      notifyErrorWithCustomMessage('Lỗi khi lấy danh sách bệnh nhân', messageApi);
    }
  };
  // Lấy thông tin bệnh nhân
  const handleSelectPatient = async (pat) => {
    localStorage.setItem('medicalRecordPatientId', pat.medicalRecordId);
    localStorage.setItem('ticketId', pat.id);
    localStorage.setItem('waitingNumber', pat.waitingNumber);
    try {
      const response = await fetch(`${apiUrl}v1/medical-records/${pat.medicalRecordId}`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${localStorage.getItem('accessToken')}`,
        },
      });
      if (!response.ok) {
        const errorText = await response.text();
        handleHttpStatusCode(response.status, '', errorText, messageApi);
        return;
      }
      const data = await response.json();
      if (data.status === 200 && data.data) {
        setSelectedPatient(data.data);
        notifySuccessWithCustomMessage('Lấy thông tin bệnh nhân thành công', messageApi);
      }
    } catch (e) {
      console.error('Error fetching patient data:', e);
      notifyErrorWithCustomMessage('Lỗi khi lấy thông tin bệnh nhân', messageApi);

    }
  };
  useEffect(() => {
    console.log('Selected patient data -----------------:', selectedPatient);
  },[selectedPatient]);

  // Gửi chẩn đoán và kê đơn thuốc
  const handleDiagnose = async (patient) => {
    localStorage.setItem('patientEncounterInfo', JSON.stringify(patient)); // Lưu medicalRecordId vào localStorage
      try {
        const encounterResponse = await fetch(
          `${apiUrl}v1/medical-records/${patient.id}/date/encounters?date=2025-05-14`,
          {
            method: 'GET',
            headers: {
              'Content-Type': 'application/json',
            },
          }
        );
        if (!encounterResponse.ok) {
          const errorText = await encounterResponse.text();
          handleHttpStatusCode(encounterResponse.status, '', errorText, messageApi);
          return;
        }
        const data = await encounterResponse.json();
        if (data.status !== 200 || !data.data || data.data.length === 0) {
          notifyErrorWithCustomMessage('Không tìm thấy encounter cho bệnh nhân này', messageApi);
          return;
        }
        if(data && data.data.length > 0) {
          const encounterId = data.data[0].id; // Lấy encounterId từ dữ liệu trả về
          localStorage.setItem('confirmEncounterId', encounterId);
          navigate('/doctor/detail-diagnose');
          notifySuccessWithCustomMessage('Lấy thông tin bệnh nhân thành công', messageApi); // Thông báo thành công
        }
      }
      catch(e){
        console.error("Error sending diagnosis:", e);
        notifyErrorWithCustomMessage("Lỗi khi gửi chẩn đoán", messageApi);
      }
  };

  return (
    <div className="w-full h-full bg-gray-50">
      <div className="bg-white p-6 shadow-sm border-b border-gray-200">

        <h1 className="text-2xl font-bold text-gray-800">Chẩn đoán bệnh án</h1>
        <p className="text-gray-500 mt-1">Xem và chẩn đoán bệnh nhân đã hoàn thành khám lâm sàng</p>

      </div>

      <div className="p-6">
        <div className="flex flex-col lg:flex-row gap-6 h-full">
          <div className="lg:w-1/3 bg-white rounded-xl shadow-sm border border-gray-200 overflow-hidden">
            <div className="p-4 border-b border-gray-100">
              <h2 className="font-bold text-gray-800 flex items-center">
                <FileTextOutlined className="mr-2 text-blue-500" />
                Danh sách bệnh nhân
              </h2>
              <div className="mt-3 flex flex-wrap gap-2">
                {scheduleSlots.length > 0 ? (
                  scheduleSlots.map((slot) => (
                    <button
                      key={slot.id}
                      className={`px-3 py-1 text-sm rounded-full ${

                        selectedSlotId === slot.id ? 'bg-blue-500 text-white' : 'bg-blue-50 text-blue-600 hover:bg-blue-100'

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

            <div className="overflow-y-auto max-h-[600px]">
              {schedulePatientData.length > 0 ? (
                schedulePatientData.map((pat, index) => (
                  <div
                    key={index}
                    className={`p-4 border-b border-gray-100 hover:bg-blue-50 cursor-pointer transition duration-150 ${

                      selectedPatient?.medicalRecordId === pat.medicalRecordId ? 'bg-blue-50' : ''

                    }`}
                    onClick={() => handleSelectPatient(pat)}
                  >
                    <div className="flex flex-row space-x-2">
                      <div className="flex flex-col w-full">
                        <div className="flex justify-between items-center">
                          <h3 className="font-medium text-gray-800">
                            {pat.patientName}
                          </h3>
                          <RightOutlined className="text-gray-400" />
                        </div>
                        <div className="flex mt-2 gap-x-4">
                          <span className="text-xs bg-blue-100 text-blue-800 px-2 py-1 rounded flex items-center">
                            <UserOutlined className="mr-1" />
                            {pat.dob}
                          </span>
                          <span className="text-xs bg-gray-100 text-gray-600 px-2 py-1 rounded">{pat.ticketCode}</span>
                        </div>
                      </div>
                      <div className="flex mt-2 gap-x-4">
                        <div className="w-[40px] h-[40px] rounded-full bg-blue-500 flex items-center justify-center">
                          <span className="text-base font-medium text-white">
                            {pat.waitingNumber}
                          </span>
                        </div>
                      </div>
                    </div>
                  </div>
                ))
              ) : (
                <div className="p-8 text-center text-gray-500">
                  <ExclamationCircleOutlined
                    style={{ fontSize: "40px" }}
                    className="block mx-auto mb-2 text-gray-300"
                  />
                  Không tìm thấy bệnh nhân
                </div>
              )}
            </div>
          </div>
          <div className="lg:w-2/3 bg-white rounded-xl shadow-sm border border-gray-200 overflow-hidden text-black">
            {selectedPatient ? (
              <div className="h-full flex flex-col">
                <div className="p-6 border-b border-gray-100 bg-gradient-to-r from-blue-50 to-white">
                  <div className="flex justify-between items-start">
                    <div className="w-full">
                      <h2 className="text-xl font-bold text-gray-800">{selectedPatient.patient?.name || 'Không có tên'}</h2>

                      <div className="flex gap-4 mt-2 text-sm text-gray-600 w-full">
                        <span className="flex items-center w-fit">
                          <CalendarOutlined className="mr-1 text-blue-500" />
                          {selectedPatient.patient?.dob

                            ? dayjs(selectedPatient.patient.dob).format('DD-MM-YYYY')
                            : 'Không có dữ liệu'}
                        </span>
                        <span>|</span>
                        <span>
                          {selectedPatient.patient?.gender === 'FEMALE' ? (

                            <WomanOutlined className="mr-1 text-blue-500" />
                          ) : (
                            <ManOutlined className="mr-1 text-blue-500" />
                          )}

                          {selectedPatient.patient?.gender === 'MALE' ? 'Nam' : 'Nữ' || 'Không có dữ liệu'}

                        </span>
                        <span>|</span>
                        <span>
                          <LinkedinOutlined className="mr-1 text-blue-500" />

                          {selectedPatient.patient?.career || 'Không có dữ liệu'}

                        </span>
                      </div>
                    </div>
                    <div className="w-full flex flex-row justify-end space-x-4">
                      <div
                        className="bg-white text-blue-600 px-4 py-2 rounded-lg shadow-sm cursor-pointer hover:bg-blue-50 transition duration-150"

                        onClick={() => handleDiagnose(selectedPatient)}
                      >
                        Chẩn đoán và kê đơn
                      </div>
                      <div className="bg-blue-600 text-white px-4 py-2 rounded-lg shadow-sm cursor-pointer hover:bg-blue-500 transition duration-150">
                        {selectedPatient.barcode || 'Không có dữ liệu'}
                      </div>
                    </div>
                  </div>
                  <div className="mt-4 grid grid-cols-1 md:grid-cols-2 gap-4 text-sm">
                    <div className="flex flex-col">
                      <span className="text-gray-500">Địa chỉ</span>
                      <span className="font-medium">{selectedPatient.patient?.address || 'Không có dữ liệu'}</span>
                    </div>
                    <div className="flex flex-col">
                      <span className="text-gray-500">Email</span>
                      <span className="font-medium">{selectedPatient.patient?.email || 'Không có dữ liệu'}</span>
                    </div>
                    <div className="flex flex-col">
                      <span className="text-gray-500">Số điện thoại</span>
                      <span className="font-medium">{selectedPatient.patient?.phone || 'Không có dữ liệu'}</span>
                    </div>
                    <div className="flex flex-col">
                      <span className="text-gray-500">Quốc tịch</span>
                      <span className="font-medium">{selectedPatient.patient?.nation || 'Không có dữ liệu'}</span>
                    </div>
                  </div>
                </div>

              </div>
            ) : (
              <div className="h-full flex items-center justify-center p-8 text-center text-gray-500">
                <div>

                  <FileTextOutlined style={{ fontSize: '64px' }} className="block mx-auto mb-4 text-gray-300" />
                  <h3 className="text-lg font-medium text-gray-700 mb-2">Chọn bệnh nhân để chẩn đoán</h3>
                  <p className="max-w-md mx-auto">Vui lòng chọn một bệnh nhân từ danh sách bên trái để chẩn đoán và kê đơn</p>

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

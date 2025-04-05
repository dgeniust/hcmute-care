import React, { useState } from 'react';
import { 
  SearchOutlined, 
  FilterOutlined, 
  CalendarOutlined, 
  RightOutlined, 
  FileTextOutlined, 
  UserOutlined, 
  ClockCircleOutlined, 
  ExclamationCircleOutlined 
} from '@ant-design/icons';
import { useNavigate } from 'react-router-dom';
const DoctorManageRecords = () => {
  const [selectedPatient, setSelectedPatient] = useState(null);
  const [searchQuery, setSearchQuery] = useState('');

  const patient = [
    {
      "name": "Nguyễn Thành Đạt",
      "dob": "14/01/2004",
      "barcode": "W24-0068373",
      "phoneNumber": "0387***823",
      "gender": "Nam",
      "email": "dathiichan141@gmail.com",
      "career": "Sinh viên",
      "nation": "Việt Nam",
      "address": "Số 6 Đường 449 Tăng Nhơn Phú A Quận 9 Thủ Đức",
      "encounter": [
        {
          "id": 1,
          "treatment": "Vật lý trị liệu",
          "diagnosis": "Căng cơ",
          "visitDate": "2025-03-25",
          "notes": "Bệnh nhân báo cáo đau nhẹ, khuyến nghị bài tập giãn cơ.",
          "prescription": {
            "id": 101,
            "issueDate": "2025-03-25",
            "status": "ĐÃ DUYỆT",
            "prescriptionItems": [
              {
                "id": 1001,
                "name": "Ibuprofen",
                "dosage": "200mg",
                "quantity": 30,
                "unit": "viên",
                "medicine": {
                  "id": 5001,
                  "name": "Ibuprofen",
                  "usage": "Uống sau khi ăn",
                  "strength": "200mg",
                  "price": 137000
                }
              }
            ]
          }
        },
        {
          "id": 2,
          "treatment": "Điều trị kháng sinh",
          "diagnosis": "Nhiễm khuẩn",
          "visitDate": "2025-04-01",
          "notes": "Kê đơn thuốc kháng sinh trong 7 ngày.",
          "prescription": {
            "id": 102,
            "issueDate": "2025-04-01",
            "status": "ĐÃ DUYỆT",
            "prescriptionItems": [
              {
                "id": 1002,
                "name": "Amoxicillin",
                "dosage": "500mg",
                "quantity": 21,
                "unit": "viên nang",
                "medicine": {
                  "id": 5002,
                  "name": "Amoxicillin",
                  "usage": "Uống 3 lần mỗi ngày",
                  "strength": "500mg",
                  "price": 250000
                }
              },
              {
                "id": 1003,
                "name": "Paracetamol",
                "dosage": "500mg",
                "quantity": 21,
                "unit": "viên nang",
                "medicine": {
                  "id": 5002,
                  "name": "Amoxicillin",
                  "usage": "Uống 1 lần mỗi ngày",
                  "strength": "500mg",
                  "price": 25000
                }
              }
            ]
          }
        }
      ]
    },
    // Giả lập thêm một vài bệnh nhân để minh họa giao diện
    {
      "name": "Trần Minh Hiếu",
      "dob": "22/05/1995",
      "barcode": "W24-0068374",
      "phoneNumber": "0905***456",
      "gender": "Nam",
      "email": "hieutran@gmail.com",
      "career": "Kỹ sư",
      "nation": "Việt Nam",
      "address": "123 Nguyễn Văn Linh, Quận 7, TP.HCM",
      "encounter": []
    },
    {
      "name": "Phạm Thu Hà",
      "dob": "10/12/1990",
      "barcode": "W24-0068375",
      "phoneNumber": "0913***789",
      "gender": "Nữ",
      "email": "hapham@gmail.com",
      "career": "Giáo viên",
      "nation": "Việt Nam",
      "address": "45 Lê Lợi, Quận 1, TP.HCM",
      "encounter": []
    }
  ];

  const handleSelectPatient = (pat) => {
    setSelectedPatient(pat);
  };

  const filteredPatients = patient.filter(pat => 
    pat.name.toLowerCase().includes(searchQuery.toLowerCase()) || 
    pat.barcode.toLowerCase().includes(searchQuery.toLowerCase()) ||
    pat.phoneNumber.includes(searchQuery)
  );
  const navigate = useNavigate();
  const handleOpenRecord = (patient) => {
    navigate('/doctor/patient', { state: { patient: patient } });
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
              
              {/* Search bar */}
              <div className="mt-3 relative">
                <input
                  type="text"
                  placeholder="Tìm kiếm theo tên, mã hoặc số điện thoại..."
                  className="w-full px-4 py-2 pl-10 rounded-lg border border-gray-200 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                  value={searchQuery}
                  onChange={(e) => setSearchQuery(e.target.value)}
                />
                <span className="absolute left-3 top-3 text-gray-400">
                  <SearchOutlined />
                </span>
              </div>
              
              {/* Filter options */}
              <div className="mt-3 flex gap-2">
                <button className="flex items-center px-3 py-1 text-sm bg-blue-50 text-blue-600 rounded-full">
                  <FilterOutlined className="mr-1" />
                  Mới nhất
                </button>
                <button className="flex items-center px-3 py-1 text-sm bg-gray-100 text-gray-600 rounded-full">
                  <CalendarOutlined className="mr-1" />
                  Tuần này
                </button>
              </div>
            </div>
            
            {/* Patient list */}
            <div className="overflow-y-auto max-h-[600px]">
              {filteredPatients.length > 0 ? (
                filteredPatients.map((pat, index) => (
                  <div 
                    key={index}
                    className={`p-4 border-b border-gray-100 hover:bg-blue-50 cursor-pointer transition duration-150 ${selectedPatient === pat ? 'bg-blue-50' : ''}`}
                    onClick={() => handleSelectPatient(pat)}
                  >
                    <div className="flex justify-between items-center">
                      <h3 className="font-medium text-gray-800">{pat.name}</h3>
                      <RightOutlined className="text-gray-400" />
                    </div>
                    <div className="flex mt-2 gap-x-4">
                      <span className="text-xs bg-blue-100 text-blue-800 px-2 py-1 rounded flex items-center">
                        <UserOutlined className="mr-1" />
                        {pat.barcode}
                      </span>
                      <span className="text-xs bg-gray-100 text-gray-600 px-2 py-1 rounded">
                        {pat.phoneNumber}
                      </span>
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
                    <div>
                      <h2 className="text-xl font-bold text-gray-800">{selectedPatient.name}</h2>
                      <div className="flex gap-4 mt-2 text-sm text-gray-600">
                        <span className="flex items-center">
                          <CalendarOutlined className="mr-1 text-blue-500" />
                          {selectedPatient.dob}
                        </span>
                        <span>|</span>
                        <span>{selectedPatient.gender}</span>
                        <span>|</span>
                        <span>{selectedPatient.career}</span>
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
                        {selectedPatient.barcode}
                        </div>
                    </div>
                  </div>
                  
                  <div className="mt-4 grid grid-cols-1 md:grid-cols-2 gap-4 text-sm">
                    <div className="flex flex-col">
                      <span className="text-gray-500">Địa chỉ</span>
                      <span className="font-medium">{selectedPatient.address}</span>
                    </div>
                    <div className="flex flex-col">
                      <span className="text-gray-500">Email</span>
                      <span className="font-medium">{selectedPatient.email}</span>
                    </div>
                    <div className="flex flex-col">
                      <span className="text-gray-500">Số điện thoại</span>
                      <span className="font-medium">{selectedPatient.phoneNumber}</span>
                    </div>
                    <div className="flex flex-col">
                      <span className="text-gray-500">Quốc tịch</span>
                      <span className="font-medium">{selectedPatient.nation}</span>
                    </div>
                  </div>
                </div>
                
                {/* Medical records */}
                <div className="p-6 overflow-y-auto flex-1">
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
                            
                            {/* Prescription */}
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
                </div>
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
    </div>
  );
};

export default DoctorManageRecords;

import React, { useState, useEffect } from 'react';
import { Avatar, Card, Badge, Typography, Space, Tag, Tooltip, Button, Table, List, Modal, Input, Select, message, Form } from 'antd';
import {
  HeartFilled,
  CalendarOutlined,
  MedicineBoxOutlined,
  FileTextOutlined,
  FileImageOutlined,
  EyeOutlined,
  DownloadOutlined,
  DeleteOutlined,
  LineChartOutlined,
  FileSearchOutlined,
  EditOutlined,
} from '@ant-design/icons';
import { notifyErrorWithCustomMessage, notifySuccessWithCustomMessage, handleHttpStatusCode } from '../../../utils/notificationHelper';
import { formatDateTime } from '../../../utils/formatDate';
import dayjs from 'dayjs';

const { Text } = Typography;
const { Option } = Select;

const DiagnoseEncounter = () => {
  const [messageApi, contextHolder] = message.useMessage();
  const [labTests, setLabTest] = useState([]);
  const [imagingTests, setImagingTests] = useState([]);
  const [functionalTests, setFunctionalTests] = useState([]);
  const [medicines, setMedicines] = useState([]);
  const [isModalPrescriptionOpen, setIsModalPrescriptionOpen] = useState(false);
  const [prescriptionForm] = Form.useForm();
  const [diagnosisForm] = Form.useForm();
  const patientInfo = JSON.parse(localStorage.getItem('patientEncounterInfo') || '{}');

  const functionalTestNames = {
    spirometry: 'Đo chức năng hô hấp',
    bloodgasanalysis: 'Phân tích khí máu',
    nerveconduction: 'Đo dẫn truyền thần kinh',
    eeg: 'Điện não đồ',
    emg: 'Điện cơ đồ',
    cardiactest: 'Xét nghiệm tim mạch',
    digestive: 'Xét nghiệm tiêu hóa',
  };

  // Lấy danh sách thuốc
  useEffect(() => {
    const fetchMedicines = async () => {
      try {
        const response = await fetch('http://localhost:8080/api/v1/medicine', {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
          },
        });
        if (!response.ok) {
          throw new Error('Failed to fetch medicines');
        }
        const data = await response.json();
        if (data.status === 200 && data.data) {
          setMedicines(data.data);
        }
      } catch (error) {
        console.error('Error fetching medicines:', error);
        notifyErrorWithCustomMessage('Lỗi khi lấy danh sách thuốc', messageApi);
      }
    };
    fetchMedicines();
  }, []);

  // Lấy dữ liệu xét nghiệm
  useEffect(() => {
    const handleDataMedicalTest = async () => {
      try {
        const response = await fetch(`http://localhost:8080/api/v1/doctors/patient/${patientInfo?.id}/medical-tests/all`, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
          },
        });
        if (!response.ok) {
          const errorText = await response.text();
          handleHttpStatusCode(response.status, '', 'Bệnh nhân không khám lâm sàng', messageApi);
          setLabTest([]);
          setImagingTests([]);
          setFunctionalTests([]);
          return;
        }
        const data = await response.json();
        if (data && data.data.length > 0) {
          const resultTestData = data.data.map((item) => ({
            id: item.id,
            notes: item.notes,
            evaluate: item.evaluate,
            encounterId: item.encounterId,
            createDate: formatDateTime(item.createDate),
            status: item.status,
            type: item.type,
            details: item.details,
          }));
          setLabTest(resultTestData.filter((item) => item.type === 'LaboratoryTests'));
          setImagingTests(resultTestData.filter((item) => item.type === 'ImagingTest'));
          setFunctionalTests(resultTestData.filter((item) => item.type !== 'LaboratoryTests' && item.type !== 'ImagingTest'));
        }
      } catch (error) {
        console.error('Error fetching medical tests:', error);
        notifyErrorWithCustomMessage('Lỗi khi lấy dữ liệu xét nghiệm', messageApi);
      }
    };
    handleDataMedicalTest();
  }, [patientInfo?.id]);

  // Hoàn thiện hàm handleSubmitPatient
  const handleSubmitPatient = async () => {
    try {
      const encounterId = localStorage.getItem('encounterId');
      const medicalRecordId = localStorage.getItem('medicalRecordPatientId');
      const diagnosisValues = await diagnosisForm.validateFields();
      const prescriptionValues = await prescriptionForm.validateFields();

      // Gửi yêu cầu tạo prescription trước
      const prescriptionPayload = {
        issueDate: new Date().toISOString(),
        status: 'PENDING',
        prescriptionItems: prescriptionValues.prescriptionItems.map((item) => ({
          dosage: item.dosage,
          quantity: item.quantity,
          unit: item.unit,
          medicineId: item.medicineId,
        })),
        encounterId: parseInt(encounterId),
      };
      console.log('Prescription Payload -----------------:', prescriptionPayload);

      const prescriptionResponse = await fetch('http://localhost:8080/api/v1/prescription', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(prescriptionPayload),
      });

      if (!prescriptionResponse.ok) {
        const errorText = await prescriptionResponse.text();
        handleHttpStatusCode(prescriptionResponse.status, '', 'Kê đơn thuốc thất bại', messageApi);
        return;
      }

      const prescriptionData = await prescriptionResponse.json();
      const prescriptionId = prescriptionData.data?.id; // Giả sử API trả về ID trong data.id

      // Gửi yêu cầu cập nhật encounter với prescriptionId
      const encounterPayload = {
        treatment: diagnosisValues.treatment,
        diagnosis: diagnosisValues.diagnosis,
        visitDate: dayjs().format('YYYY-MM-DD'),
        notes: diagnosisValues.notes,
        prescriptionId: prescriptionId ? [prescriptionId] : [],
        medicalRecordId: parseInt(medicalRecordId),
      };
      console.log('Encounter Payload -----------------:', encounterPayload);

      const encounterResponse = await fetch(`http://localhost:8080/api/v1/encounters/${encounterId}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(encounterPayload),
      });

      if (!encounterResponse.ok) {
        const errorText = await encounterResponse.text();
        handleHttpStatusCode(encounterResponse.status, '', 'Cập nhật thông tin bệnh nhân thất bại', messageApi);
        return;
      }

      notifySuccessWithCustomMessage('Cập nhật thông tin và kê đơn thành công', messageApi);
      setTimeout(() => {
        window.location.href = '/doctor/records';
      }, 2000);
    } catch (error) {
      console.error('Error in handleSubmitPatient:', error);
      notifyErrorWithCustomMessage('Cập nhật thông tin bệnh nhân hoặc kê đơn thất bại', messageApi);
    }
  };

  // Mở modal kê đơn thuốc
  const showPrescriptionModal = () => {
    setIsModalPrescriptionOpen(true);
  };

  // Đóng modal kê đơn thuốc
  const handlePrescriptionModalCancel = () => {
    setIsModalPrescriptionOpen(false);
    prescriptionForm.resetFields();
    diagnosisForm.resetFields();
  };

  return (
    <>
      <Card
        className="w-full rounded-xl overflow-hidden mt-6"
        variant={false}
        title={
          <div className="flex items-center">
            <MedicineBoxOutlined className="mr-2 text-blue-500" />
            <span>Thông tin Cận lâm sàng</span>
          </div>
        }
      >
        <div className="grid grid-cols-1 gap-6">
          {/* Xét nghiệm */}
          <Card
            className="shadow-sm hover:shadow-md transition-all duration-300"
            title={
              <div className="flex items-center">
                <FileTextOutlined className="mr-2 text-green-600" />
                <span>Xét nghiệm</span>
              </div>
            }
          >
            <Table
              dataSource={labTests}
              columns={[
                {
                  title: 'Tên xét nghiệm',
                  dataIndex: 'notes',
                  key: 'notes',
                },
                {
                  title: 'Loại',
                  dataIndex: 'notes',
                  key: 'notes',
                  render: (text) => <Tag color="green">{text}</Tag>,
                },
                {
                  title: 'Thời gian yêu cầu',
                  dataIndex: 'createDate',
                  key: 'createDate',
                },
                {
                  title: 'Trạng thái',
                  dataIndex: 'status',
                  key: 'status',
                  render: (status) => (
                    <Badge
                      status={status === 'COMPLETED' ? 'success' : status === 'PENDING' ? 'processing' : 'default'}
                      text={status === 'COMPLETED' ? 'Hoàn thành' : status === 'PENDING' ? 'Đang xử lý' : 'Chưa có'}
                    />
                  ),
                },
                {
                  title: 'Kết quả',
                  dataIndex: 'result',
                  key: 'result',
                  render: (result, record) => (
                    record.status === 'COMPLETED' ? (
                      <Button type="link" icon={<FileSearchOutlined />}>
                        Xem kết quả
                      </Button>
                    ) : (
                      <Text type="secondary">Chưa có</Text>
                    )
                  ),
                },
                {
                  title: 'Thao tác',
                  key: 'action',
                  render: (_, record) => (
                    <Space size="small">
                      <Tooltip title="Cập nhật">
                        <Button type="text" icon={<EditOutlined />} />
                      </Tooltip>
                      <Tooltip title="Xóa">
                        <Button type="text" danger icon={<DeleteOutlined />} />
                      </Tooltip>
                    </Space>
                  ),
                },
              ]}
              size="small"
              pagination={false}
            />
          </Card>

          {/* Chẩn đoán hình ảnh */}
          <Card
            className="shadow-sm hover:shadow-md transition-all duration-300"
            title={
              <div className="flex items-center">
                <FileImageOutlined className="mr-2 text-blue-600" />
                <span>Chẩn đoán hình ảnh</span>
              </div>
            }
          >
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
              {imagingTests.map((test) => (
                <Card
                  key={test.id}
                  size="small"
                  className="hover:shadow-md transition-all duration-300"
                  cover={
                    <div className="h-40 w-full bg-gray-100 flex items-center justify-center">
                      {test.status === 'COMPLETED' ? (
                        <img src={test.imageUrl || null} alt={test.name} className="object-cover h-full w-full" />
                      ) : (
                        <div className="flex flex-col items-center justify-center text-gray-400">
                          <FileImageOutlined style={{ fontSize: '3rem' }} />
                          <Text type="secondary">Chưa có hình ảnh</Text>
                        </div>
                      )}
                    </div>
                  }
                  actions={[
                    <Tooltip title="Xem chi tiết">
                      <EyeOutlined key="view" />
                    </Tooltip>,
                    <Tooltip title="Tải xuống">
                      <DownloadOutlined key="download" />
                    </Tooltip>,
                    <Tooltip title="Xóa">
                      <DeleteOutlined key="delete" />
                    </Tooltip>,
                  ]}
                >
                  <Card.Meta
                    title={<Text strong>{test.notes}</Text>}
                    description={
                      <div className="flex flex-col">
                        <Text type="secondary">{test.createDate}</Text>
                        <div className="mt-2 flex items-center">
                          <Badge
                            status={test.status === 'COMPLETED' ? 'success' : 'processing'}
                            text={test.status}
                          />
                        </div>
                      </div>
                    }
                  />
                </Card>
              ))}
            </div>
          </Card>

          {/* Thăm dò chức năng */}
          <Card
            className="shadow-sm hover:shadow-md transition-all duration-300"
            title={
              <div className="flex items-center">
                <LineChartOutlined className="mr-2 text-purple-600" />
                <span>Thăm dò chức năng</span>
              </div>
            }
          >
            <List
              dataSource={functionalTests}
              renderItem={(item) => (
                <List.Item
                  key={item.id}
                  actions={[
                    <Button key="view" type="link" icon={<FileSearchOutlined />}>
                      {item.status === 'COMPLETED' ? 'Xem kết quả' : 'Chi tiết'}
                    </Button>,
                    <Button key="edit" type="text" icon={<EditOutlined />} />,
                    <Button key="delete" type="text" danger icon={<DeleteOutlined />} />,
                  ]}
                >
                  <List.Item.Meta
                    avatar={
                      <Avatar
                        icon={<HeartFilled />}
                        style={{
                          backgroundColor:
                            item.type === 'Spirometry' ? '#ff4d4f' :
                            item.type === 'BloodGasAnalysis' ? '#13c2c2' :
                            item.type === 'NerveConduction' ? '#722ed1' :
                            item.type === 'EEG' ? '#1890ff' :
                            item.type === 'EMG' ? '#fadb14' :
                            item.type === 'CardiacTest' ? '#eb2f96' :
                            item.type === 'DigestiveTest' ? '#52c41a' : '#1890ff',
                        }}
                      />
                    }
                    title={<Text strong>{functionalTestNames[item.type]}</Text>}
                    description={
                      <div className="flex flex-col md:flex-row md:items-center text-xs">
                        <Tag color="blue">{item.type}</Tag>
                        <Text type="secondary" className="mx-6">
                          <CalendarOutlined />
                          <span className="ml-2">{item.createDate}</span>
                        </Text>
                        <Badge
                          className="ml-4"
                          status={item.status === 'COMPLETED' ? 'success' : 'processing'}
                          text={item.status}
                        />
                      </div>
                    }
                  />
                  <div>
                    <Text type="secondary">Bác sĩ chỉ định: {localStorage.getItem('userFullName')}</Text>
                  </div>
                </List.Item>
              )}
            />
          </Card>

          {/* Nút thêm yêu cầu mới */}
          <div className="flex justify-center mt-4 flex-row items-center w-full">
            <div className="flex flex-row items-center justify-around w-full">
              <Button type="primary" size="large" onClick={showPrescriptionModal}>
                Kê đơn thuốc
              </Button>
              {/* <Button type="primary" size="large" onClick={handleSubmitPatient}>
                Đã khám xong giai đoạn 1
              </Button> */}
            </div>
          </div>
        </div>
        {contextHolder}
      </Card>

      {/* Modal kê đơn thuốc */}
      <Modal
        title="Kê đơn thuốc"
        open={isModalPrescriptionOpen}
        onOk={() => {
          Promise.all([diagnosisForm.validateFields(), prescriptionForm.validateFields()])
            .then(() => handleSubmitPatient())
            .catch(() => message.error('Vui lòng điền đầy đủ thông tin'));
        }}
        onCancel={handlePrescriptionModalCancel}
        okText="Xác nhận"
        cancelText="Hủy"
        width={800}
      >
        <Form form={diagnosisForm} layout="vertical">
          <Form.Item
            name="diagnosis"
            label="Chẩn đoán"
            rules={[{ required: true, message: 'Vui lòng nhập chẩn đoán' }]}
          >
            <Input.TextArea rows={3} placeholder="Nhập chẩn đoán" />
          </Form.Item>
          <Form.Item
            name="treatment"
            label="Phương pháp điều trị"
            rules={[{ required: true, message: 'Vui lòng nhập phương pháp điều trị' }]}
          >
            <Input.TextArea rows={3} placeholder="Nhập phương pháp điều trị" />
          </Form.Item>
          <Form.Item
            name="notes"
            label="Ghi chú"
            rules={[{ required: true, message: 'Vui lòng nhập ghi chú' }]}
          >
            <Input.TextArea rows={3} placeholder="Nhập ghi chú" />
          </Form.Item>
        </Form>
        <Form form={prescriptionForm} layout="vertical">
          <Form.List name="prescriptionItems" initialValue={[{}]}>
            {(fields, { add, remove }) => (
              <>
                {fields.map(({ key, name, ...restField }) => (
                  <Card key={key} style={{ marginBottom: 16 }} title={`Thuốc ${name + 1}`}>
                    <Space style={{ width: '100%' }} direction="vertical">
                      <Form.Item
                        {...restField}
                        name={[name, 'medicineId']}
                        label="Tên thuốc"
                        rules={[{ required: true, message: 'Vui lòng chọn thuốc' }]}
                      >
                        <Select placeholder="Chọn thuốc">
                          {medicines.map((medicine) => (
                            <Option key={medicine.id} value={medicine.id}>
                              {medicine.name} ({medicine.strength})
                            </Option>
                          ))}
                        </Select>
                      </Form.Item>
                      <Form.Item
                        {...restField}
                        name={[name, 'dosage']}
                        label="Liều lượng"
                        rules={[{ required: true, message: 'Vui lòng nhập liều lượng' }]}
                      >
                        <Input placeholder="Nhập liều lượng (VD: 1 viên/lần)" />
                      </Form.Item>
                      <Form.Item
                        {...restField}
                        name={[name, 'quantity']}
                        label="Số lượng"
                        rules={[{ required: true, message: 'Vui lòng nhập số lượng' }]}
                      >
                        <Input type="number" placeholder="Nhập số lượng" />
                      </Form.Item>
                      <Form.Item
                        {...restField}
                        name={[name, 'unit']}
                        label="Đơn vị"
                        rules={[{ required: true, message: 'Vui lòng nhập đơn vị' }]}
                      >
                        <Input placeholder="Nhập đơn vị (VD: viên, ống)" />
                      </Form.Item>
                      {fields.length > 1 && (
                        <Button type="danger" onClick={() => remove(name)}>
                          Xóa thuốc
                        </Button>
                      )}
                    </Space>
                  </Card>
                ))}
                <Button type="dashed" onClick={() => add()} block>
                  Thêm thuốc
                </Button>
              </>
            )}
          </Form.List>
        </Form>
      </Modal>
    </>
  );
};

export default DiagnoseEncounter;
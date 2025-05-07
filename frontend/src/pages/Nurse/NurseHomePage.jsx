import React, { useState, useEffect } from 'react';
import { Layout, Table, Tabs, Button, Modal, Form, Input, Select, InputNumber, Typography, Image } from 'antd';
import { SearchOutlined, EditOutlined, SaveOutlined } from '@ant-design/icons';
import 'antd/dist/reset.css';
import {formatDateTime, formatDate} from '../../utils/formatDate';
const { Content } = Layout;
const { TabPane } = Tabs;
const { Option } = Select;
const { Title } = Typography;

const NurseHomePage = () => {

  //lab test
  const [labPatients, setLabPatients] = useState([]);
  const [labTests, setLabTests] = useState([]);
  // imaging test
  const [imagingPatients, setImagingPatients] = useState([]);
  const [imagingTest, setImagingTest] = useState([]);

  const [searchText, setSearchText] = useState('');
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [activeTest, setActiveTest] = useState('');
  const [form] = Form.useForm();
  const [storageImg, setStorageImg] = useState();
  const [previewImage, setPreviewImage] = useState('');
  const [selectedPatient, setSelectedPatient] = useState(null); // Store selected patient for modal
  const patientColumns = (testType) => [
    { title: 'ID', dataIndex: 'id', key: 'id' },
    { title: 'Họ và tên', dataIndex: 'name', key: 'name' },
    { title: 'Tuổi', dataIndex: 'age', key: 'age' },
    { title: 'Giới tính', dataIndex: 'gender', key: 'gender' },
    { title: 'Số điện thoại', dataIndex: 'phone', key: 'phone' },
    {
      title: 'Hành động',
      key: 'action',
      render: (_, record) => (
        <Button
          type="primary"
          onClick={() => {
            setActiveTest(testType);
            setSelectedPatient(record); // Store patient info for modal display
            // Find the test data for the patient based on encounterId or id
            let test;
            if (testType === 'labTest') {
              test = labTests.find(t => t.encounterId === record.id) || {};
              form.setFieldsValue({
                ...test,
                status: 'COMPLETED',
              });
            } else if (testType === 'imagingTest') {
              test = imagingTest.find(t => t.encounterId === record.id) || {};
              form.setFieldsValue({
                ...test,
                pdfResult: storageImg|| '',
                status: 'COMPLETED',
              });
            }
            
            if (testType === 'imagingTest' && test.imageUrl) {
              setPreviewImage(test.imageUrl);
            }
            setIsModalVisible(true);
          }}
          icon={<EditOutlined />}
        >
          Cập nhật
        </Button>
      ),
    },
  ];
  // Fetch patients for lab tests
  const fetchLabTest = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/v1/laboratory-tests/by-date?date=2025-05-05&status=PENDING', {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        }
      });
      console.log('Response in nurse lab tests 1:', response);

      if(!response.ok) {
        const errorText = await response.text();
        console.error('Error fetching lab patients:', errorText);
        return;
      }

      const data = await response.json();
      console.log('Data in nurse lab tests 1:', data);
      // Assuming API returns array of { id, patientName, age, gender, room }
      if(data && data.data.length > 0) {
        const labTestsData = data.data.map(item => ({
          id: item.id,
          encounterId: item.encounterId,
          rbc: item.rbc || null,
          hct: item.hct || null,
          hgb: item.hgb || null,
          mcv: item.mcv || null,
          mch: item.mch || null,
          wbc: item.wbc || null,
          gra: item.gra || null,
          lym: item.lym || null,
          momo: item.momo || null,
          evaluate: item.evaluate || '',
          notes: item.notes || '',
          status: item.status || 'PENDING',
          createDate: formatDateTime(item.createDate),
        }));
        setLabTests(labTestsData);
      }
    } catch (error) {
      console.error('Error fetching lab patients:', error);
    }
  };

  // Fetch patients for imaging tests
  const fetchImagingTest = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/v1/imaging-tests/by-date?date=2025-05-05&status=PENDING', {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        }
      });

      if(!response.ok) {
        const errorText = await response.text();
        console.error('Error fetching imaging patients:', errorText);
        return;
      }
      const data = await response.json();
      // Assuming API returns array of { id, patientName, age, gender, room }
      if(data && data.data.length > 0) {
        const imagingTestData = data.data.map(item => ({
            id: item.id,
            encounterId: item.encounterId,
            pdfResult: item.pdfResult || '',
            evaluate: item.evaluate || '',
            notes: item.notes || '',
            status: item.status || null,
            createDate: formatDateTime(item.createDate),
          })
        );
        setImagingTest(imagingTestData);
      }
    } catch (error) {
      console.error('Error fetching imaging patients:', error);
    }
  };

  // Fetch data when component mounts or tab changes
  useEffect(() => {
    fetchLabTest();
    fetchImagingTest();
  }, []);
  // Fetch detailed lab patients when labTest changes
  useEffect(() => {
    if (labTests.length > 0) {
      handlePatientLabTest();
    }
    if(imagingTest.length > 0) {
      handlePatientImagingTest();
    }
    console.log('Filtered lab patients:', filteredLabPatients);
    console.log('Filtered imaging patients:', filteredImagingPatients);
  }, [labTests, imagingTest]);
  // Filter patients based on search text
  const filteredLabPatients = labPatients.filter((patient) =>
    patient.name && patient.name.toLowerCase().includes(searchText.toLowerCase())
  );
  const filteredImagingPatients = imagingPatients.filter((patient) =>
    patient.name && patient.name.toLowerCase().includes(searchText.toLowerCase())
  );
  const handleSaveTest = async (values) => {
    console.log('Form values:', values);
    try {
      const testType = activeTest.replace('Test', '');
      const endpoint =
        testType === 'lab'
          ? 'http://localhost:8080/api/v1/laboratory-tests'
          : 'http://localhost:8080/api/v1/imaging-tests';
      const response = await fetch(`${endpoint}/${values.id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(values),
      });
  
      if (!response.ok) {
        console.error('Error saving test:', await response.text());
        return;
      }
  
      // Update local state
      if (testType === 'lab') {
        // setLabTests(prev =>
        //   prev.map(test => (test.id === values.id ? { ...test, ...values } : test))
        // );
        await fetchLabTest(); // Re-fetch lab tests to get updated data
      } else if (testType === 'imaging') {
        alert('Update imaging test');
        // setImagingTest(prev =>
        //   prev.map(test => (test.id === values.id ? { ...test, ...values } : test))
        // );
        await fetchImagingTest(); // Re-fetch imaging tests to get updated data
      }
  
      setIsModalVisible(false);
      form.resetFields();
      setPreviewImage('');
      setSelectedPatient(null);
    } catch (error) {
      console.error('Error saving test:', error);
    }
  };

  const handleFileUpload = async (event) => {
    const file = event.target.files[0];
    if (!file) return;

    const data = new FormData();
    data.append('file', file);
    data.append('upload_preset', 'hcmute-care');
    data.append('cloud_name', 'dujzjcmai');

    try {
      const res = await fetch('https://api.cloudinary.com/v1_1/dujzjcmai/image/upload', {
        method: 'POST',
        body: data,
      });

      if (!res.ok) throw new Error(`HTTP error! status: ${res.status}`);

      const uploadImageURL = await res.json();
      // const newId = storageImg.length > 0 ? Math.max(...storageImg.map(img => img.id)) + 1 : 1;
      if(!uploadImageURL || !uploadImageURL.url) {
        console.error('Invalid image URL:', uploadImageURL);
        return;
      }
      const imageUploadSuccess = uploadImageURL.url;
      setStorageImg(imageUploadSuccess);
      setPreviewImage(uploadImageURL.url);
      // Sync with form
      form.setFieldsValue({ pdfResult: imageUploadSuccess });
      console.log('storageImg before modal:', storageImg);
    } catch (error) {
      console.error('Error uploading file:', error);
    }
  };
  const handlePatientLabTest = async () => {
    const encounterIds = [...new Set(labTests.map(test => test.encounterId))];
    console.log('Encounter IDs:', encounterIds);
    if (!encounterIds || encounterIds.length === 0) {
      console.warn('No encounter IDs found in labTest');
      return;
    }
    try {
      const idsString = encounterIds.join(',');
      const response = await fetch(`http://localhost:8080/api/v1/encounters/all/detail-patient?ids=${idsString}`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        }
      });
      console.log('Response in nurse lab tests:', response);
      if(!response.ok) {
        const errorText = await response.text();
        console.error('Error fetching lab patients:', errorText);
        return;
      }

      const data = await response.json();
      console.log('Data in nurse lab tests:', data);
      // Assuming API returns array of { id, patient
      if (data && data.data && data.data.length > 0) {
        const patients = data.data.map(item => ({
          id: item.id,
          name: item.medicalRecord?.patient?.name || 'Unknown',
          dob: item.medicalRecord?.patient?.dob || null,
          age: item.medicalRecord?.patient?.dob
            ? Math.floor((new Date() - new Date(item.medicalRecord.patient.dob)) / (1000 * 60 * 60 * 24 * 365))
            : null,
          gender: item.medicalRecord?.patient?.gender === "MALE" ? "Nam" : "Nữ"|| 'Unknown',
          phone: item.medicalRecord?.patient?.phone || 'Unknown',
        }));
        setLabPatients(patients);
      }
      else {
        setLabPatients([]);
        console.warn('No lab patients found in response data');
      }
    }
    catch(e) {
      console.error('Error fetching lab patients:', e);
    }
  }
  const handlePatientImagingTest = async () => {
    const encounterIds = [...new Set(imagingTest.map(test => test.encounterId))];
    console.log('Encounter IDs:', encounterIds);
    if (!encounterIds || encounterIds.length === 0) {
      console.warn('No encounter IDs found in imagingTest');
      return;
    }
    try {
      const idsString = encounterIds.join(',');
      const response = await fetch(`http://localhost:8080/api/v1/encounters/all/detail-patient?ids=${idsString}`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        }
      });
      console.log('Response in nurse imaging tests:', response);
      if(!response.ok) {
        const errorText = await response.text();
        console.error('Error fetching lab patients:', errorText);
        return;
      }

      const data = await response.json();
      console.log('Data in nurse imaging tests:', data);
      // Assuming API returns array of { id, patient
      if (data && data.data && data.data.length > 0) {
        const patients = data.data.map(item => ({
          id: item.id,
          name: item.medicalRecord?.patient?.name || 'Unknown',
          dob: item.medicalRecord?.patient?.dob || null,
          age: item.medicalRecord?.patient?.dob
            ? Math.floor((new Date() - new Date(item.medicalRecord.patient.dob)) / (1000 * 60 * 60 * 24 * 365))
            : null,
          gender: item.medicalRecord?.patient?.gender === "MALE" ? "Nam" : "Nữ"|| 'Unknown',
          phone: item.medicalRecord?.patient?.phone || 'Unknown',
        }));
        setImagingPatients(patients);
      }
      else {
        setImagingPatients([]);
        console.warn('No lab patients found in response data');
      }
    }
    catch(e) {
      console.error('Error fetching lab patients:', e);
    }
  }
  const renderTestForm = () => {
    switch (activeTest) {
      case 'labTest':
        return (
          <>
            {/* Display patient info outside the form */}
            {selectedPatient && (
              <div style={{ marginBottom: 16 }}>
                <span>Bệnh nhân: </span>{selectedPatient.name}<br />
                <span>Ngày sinh: </span>{formatDate(selectedPatient.dob) || 'Unknown'}<br />
                <span>Giới tính: </span>{selectedPatient.gender || 'Unknown'}
              </div>
            )}
            <Form form={form} layout="vertical" onFinish={handleSaveTest}>
              <Form.Item name="id" hidden><Input /></Form.Item>
              <Form.Item name="encounterId" hidden><Input /></Form.Item>
              <div className='grid grid-flow-row grid-cols-3 gap-4'>
                <Form.Item name="rbc" label="RBC"><InputNumber step={0.01} className="w-full" /></Form.Item>
                <Form.Item name="hct" label="HCT"><InputNumber step={0.01} className="w-full" /></Form.Item>
                <Form.Item name="hgb" label="HGB"><InputNumber step={0.01} className="w-full" /></Form.Item>
                <Form.Item name="mcv" label="MCV"><InputNumber step={0.01} className="w-full" /></Form.Item>
                <Form.Item name="mch" label="MCH"><InputNumber step={0.01} className="w-full" /></Form.Item>
                <Form.Item name="olt" label="OLT"><InputNumber step={0.01} className="w-full" /></Form.Item>
                <Form.Item name="wbc" label="WBC"><InputNumber step={0.01} className="w-full" /></Form.Item>
                <Form.Item name="gra" label="GRA"><InputNumber step={0.01} className="w-full" /></Form.Item>
                <Form.Item name="lym" label="LYM"><InputNumber step={0.01} className="w-full" /></Form.Item>
                <Form.Item name="momo" label="MOMO"><InputNumber step={0.01} className="w-full" /></Form.Item>
              </div>
              <Form.Item name="evaluate" label="Đánh giá"><Input.TextArea rows={3} /></Form.Item>
              <Form.Item name="notes" label="Ghi chú"><Input.TextArea rows={3} /></Form.Item>
              <Form.Item name="status" label="Trạng thái">
                <Select>
                  <Option value="PENDING">PENDING</Option>
                  <Option value="COMPLETED">COMPLETED</Option>
                </Select>
              </Form.Item>
            </Form>
          </>
        );
      case 'imagingTest':
        return (
          <>
            {selectedPatient && (
              <div style={{ marginBottom: 16 }}>
                <Form.Item>Bệnh nhân: </Form.Item>{selectedPatient.name}<br />
                <Form.Item>Ngày sinh: </Form.Item>{formatDate(selectedPatient.dob) || 'Unknown'}<br />
                <Form.Item>Giới tính: </Form.Item>{selectedPatient.gender || 'Unknown'}
              </div>
            )}
            <Form form={form} layout="vertical" onFinish={handleSaveTest}>
              <Form.Item name="id" hidden><Input /></Form.Item>
              <Form.Item name="encounterId" hidden><Input /></Form.Item>
              <Form.Item name="pdfResult" hidden><Input /></Form.Item>
              <Form.Item label="Tải ảnh lên">
                <Input type="file" accept="image/*" onChange={handleFileUpload} />
              </Form.Item>
              {previewImage && (
                <Form.Item label="Xem trước ảnh">
                  <Image src={previewImage} width={200} />
                </Form.Item>
              )}
              <Form.Item name="evaluate" label="Đánh giá"><Input.TextArea rows={3} /></Form.Item>
              <Form.Item name="notes" label="Ghi chú"><Input.TextArea rows={3} /></Form.Item>
              <Form.Item name="createDate" label="Ngày tạo"><Input.TextArea rows={3} /></Form.Item>
              <Form.Item name="status" label="Trạng thái">
                <Select>
                  <Option value="PENDING">PENDING</Option>
                  <Option value="COMPLETED">COMPLETED</Option>
                </Select>
              </Form.Item>
            </Form>
          </>
        );
      case 'cardiacTest':
        return (
          <Form form={form} layout="vertical" onFinish={handleSaveTest}>
            <Form.Item name="id" hidden><Input /></Form.Item>
            <Form.Item name="ecg" label="ECG"><Input /></Form.Item>
            <Form.Item name="notes" label="Ghi chú"><Input.TextArea rows={3} /></Form.Item>
            <Form.Item name="status" label="Trạng thái">
              <Select>
                <Option value="PENDING">PENDING</Option>
                <Option value="COMPLETED">COMPLETED</Option>
              </Select>
            </Form.Item>
          </Form>
        );
      case 'digestiveTest':
        return (
          <Form form={form} layout="vertical" onFinish={handleSaveTest}>
            <Form.Item name="id" hidden><Input /></Form.Item>
            <Form.Item name="endoscopy" label="Nội soi"><Input /></Form.Item>
            <Form.Item name="notes" label="Ghi chú"><Input.TextArea rows={3} /></Form.Item>
            <Form.Item name="status" label="Trạng thái">
              <Select>
                <Option value="PENDING">PENDING</Option>
                <Option value="COMPLETED">COMPLETED</Option>
              </Select>
            </Form.Item>
          </Form>
        );
      case 'neuroTest':
        return (
          <Form form={form} layout="vertical" onFinish={handleSaveTest}>
            <Form.Item name="id" hidden><Input /></Form.Item>
            <Form.Item name="eeg" label="EEG"><Input /></Form.Item>
            <Form.Item name="emg" label="EMG"><Input /></Form.Item>
            <Form.Item name="notes" label="Ghi chú"><Input.TextArea rows={3} /></Form.Item>
            <Form.Item name="status" label="Trạng thái">
              <Select>
                <Option value="PENDING">PENDING</Option>
                <Option value="COMPLETED">COMPLETED</Option>
              </Select>
            </Form.Item>
          </Form>
        );
      case 'respiratoryTest':
        return (
          <Form form={form} layout="vertical" onFinish={handleSaveTest}>
            <Form.Item name="id" hidden><Input /></Form.Item>
            <Form.Item name="fev1" label="FEV1"><InputNumber step={0.01} className="w-full" /></Form.Item>
            <Form.Item name="fvc" label="FVC"><InputNumber step={0.01} className="w-full" /></Form.Item>
            <Form.Item name="bloodgas" label="Blood Gas"><Input /></Form.Item>
            <Form.Item name="nerve" label="Nerve Test"><Input /></Form.Item>
            <Form.Item name="notes" label="Ghi chú"><Input.TextArea rows={3} /></Form.Item>
            <Form.Item name="status" label="Trạng thái">
              <Select>
                <Option value="PENDING">PENDING</Option>
                <Option value="COMPLETED">COMPLETED</Option>
              </Select>
            </Form.Item>
          </Form>
        );
      default:
        return <div>Chọn loại xét nghiệm</div>;
    }
  };

  return (
    <div className="h-full w-full bg-gray-100">
      <div className="ml-2 p-6 bg-white rounded-lg shadow-lg">
        <Title level={2} className="mb-6 text-blue-600">Kết quả cận lâm sàng</Title>
        <Input
          placeholder="Tìm kiếm bệnh nhân..."
          prefix={<SearchOutlined />}
          className="max-w-md mb-4"
          value={searchText}
          onChange={(e) => setSearchText(e.target.value)}
        />
        <Tabs defaultActiveKey="1">
          <TabPane tab="Xét nghiệm máu" key="1" onClick={handlePatientLabTest}>
            <Table columns={patientColumns('labTest')} dataSource={filteredLabPatients} rowKey="id" pagination={{ pageSize: 5 }} />
          </TabPane>
          <TabPane tab="Chẩn đoán hình ảnh" key="2" onClick={handlePatientImagingTest}>
            <Table columns={patientColumns('imagingTest')} dataSource={filteredImagingPatients} rowKey="id" pagination={{ pageSize: 5 }} />
          </TabPane>
          <TabPane tab="Xét nghiệm chức năng" key="3">
            <Tabs defaultActiveKey="cardiac">
              <TabPane tab="Tim mạch" key="cardiac">
                <Table columns={patientColumns('cardiacTest')} dataSource={filteredLabPatients} rowKey="id" pagination={{ pageSize: 5 }} />
              </TabPane>
              <TabPane tab="Tiêu hóa" key="digestive">
                <Table columns={patientColumns('digestiveTest')} dataSource={filteredLabPatients} rowKey="id" pagination={{ pageSize: 5 }} />
              </TabPane>
              <TabPane tab="Thần kinh" key="neuro">
                <Table columns={patientColumns('neuroTest')} dataSource={filteredLabPatients} rowKey="id" pagination={{ pageSize: 5 }} />
              </TabPane>
              <TabPane tab="Hô hấp" key="respiratory">
                <Table columns={patientColumns('respiratoryTest')} dataSource={filteredLabPatients} rowKey="id" pagination={{ pageSize: 5 }} />
              </TabPane>
            </Tabs>
          </TabPane>
        </Tabs>
        <Modal
          title={`Cập nhật ${activeTest.replace(/([A-Z])/g, ' $1').replace(/^./, (str) => str.toUpperCase())}`}
          open={isModalVisible}
          onCancel={() => {
            setIsModalVisible(false);
            form.resetFields();
            setPreviewImage('');
            setSelectedPatient(null);
          }}
          footer={[
            <Button key="cancel" onClick={() => {
              setIsModalVisible(false);
              form.resetFields();
              setPreviewImage('');
              setSelectedPatient(null);
            }}>Hủy</Button>,
            <Button key="submit" type="primary" icon={<SaveOutlined />} onClick={() => form.submit()}>
              Lưu
            </Button>,
          ]}
          width={600}
        >
          {renderTestForm()}
        </Modal>
      </div>
    </div>
  );
};

export default NurseHomePage;
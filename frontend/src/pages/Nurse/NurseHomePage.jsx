import React, { useState, useEffect } from "react";
import {
  Layout,
  Table,
  Tabs,
  Button,
  Modal,
  Form,
  Input,
  Select,
  InputNumber,
  Typography,
  Image,
  Checkbox,
} from "antd";
import { SearchOutlined, EditOutlined, SaveOutlined } from "@ant-design/icons";
import "antd/dist/reset.css";
import { formatDateTime, formatDate } from "../../utils/formatDate";
import dayjs from "dayjs";
const { Content } = Layout;
const { TabPane } = Tabs;
const { Option } = Select;
const { Title } = Typography;


const NurseHomePage = () => {
  const apiUrl = import.meta.env.VITE_API_BASE_URL;

  // API endpoints for different test types
  const API_ENDPOINTS = {
    lab: `${apiUrl}v1/laboratory-tests`,
    imaging: `${apiUrl}v1/imaging-tests`,
    cardiac: `${apiUrl}v1/cardiac-tests`,
    digestive: `${apiUrl}v1/digestive-tests`,
    eeg: `'${apiUrl}v1/eeg'`,
    emg: `${apiUrl}v1/emg`,
    bloodgas: `${apiUrl}v1/blood-gas-analysis`,
    nerve: `${apiUrl}v1/nerve-conduction`,
    spirometry: `${apiUrl}v1/spirometry`,
  };

  // Test type configurations
  const TEST_TYPES = {
    labTest: {
      label: "Xét nghiệm máu",
      fetchKey: "lab",
      patientKey: "labPatients",
      testKey: "labTest",
    },
    imagingTest: {
      label: "Chẩn đoán hình ảnh",
      fetchKey: "imaging",
      patientKey: "imagingPatients",
      testKey: "imagingTest",
    },
    cardiacTest: {
      label: "Tim mạch",
      fetchKey: "cardiac",
      patientKey: "cardiacPatients",
      testKey: "cardiacTest",
    },
    digestiveTest: {
      label: "Tiêu hóa",
      fetchKey: "digestive",
      patientKey: "digestivePatients",
      testKey: "digestiveTest",
    },
    eegTest: {
      label: "Điện não đồ",
      fetchKey: "eeg",
      patientKey: "eegPatients",
      testKey: "eegTest",
    },
    emgTest: {
      label: "Điện cơ",
      fetchKey: "emg",
      patientKey: "emgPatients",
      testKey: "emgTest",
    },
    bloodgasTest: {
      label: "Khí máu",
      fetchKey: "bloodgas",
      patientKey: "bloodgasPatients",
      testKey: "bloodgasTest",
    },
    nerveTest: {
      label: "Dẫn truyền thần kinh",
      fetchKey: "nerve",
      patientKey: "nervePatients",
      testKey: "nerveTest",
    },
    spirometryTest: {
      label: "Phổi",
      fetchKey: "spirometry",
      patientKey: "spirometryPatients",
      testKey: "spirometryTest",
    },
  };
  // State management for patients and tests
  const [labPatients, setLabPatients] = useState([]);
  const [imagingPatients, setImagingPatients] = useState([]);
  const [cardiacPatients, setCardiacPatients] = useState([]);
  const [digestivePatients, setDigestivePatients] = useState([]);
  const [eegPatients, setEegPatients] = useState([]);
  const [emgPatients, setEmgPatients] = useState([]);
  const [bloodgasPatients, setBloodgasPatients] = useState([]);
  const [nervePatients, setNervePatients] = useState([]);
  const [spirometryPatients, setSpirometryPatients] = useState([]);
  const [labTests, setLabTests] = useState([]);
  const [imagingTests, setImagingTests] = useState([]);
  const [cardiacTests, setCardiacTests] = useState([]);
  const [digestiveTests, setDigestiveTests] = useState([]);
  const [eegTests, setEegTests] = useState([]);
  const [emgTests, setEmgTests] = useState([]);
  const [bloodgasTests, setBloodgasTests] = useState([]);
  const [nerveTests, setNerveTests] = useState([]);
  const [spirometryTests, setSpirometryTests] = useState([]);
  const [searchText, setSearchText] = useState("");
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [activeTest, setActiveTest] = useState("");
  const [previewImage, setPreviewImage] = useState("");
  const [selectedPatient, setSelectedPatient] = useState(null);
  const [storageImg, setStorageImg] = useState("");
  const [form] = Form.useForm();
  // Map test types to their setters for easier handling
  const testSetters = {
    lab: setLabTests,
    imaging: setImagingTests,
    cardiac: setCardiacTests,
    digestive: setDigestiveTests,
    eeg: setEegTests,
    emg: setEmgTests,
    bloodgas: setBloodgasTests,
    nerve: setNerveTests,
    spirometry: setSpirometryTests,
  };

  const patientSetters = {
    lab: setLabPatients,
    imaging: setImagingPatients,
    cardiac: setCardiacPatients,
    digestive: setDigestivePatients,
    eeg: setEegPatients,
    emg: setEmgPatients,
    bloodgas: setBloodgasPatients,
    nerve: setNervePatients,
    spirometry: setSpirometryPatients,
  };
  // Map test types to their test data
  const testDataMap = {
    labTest: labTests,
    imagingTest: imagingTests,
    cardiacTest: cardiacTests,
    digestiveTest: digestiveTests,
    eegTest: eegTests,
    emgTest: emgTests,
    bloodgasTest: bloodgasTests,
    nerveTest: nerveTests,
    spirometryTest: spirometryTests,
  };

  // Map test types to their patient data
  const patientDataMap = {
    labTest: labPatients,
    imagingTest: imagingPatients,
    cardiacTest: cardiacPatients,
    digestiveTest: digestivePatients,
    eegTest: eegPatients,
    emgTest: emgPatients,
    bloodgasTest: bloodgasPatients,
    nerveTest: nervePatients,
    spirometryTest: spirometryPatients,
  };
  // Patient table columns configuration
  const patientColumns = (testType) => [
    { title: "ID", dataIndex: "id", key: "id" },
    { title: "Họ và tên", dataIndex: "name", key: "name" },
    { title: "Tuổi", dataIndex: "age", key: "age" },
    { title: "Giới tính", dataIndex: "gender", key: "gender" },
    { title: "Số điện thoại", dataIndex: "phone", key: "phone" },
    {
      title: "Hành động",
      key: "action",
      render: (_, record) => (
        <Button
          type="primary"
          icon={<EditOutlined />}
          onClick={() => {
            setActiveTest(testType);
            setSelectedPatient(record);
            const test =
              testDataMap[testType].find((t) => t.encounterId === record.id) ||
              {};
            form.setFieldsValue({
              ...test,
              status: "COMPLETED",
              pdfResult:
                testType === "imagingTest"
                  ? storageImg || test.pdfResult
                  : test.pdfResult,
              createDate: test.createDate
                ? dayjs(test.createDate).format("YYYY-MM-DD HH:mm:ss")
                : undefined,
            });
            if (testType === "imagingTest" && test.pdfResult)
              setPreviewImage(test.pdfResult);
            setIsModalVisible(true);
          }}
        >
          Cập nhật
        </Button>
      ),
    },
  ];
  // Generalized fetch function for test data
  const fetchTestData = async (
    testType,
    setTests,
    date = dayjs().format("YYYY-MM-DD")
  ) => {
    console.log(
      "Fetching test data for:",
      testType,
      "on date ---------------:",
      date
    );
    try {
      const response = await fetch(
        `${API_ENDPOINTS[testType]}/by-date?date=${date}&status=PENDING`,
        {
          method: "GET",
          headers: { "Content-Type": "application/json" },
        }
      );

      if (!response.ok) {
        console.error(
          `Error fetching ${testType} tests:`,
          await response.text()
        );
        return;
      }

      const { data } = await response.json();
      if (data?.length) {
        const formattedData = data.map((item) => ({
          id: item.id,
          encounterId: item.encounterId,
          ...item,
          status: item.status || "PENDING",
          createDate: formatDateTime(item.createDate),
        }));
        setTests(formattedData);
      }
    } catch (error) {
      console.error(`Error fetching ${testType} tests:`, error);
    }
  };

  // Fetch patient details based on encounter IDs
  const fetchPatientDetails = async (encounterIds, setPatients) => {
    if (!encounterIds?.length) {
      console.warn("No encounter IDs provided");
      return;
    }

    try {
      const idsString = encounterIds.join(",");
      const response = await fetch(
        `${apiUrl}v1/encounters/all/detail-patient?ids=${idsString}`,
        {
          method: "GET",
          headers: { "Content-Type": "application/json" },
        }
      );

      if (!response.ok) {
        console.error("Error fetching patients:", await response.text());
        return;
      }

      const { data } = await response.json();
      if (data?.length) {
        const patients = data.map((item) => ({
          id: item.id,
          name: item.medicalRecord?.patient?.name || "Unknown",
          dob: item.medicalRecord?.patient?.dob || null,
          age: item.medicalRecord?.patient?.dob
            ? Math.floor(
                (new Date() - new Date(item.medicalRecord.patient.dob)) /
                  (1000 * 60 * 60 * 24 * 365)
              )
            : null,
          gender: item.medicalRecord?.patient?.gender === "MALE" ? "Nam" : "Nữ",
          phone: item.medicalRecord?.patient?.phone || "Unknown",
        }));
        setPatients(patients);
      } else {
        setPatients([]);
      }
    } catch (error) {
      console.error("Error fetching patients:", error);
    }
  };

  // Save test results to the server
  const handleSaveTest = async (values) => {
    console.log("Form values -------------------:", values);
    try {
      const testType = activeTest.replace("Test", "");
      const response = await fetch(`${API_ENDPOINTS[testType]}/${values.id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(values),
      });
      console.log("Response handleSaveTest --------------:", response);
      if (!response.ok) {
        console.error("Error saving test:", await response.text());
        return;
      }
      // Refresh test data after saving
      await fetchTestData(testType, testSetters[testType]);
      setIsModalVisible(false);
      form.resetFields();
      setPreviewImage("");
      setSelectedPatient(null);
    } catch (error) {
      console.error("Error saving test:", error);
    }
  };
  // Filter patients based on search text
  const filterPatients = (patients) =>
    patients.filter((patient) =>
      patient.name?.toLowerCase().includes(searchText.toLowerCase())
    );
  // Fetch data when component mounts or tab changes
  // Fetch initial data on component mount
  useEffect(() => {
    Object.keys(API_ENDPOINTS).forEach((testType) => {
      fetchTestData(testType, testSetters[testType], "2025-05-14");
    });
  }, []);
  // Fetch detailed lab patients when labTest changes
  // Fetch patient details when test data changes
  useEffect(() => {
    const testPatientMap = [
      { tests: labTests, setter: setLabPatients },
      { tests: imagingTests, setter: setImagingPatients },
      { tests: cardiacTests, setter: setCardiacPatients },
      { tests: digestiveTests, setter: setDigestivePatients },
      { tests: eegTests, setter: setEegPatients },
      { tests: emgTests, setter: setEmgPatients },
      { tests: bloodgasTests, setter: setBloodgasPatients },
      { tests: nerveTests, setter: setNervePatients },
      { tests: spirometryTests, setter: setSpirometryPatients },
    ];

    testPatientMap.forEach(({ tests, setter }) => {
      if (tests.length) {
        fetchPatientDetails(
          [...new Set(tests.map((test) => test.encounterId))],
          setter
        );
      }
    });
  }, [
    labTests,
    imagingTests,
    cardiacTests,
    digestiveTests,
    eegTests,
    emgTests,
    bloodgasTests,
    nerveTests,
    spirometryTests,
  ]);

  const handleFileUpload = async (event) => {
    const file = event.target.files[0];
    if (!file) return;

    const data = new FormData();
    data.append("file", file);
    data.append("upload_preset", "hcmute-care");
    data.append("cloud_name", "dujzjcmai");

    try {
      const res = await fetch(
        "https://api.cloudinary.com/v1_1/dujzjcmai/image/upload",
        {
          method: "POST",
          body: data,
        }
      );

      if (!res.ok) throw new Error(`HTTP error! status: ${res.status}`);

      const uploadImageURL = await res.json();
      // const newId = storageImg.length > 0 ? Math.max(...storageImg.map(img => img.id)) + 1 : 1;
      if (!uploadImageURL || !uploadImageURL.url) {
        console.error("Invalid image URL:", uploadImageURL);
        return;
      }
      const imageUploadSuccess = uploadImageURL.url;
      setStorageImg(imageUploadSuccess);
      setPreviewImage(uploadImageURL.url);
      // Sync with form
      form.setFieldsValue({ pdfResult: imageUploadSuccess });
      console.log("storageImg before modal:", storageImg);
    } catch (error) {
      console.error("Error uploading file:", error);
    }
  };

  const renderTestForm = () => {
    const commonFields = (
      <>
        {/* Display patient info outside the form */}
        {selectedPatient && (
          <div style={{ marginBottom: 16 }}>
            <span>Bệnh nhân: </span>
            {selectedPatient.name}
            <br />
            <span>Ngày sinh: </span>
            {formatDate(selectedPatient.dob) || "Unknown"}
            <br />
            <span>Giới tính: </span>
            {selectedPatient.gender || "Unknown"}
          </div>
        )}
        <Form.Item name="id" hidden>
          <Input />
        </Form.Item>
        <Form.Item name="encounterId" hidden>
          <Input />
        </Form.Item>
        <Form.Item name="evaluate" label="Đánh giá">
          <Input.TextArea rows={3} />
        </Form.Item>
        <Form.Item name="notes" label="Ghi chú">
          <Input.TextArea rows={3} />
        </Form.Item>
        <Form.Item name="createDate" label="Ngày tạo">
          <Input />
        </Form.Item>
        <Form.Item name="status" label="Trạng thái">
          <Select>
            <Option value="PENDING">PENDING</Option>
            <Option value="COMPLETED">COMPLETED</Option>
          </Select>
        </Form.Item>
      </>
    );
    return (
      <Form form={form} layout="vertical" onFinish={handleSaveTest}>
        {commonFields}
        {(() => {
          switch (activeTest) {
            case "labTest":
              return (
                <div className="grid grid-flow-row grid-cols-3 gap-4">
                  {[
                    "rbc",
                    "hct",
                    "hgb",
                    "mcv",
                    "mch",
                    "olt",
                    "wbc",
                    "gra",
                    "lym",
                    "momo",
                  ].map((field) => (
                    <Form.Item
                      key={field}
                      name={field}
                      label={field.toUpperCase()}
                    >
                      <InputNumber step={0.01} className="w-full" />
                    </Form.Item>
                  ))}
                </div>
              );
            case "imagingTest":
              return (
                <>
                  <Form.Item name="pdfResult" hidden>
                    <Input />
                  </Form.Item>
                  <Form.Item label="Tải ảnh lên">
                    <Input
                      type="file"
                      accept="image/*"
                      onChange={handleFileUpload}
                    />
                  </Form.Item>
                  {previewImage && (
                    <Form.Item label="Xem trước ảnh">
                      <Image src={previewImage} width={200} />
                    </Form.Item>
                  )}
                </>
              );
            case "cardiacTest":
              return (
                <>
                  <Form.Item name="type" label="Loại">
                    <Select>
                      <Option value="StressTest">
                        Kiểm tra căng thẳng (đo phản ứng tim khi gắng sức)
                      </Option>
                      <Option value="HolterMonitor">
                        Theo dõi Holter (ghi điện tâm liên tục trong 24-48 giờ)
                      </Option>
                      <Option value="ECG">
                        Điện tâm đồ (ghi hoạt động điện của tim)
                      </Option>
                    </Select>
                  </Form.Item>
                  <Form.Item label="Tải ảnh lên">
                    <Input
                      type="file"
                      accept="image/*"
                      onChange={handleFileUpload}
                    />
                  </Form.Item>
                  {previewImage && (
                    <Form.Item label="Xem trước ảnh">
                      <Image src={previewImage} width={200} />
                    </Form.Item>
                  )}
                  <Form.Item name="testName" label="Tên kiểm tra">
                    <Input />
                  </Form.Item>
                  <Form.Item name="organSystem" label="Hệ cơ quan">
                    <Input />
                  </Form.Item>
                  <Form.Item
                    name="isInvasive"
                    label="Có xâm lấn"
                    valuePropName="checked"
                  >
                    <div className="flex flex-row justify-around items-center sapce-x-4">
                      <div>
                        <Checkbox />
                        <span>Có</span>
                      </div>
                      <div>
                        <Checkbox />
                        <span>Không</span>
                      </div>
                    </div>
                  </Form.Item>
                  <Form.Item
                    name="isQuantitative"
                    label="Có định lượng"
                    valuePropName="checked"
                  >
                    <div className="flex flex-row justify-around items-center sapce-x-4">
                      <div>
                        <Checkbox />
                        <span>Có</span>
                      </div>
                      <div>
                        <Checkbox />
                        <span>Không</span>
                      </div>
                    </div>
                  </Form.Item>
                  <Form.Item name="recordDuration" label="Thời gian ghi">
                    <InputNumber min={0} />
                  </Form.Item>
                </>
              );
            case "digestiveTest":
              return (
                <>
                  <Form.Item name="duration" label="Thời gian diễn ra">
                    <Input />
                  </Form.Item>
                  <Form.Item label="Tải ảnh lên">
                    <Input
                      type="file"
                      accept="image/*"
                      onChange={handleFileUpload}
                    />
                  </Form.Item>
                  {previewImage && (
                    <Form.Item label="Xem trước ảnh">
                      <Image src={previewImage} width={200} />
                    </Form.Item>
                  )}
                  <Form.Item name="testName" label="Tên kiểm tra">
                    <Input />
                  </Form.Item>
                  <Form.Item name="organSystem" label="Hệ cơ quan">
                    <Input />
                  </Form.Item>
                  <Form.Item
                    name="isInvasive"
                    label="Có xâm lấn"
                    valuePropName="checked"
                  >
                    <div className="flex flex-row justify-around items-center sapce-x-4">
                      <div>
                        <Checkbox />
                        <span>Có</span>
                      </div>
                      <div>
                        <Checkbox />
                        <span>Không</span>
                      </div>
                    </div>
                  </Form.Item>
                  <Form.Item
                    name="isQuantitative"
                    label="Có định lượng"
                    valuePropName="checked"
                  >
                    <div className="flex flex-row justify-around items-center sapce-x-4">
                      <div>
                        <Checkbox />
                        <span>Có</span>
                      </div>
                      <div>
                        <Checkbox />
                        <span>Không</span>
                      </div>
                    </div>
                  </Form.Item>
                  <Form.Item name="recordDuration" label="Thời gian ghi">
                    <InputNumber min={0} />
                  </Form.Item>
                </>
              );
            case "eegTest":
              return (
                <>
                  <Form.Item label="Tải ảnh lên">
                    <Input
                      type="file"
                      accept="image/*"
                      onChange={handleFileUpload}
                    />
                  </Form.Item>
                  {previewImage && (
                    <Form.Item label="Xem trước ảnh">
                      <Image src={previewImage} width={200} />
                    </Form.Item>
                  )}
                  <Form.Item name="testName" label="Tên kiểm tra">
                    <Input />
                  </Form.Item>
                  <Form.Item name="organSystem" label="Hệ cơ quan">
                    <Input />
                  </Form.Item>
                  <Form.Item
                    name="isInvasive"
                    label="Có xâm lấn"
                    valuePropName="checked"
                  >
                    <div className="flex flex-row justify-around items-center sapce-x-4">
                      <div>
                        <Checkbox />
                        <span>Có</span>
                      </div>
                      <div>
                        <Checkbox />
                        <span>Không</span>
                      </div>
                    </div>
                  </Form.Item>
                  <Form.Item
                    name="isQuantitative"
                    label="Có định lượng"
                    valuePropName="checked"
                  >
                    <div className="flex flex-row justify-around items-center sapce-x-4">
                      <div>
                        <Checkbox />
                        <span>Có</span>
                      </div>
                      <div>
                        <Checkbox />
                        <span>Không</span>
                      </div>
                    </div>
                  </Form.Item>
                  <Form.Item
                    name="detectSeizure"
                    label="Phát hiện cơn co giật"
                    valuePropName="checked"
                  >
                    <div className="flex flex-row justify-around items-center sapce-x-4">
                      <div>
                        <Checkbox />
                        <span>Có</span>
                      </div>
                      <div>
                        <Checkbox />
                        <span>Không</span>
                      </div>
                    </div>
                  </Form.Item>
                  <Form.Item name="channels" label="Số lượng điện cực">
                    <InputNumber min={0} />
                  </Form.Item>
                  <Form.Item name="recordDuration" label="Thời gian ghi">
                    <InputNumber min={0} />
                  </Form.Item>
                </>
              );
            case "emgTest":
              return (
                <>
                  <Form.Item label="Tải ảnh lên">
                    <Input
                      type="file"
                      accept="image/*"
                      onChange={handleFileUpload}
                    />
                  </Form.Item>
                  {previewImage && (
                    <Form.Item label="Xem trước ảnh">
                      <Image src={previewImage} width={200} />
                    </Form.Item>
                  )}
                  <Form.Item name="testName" label="Tên kiểm tra">
                    <Input />
                  </Form.Item>
                  <Form.Item name="organSystem" label="Hệ cơ quan">
                    <Input />
                  </Form.Item>
                  <Form.Item
                    name="isInvasive"
                    label="Có xâm lấn"
                    valuePropName="checked"
                  >
                    <div className="flex flex-row justify-around items-center sapce-x-4">
                      <div>
                        <Checkbox />
                        <span>Có</span>
                      </div>
                      <div>
                        <Checkbox />
                        <span>Không</span>
                      </div>
                    </div>
                  </Form.Item>
                  <Form.Item
                    name="isQuantitative"
                    label="Có định lượng"
                    valuePropName="checked"
                  >
                    <div className="flex flex-row justify-around items-center sapce-x-4">
                      <div>
                        <Checkbox />
                        <span>Có</span>
                      </div>
                      <div>
                        <Checkbox />
                        <span>Không</span>
                      </div>
                    </div>
                  </Form.Item>
                  <Form.Item name="recordDuration" label="Thời gian ghi">
                    <InputNumber min={0} />
                  </Form.Item>
                </>
              );
            case "bloodgasTest":
              return (
                <>
                  <Form.Item name="id" hidden>
                    <Input />
                  </Form.Item>
                  <Form.Item name="testName" label="Tên kiểm tra">
                    <Input />
                  </Form.Item>
                  <Form.Item name="organSystem" label="Hệ cơ quan">
                    <Input />
                  </Form.Item>
                  <div className="grid grid-flow-row grid-cols-3 gap-4">
                    <Form.Item name="ph" label="pH">
                      <InputNumber step={0.01} className="w-full" />
                    </Form.Item>
                    <Form.Item name="pco2" label="pCO2">
                      <InputNumber step={0.01} className="w-full" />
                    </Form.Item>
                    <Form.Item name="po2" label="pO2">
                      <InputNumber step={0.01} className="w-full" />
                    </Form.Item>
                  </div>
                  <Form.Item
                    name="isInvasive"
                    label="Có xâm lấn"
                    valuePropName="checked"
                  >
                    <div className="flex flex-row justify-around items-center sapce-x-4">
                      <div>
                        <Checkbox />
                        <span>Có</span>
                      </div>
                      <div>
                        <Checkbox />
                        <span>Không</span>
                      </div>
                    </div>
                  </Form.Item>
                  <Form.Item
                    name="isQuantitative"
                    label="Có định lượng"
                    valuePropName="checked"
                  >
                    <div className="flex flex-row justify-around items-center sapce-x-4">
                      <div>
                        <Checkbox />
                        <span>Có</span>
                      </div>
                      <div>
                        <Checkbox />
                        <span>Không</span>
                      </div>
                    </div>
                  </Form.Item>
                  <Form.Item name="testEnvironment" label="Môi trường test">
                    <Select>
                      <Option value="Arterial">
                        Arterial (mẫu máu động mạch)
                      </Option>
                      <Option value="Venous">Venous (mẫu máu tĩnh mạch)</Option>
                      <Option value="Capillary">
                        Capillary (mẫu máu mao mạch)
                      </Option>
                    </Select>
                  </Form.Item>
                  <Form.Item name="patientPosition" label="Tư thế bệnh nhân">
                    <Select>
                      <Option value="Supine">Nằm ngửa</Option>
                      <Option value="Sitting">Ngồi</Option>
                      <Option value="Standing">Đứng</Option>
                    </Select>
                  </Form.Item>
                  <Form.Item name="recordDuration" label="Thời gian ghi">
                    <InputNumber min={0} />
                  </Form.Item>
                  <Form.Item name="evaluate" label="Đánh giá">
                    <Input.TextArea rows={2} />
                  </Form.Item>
                  <Form.Item name="notes" label="Ghi chú">
                    <Input.TextArea rows={3} />
                  </Form.Item>
                  <Form.Item name="encounterId" label="ID cuộc gặp" hidden>
                    <InputNumber min={0} />
                  </Form.Item>
                  <Form.Item name="status" label="Trạng thái">
                    <Select>
                      <Option value="PENDING">PENDING</Option>
                      <Option value="COMPLETED">COMPLETED</Option>
                    </Select>
                  </Form.Item>
                </>
              );
            case "nerveTest":
              return (
                <>
                  <Form.Item name="testName" label="Tên kiểm tra">
                    <Input />
                  </Form.Item>
                  <Form.Item name="organSystem" label="Hệ cơ quan">
                    <Input />
                  </Form.Item>
                  <Form.Item name="nerve" label="Dây thần kinh">
                    <Input />
                  </Form.Item>
                  <Form.Item name="conductionSpeed" label="Tốc độ dẫn truyền">
                    <InputNumber step={0.01} className="w-full" />
                  </Form.Item>
                  <Form.Item
                    name="isInvasive"
                    label="Có xâm lấn"
                    valuePropName="checked"
                  >
                    <div className="flex flex-row justify-around items-center sapce-x-4">
                      <div>
                        <Checkbox />
                        <span>Có</span>
                      </div>
                      <div>
                        <Checkbox />
                        <span>Không</span>
                      </div>
                    </div>
                  </Form.Item>
                  <Form.Item
                    name="isQuantitative"
                    label="Có định lượng"
                    valuePropName="checked"
                  >
                    <div className="flex flex-row justify-around items-center sapce-x-4">
                      <div>
                        <Checkbox />
                        <span>Có</span>
                      </div>
                      <div>
                        <Checkbox />
                        <span>Không</span>
                      </div>
                    </div>
                  </Form.Item>
                  <Form.Item name="testEnvironment" label="Môi trường test">
                    <Input />
                  </Form.Item>
                  <Form.Item name="patientPosition" label="Tư thế bệnh nhân">
                    <Select>
                      <Option value="Supine">Nằm ngửa</Option>
                      <Option value="Sitting">Ngồi</Option>
                      <Option value="Standing">Đứng</Option>
                    </Select>
                  </Form.Item>
                  <Form.Item name="recordDuration" label="Thời gian ghi">
                    <InputNumber min={0} />
                  </Form.Item>
                </>
              );
            case "spirometryTest":
              return (
                <>
                  <Form.Item name="testName" label="Tên kiểm tra">
                    <Input />
                  </Form.Item>
                  <Form.Item name="organSystem" label="Hệ cơ quan">
                    <Input />
                  </Form.Item>
                  <div className="grid grid-flow-row grid-cols-3 gap-4">
                    <Form.Item
                      name="fevl"
                      label="Thể tích khí thở ra mạnh trong 1 giây đầu tiên"
                    >
                      <InputNumber step={0.01} className="w-full" />
                    </Form.Item>
                    <Form.Item
                      name="fvc"
                      label="Tổng dung tích khí thở ra mạnh"
                    >
                      <InputNumber step={0.01} className="w-full" />
                    </Form.Item>
                  </div>
                  <Form.Item
                    name="isInvasive"
                    label="Có xâm lấn"
                    valuePropName="checked"
                  >
                    <div className="flex flex-row justify-around items-center sapce-x-4">
                      <div>
                        <Checkbox />
                        <span>Có</span>
                      </div>
                      <div>
                        <Checkbox />
                        <span>Không</span>
                      </div>
                    </div>
                  </Form.Item>
                  <Form.Item
                    name="isQuantitative"
                    label="Có định lượng"
                    valuePropName="checked"
                  >
                    <div className="flex flex-row justify-around items-center sapce-x-4">
                      <div>
                        <Checkbox />
                        <span>Có</span>
                      </div>
                      <div>
                        <Checkbox />
                        <span>Không</span>
                      </div>
                    </div>
                  </Form.Item>
                  <Form.Item name="testEnvironment" label="Môi trường test">
                    <Select>
                      <Option value="Arterial">
                        Arterial (mẫu máu động mạch)
                      </Option>
                      <Option value="Venous">Venous (mẫu máu tĩnh mạch)</Option>
                      <Option value="Capillary">
                        Capillary (mẫu máu mao mạch)
                      </Option>
                    </Select>
                  </Form.Item>
                  <Form.Item name="patientPosition" label="Tư thế bệnh nhân">
                    <Select>
                      <Option value="Supine">Nằm ngửa</Option>
                      <Option value="Sitting">Ngồi</Option>
                      <Option value="Standing">Đứng</Option>
                    </Select>
                  </Form.Item>
                  <Form.Item name="recordDuration" label="Thời gian ghi">
                    <InputNumber min={0} />
                  </Form.Item>
                </>
              );
            default:
              return <div>Chọn loại xét nghiệm</div>;
          }
        })()}
      </Form>
    );
  };

  return (
    <div className="h-full w-full bg-gray-100">
      <div className="ml-2 p-6 bg-white rounded-lg shadow-lg">
        <Title level={2} className="mb-6 text-blue-600">
          Kết quả cận lâm sàng
        </Title>
        <Input
          placeholder="Tìm kiếm bệnh nhân..."
          prefix={<SearchOutlined />}
          className="max-w-md mb-4"
          value={searchText}
          onChange={(e) => setSearchText(e.target.value)}
        />
        <Tabs defaultActiveKey="1">
          <TabPane tab="Xét nghiệm máu" key="1">
            <Table
              columns={patientColumns("labTest")}
              dataSource={filterPatients(labPatients)}
              rowKey="id"
              pagination={{ pageSize: 5 }}
            />
          </TabPane>
          <TabPane tab="Chẩn đoán hình ảnh" key="2">
            <Table
              columns={patientColumns("imagingTest")}
              dataSource={filterPatients(imagingPatients)}
              rowKey="id"
              pagination={{ pageSize: 5 }}
            />
          </TabPane>
          <TabPane tab="Thăm dò chức năng" key="3">
            <Tabs defaultActiveKey="cardiacTest">
              {/* Render all functional test tabs defined in TEST_TYPES */}
              {Object.entries(TEST_TYPES)
                .filter(([key]) =>
                  [
                    "cardiacTest",
                    "digestiveTest",
                    "eegTest",
                    "emgTest",
                    "bloodgasTest",
                    "nerveTest",
                    "spirometryTest",
                  ].includes(key)
                )
                .map(([key, { label }]) => (
                  <TabPane tab={label} key={key}>
                    {/* Use appropriate patient data based on test type */}
                    <Table
                      columns={patientColumns(key)}
                      dataSource={filterPatients(patientDataMap[key])}
                      rowKey="id"
                      pagination={{ pageSize: 5 }}
                    />
                  </TabPane>
                ))}
            </Tabs>
          </TabPane>
        </Tabs>
        <Modal
          title={`Cập nhật ${activeTest
            .replace(/([A-Z])/g, " $1")
            .replace(/^./, (str) => str.toUpperCase())}`}
          open={isModalVisible}
          onCancel={() => {
            setIsModalVisible(false);
            form.resetFields();
            setPreviewImage("");
            setSelectedPatient(null);
          }}
          footer={[
            <Button
              key="cancel"
              onClick={() => {
                setIsModalVisible(false);
                form.resetFields();
                setPreviewImage("");
                setSelectedPatient(null);
              }}
            >
              Hủy
            </Button>,
            <Button
              key="submit"
              type="primary"
              icon={<SaveOutlined />}
              onClick={() => form.submit()}
            >
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

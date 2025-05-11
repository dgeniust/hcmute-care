import React, { useState, useEffect } from "react";
import {
  Avatar,
  Card,
  Badge,
  Typography,
  Space,
  Tag,
  Tooltip,
  Button,
  Table,
  List,
  Modal,
  Input,
  Select,
  message,
  Form,
  Upload,
} from "antd";
import {
  HeartFilled,
  CalendarOutlined,
  MedicineBoxOutlined,
  FileTextOutlined,
  PlusOutlined,
  FileImageOutlined,
  EyeOutlined,
  DownloadOutlined,
  DeleteOutlined,
  LineChartOutlined,
  FileSearchOutlined,
  EditOutlined,
  UploadOutlined,
} from "@ant-design/icons";
import ClinicalReportExporter from "./ClinicalReportExporter";
import {
  notifyErrorWithCustomMessage,
  notifySuccessWithCustomMessage,
  handleHttpStatusCode,
} from "../../../utils/notificationHelper";
import { formatDateTime } from "../../../utils/formatDate";
const { Text } = Typography;
const ParaclinicalTest = () => {
  const apiUrl = import.meta.env.VITE_API_BASE_URL;
  const [messageApi, contextHolder] = message.useMessage();
  // Dữ liệu mẫu cho xét nghiệm
  const [labTests, setLabTest] = useState([]);

  // Dữ liệu mẫu cho chẩn đoán hình ảnh
  const [imagingTests, setImagingTests] = useState([]);

  // Dữ liệu mẫu cho thăm dò chức năng
  const [functionalTests, setFunctionalTests] = useState([]);

  const [labTestForm] = Form.useForm();
  const [imagingTestForm] = Form.useForm();
  const [functionalTestForm] = Form.useForm();
  const patientInfo = JSON.parse(
    localStorage.getItem("patientEncounterInfo") || "{}"
  );
  const [isModalLabTestOpen, setIsModalLabTestOpen] = useState(false);
  const [isModalImagingTestOpen, setIsModalImagingTestOpen] = useState(false);
  const [isModalFunctionalTestOpen, setIsModalFunctionalTestOpen] =
    useState(false);
  const [functionalTestType, setFunctionalTestType] = useState(null);
  const [isImageDetailModalOpen, setIsImageDetailModalOpen] = useState(false); // Modal xem chi tiết ảnh
  const [selectedImage, setSelectedImage] = useState(null); // Ảnh được chọn
  const showLabTestModal = () => {
    setIsModalLabTestOpen(true);
  };
  const handleLabTestCancel = () => {
    setIsModalLabTestOpen(false);
    labTestForm.resetFields(); // Reset form fields when modal is closed
  };
  const showImagingTestOpen = () => {
    setIsModalImagingTestOpen(true);
  };
  const handleImagingTestCancel = () => {
    setIsModalImagingTestOpen(false);
    imagingTestForm.resetFields(); // Reset form fields when modal is closed
  };
  const showFunctionalTestOpen = () => {
    setIsModalFunctionalTestOpen(true);
  };
  const handleFunctionalTestCancel = () => {
    setIsModalFunctionalTestOpen(false);
    functionalTestForm.resetFields(); // Reset form fields when modal is closed
  };
  // Object ánh xạ từ value sang tên đầy đủ
  const testTypeNames = {
    mau: "Công thức máu",
    nuoc_tieu: "Nước tiểu",
    dom: "Đờm",
    phan: "Phân",
    khac: "Sinh hóa máu",
  };
  const testImagingNames = {
    xq: "XQuang",
    sa: "Siêu âm",
    ct: "CT Scan",
    mri: "MRI",
  };

  const functionalTestNames = {
    spirometry: "Đo chức năng hô hấp",
    bloodgasanalysis: "Phân tích khí máu",
    nerveconduction: "Đo dẫn truyền thần kinh",
    eeg: "Điện não đồ",
    emg: "Điện cơ đồ",
    cardiactest: "Xét nghiệm tim mạch",
    digestive: "Xét nghiệm tiêu hóa",
  };

  // API endpoints for each functional test type
  const functionalTestEndpoints = {
    spirometry: `${apiUrl}v1/spirometry`,
    bloodgasanalysis: `${apiUrl}v1/blood-gas-analysis`,
    nerveconduction: `${apiUrl}v1/nerve-conduction`,
    eeg: `${apiUrl}v1/eeg`,
    emg: `${apiUrl}v1/emg`,
    cardiactest: `${apiUrl}v1/cardiac-tests`,
    digestive: `${apiUrl}v1/digestive-tests`,
  };

  const handleLabTestOk = async () => {
    const values = await labTestForm.validateFields(); // Lấy giá trị từ form
    const payload = {
      evaluate: "string",
      notes: testTypeNames[values.testType] || values.testType,
      encounterId: localStorage.getItem("encounterId"),
      rbc: 0,
      hct: 0,
      hgb: 0,
      mcv: 0,
      mch: 0,
      olt: 0,
      wbc: 0,
      gra: 0,
      lym: 0,
      momo: 0,
      status: "PENDING",
    };
    try {
      const response = await fetch(`${apiUrl}v1/laboratory-tests`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(payload),
      });
      if (!response.ok) {
        const errorData = await response.text();
        console.log("Error data:", errorData);
        handleHttpStatusCode(
          response.status,
          "",
          "Tạo xét nghiệm không thành công",
          messageApi
        );
        return;
      }
      const data = await response.json();
      console.log("Response data:", data);
      if (data.data && data.status === 201) {
        setLabTest((prevLabTests) => [
          ...prevLabTests,
          {
            id: data.data.id,
            notes: data.data.notes,
            encounterId: data.data.encounterId,
            createAt: formatDateTime(data.data.createDate),
            status: data.data.status,
          },
        ]);
        console.log("Lab tests:", labTests);
      }
      notifySuccessWithCustomMessage("Tạo xét nghiệm thành công", messageApi);
      setIsModalLabTestOpen(false);
      labTestForm.resetFields();
    } catch (e) {
      console.log("Error:", e);
      notifyErrorWithCustomMessage("Tạo xét nghiệm thất bại", messageApi);
    }
  };
  const handleImagingTestOk = async () => {
    try {
      const values = await imagingTestForm.validateFields(); // Lấy giá trị từ form
      console.log(`values in imaging: ${JSON.stringify(values)}`);
      console.log("testImagingType:", testImagingNames[values.testImagingType]);
      const payload = {
        evaluate: "string",
        notes:
          testImagingNames[values.testImagingType] || values.testImagingType,
        encounterId: localStorage.getItem("encounterId"),
        pdfResult: "string",
        status: "PENDING",
      };
      const response = await fetch(`${apiUrl}v1/imaging-tests`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(payload),
      });
      console.log("Response imaging tests:", response);
      console.log("Payload imaging tests:", payload);
      if (!response.ok) {
        const errorData = await response.text();
        console.log("Error data:", errorData);
        handleHttpStatusCode(
          response.status,
          "",
          "Tạo chẩn đoán không thành công",
          messageApi
        );
        return;
      }
      const data = await response.json();
      console.log("Response data:", data);
      if (data.data && data.status === 201) {
        setImagingTests((prevImagingTests) => [
          ...prevImagingTests,
          {
            id: data.data.id,
            evaluate: data.data.evaluate,
            notes: data.data.notes,
            encounterId: data.data.encounterId,
            pdfResult: data.data.pdfResult,
            createDate: formatDateTime(data.data.createDate),
            status: data.data.status,
          },
        ]);
        console.log("Imaging tests:", imagingTests);
      }
      notifySuccessWithCustomMessage("Tạo chẩn đoán thành công", messageApi);
      setIsModalImagingTestOpen(false);
      imagingTestForm.resetFields();
    } catch (e) {
      console.log("Error:", e);
      notifyErrorWithCustomMessage(e, messageApi);
    }
  };
  const handleFunctionalTestOk = async () => {
    try {
      const values = await functionalTestForm.validateFields();
      const encounterId = localStorage.getItem("encounterId");
      const testType = values.testFunctionalType;
      const testName = functionalTestNames[testType];
      let payload = {
        evaluate: "string",
        notes: "string",
        encounterId: encounterId,
        testName: testName,
        organSystem: "string",
        isInvasive: true,
        isQuantitative: true,
        recordDuration: 0,
        status: "PENDING",
      };

      // Add type-specific fields
      switch (values.testFunctionalType) {
        case "spirometry":
          payload = {
            ...payload,
            testEnvironment: "string",
            patientPosition: "string",
            fevl: 0,
            fvc: 0,
          };
          break;
        case "bloodgasanalysis":
          payload = {
            ...payload,
            testEnvironment: "string",
            patientPosition: "string",
            ph: 0,
            po2: 0,
            pco2: 0,
          };
          break;
        case "nerveconduction":
          payload = {
            ...payload,
            testEnvironment: "string",
            patientPosition: "string",
            nerve: "string",
            conductionSpeed: 0,
          };
          break;
        case "eeg":
          payload = {
            ...payload,
            image: "string",
            channels: 0,
            detectSeizure: false,
          };
          break;
        case "emg":
          payload = {
            ...payload,
            image: "string",
            muscleGroup: "string",
          };
          break;
        case "cardiactest":
          payload = {
            ...payload,
            type: "string",
            image: "string",
          };
          break;
        case "digestive":
          payload = {
            ...payload,
            image: "string",
            duration: 0,
          };
          break;
        default:
          throw new Error("Invalid test type");
      }

      const response = await fetch(functionalTestEndpoints[testType], {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload),
      });
      console.log("Response:", response);
      if (!response.ok) {
        const errorData = await response.text();
        console.log("Error data:", errorData);
        handleHttpStatusCode(
          response.status,
          "",
          "Tạo thăm dò chức năng không thành công",
          messageApi
        );
        return;
      }

      const data = await response.json();
      if (data.data && data.status === 201) {
        setFunctionalTests((prev) => [
          ...prev,
          {
            id: data.data.id,
            testName: data.data.testName,
            type: testType,
            createAt: formatDateTime(data.data.createAt),
            status: data.data.status,
          },
        ]);
      }
      notifySuccessWithCustomMessage(
        "Tạo thăm dò chức năng thành công",
        messageApi
      );
      setIsModalFunctionalTestOpen(false);
      console.log("Functional tests:", data.data);
      functionalTestForm.resetFields();
    } catch (e) {
      console.log("Error:", e);
      notifyErrorWithCustomMessage(
        "Tạo thăm dò chức năng thất bại",
        messageApi
      );
    }
  };
  // Log labTests khi thay đổi
  useEffect(() => {
    console.log("Updated labTests:", labTests);
  }, [labTests]);

  // Xem chi tiết ảnh
  const handleViewImage = (imageUrl) => {
    setSelectedImage(imageUrl);
    setIsImageDetailModalOpen(true);
  };
  // Tải xuống ảnh
  const handleDownloadImage = (imageUrl, testName) => {
    if (!imageUrl) {
      notifyErrorWithCustomMessage(
        "Không có hình ảnh để tải xuống",
        messageApi
      );
      return;
    }
    const link = document.createElement("a");
    link.href = imageUrl;
    link.download = `${testName || "image"}.jpg`; // Tên file tải xuống
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  };

  // Xóa Imaging Test
  // const handleDeleteImagingTest = async (testId) => {
  //     try {
  //     const response = await fetch(`http://localhost:8080/api/v1/imaging-tests/${testId}`, {
  //         method: 'DELETE',
  //         headers: {
  //             'Content-Type': 'application/json',
  //         },
  //     });
  //     if (!response.ok) {
  //         const errorData = await response.text();
  //         console.log('Error data:', errorData);
  //         handleHttpStatusCode(response.status, '', 'Xóa chẩn đoán hình ảnh thất bại', messageApi);
  //         return;
  //     }
  //         setImagingTests((prev) => prev.filter((test) => test.id !== testId));
  //         notifySuccessWithCustomMessage('Xóa chẩn đoán hình ảnh thành công', messageApi);
  //     } catch (e) {
  //         console.log('Error:', e);
  //         notifyErrorWithCustomMessage('Xóa chẩn đoán hình ảnh thất bại', messageApi);
  //     }
  // };
  const handleSubmitPatient = async () => {
    const ticketId = localStorage.getItem("ticketId");
    try {
      const response = await fetch(
        `${apiUrl}v1/tickets/${ticketId}/status?status=COMPLETED`,
        {
          method: "PATCH",
          headers: {
            "Content-Type": "application/json",
          },
        }
      );
      if (!response.ok) {
        const errorData = await response.text();
        console.log("Error data:", errorData);
        handleHttpStatusCode(
          response.status,
          "",
          "Cập nhật thông tin bệnh nhân thất bại",
          messageApi
        );
        return;
      }
      notifySuccessWithCustomMessage(
        "Cập nhật thông tin bệnh nhân thành công",
        messageApi
      );
      setTimeout(() => {
        window.location.href = "/doctor/records";
      }, 2000); // Chuyển hướng sau 2 giây
    } catch (e) {
      console.log("Error:", e);
      notifyErrorWithCustomMessage(
        "Cập nhật thông tin bệnh nhân thất bại",
        messageApi
      );
    }
  };
  return (
    <>
      {/* Yêu cầu cận lâm sàng */}
      <Card
        className="w-full rounded-xl overflow-hidden mt-6"
        variant={false}
        title={
          <div className="flex items-center">
            <MedicineBoxOutlined className="mr-2 text-blue-500" />
            <span>Yêu cầu Cận lâm sàng</span>
          </div>
        }
        extra={
          <ClinicalReportExporter
            labTests={labTests}
            imagingTests={imagingTests}
            functionalTests={functionalTests}
          />
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
            extra={
              <Tooltip title="Thêm yêu cầu xét nghiệm mới">
                <Button
                  type="primary"
                  icon={<PlusOutlined />}
                  onClick={showLabTestModal}
                >
                  Thêm
                </Button>
              </Tooltip>
            }
          >
            <Table
              dataSource={labTests}
              columns={[
                {
                  title: "Tên xét nghiệm",
                  dataIndex: "notes",
                  key: "notes",
                },
                {
                  title: "Loại",
                  dataIndex: "notes",
                  key: "notes",
                  render: (text) => <Tag color="green">{text}</Tag>,
                },
                {
                  title: "Thời gian yêu cầu",
                  dataIndex: "createAt",
                  key: "createAt",
                },
                {
                  title: "Trạng thái",
                  dataIndex: "status",
                  key: "status",
                  render: (status) => (
                    <Badge
                      status={
                        status === "COMPLETED"
                          ? "success"
                          : status === "PENDING"
                          ? "processing"
                          : "default"
                      }
                      text={
                        status === "COMPLETED"
                          ? "Hoàn thành"
                          : status === "PENDING"
                          ? "Đang xử lý"
                          : "Chưa có"
                      }
                    />
                  ),
                },
                {
                  title: "Kết quả",
                  dataIndex: "result",
                  key: "result",
                  render: (result, record) =>
                    record.status === "COMPLETED" ? (
                      <Button type="link" icon={<FileSearchOutlined />}>
                        Xem kết quả
                      </Button>
                    ) : (
                      <Text type="secondary">Chưa có</Text>
                    ),
                },
                {
                  title: "Thao tác",
                  key: "action",
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
            extra={
              <Tooltip title="Thêm yêu cầu chẩn đoán hình ảnh mới">
                <Button
                  type="primary"
                  icon={<PlusOutlined />}
                  onClick={showImagingTestOpen}
                >
                  Thêm
                </Button>
              </Tooltip>
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
                      {test.status === "COMPLETED" ? (
                        <img
                          src={test.imageUrl || null}
                          alt={test.name}
                          className="object-cover h-full w-full"
                        />
                      ) : (
                        <div className="flex flex-col items-center justify-center text-gray-400">
                          <FileImageOutlined style={{ fontSize: "3rem" }} />
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
                            status={
                              test.status === "COMPLETED"
                                ? "success"
                                : "processing"
                            }
                            text={test.status}
                          />
                        </div>
                        <div></div>
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
            extra={
              <Tooltip title="Thêm yêu cầu thăm dò chức năng mới">
                <Button
                  type="primary"
                  icon={<PlusOutlined />}
                  onClick={showFunctionalTestOpen}
                >
                  Thêm
                </Button>
              </Tooltip>
            }
          >
            <List
              dataSource={functionalTests}
              renderItem={(item) => (
                <List.Item
                  key={item.id}
                  actions={[
                    <Button
                      key="view"
                      type="link"
                      icon={<FileSearchOutlined />}
                    >
                      {item.status === "COMPLETED" ? "Xem kết quả" : "Chi tiết"}
                    </Button>,
                    <Button key="edit" type="text" icon={<EditOutlined />} />,
                    <Button
                      key="delete"
                      type="text"
                      danger
                      icon={<DeleteOutlined />}
                    />,
                  ]}
                >
                  <List.Item.Meta
                    avatar={
                      <Avatar
                        icon={<HeartFilled />}
                        style={{
                          backgroundColor:
                            item.type === "spirometry"
                              ? "#ff4d4f"
                              : item.type === "bloodgasanalysis"
                              ? "#13c2c2"
                              : item.type === "nerveconduction"
                              ? "#722ed1"
                              : item.type === "eeg"
                              ? "#1890ff"
                              : item.type === "emg"
                              ? "#fadb14"
                              : item.type === "cardiactest"
                              ? "#eb2f96"
                              : item.type === "digestive"
                              ? "#52c41a"
                              : "#1890ff",
                        }}
                      />
                    }
                    title={<Text strong>{functionalTestNames[item.type]}</Text>}
                    description={
                      <div className="flex flex-col md:flex-row md:items-center text-xs">
                        <Tag color="blue">{item.type}</Tag>
                        <Text type="secondary" className="mx-6">
                          <CalendarOutlined />
                          <span className="ml-2">{item.createAt}</span>
                        </Text>
                        <Badge
                          className="ml-4"
                          status={
                            item.status === "COMPLETED"
                              ? "success"
                              : "processing"
                          }
                          text={item.status}
                        />
                      </div>
                    }
                  />
                  <div>
                    <Text type="secondary">
                      Bác sĩ chỉ định: {localStorage.getItem("userFullName")}
                    </Text>
                  </div>
                </List.Item>
              )}
            />
          </Card>

          {/* Nút thêm yêu cầu mới */}
          <div className="flex justify-center mt-4 flex-row items-center w-full">
            <div className="flex flex-row items-center justify-around w-full">
              <Button type="primary" size="large" onClick={handleSubmitPatient}>
                Đã khám xong giai đoạn 1
              </Button>
            </div>
          </div>
        </div>
        <Modal
          title="Tạo Xét Nghiệm Mới"
          open={isModalLabTestOpen}
          onCancel={handleLabTestCancel}
          width={700}
          footer={[
            <Button key="back" onClick={handleLabTestCancel}>
              Hủy
            </Button>,
            <Button key="submit" type="primary" onClick={handleLabTestOk}>
              Tạo Xét Nghiệm
            </Button>,
          ]}
        >
          <Form form={labTestForm} layout="vertical">
            <div className="grid grid-cols-2 gap-4">
              <Form.Item
                name="testName"
                label="Tên xét nghiệm"
                rules={[
                  { required: true, message: "Vui lòng nhập tên xét nghiệm" },
                ]}
              >
                <Input placeholder="Nhập tên xét nghiệm" />
              </Form.Item>

              <Form.Item
                name="testType"
                label="Loại xét nghiệm"
                rules={[
                  { required: true, message: "Vui lòng chọn loại xét nghiệm" },
                ]}
              >
                <Select placeholder="Chọn loại xét nghiệm" className="w-full">
                  <Select.Option value="mau">Công thức máu</Select.Option>
                  <Select.Option value="nuoc_tieu">Nước tiểu</Select.Option>
                  <Select.Option value="dom">Đờm</Select.Option>
                  <Select.Option value="phan">Phân</Select.Option>
                  <Select.Option value="khac">Sinh hóa máu</Select.Option>
                </Select>
              </Form.Item>
            </div>

            <div className="grid grid-cols-2 gap-4">
              <Form.Item name="patientId" label="Mã bệnh nhân">
                <Input
                  placeholder="Nhập mã bệnh nhân"
                  defaultValue={patientInfo?.barcode || ""}
                  disabled
                />
              </Form.Item>

              <Form.Item name="patientName" label="Tên bệnh nhân">
                <Input
                  placeholder="Nhập tên bệnh nhân"
                  defaultValue={patientInfo?.patient?.name || ""}
                  disabled
                />
              </Form.Item>
            </div>
          </Form>
        </Modal>
        <Modal
          title="Tạo Chẩn Đoán Hình Ảnh Mới"
          open={isModalImagingTestOpen}
          onCancel={handleImagingTestCancel}
          width={700}
          footer={[
            <Button key="back" onClick={handleImagingTestCancel}>
              Hủy
            </Button>,
            <Button key="submit" type="primary" onClick={handleImagingTestOk}>
              Tạo Xét Nghiệm
            </Button>,
          ]}
        >
          <Form form={imagingTestForm} layout="vertical">
            <div className="grid grid-cols-2 gap-4">
              <Form.Item
                name="testImagingName"
                label="Tên chẩn đoán"
                rules={[
                  { required: true, message: "Vui lòng nhập tên chẩn đoán" },
                ]}
              >
                <Input placeholder="Nhập tên xét nghiệm" />
              </Form.Item>

              <Form.Item
                name="testImagingType"
                label="Loại chẩn đoán"
                rules={[
                  { required: true, message: "Vui lòng chọn loại chẩn đoán" },
                ]}
              >
                <Select placeholder="Chọn loại xét nghiệm" className="w-full">
                  <Select.Option value="xq">XQuang</Select.Option>
                  <Select.Option value="sa">Siêu âm</Select.Option>
                  <Select.Option value="ct">CT Scan</Select.Option>
                  <Select.Option value="mri">MRI</Select.Option>
                </Select>
              </Form.Item>
            </div>

            <div className="grid grid-cols-2 gap-4">
              <Form.Item name="patientId" label="Mã bệnh nhân">
                <Input
                  placeholder="Nhập mã bệnh nhân"
                  defaultValue={patientInfo?.barcode || ""}
                  disabled
                />
              </Form.Item>

              <Form.Item name="patientName" label="Tên bệnh nhân">
                <Input
                  placeholder="Nhập tên bệnh nhân"
                  defaultValue={patientInfo?.patient?.name || ""}
                  disabled
                />
              </Form.Item>
            </div>
          </Form>
        </Modal>
        {/* Modal for Functional Test */}
        <Modal
          title="Tạo Thăm Dò Chức Năng Mới"
          open={isModalFunctionalTestOpen}
          onCancel={handleFunctionalTestCancel}
          width={700}
          footer={[
            <Button key="back" onClick={handleFunctionalTestCancel}>
              Hủy
            </Button>,
            <Button
              key="submit"
              type="primary"
              onClick={handleFunctionalTestOk}
            >
              Tạo Thăm Dò
            </Button>,
          ]}
        >
          <Form form={functionalTestForm} layout="vertical">
            <div className="grid grid-cols-2 gap-4">
              <Form.Item
                name="testName"
                label="Tên xét nghiệm"
                rules={[
                  { required: true, message: "Vui lòng nhập tên xét nghiệm" },
                ]}
              >
                <Input placeholder="Nhập tên xét nghiệm" />
              </Form.Item>
              <Form.Item
                name="testFunctionalType"
                label="Loại thăm dò"
                rules={[
                  { required: true, message: "Vui lòng chọn loại thăm dò" },
                ]}
              >
                <Select
                  placeholder="Chọn loại thăm dò"
                  className="w-full"
                  onChange={(value) => setFunctionalTestType(value)}
                >
                  <Select.Option value="spirometry">
                    Đo chức năng hô hấp
                  </Select.Option>
                  <Select.Option value="bloodgasanalysis">
                    Phân tích khí máu
                  </Select.Option>
                  <Select.Option value="nerveconduction">
                    Đo dẫn truyền thần kinh
                  </Select.Option>
                  <Select.Option value="eeg">Điện não đồ</Select.Option>
                  <Select.Option value="emg">Điện cơ đồ</Select.Option>
                  <Select.Option value="cardiactest">
                    Xét nghiệm tim mạch
                  </Select.Option>
                  <Select.Option value="digestive">
                    Xét nghiệm tiêu hóa
                  </Select.Option>
                </Select>
              </Form.Item>
            </div>
            <div className="grid grid-cols-2 gap-4">
              <Form.Item name="patientId" label="Mã bệnh nhân">
                <Input
                  placeholder="Nhập mã bệnh nhân"
                  defaultValue={patientInfo?.barcode || ""}
                  disabled
                />
              </Form.Item>
              <Form.Item name="patientName" label="Tên bệnh nhân">
                <Input
                  placeholder="Nhập tên bệnh nhân"
                  defaultValue={patientInfo?.patient?.name || ""}
                  disabled
                />
              </Form.Item>
            </div>
          </Form>
        </Modal>
        {contextHolder}
      </Card>
    </>
  );
};

export default ParaclinicalTest;

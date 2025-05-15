import React, { useState } from 'react';
import {
  Modal,
  Form,
  Input,
  Select,
  DatePicker,
  Button,
  Space,
  Divider,
  Radio,
  Typography,
  Row,
  Col,
  Upload,
  message
} from 'antd';
import {
  PlusOutlined,
  FileTextOutlined,
  FileImageOutlined,
  LineChartOutlined,
  UploadOutlined,
  UserOutlined
} from '@ant-design/icons';
import locale from 'antd/es/date-picker/locale/vi_VN';
import dayjs from 'dayjs';

const { Option } = Select;
const { TextArea } = Input;
const { Title, Text } = Typography;

const AddParaclinicalTest = ({ visible, onCancel, onSubmit }) => {
  const [form] = Form.useForm();
  const [loaiYeuCau, setLoaiYeuCau] = useState('xetNghiem');

  // Danh sách các loại xét nghiệm
  const danhSachXetNghiem = [
    { value: 'biochem', label: 'Sinh hóa' },
    { value: 'hematology', label: 'Huyết học' },
    { value: 'immunology', label: 'Miễn dịch' },
    { value: 'microbiology', label: 'Vi sinh' },
    { value: 'pathology', label: 'Giải phẫu bệnh' },
  ];

  // Danh sách các loại chẩn đoán hình ảnh
  const danhSachChanDoanHinhAnh = [
    { value: 'xray', label: 'X-quang' },
    { value: 'ultrasound', label: 'Siêu âm' },
    { value: 'ct', label: 'CT Scanner' },
    { value: 'mri', label: 'MRI' },
    { value: 'doppler', label: 'Doppler' },
  ];

  // Danh sách các loại thăm dò chức năng
  const danhSachThamDoChucNang = [
    { value: 'ecg', label: 'Điện tim (ECG)' },
    { value: 'eeg', label: 'Điện não (EEG)' },
    { value: 'endoscopy', label: 'Nội soi' },
    { value: 'spirometry', label: 'Đo chức năng hô hấp' },
    { value: 'holter', label: 'Holter' },
  ];

  const handleLoaiYeuCauChange = (e) => {
    setLoaiYeuCau(e.target.value);
    form.resetFields(['tenYeuCau', 'loai', 'moTa', 'taiLieuDinhKem']);
  };

  const handleSubmit = () => {
    form.validateFields().then(values => {
      // Định dạng lại ngày tháng
      const formattedValues = {
        ...values,
        ngayYeuCau: values.ngayYeuCau ? values.ngayYeuCau.format('YYYY-MM-DD HH:mm:ss') : null,
        loaiYeuCau: loaiYeuCau,
        trangThai: 'Chờ xử lý',
      };
      
      onSubmit(formattedValues);
      message.success('Đã tạo yêu cầu cận lâm sàng mới thành công!');
      form.resetFields();
    }).catch(info => {
      console.log('Validate Failed:', info);
    });
  };

  // Tùy chỉnh component upload
  const normFile = (e) => {
    if (Array.isArray(e)) {
      return e;
    }
    return e?.fileList;
  };

  return (
    <Modal
      open={visible}
      title={
        <div className="flex items-center">
          <PlusOutlined className="mr-2 text-blue-500" />
          <span>Thêm yêu cầu cận lâm sàng mới</span>
        </div>
      }
      onCancel={onCancel}
      width={800}
      footer={[
        <Button key="back" onClick={onCancel}>
          Hủy
        </Button>,
        <Button key="submit" type="primary" onClick={handleSubmit}>
          Tạo yêu cầu
        </Button>,
      ]}
    >
      <Form
        form={form}
        layout="vertical"
        initialValues={{
          ngayYeuCau: dayjs(),
          loaiYeuCau: 'xetNghiem',
          doKhanCap: 'binhthuong'
        }}
      >
        <Divider orientation="left">Thông tin chung</Divider>
        
        <Row gutter={16}>
          <Col span={12}>
            <Form.Item
              name="ngayYeuCau"
              label="Ngày yêu cầu"
              rules={[{ required: true, message: 'Vui lòng chọn ngày yêu cầu!' }]}
            >
              <DatePicker 
                showTime 
                format="DD/MM/YYYY HH:mm" 
                locale={locale}
                style={{ width: '100%' }}
                placeholder="Chọn ngày và giờ"
              />
            </Form.Item>
          </Col>
          {/* <Col span={12}>
            <Form.Item
              name="bacSiChiDinh"
              label="Bác sĩ chỉ định"
              rules={[{ required: true, message: 'Vui lòng chọn bác sĩ chỉ định!' }]}
            >
              <Select
                placeholder="Chọn bác sĩ chỉ định"
                showSearch
                optionFilterProp="children"
              >
                {danhSachBacSi.map(bacSi => (
                  <Option key={bacSi.value} value={bacSi.value}>{bacSi.label}</Option>
                ))}
              </Select>
            </Form.Item>
          </Col> */}
        </Row>
        
        <Row gutter={16}>
          <Col span={12}>
            <Form.Item
              name="maYeuCau"
              label="Mã yêu cầu"
            >
              <Input placeholder="Tự động tạo" disabled />
            </Form.Item>
          </Col>
          <Col span={12}>
            <Form.Item
              name="doKhanCap"
              label="Độ khẩn cấp"
              rules={[{ required: true, message: 'Vui lòng chọn độ khẩn cấp!' }]}
            >
              <Radio.Group>
                <Radio value="khanCap">Khẩn cấp</Radio>
                <Radio value="binhthuong">Bình thường</Radio>
              </Radio.Group>
            </Form.Item>
          </Col>
        </Row>

        <Divider orientation="left">Loại yêu cầu</Divider>
        
        <Form.Item name="loaiYeuCau">
          <Radio.Group onChange={handleLoaiYeuCauChange} value={loaiYeuCau}>
            <Space direction="vertical">
              <Radio value="xetNghiem">
                <Space>
                  <FileTextOutlined className="text-green-600" />
                  <Text strong>Xét nghiệm</Text>
                </Space>
              </Radio>
              <Radio value="chanDoanHinhAnh">
                <Space>
                  <FileImageOutlined className="text-blue-600" />
                  <Text strong>Chẩn đoán hình ảnh</Text>
                </Space>
              </Radio>
              <Radio value="thamDoChucNang">
                <Space>
                  <LineChartOutlined className="text-purple-600" />
                  <Text strong>Thăm dò chức năng</Text>
                </Space>
              </Radio>
            </Space>
          </Radio.Group>
        </Form.Item>

        {loaiYeuCau === 'xetNghiem' && (
          <>
            <Form.Item
              name="tenYeuCau"
              label="Tên xét nghiệm"
              rules={[{ required: true, message: 'Vui lòng nhập tên xét nghiệm!' }]}
            >
              <Input placeholder="Nhập tên xét nghiệm" />
            </Form.Item>
            
            <Form.Item
              name="loai"
              label="Loại xét nghiệm"
              rules={[{ required: true, message: 'Vui lòng chọn loại xét nghiệm!' }]}
            >
              <Select placeholder="Chọn loại xét nghiệm">
                {danhSachXetNghiem.map(xetNghiem => (
                  <Option key={xetNghiem.value} value={xetNghiem.value}>{xetNghiem.label}</Option>
                ))}
              </Select>
            </Form.Item>
          </>
        )}

        {loaiYeuCau === 'chanDoanHinhAnh' && (
          <>
            <Form.Item
              name="tenYeuCau"
              label="Tên chẩn đoán hình ảnh"
              rules={[{ required: true, message: 'Vui lòng nhập tên chẩn đoán hình ảnh!' }]}
            >
              <Input placeholder="Nhập tên chẩn đoán hình ảnh" />
            </Form.Item>
            
            <Form.Item
              name="loai"
              label="Loại chẩn đoán hình ảnh"
              rules={[{ required: true, message: 'Vui lòng chọn loại chẩn đoán hình ảnh!' }]}
            >
              <Select placeholder="Chọn loại chẩn đoán hình ảnh">
                {danhSachChanDoanHinhAnh.map(chanDoan => (
                  <Option key={chanDoan.value} value={chanDoan.value}>{chanDoan.label}</Option>
                ))}
              </Select>
            </Form.Item>
          </>
        )}

        {loaiYeuCau === 'thamDoChucNang' && (
          <>
            <Form.Item
              name="tenYeuCau"
              label="Tên thăm dò chức năng"
              rules={[{ required: true, message: 'Vui lòng nhập tên thăm dò chức năng!' }]}
            >
              <Input placeholder="Nhập tên thăm dò chức năng" />
            </Form.Item>
            
            <Form.Item
              name="loai"
              label="Loại thăm dò chức năng"
              rules={[{ required: true, message: 'Vui lòng chọn loại thăm dò chức năng!' }]}
            >
              <Select placeholder="Chọn loại thăm dò chức năng">
                {danhSachThamDoChucNang.map(thamDo => (
                  <Option key={thamDo.value} value={thamDo.value}>{thamDo.label}</Option>
                ))}
              </Select>
            </Form.Item>
          </>
        )}

        <Form.Item
          name="moTa"
          label="Mô tả / Chỉ định"
        >
          <TextArea 
            rows={4} 
            placeholder="Nhập mô tả chi tiết hoặc các chỉ định cụ thể"
          />
        </Form.Item>

        <Form.Item
          name="taiLieuDinhKem"
          label="Tài liệu đính kèm"
          valuePropName="fileList"
          getValueFromEvent={normFile}
        >
          <Upload 
            name="files" 
            listType="picture"
            beforeUpload={() => false}
          >
            <Button icon={<UploadOutlined />}>Tải lên tài liệu</Button>
          </Upload>
        </Form.Item>

        <Form.Item
          name="ghiChu"
          label="Ghi chú"
        >
          <TextArea 
            rows={3} 
            placeholder="Nhập ghi chú (nếu có)"
          />
        </Form.Item>
      </Form>
    </Modal>
  );
};

export default AddParaclinicalTest;
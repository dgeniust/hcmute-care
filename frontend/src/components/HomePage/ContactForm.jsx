import { useState } from "react";
import { Form, Input, Button, Card, Row, Col, Typography, Select, message } from "antd";
import { SendOutlined } from "@ant-design/icons";
import emailjs from "@emailjs/browser";

const { Title } = Typography;
const { TextArea } = Input;
const { Option } = Select;

const ContactForm = () => {
  const [form] = Form.useForm();
  const [submitting, setSubmitting] = useState(false);
  const SERVICE_ID = import.meta.env.VITE_SERVICE_ID;
  const TEMPLATE_ID = import.meta.env.VITE_TEMPLATE_ID;
  const PUBLIC_KEY = import.meta.env.VITE_PUBLIC_KEY;
  const onFinish = async (values) => {
    setSubmitting(true);
    try {
      // Gửi email qua EmailJS
      const templateParams = {
        name: values.name,
        email: values.email,
        phone: values.phone,
        subject: values.subject,
        message: values.message,
        to_email: "dathiichan141@gmail.com", // Email nhận thông tin
      };

      await emailjs.send(
        SERVICE_ID, // Thay bằng Service ID từ EmailJS
        TEMPLATE_ID, // Thay bằng Template ID từ EmailJS
        templateParams,
        PUBLIC_KEY // Thay bằng Public Key từ EmailJS
      );

      message.success("Cảm ơn bạn đã liên hệ! Chúng tôi sẽ phản hồi sớm.");
      form.resetFields();
    } catch (error) {
      console.error("Error sending email:", error);
      message.error("Có lỗi xảy ra khi gửi thông tin. Vui lòng thử lại.");
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <Card className="h-full shadow-lg" variant={false}>
      <Title level={3} className="mb-6 text-blue-600">
        Gửi tin nhắn cho chúng tôi
      </Title>

      <Form form={form} layout="vertical" onFinish={onFinish} requiredMark={false}>
        <Row gutter={16}>
          <Col xs={24} md={12}>
            <Form.Item
              name="name"
              label="Họ và tên"
              rules={[{ required: true, message: "Vui lòng nhập họ tên của bạn" }]}
            >
              <Input size="large" placeholder="Nhập họ và tên của bạn" />
            </Form.Item>
          </Col>

          <Col xs={24} md={12}>
            <Form.Item
              name="email"
              label="Email"
              rules={[
                { required: true, message: "Vui lòng nhập email của bạn" },
                { type: "email", message: "Email không hợp lệ" },
              ]}
            >
              <Input size="large" placeholder="Nhập email của bạn" />
            </Form.Item>
          </Col>
        </Row>

        <Row gutter={16}>
          <Col xs={24} md={12}>
            <Form.Item
              name="phone"
              label="Số điện thoại"
              rules={[
                { required: true, message: "Vui lòng nhập số điện thoại của bạn" },
                { pattern: /^[0-9]{10,11}$/, message: "Số điện thoại không hợp lệ" },
              ]}
            >
              <Input size="large" placeholder="Nhập số điện thoại của bạn" />
            </Form.Item>
          </Col>

          <Col xs={24} md={12}>
            <Form.Item
              name="subject"
              label="Chủ đề"
              rules={[{ required: true, message: "Vui lòng chọn chủ đề" }]}
            >
              <Select size="large" placeholder="Chọn chủ đề">
                <Option value="general">Thông tin chung</Option>
                <Option value="appointment">Đặt lịch khám</Option>
                <Option value="feedback">Góp ý, phản hồi</Option>
                <Option value="support">Hỗ trợ kỹ thuật</Option>
                <Option value="other">Khác</Option>
              </Select>
            </Form.Item>
          </Col>
        </Row>

        <Form.Item
          name="message"
          label="Nội dung"
          rules={[{ required: true, message: "Vui lòng nhập nội dung tin nhắn" }]}
        >
          <TextArea rows={6} placeholder="Nhập nội dung tin nhắn của bạn" className="resize-none" />
        </Form.Item>

        <Form.Item>
          <Button
            type="primary"
            htmlType="submit"
            size="large"
            icon={<SendOutlined />}
            loading={submitting}
            className="mt-4 min-w-[150px]"
          >
            Gửi tin nhắn
          </Button>
        </Form.Item>
      </Form>
    </Card>
  );
};

export default ContactForm;
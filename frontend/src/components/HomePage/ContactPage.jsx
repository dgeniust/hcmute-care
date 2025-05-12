import { useState } from "react"
import { Form, Input, Button, Card, Row, Col, Typography, Divider, message, Select, Space } from "antd"
import {
  PhoneOutlined,
  MailOutlined,
  EnvironmentOutlined,
  ClockCircleOutlined,
  SendOutlined,
  FacebookOutlined,
  TwitterOutlined,
  InstagramOutlined,
  LinkedinOutlined,
} from "@ant-design/icons"
import ContactForm from "./ContactForm"

const { Title, Text, Paragraph } = Typography
const { TextArea } = Input
const { Option } = Select

const ContactPage = () => {
  const [form] = Form.useForm()
  const [submitting, setSubmitting] = useState(false)

  const onFinish = async (values) => {
    setSubmitting(true)
    try {
      // Giả lập gửi form
      await new Promise((resolve) => setTimeout(resolve, 1000))
      console.log("Form values:", values)
      message.success("Cảm ơn bạn đã liên hệ với chúng tôi. Chúng tôi sẽ phản hồi sớm nhất có thể!")
      form.resetFields()
    } catch (error) {
      message.error("Có lỗi xảy ra khi gửi thông tin. Vui lòng thử lại sau.")
    } finally {
      setSubmitting(false)
    }
  }

  return (
    <div className="bg-gradient-to-b from-blue-50 to-white py-12">
      <div className="container mx-auto px-4">
        <div className="mb-12 text-center">
          <Title level={1} className="text-3xl font-bold text-gray-800">
            Liên hệ với chúng tôi
          </Title>
          <Paragraph className="mx-auto mt-4 max-w-2xl text-gray-600">
            Chúng tôi luôn sẵn sàng lắng nghe và hỗ trợ bạn. Hãy liên hệ với chúng tôi nếu bạn có bất kỳ câu hỏi hoặc
            yêu cầu nào.
          </Paragraph>
        </div>

        <Row gutter={[32, 32]} className="items-stretch">
          <Col xs={24} lg={10}>
            <Card className="h-full shadow-lg" variant={false}>
              <Title level={3} className="mb-6 text-blue-600">
                Thông tin liên hệ
              </Title>

              <Space direction="vertical" size="large" className="w-full">
                <div className="flex items-start">
                  <PhoneOutlined className="mr-4 text-xl text-blue-500" />
                  <div>
                    <Text strong className="block text-gray-700">
                      Điện thoại
                    </Text>
                    <Text className="text-gray-600">+84 123 456 789</Text>
                    <Text className="block text-gray-600">+84 987 654 321 (Hotline)</Text>
                  </div>
                </div>

                <div className="flex items-start">
                  <MailOutlined className="mr-4 text-xl text-blue-500" />
                  <div>
                    <Text strong className="block text-gray-700">
                      Email
                    </Text>
                    <Text className="text-gray-600">info@healthcare.com</Text>
                    <Text className="block text-gray-600">support@healthcare.com</Text>
                  </div>
                </div>

                <div className="flex items-start">
                  <EnvironmentOutlined className="mr-4 text-xl text-blue-500" />
                  <div>
                    <Text strong className="block text-gray-700">
                      Địa chỉ
                    </Text>
                    <Text className="text-gray-600">123 Đường Nguyễn Văn Linh, Quận 7, TP. Hồ Chí Minh</Text>
                  </div>
                </div>

                <div className="flex items-start">
                  <ClockCircleOutlined className="mr-4 text-xl text-blue-500" />
                  <div>
                    <Text strong className="block text-gray-700">
                      Giờ làm việc
                    </Text>
                    <Text className="text-gray-600">Thứ Hai - Thứ Sáu: 8:00 - 17:30</Text>
                    <Text className="block text-gray-600">Thứ Bảy: 8:00 - 12:00</Text>
                    <Text className="block text-gray-600">Chủ Nhật: Nghỉ</Text>
                  </div>
                </div>
              </Space>

              <Divider />

              <div>
                <Text strong className="mb-4 block text-gray-700">
                  Kết nối với chúng tôi
                </Text>
                <Space size="middle">
                  <Button
                    type="primary"
                    shape="circle"
                    icon={<FacebookOutlined />}
                    size="large"
                    className="bg-[#1877F2]"
                  />
                  <Button
                    type="primary"
                    shape="circle"
                    icon={<TwitterOutlined />}
                    size="large"
                    className="bg-[#1DA1F2]"
                  />
                  <Button
                    type="primary"
                    shape="circle"
                    icon={<InstagramOutlined />}
                    size="large"
                    className="bg-gradient-to-r from-[#833AB4] via-[#FD1D1D] to-[#FCAF45]"
                  />
                  <Button
                    type="primary"
                    shape="circle"
                    icon={<LinkedinOutlined />}
                    size="large"
                    className="bg-[#0A66C2]"
                  />
                </Space>
              </div>
            </Card>
          </Col>

          <Col xs={24} lg={14}>
            {/* <Card className="h-full shadow-lg" bordered={false}>
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
            </Card> */}
            <ContactForm/>
          </Col>
        </Row>

        <div className="mt-12 overflow-hidden rounded-lg shadow-lg">
          <iframe
            src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3919.5177580567147!2d106.69916121471856!3d10.771594992323746!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x31752f4670702e31%3A0xa5777fb3a5bb9972!2sBitexco%20Financial%20Tower!5e0!3m2!1sen!2s!4v1652345678901!5m2!1sen!2s"
            width="100%"
            height="450"
            style={{ border: 0 }}
            allowFullScreen
            loading="lazy"
            referrerPolicy="no-referrer-when-downgrade"
          />
        </div>

        <div className="mt-12 rounded-lg bg-blue-50 p-8 text-center shadow-md">
          <Title level={3} className="text-blue-600">
            Đặt lịch khám ngay
          </Title>
          <Paragraph className="mx-auto mt-4 max-w-2xl text-gray-600">
            Bạn cần đặt lịch khám gấp? Hãy gọi cho chúng tôi hoặc đặt lịch trực tuyến.
          </Paragraph>
          <div className="mt-6 flex flex-wrap justify-center gap-4">
            <Button type="primary" size="large" icon={<PhoneOutlined />}>
              Gọi ngay: 1900 1234
            </Button>
            <Button type="default" size="large">
              Đặt lịch trực tuyến
            </Button>
          </div>
        </div>
      </div>
    </div>
  )
}

export default ContactPage

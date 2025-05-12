import { Card, Typography, Space, Button, Divider } from "antd"
import {
  PhoneOutlined,
  MailOutlined,
  EnvironmentOutlined,
  ClockCircleOutlined,
  FacebookOutlined,
  TwitterOutlined,
  InstagramOutlined,
  LinkedinOutlined,
} from "@ant-design/icons"

const { Title, Text } = Typography

const ContactInfo = () => {
  return (
    <Card className="h-full shadow-lg" bordered={false}>
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
          <Button type="primary" shape="circle" icon={<FacebookOutlined />} size="large" className="bg-[#1877F2]" />
          <Button type="primary" shape="circle" icon={<TwitterOutlined />} size="large" className="bg-[#1DA1F2]" />
          <Button
            type="primary"
            shape="circle"
            icon={<InstagramOutlined />}
            size="large"
            className="bg-gradient-to-r from-[#833AB4] via-[#FD1D1D] to-[#FCAF45]"
          />
          <Button type="primary" shape="circle" icon={<LinkedinOutlined />} size="large" className="bg-[#0A66C2]" />
        </Space>
      </div>
    </Card>
  )
}

export default ContactInfo

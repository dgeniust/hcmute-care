import React, { useState, useRef } from "react";
import { Button, Divider, Radio, Steps, Card, Typography, message, Badge, Space, Tag } from "antd";
import { 
  HeartTwoTone, 
  CreditCardTwoTone, 
  SafetyCertificateTwoTone, 
  CheckCircleFilled,
  BankOutlined,
  CreditCardOutlined,
  LockOutlined,
  InfoCircleOutlined
} from "@ant-design/icons";
import {
  handleHttpStatusCode,
  notifyErrorWithCustomMessage,
  notifySuccessWithCustomMessage,
} from "../../../utils/notificationHelper";

const { Title, Text, Paragraph } = Typography;
const { Step } = Steps;

// Styles for payment methods
const paymentMethodStyle = {
  border: "1px solid #f0f0f0",
  borderRadius: "8px",
  padding: "16px",
  marginBottom: "12px",
  transition: "all 0.3s ease",
};

const selectedPaymentStyle = {
  ...paymentMethodStyle,
  borderColor: "#1890ff",
  backgroundColor: "#e6f7ff",
  boxShadow: "0 2px 8px rgba(0, 0, 0, 0.09)",
};

// Payment method logos
const VNPAY = () => (
  <div className="flex items-center justify-center w-12 h-12 bg-white rounded-md overflow-hidden">
    <svg width="40" height="40" viewBox="0 0 40 40" fill="none">
      <rect width="40" height="40" rx="4" fill="#0066FF" />
      <path d="M7 15.3L15.75 7L24.5 15.3H7Z" fill="white" />
      <path d="M32.9993 24.7L24.2493 33L15.4993 24.7H32.9993Z" fill="white" />
      <path d="M15.75 7L32.9993 24.7H24.5L7 15.3L15.75 7Z" fill="#fff" fillOpacity="0.5" />
    </svg>
  </div>
);

const PAYPAL = () => (
  <div className="flex items-center justify-center w-12 h-12 bg-white rounded-md overflow-hidden">
    <svg width="40" height="40" viewBox="0 0 40 40" fill="none">
      <rect width="40" height="40" rx="4" fill="#white" />
      <path d="M11.5 27H8.5L10 17H13C15.5 17 16.5 18.5 16 20.5C15.5 23 13 23 11.5 23H10.5L11.5 27Z" fill="#253B80" />
      <path d="M17.5 20.5C18 18.5 17 17 14.5 17H11.5L10 27H13L13.5 24H14.5C16 24 17 23 17.5 20.5ZM14 23H13L13.5 20H14.5C15.5 20 15.5 23 14 23Z" fill="#179BD7" />
      <path d="M24 20.5C24.5 18.5 23.5 17 21 17H18L16.5 27H19.5L20 24H21C22.5 24 23.5 23 24 20.5ZM20.5 23H19.5L20 20H21C22 20 22 23 20.5 23Z" fill="#253B80" />
      <path d="M30.5 17L28 27H25L27.5 17H30.5Z" fill="#179BD7" />
    </svg>
  </div>
);

const MOMO = () => (
  <div className="flex items-center justify-center w-12 h-12 bg-white rounded-md overflow-hidden">
    <svg width="40" height="40" viewBox="0 0 40 40" fill="none">
      <rect width="40" height="40" rx="4" fill="#AF258E" />
      <path d="M28 21C28 24.866 24.866 28 21 28C17.134 28 14 24.866 14 21C14 17.134 17.134 14 21 14C24.866 14 28 17.134 28 21Z" fill="white" />
      <path d="M23 20C23 21.105 22.105 22 21 22C19.895 22 19 21.105 19 20C19 18.895 19.895 18 21 18C22.105 18 23 18.895 23 20Z" fill="#AF258E" />
    </svg>
  </div>
);

const Payment_Booking = ({ bookingList, setCurrent }) => {
  const apiUrl = import.meta.env.VITE_API_BASE_URL;
  const ref1 = useRef(null);
  const ref2 = useRef(null);
  const [valueBanking, setValueBanking] = useState(2);
  const [messageApi, contextHolder] = message.useMessage();
  const appointmentId = localStorage.getItem("appointmentId");
  
  // Calculate total price
  const totalPrice = bookingList.reduce((sum, item) => {
    // Extract the number part and remove commas
    const priceString = item.price.replace(/[^\d]/g, '');
    return sum + (parseInt(priceString) || 0);
  }, 0);
  
  // Format the total price with comma separators and VND
  const formattedTotalPrice = new Intl.NumberFormat('vi-VN', {
    style: 'currency',
    currency: 'VND',
    minimumFractionDigits: 0
  }).format(totalPrice);

  const onChange = (e) => {
    setValueBanking(e.target.value);
  };

  const handlePayment = async () => {
    try {
      const payload = {
        appointmentId: appointmentId,
      };
      
      // Show loading message
      messageApi.loading({ content: 'Đang xử lý thanh toán...', key: 'payment' });
      
      const response = await fetch(`${apiUrl}v1/payments/vn-pay`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
        },
        body: JSON.stringify(payload),
      });
      
      if (!response.ok) {
        const errorText = await response.text();
        handleHttpStatusCode(response.status, "", errorText, messageApi);
        return;
      }
      
      const data = await response.json();
      
      // Kiểm tra response và redirect nếu thành công
      if (data) {
        if (data.status === 200 && data.data) {
          messageApi.success({
            content: 'Đặt khám thành công, đang chuyển hướng tới trang thanh toán',
            key: 'payment',
            duration: 2
          });
          
          setTimeout(() => {
            window.location.href = data.data;
          }, 1000);
        } else {
          messageApi.error({
            content: 'Không tìm thấy URL thanh toán trong response.',
            key: 'payment'
          });
        }
      }
    } catch (error) {
      console.error("Error during payment:", error);
      messageApi.error({
        content: 'Có lỗi xảy ra trong quá trình thanh toán. Vui lòng thử lại sau.',
        key: 'payment'
      });
    }
  };

  return (
    <div className="w-full py-6 bg-gray-50 space-y-6">
      {contextHolder}
      <div className="max-w-4xl mx-auto grid grid-cols-1 lg:grid-cols-3 gap-6 px-4">
        {/* Left column - Payment details */}
        <div className="lg:col-span-2 space-y-6">
          <Card bordered={false} className="shadow-sm">
            <div className="flex items-center mb-4">
              <HeartTwoTone twoToneColor="#eb2f96" className="text-xl mr-2" />
              <Title level={4} className="m-0 text-[#273c75]">
                Chi tiết đặt khám
              </Title>
            </div>
            
            <div ref={ref1} className="border rounded-md overflow-hidden">
              <div className="bg-gray-50 p-3 border-b">
                <Text strong className="text-[#273c75]">
                  Chuyên khoa đã chọn ({bookingList.length})
                </Text>
              </div>
              
              {bookingList.map((item, index) => (
                <div key={index} className="border-b last:border-b-0">
                  <div className="flex justify-between items-center px-4 py-3">
                    <div className="flex-1">
                      <Text>{item.specialty}</Text>
                    </div>
                    <div>
                      <Text strong className="text-[#273c75]">{item.price}</Text>
                    </div>
                  </div>
                </div>
              ))}
            </div>
            
            <Paragraph className="text-gray-500 mt-4 text-sm">
              <InfoCircleOutlined className="mr-1" />
              Vui lòng kiểm tra thông tin chuyên khoa trước khi tiến hành thanh toán.
            </Paragraph>
          </Card>
          
          <Card bordered={false} className="shadow-sm" ref={ref2}>
            <div className="flex items-center mb-4">
              <CreditCardTwoTone twoToneColor="#eb2f96" className="text-xl mr-2" />
              <Title level={4} className="m-0 text-[#273c75]">
                Phương thức thanh toán
              </Title>
            </div>
            
            <Radio.Group
              className="w-full"
              onChange={onChange}
              value={valueBanking}
            >
              <div className="space-y-3">
                <div 
                  style={valueBanking === 2 ? selectedPaymentStyle : paymentMethodStyle} 
                  className="hover:border-blue-400 cursor-pointer"
                  onClick={() => setValueBanking(2)}
                >
                  <Radio value={2} className="w-full">
                    <div className="flex items-center">
                      <VNPAY />
                      <div className="ml-3">
                        <Text strong className={valueBanking === 2 ? "text-blue-500" : ""}>
                          Ví điện tử VNPAY
                        </Text>
                        <div>
                          <Text type="secondary" className="text-xs">
                            Thanh toán an toàn qua VNPAY
                          </Text>
                        </div>
                        {valueBanking === 2 && (
                          <Tag color="blue" className="mt-1">
                            <CheckCircleFilled className="mr-1" /> Đã chọn
                          </Tag>
                        )}
                      </div>
                    </div>
                  </Radio>
                </div>
                
                <div 
                  style={valueBanking === 1 ? selectedPaymentStyle : paymentMethodStyle} 
                  className="hover:border-blue-400 cursor-pointer"
                  onClick={() => setValueBanking(1)}
                >
                  <Radio value={1} className="w-full">
                    <div className="flex items-center">
                      <MOMO />
                      <div className="ml-3">
                        <Text strong className={valueBanking === 1 ? "text-blue-500" : ""}>
                          Ví điện tử MoMo
                        </Text>
                        <div>
                          <Text type="secondary" className="text-xs">
                            Thanh toán nhanh chóng qua MoMo
                          </Text>
                        </div>
                        {valueBanking === 1 && (
                          <Tag color="blue" className="mt-1">
                            <CheckCircleFilled className="mr-1" /> Đã chọn
                          </Tag>
                        )}
                      </div>
                    </div>
                  </Radio>
                </div>
                
                <div 
                  style={valueBanking === 3 ? selectedPaymentStyle : paymentMethodStyle} 
                  className="hover:border-blue-400 cursor-pointer"
                  onClick={() => setValueBanking(3)}
                >
                  <Radio value={3} className="w-full">
                    <div className="flex items-center">
                      <PAYPAL />
                      <div className="ml-3">
                        <Text strong className={valueBanking === 3 ? "text-blue-500" : ""}>
                          PayPal
                        </Text>
                        <div>
                          <Text type="secondary" className="text-xs">
                            Thanh toán quốc tế an toàn
                          </Text>
                        </div>
                        {valueBanking === 3 && (
                          <Tag color="blue" className="mt-1">
                            <CheckCircleFilled className="mr-1" /> Đã chọn
                          </Tag>
                        )}
                      </div>
                    </div>
                  </Radio>
                </div>
              </div>
            </Radio.Group>
            
            <div className="mt-6 bg-blue-50 rounded-md p-4 border border-blue-100">
              <div className="flex items-start">
                <SafetyCertificateTwoTone twoToneColor="#52c41a" className="text-lg mt-0.5 mr-2" />
                <div>
                  <Text strong>Bảo mật thanh toán</Text>
                  <Paragraph className="text-sm text-gray-600 m-0">
                    Thông tin thanh toán của bạn được bảo mật hoàn toàn với chuẩn mã hóa SSL mới nhất.
                  </Paragraph>
                </div>
              </div>
            </div>
          </Card>
        </div>
        
        {/* Right column - Summary */}
        <div className="lg:col-span-1">
          <Card variant={false} className="shadow-sm sticky top-6">
            <Title level={4} className="text-[#273c75]">
              Tóm tắt đơn hàng
            </Title>
            
            <div className="border-t border-b py-4 my-4 space-y-3">
              <div className="flex justify-between">
                <Text>Tổng tiền dịch vụ</Text>
                <Text strong>{formattedTotalPrice}</Text>
              </div>
              <div className="flex justify-between">
                <Text>Phí giao dịch</Text>
                <Text>0 ₫</Text>
              </div>
            </div>
            
            <div className="flex justify-between mb-6">
              <Title level={5}>Tổng cộng</Title>
              <Title level={4} className="text-[#273c75]">
                {formattedTotalPrice}
              </Title>
            </div>
            
            <Button
              type="primary"
              size="large"
              className="w-full h-12 bg-[#273c75] hover:opacity-90 hover:bg-[#273c75]"
              onClick={handlePayment}
              icon={<LockOutlined />}
            >
              Thanh toán ngay
            </Button>
            
            <div className="flex justify-center mt-4">
              <Text type="secondary" className="text-xs text-center">
                Sau khi thanh toán thành công, bạn sẽ nhận được <Text strong>PHIẾU KHÁM BỆNH</Text> qua email.
              </Text>
            </div>
            
            <div className="flex justify-center gap-2 mt-4">
              <img src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRdsd562muI70chnyy415S5EKaZGKg5vV7u5SqujlgdKIcfALfTTHjTmj2qYJJUkSk4n1w&usqp=CAU" alt="Visa" className="h-6" />
              <img src="https://vudigital.co/wp-content/uploads/2022/12/logo-mastercard-da-thay-doi-nhu-the-nao-trong-hon-50-nam-2.webp" alt="Mastercard" className="h-6" />
              <img src="https://scontent.fsgn2-3.fna.fbcdn.net/v/t39.30808-6/202166185_2021396718013233_8499389898242103910_n.png?_nc_cat=107&ccb=1-7&_nc_sid=6ee11a&_nc_eui2=AeH8XoltyCZ--dGQhuNpH4XzKwdd0u08Nc0rB13S7Tw1zdEtemV9jTe_eKpNZ8Dv0nhSnbC4DQTmqx-eGY5E7Wkn&_nc_ohc=ygi4Y-8Wh6MQ7kNvwG5W0J6&_nc_oc=Adn5YLrKNZSvo70o301ZR51Ct89psqjp1SIGIhAtL3k2D5cizjDKQ5d0pm-9CUGVONdjyZ6ZDZ3Rn4xkOs2O1dQ8&_nc_zt=23&_nc_ht=scontent.fsgn2-3.fna&_nc_gid=ddhTInM2b6m46Vv7fpbRWw&oh=00_AfL5J9q0396iBkkb9Dh1zx9EF51aYtk7X8J5WCIMV1feXA&oe=68288C69" alt="VNPAY" className="h-6" />
              <img src="https://scontent.fsgn2-9.fna.fbcdn.net/v/t39.30808-6/352757343_1879233049125660_4727560855644771929_n.jpg?_nc_cat=1&ccb=1-7&_nc_sid=6ee11a&_nc_eui2=AeHzyYJ8clPR6GbQTBFT0Qxlp5MwGKmZw8unkzAYqZnDy6TCKGCj5tFrYly4xEyj6dFPUNsmXhnxx6t6uEHuR9Pz&_nc_ohc=aDVrOObkghAQ7kNvwGS6o6m&_nc_oc=AdlQfJ8J8ro5NifGdl4x7W5iQNl1GB-EaCMcmS5bbG5UR0W13kIo0JTDtcel6s9YRX6uS8f_jvDsojIP1C_vYWRf&_nc_zt=23&_nc_ht=scontent.fsgn2-9.fna&_nc_gid=0X40om5oHTLroge-y1sxQg&oh=00_AfLBmW_DjcdRnTpyqf-WpvzJUqRMkhFS-YLgJ6w8FsSc9A&oe=68289A35" alt="MoMo" className="h-6" />
              <img src="https://www.paypalobjects.com/webstatic/mktg/logo/pp_cc_mark_111x69.jpg" alt="PayPal" className="h-6" />
            </div>
          </Card>
        </div>
      </div>
    </div>
  );
};

export default Payment_Booking;
import React, { useState, useEffect } from 'react';
import { Result, Button, Card, Typography, Space, Divider } from 'antd';
import { CheckCircleFilled, CloseCircleFilled, HomeOutlined, CheckOutlined } from '@ant-design/icons';

const { Title, Text } = Typography;

// Component chính
const PaymentResultPage = () => {
  // State để lưu trữ thông tin từ URL params
  const [paymentParams, setPaymentParams] = useState({});
  
  // State để chuyển đổi giữa trang thành công và thất bại
  const [paymentStatus, setPaymentStatus] = useState('loading');
  
  // Hàm định dạng số tiền
  const formatAmount = (amount) => {
    if (!amount) return '';
    // Chuyển đổi từ số (VND) sang định dạng hiển thị
    const numAmount = parseInt(amount, 10);
    return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' })
      .format(numAmount / 100); // VNPay trả về số tiền đã nhân 100
  };
  
  // Hàm định dạng ngày giờ
  const formatDateTime = (dateStr) => {
    if (!dateStr) return '';
    // Format từ YYYYMMDDHHmmss thành DD/MM/YYYY HH:mm:ss
    const year = dateStr.substring(0, 4);
    const month = dateStr.substring(4, 6);
    const day = dateStr.substring(6, 8);
    const hour = dateStr.substring(8, 10);
    const minute = dateStr.substring(10, 12);
    const second = dateStr.substring(12, 14);
    return `${day}/${month}/${year} ${hour}:${minute}:${second}`;
  };
  
  // Lấy thông tin từ URL khi component được mount
  useEffect(() => {
    const urlParams = new URLSearchParams(window.location.search);
    const params = {};
    for (const [key, value] of urlParams.entries()) {
      params[key] = value;
    }
    setPaymentParams(params);
  
    // ✅ Xác định trạng thái thanh toán ngay từ params
    if (params.vnp_ResponseCode === '00' && params.vnp_TransactionStatus === '00') {
      setPaymentStatus('success');
    } else {
      setPaymentStatus('failure');
    }
  
    // 🎯 Vẫn gọi API phía sau để lấy chi tiết giao dịch và lưu localStorage
    const fetchPaymentData = async () => {
      const apiUrl = "http://localhost:8080/api/v1/payments/vnpay/return?" + urlParams.toString();
      try {
        const response = await fetch(apiUrl, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${localStorage.getItem('accessToken')}`
          }
        });
  
        const result = await response.json();
  
        if (response.ok && result?.status === 200) {
          localStorage.setItem('paymentData', JSON.stringify(result.data));
        } else {
          console.warn('Không tìm thấy chi tiết giao dịch từ API.');
        }
      } catch (error) {
        console.error('Lỗi khi gọi API thanh toán:', error);
      }
    };
  
    fetchPaymentData();
  }, []);
  
  
//   useEffect(() => {
//     const searchParams = window.location.search; // lấy phần ?vnp_Amount=...&vnp_...

//     const apiUrl = "http://localhost:8080/api/v1/payments/vnpay/return" + searchParams;
//     const fetchPaymentData = async () => {
//         try {
//             const response = await fetch(apiUrl, {
//                 method: 'GET',
//                 headers: {
//                     'Content-Type': 'application/json',
//                     Authorization: `Bearer ${localStorage.getItem('accessToken')}`
//                 },
//             });
        
//             if (!response.ok) {
//                 const errorText = await response.text();
//                 console.error("Error fetching payment data:", errorText);
        
//                 if (response.status === 404 && retryCount < 3) {
//                     setTimeout(() => fetchPaymentData(retryCount + 1), 1000);
//                     return;
//                 }
        
//                 handleHttpStatusCode(response.status, "", errorText, messageApi );
//                 return;
//             }
        
//             let data;
//             try {
//                 data = await response.json();
//             } catch (jsonErr) {
//                 console.error('Lỗi parse JSON:', jsonErr);
//                 notifyErrorWithCustomMessage('Lỗi xử lý dữ liệu từ máy chủ', messageApi);
//                 return;
//             }
        
//             console.log("Payment data:", data);
//             if (data && data.status === 200) {
//                 setPaymentData(data.data);
//                 notifySuccessWithCustomMessage('Thanh toán thành công', messageApi);
//             } else {
//                 notifyErrorWithCustomMessage('Không tìm thấy giao dịch hoặc giao dịch không hợp lệ', messageApi);
//             }
        
//         } catch (error) {
//             console.error('Lỗi mạng hoặc server:', error.message || error);
//             notifyErrorWithCustomMessage('Có lỗi xảy ra trong quá trình thanh toán', messageApi);
//         }
        
//     }
//     fetchPaymentData();
// }, []);
  
  // Hàm điều hướng về trang đã hoàn thành đặt chỗ
  const redirectToCompletedBooking = () => {
    // Trong dự án thực tế, sẽ sử dụng useNavigate hoặc window.location
    console.log('Đang chuyển hướng đến /completed-booking');
    window.location.href = '/completed-booking';
  };

  // Hàm điều hướng về trang chủ
  const goToHome = () => {
    window.location.href = '/';
  };

  // Hàm quay lại trang thanh toán
  const goToPayment = () => {
    window.location.href = '/payment';
  };

  // Xây dựng thông tin thanh toán từ params
  const paymentInfo = {
    orderId: paymentParams.vnp_TxnRef || '',
    amount: formatAmount(paymentParams.vnp_Amount) || '',
    time: formatDateTime(paymentParams.vnp_PayDate) || '',
    method: 'VNPay ' + (paymentParams.vnp_CardType || 'QR'),
    bankCode: paymentParams.vnp_BankCode || '',
    transactionId: paymentParams.vnp_TransactionNo || ''
  };

  // Component hiển thị thông tin thanh toán
  const PaymentDetails = () => (
    <Card className="mt-4 w-full">
      <Space direction="vertical" className="w-full">
        <div className="flex justify-between">
          <Text strong>Mã đơn hàng:</Text>
          <Text>{paymentInfo.orderId}</Text>
        </div>
        <div className="flex justify-between">
          <Text strong>Số tiền:</Text>
          <Text className="text-red-500 font-bold">{paymentInfo.amount}</Text>
        </div>
        <div className="flex justify-between">
          <Text strong>Thời gian:</Text>
          <Text>{paymentInfo.time}</Text>
        </div>
        <div className="flex justify-between">
          <Text strong>Phương thức:</Text>
          <Text>{paymentInfo.method}</Text>
        </div>
        <div className="flex justify-between">
          <Text strong>Ngân hàng:</Text>
          <Text>{paymentInfo.bankCode}</Text>
        </div>
        <div className="flex justify-between">
          <Text strong>Mã giao dịch:</Text>
          <Text>{paymentInfo.transactionId}</Text>
        </div>
      </Space>
    </Card>
  );

  return (
    <div className="min-h-screen bg-gray-50 flex flex-col items-center py-8 px-4 w-full">
      
      {/* Loading state */}
      {paymentStatus === 'loading' && (
        <Card className="w-full max-w-md shadow-md text-center">
          <Title level={3}>Đang xử lý kết quả thanh toán...</Title>
          <div className="py-6">Vui lòng đợi trong giây lát</div>
        </Card>
      )}

      {/* Trang thanh toán thành công */}
      {paymentStatus === 'success' && (
        <Card className="w-full max-w-md shadow-md">
          <Result
            icon={<CheckCircleFilled className="text-green-500 text-6xl" />}
            title={<Title level={3} className="text-green-500">Thanh toán thành công</Title>}
            subTitle="Cảm ơn bạn đã sử dụng dịch vụ của chúng tôi"
            extra={[
              <PaymentDetails key="details" />,
              <Divider key="divider" />,
              <div key="buttons" className="flex flex-col space-y-2">
                <Button 
                  type="primary" 
                  size="large" 
                  icon={<CheckOutlined />} 
                  onClick={redirectToCompletedBooking}
                  className="bg-green-500 hover:bg-green-600"
                  block
                >
                  Xác nhận và tiếp tục
                </Button>
                <Button 
                  type="default" 
                  size="large" 
                  icon={<HomeOutlined />} 
                  onClick={goToHome}
                  block
                >
                  Về trang chủ
                </Button>
              </div>
            ]}
          />
        </Card>
      )}

      {/* Trang thanh toán thất bại */}
      {paymentStatus === 'failure' && (
        <Card className="w-full max-w-md shadow-md">
          <Result
            icon={<CloseCircleFilled className="text-red-500 text-6xl" />}
            title={<Title level={3} className="text-red-500">Thanh toán thất bại</Title>}
            subTitle="Đã xảy ra lỗi trong quá trình thanh toán. Vui lòng thử lại."
            extra={[
              <Card key="error-info" className="mt-4 w-full bg-red-50">
                <Space direction="vertical" className="w-full">
                  <div className="flex justify-between">
                    <Text strong>Mã lỗi:</Text>
                    <Text type="danger">{paymentParams.vnp_ResponseCode || 'VNPAY-E005'}</Text>
                  </div>
                  <div className="flex justify-between">
                    <Text strong>Mô tả:</Text>
                    <Text type="danger">Giao dịch không thành công</Text>
                  </div>
                  <div className="flex justify-between">
                    <Text strong>Mã đơn hàng:</Text>
                    <Text>{paymentParams.vnp_TxnRef || ''}</Text>
                  </div>
                </Space>
              </Card>,
              <Divider key="divider" />,
              <div key="buttons" className="flex flex-col space-y-2">
                <Button 
                  type="primary" 
                  size="large" 
                  danger
                  onClick={goToPayment}
                  block
                >
                  Thử lại
                </Button>
                <Button 
                  type="default" 
                  size="large" 
                  icon={<HomeOutlined />} 
                  onClick={goToHome}
                  block
                >
                  Về trang chủ
                </Button>
              </div>
            ]}
          />
        </Card>
      )}
    </div>
  );
};

export default PaymentResultPage;
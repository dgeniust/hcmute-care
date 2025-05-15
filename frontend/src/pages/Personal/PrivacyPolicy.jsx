import React from 'react';
import { Typography, Divider, Layout, Breadcrumb, Alert } from 'antd';
import { LockOutlined, SafetyCertificateOutlined, FileProtectOutlined, TeamOutlined, CalendarOutlined, MailOutlined, PhoneOutlined } from '@ant-design/icons';

const { Title, Paragraph, Text } = Typography;
const { Content, Footer } = Layout;

export default function PrivacyPolicy() {
  const currentDate = new Date();
  const formattedDate = `${currentDate.getDate()}/${currentDate.getMonth() + 1}/${currentDate.getFullYear()}`;

  return (
    <div className="min-h-screen w-full bg-gray-50">
      <Content className="mx-auto w-full p-4">
        <div className="bg-white p-6 rounded-lg shadow-md">
          <div className="flex items-center mb-6">
            <FileProtectOutlined className="text-blue-600 mr-3 text-2xl" />
            <Title level={2} className="m-0">Chính Sách Bảo Mật</Title>
          </div>
          
          <Alert
            message="Cập nhật mới nhất"
            description={`Chính sách này được cập nhật lần cuối vào ngày ${formattedDate}`}
            type="info"
            showIcon
            className="mb-6"
          />
          
          <Paragraph className="text-gray-600 mb-6">
            Chúng tôi cam kết bảo vệ quyền riêng tư và thông tin cá nhân của bạn. Chính sách bảo mật này giải thích cách chúng tôi thu thập, sử dụng, chia sẻ và bảo vệ thông tin cá nhân của bạn khi bạn sử dụng dịch vụ đặt lịch khám bệnh của chúng tôi.
          </Paragraph>
          
          <Divider />
          
          <section className="mb-8">
            <div className="flex items-center mb-4">
              <LockOutlined className="text-blue-600 mr-2 text-xl" />
              <Title level={3} className="m-0">Thông Tin Chúng Tôi Thu Thập</Title>
            </div>
            <Paragraph className="text-gray-700">
              Khi bạn sử dụng dịch vụ đặt lịch khám bệnh của chúng tôi, chúng tôi có thể thu thập các loại thông tin sau:
            </Paragraph>
            <ul className="list-disc pl-6 text-gray-700 mb-4">
              <li className="mb-2">
                <Text strong>Thông tin cá nhân:</Text> Họ tên, ngày tháng năm sinh, giới tính, địa chỉ, email, số điện thoại.
              </li>
              <li className="mb-2">
                <Text strong>Thông tin y tế:</Text> Tiền sử bệnh, triệu chứng, dị ứng, thuốc đang sử dụng và các thông tin sức khỏe khác mà bạn cung cấp.
              </li>
              <li className="mb-2">
                <Text strong>Thông tin bảo hiểm y tế:</Text> Số thẻ bảo hiểm, loại bảo hiểm và các thông tin liên quan.
              </li>
              <li>
                <Text strong>Thông tin đặt lịch:</Text> Thời gian, ngày tháng, loại dịch vụ, bác sĩ mà bạn chọn.
              </li>
            </ul>
          </section>
          
          <section className="mb-8">
            <div className="flex items-center mb-4">
              <TeamOutlined className="text-blue-600 mr-2 text-xl" />
              <Title level={3} className="m-0">Mục Đích Sử Dụng Thông Tin</Title>
            </div>
            <Paragraph className="text-gray-700">
              Chúng tôi sử dụng thông tin của bạn để:
            </Paragraph>
            <ul className="list-disc pl-6 text-gray-700 mb-4">
              <li className="mb-2">Xử lý và xác nhận lịch hẹn khám bệnh của bạn</li>
              <li className="mb-2">Cung cấp dịch vụ chăm sóc sức khỏe phù hợp với nhu cầu của bạn</li>
              <li className="mb-2">Liên hệ với bạn về các thay đổi lịch hẹn, nhắc nhở hoặc thông báo quan trọng</li>
              <li className="mb-2">Xử lý thanh toán và yêu cầu bảo hiểm</li>
              <li className="mb-2">Cải thiện dịch vụ và trải nghiệm người dùng của chúng tôi</li>
              <li>Tuân thủ các yêu cầu pháp lý và quy định về y tế</li>
            </ul>
          </section>
          
          <section className="mb-8">
            <div className="flex items-center mb-4">
              <SafetyCertificateOutlined className="text-blue-600 mr-2 text-xl" />
              <Title level={3} className="m-0">Bảo Vệ Thông Tin</Title>
            </div>
            <Paragraph className="text-gray-700">
              Chúng tôi cam kết bảo vệ thông tin của bạn bằng cách:
            </Paragraph>
            <ul className="list-disc pl-6 text-gray-700 mb-4">
              <li className="mb-2">Sử dụng các biện pháp bảo mật kỹ thuật tiên tiến như mã hóa dữ liệu, tường lửa và các hệ thống phát hiện xâm nhập</li>
              <li className="mb-2">Giới hạn quyền truy cập vào thông tin cá nhân chỉ cho những nhân viên cần biết</li>
              <li className="mb-2">Đào tạo nhân viên về các biện pháp bảo mật và quy trình bảo vệ dữ liệu</li>
              <li className="mb-2">Thực hiện đánh giá bảo mật định kỳ và cập nhật các biện pháp bảo vệ khi cần thiết</li>
              <li>Tuân thủ các tiêu chuẩn bảo mật ngành y tế và quy định pháp luật hiện hành</li>
            </ul>
          </section>
          
          <section className="mb-8">
            <div className="flex items-center mb-4">
              <CalendarOutlined className="text-blue-600 mr-2 text-xl" />
              <Title level={3} className="m-0">Thời Gian Lưu Trữ</Title>
            </div>
            <Paragraph className="text-gray-700">
              Chúng tôi chỉ lưu trữ thông tin cá nhân của bạn trong thời gian cần thiết để đáp ứng các mục đích đã nêu trong chính sách này và theo yêu cầu của pháp luật. Thông tin y tế sẽ được lưu trữ theo quy định pháp luật về hồ sơ y tế.
            </Paragraph>
          </section>
          
          <section className="mb-8">
            <div className="flex items-center mb-4">
              <TeamOutlined className="text-blue-600 mr-2 text-xl" />
              <Title level={3} className="m-0">Chia Sẻ Thông Tin</Title>
            </div>
            <Paragraph className="text-gray-700">
              Chúng tôi có thể chia sẻ thông tin của bạn trong các trường hợp sau:
            </Paragraph>
            <ul className="list-disc pl-6 text-gray-700 mb-4">
              <li className="mb-2">Với bác sĩ, nhân viên y tế và các đơn vị cung cấp dịch vụ y tế liên quan đến việc chăm sóc sức khỏe của bạn</li>
              <li className="mb-2">Với công ty bảo hiểm để xử lý yêu cầu bảo hiểm (nếu được bạn cho phép)</li>
              <li className="mb-2">Với các nhà cung cấp dịch vụ thứ ba hỗ trợ chúng tôi trong việc vận hành dịch vụ (như công ty phát triển phần mềm, dịch vụ thanh toán)</li>
              <li className="mb-2">Khi có yêu cầu của cơ quan nhà nước có thẩm quyền</li>
              <li>Khi được sự đồng ý rõ ràng của bạn</li>
            </ul>
            <Paragraph className="text-gray-700">
              Chúng tôi không bán, cho thuê hoặc trao đổi thông tin cá nhân của bạn cho bên thứ ba vì mục đích tiếp thị hoặc thương mại.
            </Paragraph>
          </section>
          
          <section className="mb-8">
            <div className="flex items-center mb-4">
              <FileProtectOutlined className="text-blue-600 mr-2 text-xl" />
              <Title level={3} className="m-0">Quyền Của Bạn</Title>
            </div>
            <Paragraph className="text-gray-700">
              Bạn có các quyền sau đối với thông tin cá nhân của mình:
            </Paragraph>
            <ul className="list-disc pl-6 text-gray-700 mb-4">
              <li className="mb-2">Quyền truy cập và nhận bản sao thông tin cá nhân của bạn</li>
              <li className="mb-2">Quyền yêu cầu sửa đổi thông tin không chính xác hoặc không đầy đủ</li>
              <li className="mb-2">Quyền yêu cầu xóa thông tin trong một số trường hợp nhất định</li>
              <li className="mb-2">Quyền hạn chế hoặc phản đối việc xử lý thông tin của bạn</li>
              <li>Quyền khiếu nại với cơ quan bảo vệ dữ liệu nếu bạn tin rằng quyền riêng tư của bạn bị vi phạm</li>
            </ul>
          </section>
          
          <section className="mb-8">
            <div className="flex items-center mb-4">
              <MailOutlined className="text-blue-600 mr-2 text-xl" />
              <Title level={3} className="m-0">Liên Hệ</Title>
            </div>
            <Paragraph className="text-gray-700">
              Nếu bạn có câu hỏi hoặc yêu cầu liên quan đến chính sách bảo mật này hoặc thông tin cá nhân của bạn, vui lòng liên hệ với chúng tôi:
            </Paragraph>
            <div className="bg-gray-50 p-4 rounded-lg">
              <p className="flex items-center mb-2">
                <MailOutlined className="text-blue-600 mr-2" />
                <span>Email: privacy@medicalappointment.com</span>
              </p>
              <p className="flex items-center mb-2">
                <PhoneOutlined className="text-blue-600 mr-2" />
                <span>Điện thoại: 1900 xxxx</span>
              </p>
              <p className="flex items-center">
                <SafetyCertificateOutlined className="text-blue-600 mr-2" />
                <span>Bộ phận Bảo vệ Dữ liệu: 123 Đường ABC, Thành phố XYZ</span>
              </p>
            </div>
          </section>
          
          <section>
            <div className="flex items-center mb-4">
              <FileProtectOutlined className="text-blue-600 mr-2 text-xl" />
              <Title level={3} className="m-0">Thay Đổi Chính Sách</Title>
            </div>
            <Paragraph className="text-gray-700">
              Chúng tôi có thể cập nhật chính sách bảo mật này theo thời gian để phản ánh những thay đổi trong hoạt động của chúng tôi hoặc để tuân thủ các yêu cầu pháp lý mới. Chúng tôi sẽ thông báo cho bạn về các thay đổi quan trọng thông qua email hoặc thông báo trên trang web của chúng tôi. Việc bạn tiếp tục sử dụng dịch vụ sau khi thay đổi được thực hiện đồng nghĩa với việc bạn đồng ý với các thay đổi đó.
            </Paragraph>
          </section>
        </div>
      </Content>
      
      <Footer className="text-center bg-gray-100 p-6">
        <p className="text-gray-600">© {new Date().getFullYear()} Hệ thống Đặt lịch Khám bệnh. Tất cả các quyền được bảo lưu.</p>
      </Footer>
    </div>
  );
}
import React, { useEffect, useState } from "react";
import { message, Card, Typography, Spin, Avatar, Button, Divider, Badge } from "antd";
import { BellOutlined, HomeOutlined, UserOutlined, MessageOutlined, SearchOutlined } from '@ant-design/icons';
import TwitterPost from "../../../components/AdminComponents/MangePostEvent/TwitterPost";
import {
  handleHttpStatusCode,
  notifyErrorWithCustomMessage,
  notifySuccessWithCustomMessage,
} from "../../../utils/notificationHelper";

const { Title } = Typography;

const DoctorHomePage = () => {
  const [messageApi, contextHolder] = message.useMessage();
  const [listPost, setListPost] = useState([]);
  const [loading, setLoading] = useState(true);
  const apiUrl = import.meta.env.VITE_API_BASE_URL;

  useEffect(() => {
    const fetchPostApi = async () => {
      setLoading(true);
      const accessToken = localStorage.getItem("accessToken");
      try {
        const response = await fetch(`${apiUrl}v1/posts/list`, {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${accessToken}`,
          },
        });

        if (!response.ok) {
          const errorText = await response.text();
          handleHttpStatusCode(
            response.status,
            "",
            `Cập nhật thông tin thất bại: ${errorText || response.statusText}`,
            messageApi
          );
          return;
        }
        const data = await response.json();
        setListPost(data.data);
      } catch (e) {
        notifyErrorWithCustomMessage(
          "Lỗi kết nối khi load bài viết",
          messageApi
        );
        console.error("Error fetching posts:", e);
      } finally {
        setLoading(false);
      }
    };
    fetchPostApi();
  }, [messageApi]);

  // Mock data for suggested connections
  const suggestedDoctors = [
    { name: "BS. Nguyễn Văn A", specialty: "Tim mạch", avatar: null },
    { name: "BS. Trần Thị B", specialty: "Da liễu", avatar: null },
    { name: "BS. Lê Văn C", specialty: "Nhi khoa", avatar: null },
  ];

  return (
    <div className="min-h-screen bg-gray-50 w-full text-black">
      {contextHolder}
      
      {/* Navigation Bar */}
      <nav className="sticky top-0 z-10 bg-white shadow-md">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between h-16">
            <div className="flex items-center">
              <div className="flex-shrink-0 flex items-center">
                <span className="text-xl font-bold text-blue-600">Care Social</span>
              </div>
              <div className="hidden md:ml-6 md:flex md:space-x-4">
                <Button type="text" icon={<HomeOutlined />} className="text-blue-600 font-medium">
                  Trang chủ
                </Button>
                <Button type="text" icon={<MessageOutlined />} className="font-medium">
                  Tin nhắn
                </Button>
                <Button type="text" icon={<UserOutlined />} className="font-medium">
                  Hồ sơ
                </Button>
              </div>
            </div>
            <div className="flex items-center">
              <div className="relative mr-4">
                <input
                  type="text"
                  placeholder="Tìm kiếm"
                  className="pl-10 pr-4 py-2 border border-gray-300 rounded-full focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                />
                <SearchOutlined className="absolute left-3 top-2.5 text-gray-400" />
              </div>
              <Badge count={3} className="mr-4">
                <Button shape="circle" icon={<BellOutlined />} />
              </Badge>
              <Avatar size="medium" icon={<UserOutlined />} className="bg-blue-500" />
            </div>
          </div>
        </div>
      </nav>

      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-6">
        <div className="flex flex-col lg:flex-row gap-6">
          {/* Left Sidebar */}
          <div className="w-full lg:w-1/4">
            <div className="bg-white rounded-lg shadow overflow-hidden mb-6">
              <div className="p-4 bg-gradient-to-r from-blue-500 to-indigo-600 text-white">
                <div className="flex flex-col items-center">
                  <Avatar size={64} icon={<UserOutlined />} className="bg-white text-blue-600 mb-3" />
                  <h3 className="text-lg font-semibold">BS. Minh Đức</h3>
                  <p className="text-sm text-blue-100">Bác sĩ Tim mạch</p>
                </div>
              </div>
              <div className="p-4 border-t">
                <div className="flex justify-between text-sm mb-1">
                  <span>Bài viết</span>
                  <span className="font-semibold">24</span>
                </div>
                <div className="flex justify-between text-sm mb-1">
                  <span>Bình luận</span>
                  <span className="font-semibold">142</span>
                </div>
                <div className="flex justify-between text-sm">
                  <span>Kết nối</span>
                  <span className="font-semibold">318</span>
                </div>
              </div>
            </div>
            
            <div className="bg-white rounded-lg shadow overflow-hidden">
              <div className="p-4 border-b">
                <h3 className="font-medium text-gray-800">Chuyên mục sức khỏe</h3>
              </div>
              <ul className="p-2">
                {['Tim mạch', 'Dinh dưỡng', 'Sức khỏe tâm thần', 'Covid-19', 'Y học cổ truyền'].map((category, index) => (
                  <li key={index} className="py-2 px-3 hover:bg-gray-50 rounded-md cursor-pointer flex items-center">
                    <div className="w-2 h-2 rounded-full bg-blue-500 mr-3"></div>
                    <span>{category}</span>
                  </li>
                ))}
              </ul>
            </div>
          </div>

          {/* Main Content */}
          <div className="w-full lg:w-2/4">
            {/* Create Post Card */}
            <Card className="mb-6 shadow-sm hover:shadow-md transition-shadow duration-300">
              <div className="flex items-center space-x-4">
                <Avatar icon={<UserOutlined />} className="bg-blue-500" />
                <div className="bg-gray-100 rounded-full py-2 px-4 flex-1 cursor-pointer hover:bg-gray-200 transition-colors">
                  <span className="text-gray-500">Chia sẻ kiến thức y khoa của bạn...</span>
                </div>
              </div>
              <Divider className="my-3" />
              <div className="flex justify-around">
                <Button type="text" className="flex items-center text-gray-600">
                  <svg className="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z"></path>
                  </svg>
                  Hình ảnh
                </Button>
                <Button type="text" className="flex items-center text-gray-600">
                  <svg className="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M15 10l4.553-2.276A1 1 0 0121 8.618v6.764a1 1 0 01-1.447.894L15 14M5 18h8a2 2 0 002-2V8a2 2 0 00-2-2H5a2 2 0 00-2 2v8a2 2 0 002 2z"></path>
                  </svg>
                  Video
                </Button>
                <Button type="text" className="flex items-center text-gray-600">
                  <svg className="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"></path>
                  </svg>
                  Tài liệu
                </Button>
              </div>
            </Card>

            {/* Posts Feed */}
            {loading ? (
              <div className="flex justify-center items-center h-64 bg-white rounded-lg shadow-sm p-6">
                <Spin size="large" />
              </div>
            ) : listPost.length === 0 ? (
              <Card className="text-center p-8 shadow-md rounded-lg">
                <div className="py-12">
                  <svg className="w-16 h-16 mx-auto text-gray-300" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="1" d="M19 11H5m14 0a2 2 0 012 2v6a2 2 0 01-2 2H5a2 2 0 01-2-2v-6a2 2 0 012-2m14 0V9a2 2 0 00-2-2M5 11V9a2 2 0 012-2m0 0V5a2 2 0 012-2h6a2 2 0 012 2v2M7 7h10"></path>
                  </svg>
                  <p className="text-gray-500 mt-4">Chưa có bài viết nào để hiển thị.</p>
                  <Button type="primary" className="mt-4 bg-blue-500">Tạo bài viết đầu tiên</Button>
                </div>
              </Card>
            ) : (
              <div className="space-y-6">
                {listPost.map((post, index) => (
                  <Card
                    key={index}
                    className="shadow-sm hover:shadow-md transition-shadow duration-300 rounded-lg overflow-hidden"
                  >
                    <TwitterPost
                      id={post.id}
                      header={post.header}
                      content={post.content || "Không có nội dung"}
                      doc={post.doc}
                      postImages={post.postImages}
                    />
                  </Card>
                ))}
              </div>
            )}
          </div>

          {/* Right Sidebar */}
          <div className="w-full lg:w-1/4">
            {/* Suggested Connections */}
            <div className="bg-white rounded-lg shadow overflow-hidden mb-6">
              <div className="p-4 border-b">
                <h3 className="font-medium text-gray-800">Kết nối gợi ý</h3>
              </div>
              <div className="p-2">
                {suggestedDoctors.map((doctor, index) => (
                  <div key={index} className="flex items-center justify-between p-2 hover:bg-gray-50">
                    <div className="flex items-center">
                      <Avatar icon={<UserOutlined />} className="bg-blue-100 text-blue-600" />
                      <div className="ml-3">
                        <p className="text-sm font-medium">{doctor.name}</p>
                        <p className="text-xs text-gray-500">{doctor.specialty}</p>
                      </div>
                    </div>
                    <Button size="small" type="primary" className="bg-blue-500">
                      Kết nối
                    </Button>
                  </div>
                ))}
                <div className="px-4 py-3 text-center">
                  <Button type="link" className="text-blue-500 text-sm">
                    Xem thêm
                  </Button>
                </div>
              </div>
            </div>

            {/* Trending Topics */}
            <div className="bg-white rounded-lg shadow overflow-hidden mb-6">
              <div className="p-4 border-b">
                <h3 className="font-medium text-gray-800">Chủ đề nổi bật</h3>
              </div>
              <div className="p-3">
                {['COVID-19 cập nhật mới', 'Dinh dưỡng cho trẻ em', 'Sức khỏe tim mạch', 'Y tế số Việt Nam', 'Thuốc mới điều trị tiểu đường'].map((topic, index) => (
                  <div key={index} className="py-2 px-1">
                    <p className="text-sm font-medium text-gray-800 hover:text-blue-600 cursor-pointer">
                      #{topic.replace(/\s+/g, '')}
                    </p>
                    <p className="text-xs text-gray-500">{100 - index * 15} bài viết</p>
                  </div>
                ))}
              </div>
            </div>

            {/* Recent News */}
            <div className="bg-white rounded-lg shadow overflow-hidden">
              <div className="p-4 border-b">
                <h3 className="font-medium text-gray-800">Tin tức y khoa</h3>
              </div>
              <div className="p-3">
                {['Nghiên cứu mới về bệnh tim mạch', 'Hội nghị y học diễn ra tại Hà Nội', 'Cập nhật phương pháp điều trị mới'].map((news, index) => (
                  <div key={index} className="py-3 border-b last:border-0">
                    <p className="text-sm font-medium hover:text-blue-600 cursor-pointer">{news}</p>
                    <p className="text-xs text-gray-500 mt-1">2 giờ trước</p>
                  </div>
                ))}
                <div className="pt-2 text-center">
                  <Button type="link" className="text-blue-500 text-sm">
                    Xem tất cả tin tức
                  </Button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default DoctorHomePage;
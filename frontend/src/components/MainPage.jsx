import React, { useState, useEffect } from "react";
import DragCards from '../components/HomePage/DragCard';
import { ArrowRightOutlined, RightOutlined, LeftOutlined } from '@ant-design/icons';
import { Carousel, Form, Select, Button, Card, Typography, Input } from 'antd';
import '../css/mainpage.css';
import InfiniteScrollingCarousel from "./HomePage/InfiniteScrollingCarousel";
import BodyContent from "./HomePage/BodyContent";
import GeneratePictureOnMouse from "./HomePage/GeneratePictureOnMouse";
import ContactPage from "../components/HomePage/ContactPage";

const { Title } = Typography;
const { Option } = Select;
const { TextArea } = Input;

const NewsCard = ({ header, description, image }) => (
  <div className="p-2">
    <div
      className="bg-red rounded-lg shadow-md overflow-hidden hover:shadow-xl transition-shadow cursor-pointer h-[300px] w-full"
    >
      <img src={image} alt={header} className="w-full h-48 object-cover" />
      <div className="p-4">
        <h3 className="text-lg font-semibold mb-2">{header}</h3>
        <p className="text-gray-600 line-clamp-2">{description}</p>
      </div>
    </div>
  </div>
);

const MainPage = () => {
  const [news, setNews] = useState([]);
  const [quote, setQuote] = useState("");
  const [form] = Form.useForm();
  const [loading, setLoading] = useState(false);

  // Ánh xạ mood với từ khóa tích cực
  const moodKeywords = {
    happy: "happiness",
    tired: "motivational",
    lost: "inspirational",
  };

  useEffect(() => {
    const handlePost = async () => {
      try {
        const response = await fetch('http://localhost:8080/api/v1/posts?page=0&size=6&sort=doc&direction=desc', {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
          },
        });
        if (!response.ok) {
          const errorText = await response.text();
          console.error('Error fetching posts:', errorText);
          return;
        }
        const data = await response.json();
        if (data && data.data.content.length > 0) {
          const postData = data.data.content.map((item) => ({
            id: item.id,
            header: item.header,
            content: item.content,
            postImages: item.postImages,
          }));
          setNews(postData);
          console.log("Data post -------------------", data.data.content);
        } else {
          setNews([]);
          console.log("No data available");
        }
        console.log(data);
      } catch (error) {
        console.error('Error fetching posts:', error);
      }
    };
    handlePost();
  }, []);

  useEffect(() => {
    console.log("News data updated:", news);
  }, [news]);

  // Handle quote generation
  const handleGenerateQuote = async (values) => {
    setLoading(true);
    try {
      const mood = values.mood;
      const keyword = moodKeywords[mood];
      const response = await fetch(`https://api.quotable.io/random?tags=${keyword}`);
      const data = await response.json();
      setQuote(data.content ? `"${data.content}" — ${data.author || "Unknown"}` : "Hãy thử lại sau!");
    } catch (error) {
      console.error("Error fetching quote:", error);
      setQuote("Hãy thử lại sau!");
    }
    setLoading(false);
  };

  return (
    <div className="w-full h-full relative z-99 space-y-8">
      <div className="hello-content w-full h-fit max-w-screen rounded-xl ">
        <GeneratePictureOnMouse />
      </div>
      <div className="w-full h-full min-h-screen flex items-center justify-center">
        <BodyContent />
      </div>
      <div className="w-full h-fit">
        <div className="flex items-center text-black justify-center">
          <h1 className="font-bold text-3xl text-black">Chữa lành tâm hồn</h1>
        </div>
        <DragCards />
      </div>
      <div className="w-full h-fit shadow-lg rounded-lg">
        <div className="flex items-center text-black justify-center">
          <h1 className="font-bold text-3xl text-black">Các dịch vụ HCMUTE CARE</h1>
        </div>
        <InfiniteScrollingCarousel />
      </div>
      <div className="w-full h-fit">
        <ContactPage />
      </div>
      <div className="w-full h-fit flex flex-col items-center justify-center">
        <div className="flex items-center justify-center">
          <h1 className="font-bold text-3xl text-black">Tâm trạng của bạn hôm nay như thế nào?</h1>
        </div>
        <Card className="w-full mx-auto mt-4 shadow-lg">
          <Form
            form={form}
            layout="vertical"
            onFinish={handleGenerateQuote}
            className="flex flex-col items-center"
          >
            <Form.Item
              name="mood"
              label="Tâm trạng"
              rules={[{ required: true, message: "Vui lòng chọn tâm trạng!" }]}
              className="w-full max-w-md"
            >
              <Select size="large" placeholder="Chọn tâm trạng của bạn">
                <Option value="happy">😊 Vui</Option>
                <Option value="tired">😴 Không có sức sống</Option>
                <Option value="lost">🌫️ Vô định</Option>
              </Select>
            </Form.Item>
            <Form.Item
              name="emotion"
              label="Cảm xúc hiện tại"
              rules={[{ required: true, message: "Vui lòng mô tả cảm xúc của bạn!" }]}
              className="w-full max-w-md"
            >
              <TextArea rows={3} placeholder="Bạn đang cảm thấy như thế nào?" />
            </Form.Item>
            <Form.Item
              name="reason"
              label="Lý do"
              rules={[{ required: true, message: "Vui lòng chia sẻ lý do!" }]}
              className="w-full max-w-md"
            >
              <TextArea rows={3} placeholder="Điều gì khiến bạn cảm thấy như vậy?" />
            </Form.Item>
            <Form.Item
              name="desire"
              label="Mong muốn"
              rules={[{ required: true, message: "Vui lòng chia sẻ điều bạn mong muốn!" }]}
              className="w-full max-w-md"
            >
              <TextArea rows={3} placeholder="Bạn mong muốn điều gì ngay lúc này?" />
            </Form.Item>
            <Form.Item>
              <Button
                type="primary"
                htmlType="submit"
                size="large"
                loading={loading}
                className="mt-4 min-w-[150px]"
              >
                Tạo Quote
              </Button>
            </Form.Item>
          </Form>
          {quote && (
            <div className="text-center p-4 bg-blue-50 rounded-lg mt-4">
              <p className="text-lg italic text-gray-700">{quote}</p>
            </div>
          )}
        </Card>
      </div>
      <div className="w-full h-fit z-0">
        <div className="flex items-center text-black justify-between mt-8">
          <h1 className="font-bold text-3xl pl-8">Tin tức nổi bật</h1>
          <p className="pr-16 hover:text-sky-600 cursor-pointer">
            Xem thêm <ArrowRightOutlined />
          </p>
        </div>
        <div className="px-4 py-4 w-full absolute mx-auto">
          <Carousel
            autoplay
            autoplaySpeed={3000}
            arrows
            prevArrow={<LeftOutlined />}
            nextArrow={<RightOutlined />}
            slidesToShow={3}
            slidesToScroll={1}
            responsive={[
              { breakpoint: 1024, settings: { slidesToShow: 2 } },
              { breakpoint: 768, settings: { slidesToShow: 1 } },
            ]}
          >
            {news.length > 0 ? (
              news.map((item) => (
                <NewsCard
                  key={item.id}
                  header={item.header}
                  description={item.content}
                  image={item.postImages?.[0]?.imageUrl || null}
                />
              ))
            ) : (
              <div>Không có tin tức</div>
            )}
          </Carousel>
        </div>
      </div>
    </div>
  );
};

export default MainPage;
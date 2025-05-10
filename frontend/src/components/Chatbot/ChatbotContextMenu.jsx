import React, { useState, useEffect } from 'react';
import { Modal, message, Spin, Button } from 'antd';
import {MessageOutlined} from '@ant-design/icons';
import '../../css/chatbot.css';

const ChatbotContextMenu = () => {
  const [isMenuVisible, setIsMenuVisible] = useState(false);
  const [menuPosition, setMenuPosition] = useState({ x: 0, y: 0 });
  const [chatResponse, setChatResponse] = useState('');
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [loading, setLoading] = useState(false);
    const [isHovered, setIsHovered] = useState(false);
  const [mounted, setMounted] = useState(false);

  useEffect(() => {
    setMounted(true);
  }, []);
  useEffect(() => {
    const handleContextMenu = (e) => {
      const selectedText = window.getSelection().toString().trim();
      if (selectedText) {
        e.preventDefault(); // Chặn menu chuột phải mặc định của Chrome
        setIsMenuVisible(true);
        setMenuPosition({ x: e.pageX, y: e.pageY });
      } else {
        setIsMenuVisible(false);
      }
    };

    const handleClick = () => {
      setIsMenuVisible(false);
    };

    document.addEventListener('contextmenu', handleContextMenu);
    document.addEventListener('click', handleClick);

    return () => {
      document.removeEventListener('contextmenu', handleContextMenu);
      document.removeEventListener('click', handleClick);
    };
  }, []);

  const askChatbot = async () => {
    const selectedText = window.getSelection().toString().trim();
    if (!selectedText) {
      message.warning('Vui lòng bôi đen văn bản trước khi hỏi chatbot');
      return;
    }

    try {
      setLoading(true);
      setIsModalOpen(true);
      
      // Giả lập thời gian phản hồi
      setTimeout(() => {
        // Thay bằng API chatbot thật (Google Gemini, OpenAI, etc.)
        setChatResponse(`Bạn đã hỏi về "${selectedText}". Hiện chatbot của chúng tôi chưa được đưa vào hoạt động.`);
        setLoading(false);
      }, 1500);
      
      // Ví dụ API thật:
      // const response = await fetch('https://your-chatbot-api', {
      //   method: 'POST',
      //   headers: {
      //     'Content-Type': 'application/json',
      //     'Authorization': 'Bearer YOUR_API_KEY',
      //   },
      //   body: JSON.stringify({ query: selectedText }),
      // });
      // const data = await response.json();
      // setChatResponse(data.response || 'Không có phản hồi từ chatbot');
    } catch (error) {
      console.error('Error calling chatbot:', error);
      setChatResponse('Lỗi khi gọi chatbot. Vui lòng thử lại.');
      setLoading(false);
    }
    setIsMenuVisible(false);
  };

  const handleModalClose = () => {
    setIsModalOpen(false);
    setChatResponse('');
  };

  // Xử lý hiển thị văn bản có định dạng (đoạn, danh sách)
  const formatResponse = (text) => {
    if (!text) return null;
    
    // Tách văn bản thành các đoạn
    const paragraphs = text.split('\n\n');
    
    return paragraphs.map((paragraph, index) => {
      // Xử lý danh sách đánh số
      if (/^\d+\.\s/.test(paragraph)) {
        const listItems = paragraph.split(/\n(?=\d+\.\s)/).map((item, i) => (
          <li key={i} className="mb-2">{item.replace(/^\d+\.\s/, '')}</li>
        ));
        return <ol key={index} className="list-decimal pl-5 my-4">{listItems}</ol>;
      }
      // Xử lý danh sách dấu đầu dòng
      else if (/^-\s/.test(paragraph)) {
        const listItems = paragraph.split(/\n(?=-\s)/).map((item, i) => (
          <li key={i} className="mb-2">{item.replace(/^-\s/, '')}</li>
        ));
        return <ul key={index} className="list-disc pl-5 my-4">{listItems}</ul>;
      } 
      // Đoạn văn thông thường
      else {
        return <p key={index} className="mb-4">{paragraph}</p>;
      }
    });
  };

  return (
    <>
      {isMenuVisible && (
        <div
          className="context-menu"
          style={{ left: menuPosition.x, top: menuPosition.y }}
        >
          {/* <button 
            onClick={askChatbot}
            className="px-4 py-2 bg-purple-100 text-purple-700 hover:bg-purple-200 rounded-md transition-all font-medium"
          >
            Hỏi chatbot
          </button> */}
          <div className="relative">
            <button
                className="flex items-center justify-center gap-2 px-4 py-2 font-medium rounded-lg shadow-md transition-all duration-300 transform text-white"
                style={{
                backgroundImage: 'linear-gradient(to right, #f56565, #ecc94b, #48bb78, #4299e1, #9f7aea)',
                backgroundSize: '200% 100%',
                backgroundPosition: isHovered ? 'right center' : 'left center',
                transition: 'all 0.8s ease',
                transform: isHovered ? 'scale(1.05)' : 'scale(1)',
                boxShadow: isHovered ? '0 10px 15px -3px rgba(0, 0, 0, 0.1), 0 4px 6px -2px rgba(0, 0, 0, 0.05)' : '0 4px 6px -1px rgba(0, 0, 0, 0.1)'
                }}
                onMouseEnter={() => setIsHovered(true)}
                onMouseLeave={() => setIsHovered(false)}
                onClick={askChatbot}
            >
                <MessageOutlined 
                style={{ 
                    animation: isHovered ? 'pulse 2s infinite' : 'none',
                    fontSize: '18px'
                }} 
                />
                <span className="text-base font-medium">Hỏi ChatBot</span>
            </button>
            
            {/* Rainbow underline effect */}
            {/* <div 
                className="absolute bottom-0 left-0 h-1 rounded-full"
                style={{
                backgroundImage: 'linear-gradient(to right, #f56565, #ecc94b, #48bb78, #4299e1, #9f7aea)',
                width: isHovered ? '100%' : '0%',
                transition: 'width 0.5s ease'
                }}
            /> */}
            </div>
        </div>
      )}
      <Modal
        title={<div className="text-xl font-semibold text-gray-800">Trả lời từ Chatbot</div>}
        open={isModalOpen}
        onOk={handleModalClose}
        onCancel={handleModalClose}
        width={700}
        style={{ maxHeight: '60vh', overflow: 'auto', top: 20 }}
        footer={[
          <div key="footer" className="flex justify-between items-center">
            <div className="text-gray-500 text-sm">
              Powered by AI Assistant
            </div>
            <div>
              <Button
                type="primary"
                onClick={handleModalClose}
              >
                Đóng
              </Button>
            </div>
          </div>
        ]}
      >
        <div className="claude-response">
          {loading ? (
            <div className="flex flex-col items-center justify-center py-12">
              <Spin size="large" />
              <p className="mt-4 text-gray-600">Đang suy nghĩ...</p>
            </div>
          ) : (
            <div className="p-6 rounded-lg bg-white border border-gray-100 shadow-sm">
              {formatResponse(chatResponse)}
            </div>
          )}
        </div>
      </Modal>
    </>
  );
};

export default ChatbotContextMenu;
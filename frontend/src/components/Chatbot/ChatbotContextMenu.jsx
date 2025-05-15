import React, { useState, useEffect } from 'react';
import { Modal, message, Spin, Button } from 'antd';
import { MessageOutlined } from '@ant-design/icons';
import '../../css/chatbot.css';
import { GoogleGenerativeAI } from '@google/generative-ai';
import ReactMarkdown from 'react-markdown';
import axios from 'axios';

const ChatbotContextMenu = () => {
  const [isMenuVisible, setIsMenuVisible] = useState(false);
  const [menuPosition, setMenuPosition] = useState({ x: 0, y: 0 });
  const [chatResponse, setChatResponse] = useState('');
  const [imageUrl, setImageUrl] = useState('');
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [loading, setLoading] = useState(false);
  const [isHovered, setIsHovered] = useState(false);
  const [mounted, setMounted] = useState(false);

  const geminiApiKey = import.meta.env.VITE_GEMINI_API_KEY;
  const stabilityApiKey = import.meta.env.VITE_STABILITY_API_KEY;
  const genAI = new GoogleGenerativeAI(geminiApiKey);
  useEffect(() => {
    setMounted(true);
  }, []);

  useEffect(() => {
    const handleContextMenu = (e) => {
      const selectedText = window.getSelection().toString().trim();
      if (selectedText) {
        e.preventDefault();
        setIsMenuVisible(true);
        setMenuPosition({ x: e.pageX, y: e.pageY });
      } else {
        setIsMenuVisible(false);
      }
    };

    const handleClick = () => setIsMenuVisible(false);

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

  setLoading(true);
  setIsModalOpen(true);

  try {
    // Text generation with Gemini
    const model = genAI.getGenerativeModel({ model: 'gemini-2.0-flash' });
    // Step 1: Translate selectedText to English using Gemini
    const translationPrompt = `Translate the following Vietnamese text to English: "${selectedText}"`;
    const textPrompt = `
      Hãy cung cấp thông tin chi tiết bằng tiếng Việt về "${selectedText}" trong lĩnh vực y tế, sử dụng định dạng markdown. Nội dung cần bao gồm:
      - **Mô tả ngắn gọn**: Tóm tắt về chủ đề.
      - **Chi tiết**: Các thông tin quan trọng (mục đích, quy trình, rủi ro, lưu ý).
      - **Định dạng**: Sử dụng tiêu đề (**text**), danh sách (- hoặc 1.), và chữ nghiêng (*text*) khi phù hợp.
      - Trả lời ngắn gọn, rõ ràng, và chuyên nghiệp.
    `;
    const textResult = await model.generateContent(textPrompt);
    const textData = textResult.response.text();
    setChatResponse(textData || 'Không có phản hồi từ chatbot');

    // Image generation with Stability AI
    // const imagePrompt = `A professional illustration of ${translatedText} in the medical field, e.g., a CT scan machine, abdominal-pelvic anatomy image, or related medical procedure. Clear, realistic style, suitable for medical documentation.`;
    
    // const formData = new FormData();
    // formData.append('prompt', imagePrompt);
    // formData.append('height', '512');
    // formData.append('width', '512');
    // formData.append('output_format', 'png');

    // console.log('FormData entries:');
    // for (let [key, value] of formData.entries()) {
    //   console.log(`${key}: ${value}`);
    // }

    // const response = await fetch(
    //   'https://api.stability.ai/v2beta/stable-image/generate/ultra',
    //   {
    //     method: 'POST',
    //     headers: {
    //       Authorization: `Bearer ${stabilityApiKey}`,
    //       Accept: 'application/json',
    //     },
    //     body: formData,
    //   }
    // );

    // if (!response.ok) {
    //   const errorData = await response.json();
    //   console.error('API Error:', errorData);
    //   throw new Error(`HTTP error! Status: ${response.status}, Details: ${JSON.stringify(errorData)}`);
    // }

    // const data = await response.json();
    // console.log('API Response:', data); // Log full response for debugging

    // if (!data.image) {
    //   const reason = data.finish_reason || 'Unknown';
    //   throw new Error(`No image returned from Stability AI. Finish reason: ${reason}`);
    // }

    // const imageBase64 = `data:image/png;base64,${data.image}`;
    // setImageUrl(imageBase64);

    // console.log('Text:', textData);
    // console.log('Image:', imageBase64);
  } catch (error) {
    console.error('Error calling APIs:', error);
    setChatResponse(`Lỗi khi gọi chatbot hoặc tạo hình ảnh: ${error.message}. Vui lòng kiểm tra API key và nội dung prompt.`);
    setImageUrl('');
  } finally {
    setLoading(false);
  }
  setIsMenuVisible(false);
};

  const handleModalClose = () => {
    setIsModalOpen(false);
    setChatResponse('');
    setImageUrl('');
  };

  const formatResponse = (text) => {
    if (!text) return null;
    const processedText = text
      .replace(/\*\*(.*?)\*\*/g, '**$1**')
      .replace(/\*(.*?)\*/g, '*$1*')
      .replace(/^\s*-\s/gm, '- ')
      .replace(/^\s*\d+\.\s/gm, (match) => match.trim());
    return (
      <ReactMarkdown
        components={{
          p: ({ children }) => <p className="mb-4">{children}</p>,
          ul: ({ children }) => <ul className="list-disc pl-5 my-4">{children}</ul>,
          ol: ({ children }) => <ol className="list-decimal pl-5 my-4">{children}</ol>,
          li: ({ children }) => <li className="mb-2">{children}</li>,
          strong: ({ children }) => <strong className="font-semibold">{children}</strong>,
          em: ({ children }) => <em className="italic">{children}</em>,
        }}
      >
        {processedText}
      </ReactMarkdown>
    );
  };

  return (
    <>
      {isMenuVisible && (
        <div className="context-menu" style={{ left: menuPosition.x, top: menuPosition.y }}>
          <div className="relative">
            <button
              className="flex items-center justify-center gap-2 px-4 py-2 font-medium rounded-lg shadow-md transition-all duration-300 transform text-white"
              style={{
                backgroundImage: 'linear-gradient(to right, #f56565, #ecc94b, #48bb78, #4299e1, #9f7aea)',
                backgroundSize: '200% 100%',
                backgroundPosition: isHovered ? 'right center' : 'left center',
                transition: 'all 0.8s ease',
                transform: isHovered ? 'scale(1.05)' : 'scale(1)',
                boxShadow: isHovered
                  ? '0 10px 15px -3px rgba(0, 0, 0, 0.1), 0 4px 6px -2px rgba(0, 0, 0, 0.05)'
                  : '0 4px 6px -1px rgba(0, 0, 0, 0.1)',
              }}
              onMouseEnter={() => setIsHovered(true)}
              onMouseLeave={() => setIsHovered(false)}
              onClick={askChatbot}
            >
              <MessageOutlined
                style={{
                  animation: isHovered ? 'pulse 2s infinite' : 'none',
                  fontSize: '18px',
                }}
              />
              <span className="text-base font-medium">Hỏi ChatBot</span>
            </button>
          </div>
        </div>
      )}
      <Modal
        title={<div className="text-xl font-semibold text-gray-800">Trả lời từ Chatbot</div>}
        open={isModalOpen}
        onOk={handleModalClose}
        onCancel={handleModalClose}
        width={1000}
        centered
        style={{ maxHeight: '90vh', overflow: 'auto', top: 20 }}
        footer={[
          <div key="footer" className="flex justify-between items-center">
            <div className="text-gray-500 text-sm">Powered by AI Assistant</div>
            <Button type="primary" onClick={handleModalClose}>
              Đóng
            </Button>
          </div>,
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
              {imageUrl && (
                <div className="mb-4">
                  <img
                    src={imageUrl}
                    alt="Generated illustration"
                    className="max-w-full h-auto rounded-lg"
                  />
                </div>
              )}
              {formatResponse(chatResponse)}
            </div>
          )}
        </div>
      </Modal>
    </>
  );
};

export default ChatbotContextMenu;
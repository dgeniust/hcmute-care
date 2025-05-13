import React, { useState, useRef, useEffect } from 'react';
import { Input, Button, Avatar, Spin, Typography, notification, Card, Badge, Tooltip, Drawer, Divider, Switch, Select, List } from 'antd';
import { 
  SendOutlined, 
  RobotOutlined, 
  UserOutlined, 
  SettingOutlined, 
  DeleteOutlined, 
  InfoCircleOutlined, 
  HistoryOutlined,
  SoundOutlined,
  CopyOutlined,
  MenuOutlined,
  PlusOutlined,
} from '@ant-design/icons';
import axios from 'axios';
import moment from 'moment';
import 'moment/locale/vi';

const { Title, Text, Paragraph } = Typography;
const { Option } = Select;

const GEMINI_API_KEY = '';
const GEMINI_API_URL = 'https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent';

const ChatbotService = () => {
  // States
  const [messages, setMessages] = useState([
    {
      type: 'bot',
      content: 'Xin chào! Tôi là UTECARE, trợ lý ảo của Trường Đại học Sư phạm Kỹ thuật TP.HCM. Tôi có thể giúp gì cho bạn hôm nay?',
      time: new Date(),
    },
  ]);
  const [inputValue, setInputValue] = useState('');
  const [isTyping, setIsTyping] = useState(false);
  const [theme, setTheme] = useState('light');
  const [drawerVisible, setDrawerVisible] = useState(false);
  const [conversationHistory, setConversationHistory] = useState([]);
  const [currentConversation, setCurrentConversation] = useState('Cuộc trò chuyện mới');
  const [speechEnabled, setSpeechEnabled] = useState(false);
  const [language, setLanguage] = useState('vi');
  const [suggestedQuestions, setSuggestedQuestions] = useState([
    'Giới thiệu về trường ĐHSPKT TP.HCM',
    'Quy trình nhập học như thế nào?',
    'Các ngành đào tạo của trường?',
    'Học phí các ngành năm 2025',
  ]);

  // Refs
  const messagesEndRef = useRef(null);
  const inputRef = useRef(null);

  // Effects
  useEffect(() => {
    scrollToBottom();
  }, [messages]);

  useEffect(() => {
    inputRef.current?.focus();
    moment.locale(language);
  }, [language]);

  useEffect(() => {
    const browserLang = navigator.language.includes('vi') ? 'vi' : 'en';
    setLanguage(browserLang);
    moment.locale(browserLang);
  }, []);

  // Functions
  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  };

  const fetchGeminiResponse = async (message) => {
    try {
      const response = await axios.post(
        `${GEMINI_API_URL}?key=${GEMINI_API_KEY}`,
        {
          contents: [
            {
              parts: [{ text: message }],
            },
          ],
        },
        {
          headers: { 'Content-Type': 'application/json' },
        }
      );
      return response.data.candidates[0].content.parts[0].text;
    } catch (error) {
      console.error('Error calling Gemini API:', error);
      throw new Error(language === 'vi' ? 
        'Không thể lấy phản hồi từ Gemini API.' : 
        'Could not fetch response from Gemini API.');
    }
  };

  const handleSend = async () => {
    if (!inputValue.trim()) return;

    const userMessage = {
      type: 'user',
      content: inputValue,
      time: new Date(),
    };

    setMessages((prev) => [...prev, userMessage]);
    setInputValue('');
    setIsTyping(true);

    try {
      const response = await fetchGeminiResponse(inputValue);
      const botMessage = {
        type: 'bot',
        content: response,
        time: new Date(),
      };
      setMessages((prev) => [...prev, botMessage]);
      
      if (speechEnabled) {
        const speech = new SpeechSynthesisUtterance(response);
        speech.lang = language === 'vi' ? 'vi-VN' : 'en-US';
        window.speechSynthesis.speak(speech);
      }
      
      generateSuggestedQuestions(response);
    } catch (error) {
      notification.error({
        message: language === 'vi' ? 'Lỗi kết nối' : 'Connection Error',
        description: error.message || (language === 'vi' ? 
          'Không thể kết nối với dịch vụ. Vui lòng thử lại sau.' : 
          'Could not connect to the service. Please try again later.'),
      });
    } finally {
      setIsTyping(false);
    }
  };

  const handleKeyDown = (e) => {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault();
      handleSend();
    }
  };

  const formatTime = (date) => {
    return moment(date).fromNow();
  };

  const clearChat = () => {
    setMessages([
      {
        type: 'bot',
        content: language === 'vi' ? 
          'Lịch sử trò chuyện đã được xóa. Tôi có thể giúp gì cho bạn?' : 
          'Chat history has been cleared. How can I help you?',
        time: new Date(),
      },
    ]);
  };

  const toggleTheme = () => {
    setTheme(theme === 'light' ? 'dark' : 'light');
  };

  const toggleSpeech = () => {
    setSpeechEnabled(!speechEnabled);
    if (!speechEnabled) {
      notification.info({
        message: language === 'vi' ? 'Đã bật đọc văn bản' : 'Text-to-speech enabled',
        description: language === 'vi' ? 
          'UTECARE sẽ đọc câu trả lời cho bạn' : 
          'UTECARE will read responses aloud',
      });
    } else {
      window.speechSynthesis.cancel();
    }
  };

  const saveConversation = () => {
    const newConversation = {
      id: Date.now(),
      title: currentConversation,
      messages: [...messages],
      date: new Date(),
    };
    setConversationHistory([newConversation, ...conversationHistory]);
    notification.success({
      message: language === 'vi' ? 'Đã lưu cuộc trò chuyện' : 'Conversation saved',
      description: language === 'vi' ? 
        'Cuộc trò chuyện đã được lưu vào lịch sử' : 
        'The conversation has been saved to history',
    });
  };

  const loadConversation = (conversation) => {
    setMessages([...conversation.messages]);
    setCurrentConversation(conversation.title);
    setDrawerVisible(false);
  };

  const copyToClipboard = (text) => {
    navigator.clipboard.writeText(text);
    notification.success({
      message: language === 'vi' ? 'Đã sao chép' : 'Copied',
      description: language === 'vi' ? 
        'Nội dung đã được sao chép vào clipboard' : 
        'Content has been copied to clipboard',
    });
  };

  const changeLanguage = (value) => {
    setLanguage(value);
    moment.locale(value);
    if (messages.length === 1 && messages[0].type === 'bot') {
      setMessages([{
        type: 'bot',
        content: value === 'vi' ? 
          'Xin chào! Tôi là UTECARE, trợ lý ảo của Trường Đại học Sư phạm Kỹ thuật TP.HCM. Tôi có thể giúp gì cho bạn hôm nay?' : 
          'Hello! I am UTECARE, the virtual assistant of Ho Chi Minh City University of Technology and Education. How can I help you today?',
        time: new Date(),
      }]);
    }
  };

  const generateSuggestedQuestions = (response) => {
    const rotatedQuestions = [...suggestedQuestions.slice(1), suggestedQuestions[0]];
    setSuggestedQuestions(rotatedQuestions);
  };

  const askSuggestedQuestion = (question) => {
    setInputValue(question);
    setTimeout(() => {
      handleSend();
    }, 100);
  };

  return (
    <div className={`flex flex-col h-screen w-full p-4 ${theme === 'dark' ? 'bg-gray-900' : 'bg-gray-100'}`}>
      {/* Header */}
      <div className={`flex items-center justify-between p-4 ${theme === 'dark' ? 'bg-gray-800' : 'bg-white'} shadow-md`}>
        <div className="flex items-center w-full">
          <Button 
            icon={<MenuOutlined />} 
            type="text" 
            onClick={() => setDrawerVisible(true)}
            className={theme === 'dark' ? 'text-gray-300 hover:text-white' : 'text-gray-600'}
            style={{ marginRight: 16, display: 'flex', justifyContent:'space-around', alignItems:'center' }} // Hide for now
          />
          <Badge dot status="success">
            <Avatar 
              size={40} 
              icon={<RobotOutlined />} 
              className="bg-indigo-600" 
            />
          </Badge>
          <div className='flex flex-row items-center justify-around gap-8'>
            <Title level={4} className={`m-0 ${theme === 'dark' ? 'text-white' : 'text-gray-800'}`}>
              UTECARE
            </Title>
            <Text className={theme === 'dark' ? 'text-gray-400' : 'text-gray-600'}>
              {language === 'vi' ? 'Trợ lý ảo thông minh' : 'Smart Virtual Assistant'}
            </Text>
          </div>
        </div>
        <div className="flex space-x-2">
          <Tooltip title={language === 'vi' ? 'Ngôn ngữ' : 'Language'}>
            <Select 
              value={language} 
              onChange={changeLanguage} 
              className="w-24"
              variant={false}
            >
              <Option value="vi">Tiếng Việt</Option>
              <Option value="en">English</Option>
            </Select>
          </Tooltip>
          <Tooltip title={language === 'vi' ? 'Đọc văn bản' : 'Text to speech'}>
            <Button 
              icon={<SoundOutlined />} 
              type={speechEnabled ? "primary" : "text"} 
              onClick={toggleSpeech}
              className={theme === 'dark' && !speechEnabled ? 'text-gray-300 hover:text-white' : ''}
            />
          </Tooltip>
          <Tooltip title={language === 'vi' ? 'Giao diện tối' : 'Dark mode'}>
            <Button 
              icon={<SettingOutlined />} 
              type="text" 
              onClick={toggleTheme}
              className={theme === 'dark' ? 'text-gray-300 hover:text-white' : ''}
            />
          </Tooltip>
          <Tooltip title={language === 'vi' ? 'Xóa cuộc trò chuyện' : 'Clear chat'}>
            <Button 
              icon={<DeleteOutlined />} 
              type="text" 
              onClick={clearChat}
              className={theme === 'dark' ? 'text-gray-300 hover:text-white' : ''}
            />
          </Tooltip>
        </div>
      </div>

      {/* Chat Messages */}
      <div className={`flex-1 p-4 overflow-y-auto ${theme === 'dark' ? 'bg-gray-900' : 'bg-gray-100'}`}>
        <div className="w-full mx-auto space-y-3">
          {messages.map((message, index) => (
            <div
              key={index}
              className={`flex ${message.type === 'user' ? 'justify-end' : 'justify-start'}`}
            >
              <div className={`flex ${message.type === 'user' ? 'flex-row-reverse' : 'flex-row'} items-start max-w-[70%]`}>
                <Avatar
                  size={36}
                  icon={message.type === 'user' ? <UserOutlined /> : <RobotOutlined />}
                  className={message.type === 'user' ? 'bg-blue-500 ml-2' : 'bg-indigo-600 mr-2'}
                />
                <div className="group relative">
                  <div
                    className={`p-3 rounded-2xl ${
                      message.type === 'user'
                        ? 'bg-blue-500 text-white'
                        : theme === 'dark' ? 'bg-gray-800 text-gray-200' : 'bg-white text-gray-800'
                    } shadow-sm`}
                  >
                    <Paragraph className={`text-sm mb-1 ${message.type === 'user' ? 'text-white' : theme === 'dark' ? 'text-gray-200' : 'text-gray-800'}`}>
                      {message.content}
                    </Paragraph>
                    <Text className={`text-xs ${message.type === 'user' ? 'text-blue-100' : theme === 'dark' ? 'text-gray-400' : 'text-gray-500'}`}>
                      {formatTime(message.time)}
                    </Text>
                  </div>
                  {message.type === 'bot' && (
                    <Button
                      type="text"
                      size="small"
                      icon={<CopyOutlined />}
                      onClick={() => copyToClipboard(message.content)}
                      className="absolute top-1 right-1 opacity-0 group-hover:opacity-100 text-gray-400 hover:text-gray-600"
                    />
                  )}
                </div>
              </div>
            </div>
          ))}
          {isTyping && (
            <div className="flex justify-start">
              <div className="flex items-start max-w-[70%]">
                <Avatar size={36} icon={<RobotOutlined />} className="bg-indigo-600 mr-2" />
                <div className={`p-3 rounded-2xl ${theme === 'dark' ? 'bg-gray-800' : 'bg-white'} shadow-sm`}>
                  <Spin size="small" />
                  <Text className={`ml-2 text-sm ${theme === 'dark' ? 'text-gray-300' : 'text-gray-600'}`}>
                    {language === 'vi' ? 'UTECARE đang nhập...' : 'UTECARE is typing...'}
                  </Text>
                </div>
              </div>
            </div>
          )}
          <div ref={messagesEndRef} />
        </div>
      </div>

      {/* Suggested Questions */}
      {suggestedQuestions.length > 0 && messages.length > 0 && !isTyping && (
        <div className={`px-4 py-2 ${theme === 'dark' ? 'bg-gray-800' : 'bg-white'} border-t`}>
          <div className="w-full mx-auto">
            <Text className={`text-xs font-medium ${theme === 'dark' ? 'text-gray-400' : 'text-gray-500'}`}>
              {language === 'vi' ? 'Gợi ý:' : 'Suggestions:'}
            </Text>
            <div className="flex flex-wrap gap-2 mt-1">
              {suggestedQuestions.map((question, index) => (
                <Button 
                  key={index} 
                  size="small" 
                  className={`rounded-full ${theme === 'dark' ? 'bg-gray-700 text-gray-300' : 'bg-gray-100 text-gray-700'}`}
                  onClick={() => askSuggestedQuestion(question)}
                >
                  {question}
                </Button>
              ))}
            </div>
          </div>
        </div>
      )}

      {/* Input Area */}
      <div className={`p-4 ${theme === 'dark' ? 'bg-gray-800' : 'bg-white'} border-t shadow-md`}>
        <div className="w-full mx-auto">
          <div className="flex items-center space-x-3">
            <Input.TextArea
              ref={inputRef}
              value={inputValue}
              onChange={(e) => setInputValue(e.target.value)}
              onKeyDown={handleKeyDown}
              placeholder={language === 'vi' ? "Nhập tin nhắn của bạn..." : "Type your message..."}
              autoSize={{ minRows: 1, maxRows: 3 }}
              className={`rounded-lg py-2 px-4 ${theme === 'dark' ? 'bg-gray-700 border-gray-600 text-white' : 'bg-gray-100 border-gray-300 text-gray-800'} focus:border-indigo-500`}
            />
            <Button
              type="primary"
              shape="circle"
              icon={<SendOutlined />}
              onClick={handleSend}
              className="bg-indigo-600 hover:bg-indigo-700"
              disabled={!inputValue.trim() || isTyping}
              size="large"
            />
          </div>
          <div className="flex justify-between mt-2 text-xs">
            <Text className={theme === 'dark' ? 'text-gray-400' : 'text-gray-600'}>
              {language === 'vi' ? 'UTECARE sử dụng công nghệ Gemini 1.5 Flash' : 'UTECARE is powered by Gemini 1.5 Flash'}
            </Text>
            <Button 
              type="link"
              size="small"
              onClick={saveConversation}
              className={theme === 'dark' ? 'text-gray-400' : 'text-gray-600'}
            >
              {language === 'vi' ? 'Lưu cuộc trò chuyện' : 'Save conversation'}
            </Button>
          </div>
        </div>
      </div>

      {/* Drawer for Settings & History */}
      <Drawer
        title={
          <div className="flex items-center">
            <HistoryOutlined className="mr-2" />
            {language === 'vi' ? 'Lịch sử & Cài đặt' : 'History & Settings'}
          </div>
        }
        placement="left"
        onClose={() => setDrawerVisible(false)}
        open={drawerVisible}
        width={300}
        bodyStyle={{ padding: 0 }}
        className={theme === 'dark' ? 'bg-gray-800' : ''}
      >
        <div className={`p-4 ${theme === 'dark' ? 'bg-gray-800' : 'bg-white'}`}>
          <Title level={5} className={theme === 'dark' ? 'text-white' : 'text-gray-800'}>
            {language === 'vi' ? 'Cài đặt' : 'Settings'}
          </Title>
          <div className="space-y-4 mt-4">
            <div className="flex justify-between items-center">
              <Text className={theme === 'dark' ? 'text-gray-300' : 'text-gray-600'}>
                {language === 'vi' ? 'Giao diện tối' : 'Dark mode'}
              </Text>
              <Switch checked={theme === 'dark'} onChange={toggleTheme} />
            </div>
            <div className="flex justify-between items-center">
              <Text className={theme === 'dark' ? 'text-gray-300' : 'text-gray-600'}>
                {language === 'vi' ? 'Đọc văn bản' : 'Text to speech'}
              </Text>
              <Switch checked={speechEnabled} onChange={toggleSpeech} />
            </div>
            <div className="flex justify-between items-center">
              <Text className={theme === 'dark' ? 'text-gray-300' : 'text-gray-600'}>
                {language === 'vi' ? 'Ngôn ngữ' : 'Language'}
              </Text>
              <Select 
                value={language} 
                onChange={changeLanguage} 
                className="w-24"
                bordered={false}
              >
                <Option value="vi">Tiếng Việt</Option>
                <Option value="en">English</Option>
              </Select>
            </div>
          </div>
        </div>
        <Divider className={theme === 'dark' ? 'bg-gray-700' : 'bg-gray-200'} />
        <div className={`p-4 ${theme === 'dark' ? 'bg-gray-800' : 'bg-white'}`}>
          <div className="flex justify-between items-center">
            <Title level={5} className={theme === 'dark' ? 'text-white' : 'text-gray-800'}>
              {language === 'vi' ? 'Cuộc trò chuyện đã lưu' : 'Saved conversations'}
            </Title>
            <Button 
              type="text" 
              icon={<PlusOutlined />} 
              onClick={saveConversation}
              className={theme === 'dark' ? 'text-gray-300' : 'text-gray-600'}
            />
          </div>
          <List
            dataSource={conversationHistory}
            locale={{ emptyText: language === 'vi' ? 'Chưa có cuộc trò chuyện nào' : 'No saved conversations' }}
            renderItem={item => (
              <List.Item 
                className={`p-2 rounded-md cursor-pointer hover:${theme === 'dark' ? 'bg-gray-700' : 'bg-gray-100'}`}
                onClick={() => loadConversation(item)}
              >
                <div className="flex items-center justify-between w-full">
                  <div className="flex-1">
                    <Text className={theme === 'dark' ? 'text-white' : 'text-gray-800'} ellipsis>{item.title}</Text>
                    <Text type="secondary" className="block text-xs">
                      {moment(item.date).format('LLL')}
                    </Text>
                  </div>
                  <Button 
                    type="text" 
                    size="small" 
                    icon={<DeleteOutlined />} 
                    onClick={(e) => {
                      e.stopPropagation();
                      setConversationHistory(conversationHistory.filter(conv => conv.id !== item.id));
                    }}
                    className={theme === 'dark' ? 'text-gray-400 hover:text-gray-200' : 'text-gray-600'}
                  />
                </div>
              </List.Item>
            )}
          />
        </div>
        <div className={`p-4 text-center ${theme === 'dark' ? 'bg-gray-800 text-gray-400' : 'bg-white text-gray-500'}`}>
          <InfoCircleOutlined className="mr-1" />
          <Text className="text-xs">
            {language === 'vi' ? 'UTECARE v2.0 - 2025' : 'UTECARE v2.0 - 2025'}
          </Text>
        </div>
      </Drawer>
    </div>
  );
};

export default ChatbotService;
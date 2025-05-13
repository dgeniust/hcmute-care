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
  CloseOutlined,
  BulbOutlined,
  GlobalOutlined
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

  // Initialize language based on browser settings
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
      
      // Text-to-speech if enabled
      if (speechEnabled) {
        const speech = new SpeechSynthesisUtterance(response);
        speech.lang = language === 'vi' ? 'vi-VN' : 'en-US';
        window.speechSynthesis.speak(speech);
      }
      
      // Generate new suggested questions based on context
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
      window.speechSynthesis.cancel(); // Stop any ongoing speech
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
    
    // Update welcome message based on language
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
    // In a real app, this would be based on context and AI analysis
    // For this demo, we'll just rotate through predefined questions
    const rotatedQuestions = [...suggestedQuestions.slice(1), suggestedQuestions[0]];
    setSuggestedQuestions(rotatedQuestions);
  };

  const askSuggestedQuestion = (question) => {
    setInputValue(question);
    setTimeout(() => {
      handleSend();
    }, 100);
  };

  // Render
  return (
    <div className={`flex flex-col h-screen w-full ${theme === 'dark' ? 'bg-gray-900' : 'bg-gradient-to-br from-blue-50 to-indigo-100'}`}>
      {/* Header */}
      <Card className={`flex items-center justify-between p-4 ${theme === 'dark' ? 'bg-gray-800 border-gray-700' : 'bg-white'} shadow-md rounded-none`}>
        <div className="flex items-center space-x-3">
          <Button 
            icon={<MenuOutlined />} 
            type="text" 
            onClick={() => setDrawerVisible(true)}
            className={theme === 'dark' ? 'text-gray-300 hover:text-white' : ''}
          />
          <Badge dot status="success">
            <Avatar 
              size={48} 
              icon={<RobotOutlined />} 
              className="bg-gradient-to-r from-blue-500 to-purple-600" 
            />
          </Badge>
          <div>
            <Title level={4} className={`m-0 ${theme === 'dark' ? 'text-white' : 'text-gray-800'}`}>
              UTECARE
            </Title>
            <Text type="secondary" className={theme === 'dark' ? 'text-gray-400' : ''}>
              {language === 'vi' ? 'Trợ lý ảo thông minh' : 'Smart Virtual Assistant'}
            </Text>
          </div>
        </div>
        <div className="flex space-x-2">
          <Select 
            defaultValue={language} 
            onChange={changeLanguage} 
            className="w-24"
            dropdownMatchSelectWidth={false}
          >
            <Option value="vi">Tiếng Việt</Option>
            <Option value="en">English</Option>
          </Select>
          <Tooltip title={language === 'vi' ? 'Đọc văn bản' : 'Text to speech'}>
            <Button 
              icon={<SoundOutlined />} 
              type={speechEnabled ? "primary" : "text"} 
              onClick={toggleSpeech} 
              className={theme === 'dark' && !speechEnabled ? 'text-gray-300 hover:text-white' : ''}
            />
          </Tooltip>
          <Tooltip title={language === 'vi' ? 'Cài đặt' : 'Settings'}>
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
      </Card>

      {/* Chat Messages */}
      <div className={`flex-1 p-4 md:p-6 overflow-y-auto ${theme === 'dark' ? 'bg-gray-900' : 'bg-gradient-to-br from-blue-50 to-indigo-100'}`}>
        <div className="max-w-4xl mx-auto space-y-4">
          {messages.map((message, index) => (
            <div
              key={index}
              className={`flex ${message.type === 'user' ? 'justify-end' : 'justify-start'} animate-fade-in`}
            >
              <div className={`flex ${message.type === 'user' ? 'flex-row-reverse' : 'flex-row'} items-start max-w-xs sm:max-w-sm md:max-w-md lg:max-w-lg`}>
                <Avatar
                  size={40}
                  icon={message.type === 'user' ? <UserOutlined /> : <RobotOutlined />}
                  className={message.type === 'user' ? 'bg-blue-500 ml-3' : 'bg-gradient-to-r from-blue-500 to-purple-600 mr-3'}
                />
                <div className="group relative">
                  <Card
                    className={`p-3 rounded-2xl ${
                      message.type === 'user'
                        ? `${theme === 'dark' ? 'bg-blue-700' : 'bg-blue-500'} text-white border-none`
                        : `${theme === 'dark' ? 'bg-gray-800' : 'bg-white'} ${theme === 'dark' ? 'text-white' : 'text-gray-800'} border-gray-200`
                    } shadow-sm`}
                  >
                    <Paragraph className={`text-sm mb-2 ${message.type === 'user' ? 'text-white' : theme === 'dark' ? 'text-gray-200' : 'text-gray-800'}`}>
                      {message.content}
                    </Paragraph>
                    <div className={`text-xs ${message.type === 'user' ? 'text-blue-100' : theme === 'dark' ? 'text-gray-400' : 'text-gray-500'}`}>
                      {formatTime(message.time)}
                    </div>
                  </Card>
                  {message.type === 'bot' && (
                    <div className="absolute top-2 right-2 opacity-0 group-hover:opacity-100 transition-opacity duration-200">
                      <Button 
                        type="text" 
                        size="small" 
                        icon={<CopyOutlined />} 
                        onClick={() => copyToClipboard(message.content)}
                        className="text-gray-400 hover:text-gray-600"
                      />
                    </div>
                  )}
                </div>
              </div>
            </div>
          ))}
          {isTyping && (
            <div className="flex justify-start animate-fade-in">
              <div className="flex flex-row items-start max-w-xs sm:max-w-sm md:max-w-md lg:max-w-lg">
                <Avatar size={40} icon={<RobotOutlined />} className="bg-gradient-to-r from-blue-500 to-purple-600 mr-3" />
                <Card className={`p-3 rounded-2xl ${theme === 'dark' ? 'bg-gray-800' : 'bg-white'} shadow-sm border-gray-200`}>
                  <div className="flex items-center">
                    <Spin size="small" />
                    <Text className={`ml-2 text-sm ${theme === 'dark' ? 'text-gray-300' : 'text-gray-600'}`}>
                      {language === 'vi' ? 'UTECARE đang nhập...' : 'UTECARE is typing...'}
                    </Text>
                  </div>
                </Card>
              </div>
            </div>
          )}
          <div ref={messagesEndRef} />
        </div>
      </div>

      {/* Suggested Questions */}
      {suggestedQuestions.length > 0 && messages.length > 0 && !isTyping && (
        <div className={`px-4 py-2 ${theme === 'dark' ? 'bg-gray-800 border-gray-700' : 'bg-white border-gray-200'} border-t`}>
          <div className="max-w-4xl mx-auto">
            <Text className={`text-xs font-medium ${theme === 'dark' ? 'text-gray-400' : 'text-gray-500'}`}>
              {language === 'vi' ? 'Gợi ý:' : 'Suggestions:'}
            </Text>
            <div className="flex flex-wrap gap-2 mt-1">
              {suggestedQuestions.map((question, index) => (
                <Button 
                  key={index} 
                  size="small" 
                  className={`rounded-full ${theme === 'dark' ? 'bg-gray-700 text-gray-300 border-gray-600' : 'bg-gray-100 text-gray-700'}`}
                  onClick={() => askSuggestedQuestion(question)}
                >
                  {question}
                </Button>
              ))}
            </div>
          </div>
        </div>
      )}

      {/* Footer with Input */}
      <Card className={`p-4 ${theme === 'dark' ? 'bg-gray-800 border-gray-700' : 'bg-white border-gray-200'} border-t rounded-none shadow-md`}>
        <div className="max-w-4xl mx-auto">
          <div className="flex items-center space-x-3">
            <Input.TextArea
              ref={inputRef}
              value={inputValue}
              onChange={(e) => setInputValue(e.target.value)}
              onKeyDown={handleKeyDown}
              placeholder={language === 'vi' ? "Nhập tin nhắn của bạn..." : "Type your message..."}
              autoSize={{ minRows: 1, maxRows: 3 }}
              className={`rounded-full py-2 px-4 ${theme === 'dark' ? 'bg-gray-700 border-gray-600 text-white' : 'bg-gray-100 border-gray-300'} focus:border-blue-500`}
            />
            <Button
              type="primary"
              shape="circle"
              icon={<SendOutlined />}
              onClick={handleSend}
              className="flex items-center justify-center bg-gradient-to-r from-blue-500 to-purple-600 hover:from-blue-600 hover:to-purple-700"
              disabled={!inputValue.trim() || isTyping}
              size="large"
            />
          </div>
          <div className="flex justify-between mt-2">
            <Text type="secondary" className={`text-xs ${theme === 'dark' ? 'text-gray-400' : 'text-gray-600'}`}>
              {language === 'vi' ? 'UTECARE sử dụng công nghệ Gemini 1.5 Flash' : 'UTECARE is powered by Gemini 1.5 Flash'}
            </Text>
            <Button 
              type="link" 
              size="small" 
              className={`text-xs px-0 ${theme === 'dark' ? 'text-gray-400' : 'text-gray-600'}`}
              onClick={saveConversation}
            >
              {language === 'vi' ? 'Lưu cuộc trò chuyện' : 'Save conversation'}
            </Button>
          </div>
        </div>
      </Card>

      {/* Settings & History Drawer */}
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
        width={320}
        bodyStyle={{ padding: 0 }}
        headerStyle={{ borderBottom: `1px solid ${theme === 'dark' ? '#303030' : '#f0f0f0'}` }}
        className={theme === 'dark' ? 'bg-gray-800 text-white' : ''}
      >
        <div className={`p-4 ${theme === 'dark' ? 'bg-gray-800 text-white' : ''}`}>
          <Title level={5} className={theme === 'dark' ? 'text-white' : ''}>
            {language === 'vi' ? 'Cài đặt' : 'Settings'}
          </Title>
          
          <div className="flex justify-between items-center mt-4">
            <Text className={theme === 'dark' ? 'text-gray-300' : ''}>
              {language === 'vi' ? 'Giao diện tối' : 'Dark mode'}
            </Text>
            <Switch checked={theme === 'dark'} onChange={toggleTheme} />
          </div>
          
          <div className="flex justify-between items-center mt-3">
            <Text className={theme === 'dark' ? 'text-gray-300' : ''}>
              {language === 'vi' ? 'Đọc văn bản' : 'Text to speech'}
            </Text>
            <Switch checked={speechEnabled} onChange={toggleSpeech} />
          </div>
          
          <div className="flex justify-between items-center mt-3">
            <Text className={theme === 'dark' ? 'text-gray-300' : ''}>
              {language === 'vi' ? 'Ngôn ngữ' : 'Language'}
            </Text>
            <Select 
              value={language} 
              onChange={changeLanguage} 
              className="w-24"
              dropdownMatchSelectWidth={false}
            >
              <Option value="vi">Tiếng Việt</Option>
              <Option value="en">English</Option>
            </Select>
          </div>
        </div>
        
        <Divider className={theme === 'dark' ? 'bg-gray-700' : ''} />
        
        <div className={`px-4 ${theme === 'dark' ? 'bg-gray-800' : ''}`}>
          <div className="flex justify-between items-center">
            <Title level={5} className={theme === 'dark' ? 'text-white' : ''}>
              {language === 'vi' ? 'Cuộc trò chuyện đã lưu' : 'Saved conversations'}
            </Title>
            <Button 
              type="text" 
              icon={<PlusOutlined />} 
              onClick={saveConversation}
              className={theme === 'dark' ? 'text-gray-300' : ''}
            />
          </div>
          
          <List
            className={`mt-2 ${theme === 'dark' ? 'text-gray-300' : ''}`}
            dataSource={conversationHistory}
            locale={{ emptyText: language === 'vi' ? 'Chưa có cuộc trò chuyện nào' : 'No saved conversations' }}
            renderItem={item => (
              <List.Item 
                className={`px-2 py-2 rounded-md cursor-pointer hover:${theme === 'dark' ? 'bg-gray-700' : 'bg-gray-100'}`}
                onClick={() => loadConversation(item)}
              >
                <div className="flex items-center justify-between w-full">
                  <div className="flex-1 mr-2">
                    <Text className={theme === 'dark' ? 'text-white' : ''} ellipsis>{item.title}</Text>
                    <Text type="secondary" className="block text-xs">
                      {moment(item.date).format('LLL')}
                    </Text>
                  </div>
                  <Button 
                    type="text" 
                    size="small" 
                    icon={<DeleteOutlined />} 
                    className={theme === 'dark' ? 'text-gray-400 hover:text-gray-200' : ''}
                    onClick={(e) => {
                      e.stopPropagation();
                      setConversationHistory(conversationHistory.filter(conv => conv.id !== item.id));
                    }}
                  />
                </div>
              </List.Item>
            )}
          />
        </div>
        
        <div className={`absolute bottom-0 left-0 right-0 p-4 ${theme === 'dark' ? 'bg-gray-800 text-gray-400' : 'bg-white text-gray-500'}`}>
          <div className="flex items-center justify-center">
            <InfoCircleOutlined className="mr-2" />
            <Text className={`text-xs ${theme === 'dark' ? 'text-gray-400' : 'text-gray-500'}`}>
              {language === 'vi' ? 'UTECARE v2.0 - 2025' : 'UTECARE v2.0 - 2025'}
            </Text>
          </div>
        </div>
      </Drawer>
    </div>
  );
};

export default ChatbotService;
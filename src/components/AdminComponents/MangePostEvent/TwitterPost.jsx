
import React, {useState, useRef, useEffect} from 'react';
import {CaretDownOutlined, CaretUpOutlined} from '@ant-design/icons';
import dayjs from 'dayjs';
const TextWithReadMore = ({ text, maxLength = 100 }) => {
  const [isExpanded, setIsExpanded] = useState(false);

  if (text.length <= maxLength) {
      return <p className="mt-1 text-gray-700">{text}</p>;
  }

  const truncatedText = isExpanded ? text : `${text.slice(0, maxLength)}...`;

  return (
      <div className="mt-1 text-gray-700">
          <p>{truncatedText}</p>
          <button
              className="text-blue-500 cursor-pointer"
              onClick={() => setIsExpanded(!isExpanded)}
          >
              {isExpanded ? (
                    <>Thu gọn <CaretUpOutlined /></>
              ) : (
                    <>Xem thêm <CaretDownOutlined /></>
              )}
          </button>
      </div>
  );
};
const TwitterPost = ({textData, storageImg, headerData}) => {
  const today = dayjs();
  const dateFormat = 'HH:mm DD/MM/YYYY'

  const [activeIndex, setActiveIndex] = useState(0);
  const scrollContainerRef = useRef(null);
  const scrollToImage = (index) => {
    if (scrollContainerRef.current) {
      const imageWidth = 48 + 8; // Image width (48) + margin (8)
      scrollContainerRef.current.scrollLeft = index * imageWidth;
      setActiveIndex(index);
    }
  };
  const handleScroll = () => {
    if (scrollContainerRef.current) {
      const scrollPosition = scrollContainerRef.current.scrollLeft;
      const imageWidth = 48 + 8; // Image width + margin
      const index = Math.round(scrollPosition / imageWidth);
      setActiveIndex(index);
    }
  };
  useEffect(() => {
    const scrollContainer = scrollContainerRef.current;
    if (scrollContainer) {
      scrollContainer.addEventListener('scroll', handleScroll);
      return () => scrollContainer.removeEventListener('scroll', handleScroll);
    }
    console.log(storageImg)
  }, [storageImg]);
  return (
    <>
      <h1 className='w-full font-bold text-xl text-center my-2'>Xem trước bài post</h1>
      <div className="max-w-xl mx-auto bg-white rounded-xl shadow-md overflow-hidden">
      {/* Twitter header */}
      <div className="p-4 flex items-start flex-col">
        <div className="mr-3 w-full h-fit flex flex-row items-center space-x-2">
          <div className="w-12 h-12 rounded-full overflow-hidden bg-gray-200">
            {/* Placeholder for profile image */}
            <div className="w-full h-full bg-slate-100">
            <img src="https://api.dicebear.com/7.x/miniavs/svg?seed=8" alt="" srcset="" 
                        className="object-center w-full h-full rounded-full" />
            </div>
          </div>
          <div className="flex items-center flex-row space-x-1 w-full justify-between">
            <div className='flex flex-row items-center space-x-1'>
              <span className="font-bold text-gray-900">Nguyễn Thành Đạt</span>
              <svg width="20px" height="64px" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <g id="SVGRepo_bgCarrier" stroke-width="0"></g>
                <g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g>
                <g id="SVGRepo_iconCarrier"> 
                  <rect width="20" height="20" fill="white"></rect> 
                  <path fill-rule="evenodd" clip-rule="evenodd" d="M9.55879 3.6972C10.7552 2.02216 13.2447 2.02216 14.4412 3.6972L14.6317 3.96387C14.8422 4.25867 15.1958 4.41652 15.5558 4.37652L16.4048 4.28218C18.3156 4.06988 19.9301 5.68439 19.7178 7.59513L19.6235 8.44415C19.5835 8.8042 19.7413 9.15774 20.0361 9.36831L20.3028 9.55879C21.9778 10.7552 21.9778 13.2447 20.3028 14.4412L20.0361 14.6317C19.7413 14.8422 19.5835 15.1958 19.6235 15.5558L19.7178 16.4048C19.9301 18.3156 18.3156 19.9301 16.4048 19.7178L15.5558 19.6235C15.1958 19.5835 14.8422 19.7413 14.6317 20.0361L14.4412 20.3028C13.2447 21.9778 10.7553 21.9778 9.55879 20.3028L9.36831 20.0361C9.15774 19.7413 8.8042 19.5835 8.44414 19.6235L7.59513 19.7178C5.68439 19.9301 4.06988 18.3156 4.28218 16.4048L4.37652 15.5558C4.41652 15.1958 4.25867 14.8422 3.96387 14.6317L3.6972 14.4412C2.02216 13.2447 2.02216 10.7553 3.6972 9.55879L3.96387 9.36831C4.25867 9.15774 4.41652 8.8042 4.37652 8.44414L4.28218 7.59513C4.06988 5.68439 5.68439 4.06988 7.59513 4.28218L8.44415 4.37652C8.8042 4.41652 9.15774 4.25867 9.36831 3.96387L9.55879 3.6972ZM15.7071 9.29289C16.0976 9.68342 16.0976 10.3166 15.7071 10.7071L11.8882 14.526C11.3977 15.0166 10.6023 15.0166 10.1118 14.526L8.29289 12.7071C7.90237 12.3166 7.90237 11.6834 8.29289 11.2929C8.68342 10.9024 9.31658 10.9024 9.70711 11.2929L11 12.5858L14.2929 9.29289C14.6834 8.90237 15.3166 8.90237 15.7071 9.29289Z" fill="#4d70ff"></path>
                  </g>
              </svg>
              <span className="ml-1 text-gray-500">@dgeniust</span>
            </div>
            <span className='text-slate-400 text-xs'>{today.format(dateFormat)}</span>
          </div>
        </div>
        
        <div className="flex-1 px-2">
          
          
          <p className="mt-1 text-gray-700">
            {/* today is my first day of <span className="text-blue-500">@_buildspace</span> school 🔥 a place where you turn your ideas into reality and make friends along the way 😊 buildspace.so */}
            <h1 className='font-bold text-base'>{headerData}</h1>
            <TextWithReadMore text={textData} />
          </p>
        </div>
      </div>
      
      {/* Tweet images with fixed horizontal scroll */}
      <div className="px-4 pb-4">
        {/* The key is to set a fixed width on the container and overflow-x-auto */}
        <div ref={scrollContainerRef} className="scrollbar-hide  w-full overflow-x-auto pb-2" style={{ WebkitOverflowScrolling: 'touch' }}>
          {/* We make sure this inner container is wider than the parent to trigger scrolling */}
          <div className="inline-flex space-x-2 pb-1 pr-4">
            {storageImg.map((image) => (
              <div 
                key={image.id} 
                className="flex-none w-48 h-48 rounded-lg overflow-hidden border border-gray-200 shadow-sm"
              >
                <img 
                  src={image.src} 
                  alt="Ảnh" 
                  className="w-full h-full object-cover"
                  draggable="false"
                />
              </div>
            ))}
          </div>
        </div>
        
        {/* Scroll indicator dots */}
        <div className="mt-1 flex justify-center">
          <div className="flex space-x-1">
            {storageImg.map((_, index) => (
              <button
                key={index}
                onClick={() => scrollToImage(index)}
                className={`w-2 h-2 rounded-full transition-colors ${
                  index === activeIndex ? 'bg-blue-500' : 'bg-gray-300'
                }`}
                aria-label={`Go to image ${index + 1}`}
              />
            ))}
          </div>
        </div>
      </div>
      
      {/* Tweet actions */}
      <div className="px-4 pb-4 flex text-gray-500">
        <div className="flex items-center mr-6">
          <svg className="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M8 12h.01M12 12h.01M16 12h.01M21 12c0 4.418-4.03 8-9 8a9.863 9.863 0 01-4.255-.949L3 20l1.395-3.72C3.512 15.042 3 13.574 3 12c0-4.418 4.03-8 9-8s9 3.582 9 8z"></path>
          </svg>
          <span>24</span>
        </div>
        <div className="flex items-center mr-6">
          <svg className="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M7 16V4m0 0L3 8m4-4l4 4m6 0v12m0 0l4-4m-4 4l-4-4"></path>
          </svg>
          <span>42</span>
        </div>
        <div className="flex items-center">
          <svg className="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z"></path>
          </svg>
          <span>128</span>
        </div>
      </div>
    </div>
    </>
  );
};

export default TwitterPost;
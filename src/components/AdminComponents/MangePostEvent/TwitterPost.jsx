// import React from 'react';

// const TwitterPost = () => {
//   // Sample array of multiple images
//   const images = [
//     {
//       id: 1,
//       src :"https://res.cloudinary.com/dujzjcmai/image/upload/v1742400693/hospital/o9sjjpnuqoavd531humj.jpg"
//     },
//     {
//       id: 2,
//       src: "https://res.cloudinary.com/dujzjcmai/image/upload/v1742400693/hospital/kn8qwm454peumh12dcbg.jpg"
//     },
//     {
//       id: 3,
//       src : "https://res.cloudinary.com/dujzjcmai/image/upload/v1742400691/hospital/mtkaasoyqwx0v3aewfwn.jpg"
//     }
//   ];

//   return (
//     <div className="max-w-xl mx-auto bg-white rounded-xl shadow-md overflow-hidden">
//       {/* Twitter header */}
//       <div className="p-4 flex items-start">
//         <div className="mr-3">
//           <div className="w-12 h-12 rounded-full overflow-hidden bg-gray-200">
//             {/* Placeholder for profile image */}
//             <div className="w-full h-full bg-blue-200"></div>
//           </div>
//         </div>
        
//         <div className="flex-1">
//           <div className="flex items-center">
//             <span className="font-bold text-gray-900">Dillion</span>
//             <svg className="ml-1 w-4 h-4 text-blue-500" fill="currentColor" viewBox="0 0 24 24">
//               <path d="M22.5 12.5c0-1.58-.875-2.95-2.148-3.6.154-.435.238-.905.238-1.4 0-2.21-1.71-3.998-3.818-3.998-.47 0-.92.084-1.336.25C14.818 2.415 13.51 1.5 12 1.5s-2.816.917-3.437 2.25c-.415-.165-.866-.25-1.336-.25-2.11 0-3.818 1.79-3.818 4 0 .494.083.964.237 1.4-1.272.65-2.147 2.018-2.147 3.6 0 1.495.782 2.798 1.942 3.486-.02.17-.032.34-.032.514 0 2.21 1.708 4 3.818 4 .47 0 .92-.086 1.335-.25.62 1.334 1.926 2.25 3.437 2.25 1.512 0 2.818-.916 3.437-2.25.415.163.865.248 1.336.248 2.11 0 3.818-1.79 3.818-4 0-.174-.012-.344-.033-.513 1.158-.687 1.943-1.99 1.943-3.484z" />
//             </svg>
//             <span className="ml-1 text-gray-500">@dillionverma</span>
//           </div>
          
//           <p className="mt-1 text-gray-700">
//             today is my first day of <span className="text-blue-500">@_buildspace</span> school 🔥 a place where you turn your ideas into reality and make friends along the way 😊 buildspace.so
//           </p>
//         </div>
//       </div>
      
//       {/* Tweet images with horizontal scroll */}
//       <div className="px-4 pb-4">
//         <div className="overflow-x-auto">
//           <div className="flex space-x-2 pb-2">
//             {images.map((image) => (
//               <div key={image.id} className="flex-shrink-0 w-64 h-64 rounded-lg overflow-hidden border border-gray-200 bg-gray-100">
//                 <div className="relative w-full h-full">
//                   <div className="absolute inset-0 flex items-center justify-center">
//                     <img src={image.src} alt="" />
//                   </div>
//                 </div>
//               </div>
//             ))}
//           </div>
//         </div>
//       </div>
      
//       {/* Tweet actions */}
//       <div className="px-4 pb-4 flex text-gray-500">
//         <div className="flex items-center mr-6">
//           <svg className="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
//             <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M8 12h.01M12 12h.01M16 12h.01M21 12c0 4.418-4.03 8-9 8a9.863 9.863 0 01-4.255-.949L3 20l1.395-3.72C3.512 15.042 3 13.574 3 12c0-4.418 4.03-8 9-8s9 3.582 9 8z"></path>
//           </svg>
//           <span>24</span>
//         </div>
//         <div className="flex items-center mr-6">
//           <svg className="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
//             <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M7 16V4m0 0L3 8m4-4l4 4m6 0v12m0 0l4-4m-4 4l-4-4"></path>
//           </svg>
//           <span>42</span>
//         </div>
//         <div className="flex items-center">
//           <svg className="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
//             <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z"></path>
//           </svg>
//           <span>128</span>
//         </div>
//       </div>
//     </div>
//   );
// };

// export default TwitterPost;

import React, {useState, useRef, useEffect} from 'react';
import {CaretDownOutlined, CaretUpOutlined} from '@ant-design/icons';
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
const TwitterPost = () => {
  const [activeIndex, setActiveIndex] = useState(0);
  const scrollContainerRef = useRef(null);
  // Sample array of multiple images
  const images = [
    {
      id: 1,
      src: "https://res.cloudinary.com/dujzjcmai/image/upload/v1742400693/hospital/o9sjjpnuqoavd531humj.jpg"
    },
    {
      id: 2,
      src: "https://res.cloudinary.com/dujzjcmai/image/upload/v1742400693/hospital/kn8qwm454peumh12dcbg.jpg"
    },
    {
      id: 3,
      src: "https://res.cloudinary.com/dujzjcmai/image/upload/v1742400691/hospital/mtkaasoyqwx0v3aewfwn.jpg"
    },
    {
      id: 4,
      src: "https://res.cloudinary.com/dujzjcmai/image/upload/v1742400693/hospital/o9sjjpnuqoavd531humj.jpg"
    },
    {
      id: 5,
      src: "https://res.cloudinary.com/dujzjcmai/image/upload/v1742400693/hospital/kn8qwm454peumh12dcbg.jpg"
    }
  ];
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
  }, []);
  const text =`Lorem ipsum dolor sit amet, consectetur adipisicing elit. Deleniti error quam doloribus distinctio blanditiis suscipit ab, deserunt autem voluptatibus voluptatem, minima  laboriosam cumque ratione repudiandae nam voluptate officia nulla? Dolorum.
            Lorem ipsum dolor sit amet, consectetur adipisicing elit. Deleniti error quam doloribus distinctio blanditiis suscipit ab, deserunt autem voluptatibus voluptatem, minima laboriosam cumque ratione repudiandae nam voluptate officia nulla? Dolorum.
            Lorem ipsum dolor sit amet, consectetur adipisicing elit. Deleniti error quam doloribus distinctio blanditiis suscipit ab, deserunt autem voluptatibus voluptatem, minima laboriosam cumque ratione repudiandae nam voluptate officia nulla? Dolorum.`
  return (
    <>
      <h1 className='w-full font-bold text-xl text-center my-2'>Xem trước bài post</h1>
      <div className="max-w-xl mx-auto bg-white rounded-xl shadow-md overflow-hidden">
      {/* Twitter header */}
      <div className="p-4 flex items-start">
        <div className="mr-3">
          <div className="w-12 h-12 rounded-full overflow-hidden bg-gray-200">
            {/* Placeholder for profile image */}
            <div className="w-full h-full bg-blue-200"></div>
          </div>
        </div>
        
        <div className="flex-1">
          <div className="flex items-center">
            <span className="font-bold text-gray-900">Dillion</span>
            <svg className="ml-1 w-4 h-4 text-blue-500" fill="currentColor" viewBox="0 0 24 24">
              <path d="M22.5 12.5c0-1.58-.875-2.95-2.148-3.6.154-.435.238-.905.238-1.4 0-2.21-1.71-3.998-3.818-3.998-.47 0-.92.084-1.336.25C14.818 2.415 13.51 1.5 12 1.5s-2.816.917-3.437 2.25c-.415-.165-.866-.25-1.336-.25-2.11 0-3.818 1.79-3.818 4 0 .494.083.964.237 1.4-1.272.65-2.147 2.018-2.147 3.6 0 1.495.782 2.798 1.942 3.486-.02.17-.032.34-.032.514 0 2.21 1.708 4 3.818 4 .47 0 .92-.086 1.335-.25.62 1.334 1.926 2.25 3.437 2.25 1.512 0 2.818-.916 3.437-2.25.415.163.865.248 1.336.248 2.11 0 3.818-1.79 3.818-4 0-.174-.012-.344-.033-.513 1.158-.687 1.943-1.99 1.943-3.484z" />
            </svg>
            <span className="ml-1 text-gray-500">@dillionverma</span>
          </div>
          
          <p className="mt-1 text-gray-700">
            {/* today is my first day of <span className="text-blue-500">@_buildspace</span> school 🔥 a place where you turn your ideas into reality and make friends along the way 😊 buildspace.so */}
            <TextWithReadMore text={text} />
          </p>
        </div>
      </div>
      
      {/* Tweet images with fixed horizontal scroll */}
      <div className="px-4 pb-4">
        {/* The key is to set a fixed width on the container and overflow-x-auto */}
        <div ref={scrollContainerRef} className="scrollbar-hide  w-full overflow-x-auto pb-2" style={{ WebkitOverflowScrolling: 'touch' }}>
          {/* We make sure this inner container is wider than the parent to trigger scrolling */}
          <div className="inline-flex space-x-2 pb-1 pr-4">
            {images.map((image) => (
              <div 
                key={image.id} 
                className="flex-none w-48 h-48 rounded-lg overflow-hidden border border-gray-200 shadow-sm"
              >
                <img 
                  src={image.src} 
                  alt="" 
                  className="w-full h-full object-cover"
                />
              </div>
            ))}
          </div>
        </div>
        
        {/* Scroll indicator dots */}
        <div className="mt-1 flex justify-center">
          <div className="flex space-x-1">
            {images.map((_, index) => (
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
import React from 'react';
import '../../../css/mainpage.css'
import { Carousel } from 'antd';
import TwitterPost from '../../../components/AdminComponents/MangePostEvent/TwitterPost';
const DoctorHomePage = () => {
    const contentStyle = {
        height: '420px',
        color: '#fff',
        lineHeight: '420px',
        textAlign: 'center',
    };
    const twitterPosts = {
        post1: {
          textData: "Hôm nay mình vừa hoàn thành một dự án cá nhân! 🎉 Cảm giác thật tuyệt vời khi thấy nó hoạt động đúng như mong đợi. Ai muốn xem thử thì để lại comment nhé! 🚀",
          headerData: "Dự án cá nhân đầu tiên!",
          storageImg: [
            { id: 1, src: "https://res.cloudinary.com/dujzjcmai/image/upload/v1742400691/hospital/mtkaasoyqwx0v3aewfwn.jpg" },
            { id: 2, src: "https://res.cloudinary.com/dujzjcmai/image/upload/v1742400693/hospital/1.jpg" }
          ]
        },
        post2: {
          textData: "Ngày đầu tiên học ReactJS! Ban đầu thấy hơi khó hiểu nhưng sau khi làm thử một vài bài tập thì đã cảm thấy tự tin hơn rất nhiều. Ai có tài liệu hay thì chia sẻ với mình nhé! 💡",
          headerData: "Bắt đầu hành trình với ReactJS",
          storageImg: [
            { id: 1, src: "https://res.cloudinary.com/dujzjcmai/image/upload/v1742400692/hospital/nil2r59dgwzxzbb5fiym.jpg" },
            { id: 2, src: "https://res.cloudinary.com/dujzjcmai/image/upload/v1742400691/hospital/nhot8fj0fesmdgomglo0.jpg" }
          ]
        },
        post3: {
            textData: "Ngày đầu tiên học ReactJS! Ban đầu thấy hơi khó hiểu nhưng sau khi làm thử một vài bài tập thì đã cảm thấy tự tin hơn rất nhiều. Ai có tài liệu hay thì chia sẻ với mình nhé! 💡",
            headerData: "Bắt đầu hành trình với ReactJS",
            storageImg: [
              { id: 1, src: "https://res.cloudinary.com/dujzjcmai/image/upload/v1742400692/hospital/nil2r59dgwzxzbb5fiym.jpg" },
              { id: 2, src: "https://res.cloudinary.com/dujzjcmai/image/upload/v1742400691/hospital/nhot8fj0fesmdgomglo0.jpg" }
            ]
          }
    };
    return (
        <div className='w-full h-full relative overflow-hidden flex flex-col space-y-60'>
            {/* <div className="w-full h-fit relative">
                <div className='w-full h-full absolute rounded-xl shadow-lg'>
                    <Carousel
                        autoplay
                        autoplaySpeed={5000}
                        arrows 
                    >
                        <div className="carousel-1">
                        <h3 style={contentStyle}></h3>
                        </div>
                        <div className="carousel-2">
                        <h3 style={contentStyle}></h3>
                        </div>
                        <div className="carousel-3">
                        <h3 style={contentStyle}></h3>
                        </div>
                        <div className="carousel-4">
                        <h3 style={contentStyle}></h3>
                        </div>
                    </Carousel>
                </div>
            </div> */}
            <div className='w-full h-full flex justify-center'>
                <div className='w-fit h-fit p-8 flex items-center justify-center flex-col'>
                    <h1 className='header-social-care font-bold text-4xl text-black text-center items-center'>Care Social</h1>
                    <div className='bg-slate-50/100 shadow-lg'>
                    {
                        Object.values(twitterPosts).map((post, index) => (
                            <TwitterPost key={index} {...post}/>
                        ))
                    }
                    </div>
                </div>
            </div>
        </div>
    )
}

export default DoctorHomePage;
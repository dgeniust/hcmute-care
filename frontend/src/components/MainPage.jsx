import React, { useState, useEffect } from "react";
import { Carousel  } from 'antd';
import DragCards from '../components/HomePage/DragCard'
import {
    ArrowRightOutlined
} from '@ant-design/icons';
import '../css/mainpage.css'
import InfiniteScrollingCarousel from "./HomePage/InfiniteScrollingCarousel";
import BodyContent from "./HomePage/BodyContent";
import GeneratePictureOnMouse from "./HomePage/GeneratePictureOnMouse";
const contentStyle = {
  height: '420px',
  color: '#fff',
  lineHeight: '420px',
  textAlign: 'center',
};
const MainPage = ({onPageChange}) =>{

  const handlePageChange = (page) => {
    onPageChange(page);
  }
    return <div className="w-full h-full">
          <div className="hello-content w-full h-fit max-w-screen mb-8 rounded-xl shadow-lg">
          {/* <Carousel
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
          </Carousel> */}
          <GeneratePictureOnMouse/>
          </div>
        {/* <div className="grid grid-cols-3 grid-flow-row gap-8 p-8 space-y-4">
        {[
          { title: "Khám chuyên khoa", onClick: null },
          { title: "Hồ sơ sức khỏe", onClick: null },
          { title: "Lịch sử đặt khám", onClick: null },
          { title: "Đặt lịch uống thuốc", onClick: null },
          { title: "Chỉ số BMI, BMR", onClick: () => handlePageChange('bmi') },
          { title: "Lịch sử tiêm chủng", onClick: null },
          ].map((card, index) => (
            <div
              key={index}
              className={`card-container-${index + 1} border border-gray-400 h-[15vh] w-[20vw] rounded-lg cursor-pointer`}
              onClick={card.onClick}
            >
              <div className="card-content flex justify-center items-center text-center h-full">
                <h1 className="font-bold text-white text-lg">{card.title}</h1>
              </div>
            </div>
          ))}
        </div> */}
        <div className="w-full h-full min-h-screen flex items-center justify-center shadow-lg">
          <BodyContent/>
        </div>
        <div className="w-full h-fit mt-4 shadow-lg">
          <InfiniteScrollingCarousel/>
        </div>
        <div className="w-full h-fit mt-4">
          <DragCards/>
        </div>
        <div className="divider flex items-center text-black justify-between mt-8">
            <h1 className="font-bold text-lg pl-8">Tin tức nổi bật</h1>
            <p className="pr-16 hover:text-sky-600 cursor-pointer">Xem thêm <ArrowRightOutlined /></p>
        </div>
        <div className="main-content">

        </div>
    </div>
    
}
export default MainPage;
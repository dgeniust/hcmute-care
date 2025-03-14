import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import { Menu, Button , theme, Carousel  } from 'antd';
import Icon, {
    ArrowRightOutlined
} from '@ant-design/icons';
import '../css/mainpage.css'
import axios from "axios";
import InfiniteScrollingCarousel from "./InfiniteScrollingCarousel";
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
    
    const [weather, setWeather] = useState('');
    const [temp, setTemp] = useState('');
    const city = "Ho Chi Minh City"; // Cố định thành phố
    const apiKey = 'fb646f7b9b9d424ba4c130439250301';
    const apiUrl = `http://api.weatherapi.com/v1/current.json?key=${apiKey}&q=${city}`;

    useEffect(() => {
        const getWeather = async () => {
          try {
            const response = await axios.get(apiUrl);
            console.log(response.data);       
            setWeather(response.data);
          } catch {
            setWeather(null);
          }
        };
    
        getWeather();
      }, []);

    function getCurrentDateTime() {
        const today = new Date();
      
        const day = today.getDate(); // Ngày
        const month = today.getMonth() + 1; // Tháng (tháng bắt đầu từ 0)
        const year = today.getFullYear(); // Năm
      
        const hours = today.getHours(); // Giờ
        const minutes = today.getMinutes(); // Phút
      
        const daysOfWeek = ["Chủ nhật", "Thứ 2", "Thứ 3", "Thứ 4", "Thứ 5", "Thứ 6", "Thứ 7"];
        const monthsOfYear = ["January", "Ferbuary", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];
        const dayOfWeek = daysOfWeek[today.getDay()]; // Thứ trong tuần
        const monthOfYear = monthsOfYear[today.getMonth()]; // Tháng trong năm
        return {
          day,
          monthOfYear,
          year,
          hours,
          minutes, dayOfWeek 
        };
    }
    const formatTime = (seconds) => {
        const minutes = Math.floor(seconds / 60);
        const remainingSeconds = seconds % 60;
        return `${remainingSeconds < 10 ? '0' : ''}${remainingSeconds}`;
    };

    return <div className="w-full h-[100vh] max-w-full">
        {/* <div
            style={{
              padding: 24,
              textAlign: 'center',
            }}
          >
            <p>long content</p>
            {
              // indicates very long content
              Array.from({ length: 100 }, (_, index) => (
                <React.Fragment key={index}>
                  {index % 20 === 0 && index ? 'more' : '...'}
                  <br />
                </React.Fragment>
              ))
            }
          </div> */}
          <div className="hello-content relative w-[74vw] h-[60vh] max-w-full mb-4">
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
        {/* <div className="p-8 flex flex-row items-center space-x-4"> 
            <div className="clock">
            <div class="before:absolute before:bg-sky-500 before:w-3 before:h-12 before:top-24 before:-right-2 before:-z-10 before:rounded-2xl before:shadow-inner before:shadow-gray-50 relative w-60 h-60 bg-sky-500 shadow-inner shadow-gray-50 flex justify-center items-center rounded-3xl">
            <div class="w-56 h-56 bg-neutral-900 shadow-inner shadow-gray-50 flex justify-center items-center rounded-3xl">
                <div class="flex flex-col items-center justify-center rounded-2xl bg-neutral-900 shadow-inner shadow-gray-50 w-52 h-52">
                <div class="before:absolute before:w-12 before:h-12 before:bg-orange-800 before:rounded-full before:blur-xl before:top-16 relative   flex flex-col justify-around items-center w-44 h-40 bg-neutral-900 text-gray-50">
                    <span class="">{getCurrentDateTime().dayOfWeek + ", "+getCurrentDateTime().monthOfYear+" "+getCurrentDateTime().day}</span>
                    <span class="z-10 flex items-center text-6xl text-amber-600 [text-shadow:_2px_2px_#fff,_1px_2px_#fff]">{getCurrentDateTime().hours}<span class="text-xl font-bold text-gray-50 [text-shadow:none]">:</span>{formatTime(getCurrentDateTime().minutes)}</span>
                    <div class="text-gray-50 w-48 flex flex-row justify-evenly">
                    <span class="text-xs font-bold">89</span>
                    <div class="flex flex-row items-center">
                        <svg y="0" xmlns="http://www.w3.org/2000/svg" x="0" width="100" viewBox="0 0 100 100" preserveAspectRatio="xMidYMid meet" height="100" class="w-5 h-5 fill-red-500 animate-bounce">
                        <path fill-rule="evenodd" d="M23,27.6a15.8,15.8,0,0,1,22.4,0L50,32.2l4.6-4.6A15.8,15.8,0,0,1,77,50L50,77,23,50A15.8,15.8,0,0,1,23,27.6Z" class="">
                        </path>
                        </svg>
                        <svg y="0" xmlns="http://www.w3.org/2000/svg" x="0" width="100" viewBox="0 0 100 100" preserveAspectRatio="xMidYMid meet" height="100" class="w-5 h-5 fill-current">
                        <path d="M80.2,40.7l-1.1-2-.2-.3.3-.3c2.2-14.7-21.3-25.6-20.7-21S57,38.1,45.4,31.8c-9.3-5.1-12.9,12.1-22.8,33.7C16.2,79.4,20.8,82.3,27,81l.3.4L29,83.3a1.4,1.4,0,0,0,1.8.5l.9-.3a1.6,1.6,0,0,0,1.1-1.9l-.5-2.5a38.2,38.2,0,0,0,4.5-2.7L38.6,78a1.8,1.8,0,0,0,2.4-.1l1.2-1.1a1.9,1.9,0,0,0,.4-1.9l-1-2.5L45.5,69l1.7,1.6a1.8,1.8,0,0,0,2.4-.1l.9-1a1.7,1.7,0,0,0,.4-1.8L50,65c5.6-5,11.9-10.9,17.3-15.8l.4.2,1.9,1.1a1.6,1.6,0,0,0,2.1-.2l.8-.8a1.6,1.6,0,0,0,.3-2.1l-1.3-2.1,3.2-3.1,2.2,1.5a1.8,1.8,0,0,0,2.2-.1l.8-.8A1.7,1.7,0,0,0,80.2,40.7Z" class="svg-fill-primary">
                        </path>
                        </svg>
                        <svg y="0" xmlns="http://www.w3.org/2000/svg" x="0" width="100" viewBox="0 0 100 100" preserveAspectRatio="xMidYMid meet" height="100" class="w-5 h-5 fill-current">
                        <path fill-rule="evenodd" d="M59.5,20.5a3.9,3.9,0,0,0-2.5-2,4.3,4.3,0,0,0-3.3.5,11.9,11.9,0,0,0-3.2,3.5,26,26,0,0,0-2.3,4.4,76.2,76.2,0,0,0-3.3,10.8,120.4,120.4,0,0,0-2.4,14.2,11.4,11.4,0,0,1-3.8-4.2c-1.3-2.7-1.5-6.1-1.5-10.5a4,4,0,0,0-2.5-3.7,3.8,3.8,0,0,0-4.3.9,27.7,27.7,0,1,0,39.2,0,62.4,62.4,0,0,1-5.3-5.8A42.9,42.9,0,0,1,59.5,20.5ZM58.4,70.3a11.9,11.9,0,0,1-20.3-8.4s3.5,2,9.9,2c0-4,2-15.9,5-17.9a21.7,21.7,0,0,0,5.4,7.5,11.8,11.8,0,0,1,3.5,8.4A12,12,0,0,1,58.4,70.3Z" class="svg-fill-primary">
                        </path>
                        </svg>
                    </div>
                    </div>
                </div>
                <span class="text-gray-700 text-lg font-light">fitbit</span>
                </div>
            </div>
            </div>
            </div>
            <div
            className="duration-300 font-mono text-white group cursor-pointer relative overflow-hidden bg-[#DCDFE4] w-28 h-58 dark:bg-[#22272B] rounded-3xl p-4 hover:w-56 hover:bg-blue-200 hover:dark:bg-[#0C66E4] border max-w-56"
            >
            <h3 class="text-xl text-center">Today</h3>
            <div class="gap-4 relative">
            {weather ? (
                    <>
                        {["clear", "cloudy","sunny", "partly cloudy"].some(condition =>
                            weather.current.condition.text.toLowerCase().includes(condition)
                        ) ? (
                            <svg
                            class="w-16 h-16 p-1 text-yellow-400 fill-current animate-[spin_5s_linear_infinite;]"
                            viewBox="0 0 512 512"
                            xmlns="http://www.w3.org/2000/svg"
                          >
                            <path
                              d="M256,104c-83.813,0-152,68.187-152,152s68.187,152,152,152,152-68.187,152-152S339.813,104,256,104Zm0,272A120,120,0,1,1,376,256,120.136,120.136,0,0,1,256,376Z"
                            ></path>
                            <rect
                              class="animate-[pulse_1s_cubic-bezier(0.4,_0,_0.6,_1)_infinite;]"
                              y="16"
                              x="240"
                              height="48"
                              width="32"
                            ></rect>
                            <rect
                              class="animate-[pulse_2s_cubic-bezier(0.4,_0,_0.6,_1)_infinite;]"
                              y="448"
                              x="240"
                              height="48"
                              width="32"
                            ></rect>
                            <rect
                              class="animate-[pulse_1s_cubic-bezier(0.4,_0,_0.6,_1)_infinite;]"
                              y="240"
                              x="448"
                              height="32"
                              width="48"
                            ></rect>
                            <rect
                              class="animate-[pulse_2s_cubic-bezier(0.4,_0,_0.6,_1)_infinite;]"
                              y="240"
                              x="16"
                              height="32"
                              width="48"
                            ></rect>
                            <rect
                              class="animate-[pulse_1s_cubic-bezier(0.4,_0,_0.6,_1)_infinite;]"
                              transform="rotate(-45 416 416)"
                              y="393.373"
                              x="400"
                              height="45.255"
                              width="32"
                            ></rect>
                            <rect
                              class="animate-[pulse_2s_cubic-bezier(0.4,_0,_0.6,_1)_infinite;]"
                              transform="rotate(-45 96 96)"
                              y="73.373"
                              x="80"
                              height="45.255"
                              width="32.001"
                            ></rect>
                            <rect
                              class="animate-[pulse_1s_cubic-bezier(0.4,_0,_0.6,_1)_infinite;]"
                              transform="rotate(-45.001 96.002 416.003)"
                              y="400"
                              x="73.373"
                              height="32"
                              width="45.255"
                            ></rect>
                            <rect
                              class="animate-[pulse_2s_cubic-bezier(0.4,_0,_0.6,_1)_infinite;]"
                              transform="rotate(-45 416 96)"
                              y="80"
                              x="393.373"
                              height="32.001"
                              width="45.255"
                            ></rect>
                          </svg>
                        ) : ["light rain", "heavy rain", "drizzle", "showers", "thunderstorm", "rain"].some(condition =>
                            weather.current.condition.text.toLowerCase().includes(condition)
                          ) ?(
                            <svg
                                viewBox="0 0 64 64"
                                xmlns:xlink="http://www.w3.org/1999/xlink"
                                xmlns="http://www.w3.org/2000/svg"
                                class="w-20 scale-[110%]"
                                >
                                <defs>
                                    <linearGradient
                                    gradientUnits="userSpaceOnUse"
                                    y2="28.33"
                                    y1="19.67"
                                    x2="21.5"
                                    x1="16.5"
                                    id="b"
                                    >
                                    <stop stop-color="#fbbf24" offset="0"></stop>
                                    <stop stop-color="#fbbf24" offset=".45"></stop>
                                    <stop stop-color="#f59e0b" offset="1"></stop>
                                    </linearGradient>
                                    <linearGradient
                                    gradientUnits="userSpaceOnUse"
                                    y2="50.8"
                                    y1="21.96"
                                    x2="39.2"
                                    x1="22.56"
                                    id="c"
                                    >
                                    <stop stop-color="#f3f7fe" offset="0"></stop>
                                    <stop stop-color="#f3f7fe" offset=".45"></stop>
                                    <stop stop-color="#deeafb" offset="1"></stop>
                                    </linearGradient>
                                    <linearGradient
                                    gradientUnits="userSpaceOnUse"
                                    y2="48.05"
                                    y1="42.95"
                                    x2="25.47"
                                    x1="22.53"
                                    id="a"
                                    >
                                    <stop stop-color="#4286ee" offset="0"></stop>
                                    <stop stop-color="#4286ee" offset=".45"></stop>
                                    <stop stop-color="#0950bc" offset="1"></stop>
                                    </linearGradient>
                                    <linearGradient
                                    xlink:href="#a"
                                    y2="48.05"
                                    y1="42.95"
                                    x2="32.47"
                                    x1="29.53"
                                    id="d"
                                    ></linearGradient>
                                    <linearGradient
                                    xlink:href="#a"
                                    y2="48.05"
                                    y1="42.95"
                                    x2="39.47"
                                    x1="36.53"
                                    id="e"
                                    ></linearGradient>
                                </defs>
                                <circle
                                    stroke-width=".5"
                                    stroke-miterlimit="10"
                                    stroke="#f8af18"
                                    fill="url(#b)"
                                    r="5"
                                    cy="24"
                                    cx="19"
                                ></circle>
                                <path
                                    d="M19 15.67V12.5m0 23v-3.17m5.89-14.22l2.24-2.24M10.87 32.13l2.24-2.24m0-11.78l-2.24-2.24m16.26 16.26l-2.24-2.24M7.5 24h3.17m19.83 0h-3.17"
                                    stroke-width="2"
                                    stroke-miterlimit="10"
                                    stroke-linecap="round"
                                    stroke="#fbbf24"
                                    fill="none"
                                >
                                    <animateTransform
                                    values="0 19 24; 360 19 24"
                                    type="rotate"
                                    repeatCount="indefinite"
                                    dur="45s"
                                    attributeName="transform"
                                    ></animateTransform>
                                </path>
                                <path
                                    d="M46.5 31.5h-.32a10.49 10.49 0 00-19.11-8 7 7 0 00-10.57 6 7.21 7.21 0 00.1 1.14A7.5 7.5 0 0018 45.5a4.19 4.19 0 00.5 0v0h28a7 7 0 000-14z"
                                    stroke-width=".5"
                                    stroke-miterlimit="10"
                                    stroke="#e6effc"
                                    fill="url(#c)"
                                ></path>
                                <path
                                    d="M24.39 43.03l-.78 4.94"
                                    stroke-width="2"
                                    stroke-miterlimit="10"
                                    stroke-linecap="round"
                                    stroke="url(#a)"
                                    fill="none"
                                >
                                    <animateTransform
                                    values="1 -5; -2 10"
                                    type="translate"
                                    repeatCount="indefinite"
                                    dur="0.7s"
                                    attributeName="transform"
                                    ></animateTransform>
                                </path>
                                <path
                                    d="M31.39 43.03l-.78 4.94"
                                    stroke-width="2"
                                    stroke-miterlimit="10"
                                    stroke-linecap="round"
                                    stroke="url(#d)"
                                    fill="none"
                                >
                                    <animateTransform
                                    values="1 -5; -2 10"
                                    type="translate"
                                    repeatCount="indefinite"
                                    dur="0.7s"
                                    begin="-0.4s"
                                    attributeName="transform"
                                    ></animateTransform>
                                </path>
                                <path
                                    d="M38.39 43.03l-.78 4.94"
                                    stroke-width="2"
                                    stroke-miterlimit="10"
                                    stroke-linecap="round"
                                    stroke="url(#e)"
                                    fill="none"
                                >
                                    <animateTransform
                                    values="1 -5; -2 10"
                                    type="translate"
                                    repeatCount="indefinite"
                                    dur="0.7s"
                                    begin="-0.2s"
                                    attributeName="transform"
                                    ></animateTransform>
                                </path>
                                </svg>
                        ) : null}
                    </>
                ) : (
                    <p>Loading weather...</p>
                )}
                <h4
                class="font-sans duration-300 absolute left-1/2 -translate-x-1/2 text-2xl text-center group-hover:translate-x-8 group-hover:-translate-y-16 group-hover:scale-150"
                >
                {weather ? `${weather.current.temp_c}°` : 'Loading...'}
                </h4>
            </div>
            <div class="absolute duration-300 -left-36 mt-2 group-hover:left-10">
                <p class="text-sm">{weather ? weather.current.condition.text : 'Loading description...'}</p>
                <p class="text-sm">Khả năng mưa💦: {weather ? weather.current.precip_in : 'Loading description...'}%</p>
                <p class="text-sm">Độ ẩm💧: {weather ? `${weather.current.humidity}%` : 'Loading humidity...'}</p>
                <p class="text-sm">Gió💨: {weather ? `${weather.current.wind_kph} km/h` : 'Loading humidity...'}</p>
            </div>
            </div>

        </div> */}
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
        {/* <div className="w-full h-[460px] text-black p-8 flex flex-col items-center justify-center bg-2-home text-center space-y-2">
          <h1 className="font-bold text-2xl w-full">HCMUTE <span className="care-text">CARE</span></h1>
          <div className="w-full flex flex-col space-y-10 h-full mt-2 items-center">
            <p>HCMUTE Care là ứng dụng đặt lịch khám bệnh và quản lý bệnh án thông minh, giúp bạn dễ dàng theo dõi sức khỏe của mình. Với HCMUTE Care, bạn có thể đặt lịch khám với các bác sĩ chuyên khoa, xem lại lịch sử khám bệnh và quản lý hồ sơ sức khỏe cá nhân ngay trên điện thoại. Ứng dụng này mang đến sự tiện lợi và tiết kiệm thời gian cho người dùng trong việc chăm sóc sức khỏe.</p>
            <Button style={{padding:'25px',width:'200px', fontWeight:"bold", color: 'white', fontSize:'17px', borderRadius: '10px'}} className='button-gradient-home'>Đặt lịch khám ngay</Button>
          </div>
        </div> */}
        <div className="w-full h-fit mt-4">
          <InfiniteScrollingCarousel/>
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
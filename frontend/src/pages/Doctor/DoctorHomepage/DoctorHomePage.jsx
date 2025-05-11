import React, { useEffect, useState } from "react";
import "../../../css/mainpage.css";
import { message } from "antd";
import TwitterPost from "../../../components/AdminComponents/MangePostEvent/TwitterPost";
import {
  handleHttpStatusCode,
  notifyErrorWithCustomMessage,
  notifySuccessWithCustomMessage,
} from "../../../utils/notificationHelper";
const DoctorHomePage = () => {
  // const twitterPosts = {
  //     post1: {
  //       textData: "Hôm nay mình vừa hoàn thành một dự án cá nhân! 🎉 Cảm giác thật tuyệt vời khi thấy nó hoạt động đúng như mong đợi. Ai muốn xem thử thì để lại comment nhé! 🚀",
  //       headerData: "Dự án cá nhân đầu tiên!",
  //       storageImg: [
  //         { id: 1, src: "https://res.cloudinary.com/dujzjcmai/image/upload/v1742400691/hospital/mtkaasoyqwx0v3aewfwn.jpg" },
  //         { id: 2, src: "https://res.cloudinary.com/dujzjcmai/image/upload/v1742400693/hospital/1.jpg" }
  //       ]
  //     },
  //     post2: {
  //       textData: "Ngày đầu tiên học ReactJS! Ban đầu thấy hơi khó hiểu nhưng sau khi làm thử một vài bài tập thì đã cảm thấy tự tin hơn rất nhiều. Ai có tài liệu hay thì chia sẻ với mình nhé! 💡",
  //       headerData: "Bắt đầu hành trình với ReactJS",
  //       storageImg: [
  //         { id: 1, src: "https://res.cloudinary.com/dujzjcmai/image/upload/v1742400692/hospital/nil2r59dgwzxzbb5fiym.jpg" },
  //         { id: 2, src: "https://res.cloudinary.com/dujzjcmai/image/upload/v1742400691/hospital/nhot8fj0fesmdgomglo0.jpg" }
  //       ]
  //     },
  //     post3: {
  //         textData: "Ngày đầu tiên học ReactJS! Ban đầu thấy hơi khó hiểu nhưng sau khi làm thử một vài bài tập thì đã cảm thấy tự tin hơn rất nhiều. Ai có tài liệu hay thì chia sẻ với mình nhé! 💡",
  //         headerData: "Bắt đầu hành trình với ReactJS",
  //         storageImg: [
  //           { id: 1, src: "https://res.cloudinary.com/dujzjcmai/image/upload/v1742400692/hospital/nil2r59dgwzxzbb5fiym.jpg" },
  //           { id: 2, src: "https://res.cloudinary.com/dujzjcmai/image/upload/v1742400691/hospital/nhot8fj0fesmdgomglo0.jpg" }
  //         ]
  //       }
  // };
  const [messageApi, contextHolder] = message.useMessage();
  const [listPost, setListPost] = useState([]);
  const apiUrl = import.meta.env.VITE_API_BASE_URL;
  useEffect(() => {
    const fetchPostApi = async () => {
      const accessToken = localStorage.getItem("accessToken");
      try {
        const response = await fetch(`${apiUrl}v1/posts/list`, {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${accessToken}`,
          },
        });

        if (!response.ok) {
          const errorText = await response.text();
          handleHttpStatusCode(
            response.status,
            "",
            `Cập nhật thông tin thất bại: ${errorText || response.statusText}`,
            messageApi
          );
          return;
        }
        const data = await response.json();
        setListPost(data.data);
        notifySuccessWithCustomMessage("Load bài viết thành công", messageApi);
      } catch (e) {
        notifyErrorWithCustomMessage(
          "Lỗi kết nối khi load bài viết",
          messageApi
        );
        console.error("Error updating customer:", e);
      }
    };
    fetchPostApi();
    console.log("listPost" + listPost.toString());
  }, []);
  return (
    <div className="w-full h-full relative overflow-hidden flex flex-col space-y-60">
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
      <div className="w-full h-full flex justify-center">
        <div className="w-fit h-fit p-8 flex items-center justify-center flex-col">
          <h1 className="header-social-care font-bold text-4xl text-black text-center items-center">
            Care Social
          </h1>
          <div className="bg-slate-50/100 shadow-lg">
            {listPost.map((post, index) => (
              <TwitterPost
                key={index}
                id={post.id}
                header={post.header}
                content={post.content || "No content available"} // Fallback for missing content
                doc={post.doc}
                postImages={post.postImages}
              />
            ))}
          </div>
        </div>
      </div>
    </div>
  );
};

export default DoctorHomePage;

import React, { useState, useEffect } from "react";
import EmojiPickerInput from "../../../components/AdminComponents/MangePostEvent/EmojiPickerInput";
import SwipeCards from "../../../components/AdminComponents/MangePostEvent/SwipeCard";
import PreviewText from "../../../components/AdminComponents/MangePostEvent/PreviewText";
import TwitterPost from "../../../components/AdminComponents/MangePostEvent/TwitterPost";
import dayjs from "dayjs";
import {
  notifyErrorWithCustomMessage,
  handleHttpStatusCode,
  notifySuccessWithCustomMessage,
} from "../../../utils/notificationHelper";
import { message } from "antd";

const ManagePost = () => {
  const apiUrl = import.meta.env.VITE_API_BASE_URL;
  const [textData, setTextData] = useState("");
  const [headerData, setHeaderData] = useState("");
  const [storageImg, setStorageImg] = useState([]);
  const [messageApi, contextHolder] = message.useMessage(); // Để dùng notifyErrorWithCustomMessage

  useEffect(() => {
    console.log("textdata: " + textData);
  }, [textData]);
  const handleUploadPost = async () => {
    const currentDate = dayjs();
    const formattedDate = currentDate.format("YYYY-MM-DD");
    const staffId = localStorage.getItem("customerId");
    const data = {
      header: headerData,
      content: textData,
      doc: formattedDate,
      postImages: storageImg.map(({ imageUrl }) => ({ imageUrl })),
      staffId: Number(staffId),
    };
    console.log("data: " + JSON.stringify(data, null, 2));

    try {
      const response = await fetch(`${apiUrl}v1/posts`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
        },
        body: JSON.stringify(data),
      });

      if (!response.ok) {
        const errorText = await response.text();
        console.error("Error response:", errorText); // Log the error response
        handleHttpStatusCode(
          response.status,
          "",
          `Đăng bài viết thất bại : ${errorText || response.statusText}`,
          messageApi
        ); // Handle the error response
        return;
      } else {
        const responseData = await response.json();
        console.log("Response data:", responseData); // Log the response data
        notifySuccessWithCustomMessage("Đăng bài viết thành công", messageApi);
      }
    } catch (e) {
      notifyErrorWithCustomMessage(
        "Dữ liệu bài viết không hợp lệ hoặc thiếu thông tin",
        messageApi
      );
      return null;
    }
  };
  return (
    <div className="w-full h-full text-black p-8">
      <h1 className="w-full font-bold text-2xl">Quản lý đăng bài - sự kiện</h1>
      <div
        className="grid grid-flow-row grid-cols-2 w-auto h-auto"
        style={{
          backgroundImage: `url("data:image/svg+xml,%3csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 32 32' width='32' height='32' fill='none' stroke-width='2' stroke='%23d4d4d4'%3e%3cpath d='M0 .5H31.5V32'/%3e%3c/svg%3e")`,
        }}
      >
        <div className=" border rounded-xl min-h-[380px] h-full w-full p-2 flex justify-center">
          <EmojiPickerInput
            setTextData={setTextData}
            setHeaderData={setHeaderData}
            setStorageImg={setStorageImg}
            storageImg={storageImg}
            handleUploadPost={handleUploadPost}
          />
        </div>
        <div className=" border rounded-xl min-h-[380px] p-2 h-full">
          <SwipeCards setStorageImg={setStorageImg} storageImg={storageImg} />
        </div>
        <div className=" border rounded-xl min-h-[380px] p-2 h-full">
          <PreviewText textData={textData} headerData={headerData} />
        </div>
        <div className=" border rounded-xl min-h-[380px] p-2 h-full">
          <h1 className="w-full font-bold text-xl text-center my-2">
            Xem trước bài post
          </h1>
          <TwitterPost
            content={textData}
            postImages={storageImg}
            header={headerData}
          />
        </div>
      </div>
      {contextHolder}
    </div>
  );
};

export default ManagePost;

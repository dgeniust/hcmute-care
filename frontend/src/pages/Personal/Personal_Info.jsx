import React, { useEffect, useState } from "react";
import { Divider, Input, Select, DatePicker, Button, message, Avatar } from "antd";
import dayjs from "dayjs";
import {
  notifyErrorWithCustomMessage,
  handleHttpStatusCode,
  notifySuccessWithCustomMessage,
} from "../../utils/notificationHelper";
const dateFormat = "YYYY/MM/DD";

const Personal_Info = () => {
  const apiUrl = import.meta.env.VITE_API_BASE_URL;
  const [formData, setFormData] = useState({});
  const [messageApi, contextHolder] = message.useMessage();

  const handleChange = (value) => {
    console.log(`selected ${value}`);
  };

  const splitFullName = (fullName) => {
    if (!fullName || typeof fullName !== "string") {
      return { lastName: "", firstName: "" };
    }
    const words = fullName.trim().split(/\s+/);
    if (words.length === 0) {
      return { lastName: "", firstName: "" };
    }
    if (words.length === 1) {
      return { lastName: "", firstName: words[0] };
    }
    const firstName = words[words.length - 1];
    const lastName = words.slice(0, -1).join(" ");
    return { lastName, firstName };
  };

  const transferGender = (gender) => {
    if (gender === "MALE") return "nam";
    else return "nu";
  };

  const transferGenderToAPI = (gender) => {
    if (gender === "nam") return "MALE";
    if (gender === "nu") return "FEMALE";
    return gender;
  };

  useEffect(() => {
    const storedData = localStorage.getItem("customerDetailsData");
    let data = null;
    try {
      data = storedData ? JSON.parse(storedData) : null;
      console.log("get data from local storage: ", data);
      if (data) {
        const nameParts = splitFullName(data.fullName);
        setFormData({
          id: data.id || "",
          fullName: data.fullName || "",
          lastName: nameParts.lastName || "",
          firstName: nameParts.firstName || "",
          phone: data.phone || "",
          email: data.email || "",
          address: data.address || "",
          gender: transferGender(data.gender) || "nam",
          dob: data.dob || "",
          membership: data.membership || "",
          nation: data.nation || "",
        });
      }
    } catch (error) {
      console.error("Error parsing localStorage data:", error);
      notifyErrorWithCustomMessage(
        "Lỗi khi tải dữ liệu từ localStorage",
        messageApi
      );
    }
  }, [messageApi]);

  const handleInputChange = (key, value) => {
    setFormData((prevData) => ({
      ...prevData,
      [key]: value,
    }));
  };

  const formatDate = (inputDate) => {
    const parsedDate = dayjs(inputDate, "YYYY/MM/DD");
    if (!parsedDate.isValid()) {
      console.error("Invalid date format. Expected YYYY/MM/DD");
      return null;
    }
    return parsedDate.format("YYYY-MM-DD");
  };

  const handleSubmitUpdate = async () => {
    const accessToken = localStorage.getItem("accessToken");
    if (!accessToken) {
      notifyErrorWithCustomMessage("Vui lòng đăng nhập lại", messageApi);
      return;
    }
    const fullname = formData.lastName
      ? `${formData.lastName} ${formData.firstName}`
      : formData.firstName;
    const payload = {
      phone: formData.phone || "",
      fullName: fullname || "",
      email: formData.email || "",
      gender: transferGenderToAPI(formData.gender) || "",
      dob: formatDate(formData.dob) || "",
      nation: formData.nation || "",
      address: formData.address || "",
      membership: formData.membership,
    };
    console.log("payload: ", payload);
    try {
      const response = await fetch(`${apiUrl}v1/customers/${formData.id}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${accessToken}`,
        },
        body: JSON.stringify(payload),
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
      const updateData = { ...formData, ...payload };
      localStorage.setItem("customerDetailsData", JSON.stringify(updateData));
      notifySuccessWithCustomMessage(
        "Cập nhật thông tin thành công",
        messageApi
      );
      setTimeout(() => {
        window.location.reload();
      }, 1000);
    } catch (e) {
      notifyErrorWithCustomMessage(
        "Lỗi kết nối khi cập nhật thông tin",
        messageApi
      );
      console.error("Error updating customer:", e);
    }
  };

  return (
    <div className="w-full p-4 my-8 bg-white rounded-xl overflow-hidden">
      {/* Header Section */}
      <div className="relative w-full h-48 bg-gradient-to-r from-blue-500 to-indigo-600">
        <div className="absolute top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 flex flex-col items-center">
          <Avatar
            size={120}
            src={
              formData.gender === "nam"
                ? "https://api.dicebear.com/7.x/miniavs/svg?seed=8"
                : "https://api.dicebear.com/9.x/miniavs/svg?seed=Liliana"
            }
            className="border-4 border-white shadow-md"
          />
          <h1 className="mt-4 text-2xl font-bold text-white">
            {formData.fullName || "Tên người dùng"}
          </h1>
        </div>
        <Divider
          orientation="left"
          plain
          className="absolute bottom-0 w-full text-white"
        >
          <span className="text-white font-semibold">Thông tin cá nhân</span>
        </Divider>
      </div>

      {/* Form Section */}
      <div className="p-8">
        <form className="space-y-6">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            {/* Phone */}
            <div className="space-y-2">
              <label className="block text-sm font-medium text-gray-700">
                Số điện thoại
              </label>
              <Input
                value={formData.phone}
                disabled
                className="w-full rounded-md border-gray-300 bg-gray-100 text-gray-900"
                onChange={(e) => handleInputChange("phone", e.target.value)}
              />
            </div>

            {/* Last Name */}
            <div className="space-y-2">
              <label className="block text-sm font-medium text-gray-700">
                Họ và tên lót
              </label>
              <Input
                value={formData.lastName}
                className="w-full rounded-md border-gray-300 focus:ring-indigo-500 focus:border-indigo-500"
                onChange={(e) => handleInputChange("lastName", e.target.value)}
              />
            </div>

            {/* First Name */}
            <div className="space-y-2">
              <label className="block text-sm font-medium text-gray-700">
                Tên
              </label>
              <Input
                value={formData.firstName}
                className="w-full rounded-md border-gray-300 focus:ring-indigo-500 focus:border-indigo-500"
                onChange={(e) => handleInputChange("firstName", e.target.value)}
              />
            </div>

            {/* Date of Birth & Gender */}
            <div className="space-y-2">
              <div className="flex space-x-4">
                <div className="w-1/2">
                  <label className="block text-sm font-medium text-gray-700">
                    Ngày sinh
                  </label>
                  <DatePicker
                    value={
                      formData.dob && dayjs(formData.dob, dateFormat).isValid()
                        ? dayjs(formData.dob, dateFormat)
                        : null
                    }
                    format={dateFormat}
                    className="w-full rounded-md border-gray-300 focus:ring-indigo-500 focus:border-indigo-500"
                    onChange={(date, dateString) =>
                      handleInputChange("dob", dateString)
                    }
                  />
                </div>
                <div className="w-1/2">
                  <label className="block text-sm font-medium text-gray-700">
                    Giới tính
                  </label>
                  <Select
                    value={formData.gender}
                    defaultValue="nam"
                    className="w-full"
                    onChange={(value) => handleInputChange("gender", value)}
                    options={[
                      { value: "nam", label: "Nam" },
                      { value: "nu", label: "Nữ" },
                    ]}
                  />
                </div>
              </div>
            </div>

            {/* Email */}
            <div className="space-y-2">
              <label className="block text-sm font-medium text-gray-700">
                Email
              </label>
              <Input
                value={formData.email}
                className="w-full rounded-md border-gray-300 focus:ring-indigo-500 focus:border-indigo-500"
                onChange={(e) => handleInputChange("email", e.target.value)}
              />
            </div>

            {/* Nation */}
            <div className="space-y-2">
              <label className="block text-sm font-medium text-gray-700">
                Quốc gia
              </label>
              <Input
                value={formData.nation}
                className="w-full rounded-md border-gray-300 focus:ring-indigo-500 focus:border-indigo-500"
                onChange={(e) => handleInputChange("nation", e.target.value)}
              />
            </div>

            {/* Address */}
            <div className="space-y-2">
              <label className="block text-sm font-medium text-gray-700">
                Địa chỉ
              </label>
              <Input
                value={formData.address}
                className="w-full rounded-md border-gray-300 focus:ring-indigo-500 focus:border-indigo-500"
                onChange={(e) => handleInputChange("address", e.target.value)}
              />
            </div>

            {/* Membership */}
            <div className="space-y-2">
              <label className="block text-sm font-medium text-gray-700">
                Membership
              </label>
              <Input
                value={formData.membership}
                disabled
                className="w-full rounded-md border-gray-300 bg-gray-100 text-gray-900"
                onChange={(e) => handleInputChange("membership", e.target.value)}
              />
            </div>
          </div>

          {/* Submit Button */}
          <div className="flex justify-center mt-8">
            <Button
              type="primary"
              size="large"
              className="bg-indigo-600 hover:bg-indigo-700 px-8"
              onClick={handleSubmitUpdate}
            >
              Cập nhật thông tin
            </Button>
          </div>
        </form>
      </div>
      {contextHolder}
    </div>
  );
};

export default Personal_Info;
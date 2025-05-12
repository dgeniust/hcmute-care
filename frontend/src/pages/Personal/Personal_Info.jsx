import React, { useEffect, useState } from "react";
import { Divider, Input, Select, DatePicker, Button, message } from "antd";
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

  // Hàm chia fullName
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
  // Hàm chuyển gender từ "nam"/"nữ" sang "MALE"/"FEMALE"
  const transferGenderToAPI = (gender) => {
    if (gender === "nam") return "MALE";
    if (gender === "nu") return "FEMALE";
    return gender; // Cho các giá trị khác như "OTHER"
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
          gender: transferGender(data.gender) || "nam", // Default to 'nam' if undefined
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
      return null; // Or handle as needed
    }
    return parsedDate.format("YYYY-MM-DD");
  };

  // Xử lý gửi yêu cầu cập nhật
  const handleSubmitUpdate = async () => {
    const accessToken = localStorage.getItem("accessToken");
    if (!accessToken) {
      notifyErrorWithCustomMessage("Vui lòng đăng nhập lại", messageApi);
      return;
    }
    const fullname = formData.lastName
      ? `${formData.lastName} ${formData.firstName}`
      : formData.firstName;

    //Chuẩn bị body cho api
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

      //Cập nhật localStorage
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
    <div className="w-full h-full shadow-lg rounded-lg">
      <div className="w-full border border-black h-[150px] personal-bg relative">
        <Divider
          orientation="left"
          plain
          style={{ position: "absolute", bottom: 0, width: "100%" }}
        >
          <div className="w-[150px] h-[150px] rounded-full bg-white absolute border border-black top-1/2 left-1/6 transform -translate-x-1/2 -translate-y-1/3">
            {formData && formData.gender === "nam" ? (
              <img
                src="https://api.dicebear.com/7.x/miniavs/svg?seed=8"
                alt=""
                srcset=""
                className="object-center w-full h-full rounded-full"
              />
            ) : (
              <img
                src="https://api.dicebear.com/9.x/miniavs/svg?seed=Liliana"
                alt=""
                srcset=""
                className="object-center w-full h-full rounded-full"
              />
            )}
            <div className="text-black font-bold text-xl mt-4 text-center w-full">
              <h1>{formData.fullName}</h1>
            </div>
          </div>
        </Divider>
      </div>
      <div className="w-full h-fit mt-[140px] px-8 py-4">
        <form className="w-full h-full">
          <div className="grid grid-flow-row grid-cols-2 gap-4">
            <div className="text-black space-y-2 flex flex-col">
              <span className="font-bold">Số điện thoại</span>
              <Input
                value={formData.phone}
                disabled
                style={{ color: "black" }}
                onChange={(e) => handleInputChange("phone", e.target.value)}
              />
            </div>
            <div className="text-black space-y-2 flex flex-col">
              <span className="font-bold">Họ và tên lót</span>
              <Input
                value={formData.lastName}
                onChange={(e) => handleInputChange("lastName", e.target.value)}
              />
            </div>
            <div className="text-black space-y-2 flex flex-col">
              <span className="font-bold">Tên</span>
              <Input
                value={formData.firstName}
                onChange={(e) => handleInputChange("firstName", e.target.value)}
              />
            </div>
            <div className="flex flex-row w-full space-x-2">
              <div className="text-black space-y-2 flex flex-col w-full">
                <span className="font-bold">Ngày sinh</span>
                <DatePicker
                  value={
                    formData.dob && dayjs(formData.dob, dateFormat).isValid()
                      ? dayjs(formData.dob, dateFormat)
                      : null
                  }
                  format={dateFormat}
                  onChange={(date, dateString) =>
                    handleInputChange("dob", dateString)
                  }
                />
              </div>
              <div className="text-black space-y-2 flex flex-col w-full">
                <span className="font-bold">Giới tính</span>
                <Select
                  value={formData.gender}
                  defaultValue="nam"
                  style={{
                    width: "100%",
                  }}
                  onChange={(value) => handleInputChange("gender", value)}
                  options={[
                    {
                      value: "nam",
                      label: "Nam",
                    },
                    {
                      value: "nu",
                      label: "Nữ",
                    },
                  ]}
                />
              </div>
            </div>

            <div className='w-full h-fit mt-[140px] px-8 py-4'>
                <form className='w-full h-full'>
                    <div className='grid grid-flow-row grid-cols-2 gap-4'>
                        <div className='text-black space-y-2 flex flex-col'>
                            <span className='font-bold'>Số điện thoại</span>
                            <Input value={formData.phone} disabled style={{color: 'black'}}
                                onChange={(e) => handleInputChange('phone', e.target.value)}
                            />
                        </div>
                        <div className='text-black space-y-2 flex flex-col'>
                            <span className='font-bold'>Họ và tên lót</span>
                            <Input value={formData.lastName} 
                                onChange={(e) => handleInputChange('lastName', e.target.value)}
                            />
                        </div>
                        <div className='text-black space-y-2 flex flex-col'>
                            <span className='font-bold'>Tên</span>
                            <Input value={formData.firstName} 
                                onChange={(e) => handleInputChange('firstName', e.target.value)}
                            />
                        </div>
                        <div className='flex flex-row w-full space-x-2'>
                            <div className='text-black space-y-2 flex flex-col w-full'>
                                <span className='font-bold'>Ngày sinh</span>
                                <DatePicker value={formData.dob && dayjs(formData.dob, dateFormat).isValid() ? dayjs(formData.dob, dateFormat) : null} format={dateFormat} 
                                    onChange={(date, dateString) => handleInputChange('dob', dateString)}
                                />
                            </div>
                            <div className='text-black space-y-2 flex flex-col w-full'>
                                <span className='font-bold'>Giới tính</span>
                                <Select
                                value={formData.gender}
                                defaultValue="nam"
                                style={{
                                    width: '100%',
                                }}
                                onChange={(value) => handleInputChange('gender', value)}
                                options={[
                                    {
                                    value: 'nam',
                                    label: 'Nam',
                                    },
                                    {
                                    value: 'nu',
                                    label: 'Nữ',
                                    },
                                ]}
                                />
                            </div>
                        </div>
                        <div className='text-black space-y-2 flex flex-col'>
                            <span className='font-bold'>Email</span>
                            <Input value={formData.email} onChange={(e) => handleInputChange('email', e.target.value)}/>
                        </div>
                        <div className='text-black space-y-2 flex flex-col'>
                            <span className='font-bold'>Quốc gia</span>
                            <Input value={formData.nation} 
                                onChange={(e) => handleInputChange('nation', e.target.value)}
                            />
                        </div>
                        <div className='text-black space-y-2 flex flex-col'>
                            <span className='font-bold'>Địa chỉ</span>
                            <Input value={formData.address} 
                                onChange={(e) => handleInputChange('address', e.target.value)}
                            />
                        </div>
                        <div className='text-black space-y-2 flex flex-col'>
                            <span className='font-bold'>Membership</span>
                            <Input value={formData.membership} 
                            disabled
                                onChange={(e) => handleInputChange('membership', e.target.value)}
                            />
                        </div>
                    </div>
                    <div className='w-full flex justify-center mt-8'>
                        <Button type="primary" style={{padding: '20px'}} onClick={handleSubmitUpdate}>Cập nhật thông tin</Button>
                    </div>
                </form>
            </div>
          </div>
          <div className="w-full flex justify-center mt-8">
            <Button
              type="primary"
              style={{ padding: "20px" }}
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

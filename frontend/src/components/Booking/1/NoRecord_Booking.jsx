import React, {useState, useEffect} from "react";
import { Button, Input, Select,DatePicker, message } from 'antd';
import {ArrowLeftOutlined,HomeOutlined, SearchOutlined,InfoCircleTwoTone   } from '@ant-design/icons';
import dayjs from 'dayjs';
import { handleHttpStatusCode, notifySuccessWithCustomMessage, notifyErrorWithCustomMessage} from '../../../utils/notificationHelper';
const NoRecord_Booking = ({setStatus}) => {
    const customerId = localStorage.getItem('customerId');
    const payloadFormat = 'YYYY-MM-DD'
    const [typeForm, setTypeForm] = useState('inputRecords');
    const [messageApi, contextHolder] = message.useMessage();
    const [formData, setFormData] = useState({
        firstName: '',
        lastName: '',
        phone: '',
        email: '',
        cccd: '',
        dob: null,
        gender: '',
        nation: '',
        ethnicity: '',
        career: '',
        province: '',
        district: '',
        ward: '',
        address: ''
    });
    const [countries, setCountries] = useState([]);
    useEffect(() => {
        const fetchCountries = async () => {
            try {
              const response = await fetch("https://restcountries.com/v3.1/all");
              if (!response.ok) {
                throw new Error("Failed to fetch countries");
              }
              const data = await response.json();
              const countryOptions = data
                .map((country) => ({
                  value: country.cca2.toLowerCase(), // ISO2 code (e.g., 'vn')
                  label: country.name.common, // Country name (e.g., 'Vietnam')
                }))
                .sort((a, b) => a.label.localeCompare(b.label)); // Sort alphabetically
              setCountries(countryOptions);
              console.log("Fetched countries:", countryOptions); // Log after setting state
            } catch (error) {
              console.error("Error fetching countries:", error);
            }
          };
      
          fetchCountries();
    }, []);
    const handleInputChange = (e, field) => {
        setFormData({ ...formData, [field]: e.target.value });
    };
    const handleSelectChange = (value, field, option) => {
        console.log('Selected value:', option.label);
        if (field === 'gender') {
            setFormData({ ...formData, [field]: value === 'male' ? 'MALE' : 'FEMALE' });
        }
        if(field === 'nation'){
            setFormData({ ...formData, [field]: option.label });
        }
        if(field === 'career'){
            setFormData({ ...formData, [field]: value });
        }
    };
    
    const handleDateChange = (date) => {
        const formattedDate = date ? date.format(payloadFormat) : null;
        console.log('Selected date:', date, 'Formatted:', formattedDate);
        setFormData({ ...formData, dob: formattedDate });
    };
    const handleSetStatus = (value) => {
        setStatus(value);
    }
    const handleChange = (value) => {
        console.log(`selected ${value}`);
      };
    const handleSubmit = async () => {
        // Validate required fields
        const requiredFields = [
            "firstName",
            "lastName",
            "phone",
            "email",
            "dob",
            "gender",
            "nation",
            "career",
            "address",
        ];
        const missingFields = requiredFields.filter(
            (field) => !formData[field] || formData[field].trim() === ""
        );
    
        if (missingFields.length > 0) {
            console.error("Missing required fields:", missingFields);
            notifyErrorWithCustomMessage(`Vui lòng điền đầy đủ thông tin bắt buộc: ${missingFields.join(", ")}`, messageApi);
            return;
        }
        const payload = {
          patient: {
            name: `${formData.firstName} ${formData.lastName}`.trim(),
            cccd: formData.cccd,
            dob: formData.dob,
            gender: formData.gender.toUpperCase(), // Chuyển thành MALE/FEMALE
            address: formData.address,
            phone: formData.phone,
            email: formData.email,
            nation: formData.nation,
            career: formData.career
          },
          customerId: customerId,
        };
        console.log('Payload:', payload);
        // Gửi payload tới API hoặc xử lý tiếp
        try{
            const response = await fetch('http://localhost:8080/api/v1/medical-records', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: `Bearer ${localStorage.getItem('accessToken')}`,
                },
                body: JSON.stringify(payload),
            })
            if(!response.ok) {
                const errorText = await response.text();
                handleHttpStatusCode(response.status, '',`Tạo hồ sơ khám bệnh thất bại: ${errorText || response.statusText}`);
                return;
            }
            const data = await response.json();
            console.log('Raw API response:', data);
            if (data && data.data) {
                notifySuccessWithCustomMessage('Tạo hồ sơ khám bệnh thành công', messageApi);
                setStatus('records');
            } else {
                notifyErrorWithCustomMessage('Tạo hồ sơ khám bệnh thất bại', messageApi);
            }
        }
        catch (error) {
            console.error('Error submitting form:', error);
            notifyErrorWithCustomMessage('Lỗi khi gửi thông tin hồ sơ', messageApi);
        }

    };
    return (
        <div className='w-full h-full space-y-4'>
            <div className='flex flex-row gap-4 w-full h-full items-center'onClick={() => handleSetStatus('records')} >
                <Button icon={<ArrowLeftOutlined />} style={{backgroundColor:'transparent', border: 'none', boxShadow: 'none'}}></Button>
                <h1 className='text-black font-bold text-lg'>Tạo hồ sơ khám bệnh</h1>
            </div>
            <div className="flex flex-col space-y-4">
                <div className="flex flex-row items-center gap-2">
                    <InfoCircleTwoTone/>
                    <p className="text-black">Vui lòng nhập thông tin bên dưới</p>
                </div>
                <div>
                    <form action="" className="flex flex-col">
                        <div className="grid grid-flow-row grid-cols-2 gap-4 text-black">
                            <div>
                                <label htmlFor="" className="flex flex-row items-center space-x-2">Họ tên lót <span className="ml-1"><img width="10" height="10" src="https://img.icons8.com/forma-bold-filled-sharp/24/FA5252/asterisk.png" alt="asterisk"/></span><span className="text-xs italic">(theo CCCD/CMND/Passpot)</span></label>
                                <Input placeholder="VD: Nguyễn Thành..." 
                                    value={formData.firstName}
                                    onChange={(e) => handleInputChange(e, 'firstName')}
                                />
                            </div>
                            <div>
                                <label htmlFor="" className="flex flex-row items-center space-x-2">Tên<span className="ml-1"><img width="10" height="10" src="https://img.icons8.com/forma-bold-filled-sharp/24/FA5252/asterisk.png" alt="asterisk"/></span><span className="text-xs italic">(theo CCCD/CMND/Passpot)</span></label>
                                <Input placeholder="VD: Đạt..." 
                                    value={formData.lastName}
                                    onChange={(e) => handleInputChange(e, 'lastName')}
                                />
                            </div>
                            <div>
                                <label htmlFor="" className="flex flex-row items-center space-x-2">Số điện thoại<span className="ml-1"><img width="10" height="10" src="https://img.icons8.com/forma-bold-filled-sharp/24/FA5252/asterisk.png" alt="asterisk"/></span></label>
                                <Input placeholder="SĐT: 09xxxxxx..." 
                                    value={formData.phone}
                                    onChange={(e) => handleInputChange(e, 'phone')}    
                                />
                            </div>
                            <div>
                                <label htmlFor="" className="flex flex-row items-center space-x-2">Email<span className="ml-1"><img width="10" height="10" src="https://img.icons8.com/forma-bold-filled-sharp/24/FA5252/asterisk.png" alt="asterisk"/></span></label>
                                <Input placeholder="Email..." 
                                    value={formData.email}
                                    onChange={(e) => handleInputChange(e, 'email')}
                                />
                            </div>
                            <div>
                                <label htmlFor="">CCCD/CMND/Passpot</label>
                                <Input placeholder="077204..." 
                                    value={formData.cccd}
                                    onChange={(e) => handleInputChange(e, 'cccd')}
                                />
                            </div>
                            <div>
                                <label htmlFor="" className="flex flex-row items-center space-x-2">Ngày sinh<span className="ml-1"><img width="10" height="10" src="https://img.icons8.com/forma-bold-filled-sharp/24/FA5252/asterisk.png" alt="asterisk"/></span></label>
                                <DatePicker defaultValue={dayjs()} format={payloadFormat} style={{width:'100%'}}
                                    onChange={handleDateChange}
                                />
                            </div>
                            <div className="flex flex-row space-x-2 w-full">
                                <div className="w-full">
                                <label htmlFor="" className="flex flex-row items-center space-x-2">Giới tính<span className="ml-1"><img width="10" height="10" src="https://img.icons8.com/forma-bold-filled-sharp/24/FA5252/asterisk.png" alt="asterisk"/></span></label>
                                    <Select
                                    defaultValue="Chọn"
                                    style={{
                                        width: '100%',
                                    }}
                                    onChange={(value, option) => handleSelectChange(value, 'gender', option)}
                                    options={[
                                        {
                                        value: 'male',
                                        label: 'Nam',
                                        },
                                        {
                                        value: 'female',
                                        label: 'Nữ',
                                        },
                                    ]}
                                    />
                                </div>
                                <div className="w-full">
                                <label htmlFor="" className="flex flex-row items-center space-x-2">Quốc gia<span className="ml-1"><img width="10" height="10" src="https://img.icons8.com/forma-bold-filled-sharp/24/FA5252/asterisk.png" alt="asterisk"/></span></label>
                                    <Select
                                    defaultValue="Chọn"
                                    style={{
                                        width: '100%',
                                    }}
                                    onChange={(value, option) => handleSelectChange(value, 'nation', option)}
                                    options={countries}
                                    />
                                </div>
                            </div>
                            <div className="flex flex-row space-x-2">
                                <div className="w-full">
                                <label htmlFor="" className="flex flex-row items-center space-x-2">Nghề nghiệp<span className="ml-1"><img width="10" height="10" src="https://img.icons8.com/forma-bold-filled-sharp/24/FA5252/asterisk.png" alt="asterisk"/></span></label>
                                    <Select
                                    defaultValue="Chọn"
                                    style={{
                                        width: '100%',
                                    }}
                                    onChange={(value, option) => handleSelectChange(value, 'career', option)}
                                    options={[
                                        {
                                            value: 'vietnam',
                                            label: 'Việt Nam',
                                            },
                                            {
                                            value: 'nga',
                                            label: 'Nga',
                                            },
                                            {
                                            value: 'hanquoc',
                                            label: 'Hàn Quốc',
                                            },
                                    ]}
                                    />
                                </div>
                            </div>
                            <div>
                            <label htmlFor="" className="flex flex-row items-center space-x-2">Địa chỉ thường trú<span className="ml-1"><img width="10" height="10" src="https://img.icons8.com/forma-bold-filled-sharp/24/FA5252/asterisk.png" alt="asterisk"/></span></label>
                                <Input placeholder="Nhập chính xác địa chỉ..." 
                                    value={formData.address}
                                    onChange={(e) => handleInputChange(e, 'address')}
                                />
                            </div>
                        </div>
                        <div className="w-full h-fit flex justify-center items-center mt-8">
                            <Button style={{backgroundColor: '#273c75', color: 'white', fontWeight: 'bold', width: '30%', height: '40px'}} onClick={handleSubmit}>
                                Xác nhận
                            </Button>
                        </div>

                    </form>
                </div>
            </div>
            
            <div className='h-full w-fit'>
                <Button
                    style={{ backgroundColor: 'transparent' }}
                    icon={<HomeOutlined />}
                    onClick={() => handleSetStatus('records')} // Go back to records view
                >
                    Quay lại
                </Button>
            </div>      
            {contextHolder}                      
        </div>
    );
}
export default NoRecord_Booking;
import React, {useEffect, useState} from 'react';
import {Radio, DatePicker, InputNumber, Button, message, notification, Skeleton, Collapse  } from 'antd';
import { CheckCircleFilled } from '@ant-design/icons';
import dayjs from 'dayjs';
import BMIWeight from '../../components/bodies';
import '../../css/BMIContent.css';
const dateFormatList = ['DD/MM/YYYY', 'DD/MM/YY', 'DD-MM-YYYY', 'DD-MM-YY'];
const style = {
    display: 'flex',
    flexDirection: 'row',
    gap: 8,
  };
const BMIContent = () => {
    const [value, setValue] = useState(1);
    const [height, setHeight] = useState();
    const [weight, setWeight] = useState();
    const [bmi, setBMI] = useState();
    const [messageApi, contextHolder] = message.useMessage();
    const [api, contextNotificationHolder] = notification.useNotification();
    const [loading, setLoading] = useState(false);
    function error(message){
      messageApi.open({
        type: 'error',
        content: message,
      });
    };  
    function openNotification(pauseOnHover) {
      api.open({
        message: 'Thực hiện tính thành công',
        description:
          'Chỉ số BMI của bạn đã được tính thành công',
        showProgress: true,
        pauseOnHover,
        icon: (<CheckCircleFilled style={{color: '#2ed573'}}/>)
      });
  };
    const onChange = (e) => {
        setValue(e.target.value);
        console.log(e.target.value);
    };
    const handleHeightChange = (value) => {
      setHeight(value);
      console.log('type', value.type);
    };
    const handleWeightChange = (value) => {
      setWeight(value);
      console.log('changed', value);
    };
    const validateForm = () => {
      let valid = true;
      if (height === undefined) {
        valid = false;
        error("Vui lòng nhập chiều cao 🫠");
        console.log('Vui lòng nhập chiều cao');
      }
      if (weight === undefined) {
        valid = false;
        error("Vui lòng nhập cân nặng 🫠");
        console.log('Vui lòng nhập cân nặng');
      }
      return valid;
    }
    const handleSubmit = (e) => {
      e.preventDefault();
      const isValid = validateForm();
      if(isValid) {
        if (height && weight) {
          let bmi = weight / ((height / 100) * (height / 100));
          setBMI(bmi.toFixed(2));
          setLoading(true);
          setTimeout(() => {
            setLoading(false);
            openNotification(true);
          }, 2000);
        }
      }
      else {
          error("Hong tính được ní ơi🫠");
      }
    }
    const resultCal = bmi !== undefined && loading == false 
    const items = [
      {
        key: '1',
        label: '1. Những điều cần biết về BMI',
        children: <p>BMI (Body Mass Index hay chỉ số khối cơ thể) là một chỉ số dùng để đánh giá tình trạng cơ thể của một người dựa trên cân nặng và chiều cao. Công thức tính BMI là cân nặng (kg) chia cho bình phương chiều cao (mét). Thông qua việc tính chỉ số BMI, ta có thể nhận định mức độ gầy hay béo của cơ thể.</p>,
      },
      {
        key: '2',
        label: '2. Lợi ích khi tính chỉ số BMI',
        children: <p>Chỉ số BMI giúp đánh giá tình trạng cơ thể như gầy, bình thường, thừa cân, hoặc béo phì, từ đó đưa ra khuyến cáo về chế độ dinh dưỡng và tập luyện phù hợp.

        Hiện có 2 thang đo chỉ số BMI của Tổ chức Y tế Thế giới (WHO) (2) và Hiệp hội đái tháo đường các nước châu Á (IDI&WPRO). Trong đó, thang đo BMI của IDI&WPRO (hay thang đo BMI châu Á) phù hợp hơn với thể trạng người Việt Nam.</p>,
      },
      {
        key: '3',
        label: '3. Cách tính BMI và đánh giá chỉ số',
        children: <p>Công thức tính chỉ số BMI:

        BMI = Cân nặng (kg) / Chiều cao² (m²).
        
        Theo thang đo kết quả tính BMI của châu Á, chỉ số BMI dưới 18,5 là thiếu cân, 18.5-22,9 là bình thường, 23-24,9 là thừa cân, từ 25 trở lên là béo phì.</p>,
      },
      {
        key: '4',
        label: '4. Kết quả tính chỉ số BMI bao nhiêu là bình thường?',
        children: <p>Theo thang đo BMI châu Á, chỉ số BMI 18,5-22,9 được xem là cân nặng bình thường cho sức khỏe.</p>,
      },
      {
        key: '5',
        label: '5. Nguy cơ khi cơ chỉ số BMI thấp',
        children: <p>Kết quả test BMI thấp hơn 18,5 cho thấy cơ thể đang trong tình trạng thiếu cân, suy dinh dưỡng, loãng xương, miễn dịch suy yếu…</p>,
      },
      {
        key: '6',
        label: '6. Nguy cơ khi cơ chỉ số BMI cao',
        children: <p>Chỉ số BMI cao hơn 25 làm tăng nguy cơ mắc các bệnh tim mạch, đường huyết, huyết áp, đột quỵ, một số bệnh ung thư…</p>,
      },
      {
        key: '7',
        label: '7. Công thức tính BMI có chính xác không?',
        children: <p>Tính BMI online hoặc tự tính bằng công thức là sự tham khảo hữu ích nhưng không đánh giá được sự phân bố mỡ và khối lượng cơ bắp. Cần kết hợp với các phương pháp tính BMI khác để đánh giá sức khỏe toàn diện, ví dụ phân tích thành phần cơ thể bằng máy InBody.</p>,
      },
    ];

    return (
        <div className='flex flex-col items-center w-full h-fit gap-4 bg-white'>
            <h1 className='text-xl mt-4 text-[#273c75] font-bold'>CÔNG CỤ TÍNH BMI</h1>
            <div className='divider w-[100px] h-[5px] bg-gray-400 mb-4 mt-2'></div>
            <div className='w-fit h-fit border border-blue-600 flex flex-col items-center rounded-3xl mb-8'>
              <form action="" method="post" className='flex flex-col items-center p-8 text-blue-600 font-bold' onSubmit={handleSubmit}>
                  <div className='grid grid-cols-2 grid-flow-row gap-8 p-8 space-y-2 '>
                      <div className='flex flex-col'>
                          <label htmlFor="">Chiều cao</label>
                          <InputNumber addonAfter="cm" onChange={handleHeightChange}/>
                      </div>
                      <div className='flex flex-col'>
                          <label htmlFor="">Giới tính</label>
                          <Radio.Group
                              style={style}
                              onChange={onChange}
                              value={value}
                              options={[
                                  {
                                  value: 'Male',
                                  label: 'Nam',
                                  },
                                  {
                                  value: 'Female',
                                  label: 'Nữ',
                                  },
                              ]}
                              />
                      </div>
                      <div className='flex flex-col'>
                          <label htmlFor="">Cân nặng</label>
                          <InputNumber addonAfter="kg" onChange={handleWeightChange}/>
                      </div>
                      
                      <div className='flex flex-col'>
                          <label htmlFor="">Ngày sinh</label>
                          <DatePicker defaultValue={dayjs('01/01/2025', dateFormatList[0])} format={dateFormatList} />
                      </div>
                  </div>
                  <div className='w-[50%] flex items-center justify-center'>
                      <Button htmlType='submit' style={{width: '100%', height: '40px', color: 'white', backgroundColor: '#273c75', fontWeight: 'bold', fontSize: '15px'}}>
                      Xem kết quả
                      </Button>
                  </div>
              </form>
            </div>
            
            <Skeleton loading={loading} active>
            {
              resultCal ? (<div className='w-full h-fit flex flex-col items-center mb-8'>
                <div className="flex items-center justify-center">
                  <p className="text-3xl font-bold text-blue-600">
                    Your BMI index is{" "}
                    <span className="text-blue-500 font-bold">
                    {bmi} kg/m²
                    </span>
                    <br />
                    <p className="text-center text-base ">
                      Healthy BMI range: 18.5 kg/m² - 25 kg/m².
                    </p>
                  </p>
                </div>
  
                <BMIWeight BMI={bmi}/>
                <div className='w-full h-5'>
                  <div className="flex flex-row w-full h-5 rounded-lg">
                    <div className="w-3/6 bg-blue-500 rounded-l-lg"></div>
                    <div className="w-[140px] bg-green-500"></div>
                    <div className="w-[90px] bg-yellow-500"></div>
                    <div className="w-[190px] bg-orange-500"></div>
                    <div className="w-[210px] bg-red-500 rounded-r-lg"></div>
                  </div>
                  <div className="flex flex-row w-full h-full text-lg font-bold text-slate-500">
                    <div className="w-3/6 ">0</div>
                    <div className="w-[140px]">18.5</div>
                    <div className="w-[90px]">23</div>
                    <div className="w-[190px]">25</div>
                    <div className="w-[210px]">30+</div>
                  </div>
                </div>
                
              </div>) : null
            }
            </Skeleton>

            <div className='text-black mt-8 h-fit w-full bg-gray-100 flex items-center justify-center p-2'>
              <div className='w-[90%]'>
                <h1 className='font-bold text-2xl mb-4'>Miễn trừ trách nhiệm</h1>
                <p>Lưu ý, kết quả từ công cụ tính BMI online chỉ mang tính tham khảo, không thể thay thế các phương pháp chẩn đoán chuyên sâu tại cơ sở y tế. Nếu có nhu cầu chẩn đoán chính xác tình trạng cơ thể và sức khỏe</p>
                <p>Kết quả tính BMI trên giúp đánh giá tình trạng thừa cân, béo phì theo tiêu chuẩn của WHO áp dụng cho người Châu Á từ 18 tuổi trở lên, được Bộ Y tế công bố.  <span><a href="https://thuvienphapluat.vn/van-ban/The-thao-Y-te/Quyet-dinh-2892-QD-BYT-2022-tai-lieu-Huong-dan-chan-doan-va-dieu-tri-benh-beo-phi-533849.aspx">1</a>)</span></p>
              </div>
            </div>
            <div className='text-black mt-8 h-fit w-full flex items-center justify-center p-2'>
              <div className='w-[90%]'>
                <h1 className='font-bold text-2xl mb-4 text-[#273c75]'>Câu hỏi thường gặp</h1>
                <Collapse ghost items={items} defaultActiveKey={['1']}/>
              </div>
            </div>


            {contextHolder}
            {contextNotificationHolder}
        </div>
    );
}

export default BMIContent;
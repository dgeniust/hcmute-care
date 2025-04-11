import React, {useState} from 'react';
import {Radio, DatePicker, InputNumber, Button, message, notification, Skeleton , Select } from 'antd';
import { CheckCircleFilled } from '@ant-design/icons';
import dayjs from 'dayjs';
import BMIWeight from '../../components/bodies';
import '../../css/BMIContent.css';
import PlanListCalo from './PlanListCalo'
import BMISVG, {BMRSVG, TDEESVG} from './SVGCalculate';
import GenerateMeal from './GenerateMeal';
const dateFormatList = ['DD/MM/YYYY', 'DD/MM/YY', 'DD-MM-YYYY', 'DD-MM-YY'];
const style = {
    display: 'flex',
    flexDirection: 'row',
    gap: 8,
  };
const BMIContent = () => {
    const [height, setHeight] = useState();
    const [weight, setWeight] = useState();
    const [gender, setGender] = useState('male');
    const [dob, setDob] = useState();
    const [workout, setWorkout] = useState();
    const [bmi, setBMI] = useState();
    const [bmr, setBMR] = useState();
    const [tdee, setTDEE] = useState();
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
    const onGenderChange = (e) => {
      setGender(e.target.value);
    };

    const handleHeightChange = (value) => {
      setHeight(value);
    };

    const handleWeightChange = (value) => {
      setWeight(value);
    };

    const onDateChange = (date) => {
      if (!date) return; // Kiểm tra nếu không có ngày nhập vào

      const selectedDate = dayjs(date);
      const today = dayjs();

      let age = today.year() - selectedDate.year(); // Tính năm chênh lệch
      const birthdayThisYear = selectedDate.month(today.month()).date(today.date()); // Sinh nhật năm nay

      // Nếu hôm nay chưa qua sinh nhật, trừ đi 1 tuổi
      if (today.isBefore(birthdayThisYear)) {
        age -= 1;
      }

      console.log(age);
      setDob(age);
    }

    const onWorkoutChange = (wk) => {
      console.log(wk);
      setWorkout(wk);
    }
    const validateForm = () => {
      const validationRules = [
        { 
          field: height, 
          errorMessage: "Vui lòng nhập chiều cao 🫠",
          condition: height === undefined 
        },
        { 
          field: weight, 
          errorMessage: "Vui lòng nhập cân nặng 🫠",
          condition: weight === undefined 
        },
        { 
          field: dob, 
          errorMessage: "Vui lòng nhập ngày sinh 🫠",
          condition: dob === undefined 
        },
        { 
          field: workout, 
          errorMessage: "Vui lòng chọn cường độ hoạt động thể chất 🫠",
          condition: workout === undefined 
        }
      ];
    
      return validationRules.reduce((isValid, rule) => {
        if (rule.condition) {
          error(rule.errorMessage);
          return false;
        }
        return isValid;
      }, true);
    };
    //Tính BMR
    const calculateBMR = (gender, weight, height, dob) => {
      const bmrFormulas = {
        male: () => 66 + (13.7 * weight) + (5 * height) - (6.8 * dob),
        female: () => 655 + (9.6 * weight) + (1.8 * height) - (4.7 * dob)
      };
    
      return bmrFormulas[gender]();
    };
    //Tính TDEE
    const calculateTDEE = (bmr, activityLevel) => {
      return bmr * activityLevel;
    };

    const handleSubmit = (e) => {
      e.preventDefault();

      // Validate form
      const isValid = validateForm();
      if (!isValid) return;

      // Ensure required values exist
      if (!height || !weight) return;

      // Calculate BMI
      const bmi = weight / ((height / 100) ** 2);

      // Calculate BMR and TDEE
      const bmrResult = calculateBMR(gender, weight, height, dob);
      const tdeeResult = calculateTDEE(bmrResult, workout);

      // Update state
      setBMI(bmi.toFixed(2));
      setBMR(bmrResult.toFixed(2));
      setTDEE(Math.round(tdeeResult));

      // Simulate loading and show notification
      setLoading(true);
      setTimeout(() => {
        setLoading(false);
        openNotification(true);
      }, 2000);
    }

    const resultCal = bmi !== undefined && loading == false 
    
    return (
        <div className='flex flex-col items-center w-full h-fit gap-4 bg-white'>
            <h1 className='text-xl mt-4 text-[#273c75] font-bold'>CÔNG CỤ TÍNH BMI, BMR, TDEE</h1>
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
                              onChange={onGenderChange}
                              value={gender}
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
                      <div className='flex flex-col'>
                          <label htmlFor="">Cân nặng</label>
                          <InputNumber addonAfter="kg" onChange={handleWeightChange}/>
                      </div>
                      
                      <div className='flex flex-col'>
                          <label htmlFor="">Ngày sinh</label>
                          <DatePicker defaultValue={dayjs('01/01/2025', dateFormatList[0])} format={dateFormatList} onChange={onDateChange}/>
                      </div>
                      <div className='col-span-2 flex flex-col'>
                        <label htmlFor="">Chọn cường độ hoạt động thể chất của bạn</label>
                        <Select
                            defaultValue="Chọn"
                            style={{ width: 600 }}
                            onChange={onWorkoutChange}
                            options={[
                              { value: '1.2', label: 'Lối sống ít vận động (ít/không tập thể dục)' },
                              { value: '1.375', label: 'Tập thể dục nhẹ (1-2 ngày/tuần)' },
                              { value: '1.55', label: 'Tập thể dục vừa phải (3-5 ngày/tuần)' },
                              { value: '1.725', label: 'Rất năng động (6-7 ngày/tuần)' },
                              { value: '1.9', label: 'Rất tích cực (rất năng động và công việc đòi hỏi thể lực)' },
                            ]}
                          />
                      </div>
                  </div>
                  <div className='w-full flex items-center justify-center'>
                      <Button htmlType='submit' style={{width: '50%', height: '35px', color: 'white', backgroundColor: '#273c75', fontWeight: 'bold', fontSize: '15px'}}>
                      Xem kết quả
                      </Button>
                  </div>
              </form>
            </div>
            
            <Skeleton loading={loading} active>
            {
              resultCal ? (<div className='w-full h-fit flex flex-col items-center mb-8 space-y-2'>
                <div className="flex items-center justify-center flex-col space-y-8 mb-8">
                    <div className='flex flex-row justify-between items-center w-full h-fit space-x-2'>
                    <div
                      class="[--shadow:rgba(60,64,67,0.3)_0_1px_2px_0,rgba(60,64,67,0.15)_0_2px_6px_2px] w-4/5 h-auto rounded-2xl bg-white [box-shadow:var(--shadow)] min-w-[330px]"
                    >
                      <div
                        class="flex flex-col items-center justify-between pt-9 px-6 pb-6 relative z-10"
                      >
                        <span class="relative mx-auto -mt-16 z-10">
                          <BMISVG/>
                        </span>

                        <h5 class="text-base font-semibold text-zinc-700">
                          Chỉ số BMI của bạn là:{" "}{bmi} kg/m²
                        </h5>
                      </div>
                      
                    </div>
                    <div
                      class="[--shadow:rgba(60,64,67,0.3)_0_1px_2px_0,rgba(60,64,67,0.15)_0_2px_6px_2px] w-4/5 h-auto rounded-2xl bg-white [box-shadow:var(--shadow)] min-w-[330px]"
                    >
                      <div
                        class="flex flex-col items-center justify-between pt-9 px-6 pb-6 relative z-10"
                      >
                        <span class="relative mx-auto -mt-16 z-10">
                          <BMRSVG/>
                        </span>

                        <h5 class="text-base font-semibold text-zinc-700">
                          Chỉ số BMR của bạn là:{" "}{bmr} Kcal
                        </h5>
                      </div>
                      
                    </div>
                    <div
                      class="[--shadow:rgba(60,64,67,0.3)_0_1px_2px_0,rgba(60,64,67,0.15)_0_2px_6px_2px] w-4/5 h-auto rounded-2xl bg-white [box-shadow:var(--shadow)] min-w-[330px]"
                    >
                      <div
                        class="flex flex-col items-center justify-between pt-9 px-6 pb-6 relative z-10"
                      >
                        <span class="relative mx-auto -mt-16 z-10">
                          <TDEESVG/>
                        </span>

                        <h5 class="text-base font-semibold text-zinc-700">
                          Chỉ số TDEE của bạn là:{" "}{tdee} Kcal
                        </h5>
                      </div>
                      
                    </div>
                    </div>
                    <p className="text-center text-xl font-bold">
                      Healthy BMI range: 18.5 kg/m² - 25 kg/m².
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
                <div className='w-fit h-fit flex items-center justify-center mt-8 p-4 bg-blue-100 rounded-xl'>
                  <h1 className='font-bold text-lg'>GIỮ CÂN<span className='text-xl font-bold text-sky-800'> {tdee} </span>CALO/NGÀY</h1>
                </div>
                <PlanListCalo tdee= {tdee}/>
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
            <div className='w-full h-fit'>
              <GenerateMeal tdee= {tdee} />
            </div>

            {contextHolder}
            {contextNotificationHolder}
        </div>
    );
}

export default BMIContent;
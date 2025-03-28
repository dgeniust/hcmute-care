import React from 'react';
import {Collapse} from 'antd';

const InfoCalculate = () => { 
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
        {
          key :'8',
          label :'8. Tỷ lệ trao đổi chất cơ bản (BMR) là gì? - Định nghĩa BMR',
          children : <p>Cơ thể chúng ta đốt cháy calo liên tục trong ngày để duy trì các chức năng sống cơ bản như hô hấp, tuần hoàn và tiêu hóa. BMR (Basal Metabolic Rate – tỉ lệ trao đổi chất cơ bản) là lượng calo tối thiểu cần thiết cho các chức năng này khi cơ thể nghỉ ngơi.<br/>
          Chỉ số này cũng xác định tốc độ cơ thể có thể đốt cháy calo, do đó sẽ thể hiện được mối liên hệ giữa lượng calo với khối lượng cơ thể.</p>
        },
        {
          key :'9',
          label :'9. Cách tính BMR',
          children : <p>Một cách phổ biến để tính tỷ lệ trao đổi chất cơ bản BMR là công thức Harris-Benedict:<br/>
          Nữ giới: BMR = 655 + (9,6 × trọng lượng tính bằng kg) + (1,8 × chiều cao tính bằng cm) – (4,7 × tuổi tính theo năm)<br/>
          Nam giới: BMR = 66 + (13,7 × trọng lượng tính bằng kg) + (5 × chiều cao tính bằng cm) – (6,8 × tuổi tính theo năm)<br/>
          Bạn cũng có thể sử dụng công cụ tính BMR của HelloBacsi để dễ dàng và nhanh chóng có kết quả.</p>
        },
        {
          key :'10',
          label :'10. Sự khác biệt giữa BMR và TDEE là gì?',
          children : <p>Nếu BMR ước tính lượng calo tối thiểu của bạn cho các chức năng cơ bản của cơ thể, thì Tổng năng lượng tiêu thụ hàng ngày (TDEE) đo lường lượng calo bạn đốt cháy mỗi ngày, bao gồm cả hoạt động thể chất.</p>
        },
      ];
    return (
        <>
            <div className='text-black mt-8 h-fit w-full flex items-center justify-center p-2'>
              <div className='w-[90%]'>
                <h1 className='font-bold text-2xl mb-4 text-[#273c75]'>Câu hỏi thường gặp</h1>
                <Collapse ghost items={items} defaultActiveKey={['1']}/>
              </div>
            </div>
        </>
    )
}

export default InfoCalculate;
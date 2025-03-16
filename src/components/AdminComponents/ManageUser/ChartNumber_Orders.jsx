import React from 'react';
import { Bar } from 'react-chartjs-2';
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend,
} from 'chart.js';

ChartJS.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend
);

const ChartNumber_Orders = () => {
  const data = {
    labels: [
      'BỆNH LÝ CỘT SỐNG',
      'CHĂM SÓC GIẢM NHẸ',
      'CHUYÊN GIA THẦN KINH',
      'DA LIỄU',
      'ĐAU MẠN TÍNH',
      'DỊ ỨNG - MIỄN DỊCH LÂM SÀNG',
      'HẬU MÔN HỌC',
      'Hen-COPD',
      'HÌNH ẢNH HỌC CAN THIỆP',
      'HÓA TRỊ',
      'HUYẾT HỌC',
      'KHÁM VÀ TƯ VẤN DINH DƯỠNG',
      'LÃO KHOA',
      'LỒNG NGỰC - MẠCH MÁU',
      'MẮT',
      'NAM KHOA',
      'NGOẠI THẦN KINH',
      'NGOẠI TIM MẠCH',
      'NHI',
      'NIỆU HỌC CHỨC NĂNG',
      'NỘI CƠ XƯƠNG KHỚP',
      'NỘI THẬN',
      'NỘI TIẾT',
      'PARKINSON VÀ RỐI LOẠN VẬN ĐỘNG',
      'PHẪU THUẬT HÀM MẶT - RHM',
      'PHỔI',
      'PHỤC HỒI CHỨC NĂNG',
      'SẢN - PHỤ KHOA',
      'SẢN KHOA-CHẨN ĐOÁN TRƯỚC SINH',
      'SƠ SINH',
      'SUY TIM',
      'TAI MŨI HỌNG',
      'TÂM THẦN KINH',
      'THẨM MỸ DA',
      'THẦN KINH',
      'THẦN KINH NHI',
      'THẬN NHÂN TẠO - LỌC MÀNG BỤNG',
      'TIẾT NIỆU',
      'TIÊU HÓA GAN MẬT',
      'TIM BẨM SINH NGƯỜI LỚN',
      'TIM MẠCH',
      'TỔNG QUÁT',
      'TƯ VẤN TÂM LÝ',
      'UNG BƯỚU',
      'UNG BƯỚU GAN MẬT VÀ GHÉP GAN',
      'VIÊM GAN',
      'VÚ',
      'XƯƠNG KHỚP CHỈNH HÌNH',
      'Y HỌC GIA ĐÌNH',
    ],
    datasets: [
      {
        label: 'Số phiếu khám bệnh',
        data: [
          12, 19, 3, 5, 2, 3, 7, 8, 9, 10, 11, 13, 14, 15, 16, 17, 18, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50,
        ],
        backgroundColor: 'rgba(54, 162, 235, 0.5)', // Màu nền cho cột
        borderColor: 'rgb(54, 162, 235)', // Màu viền cho cột
        borderWidth: 1,
      },
    ],
  };

  const options = {
    responsive: true,
    plugins: {
      legend: {
        position: 'top',
      },
      title: {
        display: true,
        text: 'Số phiếu khám bệnh theo chuyên khoa',
      },
    },
    scales: {
      y: {
        beginAtZero: true, // Bắt đầu trục y từ 0
      },
    },
  };

  return <Bar data={data} options={options} />;
};

export default ChartNumber_Orders;
import React from 'react';
import { Doughnut } from 'react-chartjs-2';
import { Chart as ChartJS, ArcElement, Tooltip, Legend } from 'chart.js';

ChartJS.register(ArcElement, Tooltip, Legend);

const DonutChart = ({ data, labels }) => {
  const chartData = {
    labels: labels,
    datasets: [
      {
        data: data,
        backgroundColor: [
          '#8884d8', // shoes
          '#82ca9d', // jeans
          '#8dd3c7', // sweatshirt
          '#fbb4ae', // dresses
          '#ffffb3', // sports suit
          '#bebada', // others
        ],
        borderWidth: 0,
        cutout: '70%', // Controls the donut hole size
      },
    ],
  };

  const options = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        position: 'bottom',
        labels: {
          usePointStyle: true,
          pointStyle: 'circle',
          padding: 20,
        },
      },
      tooltip: {
        callbacks: {
          label: (context) => {
            const label = context.label || '';
            const value = context.parsed || 0;
            return `${label}: ${value}%`;
          },
        },
      },
    },
  };

  return <Doughnut data={chartData} options={options} />;
};

export default DonutChart;
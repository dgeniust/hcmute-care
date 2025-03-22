import React from 'react';
import { Line } from 'react-chartjs-2';
import {
    Chart as ChartJS,
    CategoryScale,
    LinearScale,
    PointElement,
    LineElement,
    Title,
    Tooltip,
    Legend,
    Filler,
} from 'chart.js';
  
ChartJS.register(
    CategoryScale,
    LinearScale,
    PointElement,
    LineElement,
    Title,
    Tooltip,
    Legend,
    Filler
);
const LineChart = ({ data, labels }) => { 
    const chartData = {
        labels: labels,
        datasets: [
          {
            label: 'Doanh thu',
            data: data,
            fill: true,
            backgroundColor: 'rgba(125, 137, 228, 0.3)', // Adjust color as needed
            borderColor: 'rgb(102, 117, 223)', // Adjust color as needed
            pointBackgroundColor: 'rgb(102, 117, 223)', // Adjust color as needed
            pointBorderColor: '#fff',
            pointHoverBackgroundColor: '#fff',
            pointHoverBorderColor: 'rgb(102, 117, 223)', // Adjust color as needed
            tension: 0.4, // Thêm thuộc tính tension
          },
        ],
      };
    
      const options = {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: {
            display: false,
          },
          tooltip: {
            callbacks: {
              label: function (context) {
                let label = context.dataset.label || '';
                if (label) {
                  label += ': ';
                }
                if (context.parsed.y !== null) {
                  label += new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' }).format(context.parsed.y);
                }
                return label;
              },
            },
          },
        },
        scales: {
          x: {
            grid: {
              display: false,
            },
          },
          y: {
            beginAtZero: true,
            ticks: {
              callback: function (value, index, values) {
                if (value >= 1000) {
                  return '$' + value / 1000 + 'K';
                }
                return '$' + value;
              },
            },
          },
        },
      };
      
    return (
        <Line data={chartData} options={options} />
    )

}

export default LineChart;
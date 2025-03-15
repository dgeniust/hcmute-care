import React from 'react';
import {Modal, Tag  } from 'antd';
const TransactionTable = ({ isModalOpen, setIsModalOpen }) => {
    
    
    const handleOk = () => {
        setIsModalOpen(false);
    };
    const handleCancel = () => {
        setIsModalOpen(false);
    };
    const transactions = [
        {
          id: '#67890',
          customerName: 'Dianne Russell',
          accountData: 'VISA *** 346',
          transactionDate: '06/12/2023',
          product: 'Cashmere Sweater',
          amount: '$79.99',
          refund: null,
          status: 'complete',
        },
        {
          id: '#54321',
          customerName: 'Esther Howard',
          accountData: '*** 457',
          transactionDate: '05/12/2023',
          product: 'Ultra Skinny ',
          amount: '$120.99',
          refund: '-$120.99',
          status: 'failed',
        },
        {
          id: '#98765',
          customerName: 'Kristin Watson',
          accountData: '*** 346',
          transactionDate: '09/12/2023',
          product: 'Light Cotton',
          amount: '$34.87',
          refund: null,
          status: 'processing',
        },
        {
          id: '#78901',
          customerName: 'Cody Fisher',
          accountData: 'VISA *** 369',
          transactionDate: '23/11/2023',
          product: 'Alpaca Wool',
          amount: '$89.00',
          refund: '-$89.00',
          status: 'canceled',
        },
        {
          id: '#67890',
          customerName: 'Dianne Russell',
          accountData: 'VISA *** 346',
          transactionDate: '06/12/2023',
          product: 'Cashmere Wool Sweater',
          amount: '$79.99',
          refund: null,
          status: 'complete',
        },
        {
          id: '#54321',
          customerName: 'Esther Howard',
          accountData: '*** 457',
          transactionDate: '05/12/2023',
          product: 'Ultra Skinny Denim Jeans',
          amount: '$120.99',
          refund: '-$120.99',
          status: 'canceled',
        },
        {
          id: '#98765',
          customerName: 'Kristin Watson',
          accountData: '*** 346',
          transactionDate: '09/12/2023',
          product: 'Light Cotton Polo Shirt',
          amount: '$34.87',
          refund: null,
          status: 'processing',
        },
        {
          id: '#78901',
          customerName: 'Cody Fisher',
          accountData: 'VISA *** 369',
          transactionDate: '23/11/2023',
          product: 'Alpaca Wool Warm Pullover',
          amount: '$89.00',
          refund: '-$89.00',
          status: 'complete',
        },
        {
          id: '#12345',
          customerName: 'Jane Smith',
          accountData: 'MasterCard *** 123',
          transactionDate: '10/12/2023',
          product: 'Silk Scarf',
          amount: '$45.50',
          refund: null,
          status: 'canceled',
        },
        {
          id: '#67891',
          customerName: 'Michael Johnson',
          accountData: 'PayPal *** 789',
          transactionDate: '12/12/2023',
          product: 'Leather Belt',
          amount: '$65.00',
          refund: null,
          status: 'failed',
        },
        {
          id: '#23456',
          customerName: 'Emily Davis',
          accountData: 'Amex *** 567',
          transactionDate: '15/12/2023',
          product: 'Wool Gloves',
          amount: '$29.99',
          refund: '-$29.99',
          status: 'processing',
        },
        {
          id: '#78902',
          customerName: 'David Wilson',
          accountData: 'VISA *** 234',
          transactionDate: '18/12/2023',
          product: 'Cotton Socks',
          amount: '$12.75',
          refund: null,
          status: 'complete',
        },
        {
          id: '#34567',
          customerName: 'Sarah Brown',
          accountData: 'MasterCard *** 890',
          transactionDate: '20/12/2023',
          product: 'Denim Jacket',
          amount: '$110.00',
          refund: null,
          status: 'processing',
        },
        {
          id: '#89012',
          customerName: 'Kevin Lee',
          accountData: 'PayPal *** 345',
          transactionDate: '22/12/2023',
          product: 'Linen Shirt',
          amount: '$55.25',
          refund: '-$55.25',
          status: 'failed',
        },
        {
          id: '#45678',
          customerName: 'Jessica Garcia',
          accountData: 'Amex *** 901',
          transactionDate: '25/12/2023',
          product: 'Cashmere Scarf',
          amount: '$80.00',
          refund: null,
          status: 'complete',
        },
        {
          id: '#90123',
          customerName: 'Brian Rodriguez',
          accountData: 'VISA *** 456',
          transactionDate: '27/12/2023',
          product: 'Leather Wallet',
          amount: '$38.50',
          refund: null,
          status: 'failed',
        },
        {
          id: '#56789',
          customerName: 'Ashley Martinez',
          accountData: 'MasterCard *** 012',
          transactionDate: '29/12/2023',
          product: 'Wool Hat',
          amount: '$22.00',
          refund: '-$22.00',
          status: 'processing',
        },
        {
          id: '#01234',
          customerName: 'Christopher Anderson',
          accountData: 'PayPal *** 567',
          transactionDate: '31/12/2023',
          product: 'Silk Tie',
          amount: '$50.75',
          refund: null,
          status: 'canceled',
        },
      ];
    const headers = [
        'Số Hiệu',
        'Tên khách hàng',
        'Thông Tin Tài Khoản',
        'Thời Gian',
        'Sản Phẩm/Dịch Vụ',
        'Tổng Tiền',
    ];
    const headersModal = [
      'Số Hiệu',
      'Tên khách hàng',
      'Thông Tin Tài Khoản',
      'Thời Gian',
      'Trạng thái',
      'Sản Phẩm/Dịch Vụ',
      'Tổng Tiền',
  ];

  return (
    
    <>
        <table className="w-full divide-y divide-gray-200">
            <thead className="bg-gray-50">
            <tr>
                {headers.map((header) => (
                <th
                    key={header}
                    className="p-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                >
                    {header}
                </th>
                ))}
            </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200">
            {transactions.slice(0, 4).map((transaction) => (
                <tr key={transaction.id} className={`${transaction.status === 'complete' ? 'bg-green-200' : transaction.status === 'failed' ? 'bg-red-200' : transaction.status === 'processing' ? 'bg-blue-200' : 'bg-yellow-200'}`}>
                <td className="p-3 whitespace-nowrap">{transaction.id}</td>
                <td className="p-3 whitespace-nowrap">{transaction.customerName}</td>
                <td className="p-3 whitespace-nowrap">{transaction.accountData}</td>
                <td className="p-3 whitespace-nowrap">{transaction.transactionDate}</td>
                <td className="p-3 whitespace-nowrap">{transaction.product}</td>
                <td className="p-3 whitespace-now-rap">{transaction.amount}</td>
                </tr>
            ))}
            </tbody>
        </table>
        <Modal title="Basic Modal" open={isModalOpen} onOk={handleOk} onCancel={handleCancel} width={1000} style={{
          top: 20,
        }}>
        <table className="min-w-full divide-y divide-gray-200">
            <thead className="bg-gray-50">
                <tr>
                    {headersModal.map((header) => (
                    <th
                        key={header}
                        className="p-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                    >
                        {header}
                    </th>
                    ))}
                </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200">
                {transactions.map((transaction) => (
                <tr key={transaction.id}>
                    <td className="p-3 whitespace-nowrap">{transaction.id}</td>
                    <td className="p-3 whitespace-nowrap">
                    {transaction.customerName}
                    </td>
                    <td className="p-3 whitespace-nowrap">
                    {transaction.accountData}
                    </td>
                    <td className="p-3 whitespace-nowrap">
                    {transaction.transactionDate}
                    </td>
                    <td className="p-3 whitespace-nowrap">
                    {/* {transaction.status} */}
                    {
                        transaction.status === 'complete' ? (
                        <Tag color="green">complete</Tag>
                        ) : transaction.status === 'failed' ? (
                        <Tag color="red">failed</Tag>
                        ) : transaction.status === 'processing' ? (
                          <Tag color="blue">processing</Tag>
                        ) : (
                          <Tag color="gold">canceled</Tag>
                        )
                    }
                    </td>
                    <td className="p-3 whitespace-nowrap">{transaction.product}</td>
                    <td className="p-3 whitespace-nowrap">
                    {transaction.amount}
                    </td>
                </tr>
                ))}
            </tbody>
            </table>
        </Modal>    
    </>
  );
};

export default TransactionTable;
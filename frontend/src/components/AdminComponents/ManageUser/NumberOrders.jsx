import React, {useState} from 'react';
import {Input, Pagination, Tag} from 'antd';

const {Search} = Input;
const NumberOrders = () => {

    const headers = [
        'STT',
        'Barcode',
        'Tên khách hàng',
        'Số phiếu',
        'Ngày sinh',
        'Giờ khám dự kiến',
        'Phòng'
    ];
    const generateFakeData = (count) => {
        const data = [];
        const randomNames = ['Nguyễn Văn A', 'Trần Thị B', 'Lê Hoàng C', 'Phạm Thu D', 'Hoàng Minh E'];
        const randomRooms = [
            'Phòng 33 - Khám Nội lầu 1 Khu A',
            'Phòng 12 - Khám Ngoại lầu 2 Khu B',
            'Phòng 45 - Khám Nhi lầu 3 Khu C',
            'Phòng 21 - Khám Tim mạch lầu 1 Khu D',
            'Phòng 56 - Khám Sản phụ khoa lầu 2 Khu A',
        ];
        const randomTimes = [
            '8:00 - 9:00',
            '9:00 - 10:00',
            '10:00 - 11:00',
            '13:00 - 14:00',
            '14:00 - 15:00',
        ];

        for (let i = 1; i <= count; i++) {
            const randomName = randomNames[Math.floor(Math.random() * randomNames.length)];
            const randomRoom = randomRooms[Math.floor(Math.random() * randomRooms.length)];
            const randomTime = randomTimes[Math.floor(Math.random() * randomTimes.length)];
            const randomDay = Math.floor(Math.random() * 28) + 1;
            const randomMonth = Math.floor(Math.random() * 12) + 1;
            const randomYear = Math.floor(Math.random() * 50) + 1970;
            const randomTicketNumber = Math.floor(Math.random() * 99) + 1;
            const barcode = `W25-${Math.floor(Math.random() * 100000000)}`;
            data.push({
                stt: i,
                barcode: barcode,
                name: randomName,
                ticketNumber: randomTicketNumber,
                dob: `${randomDay}/${randomMonth}/${randomYear}`,
                time: randomTime,
                room: randomRoom,
            });
        }
        return data;
    };
    const getAreaTag = (room) => {
        if (room.includes('Khu A')) {
            return <Tag color="red">{room}</Tag>;
        } else if (room.includes('Khu B')) {
            return <Tag color="green">{room}</Tag>;
        } else if (room.includes('Khu C')) {
            return <Tag color="blue">{room}</Tag>;
        }
        return <Tag color="yellow">{room}</Tag>; // Trả về null nếu không tìm thấy khu vực
    };
    const [tableData, setTableData] = useState(generateFakeData(30)); // Generate 10 rows of fake data
    //Search 
    const [searchService, setSearchService] = useState('');
    const [filteredUsers, setFilteredUsers] = useState(tableData);
    // Hàm xử lý tìm kiếm
    const handleSearch = (value) => {
        setSearchService(value);
        const filtered = tableData.filter((data) =>
            data.name && data.name.toLowerCase().includes(value.toLowerCase())
        );
        setFilteredUsers(filtered);
        setCurrentPage(1); // Reset về trang 1 khi tìm kiếm
    };

    // Paginations
    const [currentPage, setCurrentPage] = useState(1);
    const [itemsPerPage, setItemsPerPage] = useState(10);

    const indexOfLastItem = currentPage * itemsPerPage;
    const indexOfFirstItem = indexOfLastItem - itemsPerPage;
    const currentItems = filteredUsers.slice(indexOfFirstItem, indexOfLastItem);

    const handlePageChange = (page) => {
        setCurrentPage(page);
    };
    return (
        <div className='w-full h-fit space-y-4'>
            <Search
            placeholder="Tìm kiếm số phiếu 🔍"
            style={{
                width: '100%',
                marginTop: '20px',
            }}
            value={searchService}
            onChange={(e) => handleSearch(e.target.value)}
            size="large"
            />
            <table className="min-w-full divide-y divide-gray-200">
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
                    {
                        currentItems.map((row, index) => (
                            <tr key={index}>
                                <td className="p-3 whitespace-nowrap">{row.stt}</td>
                                <td className="p-3 whitespace-nowrap">{row.barcode}</td>
                                <td className="p-3 whitespace-nowrap">{row.name}</td>
                                <td className="p-3 whitespace-nowrap">{row.ticketNumber}</td>
                                <td className="p-3 whitespace-nowrap">{row.dob}</td>
                                <td className="p-3 whitespace-nowrap">{row.time}</td>
                                <td className="p-3 whitespace-nowrap">{getAreaTag(row.room)}</td>
                            </tr>
                        ))
                    }
                </tbody>
            </table>
            <Pagination 
                align="center"
                current={currentPage}
                defaultCurrent={1}
                total={tableData.length} // Use users.length for total items
                pageSize={itemsPerPage}
                onChange={handlePageChange} />
        </div>
    )
}

export default NumberOrders;
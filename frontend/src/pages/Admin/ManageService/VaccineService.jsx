import React, {useEffect, useState} from 'react';
import axios from 'axios';
import { Input, Pagination, Skeleton, Select, Button } from 'antd';
import {ReloadOutlined} from '@ant-design/icons';
const { Search } = Input;
const VaccineService = () => {
    const [datas, setDatas] = useState([]);
    const [searchService, setSearchService] = useState('');
    const [loadingSke, setLoadingSke] = useState(false);
    const showSkeleton = () => {
        setLoadingSke(true);
        setTimeout(() => {
            setLoadingSke(false);
        }, 500);
    };
    useEffect(() => {
        axios.get('https://raw.githubusercontent.com/dgeniust/hcmute-care/refs/heads/main/frontend/src/components/Personal/Data_Personal/vaccine_price.json')
            .then(response => {
            setDatas(response.data.bang_gia_tiem_chung); // Đảm bảo API trả về một mảng
            })
            .catch(error => {
            console.log('error: '+ error);
            });
        console.log(datas);
    }, []);
    const [filteredVaccine, setFilteredVaccine] = useState(datas);
    const [filteredManufacturer, setFilteredManufacturer] = useState(undefined);
    const [filteredStatus, setFilteredStatus] = useState(undefined);
    const headers = [
        'STT',
        'Vaccine',
        'Phòng bệnh',
        'Nước sản xuất',
        'Giá bán lẻ',
        'Giá ưu đãi',
        'Tình trạng'
    ];
    const manufacturerOptions = [
        { value: 'vietnam', label: 'Việt Nam' },
        { value: 'cuba', label: 'Cu Ba' },
        { value: 'india', label: 'Ấn Độ' },
        { value: 'korea', label: 'Hàn Quốc' },
        { value: 'netherlands', label: 'Hà Lan' },
        { value: 'france', label: 'Pháp' },
        { value: 'usa', label: 'Mỹ' },
        { value: 'belgium', label: 'Bỉ' },
        { value: 'canada', label: 'Canada' },
        { value: 'thailand', label: 'Thái Lan' },
        { value: 'germany', label: 'Đức' },
        { value: 'italia', label: 'Ý' },
      ];
    
    // Paginations
    const [currentPage, setCurrentPage] = useState(1);
    const [itemsPerPage, setItemsPerPage] = useState(30);

    const indexOfLastItem = currentPage * itemsPerPage;
    const indexOfFirstItem = indexOfLastItem - itemsPerPage;
    const currentItems = filteredVaccine.slice(indexOfFirstItem, indexOfLastItem);

    const handlePageChange = (page) => {
        setCurrentPage(page);
    };

    //Search 
    const handleSearch = (value) => {
        setSearchService(value);
        filtered();
    }

    const filtered = () => {
        let filter = datas.filter((data) => {
            const searchMatch = !searchService || (data.vaccine && data.vaccine.toLowerCase().includes(searchService.toLowerCase()));
            const manufacturerMatch = !filteredManufacturer || (data.manufacturer && data.manufacturer.includes(filteredManufacturer));
            const statusMatch = !filteredStatus || (data.status && data.status.includes(filteredStatus));
            return searchMatch && manufacturerMatch && statusMatch;
        })
        setFilteredVaccine(filter);
        setCurrentPage(1);
    }

    const handleManufacturerChange = (value, country) => {
        setFilteredManufacturer(country?.label || undefined);
        filtered();
    }
    const handleStatusChange = (value, status) => {
        setFilteredStatus(status?.label || undefined);
        filtered();
    }
    const handleResetSearch = () => {
        setSearchService('');
        setFilteredManufacturer(undefined);
        setFilteredStatus(undefined);
        setFilteredVaccine(datas);
        setCurrentPage(1);
    }
    useEffect(() => {
        showSkeleton();
        setFilteredVaccine(datas);
        filtered();
    }, [searchService, filteredManufacturer, filteredStatus, datas]);
    return (
        <div className='w-full h-full p-8 text-black flex flex-col items-center justify-center text-start space-y-4'>
            <h1 className='font-bold text-xl'>Bảng giá Vaccine</h1>
            <div className='flex flex-row justify-between items-center w-full'>
                <div className='flex flex-row gap-4 w-full'>
                    <Select
                    style={{ width: 170 }}
                    value={filteredManufacturer || "Chọn nhà sản xuất"}
                    placeholder="Chọn nước sản xuất"
                    onChange={handleManufacturerChange}
                    options={manufacturerOptions}
                    />
                    <Select
                    style={{ width: 170 }}
                    value={filteredStatus || "Chọn tình trạng"}
                    placeholder="Chọn tình trạng"
                    onChange={handleStatusChange}
                    options={
                        [
                            {
                                value: 'yes',
                                label: 'Có',
                            },
                            {
                                value: 'no',
                                label: 'Không',
                            }
                        ]
                    }
                    />
                    <Button icon={<ReloadOutlined />} onClick={handleResetSearch}></Button>
                </div>
                <Search
                placeholder="Tìm kiếm vaccine"
                style={{
                    width: '50%',
                }}
                value={searchService}
                onChange={(e) => handleSearch(e.target.value)}
                size="large"
                />
            </div>
            <Skeleton loading={loadingSke}>
            <table className="w-full divide-y divide-gray-200 mb-4 table-fixed border border-slate-200 rounded-xl">
                <thead className="bg-gray-50">
                <tr>
                    {headers.map((header, index) => (
                    <th
                        key={header}
                        className={`p-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider 
                            ${
                            index === 1 || index === 2 ? 'w-1/6' : index === 0 ? 'w-[5%]' : 'w-1/12'
                            }
                        `}
                    >
                        {header}
                    </th>
                    ))}
                </tr>
                </thead>
                <tbody className="bg-white divide-y divide-gray-200">
                {currentItems.map((data) => (
                    <tr key={data.STT}>
                    <td className="p-3 whitespace-nowrap">{data.STT}</td>
                    <td className="p-3 whitespace-normal">{data.vaccine}</td>
                    <td className="p-3 max-w-1/2 break-words">{data.Phong_benh}</td>
                    <td className="p-3 whitespace-nowrap text-center">{data.manufacturer}</td>
                    <td className="p-3 whitespace-nowrap">{data.retail_price}</td>
                    <td className="p-3 whitespace-nowrap">{data.dis_price}</td>
                    <td className="p-3 whitespace-nowrap">{data.status}</td>
                    </tr>
                ))}
                </tbody>
            </table>
            </Skeleton>
            <Pagination 
            align="center"
            current={currentPage}
            defaultCurrent={1}
            total={datas.length} // Use users.length for total items
            pageSize={itemsPerPage}
            onChange={handlePageChange} />
        </div>
    )
}
export default VaccineService;
import React, {useEffect, useState} from 'react';
import axios from 'axios';
import { Input, Pagination, Skeleton } from 'antd';
const { Search } = Input;
const MedicalService = () => {
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
        axios.get('https://raw.githubusercontent.com/dgeniust/hcmute-care/refs/heads/main/frontend/src/components/Personal/Data_Personal/service_health.json')
            .then(response => {
            setDatas(response.data.Dich_vu_y_te); // Đảm bảo API trả về một mảng
            })
            .catch(error => {
            console.log('error: '+ error);
            });
    }, []);
    const [filteredService, setFilteredService] = useState(datas);
    const headers = [
        'STT',
        'Mã hiệu',
        'Tên dịch vụ',
        'Đơn vị tính',
        'Mức thu',
    ];
    // Paginations
    const [currentPage, setCurrentPage] = useState(1);
    const [itemsPerPage, setItemsPerPage] = useState(30);

    const indexOfLastItem = currentPage * itemsPerPage;
    const indexOfFirstItem = indexOfLastItem - itemsPerPage;
    const currentItems = filteredService.slice(indexOfFirstItem, indexOfLastItem);

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
            const searchMatch = !searchService || (data.Ten_dich_vu && data.Ten_dich_vu.toLowerCase().includes(searchService.toLowerCase()));
            return searchMatch;
        })
        setFilteredService(filter);
        setCurrentPage(1);
    }
    useEffect(() => {
        showSkeleton();
        setFilteredService(datas);
        filtered();
    }, [searchService, datas]);
    // //Save Data User 
    // const [dataUser, setDataUser] = useState(null);
    // //Drawer
    // const [open, setOpen] = useState(false);
    // const showDrawerUser = (data) => {
    //     setOpen(true);
    //     setDataUser(data);
    // };
    // const onClose = () => {
    //     setOpen(false);
    //     setDataUser(null);
    // };
    return (
        <div className='w-full h-full p-8 text-black flex flex-col items-center justify-center text-start space-y-8'>
            <h1 className='font-bold text-xl'>Bảng giá dịch vụ</h1>
            <Search
            placeholder="Tìm kiếm dịch vụ "
            style={{
                width: '100%',
            }}
            value={searchService}
            onChange={(e) => handleSearch(e.target.value)}
            size="large"
            />
            <Skeleton loading={loadingSke}>
            <table className="w-full divide-y divide-gray-200 mb-4 table-fixed border border-slate-200 rounded-xl">
                <thead className="bg-gray-50">
                <tr>
                    {headers.map((header, index) => (
                    <th
                        key={header}
                        className={`p-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider ${
                            index === 2 ? 'max-w-1/2' : index === 0 || index === 1 ? 'w-1/15' : 'w-1/10'
                        }`}
                    >
                        {header}
                    </th>
                    ))}
                </tr>
                </thead>
                <tbody className="bg-white divide-y divide-gray-200">
                {currentItems.map((data) => (
                    <tr key={data.stt}>
                    <td className="p-3 whitespace-nowrap">{data.Stt}</td>
                    <td className="p-3 whitespace-nowrap">{data.Ma_hieu}</td>
                    <td className="p-3 max-w-1/2 break-words">{data.Ten_dich_vu}</td>
                    <td className="p-3 whitespace-nowrap">{data.Don_vi_tinh}</td>
                    <td className="p-3 whitespace-nowrap">{data.Muc_thu}</td>
                    {/* <td className="p-3 whitespace-nowrap cursor-pointer" onClick={() =>{
                        showDrawerUser(data)
                        }}><MoreOutlined /></td> */}
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
export default MedicalService;
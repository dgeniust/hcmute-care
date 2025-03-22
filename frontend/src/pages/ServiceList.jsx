import React, {useEffect, useState} from 'react';
import axios from 'axios';
import { Input } from 'antd';
const { Search } = Input;
const ServiceList = () => {
    const [data, setData] = useState([]);
    const [searchService, setSearchService] = useState('');

    useEffect(() => {
        axios.get('https://raw.githubusercontent.com/dgeniust/hcmute-care/refs/heads/main/frontend/src/components/Personal/Data_Personal/service_health.json')
            .then(response => {
            setData(response.data.Dich_vu_y_te); // Đảm bảo API trả về một mảng
            })
            .catch(error => {
            console.log('error: '+ error);
            });
    }, []);
    return (
        <div className='w-full h-full p-8 text-black flex flex-col items-center justify-center text-start space-y-8'>
            <h1 className='font-bold text-xl'>Bảng giá dịch vụ</h1>
            <Search
            placeholder="Tìm kiếm dịch vụ 🔍"
            style={{
                width: '100%',
            }}
            value={searchService}
            onChange={(e) => setSearchService(e.target.value)}
            size="large"
            />
            <div className="grid grid-rows-[auto_1fr] border border-gray-300 w-full">
                <div className="grid grid-cols-[50px_100px_3fr_100px_150px] bg-blue-400 font-semibold text-center text-white">
                    <div className="p-2 border border-gray-200">STT</div>
                    <div className="p-2 border border-gray-200">Mã Hiệu</div>
                    <div className="p-2 border border-gray-200">Tên dịch vụ</div>
                    <div className="p-2 border border-gray-200">Đơn vị tính</div>
                    <div className="p-2 border border-gray-200">Mức thu</div>
                </div>
                <div className="grid grid-cols-[50px_100px_3fr_100px_150px]">
                    {data
                    .filter((item) =>
                        item.Ten_dich_vu && item.Ten_dich_vu.toLowerCase().includes(searchService.toLowerCase())
                    )
                    .map((item, index) => (
                    <div key={index} className="contents">
                        <div className="p-2 border border-gray-200 text-center">{item.Stt}</div>
                        <div className="p-2 border border-gray-200 text-center">{item.Ma_hieu}</div>
                        <div className="p-2 border border-gray-200">{item.Ten_dich_vu}</div>
                        <div className="p-2 border border-gray-200 text-center">{item.Don_vi_tinh}</div>
                        <div className="p-2 border border-gray-200 text-center">{Number(item.Muc_thu).toLocaleString('vi-VN')}</div>
                    </div>
                    ))}
                </div>
            </div>
        </div>
    )
}
export default ServiceList;
import React, {useEffect, useState} from 'react';
import axios from 'axios';
import { Input, Pagination, Skeleton, Select, Button, Modal, Collapse, theme, Drawer} from 'antd';
import {ReloadOutlined, CloudDownloadOutlined, PlusOutlined, EditOutlined, DeleteTwoTone, CaretRightOutlined, BarsOutlined} from '@ant-design/icons';
const { Search } = Input;
import DetailsVaccine from './DetailsVaccine';
const ModalAddVacccine= () => {
    //Modal
    const [vaccineName, setVaccineName] = useState('');
    const [vaccinePrevent, setVaccinePrevent] = useState('');
    const [vaccineDose, setVaccineDose] = useState('');
    const [vaccineCountry, setVaccineCountry] = useState('');
    const [vaccineList, setVaccineList] = useState([]);

    const handleChangeName = (name) => {
        setVaccineName(name.target.value);
        console.log(vaccineName);
    }
    const handleChangePrevent = (prevent) => {
        setVaccinePrevent(prevent.target.value);
        console.log(vaccinePrevent);
    }
    const handleChangeDose = (dose) => {
        setVaccineDose(dose.target.value);
        console.log(vaccineDose);
    }
    const handleChangeCountry = (country) => {
        setVaccineCountry(country.target.value);
        console.log(vaccineCountry);
    }

    const handleAddVaccine = () => {
        // Tạo một object chứa thông tin vaccine
        const newVaccine = {
            name: vaccineName,
            prevent: vaccinePrevent,
            dose: vaccineDose,
            country: vaccineCountry,
        };

        // Thêm object vào mảng vaccineList
        setVaccineList([...vaccineList, newVaccine]);

        // Reset các state variables
        setVaccineName('');
        setVaccinePrevent('');
        setVaccineDose('');
        setVaccineCountry('');
    };

    const { token } = theme.useToken();
    const panelStyle = {
        marginBottom: 2,
        background: token.colorFillAlter,
        borderRadius: token.borderRadiusLG,
        border: 'none',
    };
    const getItemsFromVaccineList = (panelStyle) => {
        return vaccineList.map((vaccine, index) => ({
            key: index,
            label: vaccine.name,
            children: (
                <div className='w-full h-full p-4 text-black grid grid-flow-row grid-cols-2 gap-2'>
                    <p>Tên vaccine: {vaccine.name}</p>
                    <p>Tên bệnh phòng ngừa: {vaccine.prevent}</p>
                    <p>Số mũi: {vaccine.dose}</p>
                    <p>Nước sản xuất: {vaccine.country}</p>
                </div>
            ),
            style: panelStyle,
        }));
    };
    return (
        <div className='w-full h-full p-4 text-black'>
            {/* <h1 className='font-bold text-lg text-center'>Thêm gói vaccine</h1> */}
            <form action="POST" className='w-full h-full flex flex-col'>
                <div className='space-y-2'>
                    <div className='flex flex-col space-y-2'>
                        <label htmlFor="" className='text-sm'>Tên gói</label>
                        <Input size="medium" placeholder="Tên gói"/>
                    </div>
                    <div className='flex flex-col space-y-2'>
                        <label htmlFor="" className='text-sm'>Giá gói</label>
                        <Input size="medium" placeholder="Giá gói"/>
                    </div>
                    <div className='flex flex-col space-y-2'>
                        <label htmlFor="" className='text-sm'>Vaccine</label>
                        <div className='w-full h-full px-2 py-4 flex flex-col bg-slate-100 space-y-2'>
                            <div>
                                <label htmlFor="" className='text-sm'>Tên vaccine</label>
                                <Input size="medium" placeholder="Tên vaccine" onChange={handleChangeName} value={vaccineName}/>
                            </div>
                            <div>
                                <label htmlFor="" className='text-sm'>Tên bệnh phòng ngừa</label>
                                <Input size="medium" placeholder="Tên bệnh phòng ngừa" onChange={handleChangePrevent} value={vaccinePrevent}/>
                            </div>
                            <div>
                                <label htmlFor="" className='text-sm'>Số mũi</label>
                                <Input size="medium" placeholder="Số mũi" onChange={handleChangeDose} value={vaccineDose}/>
                            </div>
                            <div>
                                <label htmlFor="" className='text-sm'>Nước sản xuất</label>
                                <Input size="medium" placeholder="Nước sản xuất" onChange={handleChangeCountry} value={vaccineCountry}/>
                            </div>
                            <Button type="primary"style={{marginTop: '5px'}} onClick={handleAddVaccine}>Thêm Vaccine</Button>
                        </div>
                        
                    </div>
                    {
                        vaccineList.length > 0 && (
                            <div>
                                <label htmlFor="" className='text-sm'>Vaccine đã thêm</label>
                                <Collapse
                                bordered={false}
                                expandIcon={({ isActive }) => <CaretRightOutlined rotate={isActive ? 90 : 0} />}
                                expandIconPosition='right'
                                style={{
                                    background: token.colorBgContainer,
                                }}
                                items={getItemsFromVaccineList(panelStyle)}
                                />
                            </div>
                        )
                    }
                </div>
                <Button style={{marginTop:'10px'}}>Thêm gói vaccine</Button>
            </form>
        </div>

    )
}

const VaccineDrawerContent = ({ DetailsVaccine }) => {
    return (
      <div>
        {DetailsVaccine.map((data, index) => (
          <div key={index} className="text-black">
            <h1 className="font-bold text-base">
              {data.STT}. {data.Ten_vacxin_phong_benh}
            </h1>
            <div className="flex flex-col justify-between w-full space-y-4">
              <p>Loại bệnh: {data.Loai_benh}</p>
              <p>
                Liều lượng và đối tượng sử dụng :{" "}
                {data.Doi_tuong_Lich_tiem_Lieu_Duong_dung}
              </p>
              <p>Phản ứng sau tiêm : {data.Phan_ung_sau_tiem_chung_thuong_gap}</p>
            </div>
          </div>
        ))}
      </div>
    );
};

const GroupVaccineService = () => {
    const [datas, setDatas] = useState([]);
    const [searchService, setSearchService] = useState('');
    const [loadingSke, setLoadingSke] = useState(false);
    const [openVaccinceDrawer, setOpenVaccinceDrawer] = useState(false);
    const showSkeleton = () => {
        setLoadingSke(true);
        setTimeout(() => {
            setLoadingSke(false);
        }, 500);
    };
    useEffect(() => {
        axios.get('https://raw.githubusercontent.com/dgeniust/hcmute-care/refs/heads/main/frontend/src/components/Personal/Data_Personal/group_vaccine_price.json')
            .then(response => {
            setDatas(response.data.bang_gia_goi_tiem_chung); // Đảm bảo API trả về một mảng
            })
            .catch(error => {
            console.log('error: '+ error);
            });
        console.log(datas);
    }, []);
    const [filteredVaccine, setFilteredVaccine] = useState(datas);
    const [filteredAge, setFilteredAge] = useState(undefined);
    const headers = [
        'STT',
        'Tên Vaccine',
        'Phòng ngừa',
        'Số mũi',
        'Nước sản xuất',
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
            const ageMatch = !filteredAge || (data.Ten_Goi && data.Ten_Goi.includes('trẻ'));
            let ageFilterResult;

            if (filteredAge === 'Trẻ em') {
                ageFilterResult = ageMatch;
            } else if (filteredAge === 'Người lớn') {
                ageFilterResult = !ageMatch;
            } else {
                ageFilterResult = true; // Nếu filteredAge không phải "Trẻ em" hoặc "Người lớn", không lọc theo tuổi
            }
            return searchMatch && ageFilterResult;
        })
        setFilteredVaccine(filter);
        setCurrentPage(1);
    }

    const handleAgeChange = (value, status) => {
        setFilteredAge(status?.label || undefined);
        filtered();
    }
    const handleResetSearch = () => {
        setSearchService('');
        setFilteredAge(undefined);
        setFilteredVaccine(datas);
        setCurrentPage(1);
    }
    useEffect(() => {
        showSkeleton();
        setFilteredVaccine(datas);
        filtered();
    }, [searchService, filteredAge, datas]);

    //Modal 
    const [isModalOpen, setIsModalOpen] = useState(false);
    const showModal = () => {
        setIsModalOpen(true);
    };
    // const handleOk = () => {
    //     setIsModalOpen(false);
    // };
    const handleCancel = () => {
        setIsModalOpen(false);
    };

    const showVaccineDrawer = () => {
        setOpenVaccinceDrawer(true);
    };
    const onClose = () => {
        setOpenVaccinceDrawer(false);
    };
    return (
        <div className='w-full h-full p-8 text-black flex flex-col items-center justify-center text-start space-y-4'>
            <h1 className='font-bold text-xl'>Bảng giá gói Vaccine</h1>
            <div className='flex flex-row justify-between items-center w-full'>
                <div className='flex flex-row gap-4 w-full'>
                    <Select
                    style={{ width: 170 }}
                    value={filteredAge || "Chọn độ tuổi"}
                    placeholder="Chọn độ tuổi"
                    onChange={handleAgeChange}
                    options={
                        [
                            {
                                value: 'baby',
                                label: 'Trẻ em',
                            },
                            {
                                value: 'grown',
                                label: 'Người lớn',
                            }
                        ]
                    }
                    />
                    <Button icon={<ReloadOutlined />} onClick={handleResetSearch}></Button>
                </div>
                <Search
                placeholder="Tìm kiếm gói vaccine"
                style={{
                    width: '50%',
                }}
                value={searchService}
                onChange={(e) => handleSearch(e.target.value)}
                size="large"
                />
            </div>
            <div className='flex flex-row justify-end w-full'>
                <div className='flex flex-row space-x-4'>
                    <Button icon={<PlusOutlined />} onClick={showModal}>Thêm</Button>
                    <Button icon={<EditOutlined />}>Sửa</Button>
                    <Button icon={<CloudDownloadOutlined />}>Export</Button>
                </div>
            </div>
            <Skeleton loading={loadingSke}>
            {
                currentItems.map((data, index) => {
                    return (
                        <div key={index} className='w-full flex flex-col space-y-4'>
                            <div>
                                <h1 className='font-bold text-base'>Tên gói: {data.Ten_Goi}</h1>
                            </div>
                            <div className='flex flex-row justify-between w-full'>
                                <p>Giá gói: {data.Gia_Goi}</p>
                                <div className='space-x-4'>
                                <BarsOutlined style={{cursor: 'pointer'}} onClick={showVaccineDrawer}/>
                                <DeleteTwoTone twoToneColor="#e74c3c" style={{cursor: 'pointer'}}/>
                                </div>
                            </div>
                            <div className='w-full'>
                                <table className="w-full divide-y divide-gray-200 mb-4 table-fixed border border-slate-200 rounded-xl">
                                    <thead className="bg-gray-50">
                                    <tr>
                                        {headers.map((header, index) => (
                                        <th
                                            key={header}
                                            className={`p-3 ${index === 3 ? 'text-center' : 'text-left'} text-xs font-medium text-gray-500 uppercase tracking-wider 
                                                ${
                                                index === 1 ? 'w-1/8' : index === 2 ? 'w-1/2' : index === 0 ? 'w-[5%]' : 'w-1/12'
                                                }
                                            `}
                                        >
                                            {header}
                                        </th>
                                        ))}
                                    </tr>
                                    </thead>
                                    <tbody className="bg-white divide-y divide-gray-200">
                                    {data.Vaccine.map((vacc, index) => (
                                        <tr key={index+1}>
                                        <td className="p-3 whitespace-nowrap">{index+1}</td>
                                        <td className="p-3 whitespace-normal">{vacc.Ten_Vac_Xin}</td>
                                        <td className="p-3 max-w-1/2 break-words">{vacc.Ten_Benh_Phong_Ngua}</td>
                                        <td className="p-3 whitespace-nowrap text-center">{vacc.So_Mui}</td>
                                        <td className="p-3 whitespace-nowrap">{vacc.Nuoc_San_Xuat}</td>
                                        </tr>
                                    ))}
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    )
                })
            }
            </Skeleton>
            <Pagination 
            align="center"
            current={currentPage}
            defaultCurrent={1}
            total={datas.length} // Use users.length for total items
            pageSize={itemsPerPage}
            onChange={handlePageChange} />

            <Modal title="Modal" open={isModalOpen} footer={null} onCancel={handleCancel}>
                <ModalAddVacccine />
            </Modal>
            <Drawer
                title="Drawer"
                placement="right"
                size="large"
                onClose={onClose}
                open={openVaccinceDrawer}
            >
                <VaccineDrawerContent DetailsVaccine={DetailsVaccine}/>
            </Drawer>
        </div>
    )
}
export default GroupVaccineService;
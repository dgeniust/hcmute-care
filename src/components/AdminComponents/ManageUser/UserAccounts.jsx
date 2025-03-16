import React, {useState} from "react";
import {Pagination, Drawer, Divider, Collapse, theme, Input} from 'antd';
import {MenuFoldOutlined, MoreOutlined, CaretRightOutlined } from '@ant-design/icons';
const { Search } = Input;
const UserAccounts = () => {
    const users = [
        {
          id: 1,
          fullName: "Nguyễn Văn A",
          dob: "1990-01-01",
          email: "nguyenvana@example.com",
          phone: "0901234567",
          gender: "Nam",
          nation: "Việt Nam",
          career: "Lập trình viên",
          country: "Việt Nam",
          district: "Quận 1",
          ward: "Phường Bến Nghé",
          specificAddress: "123 Đường A, Quận 1, TP.HCM",
          medicalRecords : [
            {
                barcode: 'W24-14012004',
                patientName: 'Nguyễn Thành Đạt',
                patientDOB: '14/01/2004',
                patientPhone: '0387731823',
                patientGender: 'Nam',
                patientAddress: '123 Đường A, Quận 1, TP.HCM',
            },
            {
                barcode: 'W24-22072004',
                patientName: 'Hàng Diễm Quỳnh',
                patientDOB: '22/07/2004',
                patientPhone: '0916048519',
                patientGender: 'Nữ',
                patientAddress: '123 Đường A, Quận 1, TP.HCM',
            }
          ]
        },
        {
          id: 2,
          fullName: "Trần Thị B",
          dob: "1995-05-15",
          email: "tranthib@example.com",
          phone: "0912345678",
          gender: "Nữ",
          nation: "Việt Nam",
          career: "Giáo viên",
          country: "Việt Nam",
          district: "Quận 3",
          ward: "Phường 14",
          specificAddress: "456 Đường B, Quận 3, TP.HCM"
        },
        // Thêm 8 người dùng khác tương tự...
        {
          id: 10,
          fullName: "Lê Văn J",
          dob: "2002-12-10",
          email: "levanj@example.com",
          phone: "0989012345",
          gender: "Nam",
          nation: "Việt Nam",
          career: "Sinh viên",
          country: "Việt Nam",
          district: "Quận 10",
          ward: "Phường 12",
          specificAddress: "789 Đường J, Quận 10, TP.HCM"
        },
        {
            id: 11,
            fullName: "Phạm Thị K",
            dob: "1998-03-22",
            email: "phamthik@example.com",
            phone: "0971234567",
            gender: "Nữ",
            nation: "Việt Nam",
            career: "Kế toán",
            country: "Việt Nam",
            district: "Quận 5",
            ward: "Phường 9",
            specificAddress: "10 Đường C, Quận 5, TP.HCM"
        },
        {
            id: 12,
            fullName: "Hoàng Văn L",
            dob: "1985-11-05",
            email: "hoangvanl@example.com",
            phone: "0962345678",
            gender: "Nam",
            nation: "Việt Nam",
            career: "Bác sĩ",
            country: "Việt Nam",
            district: "Quận 7",
            ward: "Phường Tân Phong",
            specificAddress: "25 Đường D, Quận 7, TP.HCM"
        },
        {
            id: 13,
            fullName: "Trương Thị M",
            dob: "2000-07-18",
            email: "truongthim@example.com",
            phone: "0943456789",
            gender: "Nữ",
            nation: "Việt Nam",
            career: "Thiết kế đồ họa",
            country: "Việt Nam",
            district: "Quận Bình Thạnh",
            ward: "Phường 25",
            specificAddress: "50 Đường E, Quận Bình Thạnh, TP.HCM"
        },
        {
            id: 14,
            fullName: "Đỗ Văn N",
            dob: "1992-04-30",
            email: "dovann@example.com",
            phone: "0934567890",
            gender: "Nam",
            nation: "Việt Nam",
            career: "Giám đốc kinh doanh",
            country: "Việt Nam",
            district: "Quận 2",
            ward: "Phường Thảo Điền",
            specificAddress: "75 Đường F, Quận 2, TP.HCM"
        },
        {
            id: 15,
            fullName: "Lý Thị O",
            dob: "1988-09-25",
            email: "lythio@example.com",
            phone: "0925678901",
            gender: "Nữ",
            nation: "Việt Nam",
            career: "Nhà báo",
            country: "Việt Nam",
            district: "Quận 9",
            ward: "Phường Phước Long A",
            specificAddress: "100 Đường G, Quận 9, TP.HCM"
        },
        {
            id: 16,
            fullName: "Võ Văn P",
            dob: "2003-01-12",
            email: "vovanp@example.com",
            phone: "0916789012",
            gender: "Nam",
            nation: "Việt Nam",
            career: "Sinh viên",
            country: "Việt Nam",
            district: "Quận Thủ Đức",
            ward: "Phường Linh Chiểu",
            specificAddress: "125 Đường H, Quận Thủ Đức, TP.HCM"
        },
        {
            id: 17,
            fullName: "Cao Thị Q",
            dob: "1997-06-08",
            email: "caothiq@example.com",
            phone: "0987890123",
            gender: "Nữ",
            nation: "Việt Nam",
            career: "Dược sĩ",
            country: "Việt Nam",
            district: "Quận 6",
            ward: "Phường 10",
            specificAddress: "150 Đường I, Quận 6, TP.HCM"
        },
        {
            id: 18,
            fullName: "Bùi Văn R",
            dob: "1982-12-30",
            email: "buivanr@example.com",
            phone: "0978901234",
            gender: "Nam",
            nation: "Việt Nam",
            career: "Kỹ sư xây dựng",
            country: "Việt Nam",
            district: "Quận 8",
            ward: "Phường 15",
            specificAddress: "175 Đường K, Quận 8, TP.HCM"
        },
        {
            id: 19,
            fullName: "Ngô Thị S",
            dob: "1995-08-20",
            email: "ngothis@example.com",
            phone: "0969012345",
            gender: "Nữ",
            nation: "Việt Nam",
            career: "Giáo viên",
            country: "Việt Nam",
            district: "Quận 4",
            ward: "Phường 18",
            specificAddress: "200 Đường L, Quận 4, TP.HCM"
        },
        {
            id: 20,
            fullName: "Đặng Văn T",
            dob: "2001-02-14",
            email: "dangvant@example.com",
            phone: "0950123456",
            gender: "Nam",
            nation: "Việt Nam",
            career: "Lập trình viên",
            country: "Việt Nam",
            district: "Quận 11",
            ward: "Phường 16",
            specificAddress: "225 Đường M, Quận 11, TP.HCM"
        },
        {
            id: 21,
            fullName: "Hà Thị U",
            dob: "1987-07-03",
            email: "hathiu@example.com",
            phone: "0941234567",
            gender: "Nữ",
            nation: "Việt Nam",
            career: "Luật sư",
            country: "Việt Nam",
            district: "Quận 3",
            ward: "Phường Võ Thị Sáu",
            specificAddress: "250 Đường N, Quận 3, TP.HCM"
        },
        {
            id: 22,
            fullName: "Lâm Văn V",
            dob: "1999-10-28",
            email: "lamvanv@example.com",
            phone: "0932345678",
            gender: "Nam",
            nation: "Việt Nam",
            career: "Nhân viên marketing",
            country: "Việt Nam",
            district: "Quận 12",
            ward: "Phường Tân Chánh Hiệp",
            specificAddress: "275 Đường O, Quận 12, TP.HCM"
        },
        {
            id: 23,
            fullName: "Phan Thị X",
            dob: "1984-05-22",
            email: "phanthix@example.com",
            phone: "0923456789",
            gender: "Nữ",
            nation: "Việt Nam",
            career: "Chuyên viên nhân sự",
            country: "Việt Nam",
            district: "Quận Tân Bình",
            ward: "Phường 1",
            specificAddress: "300 Đường P, Quận Tân Bình, TP.HCM"
        },
        {
            id: 24,
            fullName: "Trần Văn Y",
            dob: "2004-03-15",
            email: "tranvany@example.com",
            phone: "0914567890",
            gender: "Nam",
            nation: "Việt Nam",
            career: "Sinh viên",
            country: "Việt Nam",
            district: "Quận Gò Vấp",
            ward: "Phường 3",
            specificAddress: "325 Đường Q, Quận Gò Vấp, TP.HCM"
    },
    {
        id: 25,
        fullName: "Huỳnh Thị Z",
        dob: "1980-11-09",
        email: "huynhthiz@example.com",
        phone: "0985678901",
        gender: "Nữ",
        nation: "Việt Nam",
        career: "Quản lý khách sạn",
        country: "Việt Nam",
        district: "Quận Phú Nhuận",
        ward: "Phường 5",
        specificAddress: "350 Đường R, Quận Phú Nhuận, TP.HCM"
    }
    ];
    
    const headers = [
        'STT',
        'Tên khách hàng',
        'Email',
        'Số điện thoại',
        'Giới tính',
        'Địa chỉ',
        <MenuFoldOutlined />
    ];
    
    //Search 
    const [searchService, setSearchService] = useState('');
    const [filteredUsers, setFilteredUsers] = useState(users);
    // Hàm xử lý tìm kiếm
    const handleSearch = (value) => {
        setSearchService(value);
        const filtered = users.filter((user) =>
            user.fullName && user.fullName.toLowerCase().includes(value.toLowerCase())
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

    //Save Data User 
    const [dataUser, setDataUser] = useState(null);
    //Drawer
    const [open, setOpen] = useState(false);
    const showDrawerUser = (data) => {
        setOpen(true);
        setDataUser(data);
    };
    const onClose = () => {
        setOpen(false);
        setDataUser(null);
    };

    //Collapse
    const { token } = theme.useToken();
    const panelStyle = {
        marginBottom: 12,
        background: token.colorFillAlter,
        borderRadius: token.borderRadiusLG,
        border: 'none',
      };
    const getItems = (medicalRecords, panelStyle) => {
        return medicalRecords.map((record, index) => ({
            key: index.toString(),
            label: `Hồ sơ ${index + 1} - ${record.patientName}`,
            children: (
                <div className="grid grid-flow-row grid-cols-2 gap-4">
                    <div className="grid grid-flow-row grid-cols-[100px_200px]">
                        <p>Mã vạch:</p>
                        <p>{record.barcode}</p>
                    </div>
                    <div className="grid grid-flow-row grid-cols-[100px_200px]">
                        <p>Tên bệnh nhân:</p>
                        <p>{record.patientName}</p>
                    </div>
                    <div className="grid grid-flow-row grid-cols-[100px_200px]">
                        <p>Ngày sinh:</p>
                        <p>{record.patientDOB}</p>
                    </div>
                    <div className="grid grid-flow-row grid-cols-[100px_200px]">
                        <p>Số điện thoại:</p>
                        <p>{record.patientPhone}</p>
                    </div>
                    <div className="grid grid-flow-row grid-cols-[100px_200px]">
                        <p>Giới tính:</p>
                        <p>{record.patientGender}</p>
                    </div>
                    <div className="grid grid-flow-row grid-cols-[100px_200px]">
                        <p>Địa chỉ:</p>
                        <p>{record.patientAddress}</p>
                    </div>
                </div>
            ),
            style: panelStyle,
        }));
    };
    return (
        <>
            <Search
            placeholder="Tìm kiếm khách hàng 🔍"
            style={{
                width: '100%',
            }}
            value={searchService}
            onChange={(e) => handleSearch(e.target.value)}
            size="large"
            />
            <table className="w-full divide-y divide-gray-200 mb-4">
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
                {currentItems.map((user) => (
                    <tr key={user.id}>
                    <td className="p-3 whitespace-nowrap">{user.id}</td>
                    <td className="p-3 whitespace-nowrap">{user.fullName}</td>
                    <td className="p-3 whitespace-nowrap">{user.email}</td>
                    <td className="p-3 whitespace-nowrap">{user.phone}</td>
                    <td className="p-3 whitespace-nowrap">{user.gender}</td>
                    <td className="p-3 whitespace-nowrap">{user.specificAddress}</td>
                    <td className="p-3 whitespace-nowrap cursor-pointer" onClick={() =>{
                        showDrawerUser(user)
                        }}><MoreOutlined /></td>
                    </tr>
                ))}
                </tbody>
            </table>
            <Pagination 
            align="center"
            current={currentPage}
            defaultCurrent={1}
            total={users.length} // Use users.length for total items
            pageSize={itemsPerPage}
            onChange={handlePageChange} />

                <Drawer
                    title={'Thông tin cá nhân'}
                    placement="right"
                    size='large'
                    onClose={onClose}
                    open={open}
                >
                    <div className="w-full h-full ">
                        <div className='w-full border border-black h-[150px] personal-bg relative'>
                            <Divider orientation="left" plain style={{ position: 'absolute', bottom: 0, width: '100%' }}>
                                <div className='w-[150px] h-[150px] rounded-full bg-white absolute border border-black top-1/2 left-1/6 transform -translate-x-1/2 -translate-y-1/3'>
                                    <img src={`${dataUser && dataUser.gender === 'Nam' ? 'https://api.dicebear.com/7.x/miniavs/svg?seed=8' : 'https://api.dicebear.com/9.x/miniavs/svg?seed=Christian'}`} alt="" srcset="" 
                                    className="object-center w-full h-full rounded-full" />
                                    <div className='text-black font-bold text-xl mt-4 text-center w-full'>
                                        <h1>{dataUser ? dataUser.fullName : ""}</h1>
                                    </div>
                                </div>
                            </Divider>
                        </div>
                        <div className="w-full h-fit mt-[140px] px-8 py-4 rounded-lg shadow-lg">
                            {dataUser && ( // Conditional rendering
                                <div className="flex flex-col space-y-4">
                                    <div className="grid grid-flow-row grid-cols-2">
                                        <div className="grid grid-flow-row grid-cols-[100px_200px]">
                                            <p>Họ và tên: </p>
                                            <p>{dataUser.fullName}</p>
                                        </div>

                                        <div className="grid grid-flow-row grid-cols-[100px_200px]">
                                            <p>Giới tính : </p>
                                            <p>{dataUser.gender}</p>
                                        </div>
                                    </div>
                                    <div className="grid grid-flow-row grid-cols-[100px_300px]">
                                        <p>Địa chỉ: </p>
                                        <p>{dataUser.specificAddress}</p>
                                    </div>
                                    <div className="grid grid-flow-row grid-cols-[100px_200px]">
                                        <p>Email: </p>
                                        <p>{dataUser.email}</p>
                                    </div>
                                    <div className="grid grid-flow-row grid-cols-2">
                                        <div className="grid grid-flow-row grid-cols-[100px_200px]">
                                            <p>Số điện thoại: </p>
                                            <p>{dataUser.phone}</p>
                                        </div>
                                        <div className="grid grid-flow-row grid-cols-[100px_200px]">
                                            <p>Quốc gia: </p>
                                            <p>{dataUser.country}</p>
                                        </div>
                                    </div>
                                    <div className="grid grid-flow-row grid-cols-2">
                                        <div className="grid grid-flow-row grid-cols-[100px_200px]">
                                            <p>Quốc tịch: </p>
                                            <p>{dataUser.nation}</p>
                                        </div>
                                        <div className="grid grid-flow-row grid-cols-[100px_200px]">
                                            <p>Nghề nghiệp: </p>
                                            <p>{dataUser.career}</p>
                                        </div>
                                    </div>
                                    
                                </div>
                            )}
                        </div>
                        {
                            dataUser && Array.isArray(dataUser.medicalRecords) && dataUser.medicalRecords.length > 0 && (
                                <div className="w-full h-fit mt-4 p-4 rounded-lg shadow-lg">
                                    <h1 className="font-bold text-base p-4">Hồ sơ bệnh án</h1>
                                    <Collapse
                                    bordered={false}
                                    expandIcon={({ isActive }) => <CaretRightOutlined rotate={isActive ? 90 : 0} />}
                                    style={{
                                        background: token.colorBgContainer,
                                    }}
                                    items={dataUser && dataUser.medicalRecords ? getItems(dataUser.medicalRecords, panelStyle) : []}
                                    />
                                </div>
                            )
                        }
                    </div>
                </Drawer>
            </>
    )
}

export default UserAccounts;
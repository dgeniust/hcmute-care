import React, {useState, useEffect} from 'react';
import {Pagination, Drawer, Divider, Collapse,theme, Button, Input, Tag, Select} from 'antd';
import Icon, {MenuFoldOutlined, MoreOutlined, CaretRightOutlined, ReloadOutlined } from '@ant-design/icons';
import MedalProfessor, {Medal2, Medal3, Medal4, Medal5, College} from './SVGEmployee'
const { Search } = Input;
const EmployeeAccounts = () => {
    const users = [
        {
            id: 1,
            fullName: "Nguyễn Văn A",
            dob: "1980-01-01",
            email: "nguyenvana@example.com",
            phone: "0901234567",
            gender: "Nam",
            nation: "Việt Nam",
            education: "Giáo sư",
            career: "Bác sĩ",
            country: "Việt Nam",
            district: "Quận 1",
            ward: "Phường Bến Nghé",
            specificAddress: "123 Đường A, Quận 1, TP.HCM",
            status: 'working'
        },
        {
            id: 2,
            fullName: "Nguyễn Văn B",
            dob: "1980-01-01",
            email: "nguyenvana@example.com",
            phone: "0901234567",
            gender: "Nam",
            nation: "Việt Nam",
            education: "Phó giáo sư",
            career: "Bác sĩ",
            country: "Việt Nam",
            district: "Quận 1",
            ward: "Phường Bến Nghé",
            specificAddress: "123 Đường A, Quận 1, TP.HCM",
            status: 'working'
        },
        {
            id: 3,
            fullName: "Nguyễn Văn C",
            dob: "1980-01-01",
            email: "nguyenvana@example.com",
            phone: "0901234567",
            gender: "Nam",
            nation: "Việt Nam",
            education: "Tiến sĩ",
            career: "Bác sĩ",
            country: "Việt Nam",
            district: "Quận 1",
            ward: "Phường Bến Nghé",
            specificAddress: "123 Đường A, Quận 1, TP.HCM",
            status: 'working'
        },
        {
            id: 4,
            fullName: "Nguyễn Văn D",
            dob: "1980-01-01",
            email: "nguyenvana@example.com",
            phone: "0901234567",
            gender: "Nữ",
            nation: "Việt Nam",
            education: "Thạc sĩ",
            career: "Y tá",
            country: "Việt Nam",
            district: "Quận 1",
            ward: "Phường Bến Nghé",
            specificAddress: "123 Đường A, Quận 1, TP.HCM",
            status: 'working'
        },
        {
            id: 5,
            fullName: "Nguyễn Văn E",
            dob: "1980-01-01",
            email: "nguyenvana@example.com",
            phone: "0901234567",
            gender: "Nam",
            nation: "Việt Nam",
            education: "Đại học",
            career: "Bảo vệ",
            country: "Việt Nam",
            district: "Quận 1",
            ward: "Phường Bến Nghé",
            specificAddress: "123 Đường A, Quận 1, TP.HCM",
            status: 'working'
        },
        {
            id: 6,
            fullName: "Nguyễn Văn F",
            dob: "1980-01-01",
            email: "nguyenvana@example.com",
            phone: "0901234567",
            gender: "Nam",
            nation: "Việt Nam",
            education: "Cử nhân",
            career: "Hậu cần",
            country: "Việt Nam",
            district: "Quận 1",
            ward: "Phường Bến Nghé",
            specificAddress: "123 Đường A, Quận 1, TP.HCM",
            status: 'absent'
        },
    ];
        
        const headers = [
            'Tên',
            'Học vị',
            'Vai trò',
            'Số điện thoại',
            'Email',
            'Status',
            '',
        ];


        //Search
    const [searchService, setSearchService] = useState('');
    const [filteredUsers, setFilteredUsers] = useState(users);
    const [filteredEdu, setFilteredEdu] = useState(undefined);
    const [filteredJob, setFilteredJob] = useState(undefined);
    const [filteredStatus, setFilteredStatus] = useState(undefined);


    // // Hàm xử lý tìm kiếm = tên
    // const handleSearch = (value) => {
    //     setSearchService(value);
    //     const filtered = users.filter((user) =>
    //         user.fullName && user.fullName.toLowerCase().includes(value.toLowerCase())
    //     );
    //     setFilteredUsers(filtered);
    //     setCurrentPage(1); // Reset về trang 1 khi tìm kiếm
    // };

    // // Handle Tìm kiếm = vai trò
    // const handleJobChange = (value, job) => {
    //     // Fixed the filter function - was missing a return statement
    //     setFilteredJob(job?.label || undefined);
    //     const filtered = users.filter((user) => {
    //         return user.career && user.career.includes(job.label);
    //     });
        
    //     console.log("Filtered users:", filtered);
    //     setFilteredUsers(filtered);
    //     setCurrentPage(1); // Reset to page 1 when searching
    // };
    // // Handle Tìm kiếm = học vị
    // const handleEduChange = (value, edu) => {
    //     console.log(edu.label)
    //     setFilteredEdu(edu?.label || undefined);
    //     // Fixed the filter function - was missing a return statement
    //     const filtered = users.filter((user) => {
    //         return user.education && user.education.includes(edu.label);
    //     });
        
    //     console.log("Filtered users:", filtered);
    //     setFilteredUsers(filtered);
    //     setCurrentPage(1); // Reset to page 1 when searching
    // };
    // const handleStatusChange = (value) => {
    //     let stt = '';
    //     if(value === 'working'){
    //         stt = 'working';
    //     }
    //     else if(value === 'absent'){
    //         stt = 'absent';
    //     }
    //     setFilteredStatus(stt);
    //     const filtered = users.filter((user) => {
    //         return user.status && user.status.includes(stt);
    //     });
        
    //     console.log("Filtered users:", filtered);
    //     setFilteredUsers(filtered);
    //     setCurrentPage(1); // Reset to page 1 when searching
    // }
    // const handleResetSearch = () => {
    //     setFilteredEdu(undefined);
    //     setFilteredJob(undefined);
    //     setFilteredStatus(undefined);
    //     setSearchService('');
    //     setFilteredUsers(users);
    //     setCurrentPage(1);
    // }
    const handleSearch = (value) => {
        setSearchService(value);
        filterUsers();
    };

    const handleJobChange = (value, job) => {
        setFilteredJob(job?.label || undefined);
        filterUsers();
    };

    const handleEduChange = (value, edu) => {
        setFilteredEdu(edu?.label || undefined);
        filterUsers();
    };

    const handleStatusChange = (value) => {
        let stt = '';
        if(value === 'working'){
            stt = 'working';
        }
        else if(value === 'absent'){
            stt = 'absent';
        }
        setFilteredStatus(stt);
        filterUsers();
    };

    const handleResetSearch = () => {
        setFilteredEdu(undefined);
        setFilteredJob(undefined);
        setFilteredStatus(undefined);
        setSearchService('');
        filterUsers();
    };
    useEffect(() => {
        filterUsers();
    }, [searchService, filteredEdu, filteredJob, filteredStatus]);
    const filterUsers = () => {
        let filtered = users.filter((user) => {
            const searchMatch = !searchService || (user.fullName && user.fullName.toLowerCase().includes(searchService.toLowerCase()));
            const eduMatch = !filteredEdu || (user.education && user.education.includes(filteredEdu));
            const jobMatch = !filteredJob || (user.career && user.career.includes(filteredJob));
            const statusMatch = !filteredStatus || (user.status && user.status.includes(filteredStatus));

            return searchMatch && eduMatch && jobMatch && statusMatch;
        });

        setFilteredUsers(filtered);
        setCurrentPage(1);
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
        <div className='flex flex-col space-y-4'>
            <div className='flex flex-row justify-between items-center w-full'>
                <div className='flex flex-row gap-4 w-full'>
                    <Select
                    style={{
                        width: 150,
                    }}
                    value={filteredJob || 'Chọn vai trò'}
                    placeholder="Chọn vai trò"
                    onChange={handleJobChange}
                    options={[
                        {
                        value: 'doctor',
                        label: 'Bác sĩ',
                        },
                        {
                        value: 'nurse',
                        label: 'Y tá',
                        },
                        {
                        value: 'security',
                        label: 'Bảo vệ',
                        },
                        {
                        value: 'support',
                        label: 'Hậu cần',
                        },
                    ]}
                    />
                    <Select
                    style={{
                        width: 150,
                    }}
                    placeholder="Chọn học vị"
                    value={filteredEdu || 'Chọn học vị'}
                    onChange={handleEduChange}
                    options={[
                        {
                        value: 'professor',
                        label: 'Giáo sư',
                        },
                        {
                        value: 'associate_professor',
                        label: 'Phó giáo sư',
                        },
                        {
                        value: 'doctorate',
                        label: 'Tiến sĩ',
                        },
                        {
                        value: 'master',
                        label: 'Thạc sĩ',
                        },
                        {
                        value: 'bachelor',
                        label: 'Cử nhân',
                        },
                        {
                        value: 'undergraduate',
                        label: 'Đại học',
                        },
                    ]}
                    />
                    <Select
                    style={{
                        width: 150,
                    }}
                    placeholder="Chọn trạng thái"
                    value={filteredStatus || 'Chọn trạng thái'}
                    onChange={handleStatusChange}
                    options={[
                        {
                        value: 'working',
                        label: (
                            <Tag color="green">
                                <div className='w-full h-full flex flex-row space-x-1 items-center justify-center text-center'>
                                    <div className='w-[5px] h-[5px] rounded-full bg-green-400 flex items-center justify-center text-center'></div>
                                    <p>working</p>
                                </div>
                            </Tag>
                        ),
                        },
                        {
                        value: 'absent',
                        label: (
                            <Tag color="red">
                                <div className='w-full h-full flex flex-row space-x-1 items-center justify-center text-center'>
                                    <div className='w-[5px] h-[5px] rounded-full bg-red-400 flex items-center justify-center text-center'></div>
                                    <p>absent</p>
                                </div>
                            </Tag>
                        ),
                        },
                    ]}
                    />
                    <Button icon={<ReloadOutlined />} onClick={handleResetSearch}></Button>
                </div>
                <Search
                placeholder="Tìm kiếm nhân sự "
                style={{
                    width: '50%',
                }}
                value={searchService}
                onChange={(e) => handleSearch(e.target.value)}
                size="large"
                />
            </div>
            <table className="w-full divide-y divide-gray-200 mb-4 table-fixed border border-slate-200 rounded-xl">
                <thead className="bg-gray-50">
                <tr>
                    {headers.map((header, index) => (
                    <th
                        key={header}
                        className={`p-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider ${
                            index === 0 || index === 4 ? 'w-1/4' : index === 6 ? 'w-1/8' : 'w-1/6'
                        }`}
                    >
                        {header}
                    </th>
                    ))}
                </tr>
                </thead>
                <tbody className="bg-white divide-y divide-gray-200">
                {currentItems.map((user) => (
                    <tr key={user.id} className='hover:bg-slate-50 '>
                    {
                        user.gender === 'Nam' ? (
                            <td className="p-3 whitespace-nowrap">
                                <div className='flex flex-row items-center space-x-2'>
                                    <img
                                    src="https://api.dicebear.com/9.x/miniavs/svg?seed=Jameson"
                                    alt="avatar" className="object-center w-[40px] h-[40px] rounded-full border border-slate-200"/>
                                    <p>{user.fullName}</p>
                                </div>
                            </td>
                        ) : (
                            <td className="p-3 whitespace-nowrap">
                                <div className='flex flex-row items-center space-x-2'>
                                    <img
                                    src="https://api.dicebear.com/9.x/miniavs/svg?seed=Ryan"
                                    alt="avatar" className="object-center w-[40px] h-[40px] rounded-full border border-slate-200"/>
                                    <p>{user.fullName}</p>
                                </div>
                            </td>
                        )
                    }
                    <td className="p-3 whitespace-nowrap">
                    <p className="items-center flex flex-row space-x-2">
                        {
                        {
                            'Giáo sư': <MedalProfessor />,
                            'Phó giáo sư': <Medal2 />,
                            'Tiến sĩ': <Medal3 />,
                            'Thạc sĩ': <Medal4 />,
                            'Cử nhân': <Medal5 />,
                            'Đại học' : <College/>,
                        }[user.education] ?? <span>{user.education}</span>
                        }
                        <span>{user.education}</span>
                    </p>
                    </td>
                    {
                        user.career === 'Bác sĩ' ? (
                            <td className="p-3 whitespace-nowrap">
                                <div className='w-fit h-fit px-2 rounded-xl bg-[#273c75] text-white'>
                                {user.career}
                                </div>
                            </td>
                        ) : user.career === 'Y tá' ?(
                            <td className="p-3 whitespace-nowrap">
                                <div className='w-fit h-fit px-2 rounded-xl bg-pink-300 text-white'>
                                {user.career}
                                </div>
                            </td>
                        ) : user.career === 'Hậu cần' ?(
                            <td className="p-3 whitespace-nowrap">
                                <div className='w-fit h-fit px-2 rounded-xl bg-yellow-200 text-black'>
                                {user.career}
                                </div>
                            </td>
                        ) : user.career === 'Bảo vệ' ?(
                            <td className="p-3 whitespace-nowrap">
                                <div className='w-fit h-fit px-2 rounded-xl bg-black text-white'>
                                {user.career}
                                </div>
                            </td>
                        ) : (
                            <td className="p-3 whitespace-nowrap">
                                <div className='w-fit h-fit px-2 rounded-xl bg-white text-black'>
                                {user.career}
                                </div>
                            </td>
                        )

                    }
                    <td className="p-3 whitespace-nowrap">{user.phone}</td>
                    <td className="p-3 whitespace-nowrap">{user.email}</td>
                    {
                        user.status === 'working' ? (
                            <td className="p-3 whitespace-nowrap">
                                <Tag color="green">
                                    <div className='w-full h-full flex flex-row space-x-1 items-center justify-center text-center'>
                                        <div className='w-[5px] h-[5px] rounded-full bg-green-400 flex items-center justify-center text-center'></div>
                                        <p>{user.status}</p>
                                    </div>
                                </Tag>
                            </td>
                        ) : (
                            <td className="p-3 whitespace-nowrap">
                                <Tag color="red">
                                    <div className='w-full h-full flex flex-row space-x-1 items-center justify-center text-center'>
                                        <div className='w-[5px] h-[5px] rounded-full bg-red-400 flex items-center justify-center text-center'></div>
                                        <p>{user.status}</p>
                                    </div>
                                </Tag>
                            </td>
                        )
                    }
                    <td className="p-3 whitespace-nowrap cursor-pointer space-x-2" onClick={() =>{
                        showDrawerUser(user)
                        }}><Button style={{borderRadius: '8px'}}>Profile</Button><MoreOutlined /></td>
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
        </div>
    )
}

export default EmployeeAccounts;
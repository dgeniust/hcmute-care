import React, {useState} from 'react';
import { Avatar, Card, Badge, Typography, Space, Tag, Tooltip, Button, Table, List  } from 'antd';
import { 
  HeartFilled, 
  CalendarOutlined, MedicineBoxOutlined, FileTextOutlined, PlusOutlined, FileImageOutlined ,EyeOutlined ,DownloadOutlined ,DeleteOutlined ,LineChartOutlined ,FileSearchOutlined ,EditOutlined 
} from '@ant-design/icons';
import AddParaclinicalTest from './AddParaclinicalTest';
import ClinicalReportExporter from './ClinicalReportExporter';
const {Text } = Typography;

const ParaclinicalTest = () => {
    const labTests = [
        {
          key: '1',
          name: 'Công thức máu',
          type: 'Máu',
          requestDate: '22/03/2025',
          status: 'Hoàn thành',
          result: 'https://example.com/results/123',
          requestedBy: 'Bs. Nguyễn Văn A'
        },
        {
          key: '2',
          name: 'Sinh hóa máu',
          type: 'Máu',
          requestDate: '22/03/2025',
          status: 'Đang xử lý',
          result: null,
          requestedBy: 'Bs. Nguyễn Văn A'
        },
        {
          key: '3',
          name: 'Tổng phân tích nước tiểu',
          type: 'Nước tiểu',
          requestDate: '20/03/2025',
          status: 'Hoàn thành',
          result: 'https://example.com/results/124',
          requestedBy: 'Bs. Nguyễn Văn A'
        },
    ];
      
      // Dữ liệu mẫu cho chẩn đoán hình ảnh
    const imagingTests = [
        {
          id: '1',
          name: 'X-quang phổi',
          type: 'X-quang',
          requestDate: '22/03/2025',
          status: 'Hoàn thành',
          imageUrl: 'https://res.cloudinary.com/dujzjcmai/image/upload/v1743740625/imagingTest/lbtvrfbind6leh3tgz6g.jpg',
          requestedBy: 'Bs. Nguyễn Văn A'
        },
        {
          id: '2',
          name: 'Siêu âm ổ bụng',
          type: 'Siêu âm',
          requestDate: '22/03/2025',
          status: 'Đang xử lý',
          imageUrl: "https://res.cloudinary.com/dujzjcmai/image/upload/v1743740625/imagingTest/l5g8xeowfqx2j6rpexvt.jpg",
          requestedBy: 'Bs. Nguyễn Văn A'
        },
        {
          id: '3',
          name: 'CT Scan sọ não',
          type: 'CT Scan',
          requestDate: '21/03/2025',
          status: 'Hoàn thành',
          imageUrl: 'https://res.cloudinary.com/dujzjcmai/image/upload/v1743740625/imagingTest/n3wfcyho3ljvix2qzske.jpg',
          requestedBy: 'Bs. Trần Thị B'
        },
        {
          id: '4',
          name: 'MRI cột sống',
          type: 'MRI',
          requestDate: '20/03/2025',
          status: 'Đang xử lý',
          imageUrl: "https://res.cloudinary.com/dujzjcmai/image/upload/v1743740625/imagingTest/etofariz9jpwjfpbio1o.jpg",
          requestedBy: 'Bs. Trần Thị B'
        },
    ];
      
      // Dữ liệu mẫu cho thăm dò chức năng
    const functionalTests = [
        {
          id: '1',
          name: 'Điện tâm đồ',
          type: 'Điện tim',
          requestDate: '22/03/2025',
          status: 'Hoàn thành',
          requestedBy: 'Bs. Nguyễn Văn A'
        },
        {
          id: '2',
          name: 'Điện não đồ',
          type: 'Điện não',
          requestDate: '21/03/2025',
          status: 'Đang xử lý',
          requestedBy: 'Bs. Trần Thị B'
        },
        {
          id: '3',
          name: 'Nội soi dạ dày',
          type: 'Nội soi',
          requestDate: '20/03/2025',
          status: 'Hoàn thành',
          requestedBy: 'Bs. Nguyễn Văn A'
        },
    ];

    const [isModalVisible, setIsModalVisible] = useState(false);
      
    const showModal = () => {
        setIsModalVisible(true);
    };
      
    const handleCancel = () => {
        setIsModalVisible(false);
    };
      
    const handleSubmit = (values) => {
        console.log('Submitted values:', values);
        // Xử lý dữ liệu gửi đi
        // Thêm vào danh sách yêu cầu hoặc gọi API
        setIsModalVisible(false);
    };
    return (
        <>
            {/* Yêu cầu cận lâm sàng */}
            <Card 
            className="w-full rounded-xl overflow-hidden mt-6" 
            variant={false}
            title={
                <div className="flex items-center">
                <MedicineBoxOutlined className="mr-2 text-blue-500" />
                <span>Yêu cầu Cận lâm sàng</span>
                </div>
            }
            extra={
                <ClinicalReportExporter 
                labTests={labTests}
                imagingTests={imagingTests}
                functionalTests={functionalTests}
                />
            }
            >
            <div className="grid grid-cols-1 gap-6">
                {/* Xét nghiệm */}
                <Card 
                className="shadow-sm hover:shadow-md transition-all duration-300"
                title={
                    <div className="flex items-center">
                    <FileTextOutlined className="mr-2 text-green-600" />
                    <span>Xét nghiệm</span>
                    </div>
                }
                extra={
                    <Tooltip title="Thêm yêu cầu xét nghiệm mới">
                    <Button type="primary" icon={<PlusOutlined />}>Thêm</Button>
                    </Tooltip>
                }
                >
                <Table
                    dataSource={labTests}
                    columns={[
                    {
                        title: 'Tên xét nghiệm',
                        dataIndex: 'name',
                        key: 'name',
                    },
                    {
                        title: 'Loại',
                        dataIndex: 'type',
                        key: 'type',
                        render: (text) => <Tag color="green">{text}</Tag>,
                    },
                    {
                        title: 'Thời gian yêu cầu',
                        dataIndex: 'requestDate',
                        key: 'requestDate',
                    },
                    {
                        title: 'Trạng thái',
                        dataIndex: 'status',
                        key: 'status',
                        render: (status) => (
                        <Badge 
                            status={status === 'Hoàn thành' ? 'success' : status === 'Đang xử lý' ? 'processing' : 'default'} 
                            text={status} 
                        />
                        ),
                    },
                    {
                        title: 'Kết quả',
                        dataIndex: 'result',
                        key: 'result',
                        render: (result, record) => (
                        record.status === 'Hoàn thành' ? (
                            <Button type="link" icon={<FileSearchOutlined />}>Xem kết quả</Button>
                        ) : (
                            <Text type="secondary">Chưa có</Text>
                        )
                        ),
                    },
                    {
                        title: 'Thao tác',
                        key: 'action',
                        render: (_, record) => (
                        <Space size="small">
                            <Tooltip title="Cập nhật">
                            <Button type="text" icon={<EditOutlined />} />
                            </Tooltip>
                            <Tooltip title="Xóa">
                            <Button type="text" danger icon={<DeleteOutlined />} />
                            </Tooltip>
                        </Space>
                        ),
                    },
                    ]}
                    size="small"
                    pagination={false}
                />
                </Card>

                {/* Chẩn đoán hình ảnh */}
                <Card 
                className="shadow-sm hover:shadow-md transition-all duration-300"
                title={
                    <div className="flex items-center">
                    <FileImageOutlined className="mr-2 text-blue-600" />
                    <span>Chẩn đoán hình ảnh</span>
                    </div>
                }
                extra={
                    <Tooltip title="Thêm yêu cầu chẩn đoán hình ảnh mới">
                    <Button type="primary" icon={<PlusOutlined />}>Thêm</Button>
                    </Tooltip>
                }
                >
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                    {imagingTests.map((test) => (
                    <Card 
                        key={test.id}
                        size="small" 
                        className="hover:shadow-md transition-all duration-300"
                        cover={
                        <div className="h-40 w-full bg-gray-100 flex items-center justify-center">
                            {test.status === 'Hoàn thành' ? (
                            <img 
                                src={test.imageUrl || null} 
                                alt={test.name} 
                                className="object-cover h-full w-full"
                            />
                            ) : (
                            <div className="flex flex-col items-center justify-center text-gray-400">
                                <FileImageOutlined style={{ fontSize: '3rem' }} />
                                <Text type="secondary">Chưa có hình ảnh</Text>
                            </div>
                            )}
                        </div>
                        }
                        actions={[
                        <Tooltip title="Xem chi tiết">
                            <EyeOutlined key="view" />
                        </Tooltip>,
                        <Tooltip title="Tải xuống">
                            <DownloadOutlined key="download" />
                        </Tooltip>,
                        <Tooltip title="Xóa">
                            <DeleteOutlined key="delete" />
                        </Tooltip>,
                        ]}
                    >
                        <Card.Meta
                        title={<Text strong>{test.name}</Text>}
                        description={
                            <div className="flex flex-col">
                            <Text type="secondary">{test.requestDate}</Text>
                            <div className="mt-2 flex items-center">
                                <Badge 
                                status={test.status === 'Hoàn thành' ? 'success' : 'processing'} 
                                text={test.status} 
                                />
                            </div>
                            </div>
                        }
                        />
                    </Card>
                    ))}
                </div>
                </Card>

                {/* Thăm dò chức năng */}
                <Card 
                className="shadow-sm hover:shadow-md transition-all duration-300"
                title={
                    <div className="flex items-center">
                        <LineChartOutlined className="mr-2 text-purple-600" />
                        <span>Thăm dò chức năng</span>
                    </div>
                    }
                    extra={
                    <Tooltip title="Thêm yêu cầu thăm dò chức năng mới">
                        <Button type="primary" icon={<PlusOutlined />}>Thêm</Button>
                    </Tooltip>
                    }
                >
                    <List
                    dataSource={functionalTests}
                    renderItem={(item) => (
                        <List.Item
                        key={item.id}
                        actions={[
                            <Button key="view" type="link" icon={<FileSearchOutlined />}>
                            {item.status === 'Hoàn thành' ? 'Xem kết quả' : 'Chi tiết'}
                            </Button>,
                            <Button key="edit" type="text" icon={<EditOutlined />} />,
                            <Button key="delete" type="text" danger icon={<DeleteOutlined />} />,
                        ]}
                        >
                        <List.Item.Meta
                            avatar={
                            <Avatar 
                                icon={<HeartFilled />} 
                                style={{ 
                                backgroundColor: item.type === 'Điện tim' ? '#ff4d4f' : 
                                            item.type === 'Điện não' ? '#13c2c2' : 
                                            item.type === 'Nội soi' ? '#722ed1' : '#1890ff' 
                                }} 
                            />
                            }
                            title={<Text strong>{item.name}</Text>}
                            description={
                            <div className="flex flex-col md:flex-row md:items-center text-xs">
                                <Tag color="blue">{item.type}</Tag>
                                <Text type="secondary" className="md:ml-2">
                                <CalendarOutlined className="mr-1" />{item.requestDate}
                                </Text>
                                <Badge 
                                className="md:ml-2 mt-1 md:mt-0"
                                status={item.status === 'Hoàn thành' ? 'success' : 'processing'} 
                                text={item.status} 
                                />
                            </div>
                            }
                        />
                        <div>
                            <Text type="secondary">Bác sĩ chỉ định: {item.requestedBy}</Text>
                        </div>
                        </List.Item>
                    )}
                    />
                </Card>

                {/* Nút thêm yêu cầu mới */}
                <div className="flex justify-center mt-4 flex-row items-center">
                    <Button 
                    type="primary" 
                    size="large"
                    icon={<PlusOutlined />}
                    onClick={showModal}
                    className="bg-gradient-to-r from-blue-500 to-indigo-600 border-0 shadow"
                    >
                    Thêm yêu cầu cận lâm sàng mới
                    </Button>
                    <AddParaclinicalTest
                    visible={isModalVisible}
                    onCancel={handleCancel}
                    onSubmit={handleSubmit}
                    />
                </div>
                </div>
            </Card>
        </>
    )
}

export default ParaclinicalTest;
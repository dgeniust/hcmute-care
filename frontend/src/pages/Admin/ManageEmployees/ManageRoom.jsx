import React, { useState, useEffect } from "react";
import {
  Table,
  Button,
  Modal,
  Form,
  Input,
  InputNumber,
  message,
  Tag,
  Space,
  Typography,
  Tooltip,
  Card,
  Select,
  Breadcrumb,
  Divider,
  Statistic,
  Row,
  Col,
} from "antd";
import {
  PlusOutlined,
  EditOutlined,
  DeleteOutlined,
  ExclamationCircleOutlined,
  HomeOutlined,
  AppstoreOutlined,
  EnvironmentOutlined,
  BuildOutlined,
  BankOutlined,
} from "@ant-design/icons";

const { Title, Text } = Typography;
const { Option } = Select;

export default function ManageRoom() {
  const apiUrl = import.meta.env.VITE_API_BASE_URL;

  const [rooms, setRooms] = useState([]);
  const [loading, setLoading] = useState(false);
  const [currentPage, setCurrentPage] = useState(1);
  const [totalPages, setTotalPages] = useState(1);
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [isDeleteModalVisible, setIsDeleteModalVisible] = useState(false);
  const [idDelete, setIdDelete] = useState(null);
  const [editingRoom, setEditingRoom] = useState(null);
  const [messageApi, contextHolder] = message.useMessage();
  const [form] = Form.useForm();

  const fetchRooms = async (page = 1) => {
    try {
      setLoading(true);
      const response = await fetch(
        `${apiUrl}v1/room-details?page=${page}&size=10&sort=id&direction=asc`
      );
      if (!response.ok) throw new Error("Network response was not ok");
      const data = await response.json();
      console.log("Fetched rooms:", data.data.content); // Debug
      setRooms(data.data.content);
      setCurrentPage(data.data.currentPage);
      setTotalPages(data.data.totalPages);
    } catch (error) {
      messageApi.error("Failed to fetch rooms: " + error.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchRooms();
  }, []);

  const handleAdd = () => {
    setEditingRoom(null);
    form.resetFields();
    setIsModalVisible(true);
  };

  const handleEdit = (room) => {
    setEditingRoom(room);
    form.setFieldsValue(room);
    setIsModalVisible(true);
  };

  const handleOk = async () => {
    try {
      const values = await form.validateFields();
      const url = editingRoom
        ? `${apiUrl}v1/room-details/${editingRoom.id}`
        : `${apiUrl}v1/room-details`;
      const method = editingRoom ? "PUT" : "POST";
      setLoading(true);
      const response = await fetch(url, {
        method,
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(values),
      });
      if (!response.ok) throw new Error(`Failed to ${editingRoom ? "update" : "add"} room`);
      messageApi.success(`Room ${editingRoom ? "updated" : "added"} successfully`);
      setIsModalVisible(false);
      fetchRooms(currentPage);
    } catch (error) {
      messageApi.error("Error saving room: " + error.message);
    } finally {
      setLoading(false);
    }
  };

  const getBuildingColor = (building) => {
    const buildingColors = {
      "Building A": "green",
      "Building B": "blue",
      "Building C": "purple",
      "Building D": "orange",
    };
    return buildingColors[building] || "default";
  };
  const handleDelete = async () => {
    try {
      setLoading(true);
      const response = await fetch(`${apiUrl}v1/room-details/${idDelete}`, {
        method: "DELETE",
      });
      console.log("Delete response:", response); // Debug
      if (!response.ok) throw new Error("Failed to delete room");
      messageApi.success("Phòng xóa thành công");
      fetchRooms(currentPage);
    } catch (error) {
      messageApi.error("Error deleting room: " + error.message);
    } finally {
      setLoading(false);
      setIsModalVisible(false);
    }
  }

  const getBuildingIcon = (building) => {
    const buildingIcons = {
      "Building A": <BankOutlined />,
      "Building B": <BuildOutlined />,
      "Building C": <EnvironmentOutlined />,
      "Building D": <AppstoreOutlined />,
    };
    return buildingIcons[building] || <BuildOutlined />;
  };

  // Tính toán số lượng phòng trong từng tòa nhà
  const calculateBuildingStats = () => {
    const stats = {};
    rooms.forEach(room => {
      if (!stats[room.building]) {
        stats[room.building] = 0;
      }
      stats[room.building]++;
    });
    return stats;
  };

  const buildingStats = calculateBuildingStats();

  const columns = [
    {
      title: "ID",
      dataIndex: "id",
      key: "id",
      width: "8%",
      render: (id) => (
        <Text strong className="text-gray-700">
          #{id}
        </Text>
      ),
    },
    {
      title: "Room Name",
      dataIndex: "name",
      key: "name",
      width: "30%",
      render: (text) => (
        <div className="flex items-center">
          <AppstoreOutlined className="mr-2 text-blue-500" />
          <Text strong className="text-gray-800">
            {text}
          </Text>
        </div>
      ),
    },
    {
      title: "Building",
      dataIndex: "building",
      key: "building",
      width: "25%",
      render: (text) => (
        <Tag color={getBuildingColor(text)} className="px-3 py-1">
          <span className="flex items-center">
            {getBuildingIcon(text)}
            <span className="ml-1">{text}</span>
          </span>
        </Tag>
      ),
    },
    {
      title: "Floor",
      dataIndex: "floor",
      key: "floor",
      width: "15%",
      render: (floor) => (
        <div className="flex items-center">
          <Text className="text-gray-700">
            <span className="inline-block bg-gray-100 rounded-full px-3 py-1">
              Level {floor}
            </span>
          </Text>
        </div>
      ),
    },
    {
      title: "Actions",
      key: "actions",
      width: "15%",
      render: (_, record) => (
        <Space size="middle">
          <Tooltip title="Edit Room">
            <Button
              type="primary"
              ghost
              icon={<EditOutlined />}
              onClick={() => handleEdit(record)}
              size="middle"
              shape="circle"
              className="hover:shadow-md transition-all"
            />
          </Tooltip>
          <Tooltip title="Delete Room">
            <Button
              danger
              icon={<DeleteOutlined />}
              onClick={() => {
                setIdDelete(record.id);
                setIsDeleteModalVisible(true);
              }}
              size="middle"
              shape="circle"
              className="hover:shadow-md transition-all"
            />
          </Tooltip>
        </Space>
      ),
    },
  ];

  return (
    <div className="p-6 bg-gray-50 w-full min-h-screen">
      {contextHolder}
      <Card className="shadow-md mb-6">
        <div className="flex justify-between items-center mb-4">
          <div>
            <Title level={3} className="m-0 text-blue-700">
              <BuildOutlined className="mr-2" /> Room Management
            </Title>
            <Text type="secondary">Manage all rooms across different buildings</Text>
          </div>
          <Button 
            type="primary" 
            icon={<PlusOutlined />} 
            onClick={handleAdd}
            size="large"
            className="bg-blue-600 hover:bg-blue-700 shadow-md"
          >
            Add Room
          </Button>
        </div>
        
        <Divider className="my-4" />
        
        <Row gutter={16} className="mb-6">
          {Object.entries(buildingStats).map(([building, count]) => (
            <Col span={6} key={building}>
              <Card className="bg-gray-50 hover:shadow-md transition-all">
                <Statistic
                  title={<span className="flex items-center">{getBuildingIcon(building)} <span className="ml-2">{building}</span></span>}
                  value={count}
                  suffix="rooms"
                  valueStyle={{ color: building === "Building A" ? "#3f8600" : 
                              building === "Building B" ? "#1890ff" : 
                              building === "Building C" ? "#722ed1" : 
                              "#fa8c16" }}
                />
              </Card>
            </Col>
          ))}
        </Row>

        <Table
          columns={columns}
          dataSource={rooms}
          rowKey="id"
          loading={loading}
          className="border border-gray-200 rounded shadow-sm"
          pagination={{
            current: currentPage,
            total: totalPages * 10,
            onChange: (page) => fetchRooms(page),
            showSizeChanger: false,
            showTotal: (total) => `Total ${total} rooms`,
            className: "font-medium",
          }}
          rowClassName="hover:bg-blue-50 transition-all"
        />
      </Card>

      <Modal
        title={
          <div className="flex items-center">
            {editingRoom ? (
              <EditOutlined className="mr-2 text-blue-500" />
            ) : (
              <PlusOutlined className="mr-2 text-green-500" />
            )}
            <span>{editingRoom ? "Edit Room" : "Add New Room"}</span>
          </div>
        }
        open={isModalVisible}
        onOk={handleOk}
        onCancel={() => setIsModalVisible(false)}
        confirmLoading={loading}
        okText={editingRoom ? "Update" : "Create"}
        width={500}
        className="top-8"
        okButtonProps={{ className: editingRoom ? "bg-blue-500" : "bg-green-500" }}
      >
        <div className="pt-4">
          <Form form={form} layout="vertical">
            <Form.Item
              name="name"
              label="Room Name"
              rules={[{ required: true, message: "Please enter room name" }]}
            >
              <Input placeholder="Enter room name" prefix={<AppstoreOutlined className="text-gray-400" />} />
            </Form.Item>
            <Form.Item
              name="building"
              label="Building"
              rules={[{ required: true, message: "Please enter building" }]}
            >
              <Select placeholder="Select a building">
                <Option value="Building A">Tòa A</Option>
                <Option value="Building B">Tòa B</Option>
                <Option value="Building C">Tòa C</Option>
                <Option value="Building D">Tòa D</Option>
              </Select>
            </Form.Item>
            <Form.Item
              name="floor"
              label="Floor"
              rules={[{ required: true, message: "Please enter floor number" }]}
            >
              <InputNumber
                min={0}
                className="w-full"
                placeholder="Enter floor number"
                prefix={<span className="text-gray-400">Floor</span>}
              />
            </Form.Item>
          </Form>
        </div>
      </Modal>
      <Modal
        title={
          <div className="flex items-center">
            <ExclamationCircleOutlined className="mr-2 text-red-500" />
            <span>Confirm Delete</span>
          </div>
        }
        open={isDeleteModalVisible}
        onOk={handleDelete}
        onCancel={() => setIsDeleteModalVisible(false)}
        confirmLoading={loading}
        okText="Delete"
        okButtonProps={{ className: "bg-red-500" }}
        cancelText="Cancel"
        width={500}
        className="top-8"
      >
        <div className="pt-4">
          <Text>Bạn có chắc muốn xóa phòng này hay không?</Text>
        </div>
      </Modal>
    </div>
  );
}
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
} from "antd";
import {
  PlusOutlined,
  EditOutlined,
  DeleteOutlined,
  ExclamationCircleOutlined,
} from "@ant-design/icons";

const { Title } = Typography;
const { confirm } = Modal;

export default function ManageRoom() {
  const apiUrl = import.meta.env.VITE_API_BASE_URL;

  const [rooms, setRooms] = useState([]);
  const [loading, setLoading] = useState(false);
  const [currentPage, setCurrentPage] = useState(1);
  const [totalPages, setTotalPages] = useState(1);
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [editingRoom, setEditingRoom] = useState(null);
  const [messageApi, contextHolder] = message.useMessage();
  const [form] = Form.useForm();

  const fetchRooms = async (page = 1) => {
    try {
      setLoading(true);
      const response = await fetch(
        `${apiUrl}v1/room-details?page=${page}&size=10&sort=id&direction=asc`
      );

      if (!response.ok) {
        throw new Error("Network response was not ok");
      }

      const data = await response.json();
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

  const showDeleteConfirm = (id, name) => {
    confirm({
      title: "Are you sure you want to delete this room?",
      icon: <ExclamationCircleOutlined />,
      content: `Room: ${name} (ID: ${id})`,
      okText: "Yes, Delete",
      okType: "danger",
      cancelText: "Cancel",
      onOk() {
        handleDelete(id);
      },
    });
  };

  const handleDelete = async (id) => {
    try {
      setLoading(true);
      const response = await fetch(`${apiUrl}v1/room-details/${id}`, {
        method: "DELETE",
      });

      if (!response.ok) {
        throw new Error("Failed to delete room");
      }

      messageApi.success("Room deleted successfully");
      fetchRooms(currentPage);
    } catch (error) {
      messageApi.error("Error deleting room: " + error.message);
    } finally {
      setLoading(false);
    }
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

      if (!response.ok) {
        throw new Error(`Failed to ${editingRoom ? "update" : "add"} room`);
      }
      const data = await response.json();
      messageApi.success(`Phòng ${editingRoom ? "updated" : "added"} thành công`);
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

  const columns = [
    {
      title: "ID",
      dataIndex: "id",
      key: "id",
      width: "10%",
    },
    {
      title: "Name",
      dataIndex: "name",
      key: "name",
      width: "30%",
      render: (text) => <span className="font-medium">{text}</span>,
    },
    {
      title: "Building",
      dataIndex: "building",
      key: "building",
      width: "25%",
      render: (text) => <Tag color={getBuildingColor(text)}>{text}</Tag>,
    },
    {
      title: "Floor",
      dataIndex: "floor",
      key: "floor",
      width: "15%",
      render: (floor) => <span className="text-gray-700">{floor}</span>,
    },
    {
      title: "Actions",
      key: "actions",
      width: "20%",
      render: (_, record) => (
        <Space size="middle">
          <Tooltip title="Edit Room">
            <Button
              type="primary"
              ghost
              icon={<EditOutlined />}
              onClick={() => handleEdit(record)}
              size="small"
            />
          </Tooltip>
          <Tooltip title="Delete Room">
            <Button
              danger
              icon={<DeleteOutlined />}
              onClick={() => showDeleteConfirm(record.id, record.name)}
              size="small"
            />
          </Tooltip>
        </Space>
      ),
    },
  ];

  return (
    <div className="p-6 bg-gray-50 w-full min-h-screen">
      <div className="bg-white rounded-lg shadow-md p-6">
        <div className="flex justify-between items-center mb-6">
          <Title level={3} className="m-0">
            Room Management
          </Title>
          <Button type="primary" icon={<PlusOutlined />} onClick={handleAdd}>
            Add Room
          </Button>
        </div>

        <Table
          columns={columns}
          dataSource={rooms}
          rowKey="id"
          loading={loading}
          className="border border-gray-200 rounded"
          pagination={{
            current: currentPage,
            total: totalPages * 10,
            onChange: (page) => fetchRooms(page),
            showSizeChanger: false,
            showTotal: (total) => `Total ${total} rooms`,
          }}
        />

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
        >
          <div className="pt-4">
            <Form form={form} layout="vertical">
              <Form.Item
                name="name"
                label="Room Name"
                rules={[{ required: true, message: "Please enter room name" }]}
              >
                <Input placeholder="Enter room name" />
              </Form.Item>

              <Form.Item
                name="building"
                label="Building"
                rules={[{ required: true, message: "Please enter building" }]}
              >
                <Input placeholder="Enter building name" />
              </Form.Item>

              <Form.Item
                name="floor"
                label="Floor"
                rules={[
                  { required: true, message: "Please enter floor number" },
                ]}
              >
                <InputNumber
                  min={0}
                  className="w-full"
                  placeholder="Enter floor number"
                />
              </Form.Item>
            </Form>
          </div>
        </Modal>
      </div>
    </div>
  );
}

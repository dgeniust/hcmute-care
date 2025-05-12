import React, { useState, useEffect, useMemo } from "react";
import { useCalendarApp, ScheduleXCalendar } from "@schedule-x/react";
import {
  createViewDay,
  createViewWeek,
  createViewMonthGrid,
} from "@schedule-x/calendar";
import { createEventsServicePlugin } from "@schedule-x/events-service";
import { createDragAndDropPlugin } from "@schedule-x/drag-and-drop";
import { createEventModalPlugin } from "@schedule-x/event-modal";
import "@schedule-x/theme-default/dist/index.css";
import {
  Modal,
  Form,
  Input,
  DatePicker,
  Select,
  Button,
  Spin,
  message,
  Typography,
  Space,
  Divider,
  Card,
  Tooltip,
} from "antd";
import {
  PlusOutlined,
  CalendarOutlined,
  ReloadOutlined,
  EditOutlined,
  DeleteOutlined,
  InfoCircleOutlined,
} from "@ant-design/icons";
import dayjs from "dayjs";
import { showData } from "../../../utils/formatData";

const { Title } = Typography;

const DoctorSchedule = () => {
  const [form] = Form.useForm();
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [editingEvent, setEditingEvent] = useState(null);
  const [loading, setLoading] = useState(false);
  const [selectedView, setSelectedView] = useState("week");
  const [messageApi, contextHolder] = message.useMessage();

  // Create events service plugin once with useState
  const eventsService = useMemo(() => createEventsServicePlugin(), []);

  // State for doctor schedule data
  const [doctorScheduleData, setDoctorScheduleData] = useState([]);

  // Default example events - can be used as fallback
  const initialEvents = [
    {
      id: "1",
      title: "Morning Shift",
      start: "2025-04-14 06:00",
      end: "2025-04-14 11:00",
      description: "Regular morning checkups",
      calendarId: "work",
    },
    {
      id: "2",
      title: "Afternoon Consultations",
      start: "2025-04-14 13:30",
      end: "2025-04-14 16:30",
      description: "Meeting with surgical team",
      calendarId: "meeting",
    },
    // Additional events removed for brevity
  ];

  const doctorId = localStorage.getItem("customerId");

  // Lấy ngày hiện tại (giả sử đang ở tháng 5/2025)
  const currentDate = dayjs(); // Thay bằng dayjs() nếu muốn lấy ngày hệ thống thực tế
  console.log("Current Date:", currentDate);
  // Lấy ngày 1 của tháng hiện tại (1-5-2025)
  const startDate = currentDate.startOf("month").format("YYYY-MM-DD");

  // Lấy ngày cuối của tháng hiện tại (31-5-2025)
  const endDate = currentDate.endOf("month").format("YYYY-MM-DD");
  const apiUrl = import.meta.env.VITE_API_BASE_URL;
  // API configuration
  const base_url = `${apiUrl}v1/` || "https://api.example.com/";
  const api = `${base_url}doctors/${doctorId}/schedules?startDate=${startDate}&endDate=${endDate}&page=1&size=10&sort=id&direction=asc`;

  // Calendar configuration with custom styling
  const calendarConfig = useMemo(
    () => ({
      views: [createViewDay(), createViewWeek(), createViewMonthGrid()],
      plugins: [
        eventsService,
        createDragAndDropPlugin(),
        createEventModalPlugin(),
      ],
      defaultView: selectedView,
      calendars: {
        leisure: {
          colorName: "leisure",
          lightColors: {
            main: "#1677ff",
            container: "#e6f4ff",
            onContainer: "#004080",
          },
          darkColors: {
            main: "#69b1ff",
            container: "#0050b3",
            onContainer: "#d6e8ff",
          },
        },
        work: {
          colorName: "work",
          lightColors: {
            main: "#f5222d",
            container: "#fff1f0",
            onContainer: "#5c0011",
          },
          darkColors: {
            main: "#ff7875",
            container: "#a8071a",
            onContainer: "#ffa39e",
          },
        },
        meeting: {
          colorName: "meeting",
          lightColors: {
            main: "#faad14",
            container: "#fffbe6",
            onContainer: "#613400",
          },
          darkColors: {
            main: "#ffd666",
            container: "#ad6800",
            onContainer: "#fff1b8",
          },
        },
      },
      onEventClick: ({ event }) => showModal(event),
      translations: {
        monthView: "Month",
        weekView: "Week",
        dayView: "Day",
      },
      datePickerDefaults: {
        todayButton: {
          text: "Today",
          className: "bg-blue-500 text-white",
        },
      },
    }),
    [eventsService, selectedView]
  );

  const calendar = useCalendarApp(calendarConfig);

  // Fetch doctor schedule from API
  const fetchDoctorSchedule = async () => {
    setLoading(true);
    try {
      const response = await fetch(api, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
      });

      if (!response.ok) {
        throw new Error(`Error: ${response.status} - ${response.statusText}`);
      }

      const scheduleData = await response.json();
      const formattedData = showData(scheduleData);

      console.log("Schedule Data:", formattedData);
      setDoctorScheduleData(formattedData);
      messageApi.success("Schedule data loaded successfully");
    } catch (error) {
      console.error("❌ Error fetching data:", error);
      messageApi.error("Failed to load schedule data");

      // Load initial events as fallback
      setDoctorScheduleData(initialEvents);
    } finally {
      setLoading(false);
    }
  };

  // Load events into calendar whenever doctorScheduleData changes
  useEffect(() => {
    if (doctorScheduleData.length > 0) {
      eventsService.remove(); // Clear old events
      doctorScheduleData.forEach((event) => {
        eventsService.add(event);
      });
      // No longer calling calendar.rerender() as it doesn't exist
    }
  }, [doctorScheduleData, eventsService]);

  // Initial load
  useEffect(() => {
    fetchDoctorSchedule();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  // Handle modal display
  const showModal = (event = null) => {
    setEditingEvent(event);

    if (event) {
      form.setFieldsValue({
        title: event.title,
        start: dayjs(event.start),
        end: dayjs(event.end),
        description: event.description,
        calendarId: event.calendarId,
      });
    } else {
      const now = dayjs();
      const oneHourLater = dayjs().add(1, "hour");

      form.setFieldsValue({
        title: "",
        start: now,
        end: oneHourLater,
        description: "",
        calendarId: "work",
      });
    }

    setIsModalVisible(true);
  };

  // Handle form submission
  const handleOk = () => {
    form
      .validateFields()
      .then((values) => {
        const newEvent = {
          id: editingEvent ? editingEvent.id : `event-${Date.now()}`,
          title: values.title,
          start: values.start.format("YYYY-MM-DD HH:mm"),
          end: values.end.format("YYYY-MM-DD HH:mm"),
          description: values.description,
          calendarId: values.calendarId,
        };

        if (editingEvent) {
          eventsService.update(newEvent);
          messageApi.success("Event updated successfully");
        } else {
          eventsService.add(newEvent);
          messageApi.success("New event added successfully");
        }

        setIsModalVisible(false);
        form.resetFields();

        // Update the local state to keep things in sync
        if (editingEvent) {
          setDoctorScheduleData((prev) =>
            prev.map((event) => (event.id === newEvent.id ? newEvent : event))
          );
        } else {
          setDoctorScheduleData((prev) => [...prev, newEvent]);
        }
      })
      .catch((info) => {
        console.error("Validate Failed:", info);
      });
  };

  // Handle modal cancellation
  const handleCancel = () => {
    setIsModalVisible(false);
    form.resetFields();
  };

  // Handle event deletion
  const handleDeleteEvent = () => {
    if (editingEvent) {
      eventsService.remove([editingEvent.id]);
      setDoctorScheduleData((prev) =>
        prev.filter((event) => event.id !== editingEvent.id)
      );
      messageApi.success("Event deleted successfully");
      setIsModalVisible(false);
      form.resetFields();
    }
  };

  // Toggle between views
  const handleViewChange = (view) => {
    setSelectedView(view);
    if (calendar && calendar.setView) {
      calendar.setView(view);
    }
  };

  // Error handling for API connection issues
  const handleFetchError = () => {
    messageApi.error({
      content: "Cannot connect to the server. Using sample data instead.",
      duration: 5,
    });
    setDoctorScheduleData(initialEvents);
    setLoading(false);
  };

  return (
    <Card className="w-full shadow-md">
      {contextHolder}
      <div className="mb-6">
        <div className="flex justify-between items-center">
          <Title level={4} className="flex items-center m-0">
            <CalendarOutlined className="mr-2 text-blue-500" /> Lịch làm việc
          </Title>
          <Space>
            <Tooltip title="Add new appointment">
              <Button
                type="primary"
                icon={<PlusOutlined />}
                onClick={() => showModal()}
                className="bg-blue-500 hover:bg-blue-600"
              >
                New Appointment
              </Button>
            </Tooltip>
            <Tooltip title="Refresh schedule data">
              <Button
                icon={<ReloadOutlined />}
                onClick={fetchDoctorSchedule}
                loading={loading}
              >
                Refresh
              </Button>
            </Tooltip>
          </Space>
        </div>
        <Divider className="my-4" />
      </div>

      {loading ? (
        <div className="flex justify-center items-center h-96">
          <Spin size="large" tip="Loading schedule..." />
        </div>
      ) : (
        <div className="border rounded-md overflow-hidden">
          <ScheduleXCalendar calendarApp={calendar} className="min-h-96" />
        </div>
      )}

      <Modal
        title={
          <div className="flex items-center">
            {editingEvent ? (
              <EditOutlined className="mr-2" />
            ) : (
              <PlusOutlined className="mr-2" />
            )}
            {editingEvent ? "Edit Appointment" : "Add New Appointment"}
          </div>
        }
        open={isModalVisible}
        onOk={handleOk}
        onCancel={handleCancel}
        okButtonProps={{ className: "bg-blue-500 hover:bg-blue-600" }}
        footer={[
          editingEvent && (
            <Button
              key="delete"
              danger
              icon={<DeleteOutlined />}
              onClick={handleDeleteEvent}
            >
              Delete
            </Button>
          ),
          <Button key="cancel" onClick={handleCancel}>
            Cancel
          </Button>,
          <Button
            key="submit"
            type="primary"
            onClick={handleOk}
            className="bg-blue-500 hover:bg-blue-600"
          >
            {editingEvent ? "Update" : "Add"}
          </Button>,
        ]}
      >
        <Form form={form} layout="vertical" className="mt-4">
          <Form.Item
            name="title"
            label="Title"
            rules={[
              { required: true, message: "Please enter appointment title" },
            ]}
          >
            <Input placeholder="E.g., Morning Shift" className="w-full" />
          </Form.Item>

          <div className="grid grid-cols-2 gap-4">
            <Form.Item
              name="start"
              label="Start Time"
              rules={[{ required: true, message: "Select start time" }]}
            >
              <DatePicker
                showTime
                format="YYYY-MM-DD HH:mm"
                className="w-full"
              />
            </Form.Item>

            <Form.Item
              name="end"
              label="End Time"
              rules={[
                { required: true, message: "Select end time" },
                ({ getFieldValue }) => ({
                  validator(_, value) {
                    if (!value || getFieldValue("start").isBefore(value)) {
                      return Promise.resolve();
                    }
                    return Promise.reject(
                      new Error("End time must be after start time")
                    );
                  },
                }),
              ]}
            >
              <DatePicker
                showTime
                format="YYYY-MM-DD HH:mm"
                className="w-full"
              />
            </Form.Item>
          </div>

          <Form.Item name="description" label="Description">
            <Input.TextArea
              rows={3}
              className="w-full"
              placeholder="Add details about this appointment..."
            />
          </Form.Item>

          <Form.Item
            name="calendarId"
            label="Category"
            rules={[{ required: true, message: "Please select a category" }]}
            tooltip={{
              title: "Categorize your appointment for better organization",
              icon: <InfoCircleOutlined />,
            }}
          >
            <Select className="w-full">
              <Select.Option value="work">
                <span className="flex items-center">
                  <span className="w-3 h-3 rounded-full bg-red-500 mr-2"></span>
                  Work
                </span>
              </Select.Option>
              <Select.Option value="meeting">
                <span className="flex items-center">
                  <span className="w-3 h-3 rounded-full bg-yellow-500 mr-2"></span>
                  Meeting
                </span>
              </Select.Option>
              <Select.Option value="leisure">
                <span className="flex items-center">
                  <span className="w-3 h-3 rounded-full bg-blue-500 mr-2"></span>
                  Leisure
                </span>
              </Select.Option>
            </Select>
          </Form.Item>
        </Form>
      </Modal>
    </Card>
  );
};

export default DoctorSchedule;

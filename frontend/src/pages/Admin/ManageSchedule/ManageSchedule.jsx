import React, { useState, useEffect, useMemo } from 'react';
import { useCalendarApp, ScheduleXCalendar } from '@schedule-x/react';
import {
  createViewDay,
  createViewWeek,
  createViewMonthGrid,
} from '@schedule-x/calendar';
import { createEventsServicePlugin } from '@schedule-x/events-service';
import { createDragAndDropPlugin } from '@schedule-x/drag-and-drop';
import { createEventModalPlugin } from '@schedule-x/event-modal';
import '@schedule-x/theme-default/dist/index.css';
import { 
  Modal, 
  Form, 
  InputNumber, 
  DatePicker, 
  Select, 
  Button, 
  Spin, 
  message, 
  Typography, 
  Space, 
  Divider,
  Card,
  Tooltip
} from 'antd';
import { 
  PlusOutlined, 
  CalendarOutlined, 
  ReloadOutlined,
  SearchOutlined
} from '@ant-design/icons';
import dayjs from 'dayjs';
import { showData } from '../../../utils/formatData';

const { Title } = Typography;

const ManageSchedule = () => {
  const [form] = Form.useForm();
  const [searchForm] = Form.useForm();
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [loading, setLoading] = useState(false);
  const [selectedView, setSelectedView] = useState('week');
  const [messageApi, contextHolder] = message.useMessage();
  
  // State for doctor schedule data
  const [doctorScheduleData, setDoctorScheduleData] = useState([]);
  // State for medical specialties, doctors, and time slots
  const [specialties, setSpecialties] = useState([]);
  const [doctors, setDoctors] = useState([]);
  const [timeSlots, setTimeSlots] = useState([]);
  const [selectedSpecialty, setSelectedSpecialty] = useState(null);

  // Create events service plugin
  const eventsService = useMemo(() => {
    const service = createEventsServicePlugin();
    console.log('Initialized eventsService:', Object.getOwnPropertyNames(Object.getPrototypeOf(service)));
    return service;
  }, []);

  // Default example events - fallback
  const initialEvents = [
    {
      id: '1',
      title: 'Morning Shift',
      start: '2025-05-01 06:00',
      end: '2025-05-01 11:00',
      description: 'Regular morning checkups',
      calendarId: 'work',
    },
    {
      id: '2',
      title: 'Afternoon Consultations',
      start: '2025-05-01 13:30',
      end: '2025-05-01 16:30',
      description: 'Meeting with surgical team',
      calendarId: 'meeting',
    },
  ];

  // API configuration
  const base_url = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api/v1/';
  
  // Calendar configuration
  const calendarConfig = useMemo(() => ({
    views: [
      createViewDay(), 
      createViewWeek(), 
      createViewMonthGrid()
    ],
    plugins: [
      eventsService,
      createDragAndDropPlugin(),
      createEventModalPlugin(),
    ],
    defaultView: selectedView,
    calendars: {
      leisure: {
        colorName: 'leisure',
        lightColors: {
          main: '#1677ff',
          container: '#e6f4ff',
          onContainer: '#004080',
        },
        darkColors: {
          main: '#69b1ff',
          container: '#0050b3',
          onContainer: '#d6e8ff',
        }
      },
      work: {
        colorName: 'work',
        lightColors: {
          main: '#f5222d',
          container: '#fff1f0',
          onContainer: '#5c0011',
        },
        darkColors: {
          main: '#ff7875',
          container: '#a8071a',
          onContainer: '#ffa39e',
        },
      },
      meeting: {
        colorName: 'meeting',
        lightColors: {
          main: '#faad14',
          container: '#fffbe6',
          onContainer: '#613400',
        },
        darkColors: {
          main: '#ffd666',
          container: '#ad6800',
          onContainer: '#fff1b8',
        },
      },
    },
    translations: {
      monthView: 'Month',
      weekView: 'Week',
      dayView: 'Day',
    },
    datePickerDefaults: {
      todayButton: {
        text: 'Today',
        className: 'bg-blue-500 text-white',
      }
    }
  }), [eventsService, selectedView]);
  
  const calendar = useCalendarApp(calendarConfig);

  // Fetch medical specialties
  const fetchSpecialties = async () => {
    try {
      const response = await fetch(`${base_url}medical-specialties?page=1&size=30&sort=id&direction=asc`, {
        method: 'GET',
      });
      if (!response.ok) {
        throw new Error(`Error: ${response.status} - ${response.statusText}`);
      }
      const data = await response.json();
      setSpecialties(data.data.content || []);
    } catch (error) {
      console.error("❌ Error fetching specialties:", error);
      messageApi.error('Failed to load medical specialties');
    }
  };

  // Fetch doctors by specialty
  const fetchDoctorsBySpecialty = async (specialtyId) => {
    try {
      const response = await fetch(`${base_url}medical-specialties/${specialtyId}/doctors?page=1&size=20&sort=id&direction=asc`, {
        method: 'GET',
      });
      if (!response.ok) {
        throw new Error(`Error: ${response.status} - ${response.statusText}`);
      }
      const data = await response.json();
      setDoctors(data.data.content || []);
    } catch (error) {
      console.error("❌ Error fetching doctors:", error);
      messageApi.error('Failed to load doctors');
    }
  };

  // Fetch time slots
  const fetchTimeSlots = async () => {
    try {
      const response = await fetch(`${base_url}time-slots`, {
        method: 'GET',
      });
      if (!response.ok) {
        throw new Error(`Error: ${response.status} - ${response.statusText}`);
      }
      const data = await response.json();
      setTimeSlots(data.data || []);
    } catch (error) {
      console.error("❌ Error fetching time slots:", error);
      messageApi.error('Failed to load time slots');
    }
  };

  // Fetch doctor schedule
  const fetchDoctorSchedule = async (doctorId) => {
    setLoading(true);
    try {
      const startDate = '2025-05-01';
      const endDate = '2025-05-31';
      const api = `${base_url}schedules?doctorId=${doctorId}&startDate=${startDate}&endDate=${endDate}&page=1&size=10&sort=date&direction=asc`;
      const response = await fetch(api, { method: 'GET' });
      
      if (!response.ok) {
        throw new Error(`Error: ${response.status} - ${response.statusText}`);
      }
      
      const scheduleData = await response.json();
      const formattedData = showData(scheduleData, doctors, timeSlots);
      
      console.log('Schedule Data:', formattedData);
      setDoctorScheduleData(formattedData);
      messageApi.success('Schedule data loaded successfully');
    } catch (error) {
      console.error("❌ Error fetching schedule:", error);
      messageApi.error('Failed to load schedule data');
      setDoctorScheduleData(initialEvents);
    } finally {
      setLoading(false);
    }
  };

  // Load specialties and time slots on component mount
  useEffect(() => {
    fetchSpecialties();
    fetchTimeSlots();
  }, []);

  // Load events into calendar
  useEffect(() => {
    if (doctorScheduleData.length > 0 && eventsService) {
      console.log('eventsService methods:', Object.getOwnPropertyNames(Object.getPrototypeOf(eventsService)));
      
      if (typeof eventsService.clear === 'function') {
        eventsService.clear();
      } else if (typeof eventsService.getAll === 'function' && typeof eventsService.remove === 'function') {
        const eventIds = eventsService.getAll().map(event => event.id);
        eventsService.remove(eventIds);
      } else {
        console.warn('No clear method available for eventsService');
      }
      
      doctorScheduleData.forEach(event => {
        eventsService.add(event);
      });
    }
  }, [doctorScheduleData, eventsService]);

  // Handle specialty selection
  const handleSpecialtyChange = (specialtyId) => {
    setSelectedSpecialty(specialtyId);
    setDoctors([]);
    form.setFieldsValue({ doctorId: null });
    searchForm.setFieldsValue({ doctorId: null });
    fetchDoctorsBySpecialty(specialtyId);
  };

  // Handle search
  const handleSearch = (values) => {
    if (values.doctorId) {
      fetchDoctorSchedule(values.doctorId);
    } else {
      messageApi.warning('Please select a doctor');
    }
  };

  // Handle modal display
  const showModal = () => {
    form.setFieldsValue({
      specialtyId: null,
      doctorId: null,
      date: dayjs(),
      maxSlots: 6,
      roomId: null,
      timeSlotIds: [],
    });
    setIsModalVisible(true);
  };

  // Handle form submission
  const handleOk = () => {
    form.validateFields()
      .then(async (values) => {
        const payload = {
          date: values.date.format('YYYY-MM-DD'),
          maxSlots: values.maxSlots,
          roomId: values.roomId,
          doctorId: values.doctorId,
          timeSlotIds: values.timeSlotIds,
        };
        console.log('Form Values:', payload);
        console.log('Fetch URL:', `${base_url}schedules`);
        try {
          const response = await fetch(`${base_url}schedules`, {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
              'Accept': '*/*',
            },
            body: JSON.stringify(payload),
          });

          if (!response.ok) {
            const errorText = await response.text();
            console.error('Error response:', errorText);
            return;
          }

          const newSchedule = await response.json();
          console.log('POST response:', newSchedule);
          if(newSchedule.status === 40) {
            messageApi.error('Bác sĩ đã có lịch khám trong khoảng thời gian này');
            return;
          }
          if(newSchedule.status === 404) {
            messageApi.error('Không tìm thấy giờ làm việc');
            return;
          }
          
          const formattedData = showData(
            { data: { content: [newSchedule.data || newSchedule] } },
            doctors,
            timeSlots
          );

          if (formattedData.length > 0) {
            setDoctorScheduleData(prev => [...prev, ...formattedData]);
            messageApi.success('Schedule created successfully');
          } else {
            messageApi.warning('No events generated from the schedule');
          }

          setIsModalVisible(false);
          form.resetFields();
        } catch (error) {
          console.error('❌ Error creating schedule:', error);
          messageApi.error(error.message.includes('409') 
            ? 'Bác sĩ đã có lịch khám trong khoảng thời gian này' 
            : 'Failed to create schedule');
        }
      })
      .catch(info => {
        console.error('Validate Failed:', info);
      });
  };

  // Handle modal cancellation
  const handleCancel = () => {
    setIsModalVisible(false);
    form.resetFields();
  };

  // Toggle between views
  const handleViewChange = (view) => {
    setSelectedView(view);
    if (calendar && calendar.setView) {
      calendar.setView(view);
    }
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
            <Tooltip title="Add new schedule">
              <Button 
                type="primary"
                icon={<PlusOutlined />}
                onClick={showModal}
                className="bg-blue-500 hover:bg-blue-600"
              >
                New Schedule
              </Button>
            </Tooltip>
            <Tooltip title="Refresh schedule data">
              <Button 
                icon={<ReloadOutlined />} 
                onClick={() => searchForm.submit()}
                loading={loading}
              >
                Refresh
              </Button>
            </Tooltip>
          </Space>
        </div>
        <Divider className="my-4" />
        <Form
          form={searchForm}
          layout="inline"
          onFinish={handleSearch}
          className="mb-4"
        >
          <Form.Item
            name="specialtyId"
            label="Medical Specialty"
            rules={[{ required: true, message: 'Please select a specialty' }]}
          >
            <Select
              placeholder="Select specialty"
              onChange={handleSpecialtyChange}
              className="w-60"
            >
              {specialties.map(specialty => (
                <Select.Option key={specialty.id} value={specialty.id}>
                  {specialty.name}
                </Select.Option>
              ))}
            </Select>
          </Form.Item>
          <Form.Item
            name="doctorId"
            label="Doctor"
            rules={[{ required: true, message: 'Please select a doctor' }]}
          >
            <Select
              placeholder="Select doctor"
              className="w-60"
              disabled={!selectedSpecialty}
            >
              {doctors.map(doctor => (
                <Select.Option key={doctor.id} value={doctor.id}>
                  {doctor.fullName}
                </Select.Option>
              ))}
            </Select>
          </Form.Item>
          <Form.Item>
            <Button
              type="primary"
              icon={<SearchOutlined />}
              htmlType="submit"
              className="bg-blue-500 hover:bg-blue-600"
            >
              Search
            </Button>
          </Form.Item>
        </Form>
      </div>

      {loading ? (
        <div className="flex justify-center items-center h-96">
          <Spin size="large" tip="Loading schedule..." />
        </div>
      ) : (
        <div className="border rounded-md overflow-hidden">
          <ScheduleXCalendar 
            calendarApp={calendar} 
            className="min-h-96" 
          />
        </div>
      )}

      <Modal
        title={
          <div className="flex items-center">
            <PlusOutlined className="mr-2" />
            Add New Schedule
          </div>
        }
        open={isModalVisible}
        onOk={handleOk}
        onCancel={handleCancel}
        okButtonProps={{ className: "bg-blue-500 hover:bg-blue-600" }}
        footer={[
          <Button key="cancel" onClick={handleCancel}>
            Cancel
          </Button>,
          <Button
            key="submit"
            type="primary"
            onClick={handleOk}
            className="bg-blue-500 hover:bg-blue-600"
          >
            Add
          </Button>,
        ]}
      >
        <Form
          form={form}
          layout="vertical"
          className="mt-4"
        >
          <Form.Item
            name="specialtyId"
            label="Medical Specialty"
            rules={[{ required: true, message: 'Please select a specialty' }]}
          >
            <Select
              placeholder="Select specialty"
              onChange={handleSpecialtyChange}
            >
              {specialties.map(specialty => (
                <Select.Option key={specialty.id} value={specialty.id}>
                  {specialty.name}
                </Select.Option>
              ))}
            </Select>
          </Form.Item>
          <Form.Item
            name="doctorId"
            label="Doctor"
            rules={[{ required: true, message: 'Please select a doctor' }]}
          >
            <Select
              placeholder="Select doctor"
              className='w-[400px]'
              disabled={!doctors.length}
            >
              {doctors.map(doctor => (
                <Select.Option key={doctor.id} value={doctor.id}>
                  {doctor.fullName}
                </Select.Option>
              ))}
            </Select>
          </Form.Item>
          <Form.Item
            name="date"
            label="Date"
            rules={[{ required: true, message: 'Please select a date' }]}
          >
            <DatePicker
              format="YYYY-MM-DD"
              className="w-full"
            />
          </Form.Item>
          <Form.Item
            name="maxSlots"
            label="Max Slots"
            rules={[
              { required: true, message: 'Please enter max slots' },
              { type: 'number', min: 1, message: 'Max slots must be at least 1' },
            ]}
          >
            <InputNumber min={1} className="w-full" />
          </Form.Item>
          <Form.Item
            name="roomId"
            label="Room ID"
            rules={[{ required: true, message: 'Please enter room ID' }]}
          >
            <InputNumber min={1} className="w-full" />
          </Form.Item>
          <Form.Item
            name="timeSlotIds"
            label="Time Slots"
            rules={[{ required: true, message: 'Please select at least one time slot' }]}
          >
            <Select
              mode="multiple"
              placeholder="Select time slots"
              className="w-full"
            >
              {timeSlots.map(slot => (
                <Select.Option key={slot.id} value={slot.id}>
                  {`${slot.startTime.slice(0, 5)} - ${slot.endTime.slice(0, 5)}`}
                </Select.Option>
              ))}
            </Select>
          </Form.Item>
        </Form>
      </Modal>
    </Card>
  );
};

export default ManageSchedule;
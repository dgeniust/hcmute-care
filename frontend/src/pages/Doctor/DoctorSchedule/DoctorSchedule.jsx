import React, { useState, useEffect } from 'react'
import { useCalendarApp, ScheduleXCalendar } from '@schedule-x/react'
import {
  createViewMonthGrid,
  createViewWeek,
  createViewDay,
} from '@schedule-x/calendar'
import { createEventsServicePlugin } from '@schedule-x/events-service'
import { createDragAndDropPlugin } from '@schedule-x/drag-and-drop'
import '@schedule-x/theme-default/dist/index.css'
import { createEventModalPlugin } from '@schedule-x/event-modal'
import { Modal, Form, Input, DatePicker, Select, Button } from 'antd'
import moment from 'moment'
// import {showData} from '../../../utils/formatData'
const DoctorSchedule = () => {
  const [form] = Form.useForm()
  const [isModalVisible, setIsModalVisible] = useState(false)
  const [editingEvent, setEditingEvent] = useState(null)
  
  const eventsService = useState(() => createEventsServicePlugin())[0]

  const initialEvents = [
    {
      id: '1',
      title: 'Morning Shift',
      start: '2025-04-14 06:00',
      end: '2025-04-14 11:00',
      description: 'Regular morning checkups',
      calendarId: 'work',
    },
    {
      id: '2',
      title: 'Afternoon Consultations',
      start: '2025-04-14 13:30',
      end: '2025-04-14 16:30',
      description: 'Meeting with surgical team',
      calendarId: 'meeting',
    },
    {
      id: '3',
      title: 'Morning Shift',
      start: '2025-04-15 06:00',
      end: '2025-04-15 11:00',
      description: 'Pediatrics appointments',
      calendarId: 'work',
    },
    {
      id: '4',
      title: 'Afternoon Consultations',
      start: '2025-04-15 13:30',
      end: '2025-04-15 16:30',
      description: 'Post-op evaluations',
      calendarId: 'work',
    },
    {
      id: '5',
      title: 'Morning Shift',
      start: '2025-04-16 06:00',
      end: '2025-04-16 11:00',
      description: 'Cardiology rounds',
      calendarId: 'meeting',
    },
    {
      id: '6',
      title: 'Afternoon Consultations',
      start: '2025-04-16 13:30',
      end: '2025-04-16 16:30',
      description: 'Team strategy session',
      calendarId: 'meeting',
    },
    {
      id: '7',
      title: 'Morning Shift',
      start: '2025-04-17 06:00',
      end: '2025-04-17 11:00',
      description: 'General diagnostics',
      calendarId: 'work',
    },
    {
      id: '8',
      title: 'Afternoon Consultations',
      start: '2025-04-17 13:30',
      end: '2025-04-17 16:30',
      description: 'Patient file reviews',
      calendarId: 'work',
    },
    {
      id: '9',
      title: 'Morning Shift',
      start: '2025-04-18 06:00',
      end: '2025-04-18 11:00',
      description: 'Routine checkups',
      calendarId: 'work',
    },
    {
      id: '10',
      title: 'Afternoon Consultations',
      start: '2025-04-18 13:30',
      end: '2025-04-18 16:30',
      description: 'Staff training session',
      calendarId: 'meeting',
    },
    {
      id: '11',
      title: 'Morning Shift',
      start: '2025-04-19 06:00',
      end: '2025-04-19 11:00',
      description: 'Surgical prep overview',
      calendarId: 'meeting',
    },
    {
      id: '12',
      title: 'Afternoon Consultations',
      start: '2025-04-19 13:30',
      end: '2025-04-19 16:30',
      description: 'Clinical research update',
      calendarId: 'meeting',
    },
    {
      id: '13',
      title: 'Morning Shift',
      start: '2025-04-20 06:00',
      end: '2025-04-20 11:00',
      description: 'New patient evaluations',
      calendarId: 'work',
    },
    {
      id: '14',
      title: 'Afternoon Consultations',
      start: '2025-04-20 13:30',
      end: '2025-04-20 16:30',
      description: 'End-of-week reports',
      calendarId: 'work',
    },

  ]
  // const [doctorScheduleData, setDoctorScheduleData] = useState()
  // const base_url = import.meta.env.VITE_API_BASE_URL;
  // const api = base_url + 'schedules?page=1&size=30&sort=id&direction=asc'
  // const fetchDoctorSchedule = async () => {
  //   try {
  //     const response = await fetch(api,
  //       {
  //       method: 'POST',
  //       }
  //     );
  //     console.log('RES: ' + api);
  //     if(!response.ok) {
  //       throw new Error(`Lỗi: ${response.status} - ${response.statusText}`);
  //     }
  //     const scheduleData = await response.json();
  //     const finalResult = showData(scheduleData);
  //     console.log('Schedule Data:', finalResult);
  //   }
  //   catch(e) {
  //     console.error("❌ Lỗi khi fetch:", e);
  //   }
  // }

  const calendar = useCalendarApp({
    views: [createViewDay(), createViewWeek(), createViewMonthGrid()],
    plugins: [
      eventsService,
      createDragAndDropPlugin(),
      createEventModalPlugin(),
    ],
    calendars: {
      leisure:{
        colorName: 'leisure',
        lightColors: {
          main: '#1c7df9',
          container: '#d2e7ff',
          onContainer: '#002859',
        }
      },
      work: {
        colorName: 'work',
        lightColors: {
          main: '#f91c45',
          container: '#ffd2dc',
          onContainer: '#59000d',
        },
        darkColors: {
          main: '#ffc0cc',
          onContainer: '#ffdee6',
          container: '#a24258',
        },
      },
      meeting: {
        colorName: 'personal',
        lightColors: {
          main: '#f9d71c',
          container: '#fff5aa',
          onContainer: '#594800',
        },
        darkColors: {
          main: '#fff5c0',
          onContainer: '#fff5de',
          container: '#a29742',
        },
      },
    },
    onEventClick: ({ event }) => {
      showModal(event)
    }
  })

  useEffect(() => {
    initialEvents.forEach(event => {
      eventsService.add(event)
    })
  }, [])

  const showModal = (event = null) => {
    setEditingEvent(event)
    if (event) {
      form.setFieldsValue({
        title: event.title,
        start: moment(event.start),
        end: moment(event.end),
        description: event.description,
        calendarId: event.calendarId,
      })
    } else {
      form.resetFields()
    }
    setIsModalVisible(true)
  }

  const handleOk = () => {
    form.validateFields().then(values => {
      const newEvent = {
        id: editingEvent ? editingEvent.id : Date.now().toString(),
        title: values.title,
        start: values.start.format('YYYY-MM-DD HH:mm'),
        end: values.end.format('YYYY-MM-DD HH:mm'),
        description: values.description,
        calendarId: values.calendarId,
      }

      if (editingEvent) {
        eventsService.update(newEvent)
      } else {
        eventsService.add(newEvent)
      }

      setIsModalVisible(false)
      form.resetFields()
      // Force calendar re-render by updating events
      calendar.rerender()
    })
  }

  const handleCancel = () => {
    setIsModalVisible(false)
    form.resetFields()
  }

  return (
    <div className="w-full h-full p-4">
      <div className="mb-4">
        <Button 
          type="primary"
          onClick={() => showModal()}
          className="bg-blue-500 hover:bg-blue-600"
        >
          Add New Event
        </Button>
        <Button onClick={fetchDoctorSchedule}>Fetch</Button>
      </div>

      <ScheduleXCalendar calendarApp={calendar} />

      <Modal
        title={editingEvent ? "Edit Event" : "Add New Event"}
        open={isModalVisible}
        onOk={handleOk}
        onCancel={handleCancel}
        okButtonProps={{ className: "bg-blue-500 hover:bg-blue-600" }}
      >
        <Form
          form={form}
          layout="vertical"
          className="space-y-4"
        >
          <Form.Item
            name="title"
            label="Title"
            rules={[{ required: true, message: 'Please enter event title' }]}
          >
            <Input className="w-full" />
          </Form.Item>

          <Form.Item
            name="start"
            label="Start Time"
            rules={[{ required: true, message: 'Please select start time' }]}
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
            rules={[{ required: true, message: 'Please select end time' }]}
          >
            <DatePicker 
              showTime 
              format="YYYY-MM-DD HH:mm"
              className="w-full"
            />
          </Form.Item>

          <Form.Item
            name="description"
            label="Description"
          >
            <Input.TextArea rows={3} className="w-full" />
          </Form.Item>

          <Form.Item
            name="calendarId"
            label="Category"
            rules={[{ required: true, message: 'Please select a category' }]}
          >
            <Select className="w-full">
              <Select.Option value="work">Work</Select.Option>
              <Select.Option value="meeting">Meeting</Select.Option>
              <Select.Option value="leisure">Leisure</Select.Option>
            </Select>
          </Form.Item>
        </Form>
      </Modal>
    </div>
  )
}

export default DoctorSchedule
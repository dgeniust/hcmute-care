package vn.edu.hcmute.utecare.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.utecare.dto.request.AppointmentRequest;
import vn.edu.hcmute.utecare.dto.response.AppointmentResponse;
import vn.edu.hcmute.utecare.exception.ConflictException;
import vn.edu.hcmute.utecare.exception.ResourceNotFoundException;
import vn.edu.hcmute.utecare.mapper.AppointmentMapper;
import vn.edu.hcmute.utecare.model.Appointment;
import vn.edu.hcmute.utecare.model.MedicalRecord;
import vn.edu.hcmute.utecare.model.ScheduleSlot;
import vn.edu.hcmute.utecare.model.Ticket;
import vn.edu.hcmute.utecare.repository.AppointmentRepository;
import vn.edu.hcmute.utecare.repository.MedicalRecordRepository;
import vn.edu.hcmute.utecare.repository.ScheduleSlotRepository;
import vn.edu.hcmute.utecare.repository.TicketRepository;
import vn.edu.hcmute.utecare.service.AppointmentService;
import vn.edu.hcmute.utecare.util.enumeration.TicketStatus;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentServiceImpl implements AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final TicketRepository ticketRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final ScheduleSlotRepository scheduleSlotRepository;

    @Transactional
    @Override
    public AppointmentResponse createAppointment(AppointmentRequest request){
        log.info("Creating appointment with request: {}", request);

        MedicalRecord medicalRecord = medicalRecordRepository.findById(request.getMedicalRecordId())
                .orElseThrow(() -> new ResourceNotFoundException("Medical record not found with ID: " + request.getMedicalRecordId()));

        //thêm check permission

        List<ScheduleSlot> scheduleSlots = scheduleSlotRepository.findAllById(request.getScheduleSlotIds());
        if (scheduleSlots.size() != request.getScheduleSlotIds().size()) {
            throw new ResourceNotFoundException("Some schedule slots not found");
        }

        for (ScheduleSlot scheduleSlot : scheduleSlots) {
            if (scheduleSlot.getBookedSlots() >= scheduleSlot.getSchedule().getMaxSlots()){
                throw new ConflictException("Schedule slot is full for ID: " + scheduleSlot.getId());
            }
        }

        Set<Integer> medicalSpecialtyIds = new HashSet<>();
        for (ScheduleSlot scheduleSlot : scheduleSlots) {
            Integer specialtyId = scheduleSlot.getSchedule().getDoctor().getMedicalSpecialty().getId();
            if (!medicalSpecialtyIds.add(specialtyId)) {
                throw new ConflictException("Duplicate medical specialty detected for slot ID: " + scheduleSlot.getId());
            }
        }

        Appointment appointment = Appointment.builder()
                .medicalRecord(medicalRecord)
                .build();

        Set<Ticket> tickets = new HashSet<>();
        for (ScheduleSlot scheduleSlot : scheduleSlots) {
            Ticket ticket = Ticket.builder()
                    .status(TicketStatus.PENDING)
                    .scheduleSlot(scheduleSlot)
                    .appointment(appointment)
                    .build();
            tickets.add(ticket);
            scheduleSlot.setBookedSlots(scheduleSlot.getBookedSlots() + 1);
        }
        appointment.setTickets(tickets);
        return AppointmentMapper.INSTANCE.toResponse(appointmentRepository.save(appointment));
    }

    @Transactional(readOnly = true)
    @Override
    public AppointmentResponse getAppointmentById(Long id) {
        log.info("Fetching appointment with ID: {}", id);
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with ID: " + id));
        return AppointmentMapper.INSTANCE.toResponse(appointment);
    }
}

package vn.edu.hcmute.utecare.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.utecare.dto.request.AppointmentRequest;
import vn.edu.hcmute.utecare.dto.response.AppointmentResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
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
import vn.edu.hcmute.utecare.util.PaginationUtil;
import vn.edu.hcmute.utecare.util.enumeration.TicketStatus;

import java.security.SecureRandom;
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
    private final AppointmentMapper appointmentMapper;

    @Transactional
    @Override
    public AppointmentResponse createAppointment(AppointmentRequest request) {
        log.info("Creating appointment with request: {}", request);

        // Kiểm tra hồ sơ y tế
        MedicalRecord medicalRecord = validateMedicalRecord(request.getMedicalRecordId());

        // Kiểm tra các khung giờ lịch
        List<ScheduleSlot> scheduleSlots = validateScheduleSlots(request.getScheduleSlotIds());

        // Tạo lịch hẹn
        Appointment appointment = Appointment.builder()
                .medicalRecord(medicalRecord)
                .build();

        // Tạo vé và cập nhật khung giờ lịch
        Set<Ticket> tickets = createTicketsForAppointment(appointment, scheduleSlots);
        appointment.setTickets(tickets);
        updateScheduleSlots(scheduleSlots);

        // Lưu và trả về
        return appointmentMapper.toResponse(appointmentRepository.save(appointment));
    }

    private MedicalRecord validateMedicalRecord(Long medicalRecordId) {
        return medicalRecordRepository.findById(medicalRecordId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Không tìm thấy hồ sơ y tế với ID: %d", medicalRecordId)));
    }

    private List<ScheduleSlot> validateScheduleSlots(List<Long> slotIds) {
        List<ScheduleSlot> scheduleSlots = scheduleSlotRepository.findByIdsWithScheduleAndDoctor(slotIds);
        if (scheduleSlots.size() != slotIds.size()) {
            List<Long> foundIds = scheduleSlots.stream().map(ScheduleSlot::getId).toList();
            List<Long> missingIds = slotIds.stream().filter(id -> !foundIds.contains(id)).toList();
            throw new ResourceNotFoundException(
                    String.format("Không tìm thấy khung giờ lịch với các ID: %s", missingIds));
        }

        for (ScheduleSlot scheduleSlot : scheduleSlots) {
            int maxSlots = scheduleSlot.getSchedule().getMaxSlots();
            if (scheduleSlot.getBookedSlots() >= maxSlots) {
                throw new ConflictException(
                        String.format("Khung giờ lịch ID: %d đã đầy (giới hạn %d suất)", scheduleSlot.getId(), maxSlots));
            }
        }

        Set<Integer> medicalSpecialtyIds = new HashSet<>();
        for (ScheduleSlot scheduleSlot : scheduleSlots) {
            Integer specialtyId = scheduleSlot.getSchedule().getDoctor().getMedicalSpecialty().getId();
            if (!medicalSpecialtyIds.add(specialtyId)) {
                throw new ConflictException(
                        String.format("Phát hiện trùng chuyên khoa y tế cho khung giờ ID: %d", scheduleSlot.getId()));
            }
        }

        return scheduleSlots;
    }

    private Set<Ticket> createTicketsForAppointment(Appointment appointment, List<ScheduleSlot> scheduleSlots) {
        Set<Ticket> tickets = new HashSet<>();
        for (ScheduleSlot scheduleSlot : scheduleSlots) {
            Ticket ticket = Ticket.builder()
                    .status(TicketStatus.PENDING)
                    .scheduleSlot(scheduleSlot)
                    .appointment(appointment)
                    .build();
            tickets.add(ticket);
        }
        return tickets;
    }

    private void updateScheduleSlots(List<ScheduleSlot> scheduleSlots) {
        for (ScheduleSlot scheduleSlot : scheduleSlots) {
            scheduleSlot.setBookedSlots(scheduleSlot.getBookedSlots() + 1);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public AppointmentResponse getAppointmentById(Long id) {
        log.info("Lấy thông tin lịch hẹn với ID: {}", id);
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Không tìm thấy lịch hẹn với ID: %d", id)));
        return appointmentMapper.toResponse(appointment);
    }

    @Transactional(readOnly = true)
    @Override
    public PageResponse<AppointmentResponse> getAppointmentByMedicalRecordId(Long medicalRecordId, int page, int size, String sort, String direction) {
        log.info("Lấy danh sách lịch hẹn cho hồ sơ y tế ID: {} với trang: {} và kích thước: {}", medicalRecordId, page, size);

        Pageable pageable = PaginationUtil.createPageable(page, size, sort, direction);

        Page<Appointment> appointmentPage = appointmentRepository.findAllByMedicalRecordId(medicalRecordId, pageable);

        return PageResponse.<AppointmentResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalElements(appointmentPage.getTotalElements())
                .totalPages(appointmentPage.getTotalPages())
                .content(appointmentPage.getContent().stream()
                        .map(appointmentMapper::toResponse)
                        .toList())
                .build();
    }

    @Transactional
    @Override
    public AppointmentResponse confirmAppointment(Long appointmentId) {
        log.info("Xác nhận lịch hẹn với ID: {}", appointmentId);
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Không tìm thấy lịch hẹn với ID: %d", appointmentId)));

        for (Ticket ticket : appointment.getTickets()) {
            if (ticket.getStatus() != TicketStatus.PENDING) {
                throw new ConflictException(
                        String.format("Vé ID: %d không ở trạng thái CHỜ XÁC NHẬN (trạng thái hiện tại: %s)", ticket.getId(), ticket.getStatus()));
            }
            ticket.setWaitingNumber(calculateWaitingNumber(ticket));
            ticket.setTicketCode(generateTicketCode());
            ticket.setStatus(TicketStatus.CONFIRMED);
        }

        return appointmentMapper.toResponse(appointmentRepository.save(appointment));
    }

    @Transactional
    @Override
    public AppointmentResponse cancelAppointment(Long appointmentId) {
        log.info("Hủy lịch hẹn với ID: {}", appointmentId);
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Không tìm thấy lịch hẹn với ID: %d", appointmentId)));

        for (Ticket ticket : appointment.getTickets()) {
            if (ticket.getStatus() != TicketStatus.PENDING) {
                throw new ConflictException(
                        String.format("Vé ID: %d không ở trạng thái CHỜ XÁC NHẬN (trạng thái hiện tại: %s)", ticket.getId(), ticket.getStatus()));
            }
            ticket.setStatus(TicketStatus.CANCELLED);
        }

        return appointmentMapper.toResponse(appointmentRepository.save(appointment));
    }

    private String generateTicketCode() {
        SecureRandom random = new SecureRandom();
        long timestamp = System.currentTimeMillis() % 10_000; // 4 chữ số từ timestamp
        long randomPart = random.nextLong() % 1_000_000; // 6 chữ số ngẫu nhiên
        return String.format("HC%04d%06d", timestamp, Math.abs(randomPart));
    }

    private Integer calculateWaitingNumber(Ticket ticket) {
        return ticketRepository.countByScheduleSlot_IdAndStatus(ticket.getScheduleSlot().getId(), TicketStatus.CONFIRMED) + 1;
    }
}
package vn.edu.hcmute.utecare.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.utecare.dto.request.CreateAppointmentRequest;
import vn.edu.hcmute.utecare.dto.response.AppointmentDetailResponse;
import vn.edu.hcmute.utecare.dto.response.AppointmentSummaryResponse;
import vn.edu.hcmute.utecare.dto.response.DoctorAppointmentResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.exception.ConflictException;
import vn.edu.hcmute.utecare.exception.DuplicatedException;
import vn.edu.hcmute.utecare.exception.ResourceNotFoundException;
import vn.edu.hcmute.utecare.mapper.AppointmentMapper;
import vn.edu.hcmute.utecare.mapper.AppointmentScheduleMapper;
import vn.edu.hcmute.utecare.model.*;
import vn.edu.hcmute.utecare.repository.*;
import vn.edu.hcmute.utecare.repository.specification.AppointmentSpecificationsBuilder;
import vn.edu.hcmute.utecare.service.AppointmentService;
import vn.edu.hcmute.utecare.util.PaginationUtil;
import vn.edu.hcmute.utecare.util.enumeration.AppointmentStatus;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Pattern;

import static vn.edu.hcmute.utecare.util.AppConst.SEARCH_SPEC_OPERATOR;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentServiceImpl implements AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final SearchRepository searchRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final ScheduleRepository scheduleRepository;
    private final AppointmentScheduleRepository appointmentScheduleRepository;

    @Override
    @Transactional
    public AppointmentDetailResponse createAppointment(CreateAppointmentRequest request) {
        log.info("Creating appointment with request: {}", request);
        MedicalRecord medicalRecord = medicalRecordRepository.findById(request.getMedicalRecordId())
                .orElseThrow(() -> new ResourceNotFoundException("Medical record not found"));

        if (request.getScheduleIds().isEmpty()) {
            throw new IllegalArgumentException("At least one doctor schedule ID is required");
        }

        Set<Long> scheduleIds = new HashSet<>(request.getScheduleIds());
        if (scheduleIds.size() != request.getScheduleIds().size()) {
            throw new IllegalArgumentException("Duplicate schedule IDs are not allowed");
        }

        List<Schedule> schedules = scheduleRepository.findAllByIdIn(request.getScheduleIds());

        for (Schedule schedule : schedules) {
            if (schedule.getBookedSlots() >= schedule.getMaxSlots()) {
                throw new ConflictException("Schedule " + schedule.getId() + " is fully booked");
            }
        }

        checkDistinctSpecialties(schedules);

        checkExistingAppointments(medicalRecord.getId(), request.getScheduleIds());

        checkDuplicateTimeSlots(schedules);

        Appointment appointment = Appointment.builder()
                .medicalRecord(medicalRecord)
                .build();

        List<AppointmentSchedule> appointmentSchedules = new ArrayList<>();
        for (Schedule schedule : schedules) {
            AppointmentSchedule appointmentSchedule = AppointmentSchedule.builder()
                    .appointment(appointment)
                    .schedule(schedule)
                    .status(AppointmentStatus.PENDING)
                    .waitingNumber(calculateWaitingNumber(schedule.getId()))
                    .ticketCode(generateTicketCode())
                    .build();
            appointmentSchedules.add(appointmentSchedule);

            // Cập nhật booked_slots
            schedule.setBookedSlots(schedule.getBookedSlots() + 1);
        }
        appointment.setSchedules(appointmentSchedules);
        scheduleRepository.saveAll(schedules);

        return AppointmentMapper.INSTANCE.toDetailResponse(appointmentRepository.save(appointment));
    }

    private String generateTicketCode() {
        SecureRandom random = new SecureRandom();
        long timestamp = System.currentTimeMillis() % 10_000; // 4 chữ số từ timestamp
        long randomPart = random.nextLong() % 1_000_000; // 6 chữ số ngẫu nhiên
        return String.format("HC%04d%06d", timestamp, Math.abs(randomPart));
    }

    private Integer calculateWaitingNumber(Long scheduleId) {
        return appointmentScheduleRepository.countByScheduleId(scheduleId) + 1;
    }

    private void checkDuplicateTimeSlots(List<Schedule> schedules) {
        Set<String> timeSlotKeys = new HashSet<>();
        for (Schedule schedule : schedules) {
            String timeSlotKey = schedule.getDate() + "|"
                    + schedule.getTimeSlot().getStartTime() + "|"
                    + schedule.getTimeSlot().getEndTime();
            if (!timeSlotKeys.add(timeSlotKey)) {
                throw new ConflictException("Duplicate time slots are not allowed");
            }
        }
    }

    private void checkExistingAppointments(Long medicalRecordId, List<Long> scheduleIds) {
        List<AppointmentSchedule> existingAppointments = appointmentScheduleRepository
                .findByAppointmentMedicalRecordIdAndScheduleIdIn(medicalRecordId, scheduleIds);
        if (!existingAppointments.isEmpty()) {
            throw new ConflictException("Patient already has an appointment in one of the selected time slots");
        }
    }

    private void checkDistinctSpecialties(List<Schedule> schedules){
        Set<Integer> specialtyIds = new HashSet<>();
        for (Schedule schedule : schedules) {
            Integer specialtyId = schedule.getDoctor().getMedicalSpecialty().getId();
            if (!specialtyIds.add(specialtyId)) {
                throw new DuplicatedException("Duplicate medical specialties are not allowed");
            }
        }
    }

    @Override
    public AppointmentDetailResponse getAppointmentById(Long id) {
        log.info("Fetching appointment with ID: {}", id);
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));
        return AppointmentMapper.INSTANCE.toDetailResponse(appointment);
    }

    @Override
    public AppointmentDetailResponse updateAppointmentStatus(Long id, AppointmentStatus status) {
        log.info("Updating appointment with ID: {}", id);

        AppointmentSchedule appointmentSchedule = appointmentScheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        if (appointmentSchedule.getStatus() == AppointmentStatus.CANCELLED) {
            throw new ConflictException("Cannot update a cancelled appointment");
        }
        if (appointmentSchedule.getStatus() == AppointmentStatus.COMPLETED) {
            throw new ConflictException("Cannot update a completed appointment");
        }
        appointmentSchedule.setStatus(status);
        appointmentScheduleRepository.save(appointmentSchedule);


        return AppointmentMapper.INSTANCE.toDetailResponse(appointmentSchedule.getAppointment());
    }

    @Override
    public PageResponse<AppointmentSummaryResponse> getAllAppointments(Long medicalRecordId, int page, int size, String sort, String direction) {
        log.info("Fetching appointments with medical record ID: {}, page: {}, size: {}, sort: {}, direction: {}", medicalRecordId, page, size, sort, direction);

        MedicalRecord medicalRecord = medicalRecordRepository.findById(medicalRecordId)
                .orElseThrow(() -> new ResourceNotFoundException("Medical record not found"));

        Pageable pageable = PaginationUtil.createPageable(page, size, sort, direction);

        Page<Appointment> appointmentPage = appointmentRepository.findAppointmentsWithDetails(medicalRecord, pageable);


        return PageResponse.<AppointmentSummaryResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(appointmentPage.getTotalPages())
                .totalElements(appointmentPage.getTotalElements())
                .content(appointmentPage.getContent().stream().map(AppointmentMapper.INSTANCE::toSummaryResponse).toList())
                .build();
    }

    @Override
    public PageResponse<DoctorAppointmentResponse> getAllAppointments(Long doctorId, int page, int size, String sort, String direction, LocalDate date, AppointmentStatus status, Integer timeSlotId) {
        log.info("Fetching appointments with page: {}, size: {}, sortBy: {}, direction: {}", page, size, sort, direction);

        Pageable pageable = PaginationUtil.createPageable(page, size, sort, direction);

        Page<AppointmentSchedule> appointmentPage = appointmentScheduleRepository.findAllByDoctorIdAndStatusAndTimeSlotId(doctorId, date, status, timeSlotId, pageable);

        return PageResponse.<DoctorAppointmentResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(appointmentPage.getTotalPages())
                .totalElements(appointmentPage.getTotalElements())
                .content(appointmentPage.getContent().stream().map(AppointmentScheduleMapper.INSTANCE::toDoctorAppointmentResponse).toList())
                .build();
    }


    @Override
    public PageResponse<AppointmentSummaryResponse> getAllAppointments(int page, int size, String sort, String direction, LocalDate startDate, LocalDate endDate) {
        log.info("Fetching appointments with page: {}, size: {}, sortBy: {}, direction: {}", page, size, sort, direction);

        Pageable pageable = PaginationUtil.createPageable(page, size, sort, direction);

        Page<Appointment> appointmentPage = appointmentRepository.findAppointmentsWithDetails(startDate, endDate, pageable);

        return PageResponse.<AppointmentSummaryResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(appointmentPage.getTotalPages())
                .totalElements(appointmentPage.getTotalElements())
                .content(appointmentPage.getContent().stream().map(AppointmentMapper.INSTANCE::toSummaryResponse).toList())
                .build();
    }

    @Override
    public PageResponse<AppointmentSummaryResponse> getAllAppointments(Pageable pageable, String[] appointment, String[] medicalRecord, String[] patient) {
        log.info("Fetching appointments with criteria: appointment: {}, medicalRecord: {}, patient: {}", appointment, medicalRecord, patient);

        if (appointment != null || medicalRecord != null || patient != null) {
            if (medicalRecord != null || patient != null) {
                // Use Criteria API for joins
                 PageResponse<Appointment> appointmentPage = searchRepository.searchAppointmentByCriteria(pageable, appointment, medicalRecord, patient);
                return PageResponse.<AppointmentSummaryResponse>builder()
                        .currentPage(pageable.getPageNumber() + 1)
                        .pageSize(pageable.getPageSize())
                        .totalPages(appointmentPage.getTotalPages())
                        .totalElements(appointmentPage.getTotalElements())
                        .content(appointmentPage.getContent().stream().map(AppointmentMapper.INSTANCE::toSummaryResponse).toList())
                        .build();
            } else {
                // Use Specifications for simple Appointment search
                AppointmentSpecificationsBuilder builder = new AppointmentSpecificationsBuilder();
                Pattern pattern = Pattern.compile(SEARCH_SPEC_OPERATOR);

                if (appointment != null) {
                    for (String a : appointment) {
                        builder.with(a);
                    }
                }

                Page<Appointment> appointments = appointmentRepository.findAll(builder.build(), pageable);
                return PageResponse.<AppointmentSummaryResponse>builder()
                        .currentPage(pageable.getPageNumber() + 1)
                        .pageSize(pageable.getPageSize())
                        .totalPages(appointments.getTotalPages())
                        .totalElements(appointments.getTotalElements())
                        .content(appointments.getContent().stream().map(AppointmentMapper.INSTANCE::toSummaryResponse).toList())
                        .build();
            }
        }
        Page<Appointment> appointments = appointmentRepository.findAll(pageable);
        return PageResponse.<AppointmentSummaryResponse>builder()
                .currentPage(pageable.getPageNumber() + 1)
                .pageSize(pageable.getPageSize())
                .totalPages(appointments.getTotalPages())
                .totalElements(appointments.getTotalElements())
                .content(appointments.getContent().stream().map(AppointmentMapper.INSTANCE::toSummaryResponse).toList())
                .build();
    }


}

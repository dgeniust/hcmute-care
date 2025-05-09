package vn.edu.hcmute.utecare.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.utecare.dto.request.ScheduleRequest;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.dto.response.ScheduleInfoResponse;
import vn.edu.hcmute.utecare.dto.response.ScheduleResponse;
import vn.edu.hcmute.utecare.exception.ConflictException;
import vn.edu.hcmute.utecare.exception.ResourceNotFoundException;
import vn.edu.hcmute.utecare.mapper.ScheduleMapper;
import vn.edu.hcmute.utecare.model.*;
import vn.edu.hcmute.utecare.repository.*;
import vn.edu.hcmute.utecare.service.ScheduleService;
import vn.edu.hcmute.utecare.util.PaginationUtil;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final DoctorRepository doctorRepository;
    private final RoomDetailRepository roomDetailRepository;
    private final ScheduleMapper scheduleMapper;


    @Override
    @Transactional
    public ScheduleResponse createSchedule(ScheduleRequest request) {
        log.info("Creating schedule with request: {}", request);

        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with ID: " + request.getDoctorId()));

        RoomDetail roomDetail = roomDetailRepository.findById(request.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Room detail not found with ID: " + request.getRoomId()));

        List<TimeSlot> timeSlots = timeSlotRepository.findAllById(request.getTimeSlotIds());

        if (timeSlots.size() != request.getTimeSlotIds().size()) {
            throw new ResourceNotFoundException("Some time slots not found");
        }

        if(scheduleRepository.existsByDoctorIdAndDate(request.getDoctorId(), request.getDate())) {
            throw new ConflictException("Schedule conflict for doctor ID: " + request.getDoctorId() + " on date: " + request.getDate());
        }

        Schedule schedule = scheduleMapper.toEntity(request);

        schedule.setDoctor(doctor);
        schedule.setRoomDetail(roomDetail);

        Set<ScheduleSlot> scheduleSlots = new HashSet<>();
        for (TimeSlot timeSlot : timeSlots) {
            ScheduleSlot scheduleSlot = new ScheduleSlot();
            scheduleSlot.setTimeSlot(timeSlot);
            scheduleSlot.setSchedule(schedule);
            scheduleSlots.add(scheduleSlot);
        }

        schedule.setScheduleSlots(scheduleSlots);

        return scheduleMapper.toResponse(scheduleRepository.save(schedule));
    }

    @Transactional
    @Override
    public ScheduleResponse updateSchedule(Long id, ScheduleRequest request) {
        log.info("Updating schedule with ID: {} and request: {}", id, request);

        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found with ID: " + id));

        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with ID: " + request.getDoctorId()));

        RoomDetail roomDetail = roomDetailRepository.findById(request.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Room detail not found with ID: " + request.getRoomId()));
        List<TimeSlot> timeSlots = timeSlotRepository.findAllById(request.getTimeSlotIds());
        if (timeSlots.size() != request.getTimeSlotIds().size()) {
            throw new ResourceNotFoundException("Some time slots not found");
        }

        Set<ScheduleSlot> scheduleSlots = new HashSet<>();
        for (TimeSlot timeSlot : timeSlots) {
            ScheduleSlot scheduleSlot = new ScheduleSlot();
            scheduleSlot.setTimeSlot(timeSlot);
            scheduleSlot.setSchedule(schedule);
            scheduleSlots.add(scheduleSlot);
        }
        schedule.setDoctor(doctor);
        schedule.setRoomDetail(roomDetail);
        schedule.setDate(request.getDate());
        schedule.setMaxSlots(request.getMaxSlots());
        schedule.setScheduleSlots(scheduleSlots);
        return scheduleMapper.toResponse(scheduleRepository.save(schedule));
    }

    @Transactional
    @Override
    public void deleteSchedule(Long id) {
        log.info("Deleting schedule with ID: {}", id);
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found with ID: " + id));
        scheduleRepository.delete(schedule);
    }

    @Transactional(readOnly = true)
    @Override
    public ScheduleResponse getScheduleById(Long id) {
        log.info("Getting schedule with ID: {}", id);
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found with ID: " + id));
        return scheduleMapper.toResponse(schedule);
    }

    @Transactional(readOnly = true)
    @Override
    public PageResponse<ScheduleResponse> getAllSchedules(
            Long doctorId,
            LocalDate startDate,
            LocalDate endDate,
            Integer page, Integer size, String sort, String direction) {
        log.info("Getting all schedules with doctorId: {}, startDate: {}, endDate: {}, page: {}, size: {}, sort: {}, direction: {}",
                doctorId, startDate, endDate, page, size, sort, direction);
        Pageable pageable = PaginationUtil.createPageable(page, size, sort, direction);

        Page<Schedule> schedulePage = scheduleRepository.findAllByDoctorIdAndDateBetween(
                doctorId, startDate, endDate, pageable);

        return PageResponse.<ScheduleResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalElements(schedulePage.getTotalElements())
                .totalPages(schedulePage.getTotalPages())
                .content(schedulePage.getContent().stream()
                        .map(scheduleMapper::toResponse)
                        .toList())
                .build();
    }

    @Transactional(readOnly = true)
    @Override
    public List<ScheduleInfoResponse> getAvailableSchedules(
            Integer medicalSpecialtyId,
            LocalDate date
    ) {
        log.info("Getting available schedules with medicalSpecialtyId: {}, date: {}", medicalSpecialtyId, date);

        List<Schedule> schedules = scheduleRepository.findAllByDoctor_MedicalSpecialty_IdAndDate(medicalSpecialtyId, date);

        return schedules.stream().map(scheduleMapper::toInfoResponse).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public PageResponse<ScheduleResponse> getDoctorSchedules(
            Long doctorId,
            LocalDate startDate,
            LocalDate endDate,
            Integer page, Integer size, String sort, String direction) {
        log.info("Getting schedules for doctor with ID: {} between {} and {}", doctorId, startDate, endDate);
        Pageable pageable = PaginationUtil.createPageable(page, size, sort, direction);
        Page<Schedule> schedulePage = scheduleRepository.findAllByDoctorIdAndDateBetween(doctorId, startDate, endDate, pageable);
        return PageResponse.<ScheduleResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalElements(schedulePage.getTotalElements())
                .totalPages(schedulePage.getTotalPages())
                .content(schedulePage.getContent().stream()
                        .map(scheduleMapper::toResponse)
                        .toList())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public ScheduleResponse getDoctorSchedule(Long id, LocalDate date) {
        log.info("Getting schedule for doctor with ID: {} on date: {}", id, date);
        Schedule schedule = scheduleRepository.findByDoctor_IdAndDate(id, date)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found for doctor ID: " + id + " on date: " + date));
        return scheduleMapper.toResponse(schedule);
    }
}

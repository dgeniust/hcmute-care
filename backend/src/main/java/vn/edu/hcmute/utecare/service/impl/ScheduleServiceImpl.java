package vn.edu.hcmute.utecare.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.edu.hcmute.utecare.dto.request.ScheduleRequest;
import vn.edu.hcmute.utecare.dto.response.ScheduleResponse;
import vn.edu.hcmute.utecare.dto.response.ScheduleSummaryResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.exception.ResourceNotFoundException;
import vn.edu.hcmute.utecare.mapper.ScheduleMapper;
import vn.edu.hcmute.utecare.model.Doctor;
import vn.edu.hcmute.utecare.model.Schedule;
import vn.edu.hcmute.utecare.model.RoomDetail;
import vn.edu.hcmute.utecare.model.TimeSlot;
import vn.edu.hcmute.utecare.repository.DoctorRepository;
import vn.edu.hcmute.utecare.repository.ScheduleRepository;
import vn.edu.hcmute.utecare.repository.RoomDetailRepository;
import vn.edu.hcmute.utecare.repository.TimeSlotRepository;
import vn.edu.hcmute.utecare.service.ScheduleService;
import vn.edu.hcmute.utecare.util.PaginationUtil;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final DoctorRepository doctorRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final RoomDetailRepository roomDetailRepository;

    @Transactional(rollbackOn = Exception.class)
    @Override
    public ScheduleSummaryResponse createDoctorSchedule(ScheduleRequest request) {
        log.info("Creating doctor schedule with request: {}", request);

        if (scheduleRepository.existsByDoctor_IdAndDateAndTimeSlot_Id(
                request.getDoctorId(), request.getDate(), request.getTimeSlotId())) {
            throw new IllegalArgumentException("Schedule already exists for this doctor, date, and time slot");
        }

        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with ID: " + request.getDoctorId()));
        TimeSlot timeSlot = timeSlotRepository.findById(request.getTimeSlotId())
                .orElseThrow(() -> new ResourceNotFoundException("TimeSlot not found with ID: " + request.getTimeSlotId()));
        RoomDetail roomDetail = roomDetailRepository.findById(request.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("RoomDetail not found with ID: " + request.getRoomId()));



        if (request.getDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Schedule date cannot be in the past");
        }

        Schedule schedule = ScheduleMapper.INSTANCE.toEntity(request);
        schedule.setDoctor(doctor);
        schedule.setTimeSlot(timeSlot);
        schedule.setRoomDetail(roomDetail);

        return ScheduleMapper.INSTANCE.toSummaryResponse(scheduleRepository.save(schedule));
    }

    @Override
    public ScheduleResponse getDoctorScheduleById(Long id) {
        log.info("Getting doctor schedule by id: {}", id);
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor schedule not found with ID: " + id));
         return ScheduleMapper.INSTANCE.toResponse(schedule);
    }

    @Transactional
    @Override
    public ScheduleSummaryResponse updateDoctorSchedule(Long id, ScheduleRequest request) {
        log.info("Updating doctor schedule with id: {} and request: {}", id, request);
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor schedule not found with ID: " + id));

        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with ID: " + request.getDoctorId()));
        TimeSlot timeSlot = timeSlotRepository.findById(request.getTimeSlotId())
                .orElseThrow(() -> new ResourceNotFoundException("TimeSlot not found with ID: " + request.getTimeSlotId()));

        if (!schedule.getDoctor().getId().equals(request.getDoctorId()) ||
                !schedule.getDate().equals(request.getDate()) ||
                !schedule.getTimeSlot().getId().equals(request.getTimeSlotId())) {
            if (scheduleRepository.existsByDoctor_IdAndDateAndTimeSlot_Id(
                    request.getDoctorId(), request.getDate(), request.getTimeSlotId())) {
                throw new IllegalArgumentException("Schedule already exists for this doctor, date, and time slot");
            }
        }

        if (request.getDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Schedule date cannot be in the past");
        }

        ScheduleMapper.INSTANCE.updateEntity(request, schedule);
        schedule.setDoctor(doctor);
        schedule.setTimeSlot(timeSlot);

        return ScheduleMapper.INSTANCE.toSummaryResponse(scheduleRepository.save(schedule));
    }

    @Transactional
    @Override
    public void deleteDoctorSchedule(Long id) {
        log.info("Deleting doctor schedule with id: {}", id);
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor schedule not found with ID: " + id));
        scheduleRepository.delete(schedule);
    }

    @Override
    public PageResponse<ScheduleSummaryResponse> getAllDoctorSchedules(int page, int size, String sort, String direction) {
        log.info("Fetching all doctor schedules with pagination: page={}, size={}, sort={}, direction={}", page, size, sort, direction);
        Pageable pageable = PaginationUtil.createPageable(page, size, sort, direction);

        Page<Schedule> schedulePage = scheduleRepository.findAll(pageable);
        return PageResponse.<ScheduleSummaryResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(schedulePage.getTotalPages())
                .totalElements(schedulePage.getTotalElements())
                .content(schedulePage.getContent().stream().map(ScheduleMapper.INSTANCE::toSummaryResponse).toList())
                .build();
    }

    @Override
    public PageResponse<ScheduleSummaryResponse> searchDoctorSchedules(Long doctorId, LocalDate date, Integer timeSlotId, int page, int size, String sort, String direction) {
        log.info("Searching doctor schedules with doctorId: {}, date: {}, timeSlotId: {}, page={}, size={}, sort={}, direction={}",
                doctorId, date, timeSlotId, page, size, sort, direction);
        Pageable pageable = PaginationUtil.createPageable(page, size, sort, direction);

        Page<Schedule> schedulePage = scheduleRepository.searchDoctorSchedules(doctorId, date, timeSlotId, pageable);
        return PageResponse.<ScheduleSummaryResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(schedulePage.getTotalPages())
                .totalElements(schedulePage.getTotalElements())
                .content(schedulePage.getContent().stream().map(ScheduleMapper.INSTANCE::toSummaryResponse).toList())
                .build();
    }
}

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
        log.info("Tạo lịch khám mới với thông tin: {}", request);

        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bác sĩ với ID: " + request.getDoctorId()));

        RoomDetail roomDetail = roomDetailRepository.findById(request.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phòng với ID: " + request.getRoomId()));

        List<TimeSlot> timeSlots = timeSlotRepository.findAllById(request.getTimeSlotIds());
        if (timeSlots.size() != request.getTimeSlotIds().size()) {
            throw new ResourceNotFoundException("Không tìm thấy một số khung giờ");
        }

        if (scheduleRepository.existsByDoctorIdAndDate(request.getDoctorId(), request.getDate())) {
            throw new ConflictException("Lịch khám trùng cho bác sĩ với ID: " + request.getDoctorId() + " vào ngày: " + request.getDate());
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
        Schedule savedSchedule = scheduleRepository.save(schedule);
        log.info("Tạo lịch khám thành công với ID: {}", savedSchedule.getId());
        return scheduleMapper.toResponse(savedSchedule);
    }

    @Override
    @Transactional
    public ScheduleResponse updateSchedule(Long id, ScheduleRequest request) {
        log.info("Cập nhật lịch khám với ID: {} và thông tin: {}", id, request);

        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy lịch khám với ID: " + id));

        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bác sĩ với ID: " + request.getDoctorId()));

        RoomDetail roomDetail = roomDetailRepository.findById(request.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phòng với ID: " + request.getRoomId()));

        List<TimeSlot> timeSlots = timeSlotRepository.findAllById(request.getTimeSlotIds());
        if (timeSlots.size() != request.getTimeSlotIds().size()) {
            throw new ResourceNotFoundException("Không tìm thấy một số khung giờ");
        }

        // Kiểm tra xung đột lịch khám (trừ lịch hiện tại)
        if (scheduleRepository.existsByDoctorIdAndDateAndIdNot(request.getDoctorId(), request.getDate(), id)) {
            throw new ConflictException("Lịch khám trùng cho bác sĩ với ID: " + request.getDoctorId() + " vào ngày: " + request.getDate());
        }

        schedule.setDoctor(doctor);
        schedule.setRoomDetail(roomDetail);
        schedule.setDate(request.getDate());
        schedule.setMaxSlots(request.getMaxSlots());

        Set<ScheduleSlot> scheduleSlots = new HashSet<>();
        for (TimeSlot timeSlot : timeSlots) {
            ScheduleSlot scheduleSlot = new ScheduleSlot();
            scheduleSlot.setTimeSlot(timeSlot);
            scheduleSlot.setSchedule(schedule);
            scheduleSlots.add(scheduleSlot);
        }
        schedule.setScheduleSlots(scheduleSlots);

        Schedule updatedSchedule = scheduleRepository.save(schedule);
        log.info("Cập nhật lịch khám thành công với ID: {}", id);
        return scheduleMapper.toResponse(updatedSchedule);
    }

    @Override
    @Transactional
    public void deleteSchedule(Long id) {
        log.info("Xóa lịch khám với ID: {}", id);
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy lịch khám với ID: " + id));
        scheduleRepository.delete(schedule);
        log.info("Xóa lịch khám thành công với ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public ScheduleResponse getScheduleById(Long id) {
        log.info("Truy xuất lịch khám với ID: {}", id);
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy lịch khám với ID: " + id));
        log.info("Truy xuất lịch khám thành công với ID: {}", id);
        return scheduleMapper.toResponse(schedule);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ScheduleResponse> getAllSchedules(
            Long doctorId, LocalDate startDate, LocalDate endDate, Integer page, Integer size, String sort, String direction) {
        log.info("Truy xuất danh sách lịch khám: bác sĩ={}, ngày bắt đầu={}, ngày kết thúc={}, trang={}, kích thước={}, sắp xếp={}, hướng={}",
                doctorId, startDate, endDate, page, size, sort, direction);
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
    public List<ScheduleInfoResponse> getAvailableSchedules(Integer medicalSpecialtyId, LocalDate date) {
        log.info("Truy xuất danh sách lịch khám trống: chuyên khoa={}, ngày={}", medicalSpecialtyId, date);
        List<Schedule> schedules = scheduleRepository.findAllByDoctor_MedicalSpecialty_IdAndDate(medicalSpecialtyId, date);
        return schedules.stream().map(scheduleMapper::toInfoResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ScheduleResponse> getDoctorSchedules(
            Long doctorId, LocalDate startDate, LocalDate endDate, Integer page, Integer size, String sort, String direction) {
        log.info("Truy xuất lịch khám của bác sĩ với ID: {} từ {} đến {}", doctorId, startDate, endDate);
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
        log.info("Truy xuất lịch khám của bác sĩ với ID: {} vào ngày: {}", id, date);
        Schedule schedule = scheduleRepository.findByDoctor_IdAndDate(id, date)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy lịch khám cho bác sĩ với ID: " + id + " vào ngày: " + date));
        log.info("Truy xuất lịch khám thành công cho bác sĩ với ID: {}", id);
        return scheduleMapper.toResponse(schedule);
    }
}
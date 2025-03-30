package vn.edu.hcmute.utecare.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.edu.hcmute.utecare.dto.request.DoctorScheduleRequest;
import vn.edu.hcmute.utecare.dto.response.DoctorScheduleResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.exception.ResourceNotFoundException;
import vn.edu.hcmute.utecare.mapper.DoctorScheduleMapper;
import vn.edu.hcmute.utecare.model.Doctor;
import vn.edu.hcmute.utecare.model.DoctorSchedule;
import vn.edu.hcmute.utecare.model.TimeSlot;
import vn.edu.hcmute.utecare.repository.DoctorRepository;
import vn.edu.hcmute.utecare.repository.DoctorScheduleRepository;
import vn.edu.hcmute.utecare.repository.TimeSlotRepository;
import vn.edu.hcmute.utecare.service.DoctorScheduleService;
import vn.edu.hcmute.utecare.util.PaginationUtil;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class DoctorScheduleServiceImpl implements DoctorScheduleService {

    private final DoctorScheduleRepository doctorScheduleRepository;
    private final DoctorRepository doctorRepository;
    private final TimeSlotRepository timeSlotRepository;

    @Transactional(rollbackOn = Exception.class)
    @Override
    public DoctorScheduleResponse createDoctorSchedule(DoctorScheduleRequest request) {
        log.info("Creating doctor schedule with request: {}", request);

        if (doctorScheduleRepository.existsByDoctor_IdAndDateAndTimeSlot_Id(
                request.getDoctorId(), request.getDate(), request.getTimeSlotId())) {
            throw new IllegalArgumentException("Schedule already exists for this doctor, date, and time slot");
        }

        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with ID: " + request.getDoctorId()));
        TimeSlot timeSlot = timeSlotRepository.findById(request.getTimeSlotId())
                .orElseThrow(() -> new ResourceNotFoundException("TimeSlot not found with ID: " + request.getTimeSlotId()));

        if (request.getDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Schedule date cannot be in the past");
        }

        DoctorSchedule doctorSchedule = DoctorScheduleMapper.INSTANCE.toEntity(request);
        doctorSchedule.setDoctor(doctor);
        doctorSchedule.setTimeSlot(timeSlot);

        return DoctorScheduleMapper.INSTANCE.toResponse(doctorScheduleRepository.save(doctorSchedule));
    }

    @Override
    public DoctorScheduleResponse getDoctorScheduleById(Long id) {
        log.info("Getting doctor schedule by id: {}", id);
        DoctorSchedule doctorSchedule = doctorScheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor schedule not found with ID: " + id));
         return DoctorScheduleMapper.INSTANCE.toResponse(doctorSchedule);
    }

    @Transactional
    @Override
    public DoctorScheduleResponse updateDoctorSchedule(Long id, DoctorScheduleRequest request) {
        log.info("Updating doctor schedule with id: {} and request: {}", id, request);
        DoctorSchedule doctorSchedule = doctorScheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor schedule not found with ID: " + id));

        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with ID: " + request.getDoctorId()));
        TimeSlot timeSlot = timeSlotRepository.findById(request.getTimeSlotId())
                .orElseThrow(() -> new ResourceNotFoundException("TimeSlot not found with ID: " + request.getTimeSlotId()));

        if (!doctorSchedule.getDoctor().getId().equals(request.getDoctorId()) ||
                !doctorSchedule.getDate().equals(request.getDate()) ||
                !doctorSchedule.getTimeSlot().getId().equals(request.getTimeSlotId())) {
            if (doctorScheduleRepository.existsByDoctor_IdAndDateAndTimeSlot_Id(
                    request.getDoctorId(), request.getDate(), request.getTimeSlotId())) {
                throw new IllegalArgumentException("Schedule already exists for this doctor, date, and time slot");
            }
        }

        if (request.getDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Schedule date cannot be in the past");
        }

        DoctorScheduleMapper.INSTANCE.updateEntity(request, doctorSchedule);
        doctorSchedule.setDoctor(doctor);
        doctorSchedule.setTimeSlot(timeSlot);

        return DoctorScheduleMapper.INSTANCE.toResponse(doctorScheduleRepository.save(doctorSchedule));
    }

    @Transactional
    @Override
    public void deleteDoctorSchedule(Long id) {
        log.info("Deleting doctor schedule with id: {}", id);
        DoctorSchedule doctorSchedule = doctorScheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor schedule not found with ID: " + id));
        doctorScheduleRepository.delete(doctorSchedule);
    }

    @Override
    public PageResponse<DoctorScheduleResponse> getAllDoctorSchedules(int page, int size, String sort, String direction) {
        log.info("Fetching all doctor schedules with pagination: page={}, size={}, sort={}, direction={}", page, size, sort, direction);
        Pageable pageable = PaginationUtil.createPageable(page, size, sort, direction);

        Page<DoctorSchedule> schedulePage = doctorScheduleRepository.findAll(pageable);
        return PageResponse.<DoctorScheduleResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(schedulePage.getTotalPages())
                .totalElements(schedulePage.getTotalElements())
                .content(schedulePage.getContent().stream().map(DoctorScheduleMapper.INSTANCE::toResponse).toList())
                .build();
    }

    @Override
    public PageResponse<DoctorScheduleResponse> searchDoctorSchedules(Long doctorId, LocalDate date, Integer timeSlotId, int page, int size, String sort, String direction) {
        log.info("Searching doctor schedules with doctorId: {}, date: {}, timeSlotId: {}, page={}, size={}, sort={}, direction={}",
                doctorId, date, timeSlotId, page, size, sort, direction);
        Pageable pageable = PaginationUtil.createPageable(page, size, sort, direction);

        Page<DoctorSchedule> schedulePage = doctorScheduleRepository.searchDoctorSchedules(doctorId, date, timeSlotId, pageable);
        return PageResponse.<DoctorScheduleResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(schedulePage.getTotalPages())
                .totalElements(schedulePage.getTotalElements())
                .content(schedulePage.getContent().stream().map(DoctorScheduleMapper.INSTANCE::toResponse).toList())
                .build();
    }
}

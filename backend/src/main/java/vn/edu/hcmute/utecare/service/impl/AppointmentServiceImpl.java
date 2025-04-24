package vn.edu.hcmute.utecare.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.utecare.dto.request.CreateAppointmentRequest;
import vn.edu.hcmute.utecare.dto.response.AppointmentDetailResponse;
import vn.edu.hcmute.utecare.dto.response.AppointmentSummaryResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.exception.ResourceNotFoundException;
import vn.edu.hcmute.utecare.mapper.AppointmentMapper;
import vn.edu.hcmute.utecare.model.Appointment;
import vn.edu.hcmute.utecare.repository.AppointmentRepository;
import vn.edu.hcmute.utecare.repository.ScheduleRepository;
import vn.edu.hcmute.utecare.repository.MedicalRecordRepository;
import vn.edu.hcmute.utecare.repository.specification.AppointmentSpecificationsBuilder;
import vn.edu.hcmute.utecare.service.AppointmentService;
import vn.edu.hcmute.utecare.util.AppConst;
import vn.edu.hcmute.utecare.util.PaginationUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentServiceImpl implements AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final ScheduleRepository scheduleRepository;

    @Override
    @Transactional
    public AppointmentDetailResponse createAppointment(CreateAppointmentRequest request){
        log.info("Creating appointment with request: {}", request);

        return null;
    }

    @Override
    public AppointmentDetailResponse getAppointmentById(Long id) {
        log.info("Fetching appointment with ID: {}", id);
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));
        return AppointmentMapper.INSTANCE.toDetailResponse(appointment);
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
    public PageResponse<AppointmentSummaryResponse> getAllAppointments(int pageNo, int size, String search[], String sortBy){
        log.info("Fetching appointments with page: {}, size: {}, search: {}, sortBy: {}", pageNo, size, search, sortBy);

        int page = pageNo > 0 ? pageNo - 1 : 0;

        // Handle sorting
        List<Sort.Order> sorts = new ArrayList<>();
        if (sortBy != null && !sortBy.isEmpty()) {
            Pattern pattern = Pattern.compile(AppConst.SORT_BY);
            Matcher matcher = pattern.matcher(sortBy);
            if (matcher.find()) {
                String field = matcher.group(1);
                String direction = matcher.group(3);
                sorts.add(new Sort.Order(
                        direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, field));
            }
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by(sorts));

        // Handle search
        AppointmentSpecificationsBuilder builder = new AppointmentSpecificationsBuilder();
        if (search != null && search.length > 0) {
            Pattern pattern = Pattern.compile(AppConst.SEARCH_SPEC_OPERATOR);
            for (String s : search) {
                Matcher matcher = pattern.matcher(s);
                if (matcher.find()) {
                    builder.with(matcher.group(1), matcher.group(2), matcher.group(4), matcher.group(3), matcher.group(5));
                }
            }
        }

        Page<Appointment> appointments = appointmentRepository.findAll(Objects.requireNonNull(builder.build()), pageable);

        return PageResponse.<AppointmentSummaryResponse>builder()
                .currentPage(page + 1)
                .pageSize(size)
                .totalPages(appointments.getTotalPages())
                .totalElements(appointments.getTotalElements())
                .content(appointments.getContent().stream().map(AppointmentMapper.INSTANCE::toSummaryResponse).toList())
                .build();
    }
}

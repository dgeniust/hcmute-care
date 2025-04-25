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
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.exception.ResourceNotFoundException;
import vn.edu.hcmute.utecare.mapper.AppointmentMapper;
import vn.edu.hcmute.utecare.model.Appointment;
import vn.edu.hcmute.utecare.repository.AppointmentRepository;
import vn.edu.hcmute.utecare.repository.ScheduleRepository;
import vn.edu.hcmute.utecare.repository.MedicalRecordRepository;
import vn.edu.hcmute.utecare.repository.SearchRepository;
import vn.edu.hcmute.utecare.repository.specification.AppointmentSpecificationsBuilder;
import vn.edu.hcmute.utecare.service.AppointmentService;
import vn.edu.hcmute.utecare.util.AppConst;
import vn.edu.hcmute.utecare.util.PaginationUtil;

import java.time.LocalDate;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentServiceImpl implements AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final SearchRepository searchRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final ScheduleRepository scheduleRepository;

    @Override
    @Transactional
    public AppointmentDetailResponse createAppointment(CreateAppointmentRequest request) {
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
                Pattern pattern = Pattern.compile(AppConst.SEARCH_SPEC_OPERATOR);

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

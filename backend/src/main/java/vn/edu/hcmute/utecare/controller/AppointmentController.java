package vn.edu.hcmute.utecare.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmute.utecare.dto.request.CreateAppointmentRequest;
import vn.edu.hcmute.utecare.dto.response.AppointmentDetailResponse;
import vn.edu.hcmute.utecare.dto.response.AppointmentSummaryResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.dto.response.ResponseData;
import vn.edu.hcmute.utecare.mapper.AppointmentMapper;
import vn.edu.hcmute.utecare.service.AppointmentService;
import vn.edu.hcmute.utecare.util.enumeration.AppointmentStatus;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/appointments")
@RequiredArgsConstructor
@Tag(name = "Appointment", description = "APPOINTMENT API")
@Slf4j(topic = "APPOINTMENT_CONTROLLER")
public class AppointmentController {
    private final AppointmentService appointmentService;

    @PostMapping
    public ResponseData<AppointmentDetailResponse> createAppointment(@Valid @RequestBody CreateAppointmentRequest request) {
        log.info("Creating appointment with request: {}", request);
        return ResponseData.<AppointmentDetailResponse>builder()
                .status(HttpStatus.CREATED.value())
                .data(appointmentService.createAppointment(request))
                .message("Create appointment successfully")
                .build();
    }

    @PutMapping("/{id}/status")
    public ResponseData<AppointmentDetailResponse> updateAppointmentStatus(
            @PathVariable("id") Long id,
            @RequestParam AppointmentStatus status) {
        log.info("Updating appointment status for ID: {} to {}", id, status);
        return ResponseData.<AppointmentDetailResponse>builder()
                .status(HttpStatus.OK.value())
                .data(appointmentService.updateAppointmentStatus(id, status))
                .message("Update appointment status successfully")
                .build();
    }

    @GetMapping("/{id}")
    public ResponseData<AppointmentDetailResponse> getAppointmentById(@PathVariable("id") Long id) {
        log.info("Fetching appointment with ID: {}", id);
        return ResponseData.<AppointmentDetailResponse>builder()
                .status(HttpStatus.OK.value())
                .data(appointmentService.getAppointmentById(id))
                .message("Get appointment successfully")
                .build();
    }

    //for admin
    @GetMapping
    public ResponseData<PageResponse<AppointmentSummaryResponse>> getAppointments(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
            ) {
        log.info("Fetching appointments with page: {}, size: {}", page, size);
        return ResponseData.<PageResponse<AppointmentSummaryResponse>>builder()
                .status(HttpStatus.OK.value())
                .data(appointmentService.getAllAppointments(page, size, sort, direction, startDate, endDate))
                .message("Get appointments successfully")
                .build();
    }

    @GetMapping("/search")
    public ResponseData<PageResponse<AppointmentSummaryResponse>> searchAppointments(
            @RequestParam(value = "appointment", required = false) String[] appointment,
            @RequestParam(value = "medicalRecord", required = false) String[] medicalRecord,
            @RequestParam(value = "patient", required = false) String[] patient,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "id:asc") String sort
    ) {
        String[] sortParts = sort.split(":");
        String sortField = sortParts[0];
        Sort.Direction sortDirection = sortParts[1].equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(sortDirection, sortField));

        return ResponseData.<PageResponse<AppointmentSummaryResponse>>builder()
                .status(HttpStatus.OK.value())
                .data(appointmentService.getAllAppointments(pageable, appointment, medicalRecord, patient))
                .message("Search appointments successfully")
                .build();
    }

    @GetMapping("/test")
    public ResponseData<AppointmentDetailResponse> test(@RequestParam Long appointmentId) {
        return ResponseData.<AppointmentDetailResponse>builder()
                .status(HttpStatus.OK.value())
                .data(AppointmentMapper.INSTANCE.toDetailResponse(appointmentService.confirmAppointment(appointmentId)))
                .message("Test appointment API")
                .build();
    }
}

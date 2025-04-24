package vn.edu.hcmute.utecare.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmute.utecare.dto.request.CreateAppointmentRequest;
import vn.edu.hcmute.utecare.dto.response.AppointmentDetailResponse;
import vn.edu.hcmute.utecare.dto.response.AppointmentSummaryResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.dto.response.ResponseData;
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
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(required = false) String[] search
    ) {
        log.info("Searching appointments with page: {}, size: {}, sortBy: {}", page, size, sortBy);
        return ResponseData.<PageResponse<AppointmentSummaryResponse>>builder()
                .status(HttpStatus.OK.value())
                .data(appointmentService.getAllAppointments(page, size, search, sortBy))
                .message("Search appointments successfully")
                .build();
    }
}

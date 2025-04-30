package vn.edu.hcmute.utecare.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmute.utecare.dto.request.AppointmentRequest;
import vn.edu.hcmute.utecare.dto.response.AppointmentResponse;
import vn.edu.hcmute.utecare.dto.response.ResponseData;
import vn.edu.hcmute.utecare.service.AppointmentService;

@RestController
@RequestMapping("/api/v1/appointments")
@RequiredArgsConstructor
@Tag(name = "Appointment", description = "Appointment API")
@Slf4j(topic = "APPOINTMENT_CONTROLLER")
public class AppointmentController {
    private final AppointmentService appointmentService;

    @PostMapping
    @Operation(summary = "Create a new appointment", description = "Creates a new appointment along with its associated patient details")
    public ResponseData<AppointmentResponse> createAppointment(@RequestBody @Valid AppointmentRequest request) {
        log.info("Create appointment request: {}", request);
        return ResponseData.<AppointmentResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Appointment created successfully")
                .data(appointmentService.createAppointment(request))
                .build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get appointment by ID", description = "Retrieve an appointment by its ID")
    public ResponseData<AppointmentResponse> getAppointmentById(@PathVariable("id") Long id) {
        log.info("Get appointment request for ID: {}", id);
        return ResponseData.<AppointmentResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Appointment retrieved successfully")
                .data(appointmentService.getAppointmentById(id))
                .build();
    }
}

package vn.edu.hcmute.utecare.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.hcmute.utecare.dto.request.CreateAppointmentRequest;
import vn.edu.hcmute.utecare.dto.response.AppointmentDetailResponse;
import vn.edu.hcmute.utecare.dto.response.ResponseData;
import vn.edu.hcmute.utecare.service.AppointmentService;

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
}

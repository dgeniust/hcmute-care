package vn.edu.hcmute.utecare.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CreateAppointmentRequest {
    @NotNull(message = "Medical record ID cannot be null")
    private Long medicalRecordId;

    @NotNull(message = "Doctor schedule ID cannot be null")
    private Long doctorScheduleId;
}

package vn.edu.hcmute.utecare.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public class AppointmentRequest {
    @NotNull(message = "Medical record ID cannot be null")
    private Long medicalRecordId;

    @NotEmpty(message = "Schedule slot IDs cannot be empty")
    private List<Long> scheduleSlotIds;
}

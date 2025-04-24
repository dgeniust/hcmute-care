package vn.edu.hcmute.utecare.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import vn.edu.hcmute.utecare.util.enumeration.AppointmentStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@Builder
public class AppointmentSummaryResponse {
    private Long id;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private PatientInfoResponse patient;

    private List<AppointmentScheduleInfoResponse> schedules;
}

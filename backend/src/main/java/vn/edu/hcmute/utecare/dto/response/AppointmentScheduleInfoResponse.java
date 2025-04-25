package vn.edu.hcmute.utecare.dto.response;

import lombok.Getter;
import lombok.Setter;
import vn.edu.hcmute.utecare.util.enumeration.AppointmentStatus;

import java.time.LocalDate;

@Getter
@Setter
public class AppointmentScheduleInfoResponse {
    private Long scheduleId;

    private AppointmentStatus status;

    private String ticketCode;

    private LocalDate date;

    private DoctorInfoResponse doctor;
}
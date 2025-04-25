package vn.edu.hcmute.utecare.dto.response;

import lombok.Getter;
import lombok.Setter;
import vn.edu.hcmute.utecare.util.enumeration.AppointmentStatus;


@Getter
@Setter
public class AppointmentScheduleResponse {
    private ScheduleInfoResponse schedule;

    private AppointmentStatus status;

    private String ticketCode;

    private Integer waitingNumber;
}

package vn.edu.hcmute.utecare.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScheduleSlotInfoResponse {
    private Long id;

    private TimeSlotResponse timeSlot;

    private String doctorName;

    private String roomName;
}

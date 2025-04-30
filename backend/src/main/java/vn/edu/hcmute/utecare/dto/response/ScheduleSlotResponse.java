package vn.edu.hcmute.utecare.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScheduleSlotResponse {
    private Long id;

    private Integer bookedSlots;

    private TimeSlotResponse timeSlot;
}

package vn.edu.hcmute.utecare.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@Builder
public class TimeSlotResponse {
    private Integer id;
    private LocalTime startTime;
    private LocalTime endTime;
}

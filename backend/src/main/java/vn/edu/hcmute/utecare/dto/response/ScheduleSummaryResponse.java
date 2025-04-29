package vn.edu.hcmute.utecare.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import vn.edu.hcmute.utecare.util.enumeration.Gender;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Builder
public class ScheduleSummaryResponse {
    private Long id;

    private String doctorName;

    private Gender doctorGender;

    private String doctorQualification;

    private String roomName;

    private LocalDate date;

    private LocalTime startTime;

    private LocalTime endTime;

    private Integer bookedSlots;

    private Integer maxSlots;
}

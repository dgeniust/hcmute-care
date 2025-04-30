package vn.edu.hcmute.utecare.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Getter
public class ScheduleRequest {
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @FutureOrPresent
    private LocalDate date;

    @Min(value = 1, message = "maxSlots must be greater than or equal to 1")
    private Integer maxSlots;

    @NotNull(message = "roomId must not be null")
    private Integer roomId;

    @NotNull(message = "doctorId must not be null")
    private Long doctorId;

    @NotNull(message = "timeSlotId must not be null")
    private List<Integer> timeSlotIds;
}

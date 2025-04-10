package vn.edu.hcmute.utecare.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;
import vn.edu.hcmute.utecare.util.validator.ValidSlots;

import java.time.LocalDate;

@Getter
@ValidSlots
public class DoctorScheduleRequest {
    @NotNull(message = "doctorId must not be null")
    private Long doctorId;

    @NotNull(message = "roomId must not be null")
    private Integer roomId;

    @NotNull(message = "timeSlotId must not be null")
    private Integer timeSlotId;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @FutureOrPresent
    private LocalDate date;

    @Min(value = 1, message = "maxSlots must be greater than 0")
    private Integer maxSlots;

    @Min(value = 0, message = "bookedSlots must be greater than or equal to 0")
    private Integer bookedSlots;
}

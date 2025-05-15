package vn.edu.hcmute.utecare.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;

@Getter
public class TimeSlotRequest {
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    @Schema(type = "string", format = "time", example = "00:00:00", description = "Start time in HH:mm:ss format")
    private LocalTime startTime;

    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    @Schema(type = "string", format = "time", example = "00:00:00", description = "End time in HH:mm:ss format")
    private LocalTime endTime;
}

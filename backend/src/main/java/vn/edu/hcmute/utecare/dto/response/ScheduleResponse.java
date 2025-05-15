package vn.edu.hcmute.utecare.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
public class ScheduleResponse {
    private Long id;

    private LocalDate date;

    private Integer maxSlots;

    private DoctorInfoResponse doctor;

    private RoomDetailResponse roomDetail;

    private Set<ScheduleSlotResponse> scheduleSlots;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}

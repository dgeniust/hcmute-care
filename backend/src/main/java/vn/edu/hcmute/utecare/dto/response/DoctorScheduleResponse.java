package vn.edu.hcmute.utecare.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class DoctorScheduleResponse {
    private Long id;

    private DoctorResponse doctor;

    private TimeSlotResponse timeSlot;

    private RoomDetailResponse roomDetail;

    private LocalDate date;

    private Integer maxSlots;

    private Integer bookedSlots;
}

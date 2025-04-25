package vn.edu.hcmute.utecare.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ScheduleInfoResponse {
    private Long id;

    private DoctorInfoResponse doctor;

    private TimeSlotResponse timeSlot;

    private RoomDetailResponse roomDetail;

    private LocalDate date;
}

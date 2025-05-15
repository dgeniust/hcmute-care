package vn.edu.hcmute.utecare.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class TicketScheduleResponse {
    private Long id;

    private LocalDate date;

    private DoctorInfoResponse doctor;

    private RoomDetailResponse roomDetail;
}

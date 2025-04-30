package vn.edu.hcmute.utecare.dto.response;

import lombok.Getter;
import lombok.Setter;
import vn.edu.hcmute.utecare.util.enumeration.TicketStatus;

@Getter
@Setter
public class TicketResponse {
    private Long id;

    private String ticketCode;

    private TicketStatus status;

    private Integer waitingNumber;

    private ScheduleSlotInfoResponse scheduleSlot;
}

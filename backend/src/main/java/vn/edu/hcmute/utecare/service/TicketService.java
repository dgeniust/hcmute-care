package vn.edu.hcmute.utecare.service;

import vn.edu.hcmute.utecare.dto.response.DoctorTicketSummaryResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.dto.response.TicketResponse;
import vn.edu.hcmute.utecare.util.enumeration.TicketStatus;

import java.time.LocalDate;
import java.util.List;

public interface TicketService {
    List<DoctorTicketSummaryResponse> getTicketSummaryByScheduleSlot(Long scheduleSlotId,
                                                                     TicketStatus status);

    TicketResponse getTicketById(Long ticketId);

    TicketResponse updateTicketStatus(Long ticketId, TicketStatus status);

    PageResponse<TicketResponse> getAllTicketsByMedicalRecordId(Long medicalRecordId,
                                                                TicketStatus status,
                                                                int page,
                                                                int size,
                                                                String sort,
                                                                String direction);
}

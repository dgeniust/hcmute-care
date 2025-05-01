package vn.edu.hcmute.utecare.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmute.utecare.dto.response.DoctorTicketSummaryResponse;
import vn.edu.hcmute.utecare.dto.response.ResponseData;
import vn.edu.hcmute.utecare.service.TicketService;
import vn.edu.hcmute.utecare.util.enumeration.TicketStatus;

import java.util.List;

@RestController
@RequestMapping("/api/v1/schedule-slots")
@RequiredArgsConstructor
@Tag(name = "Schedule Slot", description = "Schedule Slot API")
@Slf4j(topic = "SCHEDULE_SLOT_CONTROLLER")
public class ScheduleSlotController {
    private final TicketService ticketService;

    @GetMapping("/{id}/tickets")
    @Operation(summary = "Get tickets by schedule slot ID", description = "Get tickets by schedule slot ID by doctor role")
    public ResponseData<List<DoctorTicketSummaryResponse>> getTicketsByScheduleSlotId(@PathVariable Long id,
                                                                                     @RequestParam(required = false) TicketStatus status) {
        log.info("Get tickets by schedule slot ID: {}, status: {}", id, status);
        return ResponseData.<List<DoctorTicketSummaryResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Tickets retrieved successfully")
                .data(ticketService.getTicketSummaryByScheduleSlot(id, status))
                .build();
    }

}

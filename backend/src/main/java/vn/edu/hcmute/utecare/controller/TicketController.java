package vn.edu.hcmute.utecare.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmute.utecare.dto.response.ResponseData;
import vn.edu.hcmute.utecare.dto.response.TicketResponse;
import vn.edu.hcmute.utecare.service.TicketService;
import vn.edu.hcmute.utecare.util.enumeration.TicketStatus;

@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
@Tag(name = "Ticket", description = "Ticket API")
@Slf4j(topic = "TICKET_CONTROLLER")
public class TicketController {
    private final TicketService ticketService;

    @GetMapping("/{id}")
    @Operation(summary = "Get ticket by ID", description = "Retrieve a ticket by its ID")
    public ResponseData<TicketResponse> getTicketById(@PathVariable Long id) {
        log.info("Get ticket by ID: {}", id);
        return ResponseData.<TicketResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Ticket retrieved successfully")
                .data(ticketService.getTicketById(id))
                .build();
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update ticket status", description = "Update the status of an existing ticket by its ID")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR')")
    public ResponseData<TicketResponse> updateTicketStatus(@PathVariable Long id, @RequestParam TicketStatus status) {
        log.info("Update ticket status for ID: {}, status: {}", id, status);
        return ResponseData.<TicketResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Ticket status updated successfully")
                .data(ticketService.updateTicketStatus(id, status))
                .build();
    }
}

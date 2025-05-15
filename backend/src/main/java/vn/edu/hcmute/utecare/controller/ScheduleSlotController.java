package vn.edu.hcmute.utecare.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmute.utecare.dto.response.DoctorTicketSummaryResponse;
import vn.edu.hcmute.utecare.dto.response.ResponseData;
import vn.edu.hcmute.utecare.service.TicketService;
import vn.edu.hcmute.utecare.util.enumeration.TicketStatus;

import java.util.List;

@RestController
@RequestMapping("/api/v1/schedule-slots")
@RequiredArgsConstructor
@Tag(name = "SCHEDULE-SLOT", description = "API quản lý các vé khám liên quan đến khung lịch khám")
@Slf4j(topic = "SCHEDULE_SLOT_CONTROLLER")
public class ScheduleSlotController {
    private final TicketService ticketService;

    @GetMapping("/{id}/tickets")
//    @PreAuthorize("hasRole('DOCTOR')")
    @Operation(
            summary = "Lấy danh sách vé khám theo khung lịch",
            description = "Truy xuất danh sách vé khám thuộc một khung lịch khám cụ thể dựa trên ID khung lịch và trạng thái (tùy chọn)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy danh sách vé khám thành công"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy khung lịch hoặc vé khám")
    })
    public ResponseData<List<DoctorTicketSummaryResponse>> getTicketsByScheduleSlotId(
            @Parameter(description = "ID của khung lịch khám") @PathVariable Long id,
            @Parameter(description = "Trạng thái của vé khám (tùy chọn)") @RequestParam(required = false) TicketStatus status) {
        log.info("Yêu cầu lấy danh sách vé khám cho khung lịch với ID: {}, trạng thái: {}", id, status);
        return ResponseData.<List<DoctorTicketSummaryResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy danh sách vé khám thành công")
                .data(ticketService.getTicketSummaryByScheduleSlot(id, status))
                .build();
    }
}
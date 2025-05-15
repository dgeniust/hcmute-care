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
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.dto.response.ResponseData;
import vn.edu.hcmute.utecare.dto.response.TicketResponse;
import vn.edu.hcmute.utecare.service.TicketService;
import vn.edu.hcmute.utecare.util.enumeration.TicketStatus;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
@Tag(name = "TICKET", description = "API quản lý thông tin vé khám của bệnh nhân")
@Slf4j(topic = "TICKET_CONTROLLER")
public class TicketController {
    private final TicketService ticketService;

    @GetMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE', 'CUSTOMER')")
    @Operation(
            summary = "Lấy thông tin vé khám theo ID",
            description = "Truy xuất thông tin chi tiết của một vé khám dựa trên ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy thông tin vé khám thành công"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy vé khám với ID được cung cấp")
    })
    public ResponseData<TicketResponse> getTicketById(
            @Parameter(description = "ID của vé khám cần truy xuất") @PathVariable Long id) {
        log.info("Yêu cầu lấy thông tin vé khám với ID: {}", id);
        return ResponseData.<TicketResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy thông tin vé khám thành công")
                .data(ticketService.getTicketById(id))
                .build();
    }

    @PatchMapping("/{id}/status")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR')")
    @Operation(
            summary = "Cập nhật trạng thái vé khám",
            description = "Cập nhật trạng thái của một vé khám hiện có dựa trên ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cập nhật trạng thái vé khám thành công"),
            @ApiResponse(responseCode = "400", description = "Trạng thái vé khám không hợp lệ"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy vé khám với ID được cung cấp")
    })
    public ResponseData<TicketResponse> updateTicketStatus(
            @Parameter(description = "ID của vé khám cần cập nhật") @PathVariable Long id,
            @Parameter(description = "Trạng thái mới của vé khám") @RequestParam TicketStatus status) {
        log.info("Yêu cầu cập nhật trạng thái vé khám với ID: {}, trạng thái: {}", id, status);
        return ResponseData.<TicketResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Cập nhật trạng thái vé khám thành công")
                .data(ticketService.updateTicketStatus(id, status))
                .build();
    }

    @GetMapping
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE', 'CUSTOMER')")
    @Operation(
            summary = "Lấy danh sách vé khám",
            description = "Truy xuất danh sách vé khám với phân trang, sắp xếp theo ngày lịch khám và trạng thái (tùy chọn)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy danh sách vé khám thành công"),
            @ApiResponse(responseCode = "400", description = "Tham số phân trang, sắp xếp hoặc lọc không hợp lệ"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập")
    })
    public ResponseData<PageResponse<TicketResponse>> getAllTickets(
            @Parameter(description = "Số trang, bắt đầu từ 1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Số lượng bản ghi mỗi trang") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Trường để sắp xếp (mặc định: scheduleSlot.schedule.date)") @RequestParam(defaultValue = "scheduleSlot.schedule.date") String sort,
            @Parameter(description = "Hướng sắp xếp: asc hoặc desc") @RequestParam(defaultValue = "asc") String direction,
            @Parameter(description = "Ngày lịch khám (tùy chọn)") @RequestParam(required = false) LocalDate scheduleDate,
            @Parameter(description = "Trạng thái vé khám (tùy chọn)") @RequestParam(required = false) TicketStatus status,
            @Parameter(description = "ID của bác sĩ (tùy chọn)") @RequestParam(required = false) Long doctorId,
            @Parameter(description = "ID của bệnh nhân (tùy chọn)") @RequestParam(required = false) Long patientId) {
        log.info("Yêu cầu lấy danh sách vé khám: trang={}, kích thước={}, sắp xếp={}, hướng={}, ngày lịch khám={}, trạng thái={}, bác sĩ={}, bệnh nhân={}",
                page, size, sort, direction, scheduleDate, status, doctorId, patientId);
        return ResponseData.<PageResponse<TicketResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy danh sách vé khám thành công")
                .data(ticketService.getAllTicket(page, size, sort, direction, scheduleDate, status, doctorId, patientId))
                .build();
    }
}
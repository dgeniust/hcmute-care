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
import vn.edu.hcmute.utecare.dto.request.TimeSlotRequest;
import vn.edu.hcmute.utecare.dto.response.ResponseData;
import vn.edu.hcmute.utecare.dto.response.TimeSlotResponse;
import vn.edu.hcmute.utecare.service.TimeSlotService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/time-slots")
@RequiredArgsConstructor
@Tag(name = "TIME-SLOT", description = "API quản lý các khung giờ trong hệ thống y tế")
@Slf4j(topic = "TIME_SLOT_CONTROLLER")
public class TimeSlotController {
    private final TimeSlotService timeSlotService;

    @PostMapping
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @Operation(
            summary = "Tạo khung giờ mới",
            description = "Tạo một khung giờ mới với thông tin chi tiết được cung cấp."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tạo khung giờ thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu yêu cầu không hợp lệ")
    })
    public ResponseData<TimeSlotResponse> createTimeSlots(@RequestBody TimeSlotRequest request) {
        log.info("Yêu cầu tạo khung giờ mới: {}", request);
        return ResponseData.<TimeSlotResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Tạo khung giờ thành công")
                .data(timeSlotService.createTimeSlot(request))
                .build();
    }

    @PutMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @Operation(
            summary = "Cập nhật khung giờ",
            description = "Cập nhật thông tin của một khung giờ hiện có dựa trên ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cập nhật khung giờ thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu yêu cầu không hợp lệ"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy khung giờ với ID được cung cấp")
    })
    public ResponseData<TimeSlotResponse> updateTimeSlots(
            @Parameter(description = "ID của khung giờ cần cập nhật") @PathVariable("id") Integer id,
            @RequestBody TimeSlotRequest request) {
        log.info("Yêu cầu cập nhật khung giờ với ID: {}", id);
        return ResponseData.<TimeSlotResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Cập nhật khung giờ thành công")
                .data(timeSlotService.updateTimeSlot(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @Operation(
            summary = "Xóa khung giờ",
            description = "Xóa một khung giờ hiện có dựa trên ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Xóa khung giờ thành công"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy khung giờ với ID được cung cấp")
    })
    public ResponseData<Void> deleteTimeSlots(
            @Parameter(description = "ID của khung giờ cần xóa") @PathVariable("id") Integer id) {
        log.info("Yêu cầu xóa khung giờ với ID: {}", id);
        timeSlotService.deleteTimeSlot(id);
        return ResponseData.<Void>builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message("Xóa khung giờ thành công")
                .build();
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Lấy thông tin khung giờ theo ID",
            description = "Truy xuất thông tin chi tiết của một khung giờ dựa trên ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy thông tin khung giờ thành công"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy khung giờ với ID được cung cấp")
    })
    public ResponseData<TimeSlotResponse> getTimeSlotsById(
            @Parameter(description = "ID của khung giờ cần truy xuất") @PathVariable("id") Integer id) {
        log.info("Yêu cầu lấy thông tin khung giờ với ID: {}", id);
        return ResponseData.<TimeSlotResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy thông tin khung giờ thành công")
                .data(timeSlotService.getTimeSlotById(id))
                .build();
    }

    @GetMapping
    @Operation(
            summary = "Lấy danh sách tất cả khung giờ",
            description = "Truy xuất danh sách tất cả các khung giờ trong hệ thống."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy danh sách khung giờ thành công")
    })
    public ResponseData<List<TimeSlotResponse>> getAllTimeSlots() {
        log.info("Yêu cầu lấy danh sách tất cả khung giờ");
        return ResponseData.<List<TimeSlotResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy danh sách khung giờ thành công")
                .data(timeSlotService.getAllTimeSlots())
                .build();
    }
}
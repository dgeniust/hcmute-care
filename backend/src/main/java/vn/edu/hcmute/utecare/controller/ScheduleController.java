package vn.edu.hcmute.utecare.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmute.utecare.dto.request.ScheduleRequest;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.dto.response.ResponseData;
import vn.edu.hcmute.utecare.dto.response.ScheduleInfoResponse;
import vn.edu.hcmute.utecare.dto.response.ScheduleResponse;
import vn.edu.hcmute.utecare.service.ScheduleService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/schedules")
@RequiredArgsConstructor
@Tag(name = "SCHEDULE", description = "API quản lý lịch khám của bác sĩ")
@Slf4j(topic = "SCHEDULE_CONTROLLER")
public class ScheduleController {
    private final ScheduleService scheduleService;

    @PostMapping
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @Operation(
            summary = "Tạo lịch khám mới",
            description = "Tạo một lịch khám mới với thông tin chi tiết được cung cấp."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tạo lịch khám thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu yêu cầu không hợp lệ"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy bác sĩ, phòng hoặc khung giờ"),
            @ApiResponse(responseCode = "409", description = "Lịch khám trùng với bác sĩ vào ngày đã chọn")
    })
    public ResponseData<ScheduleResponse> createSchedule(@Valid @RequestBody ScheduleRequest request) {
        log.info("Yêu cầu tạo lịch khám mới: {}", request);
        return ResponseData.<ScheduleResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Tạo lịch khám thành công")
                .data(scheduleService.createSchedule(request))
                .build();
    }

    @PutMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @Operation(
            summary = "Cập nhật lịch khám",
            description = "Cập nhật thông tin của một lịch khám hiện có dựa trên ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cập nhật lịch khám thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu yêu cầu không hợp lệ"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy lịch khám, bác sĩ, phòng hoặc khung giờ")
    })
    public ResponseData<ScheduleResponse> updateSchedule(
            @Parameter(description = "ID của lịch khám cần cập nhật") @PathVariable("id") Long id,
            @Valid @RequestBody ScheduleRequest request) {
        log.info("Yêu cầu cập nhật lịch khám với ID: {}", id);
        return ResponseData.<ScheduleResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Cập nhật lịch khám thành công")
                .data(scheduleService.updateSchedule(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @Operation(
            summary = "Xóa lịch khám",
            description = "Xóa một lịch khám hiện có dựa trên ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Xóa lịch khám thành công"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy lịch khám với ID được cung cấp")
    })
    public ResponseData<Void> deleteSchedule(
            @Parameter(description = "ID của lịch khám cần xóa") @PathVariable("id") Long id) {
        log.info("Yêu cầu xóa lịch khám với ID: {}", id);
        scheduleService.deleteSchedule(id);
        return ResponseData.<Void>builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message("Xóa lịch khám thành công")
                .build();
    }

    @GetMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @Operation(
            summary = "Lấy thông tin lịch khám theo ID",
            description = "Truy xuất thông tin chi tiết của một lịch khám dựa trên ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy thông tin lịch khám thành công"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy lịch khám với ID được cung cấp")
    })
    public ResponseData<ScheduleResponse> getScheduleById(
            @Parameter(description = "ID của lịch khám cần truy xuất") @PathVariable("id") Long id) {
        log.info("Yêu cầu lấy thông tin lịch khám với ID: {}", id);
        return ResponseData.<ScheduleResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy thông tin lịch khám thành công")
                .data(scheduleService.getScheduleById(id))
                .build();
    }

    @GetMapping
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @Operation(
            summary = "Lấy danh sách tất cả lịch khám",
            description = "Truy xuất danh sách lịch khám với phân trang và bộ lọc tùy chọn theo bác sĩ và khoảng thời gian."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy danh sách lịch khám thành công")
    })
    public ResponseData<PageResponse<ScheduleResponse>> getAllSchedules(
            @Parameter(description = "ID của bác sĩ (tùy chọn)") @RequestParam(value = "doctorId", required = false) Long doctorId,
            @Parameter(description = "Ngày bắt đầu (tùy chọn)") @RequestParam(value = "startDate", required = false) LocalDate startDate,
            @Parameter(description = "Ngày kết thúc (tùy chọn)") @RequestParam(value = "endDate", required = false) LocalDate endDate,
            @Parameter(description = "Số trang, bắt đầu từ 1") @RequestParam(value = "page", defaultValue = "1") Integer page,
            @Parameter(description = "Số lượng bản ghi mỗi trang") @RequestParam(value = "size", defaultValue = "10") Integer size,
            @Parameter(description = "Trường để sắp xếp (mặc định: date)") @RequestParam(value = "sort", defaultValue = "date") String sort,
            @Parameter(description = "Hướng sắp xếp: asc hoặc desc") @RequestParam(value = "direction", defaultValue = "asc") String direction) {
        log.info("Yêu cầu lấy danh sách lịch khám: bác sĩ={}, ngày bắt đầu={}, ngày kết thúc={}, trang={}, kích thước={}, sắp xếp={}, hướng={}",
                doctorId, startDate, endDate, page, size, sort, direction);
        return ResponseData.<PageResponse<ScheduleResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy danh sách lịch khám thành công")
                .data(scheduleService.getAllSchedules(doctorId, startDate, endDate, page, size, sort, direction))
                .build();
    }

    @GetMapping("/available")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'CUSTOMER', 'DOCTOR', 'NURSE')")
    @Operation(
            summary = "Lấy danh sách lịch khám trống",
            description = "Truy xuất danh sách các lịch khám còn trống theo chuyên khoa và ngày cụ thể."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy danh sách lịch khám trống thành công")
    })
    public ResponseData<List<ScheduleInfoResponse>> getAvailableSchedules(
            @Parameter(description = "ID của chuyên khoa") @RequestParam(value = "medicalSpecialtyId") Integer medicalSpecialtyId,
            @Parameter(description = "Ngày của lịch khám") @RequestParam(value = "date") LocalDate date) {
        log.info("Yêu cầu lấy danh sách lịch khám trống: chuyên khoa={}, ngày={}", medicalSpecialtyId, date);
        return ResponseData.<List<ScheduleInfoResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy danh sách lịch khám trống thành công")
                .data(scheduleService.getAvailableSchedules(medicalSpecialtyId, date))
                .build();
    }
}
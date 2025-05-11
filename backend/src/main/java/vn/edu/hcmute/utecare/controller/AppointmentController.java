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
import vn.edu.hcmute.utecare.dto.request.AppointmentRequest;
import vn.edu.hcmute.utecare.dto.response.AppointmentResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.dto.response.ResponseData;
import vn.edu.hcmute.utecare.service.AppointmentService;

@RestController
@RequestMapping("/api/v1/appointments")
@RequiredArgsConstructor
@Tag(name = "APPOINTMENT", description = "API quản lý lịch hẹn y tế, hỗ trợ tạo, truy xuất, xác nhận và hủy lịch hẹn")
@Slf4j(topic = "APPOINTMENT_CONTROLLER")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'CUSTOMER')")
    @Operation(
            summary = "Tạo lịch hẹn mới",
            description = "Tạo một lịch hẹn y tế mới dựa trên thông tin hồ sơ y tế và các khung giờ được chọn."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tạo lịch hẹn thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu yêu cầu không hợp lệ"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy hồ sơ y tế hoặc khung giờ lịch"),
            @ApiResponse(responseCode = "409", description = "Khung giờ lịch đã đầy hoặc trùng chuyên khoa")
    })
    public ResponseData<AppointmentResponse> createAppointment(@Valid @RequestBody AppointmentRequest request) {
        log.info("Yêu cầu tạo lịch hẹn: {}", request);
        return ResponseData.<AppointmentResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Tạo lịch hẹn thành công")
                .data(appointmentService.createAppointment(request))
                .build();
    }

    @GetMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE', 'CUSTOMER')")
    @Operation(
            summary = "Lấy thông tin lịch hẹn theo ID",
            description = "Truy xuất thông tin chi tiết của một lịch hẹn y tế dựa trên ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy thông tin lịch hẹn thành công"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy lịch hẹn với ID được cung cấp")
    })
    public ResponseData<AppointmentResponse> getAppointmentById(
            @Parameter(description = "ID của lịch hẹn cần truy xuất") @PathVariable("id") Long id) {
        log.info("Yêu cầu lấy thông tin lịch hẹn với ID: {}", id);
        return ResponseData.<AppointmentResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy thông tin lịch hẹn thành công")
                .data(appointmentService.getAppointmentById(id))
                .build();
    }

    @GetMapping("/by-medical-record")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE', 'CUSTOMER')")
    @Operation(
            summary = "Lấy danh sách lịch hẹn theo ID hồ sơ y tế",
            description = "Truy xuất danh sách lịch hẹn phân trang theo ID hồ sơ y tế, hỗ trợ sắp xếp."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy danh sách lịch hẹn thành công")
    })
    public ResponseData<PageResponse<AppointmentResponse>> getAppointmentByMedicalRecordId(
            @Parameter(description = "ID của hồ sơ y tế") @RequestParam Long medicalRecordId,
            @Parameter(description = "Số trang, bắt đầu từ 0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Số lượng lịch hẹn mỗi trang") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Trường để sắp xếp (ví dụ: id, createdAt)") @RequestParam(defaultValue = "id") String sort,
            @Parameter(description = "Hướng sắp xếp: asc (tăng dần) hoặc desc (giảm dần)") @RequestParam(defaultValue = "asc") String direction) {
        log.info("Yêu cầu lấy danh sách lịch hẹn cho hồ sơ y tế ID: {}, trang: {}, kích thước: {}", medicalRecordId, page, size);
        return ResponseData.<PageResponse<AppointmentResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy danh sách lịch hẹn thành công")
                .data(appointmentService.getAppointmentByMedicalRecordId(medicalRecordId, page, size, sort, direction))
                .build();
    }
}
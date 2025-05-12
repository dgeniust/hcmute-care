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
import vn.edu.hcmute.utecare.dto.request.MedicalRecordRequest;
import vn.edu.hcmute.utecare.dto.response.*;
import vn.edu.hcmute.utecare.service.AppointmentService;
import vn.edu.hcmute.utecare.service.EncounterService;
import vn.edu.hcmute.utecare.service.MedicalRecordService;
import vn.edu.hcmute.utecare.service.TicketService;
import vn.edu.hcmute.utecare.util.enumeration.TicketStatus;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/medical-records")
@RequiredArgsConstructor
@Tag(name = "MEDICAL-RECORD", description = "API quản lý hồ sơ y tế, cuộc hẹn, vé khám và cuộc gặp của bệnh nhân")
@Slf4j(topic = "MEDICAL_RECORD_CONTROLLER")
public class MedicalRecordController {
    private final MedicalRecordService medicalRecordService;
    private final AppointmentService appointmentService;
    private final TicketService ticketService;
    private final EncounterService encounterService;

    @PostMapping
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @Operation(
            summary = "Tạo hồ sơ y tế mới",
            description = "Tạo một hồ sơ y tế mới kèm theo thông tin chi tiết của bệnh nhân."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tạo hồ sơ y tế thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu yêu cầu không hợp lệ"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy khách hàng với ID được cung cấp")
    })
    public ResponseData<MedicalRecordResponse> create(@RequestBody @Valid MedicalRecordRequest request) {
        log.info("Yêu cầu tạo hồ sơ y tế mới: {}", request);
        return ResponseData.<MedicalRecordResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Tạo hồ sơ y tế thành công")
                .data(medicalRecordService.create(request))
                .build();
    }

    @GetMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE', 'CUSTOMER')")
    @Operation(
            summary = "Lấy hồ sơ y tế theo ID",
            description = "Truy xuất thông tin chi tiết của một hồ sơ y tế dựa trên ID duy nhất."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy hồ sơ y tế thành công"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy hồ sơ y tế với ID được cung cấp")
    })
    public ResponseData<MedicalRecordResponse> getById(
            @Parameter(description = "ID của hồ sơ y tế cần truy xuất") @PathVariable Long id) {
        log.info("Yêu cầu lấy hồ sơ y tế với ID: {}", id);
        return ResponseData.<MedicalRecordResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy hồ sơ y tế thành công")
                .data(medicalRecordService.getById(id))
                .build();
    }

    @GetMapping
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @Operation(
            summary = "Lấy danh sách tất cả hồ sơ y tế",
            description = "Truy xuất danh sách hồ sơ y tế phân trang, hỗ trợ sắp xếp theo các tiêu chí và thứ tự."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy danh sách hồ sơ y tế thành công"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập")
    })
    public ResponseData<PageResponse<MedicalRecordResponse>> getAll(
            @Parameter(description = "Số trang, bắt đầu từ 1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Số lượng hồ sơ mỗi trang") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Trường để sắp xếp (ví dụ: id, createdAt)") @RequestParam(defaultValue = "id") String sort,
            @Parameter(description = "Hướng sắp xếp: asc (tăng dần) hoặc desc (giảm dần)") @RequestParam(defaultValue = "asc") String direction) {
        log.info("Yêu cầu lấy danh sách hồ sơ y tế: trang={}, kích thước={}, sắp xếp={}, hướng={}", page, size, sort, direction);
        return ResponseData.<PageResponse<MedicalRecordResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy danh sách hồ sơ y tế thành công")
                .data(medicalRecordService.getAll(page, size, sort, direction))
                .build();
    }

    @PutMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @Operation(
            summary = "Cập nhật hồ sơ y tế",
            description = "Cập nhật thông tin của một hồ sơ y tế hiện có dựa trên ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cập nhật hồ sơ y tế thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu yêu cầu không hợp lệ"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy hồ sơ y tế hoặc khách hàng với ID được cung cấp")
    })
    public ResponseData<MedicalRecordResponse> update(
            @Parameter(description = "ID của hồ sơ y tế cần cập nhật") @PathVariable Long id,
            @RequestBody @Valid MedicalRecordRequest request) {
        log.info("Yêu cầu cập nhật hồ sơ y tế với ID: {}", id);
        return ResponseData.<MedicalRecordResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Cập nhật hồ sơ y tế thành công")
                .data(medicalRecordService.update(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(
            summary = "Xóa hồ sơ y tế",
            description = "Xóa một hồ sơ y tế dựa trên ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Xóa hồ sơ y tế thành công"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy hồ sơ y tế với ID được cung cấp")
    })
    public ResponseData<Void> delete(
            @Parameter(description = "ID của hồ sơ y tế cần xóa") @PathVariable Long id) {
        log.info("Yêu cầu xóa hồ sơ y tế với ID: {}", id);
        medicalRecordService.delete(id);
        return ResponseData.<Void>builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message("Xóa hồ sơ y tế thành công")
                .build();
    }

    @GetMapping("/{id}/appointments")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE', 'CUSTOMER')")
    @Operation(
            summary = "Lấy danh sách cuộc hẹn theo hồ sơ y tế",
            description = "Truy xuất danh sách cuộc hẹn phân trang liên quan đến một hồ sơ y tế dựa trên ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy danh sách cuộc hẹn thành công"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy hồ sơ y tế với ID được cung cấp")
    })
    public ResponseData<PageResponse<AppointmentResponse>> getAppointmentsByMedicalRecordId(
            @Parameter(description = "ID của hồ sơ y tế") @PathVariable("id") Long medicalRecordId,
            @Parameter(description = "Số trang, bắt đầu từ 1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Số lượng cuộc hẹn mỗi trang") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Trường để sắp xếp (ví dụ: id, date)") @RequestParam(defaultValue = "id") String sort,
            @Parameter(description = "Hướng sắp xếp: asc (tăng dần) hoặc desc (giảm dần)") @RequestParam(defaultValue = "asc") String direction) {
        log.info("Yêu cầu lấy danh sách cuộc hẹn cho hồ sơ y tế ID: {}, trang={}, kích thước={}, sắp xếp={}, hướng={}",
                medicalRecordId, page, size, sort, direction);
        return ResponseData.<PageResponse<AppointmentResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy danh sách cuộc hẹn thành công")
                .data(appointmentService.getAppointmentByMedicalRecordId(medicalRecordId, page, size, sort, direction))
                .build();
    }

    @GetMapping("/{id}/tickets")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE', 'CUSTOMER')")
    @Operation(
            summary = "Lấy danh sách vé khám theo hồ sơ y tế",
            description = "Truy xuất danh sách vé khám phân trang liên quan đến một hồ sơ y tế dựa trên ID, hỗ trợ lọc theo trạng thái vé."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy danh sách vé khám thành công"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy hồ sơ y tế với ID được cung cấp")
    })
    public ResponseData<PageResponse<TicketResponse>> getTicketsByMedicalRecordId(
            @Parameter(description = "ID của hồ sơ y tế") @PathVariable("id") Long medicalRecordId,
            @Parameter(description = "Trạng thái vé khám (ví dụ: PENDING, CONFIRMED)") @RequestParam(required = false) TicketStatus status,
            @Parameter(description = "Số trang, bắt đầu từ 1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Số lượng vé mỗi trang") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Trường để sắp xếp (ví dụ: id, date)") @RequestParam(defaultValue = "id") String sort,
            @Parameter(description = "Hướng sắp xếp: asc (tăng dần) hoặc desc (giảm dần)") @RequestParam(defaultValue = "asc") String direction) {
        log.info("Yêu cầu lấy danh sách vé khám cho hồ sơ y tế ID: {}, trạng thái: {}, trang={}, kích thước={}, sắp xếp={}, hướng={}",
                medicalRecordId, status, page, size, sort, direction);
        return ResponseData.<PageResponse<TicketResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy danh sách vé khám thành công")
                .data(ticketService.getAllTicketsByMedicalRecordId(medicalRecordId, status, page, size, sort, direction))
                .build();
    }

    @GetMapping("/{id}/encounters")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE', 'CUSTOMER')")
    @Operation(
            summary = "Lấy danh sách cuộc gặp theo hồ sơ y tế",
            description = "Truy xuất danh sách các cuộc gặp liên quan đến một hồ sơ y tế dựa trên ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy danh sách cuộc gặp thành công"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy hồ sơ y tế với ID được cung cấp")
    })
    public ResponseData<List<EncounterResponse>> getEncountersByMedicalRecordId(
            @Parameter(description = "ID của hồ sơ y tế") @PathVariable("id") Long medicalRecordId) {
        log.info("Yêu cầu lấy danh sách cuộc gặp cho hồ sơ y tế ID: {}", medicalRecordId);
        return ResponseData.<List<EncounterResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy danh sách cuộc gặp thành công")
                .data(encounterService.getAllEncounterByMedicalRecordId(medicalRecordId))
                .build();
    }

    @GetMapping("/{id}/date/encounters")
    @Operation(summary = "Get all encounter by medical record ID", description = "Retrieves all encounters with a medical record id")
    public ResponseData<List<EncounterResponse>> getEncountersByMedicalRecordIdAndDate(
            @PathVariable("id") Long medicalRecordId,
            @RequestParam("date")LocalDate date) {
        log.info("Get encounters by medical record ID: {}", medicalRecordId);
        return ResponseData.<List<EncounterResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Encounters retrieved successfully")
                .data(medicalRecordService.getEncounterByMedicalRecordIdAndDate(medicalRecordId, date))
                .build();
    }
}
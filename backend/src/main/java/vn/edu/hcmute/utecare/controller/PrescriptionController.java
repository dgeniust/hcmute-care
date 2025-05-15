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
import vn.edu.hcmute.utecare.dto.request.PrescriptionRequest;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.dto.response.PrescriptionItemResponse;
import vn.edu.hcmute.utecare.dto.response.PrescriptionResponse;
import vn.edu.hcmute.utecare.dto.response.ResponseData;
import vn.edu.hcmute.utecare.service.PrescriptionService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/prescriptions")
@RequiredArgsConstructor
@Tag(name = "PRESCRIPTION", description = "API quản lý thông tin đơn thuốc và các mục thuốc liên quan")
@Slf4j(topic = "PRESCRIPTION_CONTROLLER")
public class PrescriptionController {
    private final PrescriptionService prescriptionService;

    @GetMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE', 'CUSTOMER')")
    @Operation(
            summary = "Lấy thông tin đơn thuốc theo ID",
            description = "Truy xuất thông tin chi tiết của một đơn thuốc dựa trên ID duy nhất."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy thông tin đơn thuốc thành công"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy đơn thuốc với ID được cung cấp")
    })
    public ResponseData<PrescriptionResponse> getPrescriptionById(
            @Parameter(description = "ID của đơn thuốc cần truy xuất") @PathVariable("id") Long id) {
        log.info("Yêu cầu lấy thông tin đơn thuốc với ID: {}", id);
        return ResponseData.<PrescriptionResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy thông tin đơn thuốc thành công")
                .data(prescriptionService.getPrescriptionById(id))
                .build();
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @Operation(
            summary = "Lấy danh sách tất cả đơn thuốc",
            description = "Truy xuất danh sách đơn thuốc phân trang, dành cho admin hoặc nhân viên."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy danh sách đơn thuốc thành công"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập")
    })
    public ResponseData<PageResponse<PrescriptionResponse>> getAllPrescriptions(
            @Parameter(description = "Số trang, bắt đầu từ 1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Số lượng đơn thuốc mỗi trang") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Trường để sắp xếp (ví dụ: id, createdDate)") @RequestParam(defaultValue = "id") String sort,
            @Parameter(description = "Hướng sắp xếp: asc (tăng dần) hoặc desc (giảm dần)") @RequestParam(defaultValue = "asc") String direction) {
        log.info("Yêu cầu lấy danh sách tất cả đơn thuốc: trang={}, kích thước={}, sắp xếp={}, hướng={}", page, size, sort, direction);
        return ResponseData.<PageResponse<PrescriptionResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy danh sách đơn thuốc thành công")
                .data(prescriptionService.getAllPrescriptions(page, size, sort, direction))
                .build();
    }

    @GetMapping("/{id}/item")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE', 'CUSTOMER')")
    @Operation(
            summary = "Lấy danh sách mục thuốc theo ID đơn thuốc",
            description = "Truy xuất tất cả các mục thuốc thuộc một đơn thuốc dựa trên ID đơn thuốc."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy danh sách mục thuốc thành công"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy đơn thuốc với ID được cung cấp")
    })
    public ResponseData<List<PrescriptionItemResponse>> getAllPrescriptionItemsByPrescriptionItemId(
            @Parameter(description = "ID của đơn thuốc") @PathVariable("id") Long id) {
        log.info("Yêu cầu lấy danh sách mục thuốc với ID đơn thuốc: {}", id);
        return ResponseData.<List<PrescriptionItemResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy danh sách mục thuốc thành công")
                .data(prescriptionService.getAllPrescriptionItemsByPrescriptionId(id))
                .build();
    }

    @PostMapping
//    @PreAuthorize("hasAnyRole('DOCTOR')")
    @Operation(
            summary = "Tạo đơn thuốc mới",
            description = "Tạo một đơn thuốc mới với thông tin chi tiết và các mục thuốc liên quan."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tạo đơn thuốc thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu yêu cầu không hợp lệ"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy cuộc khám hoặc thuốc")
    })
    public ResponseData<PrescriptionResponse> createPrescription(@Valid @RequestBody PrescriptionRequest request) {
        log.info("Yêu cầu tạo đơn thuốc mới: {}", request);
        return ResponseData.<PrescriptionResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Tạo đơn thuốc thành công")
                .data(prescriptionService.addPrescription(request))
                .build();
    }

    @PutMapping("/{id}")
//    @PreAuthorize("hasAnyRole('DOCTOR')")
    @Operation(
            summary = "Cập nhật đơn thuốc",
            description = "Cập nhật thông tin của một đơn thuốc hiện có dựa trên ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cập nhật đơn thuốc thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu yêu cầu không hợp lệ"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy đơn thuốc với ID được cung cấp")
    })
    public ResponseData<PrescriptionResponse> updatePrescription(
            @Parameter(description = "ID của đơn thuốc cần cập nhật") @PathVariable("id") Long id,
            @Valid @RequestBody PrescriptionRequest request) {
        log.info("Yêu cầu cập nhật đơn thuốc với ID: {}", id);
        return ResponseData.<PrescriptionResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Cập nhật đơn thuốc thành công")
                .data(prescriptionService.updatePrescription(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    @Operation(
            summary = "Xóa đơn thuốc",
            description = "Xóa một đơn thuốc hiện có dựa trên ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Xóa đơn thuốc thành công"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy đơn thuốc với ID được cung cấp")
    })
    public ResponseData<Void> deletePrescription(
            @Parameter(description = "ID của đơn thuốc cần xóa") @PathVariable("id") Long id) {
        log.info("Yêu cầu xóa đơn thuốc với ID: {}", id);
        prescriptionService.deletePrescription(id);
        return ResponseData.<Void>builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message("Xóa đơn thuốc thành công")
                .build();
    }
}
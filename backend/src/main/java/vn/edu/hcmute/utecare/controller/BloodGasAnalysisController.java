package vn.edu.hcmute.utecare.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmute.utecare.dto.request.BloodGasAnalysisRequest;
import vn.edu.hcmute.utecare.dto.response.BloodGasAnalysisResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.dto.response.ResponseData;
import vn.edu.hcmute.utecare.service.BloodGasAnalysisService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/blood-gas-analysis")
@RequiredArgsConstructor
@Tag(name = "BLOOD-GAS-ANALYSIS", description = "API quản lý xét nghiệm phân tích khí máu trong hệ thống y tế")
@Slf4j(topic = "BLOOD_GAS_ANALYSIS_CONTROLLER")
public class BloodGasAnalysisController {

    private final BloodGasAnalysisService bloodGasAnalysisService;

    @GetMapping
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE')")
    @Operation(
            summary = "Lấy danh sách xét nghiệm phân tích khí máu có phân trang",
            description = "Truy xuất danh sách xét nghiệm phân tích khí máu với hỗ trợ phân trang và sắp xếp."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy danh sách xét nghiệm phân tích khí máu thành công")
    })
    public ResponseData<PageResponse<BloodGasAnalysisResponse>> getAllBloodGasAnalysisWithPagination(
            @Parameter(description = "Số trang, bắt đầu từ 1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Số lượng bản ghi mỗi trang") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Trường để sắp xếp (ví dụ: id, createDate)") @RequestParam(defaultValue = "id") String sort,
            @Parameter(description = "Hướng sắp xếp: asc (tăng dần) hoặc desc (giảm dần)") @RequestParam(defaultValue = "asc") String direction) {
        log.info("Yêu cầu lấy danh sách xét nghiệm phân tích khí máu: trang={}, kích thước={}, sắp xếp={}, hướng={}", page, size, sort, direction);
        return ResponseData.<PageResponse<BloodGasAnalysisResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy danh sách xét nghiệm phân tích khí máu thành công")
                .data(bloodGasAnalysisService.getAll(page, size, sort, direction))
                .build();
    }

    @GetMapping("/all")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE')")
    @Operation(
            summary = "Lấy toàn bộ danh sách xét nghiệm phân tích khí máu",
            description = "Truy xuất toàn bộ danh sách xét nghiệm phân tích khí máu trong hệ thống."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy danh sách xét nghiệm phân tích khí máu thành công")
    })
    public ResponseData<List<BloodGasAnalysisResponse>> getAllBloodGasAnalysis() {
        log.info("Yêu cầu lấy toàn bộ danh sách xét nghiệm phân tích khí máu");
        return ResponseData.<List<BloodGasAnalysisResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy danh sách xét nghiệm phân tích khí máu thành công")
                .data(bloodGasAnalysisService.getAll())
                .build();
    }

    @GetMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE', 'CUSTOMER')")
    @Operation(
            summary = "Lấy thông tin xét nghiệm phân tích khí máu theo ID",
            description = "Truy xuất thông tin chi tiết của một xét nghiệm phân tích khí máu dựa trên ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy thông tin xét nghiệm phân tích khí máu thành công"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy xét nghiệm phân tích khí máu với ID được cung cấp")
    })
    public ResponseData<BloodGasAnalysisResponse> getBloodGasAnalysisById(
            @Parameter(description = "ID của xét nghiệm phân tích khí máu cần truy xuất") @PathVariable("id") Long id) {
        log.info("Yêu cầu lấy thông tin xét nghiệm phân tích khí máu với ID: {}", id);
        return ResponseData.<BloodGasAnalysisResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy thông tin xét nghiệm phân tích khí máu thành công")
                .data(bloodGasAnalysisService.getBloodGasAnalysisById(id))
                .build();
    }

    @PostMapping
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE')")
    @Operation(
            summary = "Tạo xét nghiệm phân tích khí máu mới",
            description = "Tạo một xét nghiệm phân tích khí máu mới với thông tin được cung cấp."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tạo xét nghiệm phân tích khí máu thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu yêu cầu không hợp lệ"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy cuộc gặp với ID được cung cấp")
    })
    public ResponseData<BloodGasAnalysisResponse> createBloodGasAnalysis(@Valid @RequestBody BloodGasAnalysisRequest request) {
        log.info("Yêu cầu tạo xét nghiệm phân tích khí máu mới: {}", request);
        return ResponseData.<BloodGasAnalysisResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Tạo xét nghiệm phân tích khí máu thành công")
                .data(bloodGasAnalysisService.createBloodGasAnalysis(request))
                .build();
    }

    @PutMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE')")
    @Operation(
            summary = "Cập nhật xét nghiệm phân tích khí máu",
            description = "Cập nhật thông tin của một xét nghiệm phân tích khí máu hiện có dựa trên ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cập nhật xét nghiệm phân tích khí máu thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu yêu cầu không hợp lệ"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy xét nghiệm phân tích khí máu hoặc cuộc gặp với ID được cung cấp")
    })
    public ResponseData<BloodGasAnalysisResponse> updateBloodGasAnalysis(
            @Parameter(description = "ID của xét nghiệm phân tích khí máu cần cập nhật") @PathVariable("id") Long id,
            @Valid @RequestBody BloodGasAnalysisRequest request) {
        log.info("Yêu cầu cập nhật xét nghiệm phân tích khí máu với ID: {}", id);
        return ResponseData.<BloodGasAnalysisResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Cập nhật xét nghiệm phân tích khí máu thành công")
                .data(bloodGasAnalysisService.updateBloodGasAnalysis(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE')")
    @Operation(
            summary = "Xóa xét nghiệm phân tích khí máu",
            description = "Xóa một xét nghiệm phân tích khí máu hiện có dựa trên ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Xóa xét nghiệm phân tích khí máu thành công"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy xét nghiệm phân tích khí máu với ID được cung cấp")
    })
    public ResponseData<Void> deleteBloodGasAnalysis(
            @Parameter(description = "ID của xét nghiệm phân tích khí máu cần xóa") @PathVariable("id") Long id) {
        log.info("Yêu cầu xóa xét nghiệm phân tích khí máu với ID: {}", id);
        bloodGasAnalysisService.deleteBloodGasAnalysis(id);
        return ResponseData.<Void>builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message("Xóa xét nghiệm phân tích khí máu thành công")
                .build();
    }

    @GetMapping("/by-date")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE')")
    @Operation(
            summary = "Lấy danh sách xét nghiệm phân tích khí máu theo ngày và trạng thái",
            description = "Truy xuất danh sách xét nghiệm phân tích khí máu theo ngày và trạng thái (ví dụ: PENDING, COMPLETED)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy danh sách xét nghiệm phân tích khí máu thành công"),
            @ApiResponse(responseCode = "400", description = "Trạng thái không hợp lệ")
    })
    public ResponseData<List<BloodGasAnalysisResponse>> getAllLabTestByDateAndStatus(
            @Parameter(description = "Ngày cần truy xuất (định dạng: yyyy-MM-dd)") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @Parameter(description = "Trạng thái của xét nghiệm (ví dụ: PENDING, COMPLETED)") @RequestParam String status) {
        log.info("Yêu cầu lấy danh sách xét nghiệm phân tích khí máu theo ngày: {} và trạng thái: {}", date, status);
        return ResponseData.<List<BloodGasAnalysisResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy danh sách xét nghiệm phân tích khí máu thành công")
                .data(bloodGasAnalysisService.getAllLabTestByDateAndStatus(date, status))
                .build();
    }

    @GetMapping("/by-encounter-and-date")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE', 'CUSTOMER')")
    @Operation(
            summary = "Lấy danh sách xét nghiệm phân tích khí máu theo cuộc gặp và ngày",
            description = "Truy xuất danh sách xét nghiệm phân tích khí máu theo ID cuộc gặp và ngày (tùy chọn, mặc định là hôm nay)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy danh sách xét nghiệm phân tích khí máu thành công")
    })
    public ResponseData<List<BloodGasAnalysisResponse>> getBloodGasAnalysisByEncounterIdAndDate(
            @Parameter(description = "ID của cuộc gặp") @RequestParam Long encounterId,
            @Parameter(description = "Ngày cần truy xuất (tùy chọn, định dạng: yyyy-MM-dd)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("Yêu cầu lấy danh sách xét nghiệm phân tích khí máu theo cuộc gặp ID: {} và ngày: {}", encounterId, date);
        return ResponseData.<List<BloodGasAnalysisResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy danh sách xét nghiệm phân tích khí máu thành công")
                .data(bloodGasAnalysisService.getEncounterIdAndDate(encounterId, date))
                .build();
    }
}
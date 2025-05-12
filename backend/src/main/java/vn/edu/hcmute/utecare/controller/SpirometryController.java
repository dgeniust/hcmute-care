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
import vn.edu.hcmute.utecare.dto.request.SpirometryRequest;
import vn.edu.hcmute.utecare.dto.response.SpirometryResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.dto.response.ResponseData;
import vn.edu.hcmute.utecare.service.SpirometryService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/spirometry")
@RequiredArgsConstructor
@Tag(name = "SPIROMETRY", description = "API quản lý xét nghiệm chức năng hô hấp (Spirometry) trong hệ thống y tế")
@Slf4j(topic = "SPIROMETRY_CONTROLLER")
public class SpirometryController {

    private final SpirometryService spirometryService;

    @GetMapping
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE')")
    @Operation(
            summary = "Lấy danh sách xét nghiệm hô hấp có phân trang",
            description = "Truy xuất danh sách xét nghiệm hô hấp với hỗ trợ phân trang và sắp xếp."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy danh sách xét nghiệm hô hấp thành công")
    })
    public ResponseData<PageResponse<SpirometryResponse>> getAllSpirometryWithPagination(
            @Parameter(description = "Số trang, bắt đầu từ 1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Số lượng bản ghi mỗi trang") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Trường để sắp xếp (ví dụ: id, createDate)") @RequestParam(defaultValue = "id") String sort,
            @Parameter(description = "Hướng sắp xếp: asc (tăng dần) hoặc desc (giảm dần)") @RequestParam(defaultValue = "asc") String direction) {
        log.info("Yêu cầu lấy danh sách xét nghiệm hô hấp: trang={}, kích thước={}, sắp xếp={}, hướng={}", page, size, sort, direction);
        return ResponseData.<PageResponse<SpirometryResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy danh sách xét nghiệm hô hấp thành công")
                .data(spirometryService.getAll(page, size, sort, direction))
                .build();
    }

    @GetMapping("/all")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE')")
    @Operation(
            summary = "Lấy toàn bộ danh sách xét nghiệm hô hấp",
            description = "Truy xuất toàn bộ danh sách xét nghiệm hô hấp trong hệ thống."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy danh sách xét nghiệm hô hấp thành công")
    })
    public ResponseData<List<SpirometryResponse>> getAllSpirometry() {
        log.info("Yêu cầu lấy toàn bộ danh sách xét nghiệm hô hấp");
        return ResponseData.<List<SpirometryResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy danh sách xét nghiệm hô hấp thành công")
                .data(spirometryService.getAll())
                .build();
    }

    @GetMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE', 'CUSTOMER')")
    @Operation(
            summary = "Lấy xét nghiệm hô hấp theo ID",
            description = "Truy xuất thông tin chi tiết của một xét nghiệm hô hấp dựa trên ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy thông tin xét nghiệm hô hấp thành công"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy xét nghiệm hô hấp với ID được cung cấp")
    })
    public ResponseData<SpirometryResponse> getSpirometryById(
            @Parameter(description = "ID của xét nghiệm hô hấp cần truy xuất") @PathVariable("id") Long id) {
        log.info("Yêu cầu lấy thông tin xét nghiệm hô hấp với ID: {}", id);
        return ResponseData.<SpirometryResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy thông tin xét nghiệm hô hấp thành công")
                .data(spirometryService.getSpirometryById(id))
                .build();
    }

    @PostMapping
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE')")
    @Operation(
            summary = "Tạo xét nghiệm hô hấp mới",
            description = "Tạo một xét nghiệm hô hấp mới với thông tin được cung cấp."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tạo xét nghiệm hô hấp thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu yêu cầu không hợp lệ"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy cuộc gặp với ID được cung cấp")
    })
    public ResponseData<SpirometryResponse> createSpirometry(@Valid @RequestBody SpirometryRequest request) {
        log.info("Yêu cầu tạo xét nghiệm hô hấp mới: {}", request);
        return ResponseData.<SpirometryResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Tạo xét nghiệm hô hấp thành công")
                .data(spirometryService.createSpirometry(request))
                .build();
    }

    @PutMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE')")
    @Operation(
            summary = "Cập nhật xét nghiệm hô hấp",
            description = "Cập nhật thông tin của một xét nghiệm hô hấp hiện có dựa trên ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cập nhật xét nghiệm hô hấp thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu yêu cầu không hợp lệ"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy xét nghiệm hô hấp hoặc cuộc gặp với ID được cung cấp")
    })
    public ResponseData<SpirometryResponse> updateSpirometry(
            @Parameter(description = "ID của xét nghiệm hô hấp cần cập nhật") @PathVariable("id") Long id,
            @Valid @RequestBody SpirometryRequest request) {
        log.info("Yêu cầu cập nhật xét nghiệm hô hấp với ID: {}", id);
        return ResponseData.<SpirometryResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Cập nhật xét nghiệm hô hấp thành công")
                .data(spirometryService.updateSpirometry(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE')")
    @Operation(
            summary = "Xóa xét nghiệm hô hấp",
            description = "Xóa một xét nghiệm hô hấp hiện có dựa trên ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Xóa xét nghiệm hô hấp thành công"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy xét nghiệm hô hấp với ID được cung cấp")
    })
    public ResponseData<Void> deleteSpirometry(
            @Parameter(description = "ID của xét nghiệm hô hấp cần xóa") @PathVariable("id") Long id) {
        log.info("Yêu cầu xóa xét nghiệm hô hấp với ID: {}", id);
        spirometryService.deleteSpirometry(id);
        return ResponseData.<Void>builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message("Xóa xét nghiệm hô hấp thành công")
                .build();
    }

    @GetMapping("/by-date")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE')")
    @Operation(
            summary = "Lấy danh sách xét nghiệm hô hấp theo ngày và trạng thái",
            description = "Truy xuất danh sách xét nghiệm hô hấp theo ngày và trạng thái (ví dụ: PENDING)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy danh sách xét nghiệm hô hấp thành công"),
            @ApiResponse(responseCode = "400", description = "Trạng thái không hợp lệ")
    })
    public ResponseData<List<SpirometryResponse>> getAllLabTestByDateAndStatus(
            @Parameter(description = "Ngày cần truy xuất (định dạng: yyyy-MM-dd)") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @Parameter(description = "Trạng thái của xét nghiệm (ví dụ: PENDING, COMPLETED)") @RequestParam String status) {
        log.info("Yêu cầu lấy danh sách xét nghiệm hô hấp theo ngày: {} và trạng thái: {}", date, status);
        return ResponseData.<List<SpirometryResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy danh sách xét nghiệm hô hấp thành công")
                .data(spirometryService.getAllLabTestByDateAndStatus(date, status))
                .build();
    }

    @GetMapping("/by-encounter-and-date")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE', 'CUSTOMER')")
    @Operation(
            summary = "Lấy danh sách xét nghiệm hô hấp theo cuộc gặp và ngày",
            description = "Truy xuất danh sách xét nghiệm hô hấp theo ID cuộc gặp và ngày (tùy chọn, mặc định là hôm nay)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy danh sách xét nghiệm hô hấp thành công")
    })
    public ResponseData<List<SpirometryResponse>> getEncounterIdAndDate(
            @Parameter(description = "ID của cuộc gặp") @RequestParam Long encounterId,
            @Parameter(description = "Ngày cần truy xuất (tùy chọn, định dạng: yyyy-MM-dd)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("Yêu cầu lấy danh sách xét nghiệm hô hấp theo cuộc gặp ID: {} và ngày: {}", encounterId, date);
        return ResponseData.<List<SpirometryResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy danh sách xét nghiệm hô hấp thành công")
                .data(spirometryService.getEncounterIdAndDate(encounterId, date))
                .build();
    }
}
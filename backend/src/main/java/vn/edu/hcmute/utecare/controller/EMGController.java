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
import vn.edu.hcmute.utecare.dto.request.EMGRequest;
import vn.edu.hcmute.utecare.dto.response.EMGResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.dto.response.ResponseData;
import vn.edu.hcmute.utecare.service.EMGService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/emg")
@RequiredArgsConstructor
@Tag(name = "EMG", description = "API quản lý xét nghiệm điện cơ (EMG) trong hệ thống y tế")
@Slf4j(topic = "EMG_CONTROLLER")
public class EMGController {

    private final EMGService emgService;

    @GetMapping
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE')")
    @Operation(
            summary = "Lấy danh sách EMG có phân trang",
            description = "Truy xuất danh sách xét nghiệm EMG với hỗ trợ phân trang và sắp xếp."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy danh sách EMG thành công")
    })
    public ResponseData<PageResponse<EMGResponse>> getAllEMGWithPagination(
            @Parameter(description = "Số trang, bắt đầu từ 1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Số lượng bản ghi mỗi trang") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Trường để sắp xếp (ví dụ: id, createDate)") @RequestParam(defaultValue = "id") String sort,
            @Parameter(description = "Hướng sắp xếp: asc (tăng dần) hoặc desc (giảm dần)") @RequestParam(defaultValue = "asc") String direction) {
        log.info("Yêu cầu lấy danh sách EMG: trang={}, kích thước={}, sắp xếp={}, hướng={}", page, size, sort, direction);
        return ResponseData.<PageResponse<EMGResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy danh sách EMG thành công")
                .data(emgService.getAll(page, size, sort, direction))
                .build();
    }

    @GetMapping("/all")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE')")
    @Operation(
            summary = "Lấy toàn bộ danh sách EMG",
            description = "Truy xuất toàn bộ danh sách xét nghiệm EMG trong hệ thống."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy danh sách EMG thành công")
    })
    public ResponseData<List<EMGResponse>> getAllEMG() {
        log.info("Yêu cầu lấy toàn bộ danh sách EMG");
        return ResponseData.<List<EMGResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy danh sách EMG thành công")
                .data(emgService.getAll())
                .build();
    }

    @GetMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE', 'CUSTOMER')")
    @Operation(
            summary = "Lấy thông tin EMG theo ID",
            description = "Truy xuất thông tin chi tiết của một xét nghiệm EMG dựa trên ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy thông tin EMG thành công"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy EMG với ID được cung cấp")
    })
    public ResponseData<EMGResponse> getEMGById(
            @Parameter(description = "ID của xét nghiệm EMG cần truy xuất") @PathVariable("id") Long id) {
        log.info("Yêu cầu lấy thông tin EMG với ID: {}", id);
        return ResponseData.<EMGResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy thông tin EMG thành công")
                .data(emgService.getEMGById(id))
                .build();
    }

    @PostMapping
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE')")
    @Operation(
            summary = "Tạo xét nghiệm EMG mới",
            description = "Tạo một xét nghiệm EMG mới với thông tin được cung cấp."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tạo xét nghiệm EMG thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu yêu cầu không hợp lệ"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy cuộc gặp với ID được cung cấp")
    })
    public ResponseData<EMGResponse> createEMG(@Valid @RequestBody EMGRequest request) {
        log.info("Yêu cầu tạo xét nghiệm EMG mới: {}", request);
        return ResponseData.<EMGResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Tạo xét nghiệm EMG thành công")
                .data(emgService.createEMG(request))
                .build();
    }

    @PutMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE')")
    @Operation(
            summary = "Cập nhật xét nghiệm EMG",
            description = "Cập nhật thông tin của một xét nghiệm EMG hiện có dựa trên ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cập nhật xét nghiệm EMG thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu yêu cầu không hợp lệ"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy EMG hoặc cuộc gặp với ID được cung cấp")
    })
    public ResponseData<EMGResponse> updateEMG(
            @Parameter(description = "ID của xét nghiệm EMG cần cập nhật") @PathVariable("id") Long id,
            @Valid @RequestBody EMGRequest request) {
        log.info("Yêu cầu cập nhật xét nghiệm EMG với ID: {}", id);
        return ResponseData.<EMGResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Cập nhật xét nghiệm EMG thành công")
                .data(emgService.updateEMG(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE')")
    @Operation(
            summary = "Xóa xét nghiệm EMG",
            description = "Xóa một xét nghiệm EMG hiện có dựa trên ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Xóa xét nghiệm EMG thành công"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy EMG với ID được cung cấp")
    })
    public ResponseData<Void> deleteEMG(
            @Parameter(description = "ID của xét nghiệm EMG cần xóa") @PathVariable("id") Long id) {
        log.info("Yêu cầu xóa xét nghiệm EMG với ID: {}", id);
        emgService.deleteEMG(id);
        return ResponseData.<Void>builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message("Xóa xét nghiệm EMG thành công")
                .build();
    }

    @GetMapping("/by-date")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE')")
    @Operation(
            summary = "Lấy danh sách EMG theo ngày và trạng thái",
            description = "Truy xuất danh sách xét nghiệm EMG theo ngày và trạng thái (ví dụ: PENDING)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy danh sách EMG thành công"),
            @ApiResponse(responseCode = "400", description = "Trạng thái không hợp lệ")
    })
    public ResponseData<List<EMGResponse>> getAllLabTestByDateAndStatus(
            @Parameter(description = "Ngày cần truy xuất (định dạng: yyyy-MM-dd)") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @Parameter(description = "Trạng thái của xét nghiệm (ví dụ: PENDING, COMPLETED)") @RequestParam String status) {
        log.info("Yêu cầu lấy danh sách EMG theo ngày: {} và trạng thái: {}", date, status);
        return ResponseData.<List<EMGResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy danh sách EMG thành công")
                .data(emgService.getAllLabTestByDateAndStatus(date, status))
                .build();
    }

    @GetMapping("/by-encounter-and-date")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE', 'CUSTOMER')")
    @Operation(
            summary = "Lấy danh sách EMG theo cuộc gặp và ngày",
            description = "Truy xuất danh sách xét nghiệm EMG theo ID cuộc gặp và ngày (tùy chọn, mặc định là hôm nay)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy danh sách EMG thành công")
    })
    public ResponseData<List<EMGResponse>> getEncounterIdAndDate(
            @Parameter(description = "ID của cuộc gặp") @RequestParam Long encounterId,
            @Parameter(description = "Ngày cần truy xuất (tùy chọn, định dạng: yyyy-MM-dd)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("Yêu cầu lấy danh sách EMG theo cuộc gặp ID: {} và ngày: {}", encounterId, date);
        return ResponseData.<List<EMGResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy danh sách EMG thành công")
                .data(emgService.getEncounterIdAndDate(encounterId, date))
                .build();
    }
}
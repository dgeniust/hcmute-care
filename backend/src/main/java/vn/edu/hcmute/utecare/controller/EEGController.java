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
import vn.edu.hcmute.utecare.dto.request.EEGRequest;
import vn.edu.hcmute.utecare.dto.response.EEGResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.dto.response.ResponseData;
import vn.edu.hcmute.utecare.service.EEGService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/eeg")
@RequiredArgsConstructor
@Tag(name = "EEG", description = "API quản lý xét nghiệm điện não đồ (EEG) trong hệ thống y tế")
@Slf4j(topic = "EEG_CONTROLLER")
public class EEGController {

    private final EEGService eegService;

    @GetMapping
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE')")
    @Operation(
            summary = "Lấy danh sách EEG có phân trang",
            description = "Truy xuất danh sách xét nghiệm EEG với hỗ trợ phân trang và sắp xếp."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy danh sách EEG thành công")
    })
    public ResponseData<PageResponse<EEGResponse>> getAllEEGWithPagination(
            @Parameter(description = "Số trang, bắt đầu từ 1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Số lượng bản ghi mỗi trang") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Trường để sắp xếp (ví dụ: id, createDate)") @RequestParam(defaultValue = "id") String sort,
            @Parameter(description = "Hướng sắp xếp: asc (tăng dần) hoặc desc (giảm dần)") @RequestParam(defaultValue = "asc") String direction) {
        log.info("Yêu cầu lấy danh sách EEG: trang={}, kích thước={}, sắp xếp={}, hướng={}", page, size, sort, direction);
        return ResponseData.<PageResponse<EEGResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy danh sách EEG thành công")
                .data(eegService.getAll(page, size, sort, direction))
                .build();
    }

    @GetMapping("/all")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE')")
    @Operation(
            summary = "Lấy toàn bộ danh sách EEG",
            description = "Truy xuất toàn bộ danh sách xét nghiệm EEG trong hệ thống."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy danh sách EEG thành công")
    })
    public ResponseData<List<EEGResponse>> getAllEEG() {
        log.info("Yêu cầu lấy toàn bộ danh sách EEG");
        return ResponseData.<List<EEGResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy danh sách EEG thành công")
                .data(eegService.getAll())
                .build();
    }

    @GetMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE', 'CUSTOMER')")
    @Operation(
            summary = "Lấy thông tin EEG theo ID",
            description = "Truy xuất thông tin chi tiết của một xét nghiệm EEG dựa trên ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy thông tin EEG thành công"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy EEG với ID được cung cấp")
    })
    public ResponseData<EEGResponse> getEEGById(
            @Parameter(description = "ID của xét nghiệm EEG cần truy xuất") @PathVariable("id") Long id) {
        log.info("Yêu cầu lấy thông tin EEG với ID: {}", id);
        return ResponseData.<EEGResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy thông tin EEG thành công")
                .data(eegService.getEEGById(id))
                .build();
    }

    @PostMapping
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE')")
    @Operation(
            summary = "Tạo xét nghiệm EEG mới",
            description = "Tạo một xét nghiệm EEG mới với thông tin được cung cấp."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tạo xét nghiệm EEG thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu yêu cầu không hợp lệ"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy cuộc gặp với ID được cung cấp")
    })
    public ResponseData<EEGResponse> createEEG(@Valid @RequestBody EEGRequest request) {
        log.info("Yêu cầu tạo xét nghiệm EEG mới: {}", request);
        return ResponseData.<EEGResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Tạo xét nghiệm EEG thành công")
                .data(eegService.createEEG(request))
                .build();
    }

    @PutMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE')")
    @Operation(
            summary = "Cập nhật xét nghiệm EEG",
            description = "Cập nhật thông tin của một xét nghiệm EEG hiện có dựa trên ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cập nhật xét nghiệm EEG thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu yêu cầu không hợp lệ"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy EEG hoặc cuộc gặp với ID được cung cấp")
    })
    public ResponseData<EEGResponse> updateEEG(
            @Parameter(description = "ID của xét nghiệm EEG cần cập nhật") @PathVariable("id") Long id,
            @Valid @RequestBody EEGRequest request) {
        log.info("Yêu cầu cập nhật xét nghiệm EEG với ID: {}", id);
        return ResponseData.<EEGResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Cập nhật xét nghiệm EEG thành công")
                .data(eegService.updateEEG(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE')")
    @Operation(
            summary = "Xóa xét nghiệm EEG",
            description = "Xóa một xét nghiệm EEG hiện có dựa trên ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Xóa xét nghiệm EEG thành công"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy EEG với ID được cung cấp")
    })
    public ResponseData<Void> deleteEEG(
            @Parameter(description = "ID của xét nghiệm EEG cần xóa") @PathVariable("id") Long id) {
        log.info("Yêu cầu xóa xét nghiệm EEG với ID: {}", id);
        eegService.deleteEEG(id);
        return ResponseData.<Void>builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message("Xóa xét nghiệm EEG thành công")
                .build();
    }

    @GetMapping("/by-date")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE')")
    @Operation(
            summary = "Lấy danh sách EEG theo ngày và trạng thái",
            description = "Truy xuất danh sách xét nghiệm EEG theo ngày và trạng thái (ví dụ: PENDING)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy danh sách EEG thành công"),
            @ApiResponse(responseCode = "400", description = "Trạng thái không hợp lệ")
    })
    public ResponseData<List<EEGResponse>> getAllLabTestByDateAndStatus(
            @Parameter(description = "Ngày cần truy xuất (định dạng: yyyy-MM-dd)") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @Parameter(description = "Trạng thái của xét nghiệm (ví dụ: PENDING, COMPLETED)") @RequestParam String status) {
        log.info("Yêu cầu lấy danh sách EEG theo ngày: {} và trạng thái: {}", date, status);
        return ResponseData.<List<EEGResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy danh sách EEG thành công")
                .data(eegService.getAllLabTestByDateAndStatus(date, status))
                .build();
    }

    @GetMapping("/by-encounter-and-date")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE', 'CUSTOMER')")
    @Operation(
            summary = "Lấy danh sách EEG theo cuộc gặp và ngày",
            description = "Truy xuất danh sách xét nghiệm EEG theo ID cuộc gặp và ngày (tùy chọn, mặc định là hôm nay)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy danh sách EEG thành công")
    })
    public ResponseData<List<EEGResponse>> getEncounterIdAndDate(
            @Parameter(description = "ID của cuộc gặp") @RequestParam Long encounterId,
            @Parameter(description = "Ngày cần truy xuất (tùy chọn, định dạng: yyyy-MM-dd)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("Yêu cầu lấy danh sách EEG theo cuộc gặp ID: {} và ngày: {}", encounterId, date);
        return ResponseData.<List<EEGResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy danh sách EEG thành công")
                .data(eegService.getEncounterIdAndDate(encounterId, date))
                .build();
    }
}
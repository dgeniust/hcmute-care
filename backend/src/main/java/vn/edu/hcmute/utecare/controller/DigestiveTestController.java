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
import vn.edu.hcmute.utecare.dto.request.DigestiveTestRequest;
import vn.edu.hcmute.utecare.dto.response.DigestiveTestResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.dto.response.ResponseData;
import vn.edu.hcmute.utecare.model.Account;
import vn.edu.hcmute.utecare.service.DigestiveTestService;
import vn.edu.hcmute.utecare.util.SecurityUtil;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/digestive-tests")
@RequiredArgsConstructor
@Tag(name = "DIGESTIVE-TEST", description = "API quản lý xét nghiệm tiêu hóa trong hệ thống y tế")
@Slf4j(topic = "DIGESTIVE_TEST_CONTROLLER")
public class DigestiveTestController {

    private final DigestiveTestService digestiveTestService;

    @PostMapping
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE')")
    @Operation(
            summary = "Tạo xét nghiệm tiêu hóa mới",
            description = "Tạo một xét nghiệm tiêu hóa mới với thông tin chi tiết được cung cấp."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tạo xét nghiệm tiêu hóa thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu yêu cầu không hợp lệ"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy cuộc gặp với ID được cung cấp")
    })
    public ResponseData<DigestiveTestResponse> createDigestiveTest(@Valid @RequestBody DigestiveTestRequest request) {
        log.info("Yêu cầu tạo xét nghiệm tiêu hóa mới: {}", request);
        return ResponseData.<DigestiveTestResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Tạo xét nghiệm tiêu hóa thành công")
                .data(digestiveTestService.createDigestiveTest(request))
                .build();
    }

    @GetMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE', 'CUSTOMER')")
    @Operation(
            summary = "Lấy thông tin xét nghiệm tiêu hóa theo ID",
            description = "Truy xuất thông tin chi tiết của một xét nghiệm tiêu hóa dựa trên ID duy nhất."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy thông tin xét nghiệm tiêu hóa thành công"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy xét nghiệm tiêu hóa với ID được cung cấp")
    })
    public ResponseData<DigestiveTestResponse> getDigestiveTestById(
            @Parameter(description = "ID của xét nghiệm tiêu hóa cần truy xuất") @PathVariable("id") Long id) {
        log.info("Yêu cầu lấy thông tin xét nghiệm tiêu hóa với ID: {}", id);
        return ResponseData.<DigestiveTestResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy thông tin xét nghiệm tiêu hóa thành công")
                .data(digestiveTestService.getDigestiveTestById(id))
                .build();
    }

    @GetMapping
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE')")
    @Operation(
            summary = "Lấy danh sách tất cả xét nghiệm tiêu hóa",
            description = "Truy xuất danh sách tất cả xét nghiệm tiêu hóa với phân trang và sắp xếp."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy danh sách xét nghiệm tiêu hóa thành công")
    })
    public ResponseData<PageResponse<DigestiveTestResponse>> getAllDigestiveTests(
            @Parameter(description = "Số trang, bắt đầu từ 1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Số lượng bản ghi mỗi trang") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Trường để sắp xếp (ví dụ: id, createDate)") @RequestParam(defaultValue = "id") String sort,
            @Parameter(description = "Hướng sắp xếp: asc (tăng dần) hoặc desc (giảm dần)") @RequestParam(defaultValue = "asc") String direction) {
        log.info("Yêu cầu lấy danh sách xét nghiệm tiêu hóa: trang={}, kích thước={}, sắp xếp={}, hướng={}", page, size, sort, direction);
        return ResponseData.<PageResponse<DigestiveTestResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy danh sách xét nghiệm tiêu hóa thành công")
                .data(digestiveTestService.getAll(page, size, sort, direction))
                .build();
    }

    @PutMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE')")
    @Operation(
            summary = "Cập nhật xét nghiệm tiêu hóa",
            description = "Cập nhật thông tin của một xét nghiệm tiêu hóa hiện có dựa trên ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cập nhật xét nghiệm tiêu hóa thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu yêu cầu không hợp lệ"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy xét nghiệm tiêu hóa hoặc cuộc gặp với ID được cung cấp")
    })
    public ResponseData<DigestiveTestResponse> updateDigestiveTest(
            @Parameter(description = "ID của xét nghiệm tiêu hóa cần cập nhật") @PathVariable("id") Long id,
            @Valid @RequestBody DigestiveTestRequest request) {
        log.info("Yêu cầu cập nhật xét nghiệm tiêu hóa với ID: {}", id);
        return ResponseData.<DigestiveTestResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Cập nhật xét nghiệm tiêu hóa thành công")
                .data(digestiveTestService.updateDigestiveTest(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE')")
    @Operation(
            summary = "Xóa xét nghiệm tiêu hóa",
            description = "Xóa một xét nghiệm tiêu hóa hiện có dựa trên ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Xóa xét nghiệm tiêu hóa thành công"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy xét nghiệm tiêu hóa với ID được cung cấp")
    })
    public ResponseData<Void> deleteDigestiveTest(
            @Parameter(description = "ID của xét nghiệm tiêu hóa cần xóa") @PathVariable("id") Long id) {
        log.info("Yêu cầu xóa xét nghiệm tiêu hóa với ID: {}", id);
        digestiveTestService.deleteDigestiveTest(id);
        return ResponseData.<Void>builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message("Xóa xét nghiệm tiêu hóa thành công")
                .build();
    }

    @GetMapping("/by-date")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE')")
    @Operation(
            summary = "Lấy danh sách xét nghiệm tiêu hóa theo ngày và trạng thái",
            description = "Truy xuất danh sách xét nghiệm tiêu hóa theo ngày và trạng thái (ví dụ: PENDING)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy danh sách xét nghiệm tiêu hóa thành công"),
            @ApiResponse(responseCode = "400", description = "Trạng thái không hợp lệ")
    })
    public ResponseData<List<DigestiveTestResponse>> getAllLabTestByDateAndStatus(
            @Parameter(description = "Ngày cần truy xuất (định dạng: yyyy-MM-dd)") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @Parameter(description = "Trạng thái của xét nghiệm (ví dụ: PENDING, COMPLETED)") @RequestParam String status) {
        log.info("Yêu cầu lấy danh sách xét nghiệm tiêu hóa theo ngày: {} và trạng thái: {}", date, status);
        return ResponseData.<List<DigestiveTestResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy danh sách xét nghiệm tiêu hóa thành công")
                .data(digestiveTestService.getAllLabTestByDateAndStatus(date, status))
                .build();
    }

    @GetMapping("/by-encounter-and-date")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE', 'CUSTOMER')")
    @Operation(
            summary = "Lấy danh sách xét nghiệm tiêu hóa theo cuộc gặp và ngày",
            description = "Truy xuất danh sách xét nghiệm tiêu hóa theo ID cuộc gặp và ngày (tùy chọn, mặc định là hôm nay)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy danh sách xét nghiệm tiêu hóa thành công")
    })
    public ResponseData<List<DigestiveTestResponse>> getEncounterIdAndDate(
            @Parameter(description = "ID của cuộc gặp") @RequestParam Long encounterId,
            @Parameter(description = "Ngày cần truy xuất (tùy chọn, định dạng: yyyy-MM-dd)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("Yêu cầu lấy danh sách xét nghiệm tiêu hóa theo cuộc gặp ID: {} và ngày: {}", encounterId, date);
        return ResponseData.<List<DigestiveTestResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy danh sách xét nghiệm tiêu hóa thành công")
                .data(digestiveTestService.getEncounterIdAndDate(encounterId, date))
                .build();
    }
}
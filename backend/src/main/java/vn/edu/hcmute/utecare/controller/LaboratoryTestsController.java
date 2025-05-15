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
import vn.edu.hcmute.utecare.dto.request.LaboratoryTestsRequest;
import vn.edu.hcmute.utecare.dto.response.LaboratoryTestsResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.dto.response.ResponseData;
import vn.edu.hcmute.utecare.service.LaboratoryTestsService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/laboratory-tests")
@RequiredArgsConstructor
@Tag(name = "LABORATORY-TESTS", description = "API quản lý xét nghiệm phòng thí nghiệm trong hệ thống y tế")
@Slf4j(topic = "LABORATORY_TESTS_CONTROLLER")
public class LaboratoryTestsController {

    private final LaboratoryTestsService laboratoryTestsService;

    @GetMapping
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE')")
    @Operation(
            summary = "Lấy danh sách xét nghiệm phòng thí nghiệm có phân trang",
            description = "Truy xuất danh sách xét nghiệm phòng thí nghiệm với hỗ trợ phân trang và sắp xếp."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy danh sách xét nghiệm phòng thí nghiệm thành công")
    })
    public ResponseData<PageResponse<LaboratoryTestsResponse>> getAllLaboratoryTestsWithPagination(
            @Parameter(description = "Số trang, bắt đầu từ 1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Số lượng bản ghi mỗi trang") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Trường để sắp xếp (ví dụ: id, createDate)") @RequestParam(defaultValue = "id") String sort,
            @Parameter(description = "Hướng sắp xếp: asc (tăng dần) hoặc desc (giảm dần)") @RequestParam(defaultValue = "asc") String direction) {
        log.info("Yêu cầu lấy danh sách xét nghiệm phòng thí nghiệm: trang={}, kích thước={}, sắp xếp={}, hướng={}", page, size, sort, direction);
        return ResponseData.<PageResponse<LaboratoryTestsResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy danh sách xét nghiệm phòng thí nghiệm thành công")
                .data(laboratoryTestsService.getAll(page, size, sort, direction))
                .build();
    }

    @GetMapping("/all")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE')")
    @Operation(
            summary = "Lấy toàn bộ danh sách xét nghiệm phòng thí nghiệm",
            description = "Truy xuất toàn bộ danh sách xét nghiệm phòng thí nghiệm trong hệ thống."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy danh sách xét nghiệm phòng thí nghiệm thành công")
    })
    public ResponseData<List<LaboratoryTestsResponse>> getAllLaboratoryTests() {
        log.info("Yêu cầu lấy toàn bộ danh sách xét nghiệm phòng thí nghiệm");
        return ResponseData.<List<LaboratoryTestsResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy danh sách xét nghiệm phòng thí nghiệm thành công")
                .data(laboratoryTestsService.getAll())
                .build();
    }

    @GetMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE', 'CUSTOMER')")
    @Operation(
            summary = "Lấy xét nghiệm phòng thí nghiệm theo ID",
            description = "Truy xuất thông tin chi tiết của một xét nghiệm phòng thí nghiệm dựa trên ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy thông tin xét nghiệm phòng thí nghiệm thành công"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy xét nghiệm phòng thí nghiệm với ID được cung cấp")
    })
    public ResponseData<LaboratoryTestsResponse> getLaboratoryTestsById(
            @Parameter(description = "ID của xét nghiệm phòng thí nghiệm cần truy xuất") @PathVariable("id") Long id) {
        log.info("Yêu cầu lấy thông tin xét nghiệm phòng thí nghiệm với ID: {}", id);
        return ResponseData.<LaboratoryTestsResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy thông tin xét nghiệm phòng thí nghiệm thành công")
                .data(laboratoryTestsService.getLaboratoryTestsById(id))
                .build();
    }

    @PostMapping
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE')")
    @Operation(
            summary = "Tạo xét nghiệm phòng thí nghiệm mới",
    description = "Tạo một xét nghiệm phòng thí nghiệm mới với thông tin được cung cấp."
            )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tạo xét nghiệm phòng thí nghiệm thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu yêu cầu không hợp lệ")
    })
    public ResponseData<LaboratoryTestsResponse> createLaboratoryTests(@Valid @RequestBody LaboratoryTestsRequest request) {
        log.info("Yêu cầu tạo xét nghiệm phòng thí nghiệm mới: {}", request);
        return ResponseData.<LaboratoryTestsResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Tạo xét nghiệm phòng thí nghiệm thành công")
                .data(laboratoryTestsService.createLaboratoryTests(request))
                .build();
    }

    @PutMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE')")
    @Operation(
            summary = "Cập nhật xét nghiệm phòng thí nghiệm",
            description = "Cập nhật thông tin của một xét nghiệm phòng thí nghiệm hiện có dựa trên ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cập nhật xét nghiệm phòng thí nghiệm thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu yêu cầu không hợp lệ"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy xét nghiệm phòng thí nghiệm với ID được cung cấp")
    })
    public ResponseData<LaboratoryTestsResponse> updateLaboratoryTests(
            @Parameter(description = "ID của xét nghiệm phòng thí nghiệm cần cập nhật") @PathVariable("id") Long id,
            @Valid @RequestBody LaboratoryTestsRequest request) {
        log.info("Yêu cầu cập nhật xét nghiệm phòng thí nghiệm với ID: {}", id);
        return ResponseData.<LaboratoryTestsResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Cập nhật xét nghiệm phòng thí nghiệm thành công")
                .data(laboratoryTestsService.updateLaboratoryTests(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE')")
    @Operation(
            summary = "Xóa xét nghiệm phòng thí nghiệm",
            description = "Xóa một xét nghiệm phòng thí nghiệm hiện có dựa trên ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Xóa xét nghiệm phòng thí nghiệm thành công"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy xét nghiệm phòng thí nghiệm với ID được cung cấp")
    })
    public ResponseData<Void> deleteLaboratoryTests(
            @Parameter(description = "ID của xét nghiệm phòng thí nghiệm cần xóa") @PathVariable("id") Long id) {
        log.info("Yêu cầu xóa xét nghiệm phòng thí nghiệm với ID: {}", id);
        laboratoryTestsService.deleteLaboratoryTests(id);
        return ResponseData.<Void>builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message("Xóa xét nghiệm phòng thí nghiệm thành công")
                .build();
    }

    @GetMapping("/by-date")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE')")
    @Operation(
            summary = "Lấy danh sách xét nghiệm phòng thí nghiệm theo ngày và trạng thái",
            description = "Truy xuất danh sách xét nghiệm phòng thí nghiệm theo ngày và trạng thái (ví dụ: PENDING)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy danh sách xét nghiệm phòng thí nghiệm thành công"),
            @ApiResponse(responseCode = "400", description = "Trạng thái không hợp lệ")
    })
    public ResponseData<List<LaboratoryTestsResponse>> getAllLabTestByDateAndStatus(
            @Parameter(description = "Ngày cần truy xuất (định dạng: yyyy-MM-dd)") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @Parameter(description = "Trạng thái của xét nghiệm (ví dụ: PENDING, COMPLETED)") @RequestParam String status) {
        log.info("Yêu cầu lấy danh sách xét nghiệm phòng thí nghiệm theo ngày: {} và trạng thái: {}", date, status);
        return ResponseData.<List<LaboratoryTestsResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy danh sách xét nghiệm phòng thí nghiệm thành công")
                .data(laboratoryTestsService.getAllLabTestByDateAndStatus(date, status))
                .build();
    }

    @GetMapping("/by-encounter-and-date")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE', 'CUSTOMER')")
    @Operation(
            summary = "Lấy danh sách xét nghiệm phòng thí nghiệm theo cuộc gặp và ngày",
            description = "Truy xuất danh sách xét nghiệm phòng thí nghiệm theo ID cuộc gặp và ngày (tùy chọn, mặc định là hôm nay)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy danh sách xét nghiệm phòng thí nghiệm thành công")
    })
    public ResponseData<List<LaboratoryTestsResponse>> getEncounterIdAndDate(
            @Parameter(description = "ID của cuộc gặp") @RequestParam Long encounterId,
            @Parameter(description = "Ngày cần truy xuất (tùy chọn, định dạng: yyyy-MM-dd)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("Yêu cầu lấy danh sách xét nghiệm phòng thí nghiệm theo cuộc gặp ID: {} và ngày: {}", encounterId, date);
        return ResponseData.<List<LaboratoryTestsResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy danh sách xét nghiệm phòng thí nghiệm thành công")
                .data(laboratoryTestsService.getEncounterIdAndDate(encounterId, date))
                .build();
    }
}
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
import vn.edu.hcmute.utecare.dto.request.ImagingTestRequest;
import vn.edu.hcmute.utecare.dto.response.ImagingTestResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.dto.response.ResponseData;
import vn.edu.hcmute.utecare.service.ImagingTestService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/imaging-tests")
@RequiredArgsConstructor
@Tag(name = "IMAGING-TEST", description = "API quản lý xét nghiệm hình ảnh y khoa trong hệ thống y tế")
@Slf4j(topic = "IMAGING_TEST_CONTROLLER")
public class ImagingTestController {

    private final ImagingTestService imagingTestService;

    @GetMapping
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE')")
    @Operation(
            summary = "Lấy danh sách xét nghiệm hình ảnh có phân trang",
            description = "Truy xuất danh sách xét nghiệm hình ảnh với hỗ trợ phân trang và sắp xếp."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy danh sách xét nghiệm hình ảnh thành công")
    })
    public ResponseData<PageResponse<ImagingTestResponse>> getAllImagingTestsWithPagination(
            @Parameter(description = "Số trang, bắt đầu từ 1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Số lượng bản ghi mỗi trang") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Trường để sắp xếp (ví dụ: id, createDate)") @RequestParam(defaultValue = "id") String sort,
            @Parameter(description = "Hướng sắp xếp: asc (tăng dần) hoặc desc (giảm dần)") @RequestParam(defaultValue = "asc") String direction) {
        log.info("Yêu cầu lấy danh sách xét nghiệm hình ảnh: trang={}, kích thước={}, sắp xếp={}, hướng={}", page, size, sort, direction);
        return ResponseData.<PageResponse<ImagingTestResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy danh sách xét nghiệm hình ảnh thành công")
                .data(imagingTestService.getAll(page, size, sort, direction))
                .build();
    }

    @GetMapping("/all")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE')")
    @Operation(
            summary = "Lấy toàn bộ danh sách xét nghiệm hình ảnh",
            description = "Truy xuất toàn bộ danh sách xét nghiệm hình ảnh trong hệ thống."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy danh sách xét nghiệm hình ảnh thành công")
    })
    public ResponseData<List<ImagingTestResponse>> getAllImagingTests() {
        log.info("Yêu cầu lấy toàn bộ danh sách xét nghiệm hình ảnh");
        return ResponseData.<List<ImagingTestResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy danh sách xét nghiệm hình ảnh thành công")
                .data(imagingTestService.getAll())
                .build();
    }

    @GetMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE', 'CUSTOMER')")
    @Operation(
            summary = "Lấy xét nghiệm hình ảnh theo ID",
            description = "Truy xuất thông tin chi tiết của một xét nghiệm hình ảnh dựa trên ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy thông tin xét nghiệm hình ảnh thành công"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy xét nghiệm hình ảnh với ID được cung cấp")
    })
    public ResponseData<ImagingTestResponse> getImagingTestById(
            @Parameter(description = "ID của xét nghiệm hình ảnh cần truy xuất") @PathVariable("id") Long id) {
        log.info("Yêu cầu lấy thông tin xét nghiệm hình ảnh với ID: {}", id);
        return ResponseData.<ImagingTestResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy thông tin xét nghiệm hình ảnh thành công")
                .data(imagingTestService.getImagingTestById(id))
                .build();
    }

    @PostMapping
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE')")
    @Operation(
            summary = "Tạo xét nghiệm hình ảnh mới",
            description = "Tạo một xét nghiệm hình ảnh mới với thông tin được cung cấp."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tạo xét nghiệm hình ảnh thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu yêu cầu không hợp lệ"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy cuộc gặp với ID được cung cấp")
    })
    public ResponseData<ImagingTestResponse> createImagingTest(@Valid @RequestBody ImagingTestRequest request) {
        log.info("Yêu cầu tạo xét nghiệm hình ảnh mới: {}", request);
        return ResponseData.<ImagingTestResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Tạo xét nghiệm hình ảnh thành công")
                .data(imagingTestService.createImagingTest(request))
                .build();
    }

    @PutMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE')")
    @Operation(
            summary = "Cập nhật xét nghiệm hình ảnh",
            description = "Cập nhật thông tin của một xét nghiệm hình ảnh hiện có dựa trên ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cập nhật xét nghiệm hình ảnh thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu yêu cầu không hợp lệ"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy xét nghiệm hình ảnh hoặc cuộc gặp với ID được cung cấp")
    })
    public ResponseData<ImagingTestResponse> updateImagingTest(
            @Parameter(description = "ID của xét nghiệm hình ảnh cần cập nhật") @PathVariable("id") Long id,
            @Valid @RequestBody ImagingTestRequest request) {
        log.info("Yêu cầu cập nhật xét nghiệm hình ảnh với ID: {}", id);
        return ResponseData.<ImagingTestResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Cập nhật xét nghiệm hình ảnh thành công")
                .data(imagingTestService.updateImagingTest(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE')")
    @Operation(
            summary = "Xóa xét nghiệm hình ảnh",
            description = "Xóa một xét nghiệm hình ảnh hiện có dựa trên ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Xóa xét nghiệm hình ảnh thành công"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy xét nghiệm hình ảnh với ID được cung cấp")
    })
    public ResponseData<Void> deleteImagingTest(
            @Parameter(description = "ID của xét nghiệm hình ảnh cần xóa") @PathVariable("id") Long id) {
        log.info("Yêu cầu xóa xét nghiệm hình ảnh với ID: {}", id);
        imagingTestService.deleteImagingTest(id);
        return ResponseData.<Void>builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message("Xóa xét nghiệm hình ảnh thành công")
                .build();
    }

    @GetMapping("/by-date")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE')")
    @Operation(
            summary = "Lấy danh sách xét nghiệm hình ảnh theo ngày và trạng thái",
            description = "Truy xuất danh sách xét nghiệm hình ảnh theo ngày và trạng thái (ví dụ: PENDING)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy danh sách xét nghiệm hình ảnh thành công"),
            @ApiResponse(responseCode = "400", description = "Trạng thái không hợp lệ")
    })
    public ResponseData<List<ImagingTestResponse>> getAllLabTestByDateAndStatus(
            @Parameter(description = "Ngày cần truy xuất (định dạng: yyyy-MM-dd)") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @Parameter(description = "Trạng thái của xét nghiệm (ví dụ: PENDING, COMPLETED)") @RequestParam String status) {
        log.info("Yêu cầu lấy danh sách xét nghiệm hình ảnh theo ngày: {} và trạng thái: {}", date, status);
        return ResponseData.<List<ImagingTestResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy danh sách xét nghiệm hình ảnh thành công")
                .data(imagingTestService.getAllImagingTestByDateAndStatus(date, status))
                .build();
    }

    @GetMapping("/by-encounter-and-date")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE', 'CUSTOMER')")
    @Operation(
            summary = "Lấy danh sách xét nghiệm hình ảnh theo cuộc gặp và ngày",
            description = "Truy xuất danh sách xét nghiệm hình ảnh theo ID cuộc gặp và ngày (tùy chọn, mặc định là hôm nay)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy danh sách xét nghiệm hình ảnh thành công")
    })
    public ResponseData<List<ImagingTestResponse>> getEncounterIdAndDate(
            @Parameter(description = "ID của cuộc gặp") @RequestParam Long encounterId,
            @Parameter(description = "Ngày cần truy xuất (tùy chọn, định dạng: yyyy-MM-dd)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("Yêu cầu lấy danh sách xét nghiệm hình ảnh theo cuộc gặp ID: {} và ngày: {}", encounterId, date);
        return ResponseData.<List<ImagingTestResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy danh sách xét nghiệm hình ảnh thành công")
                .data(imagingTestService.getEncounterIdAndDate(encounterId, date))
                .build();
    }
}
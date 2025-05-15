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
import vn.edu.hcmute.utecare.dto.request.NerveConductionRequest;
import vn.edu.hcmute.utecare.dto.response.NerveConductionResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.dto.response.ResponseData;
import vn.edu.hcmute.utecare.service.NerveConductionService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/nerve-conduction")
@RequiredArgsConstructor
@Tag(name = "NERVE-CONDUCTION", description = "API quản lý các bản ghi xét nghiệm dẫn truyền thần kinh")
@Slf4j(topic = "NERVE_CONDUCTION_CONTROLLER")
public class NerveConductionController {

    private final NerveConductionService nerveConductionService;

    @GetMapping
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE')")
    @Operation(
            summary = "Lấy danh sách xét nghiệm dẫn truyền thần kinh có phân trang",
            description = "Truy xuất danh sách các bản ghi xét nghiệm dẫn truyền thần kinh với phân trang và sắp xếp."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy danh sách xét nghiệm dẫn truyền thần kinh thành công"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập danh sách xét nghiệm")
    })
    public ResponseData<PageResponse<NerveConductionResponse>> getAllNerveConductionWithPagination(
            @Parameter(description = "Số trang, bắt đầu từ 1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Số lượng bản ghi mỗi trang") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Trường để sắp xếp (ví dụ: id, createDate)") @RequestParam(defaultValue = "id") String sort,
            @Parameter(description = "Hướng sắp xếp: asc (tăng dần) hoặc desc (giảm dần)") @RequestParam(defaultValue = "asc") String direction) {
        log.info("Yêu cầu lấy danh sách xét nghiệm dẫn truyền thần kinh: trang={}, kích thước={}, sắp xếp={}, hướng={}", page, size, sort, direction);
        PageResponse<NerveConductionResponse> response = nerveConductionService.getAll(page, size, sort, direction);
        return ResponseData.<PageResponse<NerveConductionResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy danh sách xét nghiệm dẫn truyền thần kinh thành công")
                .data(response)
                .build();
    }

    @GetMapping("/all")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE')")
    @Operation(
            summary = "Lấy toàn bộ danh sách xét nghiệm dẫn truyền thần kinh",
            description = "Truy xuất toàn bộ các bản ghi xét nghiệm dẫn truyền thần kinh trong hệ thống."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy toàn bộ danh sách xét nghiệm dẫn truyền thần kinh thành công"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập danh sách xét nghiệm")
    })
    public ResponseData<List<NerveConductionResponse>> getAllNerveConduction() {
        log.info("Yêu cầu lấy toàn bộ danh sách xét nghiệm dẫn truyền thần kinh");
        List<NerveConductionResponse> response = nerveConductionService.getAll();
        return ResponseData.<List<NerveConductionResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy toàn bộ danh sách xét nghiệm dẫn truyền thần kinh thành công")
                .data(response)
                .build();
    }

    @GetMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE')")
    @Operation(
            summary = "Lấy thông tin xét nghiệm dẫn truyền thần kinh theo ID",
            description = "Truy xuất chi tiết một bản ghi xét nghiệm dẫn truyền thần kinh dựa trên ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy thông tin xét nghiệm dẫn truyền thần kinh thành công"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập thông tin xét nghiệm"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy xét nghiệm với ID được cung cấp")
    })
    public ResponseData<NerveConductionResponse> getNerveConductionById(
            @Parameter(description = "ID của xét nghiệm cần truy xuất") @PathVariable("id") Long id) {
        log.info("Yêu cầu lấy thông tin xét nghiệm dẫn truyền thần kinh với ID: {}", id);
        NerveConductionResponse response = nerveConductionService.getNerveConductionById(id);
        return ResponseData.<NerveConductionResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy thông tin xét nghiệm dẫn truyền thần kinh thành công")
                .data(response)
                .build();
    }

    @PostMapping
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR')")
    @Operation(
            summary = "Tạo mới xét nghiệm dẫn truyền thần kinh",
            description = "Tạo mới một bản ghi xét nghiệm dẫn truyền thần kinh với dữ liệu được cung cấp."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tạo xét nghiệm dẫn truyền thần kinh thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu yêu cầu không hợp lệ"),
            @ApiResponse(responseCode = "403", description = "Không có quyền tạo xét nghiệm"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy cuộc gặp với ID được cung cấp")
    })
    public ResponseData<NerveConductionResponse> createNerveConduction(@Valid @RequestBody NerveConductionRequest request) {
        log.info("Yêu cầu tạo mới xét nghiệm dẫn truyền thần kinh: {}", request);
        NerveConductionResponse response = nerveConductionService.createNerveConduction(request);
        return ResponseData.<NerveConductionResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Tạo xét nghiệm dẫn truyền thần kinh thành công")
                .data(response)
                .build();
    }

    @PutMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR')")
    @Operation(
            summary = "Cập nhật xét nghiệm dẫn truyền thần kinh theo ID",
            description = "Cập nhật thông tin một bản ghi xét nghiệm dẫn truyền thần kinh dựa trên ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cập nhật xét nghiệm dẫn truyền thần kinh thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu yêu cầu không hợp lệ"),
            @ApiResponse(responseCode = "403", description = "Không có quyền cập nhật xét nghiệm"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy xét nghiệm hoặc cuộc gặp với ID được cung cấp")
    })
    public ResponseData<NerveConductionResponse> updateNerveConduction(
            @Parameter(description = "ID của xét nghiệm cần cập nhật") @PathVariable("id") Long id,
            @Valid @RequestBody NerveConductionRequest request) {
        log.info("Yêu cầu cập nhật xét nghiệm dẫn truyền thần kinh với ID: {}", id);
        NerveConductionResponse response = nerveConductionService.updateNerveConduction(id, request);
        return ResponseData.<NerveConductionResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Cập nhật xét nghiệm dẫn truyền thần kinh thành công")
                .data(response)
                .build();
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Xóa xét nghiệm dẫn truyền thần kinh theo ID",
            description = "Xóa một bản ghi xét nghiệm dẫn truyền thần kinh dựa trên ID. Chỉ quản trị viên được phép thực hiện."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Xóa xét nghiệm dẫn truyền thần kinh thành công"),
            @ApiResponse(responseCode = "403", description = "Không có quyền xóa xét nghiệm"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy xét nghiệm với ID được cung cấp")
    })
    public ResponseData<Void> deleteNerveConduction(
            @Parameter(description = "ID của xét nghiệm cần xóa") @PathVariable("id") Long id) {
        log.info("Yêu cầu xóa xét nghiệm dẫn truyền thần kinh với ID: {}", id);
        nerveConductionService.deleteNerveConduction(id);
        return ResponseData.<Void>builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message("Xóa xét nghiệm dẫn truyền thần kinh thành công")
                .build();
    }

    @GetMapping("/by-date")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE')")
    @Operation(
            summary = "Lấy danh sách xét nghiệm dẫn truyền thần kinh theo ngày và trạng thái",
            description = "Truy xuất danh sách các xét nghiệm dẫn truyền thần kinh theo ngày và trạng thái (PENDING, COMPLETED, v.v.)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy danh sách xét nghiệm dẫn truyền thần kinh theo ngày thành công"),
            @ApiResponse(responseCode = "400", description = "Trạng thái không hợp lệ"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập danh sách xét nghiệm")
    })
    public ResponseData<List<NerveConductionResponse>> getAllLabTestByDateAndStatus(
            @Parameter(description = "Ngày cần truy xuất (định dạng yyyy-MM-dd)") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @Parameter(description = "Trạng thái xét nghiệm (PENDING, COMPLETED, v.v.)") @RequestParam String status) {
        log.info("Yêu cầu lấy danh sách xét nghiệm dẫn truyền thần kinh theo ngày: {} và trạng thái: {}", date, status);
        List<NerveConductionResponse> responses = nerveConductionService.getAllLabTestByDateAndStatus(date, status);
        return ResponseData.<List<NerveConductionResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy danh sách xét nghiệm dẫn truyền thần kinh theo ngày và trạng thái thành công")
                .data(responses)
                .build();
    }

    @GetMapping("/by-encounter-and-date")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE')")
    @Operation(
            summary = "Lấy danh sách xét nghiệm dẫn truyền thần kinh theo cuộc gặp và ngày",
            description = "Truy xuất danh sách các xét nghiệm dẫn truyền thần kinh theo ID cuộc gặp và ngày (tùy chọn, mặc định là hôm nay)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy danh sách xét nghiệm dẫn truyền thần kinh theo cuộc gặp và ngày thành công"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập danh sách xét nghiệm"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy cuộc gặp với ID được cung cấp")
    })
    public ResponseData<List<NerveConductionResponse>> getEncounterIdAndDate(
            @Parameter(description = "ID của cuộc gặp") @RequestParam Long encounterId,
            @Parameter(description = "Ngày cần truy xuất (định dạng yyyy-MM-dd, tùy chọn)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("Yêu cầu lấy danh sách xét nghiệm dẫn truyền thần kinh theo cuộc gặp ID: {} và ngày: {}", encounterId, date);
        List<NerveConductionResponse> responses = nerveConductionService.getEncounterIdAndDate(encounterId, date);
        return ResponseData.<List<NerveConductionResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy danh sách xét nghiệm dẫn truyền thần kinh theo cuộc gặp và ngày thành công")
                .data(responses)
                .build();
    }
}
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
import vn.edu.hcmute.utecare.dto.request.NurseRequest;
import vn.edu.hcmute.utecare.dto.response.NurseResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.dto.response.ResponseData;
import vn.edu.hcmute.utecare.service.NurseService;
import vn.edu.hcmute.utecare.util.SecurityUtil;

@RestController
@RequestMapping("/api/v1/nurses")
@RequiredArgsConstructor
@Tag(name = "NURSE", description = "API quản lý thông tin y tá, tài khoản liên quan và tìm kiếm theo chuyên khoa")
@Slf4j(topic = "NURSE_CONTROLLER")
public class NurseController {

    private final NurseService nurseService;

    @GetMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE')")
    @Operation(
            summary = "Lấy thông tin y tá theo ID",
            description = "Truy xuất thông tin chi tiết của một y tá dựa trên ID. Y tá chỉ có thể xem thông tin của chính họ."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy thông tin y tá thành công"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập thông tin y tá"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy y tá với ID được cung cấp")
    })
    public ResponseData<NurseResponse> getNurseById(
            @Parameter(description = "ID của y tá cần truy xuất") @PathVariable("id") Long id) {
        log.info("Yêu cầu lấy thông tin y tá với ID: {}", id);
//        // Kiểm tra nếu người dùng là NURSE, chỉ cho phép xem thông tin của chính họ
//        if (SecurityUtil.hasRole("NURSE") && !SecurityUtil.getCurrentUserId().equals(id)) {
//            log.warn("Người dùng với ID {} không có quyền truy cập thông tin y tá ID: {}", SecurityUtil.getCurrentUserId(), id);
//            throw new SecurityException("Không có quyền truy cập thông tin y tá này");
//        }
        return ResponseData.<NurseResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy thông tin y tá thành công")
                .data(nurseService.getNurseById(id))
                .build();
    }

    @PostMapping
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @Operation(
            summary = "Tạo y tá mới",
            description = "Tạo một y tá mới với thông tin chi tiết và tài khoản liên quan. Chỉ quản trị viên hoặc nhân viên được phép thực hiện."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tạo y tá thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu yêu cầu không hợp lệ hoặc số điện thoại đã tồn tại"),
            @ApiResponse(responseCode = "403", description = "Không có quyền tạo y tá"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy chuyên khoa với ID được cung cấp")
    })
    public ResponseData<NurseResponse> createNurse(@Valid @RequestBody NurseRequest request) {
        log.info("Yêu cầu tạo y tá mới: {}", request);
        return ResponseData.<NurseResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Tạo y tá thành công")
                .data(nurseService.createNurse(request))
                .build();
    }

    @PutMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'NURSE')")
    @Operation(
            summary = "Cập nhật thông tin y tá",
            description = "Cập nhật thông tin của một y tá dựa trên ID. Y tá chỉ có thể cập nhật thông tin của chính họ."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cập nhật y tá thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu yêu cầu không hợp lệ hoặc số điện thoại đã tồn tại"),
            @ApiResponse(responseCode = "403", description = "Không có quyền cập nhật thông tin y tá"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy y tá hoặc chuyên khoa với ID được cung cấp")
    })
    public ResponseData<NurseResponse> updateNurse(
            @Parameter(description = "ID của y tá cần cập nhật") @PathVariable("id") Long id,
            @Valid @RequestBody NurseRequest request) {
        log.info("Yêu cầu cập nhật y tá với ID: {}", id);
//        // Kiểm tra nếu người dùng là NURSE, chỉ cho phép cập nhật thông tin của chính họ
//        if (SecurityUtil.hasRole("NURSE") && !SecurityUtil.getCurrentUserId().equals(id)) {
//            log.warn("Người dùng với ID {} không có quyền cập nhật thông tin y tá ID: {}", SecurityUtil.getCurrentUserId(), id);
//            throw new SecurityException("Không có quyền cập nhật thông tin y tá này");
//        }
        return ResponseData.<NurseResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Cập nhật y tá thành công")
                .data(nurseService.updateNurse(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Xóa y tá",
            description = "Xóa một y tá và tài khoản liên quan dựa trên ID. Chỉ quản trị viên được phép thực hiện."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Xóa y tá thành công"),
            @ApiResponse(responseCode = "403", description = "Không có quyền xóa y tá"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy y tá hoặc tài khoản với ID được cung cấp")
    })
    public ResponseData<Void> deleteNurse(
            @Parameter(description = "ID của y tá cần xóa") @PathVariable("id") Long id) {
        log.info("Yêu cầu xóa y tá với ID: {}", id);
        nurseService.deleteNurse(id);
        return ResponseData.<Void>builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message("Xóa y tá thành công")
                .build();
    }

    @GetMapping
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE')")
    @Operation(
            summary = "Lấy danh sách tất cả y tá",
            description = "Truy xuất danh sách y tá phân trang, hỗ trợ sắp xếp theo các tiêu chí và thứ tự."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy danh sách y tá thành công"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập danh sách y tá")
    })
    public ResponseData<PageResponse<NurseResponse>> getAllNurses(
            @Parameter(description = "Số trang, bắt đầu từ 1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Số lượng y tá mỗi trang") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Trường để sắp xếp (ví dụ: id, name)") @RequestParam(defaultValue = "id") String sort,
            @Parameter(description = "Hướng sắp xếp: asc (tăng dần) hoặc desc (giảm dần)") @RequestParam(defaultValue = "asc") String direction) {
        log.info("Yêu cầu lấy danh sách y tá: trang={}, kích thước={}, sắp xếp={}, hướng={}", page, size, sort, direction);
        return ResponseData.<PageResponse<NurseResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy danh sách y tá thành công")
                .data(nurseService.getAllNurses(page, size, sort, direction))
                .build();
    }

    @GetMapping("/search")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE')")
    @Operation(
            summary = "Tìm kiếm y tá",
            description = "Tìm kiếm y tá theo từ khóa (tên, số điện thoại) với phân trang."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tìm kiếm y tá thành công"),
            @ApiResponse(responseCode = "400", description = "Từ khóa tìm kiếm không hợp lệ"),
            @ApiResponse(responseCode = "403", description = "Không có quyền tìm kiếm y tá")
    })
    public ResponseData<PageResponse<NurseResponse>> searchNurses(
            @Parameter(description = "Từ khóa tìm kiếm (tên, số điện thoại)") @RequestParam(required = false) String keyword,
            @Parameter(description = "Số trang, bắt đầu từ 1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Số lượng y tá mỗi trang") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Trường để sắp xếp (ví dụ: id, name)") @RequestParam(defaultValue = "id") String sort,
            @Parameter(description = "Hướng sắp xếp: asc (tăng dần) hoặc desc (giảm dần)") @RequestParam(defaultValue = "asc") String direction) {
        String searchKeyword = (keyword != null && !keyword.trim().isEmpty()) ? keyword.trim() : null;
        log.info("Yêu cầu tìm kiếm y tá với từ khóa: '{}', trang={}, kích thước={}, sắp xếp={}, hướng={}", searchKeyword, page, size, sort, direction);
        return ResponseData.<PageResponse<NurseResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Tìm kiếm y tá thành công")
                .data(nurseService.searchNurses(searchKeyword, page, size, sort, direction))
                .build();
    }

    @GetMapping("/by-medical-specialty/{medicalSpecialtyId}")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE')")
    @Operation(
            summary = "Lấy danh sách y tá theo chuyên khoa",
            description = "Truy xuất danh sách y tá thuộc một chuyên khoa cụ thể, hỗ trợ phân trang và sắp xếp."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy danh sách y tá theo chuyên khoa thành công"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập danh sách y tá"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy chuyên khoa với ID được cung cấp")
    })
    public ResponseData<PageResponse<NurseResponse>> getNursesByMedicalSpecialtyId(
            @Parameter(description = "ID của chuyên khoa") @PathVariable("medicalSpecialtyId") Integer medicalSpecialtyId,
            @Parameter(description = "Số trang, bắt đầu từ 1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Số lượng y tá mỗi trang") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Trường để sắp xếp (ví dụ: id, name)") @RequestParam(defaultValue = "id") String sort,
            @Parameter(description = "Hướng sắp xếp: asc (tăng dần) hoặc desc (giảm dần)") @RequestParam(defaultValue = "asc") String direction) {
        log.info("Yêu cầu lấy danh sách y tá cho chuyên khoa ID={}: trang={}, kích thước={}, sắp xếp={}, hướng={}",
                medicalSpecialtyId, page, size, sort, direction);
        return ResponseData.<PageResponse<NurseResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy danh sách y tá theo chuyên khoa thành công")
                .data(nurseService.getNursesByMedicalSpecialtyId(medicalSpecialtyId, page, size, sort, direction))
                .build();
    }

    @GetMapping("/search-by-medical-specialty/{medicalSpecialtyId}")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE')")
    @Operation(
            summary = "Tìm kiếm y tá theo chuyên khoa",
            description = "Tìm kiếm y tá thuộc một chuyên khoa cụ thể theo từ khóa (tên, số điện thoại) với phân trang."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tìm kiếm y tá theo chuyên khoa thành công"),
            @ApiResponse(responseCode = "400", description = "Từ khóa tìm kiếm không hợp lệ"),
            @ApiResponse(responseCode = "403", description = "Không có quyền tìm kiếm y tá"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy chuyên khoa với ID được cung cấp")
    })
    public ResponseData<PageResponse<NurseResponse>> searchNursesByMedicalSpecialtyId(
            @Parameter(description = "ID của chuyên khoa") @PathVariable("medicalSpecialtyId") Integer medicalSpecialtyId,
            @Parameter(description = "Từ khóa tìm kiếm (tên, số điện thoại)") @RequestParam(required = false) String keyword,
            @Parameter(description = "Số trang, bắt đầu từ 1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Số lượng y tá mỗi trang") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Trường để sắp xếp (ví dụ: id, name)") @RequestParam(defaultValue = "id") String sort,
            @Parameter(description = "Hướng sắp xếp: asc (tăng dần) hoặc desc (giảm dần)") @RequestParam(defaultValue = "asc") String direction) {
        String searchKeyword = (keyword != null && !keyword.trim().isEmpty()) ? keyword.trim() : null;
        log.info("Yêu cầu tìm kiếm y tá cho chuyên khoa ID={} với từ khóa: '{}', trang={}, kích thước={}, sắp xếp={}, hướng={}",
                medicalSpecialtyId, searchKeyword, page, size, sort, direction);
        return ResponseData.<PageResponse<NurseResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Tìm kiếm y tá theo chuyên khoa thành công")
                .data(nurseService.searchNursesByMedicalSpecialtyId(medicalSpecialtyId, searchKeyword, page, size, sort, direction))
                .build();
    }
}
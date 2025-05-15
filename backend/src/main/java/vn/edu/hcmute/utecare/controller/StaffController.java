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
import vn.edu.hcmute.utecare.dto.request.StaffRequest;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.dto.response.ResponseData;
import vn.edu.hcmute.utecare.dto.response.StaffResponse;
import vn.edu.hcmute.utecare.service.StaffService;

@RestController
@RequestMapping("/api/v1/staff")
@RequiredArgsConstructor
@Tag(name = "STAFF", description = "API quản lý thông tin nhân viên trong hệ thống y tế")
@Slf4j(topic = "STAFF_CONTROLLER")
public class StaffController {
    private final StaffService staffService;

    @GetMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @Operation(
            summary = "Lấy thông tin nhân viên theo ID",
            description = "Truy xuất thông tin chi tiết của một nhân viên dựa trên ID duy nhất."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy thông tin nhân viên thành công"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy nhân viên với ID được cung cấp")
    })
    public ResponseData<StaffResponse> getStaffById(
            @Parameter(description = "ID của nhân viên cần truy xuất") @PathVariable("id") Long id) {
        log.info("Yêu cầu lấy thông tin nhân viên với ID: {}", id);
        return ResponseData.<StaffResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy thông tin nhân viên thành công")
                .data(staffService.getStaffById(id))
                .build();
    }

    @PostMapping
//    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Tạo nhân viên mới",
            description = "Tạo một nhân viên mới với thông tin chi tiết và tài khoản liên quan được cung cấp."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tạo nhân viên thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu yêu cầu không hợp lệ hoặc số điện thoại đã tồn tại")
    })
    public ResponseData<StaffResponse> createStaff(@Valid @RequestBody StaffRequest request) {
        log.info("Yêu cầu tạo nhân viên mới: {}", request);
        return ResponseData.<StaffResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Tạo nhân viên thành công")
                .data(staffService.createStaff(request))
                .build();
    }

    @PutMapping("/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Cập nhật thông tin nhân viên",
            description = "Cập nhật thông tin của một nhân viên hiện có dựa trên ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cập nhật nhân viên thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu yêu cầu không hợp lệ hoặc số điện thoại đã tồn tại"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy nhân viên với ID được cung cấp")
    })
    public ResponseData<StaffResponse> updateStaff(
            @Parameter(description = "ID của nhân viên cần cập nhật") @PathVariable("id") Long id,
            @Valid @RequestBody StaffRequest request) {
        log.info("Yêu cầu cập nhật nhân viên với ID: {}", id);
        return ResponseData.<StaffResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Cập nhật nhân viên thành công")
                .data(staffService.updateStaff(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Xóa nhân viên",
            description = "Xóa một nhân viên hiện có dựa trên ID, bao gồm tài khoản liên quan."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Xóa nhân viên thành công"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy nhân viên hoặc tài khoản với ID được cung cấp")
    })
    public ResponseData<Void> deleteStaff(
            @Parameter(description = "ID của nhân viên cần xóa") @PathVariable("id") Long id) {
        log.info("Yêu cầu xóa nhân viên với ID: {}", id);
        staffService.deleteStaff(id);
        return ResponseData.<Void>builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message("Xóa nhân viên thành công")
                .build();
    }

    @GetMapping
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @Operation(
            summary = "Lấy danh sách tất cả nhân viên",
            description = "Truy xuất danh sách tất cả nhân viên với phân trang và sắp xếp."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy danh sách nhân viên thành công")
    })
    public ResponseData<PageResponse<StaffResponse>> getAllStaff(
            @Parameter(description = "Số trang, bắt đầu từ 1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Số lượng bản ghi mỗi trang") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Trường để sắp xếp (ví dụ: id, name)") @RequestParam(defaultValue = "id") String sort,
            @Parameter(description = "Hướng sắp xếp: asc (tăng dần) hoặc desc (giảm dần)") @RequestParam(defaultValue = "asc") String direction) {
        log.info("Yêu cầu lấy danh sách nhân viên: trang={}, kích thước={}, sắp xếp={}, hướng={}", page, size, sort, direction);
        return ResponseData.<PageResponse<StaffResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy danh sách nhân viên thành công")
                .data(staffService.getAllStaff(page, size, sort, direction))
                .build();
    }

    @GetMapping("/search")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @Operation(
            summary = "Tìm kiếm nhân viên",
            description = "Tìm kiếm nhân viên theo từ khóa (ví dụ: tên, vai trò) với phân trang và sắp xếp."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tìm kiếm nhân viên thành công")
    })
    public ResponseData<PageResponse<StaffResponse>> searchStaff(
            @Parameter(description = "Từ khóa tìm kiếm (tên hoặc vai trò)") @RequestParam String keyword,
            @Parameter(description = "Số trang, bắt đầu từ 1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Số lượng bản ghi mỗi trang") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Trường để sắp xếp (ví dụ: id, name)") @RequestParam(defaultValue = "id") String sort,
            @Parameter(description = "Hướng sắp xếp: asc (tăng dần) hoặc desc (giảm dần)") @RequestParam(defaultValue = "asc") String direction) {
        log.info("Yêu cầu tìm kiếm nhân viên: từ khóa={}, trang={}, kích thước={}, sắp xếp={}, hướng={}", keyword, page, size, sort, direction);
        return ResponseData.<PageResponse<StaffResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Tìm kiếm nhân viên thành công")
                .data(staffService.searchStaff(keyword, page, size, sort, direction))
                .build();
    }
}
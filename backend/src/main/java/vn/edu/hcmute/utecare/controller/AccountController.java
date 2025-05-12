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
import vn.edu.hcmute.utecare.dto.request.AccountStatusUpdateRequest;
import vn.edu.hcmute.utecare.dto.response.AccountResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.dto.response.ResponseData;
import vn.edu.hcmute.utecare.service.AccountService;
import vn.edu.hcmute.utecare.util.SecurityUtil;
import vn.edu.hcmute.utecare.util.enumeration.AccountStatus;
import vn.edu.hcmute.utecare.util.enumeration.Role;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
@Tag(name = "ACCOUNT", description = "API quản lý thông tin và trạng thái tài khoản người dùng trong hệ thống")
@Slf4j(topic = "ACCOUNT_CONTROLLER")
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE', 'CUSTOMER')")
    @Operation(
            summary = "Lấy thông tin tài khoản theo ID",
            description = "Truy xuất thông tin chi tiết của một tài khoản dựa trên ID. Khách hàng chỉ có thể xem thông tin tài khoản của chính họ."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy thông tin tài khoản thành công"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập tài khoản"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy tài khoản với ID được cung cấp")
    })
    public ResponseData<AccountResponse> getAccountById(
            @Parameter(description = "ID của tài khoản cần truy xuất") @PathVariable("id") Long id) {
        log.info("Yêu cầu lấy thông tin tài khoản với ID: {}", id);
        return ResponseData.<AccountResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy thông tin tài khoản thành công")
                .data(accountService.getAccountById(id))
                .build();
    }

    @PatchMapping("/{id}/status")
//    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Cập nhật trạng thái tài khoản",
            description = "Cập nhật trạng thái (ví dụ: KÍCH HOẠT, KHÓA) của một tài khoản dựa trên ID. Chỉ quản trị viên được phép thực hiện."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cập nhật trạng thái tài khoản thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu yêu cầu không hợp lệ"),
            @ApiResponse(responseCode = "403", description = "Không có quyền cập nhật trạng thái"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy tài khoản với ID được cung cấp")
    })
    public ResponseData<AccountResponse> updateAccountStatus(
            @Parameter(description = "ID của tài khoản cần cập nhật trạng thái") @PathVariable("id") Long id,
            @Valid @RequestBody AccountStatusUpdateRequest request) {
        log.info("Yêu cầu cập nhật trạng thái tài khoản với ID: {} thành trạng thái: {}", id, request.getAccountStatus());
        return ResponseData.<AccountResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Cập nhật trạng thái tài khoản thành công")
                .data(accountService.updateAccountStatus(id, request))
                .build();
    }

    @GetMapping
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @Operation(
            summary = "Lấy danh sách tất cả tài khoản",
            description = "Truy xuất danh sách tài khoản phân trang, hỗ trợ sắp xếp theo các tiêu chí và thứ tự."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy danh sách tài khoản thành công"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập danh sách tài khoản")
    })
    public ResponseData<PageResponse<AccountResponse>> getAllAccounts(
            @Parameter(description = "Số trang, bắt đầu từ 1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Số lượng tài khoản mỗi trang") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Trường để sắp xếp (ví dụ: id, createdAt)") @RequestParam(defaultValue = "id") String sort,
            @Parameter(description = "Hướng sắp xếp: asc (tăng dần) hoặc desc (giảm dần)") @RequestParam(defaultValue = "asc") String direction) {
        log.info("Yêu cầu lấy danh sách tài khoản: trang={}, kích thước={}, sắp xếp={}, hướng={}", page, size, sort, direction);
        return ResponseData.<PageResponse<AccountResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy danh sách tài khoản thành công")
                .data(accountService.getAllAccounts(page, size, sort, direction))
                .build();
    }

    @GetMapping("/search")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @Operation(
            summary = "Tìm kiếm tài khoản",
            description = "Tìm kiếm tài khoản theo từ khóa (tên, số điện thoại), vai trò hoặc trạng thái, trả về danh sách phân trang với tùy chọn sắp xếp."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tìm kiếm tài khoản thành công"),
            @ApiResponse(responseCode = "403", description = "Không có quyền tìm kiếm tài khoản")
    })
    public ResponseData<PageResponse<AccountResponse>> searchAccounts(
            @Parameter(description = "Từ khóa tìm kiếm (tên, số điện thoại, tùy chọn)") @RequestParam(required = false) String keyword,
            @Parameter(description = "Vai trò của tài khoản (ví dụ: ADMIN, CUSTOMER, tùy chọn)") @RequestParam(required = false) Role role,
            @Parameter(description = "Trạng thái của tài khoản (ví dụ: ACTIVE, INACTIVE, tùy chọn)") @RequestParam(required = false) AccountStatus status,
            @Parameter(description = "Số trang, bắt đầu từ 1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Số lượng tài khoản mỗi trang") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Trường để sắp xếp (ví dụ: id, createdAt)") @RequestParam(defaultValue = "id") String sort,
            @Parameter(description = "Hướng sắp xếp: asc (tăng dần) hoặc desc (giảm dần)") @RequestParam(defaultValue = "asc") String direction) {
        log.info("Yêu cầu tìm kiếm tài khoản với từ khóa: '{}', vai trò: '{}', trạng thái: '{}', trang={}, kích thước={}, sắp xếp={}, hướng={}",
                keyword, role, status, page, size, sort, direction);
        return ResponseData.<PageResponse<AccountResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Tìm kiếm tài khoản thành công")
                .data(accountService.searchAccounts(keyword, role, status, page, size, sort, direction))
                .build();
    }
}
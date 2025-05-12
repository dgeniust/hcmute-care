package vn.edu.hcmute.utecare.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmute.utecare.dto.response.NotificationResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.dto.response.ResponseData;
import vn.edu.hcmute.utecare.dto.response.UserSummaryResponse;
import vn.edu.hcmute.utecare.service.NotificationService;
import vn.edu.hcmute.utecare.service.UserService;
import vn.edu.hcmute.utecare.util.enumeration.Role;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "USER", description = "API quản lý người dùng")
@Slf4j(topic = "USER_CONTROLLER")
public class UserController {
    private final UserService userService;
    private final NotificationService notificationService;

    @GetMapping
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @Operation(
            summary = "Lấy danh sách người dùng",
            description = "Tìm kiếm và phân trang danh sách người dùng theo từ khóa và vai trò."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy danh sách người dùng thành công"),
            @ApiResponse(responseCode = "400", description = "Tham số tìm kiếm, phân trang hoặc sắp xếp không hợp lệ"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập")
    })
    public ResponseData<PageResponse<UserSummaryResponse>> getAllUsers(
            @Parameter(description = "Từ khóa tìm kiếm theo tên, email hoặc số điện thoại", example = "Nguyen Van A")
            @RequestParam(required = false) String keyword,

            @Parameter(description = "Vai trò của người dùng", example = "ADMIN")
            @RequestParam(required = false) Role role,

            @Parameter(description = "Trang hiện tại (bắt đầu từ 1)", example = "1")
            @RequestParam(defaultValue = "1") int page,

            @Parameter(description = "Số lượng bản ghi mỗi trang", example = "10")
            @RequestParam(defaultValue = "10") int size,

            @Parameter(description = "Trường sắp xếp", example = "id")
            @RequestParam(defaultValue = "id") String sort,

            @Parameter(description = "Thứ tự sắp xếp (asc hoặc desc)", example = "asc")
            @RequestParam(defaultValue = "asc") String direction) {

        log.info("Tìm kiếm người dùng với từ khóa: {}", keyword);
        return ResponseData.<PageResponse<UserSummaryResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy danh sách người dùng thành công")
                .data(userService.findAll(keyword, role, page, size, sort, direction))
                .build();
    }

    @GetMapping("/{id}/notifications")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE', 'CUSTOMER')")
    @Operation(
            summary = "Lấy danh sách thông báo của người dùng",
            description = "Phân trang danh sách thông báo của người dùng theo ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy danh sách thông báo thành công"),
            @ApiResponse(responseCode = "400", description = "Tham số phân trang hoặc sắp xếp không hợp lệ"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy người dùng với ID được cung cấp")
    })
    public ResponseData<PageResponse<NotificationResponse>> getUserNotifications(
            @Parameter(description = "ID người dùng", example = "123")
            @PathVariable("id") Long userId,

            @Parameter(description = "Trang hiện tại (bắt đầu từ 1)", example = "1")
            @RequestParam(defaultValue = "1") int page,

            @Parameter(description = "Số lượng bản ghi mỗi trang", example = "10")
            @RequestParam(defaultValue = "10") int size,

            @Parameter(description = "Trường sắp xếp", example = "createdAt")
            @RequestParam(defaultValue = "createdAt") String sort,

            @Parameter(description = "Thứ tự sắp xếp (asc hoặc desc)", example = "asc")
            @RequestParam(defaultValue = "asc") String direction) {

        log.info("Lấy danh sách thông báo người dùng: userId={}, page={}, size={}, sort={}, direction={}", userId, page, size, sort, direction);
        PageResponse<NotificationResponse> notifications = notificationService.getUserNotifications(page, size, sort, direction, userId);
        return ResponseData.<PageResponse<NotificationResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy danh sách thông báo thành công")
                .data(notifications)
                .build();
    }
}
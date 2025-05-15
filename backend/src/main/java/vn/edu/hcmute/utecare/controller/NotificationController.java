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
import vn.edu.hcmute.utecare.dto.request.NotificationRequest;
import vn.edu.hcmute.utecare.dto.response.NotificationResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.dto.response.ResponseData;
import vn.edu.hcmute.utecare.service.NotificationService;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@Tag(name = "NOTIFICATION", description = "API quản lý thông báo trong hệ thống y tế")
@Slf4j(topic = "NOTIFICATION_CONTROLLER")
public class NotificationController {
    private final NotificationService notificationService;

    @PostMapping("/private")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @Operation(
            summary = "Gửi thông báo riêng",
            description = "Gửi một thông báo riêng tới một người dùng dựa trên số điện thoại (username)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Gửi thông báo riêng thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu yêu cầu không hợp lệ")
    })
    public ResponseData<Void> sendPrivateNotification(
            @Parameter(description = "Số điện thoại của người nhận (username)") @RequestParam String username,
            @Parameter(description = "Nội dung thông báo") @RequestParam String message) {
        log.info("Yêu cầu gửi thông báo riêng cho người dùng: {}", username);
        notificationService.sendPrivateNotification(username, message);
        return ResponseData.<Void>builder()
                .status(HttpStatus.OK.value())
                .message("Gửi thông báo riêng thành công")
                .build();
    }

    @PostMapping("/public")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @Operation(
            summary = "Gửi thông báo công khai",
            description = "Gửi một thông báo công khai tới tất cả người dùng trong hệ thống."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Gửi thông báo công khai thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu yêu cầu không hợp lệ")
    })
    public ResponseData<Void> sendPublicNotification(
            @Parameter(description = "Nội dung thông báo công khai") @RequestParam String message) {
        log.info("Yêu cầu gửi thông báo công khai: {}", message);
        notificationService.sendPublicNotification(message);
        return ResponseData.<Void>builder()
                .status(HttpStatus.OK.value())
                .message("Gửi thông báo công khai thành công")
                .build();
    }

    @PostMapping
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @Operation(
            summary = "Gửi thông báo tùy chỉnh",
            description = "Gửi thông báo tùy chỉnh (riêng tư hoặc công khai) dựa trên thông tin yêu cầu. Nếu có userId, thông báo sẽ được gửi riêng cho người dùng đó."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Gửi thông báo thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu yêu cầu không hợp lệ"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy người dùng với ID được cung cấp")
    })
    public ResponseData<Void> sendNotification(@Valid @RequestBody NotificationRequest request) {
        log.info("Yêu cầu gửi thông báo tùy chỉnh: {}", request);
        notificationService.sendNotification(request);
        return ResponseData.<Void>builder()
                .status(HttpStatus.OK.value())
                .message("Gửi thông báo thành công")
                .build();
    }

    @GetMapping
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'CUSTOMER', 'DOCTOR', 'NURSE')")
    @Operation(
            summary = "Lấy danh sách thông báo",
            description = "Truy xuất danh sách thông báo với phân trang, sắp xếp và lọc theo số điện thoại của người dùng (tùy chọn)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy danh sách thông báo thành công")
    })
    public ResponseData<PageResponse<NotificationResponse>> getAllNotifications(
            @Parameter(description = "Số trang, bắt đầu từ 1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Số lượng bản ghi mỗi trang") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Trường để sắp xếp (mặc định: createdAt)") @RequestParam(defaultValue = "createdAt") String sort,
            @Parameter(description = "Hướng sắp xếp: asc hoặc desc") @RequestParam(defaultValue = "desc") String direction,
            @Parameter(description = "Số điện thoại của người dùng (tùy chọn)") @RequestParam(defaultValue = "") String phone) {
        log.info("Yêu cầu lấy danh sách thông báo: trang={}, kích thước={}, sắp xếp={}, hướng={}, số điện thoại={}", page, size, sort, direction, phone);
        return ResponseData.<PageResponse<NotificationResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy danh sách thông báo thành công")
                .data(notificationService.getNotifications(page, size, sort, direction, phone))
                .build();
    }
}
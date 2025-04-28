package vn.edu.hcmute.utecare.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmute.utecare.dto.request.NotificationRequest;
import vn.edu.hcmute.utecare.dto.response.NotificationResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.dto.response.ResponseData;
import vn.edu.hcmute.utecare.service.NotificationService;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@Tag(name = "Notification", description = "Notification API")
@Slf4j(topic = "NOTIFICATION_CONTROLLER")
public class NotificationController {
    private final NotificationService notificationService;

    @PostMapping("/private")
    public void sendPrivateNotification(@RequestParam String username,@RequestParam String message) {
        log.info("Sending private notification to user: {}", username);
        notificationService.sendPrivateNotification(username, message);
    }

    @PostMapping("/public")
    public void sendPublicNotification(@RequestParam String message) {
        log.info("Sending public notification: {}", message);
        notificationService.sendPublicNotification(message);
    }

    @PostMapping
    public ResponseData<Void> sendNotification(@RequestBody @Valid NotificationRequest request) {
        log.info("Sending notification: {}", request);
        notificationService.sendNotification(request);
        return ResponseData.<Void>builder()
                .status(200)
                .message("Notification sent successfully")
                .build();
    }

    @GetMapping
    public ResponseData<PageResponse<NotificationResponse>> getAllNotifications(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(defaultValue = "") String phone) {
        log.info("Fetching notifications: page={}, size={}, sort={}, direction={}, phone={}", page, size, sort, direction, phone);
        PageResponse<NotificationResponse> notifications = notificationService.getNotifications(page, size, sort, direction, phone);
        return ResponseData.<PageResponse<NotificationResponse>>builder()
                .status(200)
                .message("Notifications fetched successfully")
                .data(notifications)
                .build();
    }
}

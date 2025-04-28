package vn.edu.hcmute.utecare.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import vn.edu.hcmute.utecare.dto.request.NotificationRequest;
import vn.edu.hcmute.utecare.dto.response.NotificationResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.exception.ResourceNotFoundException;
import vn.edu.hcmute.utecare.mapper.NotificationMapper;
import vn.edu.hcmute.utecare.model.Notification;
import vn.edu.hcmute.utecare.model.User;
import vn.edu.hcmute.utecare.repository.NotificationRepository;
import vn.edu.hcmute.utecare.repository.UserRepository;
import vn.edu.hcmute.utecare.service.NotificationService;
import vn.edu.hcmute.utecare.util.PaginationUtil;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final UserRepository userRepository;

    @Override
    public void sendPrivateNotification(String username, String message) {
        log.info("Sending private notification to user {}: {}", username, message);
        messagingTemplate.convertAndSendToUser(username, "/queue/notifications", message);
    }

    @Override
    public void sendPublicNotification(String message) {
        Notification notification = new Notification();
        notification.setContent(message);
        notification.setTitle("Notification");
        notification.setCreatedAt(LocalDateTime.now());
        notification.setUpdatedAt(LocalDateTime.now());
        log.info("Sending public notification: {}", message);
        messagingTemplate.convertAndSend("/topic/notifications", notification);
    }

    @Override
    public void sendNotification(NotificationRequest request) {
        log.info("Sending notification: {}", request);
        Notification notification = new Notification();
        if (request.getUserId() == null)
        {
            notification = NotificationMapper.INSTANCE.toEntity(request);
            messagingTemplate.convertAndSend("/topic/notifications", notification);
        } else {
            User user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            notification = NotificationMapper.INSTANCE.toEntity(request);
            messagingTemplate.convertAndSendToUser(user.getPhone(), "/queue/notifications", notification);
            notification.setUser(user);
        }
        notificationRepository.save(notification);
    }

    @Override
    public PageResponse<NotificationResponse> getNotifications(int page, int size, String sort, String direction, String phone) {
        log.info("Fetching notifications: page={}, size={}, sort={}, direction={}", page, size, sort, direction);

        Pageable pageable = PaginationUtil.createPageable(page, size, sort, direction);

        Page<Notification> notifications = notificationRepository.findByUser_Phone(phone, pageable);

        return PageResponse.<NotificationResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(notifications.getTotalPages())
                .totalElements(notifications.getTotalElements())
                .content(notifications.getContent().stream()
                        .map(NotificationMapper.INSTANCE::toResponse)
                        .toList())
                .build();
    }

    @Override
    public PageResponse<NotificationResponse> getUserNotifications(int page, int size, String sort, String direction, Long userId) {
        log.info("Fetching user notifications: page={}, size={}, sort={}, direction={}", page, size, sort, direction);

        Pageable pageable = PaginationUtil.createPageable(page, size, sort, direction);

        Page<Notification> notifications = notificationRepository.findByUser_IdOrUser_IdNull(userId, pageable);

        return PageResponse.<NotificationResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(notifications.getTotalPages())
                .totalElements(notifications.getTotalElements())
                .content(notifications.getContent().stream()
                        .map(NotificationMapper.INSTANCE::toResponse)
                        .toList())
                .build();
    }
}

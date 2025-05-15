package vn.edu.hcmute.utecare.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    private final NotificationMapper notificationMapper;
    private final SimpMessagingTemplate messagingTemplate;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void sendPrivateNotification(String username, String message) {
        log.info("Gửi thông báo riêng cho người dùng với số điện thoại: {}", username);
        Notification notification = Notification.builder()
                .title("Thông báo riêng")
                .content(message)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        User user = userRepository.findByPhone(username)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng với số điện thoại: " + username));
        notification.setUser(user);
        notificationRepository.save(notification);
        messagingTemplate.convertAndSendToUser(username, "/queue/notifications", notification);
        log.info("Gửi thông báo riêng thành công cho người dùng: {}", username);
    }

    @Override
    @Transactional
    public void sendPublicNotification(String message) {
        log.info("Gửi thông báo công khai: {}", message);
        Notification notification = Notification.builder()
                .title("Thông báo công khai")
                .content(message)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        notificationRepository.save(notification);
        messagingTemplate.convertAndSend("/topic/notifications", notification);
        log.info("Gửi thông báo công khai thành công");
    }

    @Override
    @Transactional
    public void sendNotification(NotificationRequest request) {
        log.info("Gửi thông báo tùy chỉnh: {}", request);
        Notification notification = notificationMapper.toEntity(request);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setUpdatedAt(LocalDateTime.now());

        if (request.getUserId() != null) {
            User user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng với ID: " + request.getUserId()));
            notification.setUser(user);
            messagingTemplate.convertAndSendToUser(user.getPhone(), "/queue/notifications", notification);
            log.info("Gửi thông báo riêng thành công cho người dùng với ID: {}", request.getUserId());
        } else {
            messagingTemplate.convertAndSend("/topic/notifications", notification);
            log.info("Gửi thông báo công khai thành công");
        }

        notificationRepository.save(notification);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<NotificationResponse> getNotifications(int page, int size, String sort, String direction, String phone) {
        log.info("Truy xuất danh sách thông báo theo số điện thoại: trang={}, kích thước={}, sắp xếp={}, hướng={}, số điện thoại={}",
                page, size, sort, direction, phone);
        Pageable pageable = PaginationUtil.createPageable(page, size, sort, direction);
        Page<Notification> notifications = notificationRepository.findByUser_PhoneOrUserIsNull(phone, pageable);
        return PageResponse.<NotificationResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(notifications.getTotalPages())
                .totalElements(notifications.getTotalElements())
                .content(notifications.getContent().stream()
                        .map(notificationMapper::toResponse)
                        .toList())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<NotificationResponse> getUserNotifications(int page, int size, String sort, String direction, Long userId) {
        log.info("Truy xuất danh sách thông báo của người dùng: trang={}, kích thước={}, sắp xếp={}, hướng={}, userId={}",
                page, size, sort, direction, userId);
        Pageable pageable = PaginationUtil.createPageable(page, size, sort, direction);
        Page<Notification> notifications = notificationRepository.findByUser_IdOrUser_IdNull(userId, pageable);
        return PageResponse.<NotificationResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(notifications.getTotalPages())
                .totalElements(notifications.getTotalElements())
                .content(notifications.getContent().stream()
                        .map(notificationMapper::toResponse)
                        .toList())
                .build();
    }
}
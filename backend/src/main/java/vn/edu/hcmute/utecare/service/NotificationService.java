package vn.edu.hcmute.utecare.service;

import vn.edu.hcmute.utecare.dto.request.NotificationRequest;
import vn.edu.hcmute.utecare.dto.response.NotificationResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;

public interface NotificationService {
    void sendPrivateNotification(String username, String message);

    void sendPublicNotification(String message);

    void sendNotification(NotificationRequest request);

    PageResponse<NotificationResponse> getNotifications(int page, int size, String sort, String direction, String phone);

    PageResponse<NotificationResponse> getUserNotifications(int page, int size, String sort, String direction, Long UserId);
}

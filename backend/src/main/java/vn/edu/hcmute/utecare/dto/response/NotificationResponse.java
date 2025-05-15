package vn.edu.hcmute.utecare.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationResponse {
    private Long id;
    private String title;
    private String content;
    private boolean isRead;
    private String type;
    private String createdAt;
    private String updatedAt;
}

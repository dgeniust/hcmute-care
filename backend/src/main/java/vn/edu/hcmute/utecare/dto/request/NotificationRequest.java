package vn.edu.hcmute.utecare.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import vn.edu.hcmute.utecare.util.enumeration.NotificationType;
import vn.edu.hcmute.utecare.util.validator.EnumValue;

@Getter
public class NotificationRequest {
    @NotEmpty(message = "title must not be empty")
    private String title;
    @NotEmpty(message = "content must not be empty")
    private String content;

    private Long userId;

    @EnumValue(name = "type", enumClass = NotificationType.class)
    private String type;
}

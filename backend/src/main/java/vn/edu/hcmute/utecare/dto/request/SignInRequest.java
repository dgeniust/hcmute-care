package vn.edu.hcmute.utecare.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import vn.edu.hcmute.utecare.util.EnumValue;
import vn.edu.hcmute.utecare.util.PhoneNumber;
import vn.edu.hcmute.utecare.util.Platform;

@Getter
public class SignInRequest {
    @PhoneNumber(message = "phone invalid format")
    private String phone;

    @NotBlank(message = "password must be not null")
    private String password;

    @EnumValue(name = "platform", enumClass = Platform.class)
    private String platform;

    private String deviceToken;
}

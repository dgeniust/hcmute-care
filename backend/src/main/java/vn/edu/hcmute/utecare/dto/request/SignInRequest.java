package vn.edu.hcmute.utecare.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import vn.edu.hcmute.utecare.util.validator.EnumValue;
import vn.edu.hcmute.utecare.util.validator.PhoneNumber;
import vn.edu.hcmute.utecare.util.enumeration.Platform;

@Getter
public class SignInRequest {
    @PhoneNumber(message = "phone invalid format")
    private String phone;

    @NotBlank(message = "password must be not null")
    private String password;
}

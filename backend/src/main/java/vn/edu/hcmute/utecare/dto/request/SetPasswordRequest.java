package vn.edu.hcmute.utecare.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class SetPasswordRequest {

    @NotBlank(message = "password must not be blank")
    private String password;

    @NotBlank(message = "confirm password must not be blank")
    private String confirmPassword;
}

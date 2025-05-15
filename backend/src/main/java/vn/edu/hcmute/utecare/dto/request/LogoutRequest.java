package vn.edu.hcmute.utecare.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LogoutRequest {
    @NotBlank(message = "access token must not be blank")
    String accessToken;

    @NotBlank(message = "refresh token must not be blank")
    String refreshToken;
}

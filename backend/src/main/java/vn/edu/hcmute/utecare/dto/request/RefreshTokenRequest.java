package vn.edu.hcmute.utecare.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class RefreshTokenRequest {
    @NotBlank(message = "refresh token must not be blank")
    private String refreshToken;
}

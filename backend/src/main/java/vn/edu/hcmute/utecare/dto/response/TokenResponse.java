package vn.edu.hcmute.utecare.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TokenResponse {
    private String accessToken;
    private String refreshToken;
    private Long userId;
}

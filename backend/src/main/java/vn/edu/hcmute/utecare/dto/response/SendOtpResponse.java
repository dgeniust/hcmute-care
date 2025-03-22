package vn.edu.hcmute.utecare.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SendOtpResponse {
    private String phone;
    private Integer otpExpiry;
}

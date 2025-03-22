package vn.edu.hcmute.utecare.dto.request;

import lombok.Getter;
import vn.edu.hcmute.utecare.util.PhoneNumber;

@Getter
public class VerifyOtpRequest {
    @PhoneNumber(message = "phone must be valid")
    private String phone;
    private String otp;
}

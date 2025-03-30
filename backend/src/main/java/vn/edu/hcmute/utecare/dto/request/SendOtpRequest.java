package vn.edu.hcmute.utecare.dto.request;

import lombok.Getter;
import vn.edu.hcmute.utecare.util.validator.PhoneNumber;

@Getter
public class SendOtpRequest {
    @PhoneNumber(message = "phone invalid format")
    private String phone;
}

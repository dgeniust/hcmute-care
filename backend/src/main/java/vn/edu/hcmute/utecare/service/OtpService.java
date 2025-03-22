package vn.edu.hcmute.utecare.service;

import vn.edu.hcmute.utecare.dto.request.VerifyOtpRequest;
import vn.edu.hcmute.utecare.dto.response.SendOtpResponse;
import vn.edu.hcmute.utecare.util.OtpType;

public interface OtpService {
    SendOtpResponse generateAndSendOtp(String phoneNumber, OtpType otpType);

    boolean verifyOtp(VerifyOtpRequest request, OtpType otpType);
}

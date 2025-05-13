package vn.edu.hcmute.utecare.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.edu.hcmute.utecare.dto.request.VerifyOtpRequest;
import vn.edu.hcmute.utecare.dto.response.SendOtpResponse;
import vn.edu.hcmute.utecare.service.OtpService;
import vn.edu.hcmute.utecare.service.RedisService;
import vn.edu.hcmute.utecare.service.SMSService;
import vn.edu.hcmute.utecare.util.enumeration.OtpType;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class OtpServiceImpl implements OtpService {
    private final RedisService redisService;
    private final SMSService smsService;
    private static final int OTP_EXPIRY_SECONDS = 300; // 5 phút

    @Override
    public SendOtpResponse generateAndSendOtp(String phoneNumber, OtpType otpType) {
        String otp = String.format("%06d", new java.security.SecureRandom().nextInt(999999));
        String otpKey = String.format("otp:%s:%s", otpType.name().toLowerCase(), phoneNumber);
        redisService.set(otpKey, otp, OTP_EXPIRY_SECONDS, TimeUnit.SECONDS);

        try {
            String smsMessage = String.format("Mã OTP của bạn là %s. Mã này có hiệu lực trong 5 phút.", otp);
            smsService.sendSms(phoneNumber, smsMessage);
            log.info("Sent OTP {} to {} for purpose {}", otp, phoneNumber, otpType);
        } catch (Exception e) {
            log.error("Failed to send OTP to {}: {}", phoneNumber, e.getMessage());
            throw new RuntimeException("Failed to send OTP: " + e.getMessage());
        }
        return SendOtpResponse.builder()
                .phone(phoneNumber)
                .otpExpiry(OTP_EXPIRY_SECONDS)
                .build();
    }

    @Override
    public boolean verifyOtp(VerifyOtpRequest request, OtpType otpType) {
        String otpKey = String.format("otp:%s:%s", otpType.name().toLowerCase(), request.getPhone());
        try {
            String storedOtp = (String) redisService.get(otpKey);
            if (storedOtp != null && storedOtp.equals(request.getOtp())) {
                redisService.delete(otpKey);
                return true;
            }
            return false;
        } catch (Exception e) {
            log.error("Error verifying OTP for phone {}: {}", request.getPhone(), e.getMessage());
            return false;
        }
    }
}
package vn.edu.hcmute.utecare.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.edu.hcmute.utecare.dto.request.VerifyOtpRequest;
import vn.edu.hcmute.utecare.dto.response.SendOtpResponse;
import vn.edu.hcmute.utecare.exception.TooManyOtpRequestsException;
import vn.edu.hcmute.utecare.service.OtpService;
import vn.edu.hcmute.utecare.service.RedisService;
import vn.edu.hcmute.utecare.util.enumeration.OtpType;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class OtpServiceImpl implements OtpService {
    private final RedisService redisService;
    private static final int OTP_EXPIRY_SECONDS = 300; // 5 phút
    private static final int LIMIT_EXPIRY_SECONDS = 3600; // 1 giờ
    private static final int MAX_ATTEMPTS = 3;

    @Override
    public SendOtpResponse generateAndSendOtp(String phoneNumber, OtpType otpType) {
        if (!canSendOtp(phoneNumber)) {
            Long remainingSeconds = redisService.getExpire("otp:limit:" + phoneNumber, TimeUnit.SECONDS);
            if (remainingSeconds == null || remainingSeconds <= 0) {
                remainingSeconds = (long) LIMIT_EXPIRY_SECONDS;
            }
            String message = String.format("Too many OTP requests. Please wait %d seconds.", remainingSeconds);
            throw new TooManyOtpRequestsException(message);
        }

        String otp = String.format("%06d", new java.security.SecureRandom().nextInt(999999));
        String otpKey = String.format("otp:%s:%s", otpType.name().toLowerCase(), phoneNumber);
        redisService.set(otpKey, otp, OTP_EXPIRY_SECONDS, TimeUnit.SECONDS);

        // Gửi OTP qua SMS (giả lập)
        log.info("Sending OTP {} to {} for purpose {}", otp, phoneNumber, otpType);

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

    private boolean canSendOtp(String phoneNumber) {
        String limitKey = "otp:limit:" + phoneNumber;
        String count = (String) redisService.get(limitKey);
        int attempts = count != null ? Integer.parseInt(count) : 0;

        if (attempts >= MAX_ATTEMPTS) {
            return false;
        }

        if (attempts == 0) {
            redisService.set(limitKey, "1", LIMIT_EXPIRY_SECONDS, TimeUnit.SECONDS);
        } else {
            redisService.increment(limitKey);
        }
        return true;
    }
}
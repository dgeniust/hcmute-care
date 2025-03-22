package vn.edu.hcmute.utecare.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import vn.edu.hcmute.utecare.dto.request.VerifyOtpRequest;
import vn.edu.hcmute.utecare.dto.response.SendOtpResponse;
import vn.edu.hcmute.utecare.exception.TooManyOtpRequestsException;
import vn.edu.hcmute.utecare.service.OtpService;
import vn.edu.hcmute.utecare.util.OtpType;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class OtpServiceImpl implements OtpService {
    private final StringRedisTemplate redisTemplate;
    private static final int OTP_EXPIRY_SECONDS = 300;
    private static final int LIMIT_EXPIRY_SECONDS = 3600;
    private static final int MAX_ATTEMPTS = 5;

    @Override
    public SendOtpResponse generateAndSendOtp(String phoneNumber, OtpType otpType) {
        if (!canSendOtp(phoneNumber)) {
            long remainingSeconds = redisTemplate.getExpire("otp:limit:" + phoneNumber, TimeUnit.SECONDS);
            if (remainingSeconds < 0) {
                remainingSeconds = (long) LIMIT_EXPIRY_SECONDS;
            }
            String message = String.format("Too many OTP requests. Please wait %d seconds.", remainingSeconds);
            throw new TooManyOtpRequestsException(message);
        }

        String otp = String.format("%06d", new java.security.SecureRandom().nextInt(999999));
        String otpKey = String.format("otp:%s:%s", otpType.name().toLowerCase(), phoneNumber); // Sử dụng OtpType trong key
        try {
            redisTemplate.opsForValue().set(otpKey, otp, OTP_EXPIRY_SECONDS, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("Failed to store OTP in Redis for {}: {}", phoneNumber, e.getMessage());
            throw new RuntimeException("Unable to generate OTP due to server error");
        }
        // Gửi OTP qua SMS (giả lập, thay bằng API thực tế)
        log.info("Sending OTP {} to {} for purpose {}", otp, phoneNumber, otpType);

        return SendOtpResponse.builder()
                .phone(phoneNumber)
                .otpExpiry(OTP_EXPIRY_SECONDS)
                .build();
    }

    @Override
    public boolean verifyOtp(VerifyOtpRequest request, OtpType otpType) {
        String otpKey = String.format("otp:%s:%s", otpType.name().toLowerCase(), request.getPhone()); // Sử dụng OtpType trong key
        String storedOtp = redisTemplate.opsForValue().get(otpKey);
        if (storedOtp != null && storedOtp.equals(request.getOtp())) {
            redisTemplate.delete(otpKey);
            return true;
        }
        return false;
    }

    private boolean canSendOtp(String phoneNumber) {
        String limitKey = "otp:limit:" + phoneNumber;
        String count = redisTemplate.opsForValue().get(limitKey);
        int attempts = count != null ? Integer.parseInt(count) : 0;

        if (attempts >= MAX_ATTEMPTS) {
            return false;
        }

        if (attempts == 0) {
            redisTemplate.opsForValue().set(limitKey, "1", LIMIT_EXPIRY_SECONDS, TimeUnit.SECONDS);
        } else {
            redisTemplate.opsForValue().increment(limitKey);
        }
        return true;
    }
}
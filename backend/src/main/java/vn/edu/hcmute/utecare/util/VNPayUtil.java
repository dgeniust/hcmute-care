package vn.edu.hcmute.utecare.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import vn.edu.hcmute.utecare.exception.ConflictException;

import java.net.URLEncoder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class VNPayUtil {
    public static String hmacSHA512(@NonNull String key, @NonNull String data) {
        try {
            Mac hmac512 = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            hmac512.init(secretKey);
            byte[] result = hmac512.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(result.length * 2);
            for (byte b : result) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new ConflictException("Failed to generate HMAC-SHA512 hash");
        }
    }

    public static String getIpAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null || ipAddress.isEmpty()) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress.split(",")[0].trim();
    }

    public static String getRandomNumber(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("Length must be positive");
        }
        Random random = new Random();
        String chars = "0123456789";
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    public static String getPaymentURL(@NonNull Map<String, String> paramsMap, boolean encodeKey) {
        return paramsMap.entrySet().stream()
                .filter(entry -> entry.getValue() != null && !entry.getValue().isEmpty())
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> {
                    String key = encodeKey ? URLEncoder.encode(entry.getKey(), StandardCharsets.US_ASCII)
                            : entry.getKey();
                    String value = URLEncoder.encode(entry.getValue(), StandardCharsets.US_ASCII);
                    return key + "=" + value;
                })
                .collect(Collectors.joining("&"));
    }
    public static LocalDateTime parseVNPayDate(String vnpPayDate) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            return LocalDateTime.parse(vnpPayDate, formatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Không thể phân tích ngày thanh toán VNPay: " + vnpPayDate, e);
        }
    }
}

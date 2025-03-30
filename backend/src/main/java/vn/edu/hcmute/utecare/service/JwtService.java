package vn.edu.hcmute.utecare.service;

import org.springframework.security.core.userdetails.UserDetails;
import vn.edu.hcmute.utecare.util.enumeration.TokenType;

public interface JwtService {
    String generateAccessToken(UserDetails user);

    String generateRefreshToken(UserDetails user);

    String generateVerificationToken(String phone);

    String generateResetToken(UserDetails user);

    String extractUsername(String token, TokenType type);

    boolean isValid(String token, TokenType type, UserDetails userDetails);

    boolean isTokenExpired(String token, TokenType type);

    long getRemainingTime(String token, TokenType type);
}

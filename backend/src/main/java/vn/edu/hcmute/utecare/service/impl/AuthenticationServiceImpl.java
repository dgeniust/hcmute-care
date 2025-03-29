package vn.edu.hcmute.utecare.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.edu.hcmute.utecare.dto.request.*;
import vn.edu.hcmute.utecare.dto.response.SendOtpResponse;
import vn.edu.hcmute.utecare.dto.response.TokenResponse;
import vn.edu.hcmute.utecare.dto.response.VerifyOtpResponse;
import vn.edu.hcmute.utecare.model.Account;
import vn.edu.hcmute.utecare.model.Customer;
import vn.edu.hcmute.utecare.repository.AccountRepository;
import vn.edu.hcmute.utecare.service.AuthenticationService;
import vn.edu.hcmute.utecare.service.JwtService;
import vn.edu.hcmute.utecare.service.OtpService;
import vn.edu.hcmute.utecare.service.RedisService;
import vn.edu.hcmute.utecare.util.OtpType;
import vn.edu.hcmute.utecare.util.TokenType;
import vn.edu.hcmute.utecare.util.UserStatus;

import java.util.concurrent.TimeUnit;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    private final JwtService jwtService;
    private final AccountRepository accountRepository;
    private final AuthenticationManager authenticationManager;
    private final OtpService otpService;
    private final PasswordEncoder passwordEncoder;
    private final RedisService redisService;

    private static final String AT_BLACKLIST_PREFIX = "bl_at:";
    private static final String RT_BLACKLIST_PREFIX = "bl_rt:";

    @Override
    public TokenResponse signIn(SignInRequest request) {
        log.info("Get access token");

        try {
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getPhone(), request.getPassword()));

            log.info("isAuthenticated = {}", authenticate.isAuthenticated());
            log.info("Authorities: {}", authenticate.getAuthorities().toString());

            SecurityContextHolder.getContext().setAuthentication(authenticate);
        } catch (BadCredentialsException | DisabledException e) {
            log.error("errorMessage: {}", e.getMessage());
            throw new AccessDeniedException(e.getMessage());
        }

        Account account = accountRepository.findByUser_Phone(request.getPhone()).orElseThrow(() -> new AccessDeniedException("Account not found"));


        if (account.getStatus() != UserStatus.ACTIVE) {
            throw new AccessDeniedException("Account is not active");
        }

        String accessToken = jwtService.generateAccessToken(account);
        String refreshToken = jwtService.generateRefreshToken(account);

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(account.getId())
                .build();
    }

    @Override
    public TokenResponse refreshToken(String refreshToken) {
        log.info("Get new access token");

        String phone = jwtService.extractUsername(refreshToken, TokenType.REFRESH_TOKEN);
        Account account = accountRepository.findByUser_Phone(phone).orElseThrow(() -> new AccessDeniedException("Account not found"));

        String storedRefreshToken = (String) redisService.get("refresh:" + account.getUser().getPhone());
        if (storedRefreshToken == null || !storedRefreshToken.equals(refreshToken)) {
            throw new AccessDeniedException("Invalid or revoked refresh token");
        }

        if (!jwtService.isValid(refreshToken, TokenType.REFRESH_TOKEN, account)) {
            throw new AccessDeniedException("Invalid refresh token");
        }

        String newAccessToken = jwtService.generateAccessToken(account);

        return TokenResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken)
                .userId(account.getId())
                .build();
    }

    @Override
    public SendOtpResponse sendOtpForRegistration(SendOtpRequest request) {
        String phoneNumber = request.getPhone();
        if (phoneNumber == null) {
            throw new IllegalArgumentException("Invalid phone number format");
        }

        if (accountRepository.findByUser_Phone(phoneNumber).isPresent()) {
            throw new IllegalArgumentException("Phone number already registered");
        }

        return otpService.generateAndSendOtp(phoneNumber, OtpType.REGISTER);
    }

    @Override
    public VerifyOtpResponse verifyOtpForRegistration(VerifyOtpRequest request) {
        if (otpService.verifyOtp(request, OtpType.REGISTER)) {
            String verificationToken = jwtService.generateVerificationToken(request.getPhone());
            return VerifyOtpResponse.builder()
                    .phone(request.getPhone())
                    .verificationToken(verificationToken)
                    .build();
        }
        throw new AccessDeniedException("Invalid or expired OTP");
    }
    @Override
    @Transactional
    public TokenResponse registerSetPassword(String verificationToken, SetPasswordRequest request) {
        String phone = "";
        try {
            phone = jwtService.extractUsername(verificationToken, TokenType.VERIFICATION_TOKEN);
        } catch (Exception e) {
            throw new AccessDeniedException("Invalid verification token");
        }


        if (jwtService.isTokenExpired(verificationToken, TokenType.VERIFICATION_TOKEN)) {
            throw new AccessDeniedException("Expired verification token");
        }


        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        if (request.getPassword().length() < 8 || !request.getPassword().matches(".*[A-Za-z].*") || !request.getPassword().matches(".*\\d.*")) {
            throw new IllegalArgumentException("Password must be at least 8 characters long and contain letters and numbers");
        }

        if (accountRepository.findByUser_Phone(phone).isPresent()) {
            throw new IllegalArgumentException("Phone number already registered");
        }

        Customer customer = new Customer();
        customer.setPhone(phone);

        Account account = new Account();
        account.setUser(customer);
        account.setPassword(passwordEncoder.encode(request.getPassword()));
        account.setStatus(UserStatus.ACTIVE);
        accountRepository.save(account);

        String accessToken = jwtService.generateAccessToken(account);
        String refreshToken = jwtService.generateRefreshToken(account);

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(account.getId())
                .build();
    }

    @Override
    public SendOtpResponse sendOtpForForgotPassword(SendOtpRequest request) {
        String phoneNumber = request.getPhone();
        if (phoneNumber == null) {
            throw new IllegalArgumentException("Invalid phone number format");
        }

        Account account = accountRepository.findByUser_Phone(phoneNumber)
                .orElseThrow(() -> new IllegalArgumentException("Phone number not registered"));

        if (account.getStatus() != UserStatus.ACTIVE) {
            throw new AccessDeniedException("Account is not active");
        }

        return otpService.generateAndSendOtp(phoneNumber, OtpType.FORGOT_PASSWORD);
    }

    @Override
    public VerifyOtpResponse verifyOtpForForgotPassword(VerifyOtpRequest request) {
        String phoneNumber = request.getPhone();
        if (phoneNumber == null) {
            throw new IllegalArgumentException("Invalid phone number format");
        }
        Account account = accountRepository.findByUser_Phone(phoneNumber)
                .orElseThrow(() -> new IllegalArgumentException("Phone number not registered"));
        if (otpService.verifyOtp(request, OtpType.FORGOT_PASSWORD)) {
            String verificationToken = jwtService.generateResetToken(account);
            return VerifyOtpResponse.builder()
                    .phone(request.getPhone())
                    .verificationToken(verificationToken)
                    .build();
        }
        throw new AccessDeniedException("Invalid or expired OTP");
    }

    @Override
    @Transactional
    public TokenResponse resetForgotPassword(String verificationToken, SetPasswordRequest request) {
        String phone;
        try {
            phone = jwtService.extractUsername(verificationToken, TokenType.RESET_TOKEN);
        } catch (Exception e) {
            throw new AccessDeniedException("Invalid verification token");
        }

        if (jwtService.isTokenExpired(verificationToken, TokenType.RESET_TOKEN)) {
            throw new AccessDeniedException("Expired verification token");
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        if (request.getPassword().length() < 8 || !request.getPassword().matches(".*[A-Za-z].*") || !request.getPassword().matches(".*\\d.*")) {
            throw new IllegalArgumentException("Password must be at least 8 characters long and contain letters and numbers");
        }

        Account account = accountRepository.findByUser_Phone(phone)
                .orElseThrow(() -> new IllegalArgumentException("Phone number not registered"));

        account.setPassword(passwordEncoder.encode(request.getPassword()));
        accountRepository.save(account);

        String accessToken = jwtService.generateAccessToken(account);
        String refreshToken = jwtService.generateRefreshToken(account);

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(account.getId())
                .build();
    }

    @Override
    public void logout(LogoutRequest request) {
        log.info("User logout request");

        String phone;
        try {
            phone = jwtService.extractUsername(request.getAccessToken(), TokenType.ACCESS_TOKEN);
        } catch (Exception e) {
            log.warn("Invalid access token provided for logout: {}", e.getMessage());
            throw new AccessDeniedException("Invalid access token");
        }
        long remainingATTime = jwtService.getRemainingTime(request.getAccessToken(), TokenType.ACCESS_TOKEN);
        if (remainingATTime > 0) {
            redisService.set(AT_BLACKLIST_PREFIX + phone, request.getAccessToken(), remainingATTime, TimeUnit.SECONDS);
            log.info("Stored Access Token in blacklist for phone {} with TTL {} seconds", phone, remainingATTime);
        } else {
            log.info("Access Token for phone {} has already expired, not stored in blacklist", phone);
        }

        long remainingRTTime = jwtService.getRemainingTime(request.getRefreshToken(), TokenType.REFRESH_TOKEN);
        if (remainingRTTime > 0) {
            redisService.set(RT_BLACKLIST_PREFIX + phone, request.getRefreshToken(), remainingRTTime, TimeUnit.SECONDS);
            log.info("Stored Refresh Token in blacklist for phone {} with TTL {} seconds", phone, remainingRTTime);
        } else {
            log.info("Refresh Token for phone {} has already expired, not stored in blacklist", phone);
        }
    }
}

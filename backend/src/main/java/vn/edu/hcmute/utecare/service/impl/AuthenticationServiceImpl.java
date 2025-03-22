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
import vn.edu.hcmute.utecare.dto.request.SendOtpRequest;
import vn.edu.hcmute.utecare.dto.request.SetPasswordRequest;
import vn.edu.hcmute.utecare.dto.request.SignInRequest;
import vn.edu.hcmute.utecare.dto.request.VerifyOtpRequest;
import vn.edu.hcmute.utecare.dto.response.SendOtpResponse;
import vn.edu.hcmute.utecare.dto.response.TokenResponse;
import vn.edu.hcmute.utecare.dto.response.VerifyOtpResponse;
import vn.edu.hcmute.utecare.model.Account;
import vn.edu.hcmute.utecare.repository.AccountRepository;
import vn.edu.hcmute.utecare.service.AuthenticationService;
import vn.edu.hcmute.utecare.service.JwtService;
import vn.edu.hcmute.utecare.service.OtpService;
import vn.edu.hcmute.utecare.util.OtpType;
import vn.edu.hcmute.utecare.util.TokenType;
import vn.edu.hcmute.utecare.util.UserStatus;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    private final JwtService jwtService;
    private final AccountRepository accountRepository;
    private final AuthenticationManager authenticationManager;
    private final OtpService otpService;
    private final PasswordEncoder passwordEncoder;

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

        Account account = accountRepository.findByPhone(request.getPhone()).orElseThrow(() -> new AccessDeniedException("Account not found"));


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
        Account account = accountRepository.findByPhone(phone).orElseThrow(() -> new AccessDeniedException("Account not found"));

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

        if (accountRepository.findByPhone(phoneNumber).isPresent()) {
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
    public TokenResponse registerSetPassword(SetPasswordRequest request) {
        String phone = "";
        try {
            phone = jwtService.extractUsername(request.getVerificationToken(), TokenType.VERIFICATION_TOKEN);
        } catch (Exception e) {
            throw new AccessDeniedException("Invalid verification token");
        }


        if (jwtService.isTokenExpired(request.getVerificationToken(), TokenType.VERIFICATION_TOKEN)) {
            throw new AccessDeniedException("Expired verification token");
        }


        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        if (request.getPassword().length() < 8 || !request.getPassword().matches(".*[A-Za-z].*") || !request.getPassword().matches(".*\\d.*")) {
            throw new IllegalArgumentException("Password must be at least 8 characters long and contain letters and numbers");
        }

        if (accountRepository.findByPhone(phone).isPresent()) {
            throw new IllegalArgumentException("Phone number already registered");
        }

        Account account = new Account();
        account.setPhone(phone);
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

        Account account = accountRepository.findByPhone(phoneNumber)
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

        Account account = accountRepository.findByPhone(phoneNumber)
                .orElseThrow(() -> new IllegalArgumentException("Phone number not registered"));

        if (otpService.verifyOtp(request, OtpType.FORGOT_PASSWORD)) {
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
    public TokenResponse resetForgotPassword(SetPasswordRequest request) {
        String phone;
        try {
            phone = jwtService.extractUsername(request.getVerificationToken(), TokenType.VERIFICATION_TOKEN);
        } catch (Exception e) {
            throw new AccessDeniedException("Invalid verification token");
        }

        if (jwtService.isTokenExpired(request.getVerificationToken(), TokenType.VERIFICATION_TOKEN)) {
            throw new AccessDeniedException("Expired verification token");
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        if (request.getPassword().length() < 8 || !request.getPassword().matches(".*[A-Za-z].*") || !request.getPassword().matches(".*\\d.*")) {
            throw new IllegalArgumentException("Password must be at least 8 characters long and contain letters and numbers");
        }

        Account account = accountRepository.findByPhone(phone)
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
}

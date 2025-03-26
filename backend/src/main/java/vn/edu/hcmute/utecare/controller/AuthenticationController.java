package vn.edu.hcmute.utecare.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmute.utecare.dto.request.*;
import vn.edu.hcmute.utecare.dto.response.ResponseData;
import vn.edu.hcmute.utecare.dto.response.SendOtpResponse;
import vn.edu.hcmute.utecare.dto.response.TokenResponse;
import vn.edu.hcmute.utecare.dto.response.VerifyOtpResponse;
import vn.edu.hcmute.utecare.service.AuthenticationService;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication API")
@Slf4j(topic = "AUTH_CONTROLLER")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/sign-in")
    @Operation(summary = "Sign in with phone and password", description = "Authenticate user and return access and refresh tokens")
    public ResponseData<TokenResponse> signIn(@RequestBody @Valid SignInRequest request) {
        log.info("Sign in request for phone: {}", request.getPhone());
        return ResponseData.<TokenResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Sign in successful")
                .data(authenticationService.signIn(request))
                .build();
    }

    @PostMapping("/refresh-token")
    @Operation(summary = "Refresh access token", description = "Generate a new access token using refresh token")
    public ResponseData<TokenResponse> refreshToken(@RequestBody String refreshToken) {
        log.info("Refresh token request received");
        return ResponseData.<TokenResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Token refreshed successfully")
                .data(authenticationService.refreshToken(refreshToken))
                .build();
    }

    @PostMapping("/register/send-otp")
    @Operation(summary = "Send OTP for registration", description = "Send OTP to phone number for account registration")
    public ResponseData<SendOtpResponse> sendOtpForRegistration(@RequestBody @Valid SendOtpRequest request) {
        log.info("Send OTP request for registration: {}", request.getPhone());
        return ResponseData.<SendOtpResponse>builder()
                .status(HttpStatus.OK.value())
                .message("OTP sent successfully")
                .data(authenticationService.sendOtpForRegistration(request))
                .build();
    }

    @PostMapping("/register/verify-otp")
    @Operation(summary = "Verify OTP for registration", description = "Verify OTP and return verification token for registration")
    public ResponseData<VerifyOtpResponse> verifyOtpForRegistration(@RequestBody @Valid VerifyOtpRequest request) {
        log.info("Verify OTP request for registration: {}", request.getPhone());
        return ResponseData.<VerifyOtpResponse>builder()
                .status(HttpStatus.OK.value())
                .message("OTP verified successfully")
                .data(authenticationService.verifyOtpForRegistration(request))
                .build();
    }

    @PostMapping("/register/set-password")
    @Operation(summary = "Set password for registration", description = "Set password for a new account using verification token")
    public ResponseData<TokenResponse> registerSetPassword(
            @RequestHeader("X-Verification-Token") String verificationToken,
            @RequestBody @Valid SetPasswordRequest request) {
        log.info("Set password request for registration with token: {}", verificationToken);
        return ResponseData.<TokenResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Account registered successfully")
                .data(authenticationService.registerSetPassword(verificationToken, request))
                .build();
    }

    @PostMapping("/forgot-password/send-otp")
    @Operation(summary = "Send OTP for forgot password", description = "Send OTP to phone number for password reset")
    public ResponseData<SendOtpResponse> sendOtpForForgotPassword(@RequestBody @Valid SendOtpRequest request) {
        log.info("Send OTP request for forgot password: {}", request.getPhone());
        return ResponseData.<SendOtpResponse>builder()
                .status(HttpStatus.OK.value())
                .message("OTP sent successfully")
                .data(authenticationService.sendOtpForForgotPassword(request))
                .build();
    }

    @PostMapping("/forgot-password/verify-otp")
    @Operation(summary = "Verify OTP for forgot password", description = "Verify OTP and return verification token for password reset")
    public ResponseData<VerifyOtpResponse> verifyOtpForForgotPassword(@RequestBody @Valid VerifyOtpRequest request) {
        log.info("Verify OTP request for forgot password: {}", request.getPhone());
        return ResponseData.<VerifyOtpResponse>builder()
                .status(HttpStatus.OK.value())
                .message("OTP verified successfully")
                .data(authenticationService.verifyOtpForForgotPassword(request))
                .build();
    }

    @PostMapping("/forgot-password/reset")
    @Operation(summary = "Reset forgotten password", description = "Reset password using verification token")
    public ResponseData<TokenResponse> resetForgotPassword(
            @RequestHeader("X-Verification-Token") String verificationToken,
            @RequestBody @Valid SetPasswordRequest request) {
        log.info("Reset password request with token: {}", verificationToken);
        return ResponseData.<TokenResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Password reset successfully")
                .data(authenticationService.resetForgotPassword(verificationToken, request))
                .build();
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout", description = "Invalidate access token")
    public ResponseData<Void> logout(@RequestHeader("Authorization") String accessToken) {
        log.info("Logout request received");
        authenticationService.logout(accessToken);
        return ResponseData.<Void>builder()
                .status(HttpStatus.OK.value())
                .message("Logout successful")
                .build();
    }
}
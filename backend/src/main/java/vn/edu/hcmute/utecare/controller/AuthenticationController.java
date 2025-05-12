package vn.edu.hcmute.utecare.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
@Tag(name = "AUTH", description = "API quản lý xác thực, đăng nhập, đăng ký và khôi phục mật khẩu")
@Slf4j(topic = "AUTH_CONTROLLER")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/sign-in")
    @Operation(
            summary = "Đăng nhập bằng số điện thoại và mật khẩu",
            description = "Xác thực người dùng dựa trên số điện thoại và mật khẩu, trả về token truy cập và token làm mới."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Đăng nhập thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu yêu cầu không hợp lệ"),
            @ApiResponse(responseCode = "403", description = "Thông tin đăng nhập không hợp lệ hoặc tài khoản bị khóa")
    })
    public ResponseData<TokenResponse> signIn(@Valid @RequestBody SignInRequest request) {
        log.info("Yêu cầu đăng nhập cho số điện thoại: {}", request.getPhone());
        return ResponseData.<TokenResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Đăng nhập thành công")
                .data(authenticationService.signIn(request))
                .build();
    }

    @PostMapping("/refresh-token")
    @Operation(
            summary = "Làm mới token truy cập",
            description = "Sử dụng token làm mới để tạo token truy cập mới, duy trì phiên đăng nhập."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Làm mới token thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu yêu cầu không hợp lệ"),
            @ApiResponse(responseCode = "403", description = "Token làm mới không hợp lệ hoặc đã bị hủy")
    })
    public ResponseData<TokenResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest refreshToken) {
        log.info("Yêu cầu làm mới token truy cập");
        return ResponseData.<TokenResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Làm mới token thành công")
                .data(authenticationService.refreshToken(refreshToken))
                .build();
    }

    @PostMapping("/register/send-otp")
    @Operation(
            summary = "Gửi OTP để đăng ký",
            description = "Gửi mã OTP đến số điện thoại để bắt đầu quá trình đăng ký tài khoản mới."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Gửi OTP thành công"),
            @ApiResponse(responseCode = "400", description = "Số điện thoại không hợp lệ hoặc đã được đăng ký")
    })
    public ResponseData<SendOtpResponse> sendOtpForRegistration(@Valid @RequestBody SendOtpRequest request) {
        log.info("Yêu cầu gửi OTP để đăng ký: {}", request.getPhone());
        return ResponseData.<SendOtpResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Gửi OTP thành công")
                .data(authenticationService.sendOtpForRegistration(request))
                .build();
    }

    @PostMapping("/register/verify-otp")
    @Operation(
            summary = "Xác minh OTP để đăng ký",
            description = "Xác minh mã OTP để tiếp tục quá trình đăng ký, trả về token xác minh."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Xác minh OTP thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu yêu cầu không hợp lệ"),
            @ApiResponse(responseCode = "403", description = "OTP không hợp lệ hoặc đã hết hạn")
    })
    public ResponseData<VerifyOtpResponse> verifyOtpForRegistration(@Valid @RequestBody VerifyOtpRequest request) {
        log.info("Yêu cầu xác minh OTP để đăng ký: {}", request.getPhone());
        return ResponseData.<VerifyOtpResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Xác minh OTP thành công")
                .data(authenticationService.verifyOtpForRegistration(request))
                .build();
    }

    @PostMapping("/register/set-password")
    @Operation(
            summary = "Thiết lập mật khẩu để hoàn tất đăng ký",
            description = "Thiết lập mật khẩu cho tài khoản mới bằng token xác minh, trả về token truy cập và làm mới."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Đăng ký tài khoản thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu yêu cầu không hợp lệ hoặc token xác minh không hợp lệ"),
            @ApiResponse(responseCode = "403", description = "Token xác minh đã hết hạn")
    })
    public ResponseData<TokenResponse> registerSetPassword(
            @Parameter(description = "Token xác minh từ bước xác minh OTP") @RequestHeader("X-Verification-Token") String verificationToken,
            @Valid @RequestBody SetPasswordRequest request) {
        log.info("Yêu cầu thiết lập mật khẩu để đăng ký với token: {}", verificationToken);
        return ResponseData.<TokenResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Đăng ký tài khoản thành công")
                .data(authenticationService.registerSetPassword(verificationToken, request))
                .build();
    }

    @PostMapping("/forgot-password/send-otp")
    @Operation(
            summary = "Gửi OTP để khôi phục mật khẩu",
            description = "Gửi mã OTP đến số điện thoại để bắt đầu quá trình đặt lại mật khẩu."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Gửi OTP thành công"),
            @ApiResponse(responseCode = "400", description = "Số điện thoại không hợp lệ hoặc chưa được đăng ký"),
            @ApiResponse(responseCode = "403", description = "Tài khoản chưa được kích hoạt")
    })
    public ResponseData<SendOtpResponse> sendOtpForForgotPassword(@Valid @RequestBody SendOtpRequest request) {
        log.info("Yêu cầu gửi OTP để khôi phục mật khẩu: {}", request.getPhone());
        return ResponseData.<SendOtpResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Gửi OTP thành công")
                .data(authenticationService.sendOtpForForgotPassword(request))
                .build();
    }

    @PostMapping("/forgot-password/verify-otp")
    @Operation(
            summary = "Xác minh OTP để khôi phục mật khẩu",
            description = "Xác minh mã OTP để tiếp tục quá trình đặt lại mật khẩu, trả về token xác minh."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Xác minh OTP thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu yêu cầu không hợp lệ"),
            @ApiResponse(responseCode = "403", description = "OTP không hợp lệ hoặc đã hết hạn")
    })
    public ResponseData<VerifyOtpResponse> verifyOtpForForgotPassword(@Valid @RequestBody VerifyOtpRequest request) {
        log.info("Yêu cầu xác minh OTP để khôi phục mật khẩu: {}", request.getPhone());
        return ResponseData.<VerifyOtpResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Xác minh OTP thành công")
                .data(authenticationService.verifyOtpForForgotPassword(request))
                .build();
    }

    @PostMapping("/forgot-password/reset")
    @Operation(
            summary = "Đặt lại mật khẩu đã quên",
            description = "Đặt lại mật khẩu mới cho tài khoản bằng token xác minh, trả về token truy cập và làm mới."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Đặt lại mật khẩu thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu yêu cầu không hợp lệ hoặc token xác minh không hợp lệ"),
            @ApiResponse(responseCode = "403", description = "Token xác minh đã hết hạn")
    })
    public ResponseData<TokenResponse> resetForgotPassword(
            @Parameter(description = "Token xác minh từ bước xác minh OTP") @RequestHeader("X-Verification-Token") String verificationToken,
            @Valid @RequestBody SetPasswordRequest request) {
        log.info("Yêu cầu đặt lại mật khẩu với token: {}", verificationToken);
        return ResponseData.<TokenResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Đặt lại mật khẩu thành công")
                .data(authenticationService.resetForgotPassword(verificationToken, request))
                .build();
    }

    @PostMapping("/logout")
//    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "Đăng xuất",
            description = "Hủy hiệu lực token truy cập và token làm mới, kết thúc phiên đăng nhập của người dùng."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Đăng xuất thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu yêu cầu không hợp lệ"),
            @ApiResponse(responseCode = "403", description = "Token truy cập không hợp lệ")
    })
    public ResponseData<Void> logout(@Valid @RequestBody LogoutRequest request) {
        log.info("Yêu cầu đăng xuất");
        authenticationService.logout(request);
        return ResponseData.<Void>builder()
                .status(HttpStatus.OK.value())
                .message("Đăng xuất thành công")
                .build();
    }
}
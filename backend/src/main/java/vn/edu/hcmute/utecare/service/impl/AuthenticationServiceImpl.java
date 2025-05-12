package vn.edu.hcmute.utecare.service.impl;

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
import org.springframework.transaction.annotation.Transactional;
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
import vn.edu.hcmute.utecare.util.enumeration.AccountStatus;
import vn.edu.hcmute.utecare.util.enumeration.OtpType;
import vn.edu.hcmute.utecare.util.enumeration.TokenType;
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
    @Transactional(readOnly = true)
    public TokenResponse signIn(SignInRequest request) {
        log.info("Yêu cầu đăng nhập cho số điện thoại: {}", request.getPhone());

        try {
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getPhone(), request.getPassword()));
            log.debug("Xác thực thành công: {}", authenticate.isAuthenticated());
            SecurityContextHolder.getContext().setAuthentication(authenticate);
        } catch (BadCredentialsException | DisabledException e) {
            log.error("Lỗi xác thực: {}", e.getMessage());
            throw new AccessDeniedException("Thông tin đăng nhập không hợp lệ hoặc tài khoản bị khóa");
        }

        Account account = accountRepository.findByUser_Phone(request.getPhone())
                .orElseThrow(() -> new AccessDeniedException("Tài khoản không tồn tại"));

        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new AccessDeniedException("Tài khoản chưa được kích hoạt");
        }

        String accessToken = jwtService.generateAccessToken(account);
        String refreshToken = jwtService.generateRefreshToken(account);
        redisService.set("refresh:" + account.getUser().getPhone(), refreshToken, jwtService.getRefreshTokenExpiration(), TimeUnit.SECONDS);
        log.info("Đăng nhập thành công cho tài khoản ID: {}", account.getId());

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(account.getId())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public TokenResponse refreshToken(RefreshTokenRequest request) {
        log.info("Yêu cầu làm mới token truy cập");

        String phone = jwtService.extractUsername(request.getRefreshToken(), TokenType.REFRESH_TOKEN);
        Account account = accountRepository.findByUser_Phone(phone)
                .orElseThrow(() -> new AccessDeniedException("Tài khoản không tồn tại"));

        String storedRefreshToken = (String) redisService.get("refresh:" + phone);
        if (storedRefreshToken == null || !storedRefreshToken.equals(request.getRefreshToken())) {
            throw new AccessDeniedException("Token làm mới không hợp lệ hoặc đã bị hủy");
        }

        if (!jwtService.isValid(request.getRefreshToken(), TokenType.REFRESH_TOKEN, account)) {
            throw new AccessDeniedException("Token làm mới không hợp lệ");
        }

        String newAccessToken = jwtService.generateAccessToken(account);
        log.info("Làm mới token thành công cho tài khoản ID: {}", account.getId());

        return TokenResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(request.getRefreshToken())
                .userId(account.getId())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public SendOtpResponse sendOtpForRegistration(SendOtpRequest request) {
        String phoneNumber = request.getPhone();
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Định dạng số điện thoại không hợp lệ");
        }

        if (accountRepository.findByUser_Phone(phoneNumber).isPresent()) {
            throw new IllegalArgumentException("Số điện thoại đã được đăng ký");
        }

        SendOtpResponse response = otpService.generateAndSendOtp(phoneNumber, OtpType.REGISTER);
        log.info("Gửi OTP thành công cho số điện thoại: {}", phoneNumber);
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public VerifyOtpResponse verifyOtpForRegistration(VerifyOtpRequest request) {
        String phoneNumber = request.getPhone();
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Định dạng số điện thoại không hợp lệ");
        }

        if (otpService.verifyOtp(request, OtpType.REGISTER)) {
            String verificationToken = jwtService.generateVerificationToken(phoneNumber);
            log.info("Xác minh OTP thành công cho số điện thoại: {}", phoneNumber);
            return VerifyOtpResponse.builder()
                    .phone(phoneNumber)
                    .verificationToken(verificationToken)
                    .build();
        }
        throw new AccessDeniedException("OTP không hợp lệ hoặc đã hết hạn");
    }

    @Override
    @Transactional
    public TokenResponse registerSetPassword(String verificationToken, SetPasswordRequest request) {
        String phone;
        try {
            phone = jwtService.extractUsername(verificationToken, TokenType.VERIFICATION_TOKEN);
        } catch (Exception e) {
            log.error("Lỗi khi giải mã token xác minh: {}", e.getMessage());
            throw new AccessDeniedException("Token xác minh không hợp lệ");
        }

        if (jwtService.isTokenExpired(verificationToken, TokenType.VERIFICATION_TOKEN)) {
            throw new AccessDeniedException("Token xác minh đã hết hạn");
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Mật khẩu và xác nhận mật khẩu không khớp");
        }

        if (request.getPassword().length() < 8 || !request.getPassword().matches(".*[A-Za-z].*") || !request.getPassword().matches(".*\\d.*")) {
            throw new IllegalArgumentException("Mật khẩu phải dài ít nhất 8 ký tự và chứa cả chữ cái và số");
        }

        if (accountRepository.findByUser_Phone(phone).isPresent()) {
            throw new IllegalArgumentException("Số điện thoại đã được đăng ký");
        }

        Customer customer = new Customer();
        customer.setPhone(phone);

        Account account = new Account();
        account.setUser(customer);
        account.setPassword(passwordEncoder.encode(request.getPassword()));
        account.setStatus(AccountStatus.ACTIVE);
        Account savedAccount = accountRepository.save(account);

        String accessToken = jwtService.generateAccessToken(savedAccount);
        String refreshToken = jwtService.generateRefreshToken(savedAccount);
        redisService.set("refresh:" + phone, refreshToken, jwtService.getRefreshTokenExpiration(), TimeUnit.SECONDS);
        log.info("Đăng ký tài khoản thành công cho số điện thoại: {}, ID: {}", phone, savedAccount.getId());

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(savedAccount.getId())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public SendOtpResponse sendOtpForForgotPassword(SendOtpRequest request) {
        String phoneNumber = request.getPhone();
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Định dạng số điện thoại không hợp lệ");
        }

        Account account = accountRepository.findByUser_Phone(phoneNumber)
                .orElseThrow(() -> new IllegalArgumentException("Số điện thoại chưa được đăng ký"));

        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new AccessDeniedException("Tài khoản chưa được kích hoạt");
        }

        SendOtpResponse response = otpService.generateAndSendOtp(phoneNumber, OtpType.FORGOT_PASSWORD);
        log.info("Gửi OTP thành công để khôi phục mật khẩu cho số điện thoại: {}", phoneNumber);
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public VerifyOtpResponse verifyOtpForForgotPassword(VerifyOtpRequest request) {
        String phoneNumber = request.getPhone();
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Định dạng số điện thoại không hợp lệ");
        }

        Account account = accountRepository.findByUser_Phone(phoneNumber)
                .orElseThrow(() -> new IllegalArgumentException("Số điện thoại chưa được đăng ký"));

        if (otpService.verifyOtp(request, OtpType.FORGOT_PASSWORD)) {
            String verificationToken = jwtService.generateResetToken(account);
            log.info("Xác minh OTP thành công để khôi phục mật khẩu cho số điện thoại: {}", phoneNumber);
            return VerifyOtpResponse.builder()
                    .phone(phoneNumber)
                    .verificationToken(verificationToken)
                    .build();
        }
        throw new AccessDeniedException("OTP không hợp lệ hoặc đã hết hạn");
    }

    @Override
    @Transactional
    public TokenResponse resetForgotPassword(String verificationToken, SetPasswordRequest request) {
        String phone;
        try {
            phone = jwtService.extractUsername(verificationToken, TokenType.RESET_TOKEN);
        } catch (Exception e) {
            log.error("Lỗi khi giải mã token đặt lại mật khẩu: {}", e.getMessage());
            throw new AccessDeniedException("Token xác minh không hợp lệ");
        }

        if (jwtService.isTokenExpired(verificationToken, TokenType.RESET_TOKEN)) {
            throw new AccessDeniedException("Token xác minh đã hết hạn");
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Mật khẩu và xác nhận mật khẩu không khớp");
        }

        if (request.getPassword().length() < 8 || !request.getPassword().matches(".*[A-Za-z].*") || !request.getPassword().matches(".*\\d.*")) {
            throw new IllegalArgumentException("Mật khẩu phải dài ít nhất 8 ký tự và chứa cả chữ cái và số");
        }

        Account account = accountRepository.findByUser_Phone(phone)
                .orElseThrow(() -> new IllegalArgumentException("Số điện thoại chưa được đăng ký"));

        account.setPassword(passwordEncoder.encode(request.getPassword()));
        Account updatedAccount = accountRepository.save(account);

        String accessToken = jwtService.generateAccessToken(updatedAccount);
        String refreshToken = jwtService.generateRefreshToken(updatedAccount);
        redisService.set("refresh:" + phone, refreshToken, jwtService.getRefreshTokenExpiration(), TimeUnit.SECONDS);
        log.info("Đặt lại mật khẩu thành công cho số điện thoại: {}, ID: {}", phone, updatedAccount.getId());

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(updatedAccount.getId())
                .build();
    }

    @Override
    @Transactional
    public void logout(LogoutRequest request) {
        log.info("Yêu cầu đăng xuất");

        String phone;
        try {
            phone = jwtService.extractUsername(request.getAccessToken(), TokenType.ACCESS_TOKEN);
        } catch (Exception e) {
            log.warn("Token truy cập không hợp lệ khi đăng xuất: {}", e.getMessage());
            throw new AccessDeniedException("Token truy cập không hợp lệ");
        }

//        // Kiểm tra quyền sở hữu token
//        Long currentUserId = SecurityUtil.getCurrentUserId();
//        Account account = accountRepository.findByUser_Phone(phone)
//                .orElseThrow(() -> new AccessDeniedException("Tài khoản không tồn tại"));
//        if (!currentUserId.equals(account.getId())) {
//            log.warn("Người dùng ID {} cố đăng xuất với token của tài khoản ID: {}", currentUserId, account.getId());
//            throw new AccessDeniedException("Không có quyền đăng xuất tài khoản này");
//        }

        // Thêm token vào danh sách đen
        long remainingATTime = jwtService.getRemainingTime(request.getAccessToken(), TokenType.ACCESS_TOKEN);
        if (remainingATTime > 0) {
            redisService.set(AT_BLACKLIST_PREFIX + phone, request.getAccessToken(), remainingATTime, TimeUnit.SECONDS);
            log.info("Lưu token truy cập vào danh sách đen cho số điện thoại: {}", phone);
        }

        long remainingRTTime = jwtService.getRemainingTime(request.getRefreshToken(), TokenType.REFRESH_TOKEN);
        if (remainingRTTime > 0) {
            redisService.set(RT_BLACKLIST_PREFIX + phone, request.getRefreshToken(), remainingRTTime, TimeUnit.SECONDS);
            redisService.delete("refresh:" + phone);
            log.info("Lưu token làm mới vào danh sách đen và xóa token làm mới khỏi Redis cho số điện thoại: {}", phone);
        }

        log.info("Đăng xuất thành công cho số điện thoại: {}", phone);
    }
}
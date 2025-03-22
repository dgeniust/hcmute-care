package vn.edu.hcmute.utecare.service;

import vn.edu.hcmute.utecare.dto.request.SendOtpRequest;
import vn.edu.hcmute.utecare.dto.request.SetPasswordRequest;
import vn.edu.hcmute.utecare.dto.request.SignInRequest;
import vn.edu.hcmute.utecare.dto.request.VerifyOtpRequest;
import vn.edu.hcmute.utecare.dto.response.SendOtpResponse;
import vn.edu.hcmute.utecare.dto.response.TokenResponse;
import vn.edu.hcmute.utecare.dto.response.VerifyOtpResponse;

public interface AuthenticationService {
    TokenResponse signIn(SignInRequest request);

    TokenResponse refreshToken(String refreshToken);

    SendOtpResponse sendOtpForRegistration(SendOtpRequest request);

    VerifyOtpResponse verifyOtpForRegistration(VerifyOtpRequest request);

    TokenResponse registerSetPassword(SetPasswordRequest request);

    SendOtpResponse sendOtpForForgotPassword(SendOtpRequest request);

    VerifyOtpResponse verifyOtpForForgotPassword(VerifyOtpRequest request);

    TokenResponse resetForgotPassword(SetPasswordRequest request);
}
package vn.edu.hcmute.utecare.service;

import jakarta.servlet.http.HttpServletRequest;
import vn.edu.hcmute.utecare.dto.request.PaymentRequest;
import vn.edu.hcmute.utecare.dto.response.PaymentResponse;
import vn.edu.hcmute.utecare.dto.response.VNPayResponse;


public interface PaymentService {
    PaymentResponse createPaymentUrl(PaymentRequest request, HttpServletRequest httpServletRequest);

}

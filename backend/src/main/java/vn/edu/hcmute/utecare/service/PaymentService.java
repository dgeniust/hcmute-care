package vn.edu.hcmute.utecare.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.utecare.dto.request.PaymentRequest;
import vn.edu.hcmute.utecare.dto.response.*;
import vn.edu.hcmute.utecare.util.enumeration.PaymentStatus;

import java.time.LocalDateTime;


public interface PaymentService {
    String createPaymentUrl(PaymentRequest request, HttpServletRequest httpServletRequest);

    PaymentAppointmentResponse processPaymentReturn(HttpServletRequest request, HttpServletResponse response);

    PaymentAppointmentResponse getPaymentByTransactionId(String transactionId);

    PaymentAppointmentResponse getPaymentByAppointmentId(Long appointmentId);

    PageResponse<PaymentResponse> getAllPayments(
            String transactionId,
            PaymentStatus paymentStatus,
            LocalDateTime startDate,
            LocalDateTime endDate,
            int page,
            int size,
            String sort,
            String direction
    );
}

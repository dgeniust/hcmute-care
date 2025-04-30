package vn.edu.hcmute.utecare.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.edu.hcmute.utecare.configuration.VNPayConfig;
import vn.edu.hcmute.utecare.dto.request.PaymentRequest;
import vn.edu.hcmute.utecare.dto.response.PaymentResponse;
import vn.edu.hcmute.utecare.model.Appointment;
import vn.edu.hcmute.utecare.model.Payment;
import vn.edu.hcmute.utecare.repository.PaymentRepository;
import vn.edu.hcmute.utecare.service.PaymentService;
import vn.edu.hcmute.utecare.util.VNPayUtil;
import vn.edu.hcmute.utecare.util.enumeration.PaymentMethod;
import vn.edu.hcmute.utecare.util.enumeration.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final VNPayConfig vnpayConfig;
//    private final AppointmentRepository appointmentRepository;
    @Override
    public PaymentResponse createPaymentUrl(PaymentRequest request, HttpServletRequest httpServletRequest) {

//        Appointment appointment = appointmentRepository.findById(request.getAppointmentId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Appointment not found"));
            Appointment appointment = new Appointment();

        Payment payment = Payment.builder()
                .appointment(appointment)
                .amount(request.getAmount())
                .paymentMethod(PaymentMethod.VNPAY)
                .paymentStatus(PaymentStatus.PENDING)
                .paymentDate(LocalDateTime.now())
                .transactionId(VNPayUtil.getRandomNumber(8))
                .build();
        paymentRepository.save(payment);
        Map<String, String> vnpParamsMap = vnpayConfig.getVNPayConfig();
        vnpParamsMap.put("vnp_Amount", String.valueOf(request.getAmount().multiply(new BigDecimal(100)).longValue()));
        vnpParamsMap.put("vnp_BankCode", request.getBankCode());
        vnpParamsMap.put("vnp_OrderInfo", "Thanh toán lịch khám: " + payment.getTransactionId());
        vnpParamsMap.put("vnp_TxnRef", payment.getTransactionId());
        vnpParamsMap.put("vnp_IpAddr", VNPayUtil.getIpAddress(httpServletRequest));
        String queryUrl = VNPayUtil.getPaymentURL(vnpParamsMap, true);
        String hashData = VNPayUtil.getPaymentURL(vnpParamsMap, false);
        queryUrl += "&vnp_SecureHash=" + VNPayUtil.hmacSHA512(vnpayConfig.getSecretKey(), hashData);;
        String returnUrl = vnpayConfig.getVnp_PayUrl() + "?" + queryUrl;
        return PaymentResponse.builder()
                .id(payment.getId())
                .appointmentId(payment.getAppointment().getId())
                .amount(payment.getAmount())
                .paymentMethod(payment.getPaymentMethod())
                .paymentStatus(payment.getPaymentStatus())
                .paymentDate(payment.getPaymentDate())
                .transactionId(payment.getTransactionId())
                .vnpResponseCode(null) // Not available yet
                .vnpTransactionNo(null) // Not available yet
                .vnpBankCode(request.getBankCode()) // Set from request
                .vnpPayDate(null) // Not available yet
                .paymentUrl(returnUrl) // Include payment URL
                .build();
    }
}

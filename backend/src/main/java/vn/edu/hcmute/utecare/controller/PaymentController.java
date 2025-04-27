package vn.edu.hcmute.utecare.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmute.utecare.dto.request.PaymentRequest;
import vn.edu.hcmute.utecare.dto.response.PaymentResponse;
import vn.edu.hcmute.utecare.dto.response.ResponseData;
import vn.edu.hcmute.utecare.dto.response.VNPayResponse;
import vn.edu.hcmute.utecare.model.Payment;
import vn.edu.hcmute.utecare.repository.PaymentRepository;
import vn.edu.hcmute.utecare.service.PaymentService;
import vn.edu.hcmute.utecare.util.VNPayUtil;
import vn.edu.hcmute.utecare.util.enumeration.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
@Tag(name = "Payment", description = "Payment API")
@Slf4j(topic = "PAYMENT_CONTROLLER")
public class PaymentController {
    private final PaymentService paymentService;
    private final PaymentRepository paymentRepository;

    @PostMapping("/vn-pay")
    @Operation(summary = "Payment by VNPay", description = "Pay the appointment by VNPay")
    public ResponseData<PaymentResponse> vnPayPayment(@RequestBody @Valid PaymentRequest request, HttpServletRequest httpServletRequest) {
        log.info("Payment request: {}", request);
        try {
            PaymentResponse paymentResponse = paymentService.createPaymentUrl(request, httpServletRequest);
            return ResponseData.<PaymentResponse>builder()
                    .status(HttpStatus.OK.value())
                    .message("Payment URL created successfully")
                    .data(paymentResponse)
                    .build();
        } catch (Exception e) {
            log.error("Payment failed: {}", e.getMessage(), e);
            return ResponseData.<PaymentResponse>builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("Payment failed: " + e.getMessage())
                    .data(null)
                    .build();
        }
    }

    @GetMapping("/vnpay-callback")
    public ResponseData<PaymentResponse> payCallbackHandler(@RequestParam("vnp_SecureHash") String secureHash,
                                                            @RequestParam("vnp_TxnRef") String vnpTxnRef,
                                                            @RequestParam("vnp_ResponseCode") String vnpResponseCode,
                                                            @RequestParam(value = "vnp_PayDate", required = false) String vnpPayDate,
                                                            @RequestParam(value = "vnp_TransactionNo", required = false) String vnpTransactionNo,
                                                            @RequestParam(value = "vnp_BankCode", required = false) String vnpBankCode) {
        try {
            if(secureHash == null) {
                log.error("Invalid VNPay secure hash");
                return ResponseData.<PaymentResponse>builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .message("Invalid VNPay response: Invalid secure hash")
                        .data(null)
                        .build();
            }
            if(vnpTxnRef == null) {
                log.error("Missing vnp_TxnRef in VNPay callback");
                return ResponseData.<PaymentResponse>builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .message("Missing transaction reference")
                        .data(null)
                        .build();
            }

            Payment payment = paymentRepository.findByTransactionId(vnpTxnRef);

            payment.setPaymentStatus("00".equals(vnpResponseCode) ? PaymentStatus.COMPLETED : PaymentStatus.FAILED);

            if (vnpPayDate != null) {
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
                    payment.setPaymentDate(LocalDateTime.parse(vnpPayDate, formatter));
                } catch (Exception e) {
                    log.warn("Failed to parse vnp_PayDate: {}", vnpPayDate);
                }
            }
            paymentRepository.save(payment);

            // Build PaymentResponse
            PaymentResponse paymentResponse = PaymentResponse.builder()
                    .id(payment.getId())
                    .amount(payment.getAmount())
                    .paymentMethod(payment.getPaymentMethod())
                    .paymentStatus(payment.getPaymentStatus())
                    .paymentDate(payment.getPaymentDate())
                    .transactionId(payment.getTransactionId())
                    .appointmentId(payment.getAppointment().getId())
                    .vnpResponseCode(vnpResponseCode)
                    .vnpTransactionNo(vnpTransactionNo)
                    .vnpBankCode(vnpBankCode)
                    .vnpPayDate(vnpPayDate)
                    .paymentUrl(null) // Not needed in callback
                    .build();
            String message = "00".equals(vnpResponseCode) ? "Payment successful" : "Payment failed";
            return ResponseData.<PaymentResponse>builder()
                    .status("00".equals(vnpResponseCode) ? HttpStatus.OK.value() : HttpStatus.BAD_REQUEST.value())
                    .message(message)
                    .data(paymentResponse)
                    .build();
        }
        catch (IllegalArgumentException e) {
            log.error("Callback error: {}", e.getMessage());
            return ResponseData.<PaymentResponse>builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message(e.getMessage())
                    .data(null)
                    .build();
        } catch (Exception e) {
            log.error("Callback processing failed: {}", e.getMessage(), e);
            return ResponseData.<PaymentResponse>builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Callback processing failed: " + e.getMessage())
                    .data(null)
                    .build();
        }
    }

}

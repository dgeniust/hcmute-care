package vn.edu.hcmute.utecare.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmute.utecare.dto.request.PaymentRequest;
import vn.edu.hcmute.utecare.dto.response.*;
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
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
@Tag(name = "Payment", description = "Payment API")
@Slf4j(topic = "PAYMENT_CONTROLLER")
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/vn-pay")
    @Operation(summary = "Payment by VNPay", description = "Pay the appointment by VNPay")
    public ResponseData<String> vnPayPayment(@RequestBody @Valid PaymentRequest request, HttpServletRequest httpServletRequest) {
        log.info("Payment request: {}", request);
        return ResponseData.<String>builder()
                .status(HttpStatus.OK.value())
                .message("Payment request successful")
                .data(paymentService.createPaymentUrl(request, httpServletRequest))
                .build();
    }


    @GetMapping("/vnpay/return")
    public ResponseData<PaymentAppointmentResponse> vnpayReturn(HttpServletRequest request) {
        log.info("VNPay return request: {}", request.getQueryString());

        return ResponseData.<PaymentAppointmentResponse>builder()
                .status(HttpStatus.OK.value())
                .message("VNPay return request successful")
                .data(paymentService.processPaymentReturn(request))
                .build();
    }

    @GetMapping("/transaction/{transactionId}")
    @Operation(summary = "Get payment by transaction ID", description = "Get payment details by transaction ID")
    public ResponseData<PaymentAppointmentResponse> getPaymentByTransactionId(@PathVariable String transactionId) {
        log.info("Get payment by transaction ID: {}", transactionId);
        return ResponseData.<PaymentAppointmentResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Get payment by transaction ID successful")
                .data(paymentService.getPaymentByTransactionId(transactionId))
                .build();
    }

    @GetMapping("/appointments/{appointmentId}")
    @Operation(summary = "Get payment by ID", description = "Get payment details by ID")
    public ResponseData<PaymentAppointmentResponse> getPaymentById(@PathVariable("appointmentId") Long appointmentId) {
        log.info("Get payment by ID: {}", appointmentId);
        return ResponseData.<PaymentAppointmentResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Get payment by ID successful")
                .data(paymentService.getPaymentByAppointmentId(appointmentId))
                .build();
    }

    @GetMapping
    @Operation(summary = "Get all payments", description = "Get all payments with optional filters")
    public ResponseData<PageResponse<PaymentResponse>> getAllPayments(
            @RequestParam(required = false) String transactionId,
            @RequestParam(required = false) PaymentStatus paymentStatus,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        log.info("Get all payments with filters: transactionId={}, paymentStatus={}, startDate={}, endDate={}, page={}, size={}, sort={}, direction={}",
                transactionId, paymentStatus, startDate, endDate, page, size, sort, direction);


        return ResponseData.<PageResponse<PaymentResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Get all payments successful")
                .data(paymentService.getAllPayments(transactionId, paymentStatus, startDate, endDate, page, size, sort, direction))
                .build();
    }
    @GetMapping("/all")
    @Operation(summary = "Get all payments for admin", description = "Get all payments with optional filters")
    public ResponseData<PageResponse<PaymentResponse>> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        log.info("Get all payments with filters: page={}, size={}, sort={}, direction={}",
                page, size, sort, direction);

        return ResponseData.<PageResponse<PaymentResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Get all payments successful")
                .data(paymentService.getAll(page, size, sort, direction))
                .build();
    }
}

package vn.edu.hcmute.utecare.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.utecare.configuration.VNPayConfig;
import vn.edu.hcmute.utecare.dto.request.PaymentRequest;
import vn.edu.hcmute.utecare.dto.response.AppointmentResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.dto.response.PaymentAppointmentResponse;
import vn.edu.hcmute.utecare.dto.response.PaymentResponse;
import vn.edu.hcmute.utecare.exception.ResourceNotFoundException;
import vn.edu.hcmute.utecare.mapper.PaymentMapper;
import vn.edu.hcmute.utecare.model.Appointment;
import vn.edu.hcmute.utecare.model.Payment;
import vn.edu.hcmute.utecare.model.Ticket;
import vn.edu.hcmute.utecare.repository.AppointmentRepository;
import vn.edu.hcmute.utecare.repository.PaymentRepository;
import vn.edu.hcmute.utecare.service.AppointmentService;
import vn.edu.hcmute.utecare.service.PaymentService;
import vn.edu.hcmute.utecare.util.PaginationUtil;
import vn.edu.hcmute.utecare.util.VNPayUtil;
import vn.edu.hcmute.utecare.util.enumeration.PaymentMethod;
import vn.edu.hcmute.utecare.util.enumeration.PaymentStatus;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final VNPayConfig vnpayConfig;
    private final AppointmentService appointmentService;
    private final AppointmentRepository appointmentRepository;


    @Transactional
    @Override
    public String createPaymentUrl(PaymentRequest request, HttpServletRequest httpServletRequest) {
        log.info("Creating payment URL for appointment ID: {}", request.getAppointmentId());

        Appointment appointment = appointmentRepository.findById(request.getAppointmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with ID: " + request.getAppointmentId()));

        BigDecimal amount = BigDecimal.ZERO;
        for (Ticket ticket : appointment.getTickets()) {
            BigDecimal price = ticket.getScheduleSlot().getSchedule().getDoctor().getMedicalSpecialty().getPrice();
            if (price == null) {
                throw new IllegalStateException("Price is not defined for medical specialty");
            }
            amount = amount.add(price);
        }

        Map<String, String> vnpParams = vnpayConfig.getVNPayConfig();
        String vnp_TxnRef = VNPayUtil.getRandomNumber(8);
        vnpParams.put("vnp_TxnRef", vnp_TxnRef);
        vnpParams.put("vnp_Amount", String.valueOf(amount.multiply(new BigDecimal(100)).longValue()));
        vnpParams.put("vnp_OrderInfo", "other");
        vnpParams.put("vnp_IpAddr", VNPayUtil.getIpAddress(httpServletRequest));

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(calendar.getTime());
        vnpParams.put("vnp_CreateDate", vnp_CreateDate);

        calendar.add(Calendar.MINUTE, 10);
        String vnp_ExpireDate = formatter.format(calendar.getTime());
        vnpParams.put("vnp_ExpireDate", vnp_ExpireDate);

        String hashData = VNPayUtil.getPaymentURL(vnpParams, false);
        String vnp_SecureHash = VNPayUtil.hmacSHA512(vnpayConfig.getSecretKey(), hashData);
        vnpParams.put("vnp_SecureHash", vnp_SecureHash);

        String paymentUrl = vnpayConfig.getVnpPayUrl() + "?" + VNPayUtil.getPaymentURL(vnpParams, true);

        Payment payment = Payment.builder()
                .amount(amount)
                .paymentMethod(PaymentMethod.VNPAY)
                .paymentStatus(PaymentStatus.PENDING)
                .transactionId(vnp_TxnRef)
                .appointment(appointment)
                .paymentDate(LocalDateTime.now())
                .build();
        paymentRepository.save(payment);

        return paymentUrl;
    }


    @Transactional
    @Override
    public PaymentAppointmentResponse processPaymentReturn(HttpServletRequest request){
        log.info("Processing payment return for request: {}", request);

        Map<String, String> vnpParams = new HashMap<>();
        for (String paramName : request.getParameterMap().keySet()) {
            vnpParams.put(paramName, request.getParameter(paramName));
        }

        String vnpSecureHash = vnpParams.remove("vnp_SecureHash");
        String hashData = VNPayUtil.getPaymentURL(vnpParams, false);
        String calculatedHash = VNPayUtil.hmacSHA512(vnpayConfig.getSecretKey(), hashData);
        if (!calculatedHash.equalsIgnoreCase(vnpSecureHash)) {
            throw new SecurityException("Invalid secure hash");
        }

        String vnpResponseCode = vnpParams.get("vnp_ResponseCode");
        String vnpTransactionNo = vnpParams.get("vnp_TransactionNo");
        String vnpPayDate = vnpParams.get("vnp_PayDate");
        String vnpTxnRef = vnpParams.get("vnp_TxnRef");

        Payment payment = paymentRepository.findByTransactionId(vnpTxnRef)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with transaction ID: " + vnpTxnRef));

        if ("00".equals(vnpResponseCode)) {
            payment.setPaymentStatus(PaymentStatus.COMPLETED);
            payment.setTransactionId(vnpTransactionNo);
            payment.setPaymentDate(VNPayUtil.parseVNPayDate(vnpPayDate));
            paymentRepository.save(payment);

            appointmentService.confirmAppointment(payment.getAppointment().getId());
        } else {
            payment.setPaymentStatus(PaymentStatus.FAILED);
            paymentRepository.save(payment);
            appointmentService.cancelAppointment(payment.getAppointment().getId());
        }
        return PaymentMapper.INSTANCE.toPaymentAppointmentResponse(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponse getPaymentByTransactionId(String transactionId) {
        log.info("Fetching payment with transaction ID: {}", transactionId);
        Payment payment = paymentRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with transaction ID: " + transactionId));
        return PaymentMapper.INSTANCE.toResponse(payment);
    }

    @Override
    public PageResponse<PaymentResponse> getAllPayments(
            String transactionId,
            PaymentStatus paymentStatus,
            LocalDateTime startDate,
            LocalDateTime endDate,
            int page,
            int size,
            String sort,
            String direction
    ){
        log.info("Fetching all payments with transaction ID: {}, payment status: {}, start date: {}, end date: {}, page: {}, size: {}, sort: {}, direction: {}",
                transactionId, paymentStatus, startDate, endDate, page, size, sort, direction);
        Pageable pageable = PaginationUtil.createPageable(page, size, sort, direction);
        Page<Payment> paymentPage = paymentRepository.findAllByTransactionIdAndPaymentStatusAndPaymentDateBetween(
                transactionId,
                paymentStatus,
                startDate,
                endDate,
                pageable
        );

        return PageResponse.<PaymentResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalElements(paymentPage.getTotalElements())
                .totalPages(paymentPage.getTotalPages())
                .content(paymentPage.getContent().stream()
                        .map(PaymentMapper.INSTANCE::toResponse)
                        .toList())
                .build();
    }
}

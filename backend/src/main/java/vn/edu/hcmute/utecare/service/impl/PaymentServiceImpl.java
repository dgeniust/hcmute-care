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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final VNPayConfig vnpayConfig;
    private final AppointmentService appointmentService;
    private final AppointmentRepository appointmentRepository;
    private final PaymentMapper paymentMapper;

    @Transactional
    @Override
    public String createPaymentUrl(PaymentRequest request, HttpServletRequest httpServletRequest) {
        log.info("Đang tạo URL thanh toán cho cuộc hẹn ID: {}", request.getAppointmentId());

        Appointment appointment = appointmentRepository.findById(request.getAppointmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy cuộc hẹn với ID: " + request.getAppointmentId()));

        BigDecimal amount = appointmentService.calculateTotalPrice(appointment);

        Map<String, String> vnpParams = initializeVNPayParams(appointment, amount, httpServletRequest);
        String paymentUrl = buildPaymentUrl(vnpParams);

        Payment payment = Payment.builder()
                .amount(amount)
                .paymentMethod(PaymentMethod.VNPAY)
                .paymentStatus(PaymentStatus.PENDING)
                .transactionId(vnpParams.get("vnp_TxnRef"))
                .appointment(appointment)
                .paymentDate(LocalDateTime.now())
                .build();
        paymentRepository.save(payment);
        log.info("Tạo URL thanh toán thành công cho giao dịch: {}", vnpParams.get("vnp_TxnRef"));

        return paymentUrl;
    }

    private Map<String, String> initializeVNPayParams(Appointment appointment, BigDecimal amount, HttpServletRequest request) {
        Map<String, String> params = vnpayConfig.getVNPayConfig();
        String transactionRef = VNPayUtil.getRandomNumber(8);

        params.put("vnp_TxnRef", transactionRef);
        params.put("vnp_Amount", String.valueOf(amount.multiply(BigDecimal.valueOf(100)).longValue()));
        params.put("vnp_OrderInfo", "Thanh toán cuộc hẹn ID: " + appointment.getId());
        params.put("vnp_IpAddr", VNPayUtil.getIpAddress(request));
        params.put("vnp_CreateDate", LocalDateTime.now(ZoneId.of("GMT+7"))
                .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
        params.put("vnp_ExpireDate", LocalDateTime.now(ZoneId.of("GMT+7"))
                .plusMinutes(VNPayUtil.PAYMENT_EXPIRY_MINUTES)
                .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));

        return params;
    }

    private String buildPaymentUrl(Map<String, String> vnpParams) {
        String hashData = VNPayUtil.getPaymentURL(vnpParams, false);
        String secureHash = VNPayUtil.hmacSHA512(vnpayConfig.getSecretKey(), hashData);
        vnpParams.put("vnp_SecureHash", secureHash);
        return vnpayConfig.getVnpPayUrl() + "?" + VNPayUtil.getPaymentURL(vnpParams, true);
    }

    @Transactional
    @Override
    public PaymentAppointmentResponse processPaymentReturn(HttpServletRequest request) {
        log.info("Đang xử lý phản hồi thanh toán VNPay: {}", request.getQueryString());

        Map<String, String> vnpParams = extractParameters(request);
        validateSecureHash(vnpParams);

        String vnpTxnRef = vnpParams.get("vnp_TxnRef");
        Payment payment = paymentRepository.findByTransactionId(vnpTxnRef)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy giao dịch thanh toán với mã: " + vnpTxnRef));

        updatePaymentStatus(payment, vnpParams);
        return paymentMapper.toPaymentAppointmentResponse(payment);
    }

    private Map<String, String> extractParameters(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        request.getParameterMap().forEach((key, value) -> params.put(key, value[0]));
        return params;
    }

    private void validateSecureHash(Map<String, String> vnpParams) {
        String receivedHash = vnpParams.remove("vnp_SecureHash");
        String hashData = VNPayUtil.getPaymentURL(vnpParams, false);
        String calculatedHash = VNPayUtil.hmacSHA512(vnpayConfig.getSecretKey(), hashData);

        if (!calculatedHash.equalsIgnoreCase(receivedHash)) {
            throw new SecurityException("Mã bảo mật không hợp lệ");
        }
    }

    private void updatePaymentStatus(Payment payment, Map<String, String> vnpParams) {
        String responseCode = vnpParams.get("vnp_ResponseCode");
        String transactionNo = vnpParams.get("vnp_TransactionNo");
        String payDate = vnpParams.get("vnp_PayDate");

        if ("00".equals(responseCode)) {
            payment.setPaymentStatus(PaymentStatus.COMPLETED);
            payment.setTransactionId(transactionNo);
            payment.setPaymentDate(VNPayUtil.parseVNPayDate(payDate));
            paymentRepository.save(payment);
            appointmentService.confirmAppointment(payment.getAppointment().getId());
            log.info("Thanh toán thành công cho giao dịch: {}", transactionNo);
        } else {
            payment.setPaymentStatus(PaymentStatus.FAILED);
            paymentRepository.save(payment);
            appointmentService.cancelAppointment(payment.getAppointment().getId());
            log.info("Thanh toán thất bại cho giao dịch: {}", vnpParams.get("vnp_TxnRef"));
        }
    }


    @Override
    @Transactional(readOnly = true)
    public PaymentAppointmentResponse getPaymentByTransactionId(String transactionId) {
        log.info("Truy xuất thanh toán với mã giao dịch: {}", transactionId);
        Payment payment = paymentRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy giao dịch thanh toán với mã: " + transactionId));
        log.info("Truy xuất thanh toán thành công với mã giao dịch: {}", transactionId);
        return paymentMapper.toPaymentAppointmentResponse(payment);
    }

    @Transactional(readOnly = true)
    @Override
    public PaymentAppointmentResponse getPaymentByAppointmentId(Long appointmentId) {
        log.info("Truy xuất thanh toán với ID cuộc hẹn: {}", appointmentId);
        Payment payment = paymentRepository.findByAppointment_Id(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy giao dịch thanh toán với ID cuộc hẹn: " + appointmentId));
        log.info("Truy xuất thanh toán thành công với ID cuộc hẹn: {}", appointmentId);
        return paymentMapper.toPaymentAppointmentResponse(payment);
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
            String direction) {
        log.info("Truy xuất danh sách giao dịch thanh toán: mã giao dịch={}, trạng thái={}, ngày bắt đầu={}, ngày kết thúc={}, trang={}, kích thước={}, sắp xếp={}, hướng={}",
                transactionId, paymentStatus, startDate, endDate, page, size, sort, direction);
        Pageable pageable = PaginationUtil.createPageable(page, size, sort, direction);
        Page<Payment> paymentPage = paymentRepository.findAllByTransactionIdAndPaymentStatusAndPaymentDateBetween(
                transactionId,
                paymentStatus,
                startDate,
                endDate,
                pageable);

        return PageResponse.<PaymentResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalElements(paymentPage.getTotalElements())
                .totalPages(paymentPage.getTotalPages())
                .content(paymentPage.getContent().stream()
                        .map(paymentMapper::toResponse)
                        .toList())
                .build();
    }

    @Override
    public PageResponse<PaymentResponse> getAll(int page, int size, String sort, String direction) {
        log.info("Truy xuất danh sách tất cả giao dịch thanh toán: trang={}, kích thước={}, sắp xếp={}, hướng={}",
                page, size, sort, direction);
        Pageable pageable = PaginationUtil.createPageable(page, size, sort, direction);
        Page<Payment> paymentPages = paymentRepository.findAll(pageable);
        return PageResponse.<PaymentResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalElements(paymentPages.getTotalElements())
                .totalPages(paymentPages.getTotalPages())
                .content(paymentPages.getContent().stream().map(paymentMapper::toResponse).toList())
                .build();
    }

    @Override
    public PageResponse<PaymentResponse> getAllPaymentsByCustomerId(
            Long customerId,
            PaymentStatus paymentStatus,
            int page, int size, String sort, String direction) {
        log.info("Truy xuất danh sách giao dịch thanh toán cho khách hàng ID: {}, trạng thái={}, trang={}, kích thước={}, sắp xếp={}, hướng={}",
                customerId, paymentStatus, page, size, sort, direction);
        Pageable pageable = PaginationUtil.createPageable(page, size, sort, direction);
        Page<Payment> paymentPage = paymentRepository.findAllByCustomerId(customerId, paymentStatus, pageable);
        return PageResponse.<PaymentResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalElements(paymentPage.getTotalElements())
                .totalPages(paymentPage.getTotalPages())
                .content(paymentPage.getContent().stream()
                        .map(paymentMapper::toResponse)
                        .toList())
                .build();
    }
}
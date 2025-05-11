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
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

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
        log.info("Tạo URL thanh toán cho cuộc hẹn ID: {}", request.getAppointmentId());

        Appointment appointment = appointmentRepository.findById(request.getAppointmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy cuộc hẹn với ID: " + request.getAppointmentId()));

        BigDecimal amount = BigDecimal.ZERO;
        for (Ticket ticket : appointment.getTickets()) {
            BigDecimal price = ticket.getScheduleSlot().getSchedule().getDoctor().getMedicalSpecialty().getPrice();
            if (price == null) {
                throw new IllegalStateException("Giá chưa được định nghĩa cho chuyên khoa");
            }
            amount = amount.add(price);
        }

        Map<String, String> vnpParams = vnpayConfig.getVNPayConfig();
        String vnp_TxnRef = VNPayUtil.getRandomNumber(8);
        vnpParams.put("vnp_TxnRef", vnp_TxnRef);
        vnpParams.put("vnp_Amount", String.valueOf(amount.multiply(new BigDecimal(100)).longValue()));
        vnpParams.put("vnp_OrderInfo", "Thanh toán cuộc hẹn ID: " + request.getAppointmentId());
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
        log.info("Tạo URL thanh toán thành công cho giao dịch: {}", vnp_TxnRef);

        return paymentUrl;
    }

    @Transactional
    @Override
    public PaymentAppointmentResponse processPaymentReturn(HttpServletRequest request) {
        log.info("Xử lý phản hồi thanh toán VNPay: {}", request.getQueryString());

        Map<String, String> vnpParams = new HashMap<>();
        for (String paramName : request.getParameterMap().keySet()) {
            vnpParams.put(paramName, request.getParameter(paramName));
        }

        String vnpSecureHash = vnpParams.remove("vnp_SecureHash");
        String hashData = VNPayUtil.getPaymentURL(vnpParams, false);
        String calculatedHash = VNPayUtil.hmacSHA512(vnpayConfig.getSecretKey(), hashData);
        if (!calculatedHash.equalsIgnoreCase(vnpSecureHash)) {
            throw new SecurityException("Mã bảo mật không hợp lệ");
        }

        String vnpResponseCode = vnpParams.get("vnp_ResponseCode");
        String vnpTransactionNo = vnpParams.get("vnp_TransactionNo");
        String vnpPayDate = vnpParams.get("vnp_PayDate");
        String vnpTxnRef = vnpParams.get("vnp_TxnRef");

        Payment payment = paymentRepository.findByTransactionId(vnpTxnRef)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy giao dịch thanh toán với mã: " + vnpTxnRef));

        if ("00".equals(vnpResponseCode)) {
            payment.setPaymentStatus(PaymentStatus.COMPLETED);
            payment.setTransactionId(vnpTransactionNo);
            payment.setPaymentDate(VNPayUtil.parseVNPayDate(vnpPayDate));
            paymentRepository.save(payment);
            appointmentService.confirmAppointment(payment.getAppointment().getId());
            log.info("Thanh toán thành công cho giao dịch: {}", vnpTransactionNo);
        } else {
            payment.setPaymentStatus(PaymentStatus.FAILED);
            paymentRepository.save(payment);
            appointmentService.cancelAppointment(payment.getAppointment().getId());
            log.info("Thanh toán thất bại cho giao dịch: {}", vnpTxnRef);
        }
        return paymentMapper.toPaymentAppointmentResponse(payment);
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
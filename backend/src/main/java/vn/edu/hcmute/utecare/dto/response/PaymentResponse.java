package vn.edu.hcmute.utecare.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.hcmute.utecare.util.enumeration.PaymentMethod;
import vn.edu.hcmute.utecare.util.enumeration.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
//    private Long id;
//    private BigDecimal amount;
//    private PaymentMethod paymentMethod;
//    private PaymentStatus paymentStatus;
//    private LocalDateTime paymentDate;
//    private String transactionId;
//    private Long appointmentId;
//    private String returnUrl;
    // VNPay-specific response fields

    //Mã phản hồi kết quả thanh toán. Quy định mã trả lời 00 ứng với kết quả Thành công cho tất cả các API.
//    private String vnpResponseCode; // VNPay response code (e.g., "00" for success)
//
//    //Mã giao dịch ghi nhận tại hệ thống VNPAY. Ví dụ: 20170829153052
//    private String vnpTransactionNo; // VNPay transaction number
//
//    //vnp_BankCode=VNPAYQR -- Thanh toán quét mã QR
//    //vnp_BankCode=VNBANK -- Thẻ ATM - Tài khoản ngân hàng nội địa
//    //vnp_BankCode=INTCARD -- Thẻ thanh toán quốc tế
//    private String vnpBankCode;      // Bank code used for payment
//
//    //Thời gian thanh toán. Định dạng: yyyyMMddHHmmss
//    private String vnpPayDate;       // Payment date from VNPay
    private Long id;
    private BigDecimal amount;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private LocalDateTime paymentDate;
    private String transactionId;
    private Long appointmentId;
    private String vnpResponseCode;
    private String vnpTransactionNo;
    private String vnpBankCode;
    private String vnpPayDate;
    private String paymentUrl;
}

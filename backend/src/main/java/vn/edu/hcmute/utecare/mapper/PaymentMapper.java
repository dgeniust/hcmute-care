package vn.edu.hcmute.utecare.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import vn.edu.hcmute.utecare.dto.request.PaymentRequest;
import vn.edu.hcmute.utecare.dto.response.PaymentResponse;
import vn.edu.hcmute.utecare.model.Payment;

import java.util.Map;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PaymentMapper {

    PaymentMapper INSTANCE = Mappers.getMapper(PaymentMapper.class);

    Payment toEntity(PaymentRequest paymentRequest);

//    @Mapping(target = "appointmentId", source = "appointment.id")
    PaymentResponse toResponse(Payment payment, String returnUrl);

//    default PaymentResponse createPaymentResponse(Payment payment, String returnUrl) {
//        return PaymentResponse.builder()
//                .id(payment.getId())
//                .amount(payment.getAmount())
//                .paymentMethod(payment.getPaymentMethod())
//                .paymentStatus(payment.getPaymentStatus())
//                .paymentDate(payment.getPaymentDate())
//                .transactionId(payment.getTransactionId())
//                .appointmentId(payment.getAppointment().getId())
//                .returnUrl(returnUrl)
////                .vnpResponseCode(getVNPayConfig.get("vnp_ResponseCode"))
////                .vnpTransactionNo(getVNPayConfig.get("vnp_TransactionNo"))
////                .vnpBankCode(getVNPayConfig.get("vnp_BankCode"))
////                .vnpPayDate(getVNPayConfig.get("vnp_PayDate"))
//                .build();
//    }
}

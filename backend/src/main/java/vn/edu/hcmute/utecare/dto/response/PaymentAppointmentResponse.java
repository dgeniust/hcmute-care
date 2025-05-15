package vn.edu.hcmute.utecare.dto.response;

import lombok.Getter;
import lombok.Setter;
import vn.edu.hcmute.utecare.util.enumeration.PaymentMethod;
import vn.edu.hcmute.utecare.util.enumeration.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class PaymentAppointmentResponse {
    private Long id;
    private BigDecimal amount;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private LocalDateTime paymentDate;
    private String transactionId;
    private AppointmentResponse appointment;
}

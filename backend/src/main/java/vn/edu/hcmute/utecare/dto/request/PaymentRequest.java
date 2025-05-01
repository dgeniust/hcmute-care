package vn.edu.hcmute.utecare.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class PaymentRequest {
    @NotNull(message = "bank code is required")
    private String bankCode;

    @NotNull(message = "appointment id is required")
    private Long appointmentId;
}

package vn.edu.hcmute.utecare.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
public class MedicineRequest {

    @NotNull(message = "medicine name is required")
    private String name;

    @NotNull(message = "medicine price is required")
    private BigDecimal price;

    @NotNull(message = "medicine usage is required")
    private String medicineUsage;

    @NotNull(message = "medicine strength is required")
    private String strength;
}
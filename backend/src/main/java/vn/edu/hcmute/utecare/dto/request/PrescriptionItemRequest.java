package vn.edu.hcmute.utecare.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class PrescriptionItemRequest {
    @NotNull(message = "dosage is required")
    private String dosage;

    @NotNull(message = "quantity is required")
    private Integer quantity;

    @NotNull(message = "unit is required")
    private String unit;

    @NotNull(message = "Medicine ID is required")
    private Long medicineId;

    @NotNull(message = "Prescription ID is required")
    private Long prescriptionId;

}
package vn.edu.hcmute.utecare.dto.request;

import lombok.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecordRequest {
    @NotNull(message = "Patient information is required")
    private PatientRequest patient;

    @NotBlank(message = "Barcode is required")
    private String barcode;

    @NotNull(message = "Customer ID is required")
    private Long customerId;
}
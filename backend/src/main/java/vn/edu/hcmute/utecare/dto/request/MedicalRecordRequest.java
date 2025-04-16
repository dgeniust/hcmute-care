package vn.edu.hcmute.utecare.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import vn.edu.hcmute.utecare.dto.request.PatientRequest;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecordRequest {
    @NotNull(message = "Patient details are required")
    private PatientRequest patient;

    @NotNull(message = "Customer ID is required")
    private Long customerId;
}
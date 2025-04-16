package vn.edu.hcmute.utecare.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecordResponse {
    private Long id;
    private String barcode;
    private Long customerId;
    private PatientResponse patient;
}
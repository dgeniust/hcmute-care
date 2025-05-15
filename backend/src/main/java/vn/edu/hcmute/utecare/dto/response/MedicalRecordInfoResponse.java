package vn.edu.hcmute.utecare.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MedicalRecordInfoResponse {
    private Long id;

    private String barcode;

    private Long patientId;

    private String patientName;

    private String dob;

    private String gender;

    private String phone;
}

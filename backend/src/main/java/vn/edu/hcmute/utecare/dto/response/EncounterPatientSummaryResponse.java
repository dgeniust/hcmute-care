package vn.edu.hcmute.utecare.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@Builder
public class EncounterPatientSummaryResponse {
    private Long id;

    private String treatment;

    private String diagnosis;

    private LocalDate visitDate;

    private String notes;

    private List<PrescriptionResponse> prescriptions;

    private MedicalRecordResponse medicalRecord;
}

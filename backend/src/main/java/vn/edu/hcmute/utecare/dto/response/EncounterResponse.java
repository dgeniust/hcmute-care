package vn.edu.hcmute.utecare.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.time.LocalDate;
@Getter
@Setter
@Builder
public class EncounterResponse {
    private Long id;

    private String treatment;

    private String diagnosis;

    private LocalDate visitDate;

    private String notes;

    private List<Long> prescriptionId;

    private Long medicalRecordId;
}

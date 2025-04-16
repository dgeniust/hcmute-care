package vn.edu.hcmute.utecare.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class EncounterRequest {
    @NotNull(message = "treatment must not be null")
    private String treatment;

    @NotNull(message = "diagnosis must not be null")
    private String diagnosis;

    @NotNull(message = "visitDate must not be null")
    private LocalDate visitDate;

    @NotNull(message = "notes must not be null")
    private String notes;

    @NotNull(message = "Prescription ID is required")
    private Long prescriptionId;

    @NotNull(message = "Medical Record ID is required")
    private Long medicalRecordId;
}

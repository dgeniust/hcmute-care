package vn.edu.hcmute.utecare.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.edu.hcmute.utecare.util.enumeration.EMedicalTest;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SpirometryRequest {

    private String evaluate;

    private String notes;

    @NotNull(message = "Encounter ID cannot be null")
    private Long encounterId;

    private String testEnvironment;

    private String patientPosition;

    private String testName;

    private String organSystem;

    private Boolean isInvasive;

    private Boolean isQuantitative;

    private Integer recordDuration;

    private Float fevl;

    private Float fvc;


    private EMedicalTest status;

}

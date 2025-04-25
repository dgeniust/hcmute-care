package vn.edu.hcmute.utecare.dto.request;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NerveConductionRequest {
    @NotBlank(message = "Evaluation cannot be blank")
    private String evaluate;

    private String notes;

    @NotNull(message = "Encounter ID cannot be null")
    private Long encounterId;

    @NotBlank(message = "Test Environment cannot be blank")
    private String testEnvironment;

    @NotBlank(message = "Patient Position cannot be blank")
    private String patientPosition;

    @NotBlank(message = "Test name cannot be blank")
    private String testName;

    @NotBlank(message = "Organ system cannot be blank")
    private String organSystem;

    private Boolean isInvasive;

    private Boolean isQuantitative;

    private Integer recordDuration;

    @NotBlank(message = "Nerve cannot be blank")
    private String nerve;

    private Float conductionSpeed;

}

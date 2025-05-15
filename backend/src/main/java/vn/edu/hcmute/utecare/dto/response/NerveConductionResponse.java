package vn.edu.hcmute.utecare.dto.response;

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
public class NerveConductionResponse {
    private Long id;
    private String evaluate;
    private String notes;
    private Long encounterId;
    private String testEnvironment;
    private String patientPosition;
    private String testName;
    private String organSystem;
    private Boolean isInvasive;
    private Boolean isQuantitative;
    private Integer recordDuration;
    private String nerve;
    private Float conductionSpeed;
    private EMedicalTest status;
    private LocalDateTime createDate;
}

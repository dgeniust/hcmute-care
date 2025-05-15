package vn.edu.hcmute.utecare.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.edu.hcmute.utecare.util.enumeration.EMedicalTest;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EMGRequest {

    private String evaluate;

    private String notes;

    @NotNull(message = "Encounter ID cannot be null")
    private Long encounterId;

    private String testName;

    private String organSystem;

    private Boolean isInvasive;

    private Boolean isQuantitative;

    private Integer recordDuration;

    private String image;

    private String muscleGroup;

    private EMedicalTest status;

}

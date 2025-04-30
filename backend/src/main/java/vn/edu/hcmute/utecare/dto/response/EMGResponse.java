package vn.edu.hcmute.utecare.dto.response;


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
public class EMGResponse {
    private Long id;
    private String evaluate;
    private String notes;
    private Long encounterId;
    private String testName;
    private String organSystem;
    private Boolean isInvasive;
    private Boolean isQuantitative;
    private Integer recordDuration;
    private String image;
    private String muscleGroup;
}

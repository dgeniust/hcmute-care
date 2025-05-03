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
public class LaboratoryTestsRequest {

    @NotBlank(message = "Evaluation cannot be blank")
    private String evaluate;

    private String notes;

    @NotNull(message = "Encounter ID cannot be null")
    private Long encounterId;

    private float rbc;
    private float hct;
    private float hgb;
    private float mcv;
    private float mch;
    private float olt;
    private float wbc;
    private float gra;
    private float lym;
    private float momo;

    private EMedicalTest status;

}
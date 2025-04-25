package vn.edu.hcmute.utecare.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LaboratoryTestsResponse {

    private Long id;
    private String evaluate;
    private String notes;
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
}
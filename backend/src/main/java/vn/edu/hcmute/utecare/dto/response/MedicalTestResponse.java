package vn.edu.hcmute.utecare.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicalTestResponse {

    private Long id;
    private String evaluate;
    private String notes;
    private Long encounterId;
}
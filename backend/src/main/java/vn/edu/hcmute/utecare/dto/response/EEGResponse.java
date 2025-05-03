package vn.edu.hcmute.utecare.dto.response;

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
public class EEGResponse {
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
    private Integer channels;
    private Boolean detectSeizure;
    private EMedicalTest status;
    private LocalDateTime createDate;
}

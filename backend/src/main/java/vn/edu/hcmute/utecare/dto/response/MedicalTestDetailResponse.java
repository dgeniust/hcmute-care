package vn.edu.hcmute.utecare.dto.response;

import lombok.Builder;
import lombok.Data;
import vn.edu.hcmute.utecare.util.enumeration.EMedicalTest;

import java.time.LocalDateTime;

@Data
@Builder
public class MedicalTestDetailResponse {
    private Long id;
    private String evaluate;
    private String notes;
    private Long encounterId;
    private LocalDateTime createDate;
    private EMedicalTest status;
    private String type;
    private Object details;
}
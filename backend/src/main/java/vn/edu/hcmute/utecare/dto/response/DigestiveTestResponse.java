package vn.edu.hcmute.utecare.dto.response;

import lombok.Data;
import vn.edu.hcmute.utecare.util.enumeration.EMedicalTest;

import java.time.LocalDateTime;

@Data
public class DigestiveTestResponse {
    private Long id; // Từ MedicalTest
    private String image; // Từ DigestiveTest
    private Integer duration; // Từ DigestiveTest
    private String testName; // Từ FunctionalTests
    private String organSystem; // Từ FunctionalTests
    private Boolean isInvasive; // Từ FunctionalTests
    private Boolean isQuantitative; // Từ FunctionalTests
    private Integer recordDuration; // Từ FunctionalTests
    private String evaluate; // Từ MedicalTest
    private String notes; // Từ MedicalTest
    private Long encounterId; // Từ MedicalTest

    private EMedicalTest status;
    private LocalDateTime createDate;
}
package vn.edu.hcmute.utecare.dto.response;

import lombok.Data;

@Data
public class CardiacTestResponse {
    private Long id; // Từ MedicalTest
    private String type; // Từ CardiacTest
    private String image; // Từ CardiacTest
    private String testName; // Từ FunctionalTests
    private String organSystem; // Từ FunctionalTests
    private Boolean isInvasive; // Từ FunctionalTests
    private Boolean isQuantitative; // Từ FunctionalTests
    private Integer recordDuration; // Từ FunctionalTests
    private String evaluate; // Từ MedicalTest
    private String notes; // Từ MedicalTest
    private Long encounterId; // Từ MedicalTest
}
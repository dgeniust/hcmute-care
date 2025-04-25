package vn.edu.hcmute.utecare.dto.request;

import lombok.Data;

@Data
public class DigestiveTestRequest {
    // Thuộc tính của DigestiveTest
    private String image;
    private Integer duration;

    // Thuộc tính của FunctionalTests
    private String testName;
    private String organSystem;
    private Boolean isInvasive;
    private Boolean isQuantitative;
    private Integer recordDuration;

    // Thuộc tính của MedicalTest
    private String evaluate;
    private String notes;
    private Long encounterId; // Để liên kết với Encounter
}
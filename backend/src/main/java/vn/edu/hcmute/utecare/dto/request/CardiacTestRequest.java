package vn.edu.hcmute.utecare.dto.request;

import lombok.Data;

@Data
public class CardiacTestRequest {
    // Thuộc tính của CardiacTest
    private String type; // Kiểu ECardiacTest (ví dụ: "ECG", "ECHO")
    private String image;

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
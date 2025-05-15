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
public class ImagingTestRequest {

    private String evaluate;

    private String notes;

    @NotNull(message = "Encounter ID cannot be null")
    private Long encounterId;

    private String pdfResult;
    private EMedicalTest status;
}

package vn.edu.hcmute.utecare.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class MedicalSpecialtyRequest {
    @NotNull(message = "Name must not be null")
    private String name;

    @Min(value = 0, message = "Price must be greater than or equal to 0")
    @NotNull(message = "Price must not be null")
    private Double price;

    private String note;
}

package vn.edu.hcmute.utecare.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PromptRequest {
    @NotBlank(message = "Prompt cannot be empty")
    @Size(max = 500, message = "Prompt cannot exceed 500 characters")
    private String prompt;
}
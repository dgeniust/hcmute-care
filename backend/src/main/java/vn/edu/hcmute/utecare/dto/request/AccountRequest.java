package vn.edu.hcmute.utecare.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class AccountRequest {
    @NotBlank(message = "Password is required")
    private String password;
}

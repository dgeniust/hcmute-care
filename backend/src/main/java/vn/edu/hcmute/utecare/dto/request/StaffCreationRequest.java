package vn.edu.hcmute.utecare.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class StaffCreationRequest {
    @NotNull(message = "accountRequest must not be null")
    private AccountRequest accountRequest;

    @NotNull(message = "staffRequest must not be null")
    private StaffRequest staffRequest;
}
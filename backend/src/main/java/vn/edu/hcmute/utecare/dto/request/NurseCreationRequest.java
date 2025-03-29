package vn.edu.hcmute.utecare.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class NurseCreationRequest {
    @NotNull(message = "accountRequest must not be null")
    private AccountRequest accountRequest;

    @NotNull(message = "nurseRequest must not be null")
    private NurseRequest nurseRequest;
}
package vn.edu.hcmute.utecare.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class DoctorCreationRequest {
    @NotNull(message = "accountRequest must not be null")
    private AccountRequest accountRequest;

    @NotNull(message = "doctorRequest must not be null")
    private DoctorRequest doctorRequest;
}

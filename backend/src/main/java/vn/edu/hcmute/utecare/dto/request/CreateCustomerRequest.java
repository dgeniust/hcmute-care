package vn.edu.hcmute.utecare.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CreateCustomerRequest {
    @NotNull(message = "accountRequest must not be null")
    private AccountRequest accountRequest;

    @NotNull(message = "customerRequest must not be null")
    private CustomerRequest customerRequest;
}

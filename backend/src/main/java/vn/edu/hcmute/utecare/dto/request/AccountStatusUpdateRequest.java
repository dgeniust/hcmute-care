package vn.edu.hcmute.utecare.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import vn.edu.hcmute.utecare.util.AccountStatus;
import vn.edu.hcmute.utecare.util.EnumValue;

@Getter
public class AccountStatusUpdateRequest {
    @NotBlank(message = "New status cannot be null")
    @EnumValue(name = "accountStatus", enumClass = AccountStatus.class)
    private AccountStatus accountStatus;
}

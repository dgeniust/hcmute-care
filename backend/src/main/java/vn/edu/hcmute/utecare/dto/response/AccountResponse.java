package vn.edu.hcmute.utecare.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import vn.edu.hcmute.utecare.util.AccountStatus;
import vn.edu.hcmute.utecare.util.Role;

@Getter
@Setter
@Builder
public class AccountResponse {
    private Long id;

    private String phone;

    private String role;

    private String status;

    private Long userId;

    private String userFullName;
}

package vn.edu.hcmute.utecare.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

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

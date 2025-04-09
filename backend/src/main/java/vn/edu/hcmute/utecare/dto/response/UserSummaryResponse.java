package vn.edu.hcmute.utecare.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import vn.edu.hcmute.utecare.util.enumeration.Gender;
import vn.edu.hcmute.utecare.util.enumeration.Role;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class UserSummaryResponse {
    private Long id;
    private String fullName;
    private String phone;
    private String email;
    private Gender gender;
    private LocalDate dob;
    private Role roleName;
}

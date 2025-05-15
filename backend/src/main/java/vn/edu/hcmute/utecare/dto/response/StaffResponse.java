package vn.edu.hcmute.utecare.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class StaffResponse {
    private Long id;
    private String phone;
    private String fullName;
    private String email;
    private String gender;
    private LocalDate dob;
    private String nation;
    private String address;
    private String staffRole;
}
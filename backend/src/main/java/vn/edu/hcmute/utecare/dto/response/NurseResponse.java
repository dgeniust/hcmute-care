package vn.edu.hcmute.utecare.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class NurseResponse {
    private Long id;
    private String phone;
    private String fullName;
    private String email;
    private String gender;
    private LocalDate dob;
    private String nation;
    private String address;
    private String position;
    private String qualification;
}

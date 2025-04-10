package vn.edu.hcmute.utecare.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import vn.edu.hcmute.utecare.util.enumeration.Gender;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@Builder
public class DoctorResponse {
    private Long id;

    private String phone;

    private String fullName;

    private String email;

    private Gender gender;

    private LocalDate dob;

    private String nation;

    private String address;

    private String position;

    private String qualification;
}

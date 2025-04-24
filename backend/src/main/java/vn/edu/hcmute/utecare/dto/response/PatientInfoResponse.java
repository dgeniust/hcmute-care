package vn.edu.hcmute.utecare.dto.response;

import lombok.Getter;
import lombok.Setter;
import vn.edu.hcmute.utecare.util.enumeration.Gender;

import java.time.LocalDate;

@Getter
@Setter
public class PatientInfoResponse {
    private Long id;

    private String name;

    private LocalDate dob;

    private Gender gender;
}

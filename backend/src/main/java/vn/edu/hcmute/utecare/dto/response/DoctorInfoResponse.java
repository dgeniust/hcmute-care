package vn.edu.hcmute.utecare.dto.response;

import lombok.Getter;
import lombok.Setter;
import vn.edu.hcmute.utecare.util.enumeration.Gender;

import java.time.LocalDate;

@Getter
@Setter
public class DoctorInfoResponse {
    private Long id;

    private String phone;

    private String fullName;

    private Gender gender;

    private String position;

    private MedicalSpecialtyResponse medicalSpecialty;
}

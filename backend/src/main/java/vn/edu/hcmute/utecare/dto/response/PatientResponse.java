package vn.edu.hcmute.utecare.dto.response;

import lombok.*;
import vn.edu.hcmute.utecare.util.enumeration.Gender;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientResponse {
    private Long id;
    private String name;
    private String cccd;
    private Date dob;
    private Gender gender;
    private String address;
    private String phone;
    private String email;
    private String nation;
    private String career;
}
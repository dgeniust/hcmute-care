package vn.edu.hcmute.utecare.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
public class DoctorResponse {
    private Long id;

    private String phone;

    private String fullName;

    private String email;

    private String gender;

    private Date dob;

    private String nation;

    private String address;

    private String position;

    private String qualification;
}

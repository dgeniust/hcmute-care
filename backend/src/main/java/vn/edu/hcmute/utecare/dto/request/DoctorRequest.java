package vn.edu.hcmute.utecare.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;
import vn.edu.hcmute.utecare.util.validator.EnumValue;
import vn.edu.hcmute.utecare.util.enumeration.Gender;
import vn.edu.hcmute.utecare.util.validator.PhoneNumber;

import java.time.LocalDate;

@Getter
public class DoctorRequest {
    @NotBlank(message = "phone must be not null")
    @PhoneNumber(message = "phone invalid format")
    private String phone;

    @NotEmpty(message = "fullName must be not null")
    private String fullName;

    @Email(message = "email invalid format")
    private String email;

    @NotNull(message = "gender must be not null")
    @EnumValue(name = "gender", enumClass = Gender.class)
    private String gender;

    @NotNull(message = "dateOfBirth must be not null")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dob;

    @NotEmpty(message = "nation must be not null")
    private String nation;

    @NotEmpty(message = "address must be not null")
    private String address;

    @NotEmpty(message = "position must be not null")
    private String position;

    @NotEmpty(message = "qualification must be not null")
    private String qualification;

    @Min(value = 1, message = "medicalSpecialtyId must be greater 0")
    private Integer medicalSpecialtyId;
}

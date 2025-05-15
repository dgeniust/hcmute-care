package vn.edu.hcmute.utecare.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;
import vn.edu.hcmute.utecare.util.validator.EnumValue;
import vn.edu.hcmute.utecare.util.enumeration.Gender;
import vn.edu.hcmute.utecare.util.enumeration.StaffRole;

import java.time.LocalDate;

@Getter
public class StaffRequest {
    @NotBlank(message = "phone must not be null")
    private String phone;

    @NotEmpty(message = "fullName must not be null")
    private String fullName;

    @Email(message = "email invalid format")
    private String email;

    @NotNull(message = "gender must not be null")
    @EnumValue(name = "gender", enumClass = Gender.class)
    private String gender;

    @NotNull(message = "dateOfBirth must not be null")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dob;

    @NotEmpty(message = "nation must not be null")
    private String nation;

    @NotEmpty(message = "address must not be null")
    private String address;

    @NotNull(message = "staffRole must not be null")
    @EnumValue(name = "staffRole", enumClass = StaffRole.class)
    private String staffRole;
}
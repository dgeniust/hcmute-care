package vn.edu.hcmute.utecare.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;
import vn.edu.hcmute.utecare.util.EnumValue;
import vn.edu.hcmute.utecare.util.Gender;

import java.time.LocalDate;

@Getter
public class NurseRequest {
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
    @JsonFormat(pattern = "MM/dd/yyyy")
    private LocalDate dob;

    @NotEmpty(message = "nation must not be null")
    private String nation;

    @NotEmpty(message = "address must not be null")
    private String address;

    @NotEmpty(message = "position must not be null")
    private String position;

    @NotEmpty(message = "qualification must not be null")
    private String qualification;
}
package vn.edu.hcmute.utecare.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import vn.edu.hcmute.utecare.util.enumeration.Gender;
import vn.edu.hcmute.utecare.util.validator.PhoneNumber;

import java.time.LocalDate;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientRequest {
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "CCCD is required")
    @Size(min = 12, max = 12, message = "CCCD must be 12 digits")
    private String cccd;

    @NotNull(message = "Date of birth is required")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dob;

    @NotNull(message = "Gender is required")
    private Gender gender;

    private String address;

    @PhoneNumber(message = "Invalid phone number format")
    private String phone;

    @Email(message = "Invalid email format")
    private String email;

    private String nation;

    private String career;
}
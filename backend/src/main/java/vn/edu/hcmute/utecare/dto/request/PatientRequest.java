package vn.edu.hcmute.utecare.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;
import vn.edu.hcmute.utecare.util.enumeration.Gender;

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
    @Past(message = "Date of birth must be in the past")
    private Date dob;

    @NotNull(message = "Gender is required")
    private Gender gender;

    private String address;

    @Pattern(regexp = "^\\d{10}$", message = "Phone must be 10 digits")
    private String phone;

    @Email(message = "Invalid email format")
    private String email;

    private String nation;

    private String career;
}
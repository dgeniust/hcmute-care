package vn.edu.hcmute.utecare.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import vn.edu.hcmute.utecare.model.PostImage;
import vn.edu.hcmute.utecare.model.Staff;
import vn.edu.hcmute.utecare.util.enumeration.Gender;
import vn.edu.hcmute.utecare.util.validator.EnumValue;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
public class PostRequest {
    @NotBlank(message = "header must not be null")
    private String header;

    @NotNull(message = "content must not be null")
    private String content;

    @NotNull(message = "dateOfCreate must not be null")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate doc;

    @NotBlank(message = "postImage must not be null")
    private Set<PostImageRequest> postImages;

    @NotBlank(message = "staff must not be null")
    private Long staffId;

}

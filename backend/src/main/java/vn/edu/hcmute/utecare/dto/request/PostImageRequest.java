package vn.edu.hcmute.utecare.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class PostImageRequest {
    @NotBlank(message = "Image URL must not be null or empty")
    private String imageUrl;
}

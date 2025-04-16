package vn.edu.hcmute.utecare.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import vn.edu.hcmute.utecare.model.PostImage;
import vn.edu.hcmute.utecare.model.Staff;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@Builder
public class PostResponse {
    private Long id;

    private String header;

    private String content;

    private LocalDate doc;

    private Set<PostImageResponse> postImages;

    private Long staffId;
}

package vn.edu.hcmute.utecare.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MedicalSpecialtyResponse {
    private Integer id;

    private String name;

    private Double price;

    private String note;
}

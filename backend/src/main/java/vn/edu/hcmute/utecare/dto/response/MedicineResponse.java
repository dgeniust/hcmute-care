package vn.edu.hcmute.utecare.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class MedicineResponse {
    private Long id;

    private String name;

    private BigDecimal price;

    private String medicineUsage;

    private String strength;
}

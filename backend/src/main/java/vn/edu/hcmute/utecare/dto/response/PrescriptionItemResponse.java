package vn.edu.hcmute.utecare.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PrescriptionItemResponse {

    private Long id;

    private String name;

    private String dosage;

    private Integer quantity;

    private String unit;

    private Long medicineId;

    private Long prescriptionId;
}

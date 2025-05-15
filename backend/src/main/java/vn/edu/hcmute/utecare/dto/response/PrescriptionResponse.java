package vn.edu.hcmute.utecare.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import vn.edu.hcmute.utecare.util.enumeration.PrescriptionStatus;
import java.util.List;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class PrescriptionResponse {
    private Long id;

    private LocalDateTime issueDate;

    private PrescriptionStatus status;

    private Long encounterId;

    private List<PrescriptionItemResponse> prescriptionItems;
}

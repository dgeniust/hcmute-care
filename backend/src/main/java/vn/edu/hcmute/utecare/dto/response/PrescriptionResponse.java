package vn.edu.hcmute.utecare.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import vn.edu.hcmute.utecare.model.Encounter;
import vn.edu.hcmute.utecare.model.PrescriptionItem;
import vn.edu.hcmute.utecare.util.enumeration.PrescriptionStatus;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Builder
public class PrescriptionResponse {
    private Long id;

    private LocalDateTime issueDate;

    private PrescriptionStatus status;

    private Encounter encounter;

    private Set<PrescriptionItem> prescriptionItems;
}

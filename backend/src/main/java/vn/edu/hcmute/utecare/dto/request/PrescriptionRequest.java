package vn.edu.hcmute.utecare.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import vn.edu.hcmute.utecare.util.enumeration.PrescriptionStatus;
import vn.edu.hcmute.utecare.util.validator.EnumValue;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class PrescriptionRequest {

    @NotNull(message = "Issue date is required")
    private LocalDateTime issueDate;

    @NotNull(message = "Status is required")
    @EnumValue(name = "prescriptionStatus", enumClass = PrescriptionStatus.class)
    private String status;

    //List of prescription items request (IDs, quantity, etc.)
    @NotNull(message = "Prescription items are required")
    private List<PrescriptionItemRequest> prescriptionItems;

    @NotNull(message = "Encounter id is required")
    private Long encounterId;
}

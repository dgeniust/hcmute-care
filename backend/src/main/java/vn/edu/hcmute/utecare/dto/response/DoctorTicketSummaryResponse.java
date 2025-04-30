package vn.edu.hcmute.utecare.dto.response;

import lombok.Setter;
import vn.edu.hcmute.utecare.util.enumeration.TicketStatus;

@Setter
public class DoctorTicketSummaryResponse {
    private Long id;

    private String ticketCode;

    private TicketStatus status;

    private Integer waitingNumber;

    private String patientName;

    private String patientGender;

    private String patientDob;

    private String medicalRecordId;

    private String medicalRecordBarcode;
}

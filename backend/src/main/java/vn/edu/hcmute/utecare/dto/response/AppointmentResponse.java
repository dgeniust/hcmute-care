package vn.edu.hcmute.utecare.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class AppointmentResponse {
    private Long id;

    private MedicalRecordInfoResponse medicalRecord;

    private Set<TicketResponse> tickets;
}

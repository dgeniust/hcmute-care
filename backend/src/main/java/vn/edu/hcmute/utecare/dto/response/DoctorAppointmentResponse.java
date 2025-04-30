package vn.edu.hcmute.utecare.dto.response;

import lombok.Getter;
import lombok.Setter;
import vn.edu.hcmute.utecare.util.enumeration.TicketStatus;

import java.time.LocalDate;

@Getter
@Setter
public class DoctorAppointmentResponse {
    private Long appointmentId;

    private Integer waitingNumber;

    private TicketStatus status;

    private LocalDate date;

    private PatientInfoResponse patient;

    private TimeSlotResponse timeSlot;
}

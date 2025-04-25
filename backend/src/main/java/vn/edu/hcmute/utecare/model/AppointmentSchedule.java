package vn.edu.hcmute.utecare.model;

import jakarta.persistence.*;
import lombok.*;
import vn.edu.hcmute.utecare.util.enumeration.AppointmentStatus;

@Entity
@Table(name = "tbl_appointment_schedule")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "appointment_id", referencedColumnName = "id")
    private Appointment appointment;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;

    @ManyToOne
    @JoinColumn(name = "schedule_id", referencedColumnName = "id")
    private Schedule schedule;

    @Column(name = "ticket_code", unique = true)
    private String ticketCode;

    @Column(name = "waiting_number")
    private Integer waitingNumber;
}

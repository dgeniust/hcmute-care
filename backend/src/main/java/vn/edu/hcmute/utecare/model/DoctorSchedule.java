package vn.edu.hcmute.utecare.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "tbl_doctor_schedule")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "doctor_id", referencedColumnName = "id")
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "time_slot_id", referencedColumnName = "id")
    private TimeSlot timeSlot;

    @ManyToOne
    @JoinColumn(name = "room_detail_id", referencedColumnName = "id")
    private RoomDetail roomDetail;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "max_slots")
    private Integer maxSlots;

    @Column(name = "booked_slots")
    private Integer bookedSlots;
}

package vn.edu.hcmute.utecare.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "tbl_encounter")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Encounter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "treatment")
    private String treatment;

    @Column(name = "diagnosis")
    private String diagnosis;

    @Column(name = "visit_date")
    @Temporal(TemporalType.DATE)
    private Date visitDate;

    @Column(name = "notes")
    private String notes;

    @OneToOne
    @JoinColumn(name = "prescription_id", referencedColumnName = "id")
    private Prescription prescription;

    @ManyToOne
    @JoinColumn(name = "medical_record_id", referencedColumnName = "id")
    private MedicalRecord medicalRecord;
}

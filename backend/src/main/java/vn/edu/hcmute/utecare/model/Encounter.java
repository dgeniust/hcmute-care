package vn.edu.hcmute.utecare.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

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
    private LocalDate visitDate;

    @Column(name = "notes")
    private String notes;

    @OneToMany(mappedBy = "encounter")
    private Set<Prescription> prescriptions;

    @ManyToOne
    @JoinColumn(name = "medical_record_id", referencedColumnName = "id")
    private MedicalRecord medicalRecord;
}

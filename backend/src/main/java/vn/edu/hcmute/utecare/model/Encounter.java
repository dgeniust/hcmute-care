package vn.edu.hcmute.utecare.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
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
    @Builder.Default
    @JsonManagedReference
    private Set<Prescription> prescriptions = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medical_record_id", referencedColumnName = "id")
    @JsonBackReference
    private MedicalRecord medicalRecord;
}

package vn.edu.hcmute.utecare.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "tbl_nurse")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Nurse extends User {
    @Column(name = "position")
    private String position;

    @Column(name = "qualification")
    private String qualification;

    @ManyToOne
    @JoinColumn(name = "medical_specialty_id", referencedColumnName = "id")
    private MedicalSpecialty medicalSpecialty;
}

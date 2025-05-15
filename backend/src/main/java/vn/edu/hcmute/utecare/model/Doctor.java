package vn.edu.hcmute.utecare.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tbl_doctor")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Doctor extends User {
    @Column(name = "position")
    private String position;

    @Column(name = "qualification")
    private String qualification;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medical_specialty_id", referencedColumnName = "id")
    @JsonBackReference
    private MedicalSpecialty medicalSpecialty;
}

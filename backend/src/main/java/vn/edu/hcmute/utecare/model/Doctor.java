package vn.edu.hcmute.utecare.model;

import jakarta.persistence.*;
import lombok.*;
import vn.edu.hcmute.utecare.util.Membership;

@Entity
@Table(name = "tbl_doctor")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Doctor extends User {
    @Column(name = "position")
    private String position;

    @Column(name = "qualification")
    private String qualification;

    @ManyToOne
    @JoinColumn(name = "medical_specialty_id", referencedColumnName = "id")
    private MedicalSpecialty medicalSpecialty;
}

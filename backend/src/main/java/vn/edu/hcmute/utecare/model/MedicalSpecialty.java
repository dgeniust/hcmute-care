package vn.edu.hcmute.utecare.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "tbl_medical_specialty")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicalSpecialty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", columnDefinition = "TEXT")
    private String name;

    @Column(name = "price")
    private Double price;

    @Column(name =  "note")
    private String note;

    @OneToMany(mappedBy = "medicalSpecialty")
    private Set<Doctor> doctors;
}


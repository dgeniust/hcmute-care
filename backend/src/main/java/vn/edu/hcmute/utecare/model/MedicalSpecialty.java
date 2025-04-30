package vn.edu.hcmute.utecare.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
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

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name =  "note")
    private String note;

    @OneToMany(mappedBy = "medicalSpecialty", cascade = CascadeType.ALL)
    @JsonManagedReference
    @Builder.Default
    private Set<Doctor> doctors = new java.util.HashSet<>();

    @OneToMany(mappedBy = "medicalSpecialty", cascade = CascadeType.ALL)
    @JsonManagedReference
    @Builder.Default
    private Set<Nurse> nurses = new java.util.HashSet<>();
}


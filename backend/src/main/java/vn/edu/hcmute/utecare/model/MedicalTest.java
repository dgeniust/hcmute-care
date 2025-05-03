package vn.edu.hcmute.utecare.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import vn.edu.hcmute.utecare.util.enumeration.EMedicalTest;
import vn.edu.hcmute.utecare.util.enumeration.PaymentStatus;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_medical_test")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class MedicalTest implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "evaluate")
    private String evaluate;

    @Column(name = "notes")
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "encounter_id", nullable = false)
    private Encounter encounter;

    @CreationTimestamp
    @Column(name = "create_date", updatable = false)
    private LocalDateTime createDate;

    @Column(name = "medical_status")
    @Enumerated(EnumType.STRING)
    private EMedicalTest status;
}

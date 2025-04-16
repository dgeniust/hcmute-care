package vn.edu.hcmute.utecare.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbl_prescription_item")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dosage")
    private String dosage;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "unit")
    private String unit;

    @ManyToOne
    @JoinColumn(name = "medicine_id", referencedColumnName = "id")
    private Medicine medicine;

    @ManyToOne
    @JoinColumn(name = "prescription_id", referencedColumnName = "id")
    private Prescription prescription;
}


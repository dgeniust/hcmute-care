package vn.edu.hcmute.utecare.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbl_medicine")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Medicine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private Double price;

    @Column(name = "medicine_usage")
    private String medicineUsage;

    @Column(name = "unit")
    private String unit;
}

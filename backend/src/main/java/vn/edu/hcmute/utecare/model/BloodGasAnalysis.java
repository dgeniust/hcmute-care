package vn.edu.hcmute.utecare.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;


@Entity
@Table(name = "tbl_blood_gas_analysis")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class BloodGasAnalysis extends RespiratoryTest implements Serializable {

    @Column(name = "pH")
    private float pH;

    @Column(name = "pCO2")
    private float pCO2;

    @Column(name = "pO2")
    private float pO2;

}

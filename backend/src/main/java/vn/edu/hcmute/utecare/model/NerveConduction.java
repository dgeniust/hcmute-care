package vn.edu.hcmute.utecare.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;


@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_nerve_conduction")
@Inheritance(strategy = InheritanceType.JOINED)
public class NerveConduction extends RespiratoryTest implements Serializable {

    @Column(name = "nerve")
    private String nerve;

    @Column(name = "conduction_speed")
    private float conductionSpeed;
}

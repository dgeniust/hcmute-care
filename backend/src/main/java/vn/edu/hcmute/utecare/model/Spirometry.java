package vn.edu.hcmute.utecare.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;


@Entity
@Table(name = "tbl_spirometry")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class Spirometry extends RespiratoryTest implements Serializable {

    @Column(name = "fevl")
    private float fevl;

    @Column(name = "fvc")
    private float fvc;
}

package vn.edu.hcmute.utecare.model;

import jakarta.persistence.*;
import lombok.*;
import vn.edu.hcmute.utecare.util.enumeration.ECardiacTest;


import java.io.Serializable;



@Entity
@Table(name = "tbl_cardiac_test")
@Getter
@Setter

@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class CardiacTest extends FunctionalTests implements Serializable {

    @Column(name = "image")
    private String image;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private ECardiacTest cardiacTest;

}

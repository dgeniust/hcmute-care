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
@Table(name = "tbl_neuro_test")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class NeuroTest extends FunctionalTests implements Serializable {

    @Column(name = "image")
    private String image;
}

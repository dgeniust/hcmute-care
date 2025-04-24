package vn.edu.hcmute.utecare.model;


import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "tbl_laboratory_test")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class LaboratoryTests extends MedicalTest implements Serializable  {

    @Column(name = "rbc")
    private float rbc;

    @Column(name = "hct")
    private float hct;


    @Column(name = "hgb")
    private float hgb;

    @Column(name = "mcv")
    private float mcv;


    @Column(name = "mch")
    private float mch;

    @Column(name = "plt")
    private float olt;

    @Column(name = "wbc")
    private float wbc;

    @Column(name = "gra")
    private float gra;

    @Column(name = "lym")
    private float lym;

    @Column(name = "momo")
    private float momo;



}

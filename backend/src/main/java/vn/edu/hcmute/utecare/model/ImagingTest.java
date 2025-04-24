package vn.edu.hcmute.utecare.model;


import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "tbl_imaging_test")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class ImagingTest extends MedicalTest implements Serializable {

    @Column(name = "pdfResult")
    private String pdfResult;
}

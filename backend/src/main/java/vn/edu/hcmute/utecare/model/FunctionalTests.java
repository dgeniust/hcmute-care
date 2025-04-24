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
@Table(name = "tbl_functional_tests")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class FunctionalTests extends MedicalTest implements Serializable {

    @Column(name = "test_name")
    private String testName;

    @Column(name = "organ_system")
    private String organSystem;

    @Column(name = "is_invasive")
    private Boolean isInvasive;

    @Column(name = "is_quantitative")
    private Boolean isQuantitative;

    @Column(name = "record_duration")
    private Integer recordDuration;
}
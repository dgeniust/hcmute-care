package vn.edu.hcmute.utecare.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "tbl_nurse")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Nurse extends User {
    @Column(name = "position")
    private String position;

    @Column(name = "qualification")
    private String qualification;
}

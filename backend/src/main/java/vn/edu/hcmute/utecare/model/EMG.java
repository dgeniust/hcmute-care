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
@Table(name = "tbl_emg")
@Inheritance(strategy = InheritanceType.JOINED)
public class EMG extends NeuroTest implements Serializable {

    @Column(name = "muscle_group")
    private String muscleGroup;
}

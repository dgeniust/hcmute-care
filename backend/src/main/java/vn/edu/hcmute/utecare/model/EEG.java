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
@Table(name = "tbl_eeg")
@Inheritance(strategy = InheritanceType.JOINED)
public class EEG extends NeuroTest implements Serializable {

    @Column(name = "channels")
    private Integer channels;

    @Column(name = "detects_seizure")
    private boolean detectSeizure;
}

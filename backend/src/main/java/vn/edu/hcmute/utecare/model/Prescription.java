package vn.edu.hcmute.utecare.model;

import jakarta.persistence.*;
import lombok.*;
import vn.edu.hcmute.utecare.util.enumeration.PrescriptionStatus;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tbl_prescription")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Prescription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "issue_date", nullable = false)
    private LocalDateTime issueDate;

    @Column(name = "status")
    private PrescriptionStatus status;

    @ManyToOne
    @JoinColumn(name = "encounter_id", referencedColumnName = "id")
    private Encounter encounter;

    @OneToMany(mappedBy = "prescription", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<PrescriptionItem> prescriptionItems = new HashSet<>();

}

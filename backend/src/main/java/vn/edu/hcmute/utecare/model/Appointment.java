package vn.edu.hcmute.utecare.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbl_appointment")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}

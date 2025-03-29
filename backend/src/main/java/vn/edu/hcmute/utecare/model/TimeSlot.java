package vn.edu.hcmute.utecare.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "tbl_time_slot")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimeSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "start_time")
    @Temporal(TemporalType.DATE)
    private Date startTime;

    @Column(name = "end_time")
    @Temporal(TemporalType.DATE)
    private Date endTime;
}

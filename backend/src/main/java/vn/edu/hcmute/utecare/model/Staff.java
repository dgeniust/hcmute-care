package vn.edu.hcmute.utecare.model;

import jakarta.persistence.*;
import lombok.*;
import vn.edu.hcmute.utecare.util.StaffRole;

@Entity
@Table(name = "tbl_staff")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Staff extends User {
    @Column(name = "staff_role")
    @Enumerated(EnumType.STRING)
    private StaffRole staffRole;
}

package vn.edu.hcmute.utecare.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import vn.edu.hcmute.utecare.util.enumeration.StaffRole;

@Entity
@Table(name = "tbl_staff")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Staff extends User {
    @Column(name = "staff_role")
    @Enumerated(EnumType.STRING)
    private StaffRole staffRole;
}

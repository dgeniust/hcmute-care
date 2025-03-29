package vn.edu.hcmute.utecare.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;
import vn.edu.hcmute.utecare.util.AdminRole;

@Entity
@Table(name = "tbl_admin")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Admin extends User {
    @Column(name = "admin_role")
    private AdminRole adminRole;
}

package vn.edu.hcmute.utecare.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.*;
import vn.edu.hcmute.utecare.util.Membership;

@Entity
@Table(name = "tbl_customer")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer extends User {
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Membership membership = Membership.NORMAL;
}

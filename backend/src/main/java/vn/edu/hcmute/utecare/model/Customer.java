package vn.edu.hcmute.utecare.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import vn.edu.hcmute.utecare.util.Membership;

@Entity
@Table(name = "tbl_customer")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Customer extends User {
    @Column(name = "career")
    private String career;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Membership membership = Membership.NORMAL;
}

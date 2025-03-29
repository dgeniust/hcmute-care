package vn.edu.hcmute.utecare.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import vn.edu.hcmute.utecare.util.Gender;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "phone", unique = true)
    private String phone;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "email")
    private String email;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "date_of_birth")
    @Temporal(TemporalType.DATE)
    private Date birthday;

    @Column(name = "nation")
    private String nation;

    @Column(name = "career")
    private String career;

    @Column(name = "address")
    private String address;
}

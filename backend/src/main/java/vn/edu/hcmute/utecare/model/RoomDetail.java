package vn.edu.hcmute.utecare.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbl_room_detail")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "building")
    private String building;

    @Column(name = "floor")
    private Integer floor;
}

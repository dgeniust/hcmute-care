package vn.edu.hcmute.utecare.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RoomDetailResponse {
    private Integer id;

    private String name;

    private String building;

    private Integer floor;
}

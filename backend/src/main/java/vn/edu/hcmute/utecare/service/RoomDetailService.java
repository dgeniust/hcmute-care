package vn.edu.hcmute.utecare.service;

import vn.edu.hcmute.utecare.dto.request.RoomDetailRequest;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.dto.response.RoomDetailResponse;

import java.util.List;

public interface RoomDetailService {
    RoomDetailResponse getRoomDetailById(Integer id);

    RoomDetailResponse createRoomDetail(RoomDetailRequest request);

    RoomDetailResponse updateRoomDetail(Integer id, RoomDetailRequest request);

    void deleteRoomDetail(Integer id);

    List<RoomDetailResponse> getAllRoomDetails();

    PageResponse<RoomDetailResponse> searchRoomDetails(String keyword, int page, int size, String sort, String direction);
}

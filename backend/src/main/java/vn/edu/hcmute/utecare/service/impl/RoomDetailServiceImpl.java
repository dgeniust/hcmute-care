package vn.edu.hcmute.utecare.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.edu.hcmute.utecare.dto.request.RoomDetailRequest;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.dto.response.RoomDetailResponse;
import vn.edu.hcmute.utecare.exception.ResourceNotFoundException;
import vn.edu.hcmute.utecare.mapper.RoomDetailMapper;
import vn.edu.hcmute.utecare.model.RoomDetail;
import vn.edu.hcmute.utecare.repository.RoomDetailRepository;
import vn.edu.hcmute.utecare.service.RoomDetailService;
import vn.edu.hcmute.utecare.util.PaginationUtil;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomDetailServiceImpl implements RoomDetailService {
    private final RoomDetailRepository roomDetailRepository;

    @Override
    public RoomDetailResponse getRoomDetailById(Integer id) {
        log.info("Fetching room detail with ID: {}", id);
        RoomDetail roomDetail = roomDetailRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room detail not found with ID: " + id));
        return RoomDetailMapper.INSTANCE.toResponse(roomDetail);
    }

    @Override
    public RoomDetailResponse createRoomDetail(RoomDetailRequest request){
        log.info("Creating room detail with request: {}", request);
        RoomDetail roomDetail = RoomDetailMapper.INSTANCE.toEntity(request);
        return RoomDetailMapper.INSTANCE.toResponse(roomDetailRepository.save(roomDetail));
    }

    @Override
    public RoomDetailResponse updateRoomDetail(Integer id, RoomDetailRequest request) {
        log.info("Updating room detail with ID: {}", id);
        RoomDetail roomDetail = roomDetailRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room detail not found with ID: " + id));
        RoomDetailMapper.INSTANCE.update(request, roomDetail);
        return RoomDetailMapper.INSTANCE.toResponse(roomDetailRepository.save(roomDetail));
    }

    @Override
    public void deleteRoomDetail(Integer id) {
        log.info("Deleting room detail with ID: {}", id);
        if (!roomDetailRepository.existsById(id)) {
            throw new ResourceNotFoundException("Room detail not found with ID: " + id);
        }
        roomDetailRepository.deleteById(id);
    }

    @Override
    public PageResponse<RoomDetailResponse> getAllRoomDetails(int page, int size, String sort, String direction) {
        log.info("Fetching all room details");
        Pageable pageable = PaginationUtil.createPageable(page, size, sort, direction);

        Page<RoomDetail> roomDetailPage = roomDetailRepository.findAll(pageable);


        return PageResponse.<RoomDetailResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(roomDetailPage.getTotalPages())
                .totalElements(roomDetailPage.getTotalElements())
                .content(roomDetailPage.getContent().stream().map(RoomDetailMapper.INSTANCE::toResponse).toList())
                .build();
    }

    @Override
    public PageResponse<RoomDetailResponse> searchRoomDetails(String keyword, int page, int size, String sort, String direction) {
        log.info("Fetching all room details with pagination: page {}, size {}", page, size);
        Pageable pageable = PaginationUtil.createPageable(page, size, sort, direction);

        Page<RoomDetail> roomDetailPage = roomDetailRepository.searchRoomDetails(keyword, pageable);


        return PageResponse.<RoomDetailResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(roomDetailPage.getTotalPages())
                .totalElements(roomDetailPage.getTotalElements())
                .content(roomDetailPage.getContent().stream().map(RoomDetailMapper.INSTANCE::toResponse).toList())
                .build();
    }

}

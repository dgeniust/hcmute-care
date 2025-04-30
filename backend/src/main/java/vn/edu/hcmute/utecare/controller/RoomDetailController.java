package vn.edu.hcmute.utecare.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmute.utecare.dto.request.RoomDetailRequest;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.dto.response.ResponseData;
import vn.edu.hcmute.utecare.dto.response.RoomDetailResponse;
import vn.edu.hcmute.utecare.service.RoomDetailService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/room-details")
@RequiredArgsConstructor
@Tag(name = "Room Detail", description = "Room Detail Management API")
@Slf4j(topic = "ROOM_DETAIL_CONTROLLER")
public class RoomDetailController {

    private final RoomDetailService roomDetailService;

    @GetMapping("/{id}")
    @Operation(summary = "Get room detail by ID", description = "Retrieve a room detail by its ID")
    public ResponseData<RoomDetailResponse> getRoomDetailById(@PathVariable("id") Integer id) {
        log.info("Get room detail request for id: {}", id);
        return ResponseData.<RoomDetailResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Room detail retrieved successfully")
                .data(roomDetailService.getRoomDetailById(id))
                .build();
    }

    @PostMapping
    @Operation(summary = "Create a new room detail", description = "Create a new room detail with the provided details")
    public ResponseData<RoomDetailResponse> createRoomDetail(
            @RequestBody @Valid RoomDetailRequest request) {
        log.info("Create room detail request: {}", request);
        return ResponseData.<RoomDetailResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Room detail created successfully")
                .data(roomDetailService.createRoomDetail(request))
                .build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update room detail", description = "Update an existing room detail by its ID")
    public ResponseData<RoomDetailResponse> updateRoomDetail(
            @PathVariable("id") Integer id,
            @RequestBody @Valid RoomDetailRequest request) {
        log.info("Update room detail request for id: {}", id);
        return ResponseData.<RoomDetailResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Room detail updated successfully")
                .data(roomDetailService.updateRoomDetail(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete room detail", description = "Delete a room detail by its ID")
    public ResponseData<Void> deleteRoomDetail(@PathVariable("id") Integer id) {
        log.info("Delete room detail request for id: {}", id);
        roomDetailService.deleteRoomDetail(id);
        return ResponseData.<Void>builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message("Room detail deleted successfully")
                .build();
    }

    @GetMapping
    @Operation(summary = "Get all room details", description = "Retrieve a list of all room details")
    public ResponseData<PageResponse<RoomDetailResponse>> getAllRoomDetails(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        log.info("Get all room details request: page={}, size={}, sort={}, direction={}", page, size, sort, direction);
        return ResponseData.<PageResponse<RoomDetailResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Room details retrieved successfully")
                .data(roomDetailService.getAllRoomDetails(page, size, sort, direction))
                .build();
    }

    @GetMapping("/search")
    @Operation(summary = "Search room details", description = "Search for room details by keyword")
    public ResponseData<PageResponse<RoomDetailResponse>> searchRoomDetails(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String direction) {
        log.info("Search room details request: keyword={}, page={}, size={}, sort={}, direction={}", keyword, page, size, sort, direction);
        return ResponseData.<PageResponse<RoomDetailResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Room details searched successfully")
                .data(roomDetailService.searchRoomDetails(keyword, page, size, sort, direction))
                .build();
    }
}
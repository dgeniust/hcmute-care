package vn.edu.hcmute.utecare.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmute.utecare.dto.request.RoomDetailRequest;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.dto.response.ResponseData;
import vn.edu.hcmute.utecare.dto.response.RoomDetailResponse;
import vn.edu.hcmute.utecare.service.RoomDetailService;

@RestController
@RequestMapping("/api/v1/room-details")
@RequiredArgsConstructor
@Tag(name = "ROOM-DETAIL", description = "API quản lý thông tin chi tiết về phòng trong hệ thống y tế")
@Slf4j(topic = "ROOM_DETAIL_CONTROLLER")
public class RoomDetailController {

    private final RoomDetailService roomDetailService;

    @GetMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE', 'CUSTOMER')")
    @Operation(
            summary = "Lấy thông tin chi tiết phòng theo ID",
            description = "Truy xuất thông tin chi tiết của một phòng dựa trên ID duy nhất."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy thông tin chi tiết phòng thành công"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy phòng với ID được cung cấp")
    })
    public ResponseData<RoomDetailResponse> getRoomDetailById(
            @Parameter(description = "ID của phòng cần truy xuất") @PathVariable("id") Integer id) {
        log.info("Yêu cầu lấy thông tin chi tiết phòng với ID: {}", id);
        return ResponseData.<RoomDetailResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy thông tin chi tiết phòng thành công")
                .data(roomDetailService.getRoomDetailById(id))
                .build();
    }

    @PostMapping
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @Operation(
            summary = "Tạo chi tiết phòng mới",
            description = "Tạo một bản ghi chi tiết phòng mới với thông tin được cung cấp."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tạo chi tiết phòng thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu yêu cầu không hợp lệ"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập")
    })
    public ResponseData<RoomDetailResponse> createRoomDetail(@Valid @RequestBody RoomDetailRequest request) {
        log.info("Yêu cầu tạo chi tiết phòng mới: {}", request);
        return ResponseData.<RoomDetailResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Tạo chi tiết phòng thành công")
                .data(roomDetailService.createRoomDetail(request))
                .build();
    }

    @PutMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @Operation(
            summary = "Cập nhật chi tiết phòng",
            description = "Cập nhật thông tin của một chi tiết phòng hiện có dựa trên ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cập nhật chi tiết phòng thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu yêu cầu không hợp lệ"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy phòng với ID được cung cấp")
    })
    public ResponseData<RoomDetailResponse> updateRoomDetail(
            @Parameter(description = "ID của phòng cần cập nhật") @PathVariable("id") Integer id,
            @Valid @RequestBody RoomDetailRequest request) {
        log.info("Yêu cầu cập nhật chi tiết phòng với ID: {}", id);
        return ResponseData.<RoomDetailResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Cập nhật chi tiết phòng thành công")
                .data(roomDetailService.updateRoomDetail(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @Operation(
            summary = "Xóa chi tiết phòng",
            description = "Xóa một chi tiết phòng hiện có dựa trên ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Xóa chi tiết phòng thành công"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy phòng với ID được cung cấp")
    })
    public ResponseData<Void> deleteRoomDetail(
            @Parameter(description = "ID của phòng cần xóa") @PathVariable("id") Integer id) {
        log.info("Yêu cầu xóa chi tiết phòng với ID: {}", id);
        roomDetailService.deleteRoomDetail(id);
        return ResponseData.<Void>builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message("Xóa chi tiết phòng thành công")
                .build();
    }

    @GetMapping
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE', 'CUSTOMER')")
    @Operation(
            summary = "Lấy danh sách tất cả chi tiết phòng",
            description = "Truy xuất danh sách tất cả các chi tiết phòng với phân trang và sắp xếp."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy danh sách chi tiết phòng thành công"),
            @ApiResponse(responseCode = "400", description = "Tham số phân trang hoặc sắp xếp không hợp lệ"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập")
    })
    public ResponseData<PageResponse<RoomDetailResponse>> getAllRoomDetails(
            @Parameter(description = "Số trang, bắt đầu từ 1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Số lượng bản ghi mỗi trang") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Trường để sắp xếp (ví dụ: id, name)") @RequestParam(defaultValue = "id") String sort,
            @Parameter(description = "Hướng sắp xếp: asc (tăng dần) hoặc desc (giảm dần)") @RequestParam(defaultValue = "asc") String direction) {
        log.info("Yêu cầu lấy danh sách chi tiết phòng: trang={}, kích thước={}, sắp xếp={}, hướng={}", page, size, sort, direction);
        return ResponseData.<PageResponse<RoomDetailResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy danh sách chi tiết phòng thành công")
                .data(roomDetailService.getAllRoomDetails(page, size, sort, direction))
                .build();
    }

    @GetMapping("/search")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE', 'CUSTOMER')")
    @Operation(
            summary = "Tìm kiếm chi tiết phòng",
            description = "Tìm kiếm các chi tiết phòng dựa trên từ khóa với phân trang và sắp xếp."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tìm kiếm chi tiết phòng thành công"),
            @ApiResponse(responseCode = "400", description = "Tham số tìm kiếm hoặc phân trang không hợp lệ"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập")
    })
    public ResponseData<PageResponse<RoomDetailResponse>> searchRoomDetails(
            @Parameter(description = "Từ khóa tìm kiếm (tùy chọn)") @RequestParam(required = false) String keyword,
            @Parameter(description = "Số trang, bắt đầu từ 1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Số lượng bản ghi mỗi trang") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Trường để sắp xếp (ví dụ: id, name)") @RequestParam(defaultValue = "id") String sort,
            @Parameter(description = "Hướng sắp xếp: asc (tăng dần) hoặc desc (giảm dần)") @RequestParam(defaultValue = "asc") String direction) {
        log.info("Yêu cầu tìm kiếm chi tiết phòng: từ khóa={}, trang={}, kích thước={}, sắp xếp={}, hướng={}", keyword, page, size, sort, direction);
        return ResponseData.<PageResponse<RoomDetailResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Tìm kiếm chi tiết phòng thành công")
                .data(roomDetailService.searchRoomDetails(keyword, page, size, sort, direction))
                .build();
    }
}
package vn.edu.hcmute.utecare.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmute.utecare.dto.request.SpirometryRequest;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.dto.response.ResponseData;
import vn.edu.hcmute.utecare.dto.response.SpirometryResponse;
import vn.edu.hcmute.utecare.service.SpirometryService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/spirometry")
@RequiredArgsConstructor
@Tag(name = "Spirometry", description = "Spirometry API")
@Slf4j(topic = "SPIROMETRY_CONTROLLER")
public class SpirometryController {

    private final SpirometryService spirometryService;

    @GetMapping
    @Operation(summary = "Lấy danh sách Spirometry có phân trang", description = "Lấy danh sách Spirometry với các tham số phân trang.")
    public ResponseData<PageResponse<SpirometryResponse>> getAllSpirometryWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String direction) {
        log.info("Yêu cầu lấy danh sách Spirometry có phân trang: page={}, size={}, sort={}, direction={}", page, size, sort, direction);
        PageResponse<SpirometryResponse> response = spirometryService.getAll(page, size, sort, direction);
        return ResponseData.<PageResponse<SpirometryResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Danh sách Spirometry phân trang được trả về thành công")
                .data(response)
                .build();
    }

    @GetMapping("/all")
    @Operation(summary = "Lấy toàn bộ danh sách Spirometry", description = "Lấy toàn bộ các bản ghi Spirometry trong hệ thống.")
    public ResponseData<List<SpirometryResponse>> getAllSpirometry() {
        log.info("Yêu cầu lấy toàn bộ danh sách Spirometry");
        List<SpirometryResponse> response = spirometryService.getAll();
        return ResponseData.<List<SpirometryResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Toàn bộ danh sách Spirometry được trả về thành công")
                .data(response)
                .build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy thông tin Spirometry theo ID", description = "Truy xuất chi tiết một bản ghi Spirometry dựa trên ID.")
    public ResponseData<SpirometryResponse> getSpirometryById(@PathVariable("id") Long id) {
        log.info("Yêu cầu lấy thông tin Spirometry với id: {}", id);
        SpirometryResponse response = spirometryService.getSpirometryById(id);
        return ResponseData.<SpirometryResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Thông tin Spirometry được lấy thành công")
                .data(response)
                .build();
    }

    @PostMapping
    @Operation(summary = "Tạo mới một Spirometry", description = "Tạo mới bản ghi Spirometry với dữ liệu được cung cấp.")
    public ResponseData<SpirometryResponse> createSpirometry(@RequestBody @Valid SpirometryRequest request) {
        log.info("Yêu cầu tạo mới Spirometry: {}", request);
        SpirometryResponse response = spirometryService.createSpirometry(request);
        return ResponseData.<SpirometryResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Spirometry được tạo thành công")
                .data(response)
                .build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật thông tin Spirometry theo ID", description = "Cập nhật thông tin bản ghi Spirometry theo ID.")
    public ResponseData<SpirometryResponse> updateSpirometry(
            @PathVariable("id") Long id,
            @RequestBody @Valid SpirometryRequest request) {
        log.info("Yêu cầu cập nhật Spirometry với id: {}", id);
        SpirometryResponse response = spirometryService.updateSpirometry(id, request);
        return ResponseData.<SpirometryResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Spirometry được cập nhật thành công")
                .data(response)
                .build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa Spirometry theo ID", description = "Xóa một bản ghi Spirometry dựa trên ID.")
    public ResponseData<Void> deleteSpirometry(@PathVariable("id") Long id) {
        log.info("Yêu cầu xóa Spirometry với id: {}", id);
        spirometryService.deleteSpirometry(id);
        return ResponseData.<Void>builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message("Spirometry được xóa thành công")
                .build();
    }
}

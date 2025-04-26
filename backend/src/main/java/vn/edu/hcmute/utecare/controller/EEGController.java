package vn.edu.hcmute.utecare.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmute.utecare.dto.request.EEGRequest;
import vn.edu.hcmute.utecare.dto.response.EEGResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.dto.response.ResponseData;
import vn.edu.hcmute.utecare.service.EEGService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/eeg")
@RequiredArgsConstructor
@Tag(name = "EEG", description = "EEG API")
@Slf4j(topic = "EEG_CONTROLLER")
public class EEGController {

    private final EEGService eegService;

    @GetMapping
    @Operation(summary = "Lấy danh sách EEG có phân trang", description = "Lấy danh sách EEG với các tham số phân trang.")
    public ResponseData<PageResponse<EEGResponse>> getAllEEGWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String direction) {
        log.info("Yêu cầu lấy danh sách EEG có phân trang: page={}, size={}, sort={}, direction={}", page, size, sort, direction);
        PageResponse<EEGResponse> response = eegService.getAll(page, size, sort, direction);
        return ResponseData.<PageResponse<EEGResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Danh sách EEG phân trang được trả về thành công")
                .data(response)
                .build();
    }

    @GetMapping("/all")
    @Operation(summary = "Lấy toàn bộ danh sách EEG", description = "Lấy toàn bộ các bản ghi EEG trong hệ thống.")
    public ResponseData<List<EEGResponse>> getAllEEG() {
        log.info("Yêu cầu lấy toàn bộ danh sách EEG");
        List<EEGResponse> response = eegService.getAll();
        return ResponseData.<List<EEGResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Toàn bộ danh sách EEG được trả về thành công")
                .data(response)
                .build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy thông tin EEG theo ID", description = "Truy xuất chi tiết một bản ghi EEG dựa trên ID.")
    public ResponseData<EEGResponse> getEEGById(@PathVariable("id") Long id) {
        log.info("Yêu cầu lấy thông tin EEG với id: {}", id);
        EEGResponse response = eegService.getEEGById(id);
        return ResponseData.<EEGResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Thông tin EEG được lấy thành công")
                .data(response)
                .build();
    }

    @PostMapping
    @Operation(summary = "Tạo mới một EEG", description = "Tạo mới bản ghi EEG với dữ liệu được cung cấp.")
    public ResponseData<EEGResponse> createEEG(@RequestBody @Valid EEGRequest request) {
        log.info("Yêu cầu tạo mới EEG: {}", request);
        EEGResponse response = eegService.createEEG(request);
        return ResponseData.<EEGResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("EEG được tạo thành công")
                .data(response)
                .build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật thông tin EEG theo ID", description = "Cập nhật thông tin bản ghi EEG theo ID.")
    public ResponseData<EEGResponse> updateEEG(
            @PathVariable("id") Long id,
            @RequestBody @Valid EEGRequest request) {
        log.info("Yêu cầu cập nhật EEG với id: {}", id);
        EEGResponse response = eegService.updateEEG(id, request);
        return ResponseData.<EEGResponse>builder()
                .status(HttpStatus.OK.value())
                .message("EEG được cập nhật thành công")
                .data(response)
                .build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa EEG theo ID", description = "Xóa một bản ghi EEG dựa trên ID.")
    public ResponseData<Void> deleteEEG(@PathVariable("id") Long id) {
        log.info("Yêu cầu xóa EEG với id: {}", id);
        eegService.deleteEEG(id);
        return ResponseData.<Void>builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message("EEG được xóa thành công")
                .build();
    }
}

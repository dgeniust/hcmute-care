package vn.edu.hcmute.utecare.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmute.utecare.dto.request.EMGRequest;
import vn.edu.hcmute.utecare.dto.response.DigestiveTestResponse;
import vn.edu.hcmute.utecare.dto.response.EMGResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.dto.response.ResponseData;
import vn.edu.hcmute.utecare.service.EMGService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/emg")
@RequiredArgsConstructor
@Tag(name = "EMG", description = "EMG API")
@Slf4j(topic = "EMG_CONTROLLER")
public class EMGController {

    private final EMGService emgService;

    @GetMapping
    @Operation(summary = "Lấy danh sách EMG có phân trang", description = "Lấy danh sách EMG với các tham số phân trang.")
    public ResponseData<PageResponse<EMGResponse>> getAllEMGWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String direction) {
        log.info("Yêu cầu lấy danh sách EMG có phân trang: page={}, size={}, sort={}, direction={}", page, size, sort, direction);
        PageResponse<EMGResponse> response = emgService.getAll(page, size, sort, direction);
        return ResponseData.<PageResponse<EMGResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Danh sách EMG phân trang được trả về thành công")
                .data(response)
                .build();
    }

    @GetMapping("/all")
    @Operation(summary = "Lấy toàn bộ danh sách EMG", description = "Lấy toàn bộ các bản ghi EMG trong hệ thống.")
    public ResponseData<List<EMGResponse>> getAllEMG() {
        log.info("Yêu cầu lấy toàn bộ danh sách EMG");
        List<EMGResponse> response = emgService.getAll();
        return ResponseData.<List<EMGResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Toàn bộ danh sách EMG được trả về thành công")
                .data(response)
                .build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy thông tin EMG theo ID", description = "Truy xuất chi tiết một bản ghi EMG dựa trên ID.")
    public ResponseData<EMGResponse> getEMGById(@PathVariable("id") Long id) {
        log.info("Yêu cầu lấy thông tin EMG với id: {}", id);
        EMGResponse response = emgService.getEMGById(id);
        return ResponseData.<EMGResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Thông tin EMG được lấy thành công")
                .data(response)
                .build();
    }

    @PostMapping
    @Operation(summary = "Tạo mới một EMG", description = "Tạo mới bản ghi EMG với dữ liệu được cung cấp.")
    public ResponseData<EMGResponse> createEMG(@RequestBody @Valid EMGRequest request) {
        log.info("Yêu cầu tạo mới EMG: {}", request);
        EMGResponse response = emgService.createEMG(request);
        return ResponseData.<EMGResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("EMG được tạo thành công")
                .data(response)
                .build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật thông tin EMG theo ID", description = "Cập nhật thông tin bản ghi EMG theo ID.")
    public ResponseData<EMGResponse> updateEMG(
            @PathVariable("id") Long id,
            @RequestBody @Valid EMGRequest request) {
        log.info("Yêu cầu cập nhật EMG với id: {}", id);
        EMGResponse response = emgService.updateEMG(id, request);
        return ResponseData.<EMGResponse>builder()
                .status(HttpStatus.OK.value())
                .message("EMG được cập nhật thành công")
                .data(response)
                .build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa EMG theo ID", description = "Xóa một bản ghi EMG dựa trên ID.")
    public ResponseData<Void> deleteEMG(@PathVariable("id") Long id) {
        log.info("Yêu cầu xóa EMG với id: {}", id);
        emgService.deleteEMG(id);
        return ResponseData.<Void>builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message("EMG được xóa thành công")
                .build();
    }

    @GetMapping("/by-date")
    @Operation(summary = "Lấy danh sách emg theo ngày", description = "Lấy danh sách emg của ngày được chỉ định với trạng thái PENDING.")
    public ResponseData<List<EMGResponse>> getAllLabTestByDateAndStatus(
            @RequestParam @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam("status") String status
    ) {
        log.info("Yêu cầu lấy danh sách emg theo ngày: {}", date);
        List<EMGResponse> responses = emgService.getAllLabTestByDateAndStatus(date, status);
        return ResponseData.<List<EMGResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Danh sách emg theo ngày được trả về thành công")
                .data(responses)
                .build();
    }

    @GetMapping("/by-encounter-and-date")
    @Operation(summary = "Lấy danh sách EMG theo encounterId và ngày", description = "Lấy danh sách EMG theo encounterId và ngày được chỉ định.")
    public ResponseData<List<EMGResponse>> getEncounterIdAndDate(
            @RequestParam Long encounterId,
            @RequestParam(required = false) @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("Yêu cầu lấy danh sách EMG theo encounterId: {} và ngày: {}", encounterId, date);
        List<EMGResponse> responses = emgService.getEncounterIdAndDate(encounterId, date);
        return ResponseData.<List<EMGResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Danh sách EMG theo encounterId và ngày được trả về thành công")
                .data(responses)
                .build();
    }


}

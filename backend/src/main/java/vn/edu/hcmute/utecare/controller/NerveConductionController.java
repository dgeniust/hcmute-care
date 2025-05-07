package vn.edu.hcmute.utecare.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmute.utecare.dto.request.NerveConductionRequest;
import vn.edu.hcmute.utecare.dto.response.DigestiveTestResponse;
import vn.edu.hcmute.utecare.dto.response.NerveConductionResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.dto.response.ResponseData;
import vn.edu.hcmute.utecare.service.NerveConductionService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/nerve-conduction")
@RequiredArgsConstructor
@Tag(name = "NerveConduction", description = "Nerve Conduction API")
@Slf4j(topic = "NERVE_CONDUCTION_CONTROLLER")
public class NerveConductionController {

    private final NerveConductionService nerveConductionService;

    @GetMapping
    @Operation(summary = "Lấy danh sách NerveConduction có phân trang", description = "Lấy danh sách NerveConduction với các tham số phân trang.")
    public ResponseData<PageResponse<NerveConductionResponse>> getAllNerveConductionWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String direction) {
        log.info("Yêu cầu lấy danh sách NerveConduction có phân trang: page={}, size={}, sort={}, direction={}", page, size, sort, direction);
        PageResponse<NerveConductionResponse> response = nerveConductionService.getAll(page, size, sort, direction);
        return ResponseData.<PageResponse<NerveConductionResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Danh sách NerveConduction phân trang được trả về thành công")
                .data(response)
                .build();
    }

    @GetMapping("/all")
    @Operation(summary = "Lấy toàn bộ danh sách NerveConduction", description = "Lấy toàn bộ các bản ghi NerveConduction trong hệ thống.")
    public ResponseData<List<NerveConductionResponse>> getAllNerveConduction() {
        log.info("Yêu cầu lấy toàn bộ danh sách NerveConduction");
        List<NerveConductionResponse> response = nerveConductionService.getAll();
        return ResponseData.<List<NerveConductionResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Toàn bộ danh sách NerveConduction được trả về thành công")
                .data(response)
                .build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy thông tin NerveConduction theo ID", description = "Truy xuất chi tiết một bản ghi NerveConduction dựa trên ID.")
    public ResponseData<NerveConductionResponse> getNerveConductionById(@PathVariable("id") Long id) {
        log.info("Yêu cầu lấy thông tin NerveConduction với id: {}", id);
        NerveConductionResponse response = nerveConductionService.getNerveConductionById(id);
        return ResponseData.<NerveConductionResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Thông tin NerveConduction được lấy thành công")
                .data(response)
                .build();
    }

    @PostMapping
    @Operation(summary = "Tạo mới một NerveConduction", description = "Tạo mới bản ghi NerveConduction với dữ liệu được cung cấp.")
    public ResponseData<NerveConductionResponse> createNerveConduction(@RequestBody @Valid NerveConductionRequest request) {
        log.info("Yêu cầu tạo mới NerveConduction: {}", request);
        NerveConductionResponse response = nerveConductionService.createNerveConduction(request);
        return ResponseData.<NerveConductionResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("NerveConduction được tạo thành công")
                .data(response)
                .build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật thông tin NerveConduction theo ID", description = "Cập nhật thông tin bản ghi NerveConduction theo ID.")
    public ResponseData<NerveConductionResponse> updateNerveConduction(
            @PathVariable("id") Long id,
            @RequestBody @Valid NerveConductionRequest request) {
        log.info("Yêu cầu cập nhật NerveConduction với id: {}", id);
        NerveConductionResponse response = nerveConductionService.updateNerveConduction(id, request);
        return ResponseData.<NerveConductionResponse>builder()
                .status(HttpStatus.OK.value())
                .message("NerveConduction được cập nhật thành công")
                .data(response)
                .build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa NerveConduction theo ID", description = "Xóa một bản ghi NerveConduction dựa trên ID.")
    public ResponseData<Void> deleteNerveConduction(@PathVariable("id") Long id) {
        log.info("Yêu cầu xóa NerveConduction với id: {}", id);
        nerveConductionService.deleteNerveConduction(id);
        return ResponseData.<Void>builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message("NerveConduction được xóa thành công")
                .build();
    }

    @GetMapping("/by-date")
    @Operation(summary = "Lấy danh sách NerveConduction theo ngày", description = "Lấy danh sách NerveConduction của ngày được chỉ định với trạng thái PENDING.")
    public ResponseData<List<NerveConductionResponse>> getAllLabTestByDateAndStatus(
            @RequestParam @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam("status") String status
    ) {
        log.info("Yêu cầu lấy danh sách LaboratoryTests theo ngày: {}", date);
        List<NerveConductionResponse> responses = nerveConductionService.getAllLabTestByDateAndStatus(date, status);
        return ResponseData.<List<NerveConductionResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Danh sách LaboratoryTests theo ngày được trả về thành công")
                .data(responses)
                .build();
    }
}

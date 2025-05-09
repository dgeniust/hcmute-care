package vn.edu.hcmute.utecare.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmute.utecare.dto.request.LaboratoryTestsRequest;
import vn.edu.hcmute.utecare.dto.response.LaboratoryTestsResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.dto.response.ResponseData;
import vn.edu.hcmute.utecare.service.LaboratoryTestsService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/laboratory-tests")
@RequiredArgsConstructor
@Tag(name = "Laboratory Tests", description = "Laboratory Tests API")
@Slf4j(topic = "LABORATORY_TESTS_CONTROLLER")
public class LaboratoryTestsController {

    private final LaboratoryTestsService laboratoryTestsService;

    @GetMapping
   // @PreAuthorize("hasAnyRole('DOCTOR', 'PATIENT')")
    @Operation(summary = "Lấy danh sách LaboratoryTests với phân trang", description = "Lấy danh sách các LaboratoryTests với hỗ trợ phân trang, sắp xếp theo các tham số page, size, sort và direction.")
    public ResponseData<PageResponse<LaboratoryTestsResponse>> getAllLaboratoryTestsWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String direction) {
        log.info("Yêu cầu lấy danh sách LaboratoryTests với phân trang: page={}, size={}, sort={}, direction={}", page, size, sort, direction);
        PageResponse<LaboratoryTestsResponse> response = laboratoryTestsService.getAll(page, size, sort, direction);
        return ResponseData.<PageResponse<LaboratoryTestsResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Danh sách LaboratoryTests phân trang được trả về thành công")
                .data(response)
                .build();
    }

    @GetMapping("/all")
    //@PreAuthorize("hasAnyRole('DOCTOR', 'PATIENT')")
    @Operation(summary = "Lấy toàn bộ danh sách LaboratoryTests", description = "Lấy toàn bộ danh sách các LaboratoryTests hiện có trong hệ thống.")
    public ResponseData<List<LaboratoryTestsResponse>> getAllLaboratoryTests() {
        log.info("Yêu cầu lấy toàn bộ danh sách LaboratoryTests");
        List<LaboratoryTestsResponse> responses = laboratoryTestsService.getAll();
        return ResponseData.<List<LaboratoryTestsResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Toàn bộ danh sách LaboratoryTests được trả về thành công")
                .data(responses)
                .build();
    }

    @GetMapping("/{id}")
    //@PreAuthorize("hasAnyRole('DOCTOR', 'PATIENT')")
    @Operation(summary = "Lấy LaboratoryTests theo ID", description = "Lấy thông tin chi tiết của một LaboratoryTests dựa trên ID của nó.")
    public ResponseData<LaboratoryTestsResponse> getLaboratoryTestsById(@PathVariable("id") Long id) {
        log.info("Yêu cầu lấy LaboratoryTests với id: {}", id);
        LaboratoryTestsResponse response = laboratoryTestsService.getLaboratoryTestsById(id);
        return ResponseData.<LaboratoryTestsResponse>builder()
                .status(HttpStatus.OK.value())
                .message("LaboratoryTests được lấy thành công")
                .data(response)
                .build();
    }

    @PostMapping
    //@PreAuthorize("hasRole('DOCTOR')")
    @Operation(summary = "Tạo một LaboratoryTests mới", description = "Tạo một LaboratoryTests mới với thông tin được cung cấp.")
    public ResponseData<LaboratoryTestsResponse> createLaboratoryTests(@RequestBody @Valid LaboratoryTestsRequest request) {
        log.info("Yêu cầu tạo LaboratoryTests: {}", request);
        LaboratoryTestsResponse response = laboratoryTestsService.createLaboratoryTests(request);
        return ResponseData.<LaboratoryTestsResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("LaboratoryTests được tạo thành công")
                .data(response)
                .build();
    }

    @PutMapping("/{id}")
    //@PreAuthorize("hasRole('DOCTOR')")
    @Operation(summary = "Cập nhật LaboratoryTests theo ID", description = "Cập nhật thông tin của một LaboratoryTests dựa trên ID của nó.")
    public ResponseData<LaboratoryTestsResponse> updateLaboratoryTests(
            @PathVariable("id") Long id,
            @RequestBody @Valid LaboratoryTestsRequest request) {
        log.info("Yêu cầu cập nhật LaboratoryTests với id: {}", id);
        LaboratoryTestsResponse response = laboratoryTestsService.updateLaboratoryTests(id, request);
        return ResponseData.<LaboratoryTestsResponse>builder()
                .status(HttpStatus.OK.value())
                .message("LaboratoryTests được cập nhật thành công")
                .data(response)
                .build();
    }

    @DeleteMapping("/{id}")
    //@PreAuthorize("hasRole('DOCTOR')")
    @Operation(summary = "Xóa LaboratoryTests theo ID", description = "Xóa một LaboratoryTests dựa trên ID của nó.")
    public ResponseData<Void> deleteLaboratoryTests(@PathVariable("id") Long id) {
        log.info("Yêu cầu xóa LaboratoryTests với id: {}", id);
        laboratoryTestsService.deleteLaboratoryTests(id);
        return ResponseData.<Void>builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message("LaboratoryTests được xóa thành công")
                .build();
    }

    @GetMapping("/by-date")
    @Operation(summary = "Lấy danh sách LaboratoryTests theo ngày", description = "Lấy danh sách LaboratoryTests của ngày được chỉ định với trạng thái PENDING.")
    public ResponseData<List<LaboratoryTestsResponse>> getAllLabTestByDateAndStatus(
            @RequestParam @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam("status") String status
    ) {
        log.info("Yêu cầu lấy danh sách LaboratoryTests theo ngày: {}", date);
        List<LaboratoryTestsResponse> responses = laboratoryTestsService.getAllLabTestByDateAndStatus(date, status);
        return ResponseData.<List<LaboratoryTestsResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Danh sách LaboratoryTests theo ngày được trả về thành công")
                .data(responses)
                .build();
    }

    @GetMapping("/by-encounter-and-date")
    @Operation(summary = "Lấy danh sách LaboratoryTests theo encounterId và ngày", description = "Lấy danh sách LaboratoryTests theo encounterId và ngày được chỉ định.")
    public ResponseData<List<LaboratoryTestsResponse>> getEncounterIdAndDate(
            @RequestParam Long encounterId,
            @RequestParam(required = false) @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("Yêu cầu lấy danh sách LaboratoryTests theo encounterId: {} và ngày: {}", encounterId, date);
        List<LaboratoryTestsResponse> responses = laboratoryTestsService.getEncounterIdAndDate(encounterId, date);
        return ResponseData.<List<LaboratoryTestsResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Danh sách LaboratoryTests theo encounterId và ngày được trả về thành công")
                .data(responses)
                .build();
    }

}
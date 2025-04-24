package vn.edu.hcmute.utecare.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmute.utecare.dto.request.MedicalTestRequest;
import vn.edu.hcmute.utecare.dto.response.MedicalTestResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.dto.response.ResponseData;
import vn.edu.hcmute.utecare.service.MedicalTestService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/medical-tests")
@RequiredArgsConstructor
@Tag(name = "Medical Test", description = "Medical Test API")
@Slf4j(topic = "MEDICAL_TEST_CONTROLLER")
public class MedicalTestController {

    private final MedicalTestService medicalTestService;

    @GetMapping
    @Operation(summary = "Lấy danh sách MedicalTest với phân trang", description = "Lấy danh sách các MedicalTest với hỗ trợ phân trang, sắp xếp theo các tham số page, size, sort và direction.")
    public ResponseData<PageResponse<MedicalTestResponse>> getAllMedicalTestsWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String direction) {
        log.info("Yêu cầu lấy danh sách MedicalTest với phân trang: page={}, size={}, sort={}, direction={}", page, size, sort, direction);
        PageResponse<MedicalTestResponse> response = medicalTestService.getAll(page, size, sort, direction);
        return ResponseData.<PageResponse<MedicalTestResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Danh sách MedicalTest phân trang được trả về thành công")
                .data(response)
                .build();
    }

    @GetMapping("/all")
    @Operation(summary = "Lấy toàn bộ danh sách MedicalTest", description = "Lấy toàn bộ danh sách các MedicalTest hiện có trong hệ thống.")
    public ResponseData<List<MedicalTestResponse>> getAllMedicalTests() {
        log.info("Yêu cầu lấy toàn bộ danh sách MedicalTest");
        List<MedicalTestResponse> responses = medicalTestService.getAll();
        return ResponseData.<List<MedicalTestResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Toàn bộ danh sách MedicalTest được trả về thành công")
                .data(responses)
                .build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy MedicalTest theo ID", description = "Lấy thông tin chi tiết của một MedicalTest dựa trên ID của nó.")
    public ResponseData<MedicalTestResponse> getMedicalTestById(@PathVariable("id") Long id) {
        log.info("Yêu cầu lấy MedicalTest với id: {}", id);
        MedicalTestResponse response = medicalTestService.getMedicalTestById(id);
        return ResponseData.<MedicalTestResponse>builder()
                .status(HttpStatus.OK.value())
                .message("MedicalTest được lấy thành công")
                .data(response)
                .build();
    }

    @PostMapping
    @Operation(summary = "Tạo một MedicalTest mới", description = "Tạo một MedicalTest mới với thông tin được cung cấp. Lưu ý: Endpoint này hiện không khả dụng do MedicalTest là lớp trừu tượng.")
    public ResponseData<MedicalTestResponse> createMedicalTest(@RequestBody @Valid MedicalTestRequest request) {
        log.info("Yêu cầu tạo MedicalTest: {}", request);
        MedicalTestResponse response = medicalTestService.createMedicalTest(request);
        return ResponseData.<MedicalTestResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("MedicalTest được tạo thành công")
                .data(response)
                .build();
    }
    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật MedicalTest theo ID", description = "Cập nhật thông tin của một MedicalTest dựa trên ID của nó.")
    public ResponseData<MedicalTestResponse> updateMedicalTest(
            @PathVariable("id") Long id,
            @RequestBody @Valid MedicalTestRequest request) {
        log.info("Yêu cầu cập nhật MedicalTest với id: {}", id);
        MedicalTestResponse response = medicalTestService.updateMedicalTest(id, request);
        return ResponseData.<MedicalTestResponse>builder()
                .status(HttpStatus.OK.value())
                .message("MedicalTest được cập nhật thành công")
                .data(response)
                .build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa MedicalTest theo ID", description = "Xóa một MedicalTest dựa trên ID của nó.")
    public ResponseData<Void> deleteMedicalTest(@PathVariable("id") Long id) {
        log.info("Yêu cầu xóa MedicalTest với id: {}", id);
        medicalTestService.deleteMedicalTest(id);
        return ResponseData.<Void>builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message("MedicalTest được xóa thành công")
                .build();
    }
}
package vn.edu.hcmute.utecare.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmute.utecare.dto.request.BloodGasAnalysisRequest;
import vn.edu.hcmute.utecare.dto.response.BloodGasAnalysisResponse;
import vn.edu.hcmute.utecare.dto.response.CardiacTestResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.dto.response.ResponseData;
import vn.edu.hcmute.utecare.service.BloodGasAnalysisService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/blood-gas-analysis")
@RequiredArgsConstructor
@Tag(name = "BloodGasAnalysis", description = "Blood Gas Analysis API")
@Slf4j(topic = "BLOOD_GAS_ANALYSIS_CONTROLLER")
public class BloodGasAnalysisController {

    private final BloodGasAnalysisService bloodGasAnalysisService;

    @GetMapping
    @Operation(summary = "Lấy danh sách BloodGasAnalysis có phân trang", description = "Lấy danh sách BloodGasAnalysis với các tham số phân trang.")
    public ResponseData<PageResponse<BloodGasAnalysisResponse>> getAllBloodGasAnalysisWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String direction) {
        log.info("Yêu cầu lấy danh sách BloodGasAnalysis có phân trang: page={}, size={}, sort={}, direction={}", page, size, sort, direction);
        PageResponse<BloodGasAnalysisResponse> response = bloodGasAnalysisService.getAll(page, size, sort, direction);
        return ResponseData.<PageResponse<BloodGasAnalysisResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Danh sách BloodGasAnalysis phân trang được trả về thành công")
                .data(response)
                .build();
    }

    @GetMapping("/all")
    @Operation(summary = "Lấy toàn bộ danh sách BloodGasAnalysis", description = "Lấy toàn bộ các bản ghi BloodGasAnalysis trong hệ thống.")
    public ResponseData<List<BloodGasAnalysisResponse>> getAllBloodGasAnalysis() {
        log.info("Yêu cầu lấy toàn bộ danh sách BloodGasAnalysis");
        List<BloodGasAnalysisResponse> response = bloodGasAnalysisService.getAll();
        return ResponseData.<List<BloodGasAnalysisResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Toàn bộ danh sách BloodGasAnalysis được trả về thành công")
                .data(response)
                .build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy thông tin BloodGasAnalysis theo ID", description = "Truy xuất chi tiết một bản ghi BloodGasAnalysis dựa trên ID.")
    public ResponseData<BloodGasAnalysisResponse> getBloodGasAnalysisById(@PathVariable("id") Long id) {
        log.info("Yêu cầu lấy thông tin BloodGasAnalysis với id: {}", id);
        BloodGasAnalysisResponse response = bloodGasAnalysisService.getBloodGasAnalysisById(id);
        return ResponseData.<BloodGasAnalysisResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Thông tin BloodGasAnalysis được lấy thành công")
                .data(response)
                .build();
    }

    @PostMapping
    @Operation(summary = "Tạo mới một BloodGasAnalysis", description = "Tạo mới bản ghi BloodGasAnalysis với dữ liệu được cung cấp.")
    public ResponseData<BloodGasAnalysisResponse> createBloodGasAnalysis(@RequestBody @Valid BloodGasAnalysisRequest request) {
        log.info("Yêu cầu tạo mới BloodGasAnalysis: {}", request);
        BloodGasAnalysisResponse response = bloodGasAnalysisService.createBloodGasAnalysis(request);
        return ResponseData.<BloodGasAnalysisResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("BloodGasAnalysis được tạo thành công")
                .data(response)
                .build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật thông tin BloodGasAnalysis theo ID", description = "Cập nhật thông tin bản ghi BloodGasAnalysis theo ID.")
    public ResponseData<BloodGasAnalysisResponse> updateBloodGasAnalysis(
            @PathVariable("id") Long id,
            @RequestBody @Valid BloodGasAnalysisRequest request) {
        log.info("Yêu cầu cập nhật BloodGasAnalysis với id: {}", id);
        BloodGasAnalysisResponse response = bloodGasAnalysisService.updateBloodGasAnalysis(id, request);
        return ResponseData.<BloodGasAnalysisResponse>builder()
                .status(HttpStatus.OK.value())
                .message("BloodGasAnalysis được cập nhật thành công")
                .data(response)
                .build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa BloodGasAnalysis theo ID", description = "Xóa một bản ghi BloodGasAnalysis dựa trên ID.")
    public ResponseData<Void> deleteBloodGasAnalysis(@PathVariable("id") Long id) {
        log.info("Yêu cầu xóa BloodGasAnalysis với id: {}", id);
        bloodGasAnalysisService.deleteBloodGasAnalysis(id);
        return ResponseData.<Void>builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message("BloodGasAnalysis được xóa thành công")
                .build();
    }

    @GetMapping("/by-date")
    @Operation(summary = "Lấy danh sách BloodGasAnalysis theo ngày", description = "Lấy danh sách BloodGasAnalysis của ngày được chỉ định với trạng thái PENDING.")
    public ResponseData<List<BloodGasAnalysisResponse>> getAllLabTestByDateAndStatus(
            @RequestParam @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam("status") String status
    ) {
        log.info("Yêu cầu lấy danh sách LaboratoryTests theo ngày: {}", date);
        List<BloodGasAnalysisResponse> responses = bloodGasAnalysisService.getAllLabTestByDateAndStatus(date, status);
        return ResponseData.<List<BloodGasAnalysisResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Danh sách LaboratoryTests theo ngày được trả về thành công")
                .data(responses)
                .build();
    }

    @GetMapping("/by-encounter-and-date")
    @Operation(summary = "Lấy danh sách BloodGasAnalysis theo Encounter ID và ngày", description = "Lấy danh sách BloodGasAnalysis theo Encounter ID và ngày được chỉ định. Nếu không cung cấp ngày, mặc định là ngày hiện tại.")
    public ResponseData<List<BloodGasAnalysisResponse>> getBloodGasAnalysisByEncounterIdAndDate(
            @RequestParam Long encounterId,
            @RequestParam(required = false) @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("Yêu cầu lấy danh sách BloodGasAnalysis theo encounterId: {} và ngày: {}", encounterId, date);
        List<BloodGasAnalysisResponse> responses = bloodGasAnalysisService.getEncounterIDandDate(encounterId, date);
        return ResponseData.<List<BloodGasAnalysisResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Danh sách BloodGasAnalysis theo Encounter ID và ngày được trả về thành công")
                .data(responses)
                .build();
    }

}

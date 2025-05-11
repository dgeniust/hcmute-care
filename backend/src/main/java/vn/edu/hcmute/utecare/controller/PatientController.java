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
import vn.edu.hcmute.utecare.dto.request.PatientRequest;
import vn.edu.hcmute.utecare.dto.response.PatientResponse;
import vn.edu.hcmute.utecare.dto.response.ResponseData;
import vn.edu.hcmute.utecare.service.PatientService;

@RestController
@RequestMapping("/api/v1/patients")
@Tag(name = "PATIENT", description = "API quản lý thông tin bệnh nhân")
@RequiredArgsConstructor
@Slf4j(topic = "PATIENT_CONTROLLER")
public class PatientController {
    private final PatientService patientService;

    @Operation(
            summary = "Lấy thông tin bệnh nhân theo ID",
            description = "Truy xuất thông tin chi tiết của một bệnh nhân dựa trên ID duy nhất."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy thông tin bệnh nhân thành công"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy bệnh nhân với ID được cung cấp")
    })
    @GetMapping("/{id}")
    public ResponseData<PatientResponse> getById(
            @Parameter(description = "ID của bệnh nhân cần truy xuất") @PathVariable Long id) {
        log.info("Yêu cầu lấy thông tin bệnh nhân với ID: {}", id);
        return ResponseData.<PatientResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy thông tin bệnh nhân thành công")
                .data(patientService.getById(id))
                .build();
    }

    @Operation(
            summary = "Cập nhật thông tin bệnh nhân",
            description = "Cập nhật thông tin của một bệnh nhân hiện có dựa trên ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cập nhật thông tin bệnh nhân thành công"),
            @ApiResponse(responseCode = "400", description = "CCCD hoặc email đã tồn tại"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy bệnh nhân với ID được cung cấp")
    })
    @PutMapping("/{id}")
    public ResponseData<PatientResponse> update(
            @Parameter(description = "ID của bệnh nhân cần cập nhật") @PathVariable Long id,
            @Valid @RequestBody PatientRequest request) {
        log.info("Yêu cầu cập nhật bệnh nhân với ID: {}", id);
        return ResponseData.<PatientResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Cập nhật thông tin bệnh nhân thành công")
                .data(patientService.update(id, request))
                .build();
    }
}
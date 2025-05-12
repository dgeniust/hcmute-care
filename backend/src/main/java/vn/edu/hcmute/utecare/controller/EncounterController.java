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
import vn.edu.hcmute.utecare.dto.request.EncounterRequest;
import vn.edu.hcmute.utecare.dto.response.EncounterPatientSummaryResponse;
import vn.edu.hcmute.utecare.dto.response.EncounterResponse;
import vn.edu.hcmute.utecare.dto.response.ResponseData;
import vn.edu.hcmute.utecare.service.EncounterService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/encounters")
@RequiredArgsConstructor
@Tag(name = "ENCOUNTER", description = "API quản lý thông tin cuộc gặp y tế giữa bác sĩ và bệnh nhân")
@Slf4j(topic = "ENCOUNTER_CONTROLLER")
public class EncounterController {
    private final EncounterService encounterService;

    @GetMapping
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @Operation(
            summary = "Lấy danh sách tất cả cuộc gặp",
            description = "Truy xuất toàn bộ danh sách các cuộc gặp y tế."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy danh sách cuộc gặp thành công"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập")
    })
    public ResponseData<List<EncounterResponse>> getAllEncounter() {
        log.info("Yêu cầu lấy danh sách tất cả cuộc gặp");
        return ResponseData.<List<EncounterResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy danh sách cuộc gặp thành công")
                .data(encounterService.getAllEncounter())
                .build();
    }

    @GetMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE', 'CUSTOMER')")
    @Operation(
            summary = "Lấy thông tin cuộc gặp theo ID",
            description = "Truy xuất thông tin chi tiết của một cuộc gặp dựa trên ID duy nhất."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy thông tin cuộc gặp thành công"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy cuộc gặp với ID được cung cấp")
    })
    public ResponseData<EncounterResponse> getEncounterById(
            @Parameter(description = "ID của cuộc gặp cần truy xuất") @PathVariable("id") Long id) {
        log.info("Yêu cầu lấy thông tin cuộc gặp với ID: {}", id);
        return ResponseData.<EncounterResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy thông tin cuộc gặp thành công")
                .data(encounterService.getEncounterById(id))
                .build();
    }

    @PostMapping
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR')")
    @Operation(
            summary = "Tạo cuộc gặp mới",
            description = "Tạo một cuộc gặp y tế mới với thông tin chi tiết được cung cấp."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tạo cuộc gặp thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu yêu cầu không hợp lệ"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy hồ sơ y tế hoặc tài nguyên liên quan")
    })
    public ResponseData<EncounterResponse> createEncounter(@RequestBody @Valid EncounterRequest request) {
        log.info("Yêu cầu tạo cuộc gặp mới: {}", request);
        return ResponseData.<EncounterResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Tạo cuộc gặp thành công")
                .data(encounterService.createEncounter(request))
                .build();
    }

    @PutMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR')")
    @Operation(
            summary = "Cập nhật thông tin cuộc gặp",
            description = "Cập nhật thông tin của một cuộc gặp hiện có dựa trên ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cập nhật cuộc gặp thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu yêu cầu không hợp lệ"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy cuộc gặp hoặc tài nguyên liên quan")
    })
    public ResponseData<EncounterResponse> updateEncounter(
            @Parameter(description = "ID của cuộc gặp cần cập nhật") @PathVariable("id") Long id,
            @RequestBody @Valid EncounterRequest request) {
        log.info("Yêu cầu cập nhật cuộc gặp với ID: {}", id);
        return ResponseData.<EncounterResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Cập nhật cuộc gặp thành công")
                .data(encounterService.updateEncounter(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(
            summary = "Xóa cuộc gặp",
            description = "Xóa một cuộc gặp dựa trên ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Xóa cuộc gặp thành công"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy cuộc gặp với ID được cung cấp")
    })
    public ResponseData<Void> deleteEncounter(
            @Parameter(description = "ID của cuộc gặp cần xóa") @PathVariable("id") Long id) {
        log.info("Yêu cầu xóa cuộc gặp với ID: {}", id);
        encounterService.deleteEncounter(id);
        return ResponseData.<Void>builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message("Xóa cuộc gặp thành công")
                .build();
    }

    @GetMapping("/{id}/detail-patient")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE', 'CUSTOMER')")
    @Operation(
            summary = "Lấy chi tiết cuộc gặp của bệnh nhân",
            description = "Truy xuất thông tin chi tiết của một cuộc gặp liên quan đến bệnh nhân dựa trên ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy chi tiết cuộc gặp của bệnh nhân thành công"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy cuộc gặp với ID được cung cấp")
    })
    public ResponseData<EncounterPatientSummaryResponse> getEncounterDetailPatient(
            @Parameter(description = "ID của cuộc gặp cần truy xuất chi tiết") @PathVariable("id") Long id) {
        log.info("Yêu cầu lấy chi tiết cuộc gặp của bệnh nhân với ID: {}", id);
        return ResponseData.<EncounterPatientSummaryResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy chi tiết cuộc gặp của bệnh nhân thành công")
                .data(encounterService.getEncounterPatientSummaryById(id))
                .build();
    }

    @GetMapping("/all/detail-patient")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE', 'CUSTOMER')")
    @Operation(
            summary = "Lấy chi tiết nhiều cuộc gặp của bệnh nhân",
            description = "Truy xuất thông tin chi tiết của nhiều cuộc gặp liên quan đến bệnh nhân dựa trên danh sách ID (phân tách bằng dấu phẩy)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy chi tiết nhiều cuộc gặp của bệnh nhân thành công"),
            @ApiResponse(responseCode = "400", description = "Danh sách ID không hợp lệ"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy một hoặc nhiều cuộc gặp với ID được cung cấp")
    })
    public ResponseData<List<EncounterPatientSummaryResponse>> getAllEncounterDetailPatient(
            @Parameter(description = "Danh sách ID cuộc gặp (phân tách bằng dấu phẩy)") @RequestParam(name = "ids", required = false) String ids) {
        log.info("Yêu cầu lấy chi tiết nhiều cuộc gặp của bệnh nhân với ID: {}", ids);

        List<Long> idList = (ids == null || ids.isBlank())
                ? Collections.emptyList()
                : Arrays.stream(ids.split(","))
                .map(String::trim)
                .map(Long::parseLong)
                .collect(Collectors.toList());

        return ResponseData.<List<EncounterPatientSummaryResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy chi tiết nhiều cuộc gặp của bệnh nhân thành công")
                .data(encounterService.getAllEncounterPatientSummaryById(idList))
                .build();
    }
}
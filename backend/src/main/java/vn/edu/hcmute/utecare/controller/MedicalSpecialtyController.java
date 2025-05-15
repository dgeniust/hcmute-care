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
import vn.edu.hcmute.utecare.dto.request.MedicalSpecialtyRequest;
import vn.edu.hcmute.utecare.dto.response.*;
import vn.edu.hcmute.utecare.service.DoctorService;
import vn.edu.hcmute.utecare.service.MedicalSpecialtyService;
import vn.edu.hcmute.utecare.service.NurseService;

@RestController
@RequestMapping("/api/v1/medical-specialties")
@RequiredArgsConstructor
@Tag(name = "MEDICAL-SPECIALTY", description = "API quản lý thông tin chuyên khoa, bác sĩ và y tá liên quan")
@Slf4j(topic = "MEDICAL_SPECIALTY_CONTROLLER")
public class MedicalSpecialtyController {
    private final MedicalSpecialtyService medicalSpecialtyService;
    private final DoctorService doctorService;
    private final NurseService nurseService;

    @GetMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE', 'CUSTOMER')")
    @Operation(
            summary = "Lấy chuyên khoa theo ID",
            description = "Truy xuất thông tin chi tiết của một chuyên khoa dựa trên ID duy nhất."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy thông tin chuyên khoa thành công"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy chuyên khoa với ID được cung cấp")
    })
    public ResponseData<MedicalSpecialtyResponse> getMedicalSpecialtyById(
            @Parameter(description = "ID của chuyên khoa cần truy xuất") @PathVariable("id") Integer id) {
        log.info("Yêu cầu lấy chuyên khoa với ID: {}", id);
        return ResponseData.<MedicalSpecialtyResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy thông tin chuyên khoa thành công")
                .data(medicalSpecialtyService.getMedicalSpecialtyById(id))
                .build();
    }

    @PostMapping
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @Operation(
            summary = "Tạo chuyên khoa mới",
            description = "Tạo một chuyên khoa mới với thông tin chi tiết được cung cấp."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tạo chuyên khoa thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu yêu cầu không hợp lệ"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập")
    })
    public ResponseData<MedicalSpecialtyResponse> createMedicalSpecialty(@RequestBody @Valid MedicalSpecialtyRequest request) {
        log.info("Yêu cầu tạo chuyên khoa mới: {}", request);
        return ResponseData.<MedicalSpecialtyResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Tạo chuyên khoa thành công")
                .data(medicalSpecialtyService.createMedicalSpecialty(request))
                .build();
    }

    @PutMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @Operation(
            summary = "Cập nhật chuyên khoa",
            description = "Cập nhật thông tin của một chuyên khoa hiện có dựa trên ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cập nhật chuyên khoa thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu yêu cầu không hợp lệ"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy chuyên khoa với ID được cung cấp")
    })
    public ResponseData<MedicalSpecialtyResponse> updateMedicalSpecialty(
            @Parameter(description = "ID của chuyên khoa cần cập nhật") @PathVariable("id") Integer id,
            @RequestBody @Valid MedicalSpecialtyRequest request) {
        log.info("Yêu cầu cập nhật chuyên khoa với ID: {}", id);
        return ResponseData.<MedicalSpecialtyResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Cập nhật chuyên khoa thành công")
                .data(medicalSpecialtyService.updateMedicalSpecialty(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(
            summary = "Xóa chuyên khoa",
            description = "Xóa một chuyên khoa dựa trên ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Xóa chuyên khoa thành công"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy chuyên khoa với ID được cung cấp")
    })
    public ResponseData<Void> deleteMedicalSpecialty(
            @Parameter(description = "ID của chuyên khoa cần xóa") @PathVariable("id") Integer id) {
        log.info("Yêu cầu xóa chuyên khoa với ID: {}", id);
        medicalSpecialtyService.deleteMedicalSpecialty(id);
        return ResponseData.<Void>builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message("Xóa chuyên khoa thành công")
                .build();
    }

    @GetMapping
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE', 'CUSTOMER')")
    @Operation(
            summary = "Lấy danh sách tất cả chuyên khoa",
            description = "Truy xuất danh sách chuyên khoa phân trang, hỗ trợ sắp xếp theo các tiêu chí và thứ tự."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy danh sách chuyên khoa thành công"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập")
    })
    public ResponseData<PageResponse<MedicalSpecialtyResponse>> getAllMedicalSpecialties(
            @Parameter(description = "Số trang, bắt đầu từ 1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Số lượng chuyên khoa mỗi trang") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Trường để sắp xếp (ví dụ: id, name)") @RequestParam(defaultValue = "id") String sort,
            @Parameter(description = "Hướng sắp xếp: asc (tăng dần) hoặc desc (giảm dần)") @RequestParam(defaultValue = "asc") String direction) {
        log.info("Yêu cầu lấy danh sách chuyên khoa: trang={}, kích thước={}, sắp xếp={}, hướng={}", page, size, sort, direction);
        return ResponseData.<PageResponse<MedicalSpecialtyResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy danh sách chuyên khoa thành công")
                .data(medicalSpecialtyService.getAllMedicalSpecialties(page, size, sort, direction))
                .build();
    }

    @GetMapping("/search")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE', 'CUSTOMER')")
    @Operation(
            summary = "Tìm kiếm chuyên khoa",
            description = "Tìm kiếm chuyên khoa theo từ khóa (ví dụ: tên chuyên khoa) với phân trang."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tìm kiếm chuyên khoa thành công"),
            @ApiResponse(responseCode = "400", description = "Từ khóa tìm kiếm không hợp lệ"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập")
    })
    public ResponseData<PageResponse<MedicalSpecialtyResponse>> searchMedicalSpecialties(
            @Parameter(description = "Từ khóa tìm kiếm (ví dụ: tên chuyên khoa)") @RequestParam String keyword,
            @Parameter(description = "Số trang, bắt đầu từ 1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Số lượng chuyên khoa mỗi trang") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Trường để sắp xếp (ví dụ: id, name)") @RequestParam(defaultValue = "id") String sort,
            @Parameter(description = "Hướng sắp xếp: asc (tăng dần) hoặc desc (giảm dần)") @RequestParam(defaultValue = "asc") String direction) {
        log.info("Yêu cầu tìm kiếm chuyên khoa với từ khóa: {}", keyword);
        return ResponseData.<PageResponse<MedicalSpecialtyResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Tìm kiếm chuyên khoa thành công")
                .data(medicalSpecialtyService.searchMedicalSpecialties(keyword, page, size, sort, direction))
                .build();
    }

    @GetMapping("/{id}/doctors")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE', 'CUSTOMER')")
    @Operation(
            summary = "Lấy danh sách bác sĩ theo chuyên khoa",
            description = "Truy xuất danh sách bác sĩ phân trang thuộc một chuyên khoa cụ thể dựa trên ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy danh sách bác sĩ thành công"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy chuyên khoa với ID được cung cấp")
    })
    public ResponseData<PageResponse<DoctorResponse>> getDoctorsByMedicalSpecialtyId(
            @Parameter(description = "ID của chuyên khoa") @PathVariable("id") Integer id,
            @Parameter(description = "Số trang, bắt đầu từ 1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Số lượng bác sĩ mỗi trang") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Trường để sắp xếp (ví dụ: id, name)") @RequestParam(defaultValue = "id") String sort,
            @Parameter(description = "Hướng sắp xếp: asc (tăng dần) hoặc desc (giảm dần)") @RequestParam(defaultValue = "asc") String direction) {
        log.info("Yêu cầu lấy danh sách bác sĩ cho chuyên khoa ID: {}, trang={}, kích thước={}, sắp xếp={}, hướng={}", id, page, size, sort, direction);
        return ResponseData.<PageResponse<DoctorResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy danh sách bác sĩ thành công")
                .data(doctorService.getDoctorsByMedicalSpecialtyId(id, page, size, sort, direction))
                .build();
    }

    @GetMapping("/{id}/doctors/search")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE', 'CUSTOMER')")
    @Operation(
            summary = "Tìm kiếm bác sĩ theo chuyên khoa",
            description = "Tìm kiếm bác sĩ theo từ khóa (ví dụ: tên bác sĩ) trong một chuyên khoa cụ thể với phân trang."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tìm kiếm bác sĩ thành công"),
            @ApiResponse(responseCode = "400", description = "Từ khóa tìm kiếm không hợp lệ"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy chuyên khoa với ID được cung cấp")
    })
    public ResponseData<PageResponse<DoctorResponse>> searchDoctorsByMedicalSpecialtyId(
            @Parameter(description = "ID của chuyên khoa") @PathVariable("id") Integer id,
            @Parameter(description = "Từ khóa tìm kiếm (ví dụ: tên bác sĩ)") @RequestParam String keyword,
            @Parameter(description = "Số trang, bắt đầu từ 1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Số lượng bác sĩ mỗi trang") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Trường để sắp xếp (ví dụ: id, name)") @RequestParam(defaultValue = "id") String sort,
            @Parameter(description = "Hướng sắp xếp: asc (tăng dần) hoặc desc (giảm dần)") @RequestParam(defaultValue = "asc") String direction) {
        log.info("Yêu cầu tìm kiếm bác sĩ cho chuyên khoa ID: {} với từ khóa: {}, trang={}, kích thước={}, sắp xếp={}, hướng={}", id, keyword, page, size, sort, direction);
        return ResponseData.<PageResponse<DoctorResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Tìm kiếm bác sĩ thành công")
                .data(doctorService.searchDoctorsByMedicalSpecialtyId(id, keyword, page, size, sort, direction))
                .build();
    }

    @GetMapping("/{id}/nurses")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE', 'CUSTOMER')")
    @Operation(
            summary = "Lấy danh sách y tá theo chuyên khoa",
            description = "Truy xuất danh sách y tá phân trang thuộc một chuyên khoa cụ thể dựa trên ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy danh sách y tá thành công"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy chuyên khoa với ID được cung cấp")
    })
    public ResponseData<PageResponse<NurseResponse>> getNursesByMedicalSpecialtyId(
            @Parameter(description = "ID của chuyên khoa") @PathVariable("id") Integer id,
            @Parameter(description = "Số trang, bắt đầu từ 1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Số lượng y tá mỗi trang") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Trường để sắp xếp (ví dụ: id, name)") @RequestParam(defaultValue = "id") String sort,
            @Parameter(description = "Hướng sắp xếp: asc (tăng dần) hoặc desc (giảm dần)") @RequestParam(defaultValue = "asc") String direction) {
        log.info("Yêu cầu lấy danh sách y tá cho chuyên khoa ID: {}, trang={}, kích thước={}, sắp xếp={}, hướng={}", id, page, size, sort, direction);
        return ResponseData.<PageResponse<NurseResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy danh sách y tá thành công")
                .data(nurseService.getNursesByMedicalSpecialtyId(id, page, size, sort, direction))
                .build();
    }

    @GetMapping("/{id}/nurses/search")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE', 'CUSTOMER')")
    @Operation(
            summary = "Tìm kiếm y tá theo chuyên khoa",
            description = "Tìm kiếm y tá theo từ khóa (ví dụ: tên y tá) trong một chuyên khoa cụ thể với phân trang."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tìm kiếm y tá thành công"),
            @ApiResponse(responseCode = "400", description = "Từ khóa tìm kiếm không hợp lệ"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy chuyên khoa với ID được cung cấp")
    })
    public ResponseData<PageResponse<NurseResponse>> searchNursesByMedicalSpecialtyId(
            @Parameter(description = "ID của chuyên khoa") @PathVariable("id") Integer id,
            @Parameter(description = "Từ khóa tìm kiếm (ví dụ: tên y tá)") @RequestParam String keyword,
            @Parameter(description = "Số trang, bắt đầu từ 1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Số lượng y tá mỗi trang") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Trường để sắp xếp (ví dụ: id, name)") @RequestParam(defaultValue = "id") String sort,
            @Parameter(description = "Hướng sắp xếp: asc (tăng dần) hoặc desc (giảm dần)") @RequestParam(defaultValue = "asc") String direction) {
        log.info("Yêu cầu tìm kiếm y tá cho chuyên khoa ID: {} với từ khóa: {}, trang={}, kích thước={}, sắp xếp={}, hướng={}", id, keyword, page, size, sort, direction);
        return ResponseData.<PageResponse<NurseResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Tìm kiếm y tá thành công")
                .data(nurseService.searchNursesByMedicalSpecialtyId(id, keyword, page, size, sort, direction))
                .build();
    }
}
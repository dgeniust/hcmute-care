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
import vn.edu.hcmute.utecare.dto.request.MedicineRequest;
import vn.edu.hcmute.utecare.dto.response.MedicineResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.dto.response.ResponseData;
import vn.edu.hcmute.utecare.service.MedicineService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/medicines")
@RequiredArgsConstructor
@Tag(name = "MEDICINE", description = "API quản lý thông tin thuốc trong hệ thống y tế")
@Slf4j(topic = "MEDICINE_CONTROLLER")
public class MedicineController {
    private final MedicineService medicineService;

    @GetMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE', 'CUSTOMER')")
    @Operation(
            summary = "Lấy thông tin thuốc theo ID",
            description = "Truy xuất thông tin chi tiết của một loại thuốc dựa trên ID duy nhất."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy thông tin thuốc thành công"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy thuốc với ID được cung cấp")
    })
    public ResponseData<MedicineResponse> getMedicineById(
            @Parameter(description = "ID của thuốc cần truy xuất") @PathVariable("id") Long id) {
        log.info("Yêu cầu lấy thông tin thuốc với ID: {}", id);
        return ResponseData.<MedicineResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy thông tin thuốc thành công")
                .data(medicineService.getMedicineById(id))
                .build();
    }

    @GetMapping
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE', 'CUSTOMER')")
    @Operation(
            summary = "Lấy danh sách tất cả thuốc",
            description = "Truy xuất toàn bộ danh sách các loại thuốc trong hệ thống."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy danh sách thuốc thành công"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập")
    })
    public ResponseData<List<MedicineResponse>> getAllMedicine() {
        log.info("Yêu cầu lấy danh sách tất cả thuốc");
        return ResponseData.<List<MedicineResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy danh sách thuốc thành công")
                .data(medicineService.getAllMedicine())
                .build();
    }

    @PostMapping
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @Operation(
            summary = "Tạo thuốc mới",
            description = "Tạo một loại thuốc mới với thông tin chi tiết được cung cấp."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tạo thuốc thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu yêu cầu không hợp lệ"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập")
    })
    public ResponseData<MedicineResponse> createMedicine(@RequestBody @Valid MedicineRequest request) {
        log.info("Yêu cầu tạo thuốc mới: {}", request);
        return ResponseData.<MedicineResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Tạo thuốc thành công")
                .data(medicineService.createMedicine(request))
                .build();
    }

    @PutMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @Operation(
            summary = "Cập nhật thông tin thuốc",
            description = "Cập nhật thông tin của một loại thuốc hiện có dựa trên ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cập nhật thuốc thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu yêu cầu không hợp lệ"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy thuốc với ID được cung cấp")
    })
    public ResponseData<MedicineResponse> updateMedicine(
            @Parameter(description = "ID của thuốc cần cập nhật") @PathVariable("id") Long id,
            @RequestBody @Valid MedicineRequest request) {
        log.info("Yêu cầu cập nhật thuốc với ID: {}", id);
        return ResponseData.<MedicineResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Cập nhật thuốc thành công")
                .data(medicineService.updateMedicine(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(
            summary = "Xóa thuốc",
            description = "Xóa một loại thuốc dựa trên ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Xóa thuốc thành công"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy thuốc với ID được cung cấp")
    })
    public ResponseData<Void> deleteMedicine(
            @Parameter(description = "ID của thuốc cần xóa") @PathVariable("id") Long id) {
        log.info("Yêu cầu xóa thuốc với ID: {}", id);
        medicineService.deleteMedicine(id);
        return ResponseData.<Void>builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message("Xóa thuốc thành công")
                .build();
    }

    @GetMapping("/search")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE', 'CUSTOMER')")
    @Operation(
            summary = "Tìm kiếm thuốc theo tên",
            description = "Tìm kiếm thuốc theo tên với phân trang, hỗ trợ sắp xếp theo các tiêu chí và thứ tự."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tìm kiếm thuốc thành công"),
            @ApiResponse(responseCode = "400", description = "Tên thuốc tìm kiếm không hợp lệ"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập")
    })
    public ResponseData<PageResponse<MedicineResponse>> searchMedicineByName(
            @Parameter(description = "Tên thuốc cần tìm kiếm") @RequestParam("name") String name,
            @Parameter(description = "Số trang, bắt đầu từ 1") @RequestParam(value = "page", defaultValue = "1") int page,
            @Parameter(description = "Số lượng thuốc mỗi trang") @RequestParam(value = "size", defaultValue = "10") int size,
            @Parameter(description = "Trường để sắp xếp (ví dụ: id, name)") @RequestParam(value = "sort", defaultValue = "id") String sort,
            @Parameter(description = "Hướng sắp xếp: asc (tăng dần) hoặc desc (giảm dần)") @RequestParam(value = "direction", defaultValue = "asc") String direction) {
        log.info("Yêu cầu tìm kiếm thuốc với tên: {}, trang={}, kích thước={}, sắp xếp={}, hướng={}", name, page, size, sort, direction);
        return ResponseData.<PageResponse<MedicineResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Tìm kiếm thuốc thành công")
                .data(medicineService.searchByName(name, page, size, sort, direction))
                .build();
    }
}
package vn.edu.hcmute.utecare.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmute.utecare.dto.request.ImagingTestRequest;
import vn.edu.hcmute.utecare.dto.response.ImagingTestResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.dto.response.ResponseData;
import vn.edu.hcmute.utecare.service.ImagingTestService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/imaging-tests")
@RequiredArgsConstructor
@Tag(name = "Imaging Test", description = "Imaging Test API")
@Slf4j(topic = "IMAGING_TEST_CONTROLLER")
public class ImagingTestController {

    private final ImagingTestService imagingTestService;

    @GetMapping
   // @PreAuthorize("hasAnyRole('DOCTOR', 'PATIENT')")
    @Operation(summary = "Lấy danh sách ImagingTest với phân trang", description = "Lấy danh sách các ImagingTest với hỗ trợ phân trang, sắp xếp theo các tham số page, size, sort và direction.")
    public ResponseData<PageResponse<ImagingTestResponse>> getAllImagingTestsWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String direction) {
        log.info("Yêu cầu lấy danh sách ImagingTest với phân trang: page={}, size={}, sort={}, direction={}", page, size, sort, direction);
        PageResponse<ImagingTestResponse> response = imagingTestService.getAll(page, size, sort, direction);
        return ResponseData.<PageResponse<ImagingTestResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Danh sách ImagingTest phân trang được trả về thành công")
                .data(response)
                .build();
    }

    @GetMapping("/all")
    //@PreAuthorize("hasAnyRole('DOCTOR', 'PATIENT')")
    @Operation(summary = "Lấy toàn bộ danh sách ImagingTest", description = "Lấy toàn bộ danh sách các ImagingTest hiện có trong hệ thống.")
    public ResponseData<List<ImagingTestResponse>> getAllImagingTests() {
        log.info("Yêu cầu lấy toàn bộ danh sách ImagingTest");
        List<ImagingTestResponse> responses = imagingTestService.getAll();
        return ResponseData.<List<ImagingTestResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Toàn bộ danh sách ImagingTest được trả về thành công")
                .data(responses)
                .build();
    }

    @GetMapping("/{id}")
    //@PreAuthorize("hasAnyRole('DOCTOR', 'PATIENT')")
    @Operation(summary = "Lấy ImagingTest theo ID", description = "Lấy thông tin chi tiết của một ImagingTest dựa trên ID của nó.")
    public ResponseData<ImagingTestResponse> getImagingTestById(@PathVariable("id") Long id) {
        log.info("Yêu cầu lấy ImagingTest với id: {}", id);
        ImagingTestResponse response = imagingTestService.getImagingTestById(id);
        return ResponseData.<ImagingTestResponse>builder()
                .status(HttpStatus.OK.value())
                .message("ImagingTest được lấy thành công")
                .data(response)
                .build();
    }

    @PostMapping
   // @PreAuthorize("hasRole('DOCTOR')")
    @Operation(summary = "Tạo một ImagingTest mới", description = "Tạo một ImagingTest mới với thông tin được cung cấp.")
    public ResponseData<ImagingTestResponse> createImagingTest(@RequestBody @Valid ImagingTestRequest request) {
        log.info("Yêu cầu tạo ImagingTest: {}", request);
        ImagingTestResponse response = imagingTestService.createImagingTest(request);
        return ResponseData.<ImagingTestResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("ImagingTest được tạo thành công")
                .data(response)
                .build();
    }

    @PutMapping("/{id}")
    //@PreAuthorize("hasRole('DOCTOR')")
    @Operation(summary = "Cập nhật ImagingTest theo ID", description = "Cập nhật thông tin của một ImagingTest dựa trên ID của nó.")
    public ResponseData<ImagingTestResponse> updateImagingTest(
            @PathVariable("id") Long id,
            @RequestBody @Valid ImagingTestRequest request) {
        log.info("Yêu cầu cập nhật ImagingTest với id: {}", id);
        ImagingTestResponse response = imagingTestService.updateImagingTest(id, request);
        return ResponseData.<ImagingTestResponse>builder()
                .status(HttpStatus.OK.value())
                .message("ImagingTest được cập nhật thành công")
                .data(response)
                .build();
    }

    @DeleteMapping("/{id}")
    //@PreAuthorize("hasRole('DOCTOR')")
    @Operation(summary = "Xóa ImagingTest theo ID", description = "Xóa một ImagingTest dựa trên ID của nó.")
    public ResponseData<Void> deleteImagingTest(@PathVariable("id") Long id) {
        log.info("Yêu cầu xóa ImagingTest với id: {}", id);
        imagingTestService.deleteImagingTest(id);
        return ResponseData.<Void>builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message("ImagingTest được xóa thành công")
                .build();
    }
}
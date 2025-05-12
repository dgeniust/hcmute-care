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
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmute.utecare.dto.request.PrescriptionItemRequest;
import vn.edu.hcmute.utecare.dto.response.PrescriptionItemResponse;
import vn.edu.hcmute.utecare.dto.response.ResponseData;
import vn.edu.hcmute.utecare.service.PrescriptionItemService;


@RestController
@RequestMapping("/api/v1/prescription-items")
@RequiredArgsConstructor
@Tag(name = "PRESCRIPTION-ITEM", description = "API quản lý các mục thuốc trong đơn thuốc")
@Slf4j(topic = "PRESCRIPTION_ITEM_CONTROLLER")
public class PrescriptionItemController {
    private final PrescriptionItemService prescriptionItemService;

    @GetMapping("/{id}")
    @Operation(
            summary = "Lấy thông tin mục thuốc theo ID",
            description = "Truy xuất thông tin chi tiết của một mục thuốc dựa trên ID duy nhất."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy thông tin mục thuốc thành công"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy mục thuốc với ID được cung cấp")
    })
    public ResponseData<PrescriptionItemResponse> getPrescriptionItem(
            @Parameter(description = "ID của mục thuốc cần truy xuất") @PathVariable("id") Long id) {
        log.info("Yêu cầu lấy thông tin mục thuốc với ID: {}", id);
        return ResponseData.<PrescriptionItemResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy thông tin mục thuốc thành công")
                .data(prescriptionItemService.getPrescriptionItemById(id))
                .build();
    }


    @PostMapping
    @Operation(
            summary = "Tạo mục thuốc mới",
            description = "Tạo một mục thuốc mới với thông tin chi tiết được cung cấp."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tạo mục thuốc thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu yêu cầu không hợp lệ"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy thuốc hoặc đơn thuốc liên quan")
    })
    public ResponseData<PrescriptionItemResponse> createPrescriptionItem(@Valid @RequestBody PrescriptionItemRequest request) {
        log.info("Yêu cầu tạo mục thuốc mới: {}", request);
        return ResponseData.<PrescriptionItemResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Tạo mục thuốc thành công")
                .data(prescriptionItemService.addPrescriptionItem(request))
                .build();
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Cập nhật mục thuốc",
            description = "Cập nhật thông tin của một mục thuốc hiện có dựa trên ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cập nhật mục thuốc thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu yêu cầu không hợp lệ"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy mục thuốc với ID được cung cấp")
    })
    public ResponseData<PrescriptionItemResponse> updatePrescriptionItem(
            @Parameter(description = "ID của mục thuốc cần cập nhật") @PathVariable("id") Long id,
            @Valid @RequestBody PrescriptionItemRequest request) {
        log.info("Yêu cầu cập nhật mục thuốc với ID: {}", id);
        return ResponseData.<PrescriptionItemResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Cập nhật mục thuốc thành công")
                .data(prescriptionItemService.updatePrescriptionItem(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Xóa mục thuốc",
            description = "Xóa một mục thuốc hiện có dựa trên ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Xóa mục thuốc thành công"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy mục thuốc với ID được cung cấp")
    })
    public ResponseData<Void> deletePrescriptionItem(
            @Parameter(description = "ID của mục thuốc cần xóa") @PathVariable("id") Long id) {
        log.info("Yêu cầu xóa mục thuốc với ID: {}", id);
        prescriptionItemService.deletePrescriptionItem(id);
        return ResponseData.<Void>builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message("Xóa mục thuốc thành công")
                .build();
    }
}
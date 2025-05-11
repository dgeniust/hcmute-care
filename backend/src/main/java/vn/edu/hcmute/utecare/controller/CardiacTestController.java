package vn.edu.hcmute.utecare.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmute.utecare.dto.request.CardiacTestRequest;
import vn.edu.hcmute.utecare.dto.response.CardiacTestResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.dto.response.ResponseData;
import vn.edu.hcmute.utecare.model.Account;
import vn.edu.hcmute.utecare.service.CardiacTestService;
import vn.edu.hcmute.utecare.util.SecurityUtil;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/cardiac-tests")
@RequiredArgsConstructor
@Tag(name = "CARDIAC-TEST", description = "API quản lý xét nghiệm tim mạch trong hệ thống y tế")
@Slf4j(topic = "CARDIAC_TEST_CONTROLLER")
public class CardiacTestController {

    private final CardiacTestService cardiacTestService;

    @PostMapping
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE')")
    @Operation(
            summary = "Tạo xét nghiệm tim mạch mới",
            description = "Tạo một xét nghiệm tim mạch mới với thông tin chi tiết được cung cấp."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tạo xét nghiệm tim mạch thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu yêu cầu không hợp lệ"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy cuộc gặp với ID được cung cấp")
    })
    public ResponseData<CardiacTestResponse> createCardiacTest(@Valid @RequestBody CardiacTestRequest request) {
        log.info("Yêu cầu tạo xét nghiệm tim mạch mới: {}", request);
        return ResponseData.<CardiacTestResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Tạo xét nghiệm tim mạch thành công")
                .data(cardiacTestService.createCardiacTest(request))
                .build();
    }

    @GetMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE', 'CUSTOMER')")
    @Operation(
            summary = "Lấy thông tin xét nghiệm tim mạch theo ID",
            description = "Truy xuất thông tin chi tiết của một xét nghiệm tim mạch dựa trên ID duy nhất."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy thông tin xét nghiệm tim mạch thành công"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy xét nghiệm tim mạch với ID được cung cấp")
    })
    public ResponseData<CardiacTestResponse> getCardiacTestById(
            @Parameter(description = "ID của xét nghiệm tim mạch cần truy xuất") @PathVariable("id") Long id) {
        log.info("Yêu cầu lấy thông tin xét nghiệm tim mạch với ID: {}", id);
        return ResponseData.<CardiacTestResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy thông tin xét nghiệm tim mạch thành công")
                .data(cardiacTestService.getCardiacTestById(id))
                .build();
    }

    @GetMapping
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE')")
    @Operation(
            summary = "Lấy danh sách tất cả xét nghiệm tim mạch",
            description = "Truy xuất danh sách tất cả xét nghiệm tim mạch với phân trang và sắp xếp."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy danh sách xét nghiệm tim mạch thành công")
    })
    public ResponseData<PageResponse<CardiacTestResponse>> getAllCardiacTests(
            @Parameter(description = "Số trang, bắt đầu từ 1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Số lượng bản ghi mỗi trang") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Trường để sắp xếp (ví dụ: id, createDate)") @RequestParam(defaultValue = "id") String sort,
            @Parameter(description = "Hướng sắp xếp: asc (tăng dần) hoặc desc (giảm dần)") @RequestParam(defaultValue = "asc") String direction) {
        log.info("Yêu cầu lấy danh sách xét nghiệm tim mạch: trang={}, kích thước={}, sắp xếp={}, hướng={}", page, size, sort, direction);
        return ResponseData.<PageResponse<CardiacTestResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy danh sách xét nghiệm tim mạch thành công")
                .data(cardiacTestService.getAll(page, size, sort, direction))
                .build();
    }

    @PutMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE')")
    @Operation(
            summary = "Cập nhật xét nghiệm tim mạch",
            description = "Cập nhật thông tin của một xét nghiệm tim mạch hiện có dựa trên ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cập nhật xét nghiệm tim mạch thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu yêu cầu không hợp lệ"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy xét nghiệm tim mạch hoặc cuộc gặp với ID được cung cấp")
    })
    public ResponseData<CardiacTestResponse> updateCardiacTest(
            @Parameter(description = "ID của xét nghiệm tim mạch cần cập nhật") @PathVariable("id") Long id,
            @Valid @RequestBody CardiacTestRequest request) {
        log.info("Yêu cầu cập nhật xét nghiệm tim mạch với ID: {}", id);
        return ResponseData.<CardiacTestResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Cập nhật xét nghiệm tim mạch thành công")
                .data(cardiacTestService.updateCardiacTest(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE')")
    @Operation(
            summary = "Xóa xét nghiệm tim mạch",
            description = "Xóa một xét nghiệm tim mạch hiện có dựa trên ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Xóa xét nghiệm tim mạch thành công"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy xét nghiệm tim mạch với ID được cung cấp")
    })
    public ResponseData<Void> deleteCardiacTest(
            @Parameter(description = "ID của xét nghiệm tim mạch cần xóa") @PathVariable("id") Long id) {
        log.info("Yêu cầu xóa xét nghiệm tim mạch với ID: {}", id);
        cardiacTestService.deleteCardiacTest(id);
        return ResponseData.<Void>builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message("Xóa xét nghiệm tim mạch thành công")
                .build();
    }


    @GetMapping("/by-date")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE')")
    @Operation(
            summary = "Lấy danh sách xét nghiệm tim mạch theo ngày và trạng thái",
            description = "Truy xuất danh sách xét nghiệm tim mạch theo ngày và trạng thái (ví dụ: PENDING)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy danh sách xét nghiệm tim mạch thành công"),
            @ApiResponse(responseCode = "400", description = "Trạng thái không hợp lệ")
    })
    public ResponseData<List<CardiacTestResponse>> getAllLabTestByDateAndStatus(
            @Parameter(description = "Ngày cần truy xuất (định dạng: yyyy-MM-dd)") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @Parameter(description = "Trạng thái của xét nghiệm (ví dụ: PENDING, COMPLETED)") @RequestParam String status) {
        log.info("Yêu cầu lấy danh sách xét nghiệm tim mạch theo ngày: {} và trạng thái: {}", date, status);
        return ResponseData.<List<CardiacTestResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy danh sách xét nghiệm tim mạch thành công")
                .data(cardiacTestService.getAllLabTestByDateAndStatus(date, status))
                .build();
    }

    @GetMapping("/by-encounter-and-date")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE', 'CUSTOMER')")
    @Operation(
            summary = "Lấy danh sách xét nghiệm tim mạch theo cuộc gặp và ngày",
            description = "Truy xuất danh sách xét nghiệm tim mạch theo ID cuộc gặp và ngày (tùy chọn, mặc định là hôm nay)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy danh sách xét nghiệm tim mạch thành công")
    })
    public ResponseData<List<CardiacTestResponse>> getEncounterIdAndDate(
            @Parameter(description = "ID của cuộc gặp") @RequestParam Long encounterId,
            @Parameter(description = "Ngày cần truy xuất (tùy chọn, định dạng: yyyy-MM-dd)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("Yêu cầu lấy danh sách xét nghiệm tim mạch theo cuộc gặp ID: {} và ngày: {}", encounterId, date);
        return ResponseData.<List<CardiacTestResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy danh sách xét nghiệm tim mạch thành công")
                .data(cardiacTestService.getEncounterIdAndDate(encounterId, date))
                .build();
    }
}
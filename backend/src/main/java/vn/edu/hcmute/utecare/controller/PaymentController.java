package vn.edu.hcmute.utecare.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmute.utecare.dto.request.PaymentRequest;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.dto.response.PaymentAppointmentResponse;
import vn.edu.hcmute.utecare.dto.response.PaymentResponse;
import vn.edu.hcmute.utecare.dto.response.ResponseData;
import vn.edu.hcmute.utecare.service.PaymentService;
import vn.edu.hcmute.utecare.util.enumeration.PaymentStatus;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
@Tag(name = "PAYMENT", description = "API quản lý các giao dịch thanh toán, bao gồm thanh toán qua VNPay")
@Slf4j(topic = "PAYMENT_CONTROLLER")
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/vn-pay")
//    @PreAuthorize("hasAnyRole('CUSTOMER')")
    @Operation(
            summary = "Thanh toán qua VNPay",
            description = "Tạo URL thanh toán qua VNPay cho một cuộc hẹn."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tạo URL thanh toán thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu yêu cầu không hợp lệ"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy cuộc hẹn")
    })
    public ResponseData<String> vnPayPayment(
            @Valid @RequestBody PaymentRequest request,
            HttpServletRequest httpServletRequest) {
        log.info("Yêu cầu thanh toán qua VNPay cho cuộc hẹn ID: {}", request.getAppointmentId());
        return ResponseData.<String>builder()
                .status(HttpStatus.OK.value())
                .message("Tạo URL thanh toán VNPay thành công")
                .data(paymentService.createPaymentUrl(request, httpServletRequest))
                .build();
    }

    @GetMapping("/vnpay/return")
//    @PreAuthorize("hasAnyRole('CUSTOMER')")
    @Operation(
            summary = "Xử lý kết quả thanh toán VNPay",
            description = "Xử lý phản hồi từ VNPay sau khi khách hàng hoàn tất thanh toán."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Xử lý kết quả thanh toán thành công"),
            @ApiResponse(responseCode = "400", description = "Mã bảo mật không hợp lệ hoặc giao dịch thất bại"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy giao dịch thanh toán")
    })
    public ResponseData<PaymentAppointmentResponse> vnpayReturn(HttpServletRequest request) {
        log.info("Xử lý phản hồi VNPay: {}", request.getQueryString());
        return ResponseData.<PaymentAppointmentResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Xử lý kết quả thanh toán VNPay thành công")
                .data(paymentService.processPaymentReturn(request))
                .build();
    }

    @GetMapping("/transaction/{transactionId}")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'CUSTOMER')")
    @Operation(
            summary = "Lấy thông tin thanh toán theo mã giao dịch",
            description = "Truy xuất thông tin chi tiết của một giao dịch thanh toán dựa trên mã giao dịch."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy thông tin thanh toán thành công"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy giao dịch với mã được cung cấp")
    })
    public ResponseData<PaymentAppointmentResponse> getPaymentByTransactionId(
            @Parameter(description = "Mã giao dịch thanh toán") @PathVariable String transactionId) {
        log.info("Yêu cầu lấy thông tin thanh toán với mã giao dịch: {}", transactionId);
        return ResponseData.<PaymentAppointmentResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy thông tin thanh toán thành công")
                .data(paymentService.getPaymentByTransactionId(transactionId))
                .build();
    }

    @GetMapping("/appointments/{appointmentId}")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'CUSTOMER', 'DOCTOR', 'NURSE')")
    @Operation(
            summary = "Lấy thông tin thanh toán theo ID cuộc hẹn",
            description = "Truy xuất thông tin chi tiết của một giao dịch thanh toán dựa trên ID cuộc hẹn."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy thông tin thanh toán thành công"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy giao dịch với ID cuộc hẹn được cung cấp")
    })
    public ResponseData<PaymentAppointmentResponse> getPaymentById(
            @Parameter(description = "ID của cuộc hẹn") @PathVariable("appointmentId") Long appointmentId) {
        log.info("Yêu cầu lấy thông tin thanh toán với ID cuộc hẹn: {}", appointmentId);
        return ResponseData.<PaymentAppointmentResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy thông tin thanh toán thành công")
                .data(paymentService.getPaymentByAppointmentId(appointmentId))
                .build();
    }

    @GetMapping
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @Operation(
            summary = "Lấy danh sách tất cả giao dịch thanh toán",
            description = "Truy xuất danh sách các giao dịch thanh toán với bộ lọc tùy chọn (mã giao dịch, trạng thái, ngày bắt đầu, ngày kết thúc) và phân trang."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy danh sách giao dịch thanh toán thành công"),
            @ApiResponse(responseCode = "400", description = "Tham số lọc không hợp lệ"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập")
    })
    public ResponseData<PageResponse<PaymentResponse>> getAllPayments(
            @Parameter(description = "Mã giao dịch (tùy chọn)") @RequestParam(required = false) String transactionId,
            @Parameter(description = "Trạng thái thanh toán (PENDING, COMPLETED, FAILED, tùy chọn)") @RequestParam(required = false) PaymentStatus paymentStatus,
            @Parameter(description = "Ngày bắt đầu (tùy chọn)") @RequestParam(required = false) LocalDateTime startDate,
            @Parameter(description = "Ngày kết thúc (tùy chọn)") @RequestParam(required = false) LocalDateTime endDate,
            @Parameter(description = "Số trang, bắt đầu từ 1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Số lượng giao dịch mỗi trang") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Trường để sắp xếp (ví dụ: id, paymentDate)") @RequestParam(defaultValue = "id") String sort,
            @Parameter(description = "Hướng sắp xếp: asc (tăng dần) hoặc desc (giảm dần)") @RequestParam(defaultValue = "asc") String direction) {
        log.info("Yêu cầu lấy danh sách giao dịch thanh toán: mã giao dịch={}, trạng thái={}, ngày bắt đầu={}, ngày kết thúc={}, trang={}, kích thước={}, sắp xếp={}, hướng={}",
                transactionId, paymentStatus, startDate, endDate, page, size, sort, direction);
        return ResponseData.<PageResponse<PaymentResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy danh sách giao dịch thanh toán thành công")
                .data(paymentService.getAllPayments(transactionId, paymentStatus, startDate, endDate, page, size, sort, direction))
                .build();
    }

    @GetMapping("/all")
//    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(
            summary = "Lấy danh sách tất cả giao dịch thanh toán (admin)",
            description = "Truy xuất danh sách tất cả các giao dịch thanh toán với phân trang, dành cho admin."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy danh sách giao dịch thanh toán thành công"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập")
    })
    public ResponseData<PageResponse<PaymentResponse>> getAll(
            @Parameter(description = "Số trang, bắt đầu từ 1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Số lượng giao dịch mỗi trang") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Trường để sắp xếp (ví dụ: id, paymentDate)") @RequestParam(defaultValue = "id") String sort,
            @Parameter(description = "Hướng sắp xếp: asc (tăng dần) hoặc desc (giảm dần)") @RequestParam(defaultValue = "asc") String direction) {
        log.info("Yêu cầu lấy danh sách tất cả giao dịch thanh toán: trang={}, kích thước={}, sắp xếp={}, hướng={}",
                page, size, sort, direction);
        return ResponseData.<PageResponse<PaymentResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy danh sách giao dịch thanh toán thành công")
                .data(paymentService.getAll(page, size, sort, direction))
                .build();
    }
}
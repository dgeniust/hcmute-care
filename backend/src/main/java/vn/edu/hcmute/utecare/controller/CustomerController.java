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
import vn.edu.hcmute.utecare.dto.request.CustomerRequest;
import vn.edu.hcmute.utecare.dto.response.*;
import vn.edu.hcmute.utecare.service.CustomerService;
import vn.edu.hcmute.utecare.service.MedicalRecordService;
import vn.edu.hcmute.utecare.service.PaymentService;
import vn.edu.hcmute.utecare.util.SecurityUtil;
import vn.edu.hcmute.utecare.util.enumeration.Membership;
import vn.edu.hcmute.utecare.util.enumeration.PaymentStatus;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
@Tag(name = "CUSTOMER", description = "API quản lý thông tin khách hàng, hồ sơ y tế và giao dịch thanh toán")
@Slf4j(topic = "CUSTOMER_CONTROLLER")
public class CustomerController {

    private final CustomerService customerService;
    private final PaymentService paymentService;
    private final MedicalRecordService medicalRecordService;

    @GetMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE', 'CUSTOMER')")
    @Operation(
            summary = "Lấy thông tin khách hàng theo ID",
            description = "Truy xuất thông tin chi tiết của một khách hàng dựa trên ID. Khách hàng chỉ có thể xem thông tin của chính họ."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy thông tin khách hàng thành công"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập thông tin khách hàng"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy khách hàng với ID được cung cấp")
    })
    public ResponseData<CustomerResponse> getCustomerById(
            @Parameter(description = "ID của khách hàng cần truy xuất") @PathVariable("id") Long id) {
        log.info("Yêu cầu lấy thông tin khách hàng với ID: {}", id);
//        // Kiểm tra nếu người dùng là CUSTOMER, chỉ cho phép xem thông tin của chính họ
//        if (SecurityUtil.hasRole("CUSTOMER") && !SecurityUtil.getCurrentUserId().equals(id)) {
//            log.warn("Người dùng với ID {} không có quyền truy cập thông tin khách hàng ID: {}", SecurityUtil.getCurrentUserId(), id);
//            throw new SecurityException("Không có quyền truy cập thông tin khách hàng này");
//        }
        return ResponseData.<CustomerResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy thông tin khách hàng thành công")
                .data(customerService.getCustomerById(id))
                .build();
    }

    @PostMapping
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @Operation(
            summary = "Tạo khách hàng mới",
            description = "Tạo một khách hàng mới với thông tin chi tiết và tài khoản liên kết. Chỉ quản trị viên hoặc nhân viên được phép thực hiện."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tạo khách hàng thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu yêu cầu không hợp lệ hoặc số điện thoại đã tồn tại"),
            @ApiResponse(responseCode = "403", description = "Không có quyền tạo khách hàng")
    })
    public ResponseData<CustomerResponse> createCustomer(@Valid @RequestBody CustomerRequest request) {
        log.info("Yêu cầu tạo khách hàng mới: {}", request);
        return ResponseData.<CustomerResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Tạo khách hàng thành công")
                .data(customerService.createCustomer(request))
                .build();
    }

    @PutMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'CUSTOMER')")
    @Operation(
            summary = "Cập nhật thông tin khách hàng",
            description = "Cập nhật thông tin của một khách hàng dựa trên ID. Khách hàng chỉ có thể cập nhật thông tin của chính họ."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cập nhật khách hàng thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu yêu cầu không hợp lệ hoặc số điện thoại đã tồn tại"),
            @ApiResponse(responseCode = "403", description = "Không có quyền cập nhật thông tin khách hàng"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy khách hàng với ID được cung cấp")
    })
    public ResponseData<CustomerResponse> updateCustomer(
            @Parameter(description = "ID của khách hàng cần cập nhật") @PathVariable("id") Long id,
            @Valid @RequestBody CustomerRequest request) {
        log.info("Yêu cầu cập nhật khách hàng với ID: {}", id);
//        // Kiểm tra nếu người dùng là CUSTOMER, chỉ cho phép cập nhật thông tin của chính họ
//        if (SecurityUtil.hasRole("CUSTOMER") && !SecurityUtil.getCurrentUserId().equals(id)) {
//            log.warn("Người dùng với ID {} không có quyền cập nhật thông tin khách hàng ID: {}", SecurityUtil.getCurrentUserId(), id);
//            throw new SecurityException("Không có quyền cập nhật thông tin khách hàng này");
//        }
        return ResponseData.<CustomerResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Cập nhật khách hàng thành công")
                .data(customerService.updateCustomer(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Xóa khách hàng",
            description = "Xóa một khách hàng và tài khoản liên kết dựa trên ID. Chỉ quản trị viên được phép thực hiện."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Xóa khách hàng thành công"),
            @ApiResponse(responseCode = "403", description = "Không có quyền xóa khách hàng"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy khách hàng hoặc tài khoản với ID được cung cấp")
    })
    public ResponseData<Void> deleteCustomer(
            @Parameter(description = "ID của khách hàng cần xóa") @PathVariable("id") Long id) {
        log.info("Yêu cầu xóa khách hàng với ID: {}", id);
        customerService.deleteCustomer(id);
        return ResponseData.<Void>builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message("Xóa khách hàng thành công")
                .build();
    }

    @GetMapping
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @Operation(
            summary = "Lấy danh sách tất cả khách hàng",
            description = "Truy xuất danh sách khách hàng phân trang, hỗ trợ sắp xếp theo các tiêu chí và thứ tự."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy danh sách khách hàng thành công"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập danh sách khách hàng")
    })
    public ResponseData<PageResponse<CustomerResponse>> getAllCustomers(
            @Parameter(description = "Số trang, bắt đầu từ 1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Số lượng khách hàng mỗi trang") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Trường để sắp xếp (ví dụ: id, createdAt)") @RequestParam(defaultValue = "id") String sort,
            @Parameter(description = "Hướng sắp xếp: asc (tăng dần) hoặc desc (giảm dần)") @RequestParam(defaultValue = "asc") String direction) {
        log.info("Yêu cầu lấy danh sách khách hàng: trang={}, kích thước={}, sắp xếp={}, hướng={}", page, size, sort, direction);
        return ResponseData.<PageResponse<CustomerResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy danh sách khách hàng thành công")
                .data(customerService.getAllCustomers(page, size, sort, direction))
                .build();
    }

    @GetMapping("/search")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @Operation(
            summary = "Tìm kiếm khách hàng",
            description = "Tìm kiếm khách hàng theo từ khóa (tên, số điện thoại) và/hoặc cấp thành viên, trả về danh sách phân trang."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tìm kiếm khách hàng thành công"),
            @ApiResponse(responseCode = "400", description = "Từ khóa tìm kiếm không hợp lệ"),
            @ApiResponse(responseCode = "403", description = "Không có quyền tìm kiếm khách hàng")
    })
    public ResponseData<PageResponse<CustomerResponse>> searchCustomers(
            @Parameter(description = "Từ khóa tìm kiếm (tên, số điện thoại)") @RequestParam(required = false) String keyword,
            @Parameter(description = "Cấp thành viên (ví dụ: STANDARD, PREMIUM)") @RequestParam(required = false) Membership membership,
            @Parameter(description = "Số trang, bắt đầu từ 1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Số lượng khách hàng mỗi trang") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Trường để sắp xếp (ví dụ: id, createdAt)") @RequestParam(defaultValue = "id") String sort,
            @Parameter(description = "Hướng sắp xếp: asc (tăng dần) hoặc desc (giảm dần)") @RequestParam(defaultValue = "asc") String direction) {
        String searchKeyword = (keyword != null && !keyword.trim().isEmpty()) ? keyword.trim() : null;
        log.info("Yêu cầu tìm kiếm khách hàng với từ khóa: '{}', cấp thành viên: '{}', trang={}, kích thước={}, sắp xếp={}, hướng={}",
                searchKeyword, membership, page, size, sort, direction);
        return ResponseData.<PageResponse<CustomerResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Tìm kiếm khách hàng thành công")
                .data(customerService.searchCustomers(searchKeyword, membership, page, size, sort, direction))
                .build();
    }

    @GetMapping("/{id}/medicalRecords")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'NURSE', 'CUSTOMER')")
    @Operation(
            summary = "Lấy danh sách hồ sơ y tế",
            description = "Truy xuất danh sách hồ sơ y tế phân trang của một khách hàng dựa trên ID. Khách hàng chỉ có thể xem hồ sơ của chính họ."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy danh sách hồ sơ y tế thành công"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập hồ sơ y tế"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy khách hàng với ID được cung cấp")
    })
    public ResponseData<PageResponse<MedicalRecordResponse>> getAllMedicalRecords(
            @Parameter(description = "ID của khách hàng cần lấy hồ sơ y tế") @PathVariable("id") Long customerId,
            @Parameter(description = "Số trang, bắt đầu từ 1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Số lượng hồ sơ mỗi trang") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Trường để sắp xếp (ví dụ: id, createdAt)") @RequestParam(defaultValue = "id") String sort,
            @Parameter(description = "Hướng sắp xếp: asc (tăng dần) hoặc desc (giảm dần)") @RequestParam(defaultValue = "asc") String direction) {
        log.info("Yêu cầu lấy danh sách hồ sơ y tế cho khách hàng ID={}: trang={}, kích thước={}, sắp xếp={}, hướng={}",
                customerId, page, size, sort, direction);
//        // Kiểm tra nếu người dùng là CUSTOMER, chỉ cho phép xem hồ sơ của chính họ
//        if (SecurityUtil.hasRole("CUSTOMER") && !SecurityUtil.getCurrentUserId().equals(customerId)) {
//            log.warn("Người dùng với ID {} không có quyền truy cập hồ sơ y tế của khách hàng ID: {}", SecurityUtil.getCurrentUserId(), customerId);
//            throw new SecurityException("Không có quyền truy cập hồ sơ y tế của khách hàng này");
//        }
        return ResponseData.<PageResponse<MedicalRecordResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy danh sách hồ sơ y tế thành công")
                .data(medicalRecordService.getAllMedicalRecordsByCustomer(customerId, page, size, sort, direction))
                .build();
    }

    @GetMapping("/{id}/payments")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'CUSTOMER')")
    @Operation(
            summary = "Lấy danh sách giao dịch thanh toán",
            description = "Truy xuất danh sách giao dịch thanh toán của một khách hàng dựa trên ID, hỗ trợ lọc theo trạng thái thanh toán. Khách hàng chỉ có thể xem giao dịch của chính họ."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy danh sách giao dịch thanh toán thành công"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập danh sách giao dịch"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy khách hàng với ID được cung cấp")
    })
    public ResponseData<PageResponse<PaymentResponse>> getAllPayments(
            @Parameter(description = "ID của khách hàng cần lấy giao dịch") @PathVariable("id") Long customerId,
            @Parameter(description = "Trạng thái thanh toán (ví dụ: SUCCESS, PENDING)") @RequestParam(required = false) PaymentStatus paymentStatus,
            @Parameter(description = "Số trang, bắt đầu từ 1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Số lượng giao dịch mỗi trang") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Trường để sắp xếp (ví dụ: paymentDate, amount)") @RequestParam(defaultValue = "paymentDate") String sort,
            @Parameter(description = "Hướng sắp xếp: asc (tăng dần) hoặc desc (giảm dần)") @RequestParam(defaultValue = "asc") String direction) {
        log.info("Yêu cầu lấy danh sách giao dịch cho khách hàng ID={}: trạng thái={}, trang={}, kích thước={}, sắp xếp={}, hướng={}",
                customerId, paymentStatus, page, size, sort, direction);
//        // Kiểm tra nếu người dùng là CUSTOMER, chỉ cho phép xem giao dịch của chính họ
//        if (SecurityUtil.hasRole("CUSTOMER") && !SecurityUtil.getCurrentUserId().equals(customerId)) {
//            log.warn("Người dùng với ID {} không có quyền truy cập giao dịch của khách hàng ID: {}", SecurityUtil.getCurrentUserId(), customerId);
//            throw new SecurityException("Không có quyền truy cập danh sách giao dịch của khách hàng này");
//        }
        return ResponseData.<PageResponse<PaymentResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy danh sách giao dịch thanh toán thành công")
                .data(paymentService.getAllPaymentsByCustomerId(customerId, paymentStatus, page, size, sort, direction))
                .build();
    }
}
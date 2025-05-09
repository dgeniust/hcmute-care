package vn.edu.hcmute.utecare.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmute.utecare.dto.request.CreateCustomerRequest;
import vn.edu.hcmute.utecare.dto.request.CustomerRequest;
import vn.edu.hcmute.utecare.dto.response.*;
import vn.edu.hcmute.utecare.service.CustomerService;
import vn.edu.hcmute.utecare.service.PaymentService;
import vn.edu.hcmute.utecare.util.enumeration.Membership;
import vn.edu.hcmute.utecare.util.enumeration.PaymentStatus;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
@Tag(name = "Customer", description = "Customer API")
@Slf4j(topic = "CUSTOMER_CONTROLLER")
public class CustomerController {
    private final CustomerService customerService;
    private final PaymentService paymentService;

    @GetMapping("/{id}")
    @Operation(summary = "Get customer by ID", description = "Retrieve a customer by their ID")
    public ResponseData<CustomerResponse> getCustomerById(@PathVariable("id") Long id) {
        log.info("Get customer request for id: {}", id);
        return ResponseData.<CustomerResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Customer retrieved successfully")
                .data(customerService.getCustomerById(id))
                .build();
    }

    @PostMapping
    @Operation(summary = "Create a new customer", description = "Create a new customer with provided details and associated account")
    public ResponseData<CustomerResponse> createCustomer(@RequestBody @Valid CustomerRequest request) {
        log.info("Create customer request: {}", request);
        return ResponseData.<CustomerResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Customer created successfully")
                .data(customerService.createCustomer(request))
                .build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a customer", description = "Update an existing customer by their ID")
    public ResponseData<CustomerResponse> updateCustomer(
            @PathVariable("id") Long id,
            @RequestBody @Valid CustomerRequest request) {
        log.info("Update customer request for id: {}", id);
        return ResponseData.<CustomerResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Customer updated successfully")
                .data(customerService.updateCustomer(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a customer", description = "Delete a customer by their ID")
    public ResponseData<Void> deleteCustomer(@PathVariable("id") Long id) {
        log.info("Delete customer request for id: {}", id);
        customerService.deleteCustomer(id);
        return ResponseData.<Void>builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message("Customer deleted successfully")
                .build();
    }

    @GetMapping
    @Operation(summary = "Get all customers", description = "Retrieve a paginated list of all customers")
    public ResponseData<PageResponse<CustomerResponse>> getAllCustomers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String direction) {
        log.info("Get all customers request: page={}, size={}, sort={}, direction={}", page, size, sort, direction);
        return ResponseData.<PageResponse<CustomerResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Customers retrieved successfully")
                .data(customerService.getAllCustomers(page, size, sort, direction))
                .build();
    }

    @GetMapping("/search")
    @Operation(summary = "Search customers", description = "Search customers by keyword and membership with pagination")
    public ResponseData<PageResponse<CustomerResponse>> searchCustomers(
            @RequestParam String keyword,
            @RequestParam(required = false) Membership membership,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String direction) {
        log.info("Search customers request with keyword: {} and membership: {}", keyword, membership);
        return ResponseData.<PageResponse<CustomerResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Customers search completed successfully")
                .data(customerService.searchCustomers(keyword, membership, page, size, sort, direction))
                .build();
    }

    @GetMapping("/{id}/medicalRecords")
    @Operation(summary = "Get all medical records", description = "Retrieve a paginated list of all medical records for a specific customer")
    public ResponseData<PageResponse<MedicalRecordResponse>> getAllMedicalRecords(
            @PathVariable("id") Long customerId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String direction) {
        log.info("Get all medical records request for customerId={}: page={}, size={}, sort={}, direction={}",
                customerId, page, size, sort, direction);
        return ResponseData.<PageResponse<MedicalRecordResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Medical records retrieved successfully")
                .data(customerService.getAllMedicalRecords(customerId, page, size, sort, direction))
                .build();
    }

    @GetMapping("/{id}/payments")
    @Operation(
            summary = "Lấy danh sách tất cả payments theo customer",
            description = "Trả về danh sách các giao dịch thanh toán (payments) liên quan đến một customer được chỉ định, hỗ trợ phân trang và sắp xếp theo các trường như paymentDate, amount, hoặc paymentStatus."
    )
//    @PreAuthorize("hasRole('ADMIN') or #customerId == authentication.principal.id")
    public ResponseData<PageResponse<PaymentResponse>> getAllPayments(
            @PathVariable("id") Long customerId,
            @RequestParam(required = false) PaymentStatus paymentStatus,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Trường để sắp xếp (paymentDate, amount)", example = "paymentDate")
            @RequestParam(defaultValue = "paymentDate") String sort,
            @RequestParam(defaultValue = "asc") String direction
    ){
        log.info("Get all payments request for customerId={}: paymentStatus={}, page={}, size={}, sort={}, direction={}",
                customerId, paymentStatus, page, size, sort, direction);
        return ResponseData.<PageResponse<PaymentResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Medical records retrieved successfully")
                .data(paymentService.getAllPaymentsByCustomerId(customerId, paymentStatus, page, size, sort, direction))
                .build();
    }
}
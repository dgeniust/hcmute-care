package vn.edu.hcmute.utecare.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmute.utecare.dto.request.CustomerCreationRequest;
import vn.edu.hcmute.utecare.dto.request.CustomerRequest;
import vn.edu.hcmute.utecare.dto.response.CustomerResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.dto.response.ResponseData;
import vn.edu.hcmute.utecare.service.CustomerService;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
@Tag(name = "Customer", description = "Customer API")
@Slf4j(topic = "CUSTOMER_CONTROLLER")
public class CustomerController {
    private final CustomerService customerService;

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
    public ResponseData<CustomerResponse> createCustomer(@RequestBody @Valid CustomerCreationRequest request) {
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
            @RequestParam(required = false) String membership,
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
}
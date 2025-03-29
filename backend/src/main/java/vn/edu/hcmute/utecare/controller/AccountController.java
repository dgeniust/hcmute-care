package vn.edu.hcmute.utecare.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmute.utecare.dto.request.AccountStatusUpdateRequest;
import vn.edu.hcmute.utecare.dto.response.AccountResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.dto.response.ResponseData;
import vn.edu.hcmute.utecare.service.AccountService;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
@Tag(name = "Account", description = "Account Management API")
@Slf4j(topic = "ACCOUNT_CONTROLLER")
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/{id}")
    @Operation(summary = "Get account by ID", description = "Retrieve an account by its ID")
    public ResponseData<AccountResponse> getAccountById(@PathVariable("id") Long id) {
        log.info("Get account request for id: {}", id);
        return ResponseData.<AccountResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Account retrieved successfully")
                .data(accountService.getAccountById(id))
                .build();
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update account status", description = "Update the status of an existing account by its ID")
    public ResponseData<AccountResponse> updateAccountStatus(
            @PathVariable("id") Long id,
            @RequestBody @Valid AccountStatusUpdateRequest request) {
        log.info("Update account status request for id: {} with status: {}", id, request.getAccountStatus());
        return ResponseData.<AccountResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Account status updated successfully")
                .data(accountService.updateAccountStatus(id, request))
                .build();
    }

    @GetMapping
    @Operation(summary = "Get all accounts", description = "Retrieve a paginated list of all accounts")
    public ResponseData<PageResponse<AccountResponse>> getAllAccounts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String direction) {
        log.info("Get all accounts request: page={}, size={}, sort={}, direction={}", page, size, sort, direction);
        return ResponseData.<PageResponse<AccountResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Accounts retrieved successfully")
                .data(accountService.getAllAccounts(page, size, sort, direction))
                .build();
    }

    @GetMapping("/search")
    @Operation(summary = "Search accounts", description = "Search accounts by keyword and/or status with pagination")
    public ResponseData<PageResponse<AccountResponse>> searchAccounts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String direction) {
        log.info("Search accounts request with keyword: '{}', status: '{}', page={}, size={}, sort={}, direction={}",
                keyword, status, page, size, sort, direction);
        return ResponseData.<PageResponse<AccountResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Accounts search completed successfully")
                .data(accountService.searchAccounts(keyword, role, status, page, size, sort, direction))
                .build();
    }
}
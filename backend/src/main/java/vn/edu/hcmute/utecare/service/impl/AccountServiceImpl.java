package vn.edu.hcmute.utecare.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.edu.hcmute.utecare.dto.request.AccountStatusUpdateRequest;
import vn.edu.hcmute.utecare.dto.response.AccountResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.exception.ResourceNotFoundException;
import vn.edu.hcmute.utecare.mapper.AccountMapper;
import vn.edu.hcmute.utecare.model.Account;
import vn.edu.hcmute.utecare.repository.AccountRepository;
import vn.edu.hcmute.utecare.service.AccountService;
import vn.edu.hcmute.utecare.util.enumeration.AccountStatus;
import vn.edu.hcmute.utecare.util.PaginationUtil;
import vn.edu.hcmute.utecare.util.enumeration.Role;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;

    @Override
    public AccountResponse getAccountById(Long id) {
        log.info("Fetching account by ID: {}", id);
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with ID: " + id));

        return AccountMapper.INSTANCE.toResponse(account);
    }

    @Override
    @Transactional
    //@PreAuthorize("hasRole('ADMIN')")
    public AccountResponse updateAccountStatus(Long id, AccountStatusUpdateRequest request) {
        log.info("Updating status for account ID {}: {}", id, request.getAccountStatus());
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with ID: " + id));

        account.setStatus(request.getAccountStatus());
        return AccountMapper.INSTANCE.toResponse(accountRepository.save(account));
    }

    @Override
    //@PreAuthorize("hasRole('ADMIN')")
    public PageResponse<AccountResponse> getAllAccounts(int page, int size, String sort, String direction) {
        log.info("Fetching all accounts: page={}, size={}, sort={}, direction={}", page, size, sort, direction);
        Pageable pageable = PaginationUtil.createPageable(page, size, sort, direction);
        Page<Account> accountPage = accountRepository.findAll(pageable);

        return PageResponse.<AccountResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(accountPage.getTotalPages())
                .totalElements(accountPage.getTotalElements())
                .content(accountPage.getContent().stream().map(AccountMapper.INSTANCE::toResponse).toList())
                .build();
    }

    @Override
    //@PreAuthorize("hasRole('ADMIN')")
    public PageResponse<AccountResponse> searchAccounts(String keyword, String accountRole, String accountStatus, int page, int size, String sort, String direction) {
        log.info("Searching accounts with keyword: {}", keyword);
        Pageable pageable = PaginationUtil.createPageable(page, size, sort, direction);
        AccountStatus status = accountStatus != null ? AccountStatus.valueOf(accountStatus.toUpperCase()) : null;
        Role role = accountRole != null ? Role.valueOf(accountRole.toUpperCase()) : null;
        Page<Account> accountPage = accountRepository.searchAccounts(keyword, role, status, pageable);

        return PageResponse.<AccountResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(accountPage.getTotalPages())
                .totalElements(accountPage.getTotalElements())
                .content(accountPage.getContent().stream().map(AccountMapper.INSTANCE::toResponse).toList())
                .build();
    }
}

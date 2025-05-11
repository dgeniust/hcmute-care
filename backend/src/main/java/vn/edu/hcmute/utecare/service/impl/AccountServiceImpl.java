package vn.edu.hcmute.utecare.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.utecare.dto.request.AccountStatusUpdateRequest;
import vn.edu.hcmute.utecare.dto.response.AccountResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.exception.ResourceNotFoundException;
import vn.edu.hcmute.utecare.mapper.AccountMapper;
import vn.edu.hcmute.utecare.model.Account;
import vn.edu.hcmute.utecare.repository.AccountRepository;
import vn.edu.hcmute.utecare.service.AccountService;
import vn.edu.hcmute.utecare.util.PaginationUtil;
import vn.edu.hcmute.utecare.util.enumeration.AccountStatus;
import vn.edu.hcmute.utecare.util.enumeration.Role;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    @Override
    @Transactional(readOnly = true)
    public AccountResponse getAccountById(Long id) {
        log.info("Truy xuất tài khoản với ID: {}", id);
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tài khoản với ID: " + id));
        log.info("Truy xuất tài khoản thành công với ID: {}", id);
        return accountMapper.toResponse(account);
    }

    @Override
    @Transactional
    public AccountResponse updateAccountStatus(Long id, AccountStatusUpdateRequest request) {
        log.info("Yêu cầu cập nhật trạng thái tài khoản với ID: {} thành: {}", id, request.getAccountStatus());
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tài khoản với ID: " + id));

        AccountStatus newStatus = request.getAccountStatus();
        if (account.getStatus() == newStatus) {
            log.info("Trạng thái tài khoản với ID: {} đã là {}, không cần cập nhật", id, newStatus);
            return accountMapper.toResponse(account);
        }

        account.setStatus(newStatus);
        Account updatedAccount = accountRepository.save(account);
        log.info("Cập nhật trạng thái tài khoản thành công với ID: {} thành: {}", id, newStatus);
        return accountMapper.toResponse(updatedAccount);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<AccountResponse> getAllAccounts(int page, int size, String sort, String direction) {
        log.info("Truy xuất danh sách tài khoản: trang={}, kích thước={}, sắp xếp={}, hướng={}", page, size, sort, direction);
        Pageable pageable = PaginationUtil.createPageable(page, size, sort, direction);
        Page<Account> accountPage = accountRepository.findAll(pageable);

        return PageResponse.<AccountResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(accountPage.getTotalPages())
                .totalElements(accountPage.getTotalElements())
                .content(accountPage.getContent().stream()
                        .map(accountMapper::toResponse)
                        .toList())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<AccountResponse> searchAccounts(String keyword, Role role, AccountStatus status, int page, int size, String sort, String direction) {
        String searchKeyword = (keyword != null && !keyword.trim().isEmpty()) ? keyword.trim() : null;
        log.info("Tìm kiếm tài khoản với từ khóa: '{}', vai trò: '{}', trạng thái: '{}'", searchKeyword, role, status);
        Pageable pageable = PaginationUtil.createPageable(page, size, sort, direction);
        Page<Account> accountPage = accountRepository.searchAccounts(searchKeyword, role, status, pageable);

        return PageResponse.<AccountResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(accountPage.getTotalPages())
                .totalElements(accountPage.getTotalElements())
                .content(accountPage.getContent().stream()
                        .map(accountMapper::toResponse)
                        .toList())
                .build();
    }
}
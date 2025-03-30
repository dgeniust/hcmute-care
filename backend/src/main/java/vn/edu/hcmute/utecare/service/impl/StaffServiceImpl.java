package vn.edu.hcmute.utecare.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.edu.hcmute.utecare.dto.request.StaffCreationRequest;
import vn.edu.hcmute.utecare.dto.request.StaffRequest;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.dto.response.StaffResponse;
import vn.edu.hcmute.utecare.exception.ResourceNotFoundException;
import vn.edu.hcmute.utecare.mapper.AccountMapper;
import vn.edu.hcmute.utecare.mapper.StaffMapper;
import vn.edu.hcmute.utecare.model.Account;
import vn.edu.hcmute.utecare.model.Staff;
import vn.edu.hcmute.utecare.repository.AccountRepository;
import vn.edu.hcmute.utecare.repository.StaffRepository;
import vn.edu.hcmute.utecare.service.StaffService;
import vn.edu.hcmute.utecare.util.enumeration.AccountStatus;
import vn.edu.hcmute.utecare.util.PaginationUtil;
import vn.edu.hcmute.utecare.util.enumeration.Role;

@Service
@RequiredArgsConstructor
@Slf4j
public class StaffServiceImpl implements StaffService {
    private final AccountRepository accountRepository;
    private final StaffRepository staffRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public StaffResponse createStaff(StaffCreationRequest request) {
        log.info("Creating staff with request: {}", request);

        if (staffRepository.existsByPhone(request.getStaffRequest().getPhone())) {
            throw new IllegalArgumentException("Phone number already exists");
        }

        Staff staff = StaffMapper.INSTANCE.toEntity(request.getStaffRequest());
        Staff savedStaff = staffRepository.saveAndFlush(staff);

        Account account = AccountMapper.INSTANCE.toEntity(request.getAccountRequest());
        account.setPassword(passwordEncoder.encode(request.getAccountRequest().getPassword()));
        account.setUser(staff);
        account.setRole(Role.STAFF);
        account.setStatus(AccountStatus.ACTIVE);

        accountRepository.save(account);

        log.info("Staff created successfully with ID: {}", savedStaff.getId());
        return StaffMapper.INSTANCE.toResponse(savedStaff);
    }

    @Override
    public StaffResponse getStaffById(Long id) {
        log.info("Getting staff by id: {}", id);
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found with ID: " + id));
        return StaffMapper.INSTANCE.toResponse(staff);
    }

    @Override
    @Transactional
    public StaffResponse updateStaff(Long id, StaffRequest request) {
        log.info("Updating staff with id: {} and request: {}", id, request);
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found with ID: " + id));

        if (!staff.getPhone().equals(request.getPhone()) && staffRepository.existsByPhone(request.getPhone())) {
            throw new IllegalArgumentException("Phone number already exists");
        }

        StaffMapper.INSTANCE.updateEntity(request, staff);
        return StaffMapper.INSTANCE.toResponse(staffRepository.save(staff));
    }

    @Override
    @Transactional
    public void deleteStaff(Long id) {
        log.info("Deleting staff with id: {}", id);
        Account account = accountRepository.findByUser_Id(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found for staff with ID: " + id));
        accountRepository.delete(account);
        log.info("Successfully deleted staff with id: {}", id);
    }

    @Override
    public PageResponse<StaffResponse> getAllStaff(int page, int size, String sort, String direction) {
        log.info("Fetching all staff with pagination: page={}, size={}, sort={}, direction={}", page, size, sort, direction);
        Pageable pageable = PaginationUtil.createPageable(page, size, sort, direction);

        Page<Staff> staffPage = staffRepository.findAll(pageable);
        return PageResponse.<StaffResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(staffPage.getTotalPages())
                .totalElements(staffPage.getTotalElements())
                .content(staffPage.getContent().stream().map(StaffMapper.INSTANCE::toResponse).toList())
                .build();
    }

    @Override
    public PageResponse<StaffResponse> searchStaff(String keyword, int page, int size, String sort, String direction) {
        log.info("Searching staff with keyword: {}, page={}, size={}, sort={}, direction={}", keyword, page, size, sort, direction);
        Pageable pageable = PaginationUtil.createPageable(page, size, sort, direction);

        Page<Staff> staffPage = staffRepository.searchStaff(keyword, pageable);
        return PageResponse.<StaffResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(staffPage.getTotalPages())
                .totalElements(staffPage.getTotalElements())
                .content(staffPage.getContent().stream().map(StaffMapper.INSTANCE::toResponse).toList())
                .build();
    }
}
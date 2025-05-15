package vn.edu.hcmute.utecare.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.utecare.dto.request.StaffRequest;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.dto.response.StaffResponse;
import vn.edu.hcmute.utecare.exception.ConflictException;
import vn.edu.hcmute.utecare.exception.ResourceNotFoundException;
import vn.edu.hcmute.utecare.mapper.StaffMapper;
import vn.edu.hcmute.utecare.model.Account;
import vn.edu.hcmute.utecare.model.Staff;
import vn.edu.hcmute.utecare.repository.AccountRepository;
import vn.edu.hcmute.utecare.repository.StaffRepository;
import vn.edu.hcmute.utecare.service.StaffService;
import vn.edu.hcmute.utecare.util.PaginationUtil;
import vn.edu.hcmute.utecare.util.enumeration.AccountStatus;
import vn.edu.hcmute.utecare.util.enumeration.Role;

@Service
@RequiredArgsConstructor
@Slf4j
public class StaffServiceImpl implements StaffService {
    private final AccountRepository accountRepository;
    private final StaffRepository staffRepository;
    private final StaffMapper staffMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public StaffResponse createStaff(StaffRequest request) {
        log.info("Tạo nhân viên mới với thông tin: {}", request);

        if (staffRepository.existsByPhone(request.getPhone())) {
            throw new ConflictException("Số điện thoại đã tồn tại: " + request.getPhone());
        }

        Staff staff = staffMapper.toEntity(request);
        Staff savedStaff = staffRepository.save(staff);

        Account account = Account.builder()
                .password(passwordEncoder.encode(request.getPhone()))
                .user(savedStaff)
                .role(Role.STAFF)
                .status(AccountStatus.ACTIVE)
                .build();
        accountRepository.save(account);

        log.info("Tạo nhân viên thành công với ID: {}", savedStaff.getId());
        return staffMapper.toResponse(savedStaff);
    }

    @Override
    @Transactional(readOnly = true)
    public StaffResponse getStaffById(Long id) {
        log.info("Truy xuất nhân viên với ID: {}", id);
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhân viên với ID: " + id));
        log.info("Truy xuất nhân viên thành công với ID: {}", id);
        return staffMapper.toResponse(staff);
    }

    @Override
    @Transactional
    public StaffResponse updateStaff(Long id, StaffRequest request) {
        log.info("Cập nhật nhân viên với ID: {} và thông tin: {}", id, request);
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhân viên với ID: " + id));

        if (!staff.getPhone().equals(request.getPhone()) && staffRepository.existsByPhone(request.getPhone())) {
            throw new ConflictException("Số điện thoại đã tồn tại: " + request.getPhone());
        }

        staffMapper.updateEntity(request, staff);
        Staff updatedStaff = staffRepository.save(staff);
        log.info("Cập nhật nhân viên thành công với ID: {}", id);
        return staffMapper.toResponse(updatedStaff);
    }

    @Override
    @Transactional
    public void deleteStaff(Long id) {
        log.info("Xóa nhân viên với ID: {}", id);
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhân viên với ID: " + id));
        Account account = accountRepository.findByUser_Id(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tài khoản cho nhân viên với ID: " + id));
        accountRepository.delete(account);
        staffRepository.delete(staff);
        log.info("Xóa nhân viên thành công với ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<StaffResponse> getAllStaff(int page, int size, String sort, String direction) {
        log.info("Truy xuất danh sách nhân viên: trang={}, kích thước={}, sắp xếp={}, hướng={}", page, size, sort, direction);
        Pageable pageable = PaginationUtil.createPageable(page, size, sort, direction);
        Page<Staff> staffPage = staffRepository.findAll(pageable);
        return PageResponse.<StaffResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(staffPage.getTotalPages())
                .totalElements(staffPage.getTotalElements())
                .content(staffPage.getContent().stream()
                        .map(staffMapper::toResponse)
                        .toList())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<StaffResponse> searchStaff(String keyword, int page, int size, String sort, String direction) {
        log.info("Tìm kiếm nhân viên: từ khóa={}, trang={}, kích thước={}, sắp xếp={}, hướng={}", keyword, page, size, sort, direction);
        Pageable pageable = PaginationUtil.createPageable(page, size, sort, direction);
        Page<Staff> staffPage = staffRepository.searchStaff(keyword, pageable);
        return PageResponse.<StaffResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(staffPage.getTotalPages())
                .totalElements(staffPage.getTotalElements())
                .content(staffPage.getContent().stream()
                        .map(staffMapper::toResponse)
                        .toList())
                .build();
    }
}
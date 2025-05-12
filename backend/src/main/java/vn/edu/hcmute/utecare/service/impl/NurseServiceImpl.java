package vn.edu.hcmute.utecare.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.edu.hcmute.utecare.dto.request.NurseRequest;
import vn.edu.hcmute.utecare.dto.response.NurseResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.exception.ResourceNotFoundException;
import vn.edu.hcmute.utecare.mapper.NurseMapper;
import vn.edu.hcmute.utecare.model.Account;
import vn.edu.hcmute.utecare.model.MedicalSpecialty;
import vn.edu.hcmute.utecare.model.Nurse;
import vn.edu.hcmute.utecare.repository.AccountRepository;
import vn.edu.hcmute.utecare.repository.MedicalSpecialtyRepository;
import vn.edu.hcmute.utecare.repository.NurseRepository;
import vn.edu.hcmute.utecare.service.NurseService;
import vn.edu.hcmute.utecare.util.enumeration.AccountStatus;
import vn.edu.hcmute.utecare.util.enumeration.Role;
import vn.edu.hcmute.utecare.util.PaginationUtil;

@Service
@RequiredArgsConstructor
@Slf4j
public class NurseServiceImpl implements NurseService {
    private final AccountRepository accountRepository;
    private final MedicalSpecialtyRepository medicalSpecialtyRepository;
    private final NurseRepository nurseRepository;
    private final NurseMapper nurseMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public NurseResponse createNurse(NurseRequest request) {
        log.info("Tạo y tá mới với thông tin: {}", request);

        if (nurseRepository.existsByPhone(request.getPhone())) {
            throw new IllegalArgumentException("Số điện thoại đã tồn tại");
        }

        Nurse nurse = nurseMapper.toEntity(request);
        if (request.getMedicalSpecialtyId() != null) {
            MedicalSpecialty specialty = medicalSpecialtyRepository.findById(request.getMedicalSpecialtyId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy chuyên khoa với ID: " + request.getMedicalSpecialtyId()));
            nurse.setMedicalSpecialty(specialty);
        }
        Nurse savedNurse = nurseRepository.save(nurse);
        Account account = Account.builder()
                .password(passwordEncoder.encode(request.getPhone()))
                .user(savedNurse)
                .role(Role.NURSE)
                .status(AccountStatus.ACTIVE)
                .build();
        accountRepository.save(account);
        log.info("Tạo y tá thành công với ID: {}", savedNurse.getId());
        return nurseMapper.toResponse(savedNurse);
    }

    @Override
    public NurseResponse getNurseById(Long id) {
        log.info("Truy xuất y tá với ID: {}", id);
        Nurse nurse = nurseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy y tá với ID: " + id));
        log.info("Truy xuất y tá thành công với ID: {}", id);
        return nurseMapper.toResponse(nurse);
    }

    @Override
    @Transactional
    public NurseResponse updateNurse(Long id, NurseRequest request) {
        log.info("Cập nhật y tá với ID: {} và thông tin: {}", id, request);
        Nurse nurse = nurseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy y tá với ID: " + id));

        if (!nurse.getPhone().equals(request.getPhone()) && nurseRepository.existsByPhone(request.getPhone())) {
            throw new IllegalArgumentException("Số điện thoại đã tồn tại");
        }

        nurseMapper.updateEntity(request, nurse);
        if (request.getMedicalSpecialtyId() != null) {
            MedicalSpecialty specialty = medicalSpecialtyRepository.findById(request.getMedicalSpecialtyId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy chuyên khoa với ID: " + request.getMedicalSpecialtyId()));
            nurse.setMedicalSpecialty(specialty);
        }
        Nurse updatedNurse = nurseRepository.save(nurse);
        log.info("Cập nhật y tá thành công với ID: {}", id);
        return nurseMapper.toResponse(updatedNurse);
    }

    @Override
    @Transactional
    public void deleteNurse(Long id) {
        log.info("Xóa y tá với ID: {}", id);
        Account account = accountRepository.findByUser_Id(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tài khoản cho y tá với ID: " + id));
        accountRepository.delete(account);
        log.info("Xóa y tá thành công với ID: {}", id);
    }

    @Override
    public PageResponse<NurseResponse> getAllNurses(int page, int size, String sort, String direction) {
        log.info("Truy xuất danh sách y tá: trang={}, kích thước={}, sắp xếp={}, hướng={}", page, size, sort, direction);
        Pageable pageable = PaginationUtil.createPageable(page, size, sort, direction);

        Page<Nurse> nursePage = nurseRepository.findAll(pageable);
        return PageResponse.<NurseResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(nursePage.getTotalPages())
                .totalElements(nursePage.getTotalElements())
                .content(nursePage.getContent().stream().map(nurseMapper::toResponse).toList())
                .build();
    }

    @Override
    public PageResponse<NurseResponse> searchNurses(String keyword, int page, int size, String sort, String direction) {
        log.info("Tìm kiếm y tá với từ khóa: {}, trang={}, kích thước={}, sắp xếp={}, hướng={}", keyword, page, size, sort, direction);
        Pageable pageable = PaginationUtil.createPageable(page, size, sort, direction);

        Page<Nurse> nursePage = nurseRepository.searchNurses(keyword, pageable);
        return PageResponse.<NurseResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(nursePage.getTotalPages())
                .totalElements(nursePage.getTotalElements())
                .content(nursePage.getContent().stream().map(nurseMapper::toResponse).toList())
                .build();
    }

    @Override
    public PageResponse<NurseResponse> getNursesByMedicalSpecialtyId(Integer medicalSpecialtyId, int page, int size, String sort, String direction) {
        log.info("Truy xuất danh sách y tá cho chuyên khoa với ID: {}, trang={}, kích thước={}, sắp xếp={}, hướng={}", medicalSpecialtyId, page, size, sort, direction);
        Pageable pageable = PaginationUtil.createPageable(page, size, sort, direction);

        Page<Nurse> nursePage = nurseRepository.findByMedicalSpecialty_Id(medicalSpecialtyId, pageable);
        return PageResponse.<NurseResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(nursePage.getTotalPages())
                .totalElements(nursePage.getTotalElements())
                .content(nursePage.getContent().stream().map(nurseMapper::toResponse).toList())
                .build();
    }

    @Override
    public PageResponse<NurseResponse> searchNursesByMedicalSpecialtyId(Integer medicalSpecialtyId, String keyword, int page, int size, String sort, String direction) {
        log.info("Tìm kiếm y tá cho chuyên khoa với ID: {} và từ khóa: {}, trang={}, kích thước={}, sắp xếp={}, hướng={}", medicalSpecialtyId, keyword, page, size, sort, direction);
        Pageable pageable = PaginationUtil.createPageable(page, size, sort, direction);

        Page<Nurse> nursePage = nurseRepository.searchNursesByMedicalSpecialty(medicalSpecialtyId, keyword, pageable);
        return PageResponse.<NurseResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(nursePage.getTotalPages())
                .totalElements(nursePage.getTotalElements())
                .content(nursePage.getContent().stream().map(nurseMapper::toResponse).toList())
                .build();
    }
}
package vn.edu.hcmute.utecare.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.edu.hcmute.utecare.dto.request.NurseCreationRequest;
import vn.edu.hcmute.utecare.dto.request.NurseRequest;
import vn.edu.hcmute.utecare.dto.response.NurseResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.exception.ResourceNotFoundException;
import vn.edu.hcmute.utecare.mapper.AccountMapper;
import vn.edu.hcmute.utecare.mapper.NurseMapper;
import vn.edu.hcmute.utecare.model.Account;
import vn.edu.hcmute.utecare.model.Nurse;
import vn.edu.hcmute.utecare.repository.AccountRepository;
import vn.edu.hcmute.utecare.repository.NurseRepository;
import vn.edu.hcmute.utecare.service.NurseService;
import vn.edu.hcmute.utecare.util.AccountStatus;
import vn.edu.hcmute.utecare.util.PaginationUtil;
import vn.edu.hcmute.utecare.util.Role;

@Service
@RequiredArgsConstructor
@Slf4j
public class NurseServiceImpl implements NurseService {
    private final AccountRepository accountRepository;
    private final NurseRepository nurseRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public NurseResponse createNurse(NurseCreationRequest request) {
        log.info("Creating nurse with request: {}", request);

        if (nurseRepository.existsByPhone(request.getNurseRequest().getPhone())) {
            throw new IllegalArgumentException("Phone number already exists");
        }

        Nurse nurse = NurseMapper.INSTANCE.toEntity(request.getNurseRequest());
        Nurse savedNurse = nurseRepository.saveAndFlush(nurse);

        Account account = AccountMapper.INSTANCE.toEntity(request.getAccountRequest());
        account.setPassword(passwordEncoder.encode(request.getAccountRequest().getPassword()));
        account.setUser(nurse);
        account.setRole(Role.NURSE);
        account.setStatus(AccountStatus.ACTIVE);

        accountRepository.save(account);

        return NurseMapper.INSTANCE.toResponse(savedNurse);
    }

    @Override
    public NurseResponse getNurseById(Long id) {
        log.info("Getting nurse by id: {}", id);
        Nurse nurse = nurseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Nurse not found with ID: " + id));
        return NurseMapper.INSTANCE.toResponse(nurse);
    }

    @Override
    @Transactional
    public NurseResponse updateNurse(Long id, NurseRequest request) {
        log.info("Updating nurse with id: {} and request: {}", id, request);
        Nurse nurse = nurseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Nurse not found with ID: " + id));

        if (nurseRepository.existsByPhone(request.getPhone())) {
            throw new IllegalArgumentException("Phone number already exists");
        }

        NurseMapper.INSTANCE.updateEntity(request, nurse);
        return NurseMapper.INSTANCE.toResponse(nurseRepository.save(nurse));
    }

    @Override
    @Transactional
    public void deleteNurse(Long id) {
        log.info("Deleting nurse with id: {}", id);
        Account account = accountRepository.findByUser_Id(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found for nurse with ID: " + id));
        accountRepository.delete(account);
        log.info("Successfully deleted nurse with id: {}", id);
    }

    @Override
    public PageResponse<NurseResponse> getAllNurses(int page, int size, String sort, String direction) {
        log.info("Fetching all nurses with pagination: page={}, size={}, sort={}, direction={}", page, size, sort, direction);
        Pageable pageable = PaginationUtil.createPageable(page, size, sort, direction);

        Page<Nurse> nursePage = nurseRepository.findAll(pageable);
        return PageResponse.<NurseResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(nursePage.getTotalPages())
                .totalElements(nursePage.getTotalElements())
                .content(nursePage.getContent().stream().map(NurseMapper.INSTANCE::toResponse).toList())
                .build();
    }

    @Override
    public PageResponse<NurseResponse> searchNurses(String keyword, int page, int size, String sort, String direction) {
        log.info("Searching nurses with keyword: {}, page={}, size={}, sort={}, direction={}", keyword, page, size, sort, direction);
        Pageable pageable = PaginationUtil.createPageable(page, size, sort, direction);

        Page<Nurse> nursePage = nurseRepository.searchNurses(keyword, pageable);
        return PageResponse.<NurseResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(nursePage.getTotalPages())
                .totalElements(nursePage.getTotalElements())
                .content(nursePage.getContent().stream().map(NurseMapper.INSTANCE::toResponse).toList())
                .build();
    }
}
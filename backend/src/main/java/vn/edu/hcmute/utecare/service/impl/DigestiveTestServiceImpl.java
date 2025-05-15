package vn.edu.hcmute.utecare.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.utecare.dto.request.DigestiveTestRequest;
import vn.edu.hcmute.utecare.dto.response.DigestiveTestResponse;
import vn.edu.hcmute.utecare.exception.ResourceNotFoundException;
import vn.edu.hcmute.utecare.mapper.DigestiveTestMapper;
import vn.edu.hcmute.utecare.model.DigestiveTest;
import vn.edu.hcmute.utecare.model.Encounter;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.repository.DigestiveTestRepository;
import vn.edu.hcmute.utecare.repository.EncounterRepository;
import vn.edu.hcmute.utecare.service.DigestiveTestService;
import vn.edu.hcmute.utecare.util.PaginationUtil;
import vn.edu.hcmute.utecare.util.enumeration.EMedicalTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DigestiveTestServiceImpl implements DigestiveTestService {

    private final DigestiveTestRepository digestiveTestRepository;
    private final EncounterRepository encounterRepository;
    private final DigestiveTestMapper digestiveTestMapper;

    @Override
    @Transactional
    public DigestiveTestResponse createDigestiveTest(DigestiveTestRequest request) {
        log.info("Tạo xét nghiệm tiêu hóa mới: {}", request);
        Encounter encounter = encounterRepository.findById(request.getEncounterId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy cuộc gặp với ID: " + request.getEncounterId()));
        DigestiveTest digestiveTest = digestiveTestMapper.toEntity(request);
        digestiveTest.setEncounter(encounter);
        digestiveTest.setStatus(EMedicalTest.PENDING);
        DigestiveTest savedDigestiveTest = digestiveTestRepository.save(digestiveTest);
        log.info("Tạo xét nghiệm tiêu hóa thành công với ID: {}", savedDigestiveTest.getId());
        return digestiveTestMapper.toResponse(savedDigestiveTest);
    }

    @Override
    @Transactional(readOnly = true)
    public DigestiveTestResponse getDigestiveTestById(Long id) {
        log.info("Truy xuất xét nghiệm tiêu hóa với ID: {}", id);
        DigestiveTest digestiveTest = digestiveTestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy xét nghiệm tiêu hóa với ID: " + id));
        log.info("Truy xuất xét nghiệm tiêu hóa thành công với ID: {}", id);
        return digestiveTestMapper.toResponse(digestiveTest);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DigestiveTestResponse> getAll() {
        log.info("Truy xuất danh sách tất cả xét nghiệm tiêu hóa");
        return digestiveTestRepository.findAll().stream()
                .map(digestiveTestMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<DigestiveTestResponse> getAll(int page, int size, String sort, String direction) {
        log.info("Truy xuất danh sách xét nghiệm tiêu hóa: trang={}, kích thước={}, sắp xếp={}, hướng={}", page, size, sort, direction);
        Pageable pageable = PaginationUtil.createPageable(page, size, sort, direction);
        Page<DigestiveTest> digestiveTestPage = digestiveTestRepository.findAll(pageable);
        return PageResponse.<DigestiveTestResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(digestiveTestPage.getTotalPages())
                .totalElements(digestiveTestPage.getTotalElements())
                .content(digestiveTestPage.getContent().stream()
                        .map(digestiveTestMapper::toResponse)
                        .toList())
                .build();
    }

    @Override
    @Transactional
    public DigestiveTestResponse updateDigestiveTest(Long id, DigestiveTestRequest request) {
        log.info("Cập nhật xét nghiệm tiêu hóa với ID: {} và thông tin: {}", id, request);
        DigestiveTest digestiveTest = digestiveTestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy xét nghiệm tiêu hóa với ID: " + id));
        Encounter encounter = encounterRepository.findById(request.getEncounterId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy cuộc gặp với ID: " + request.getEncounterId()));
        digestiveTestMapper.updateEntity(digestiveTest, request);
        digestiveTest.setEncounter(encounter);
        DigestiveTest updatedDigestiveTest = digestiveTestRepository.save(digestiveTest);
        log.info("Cập nhật xét nghiệm tiêu hóa thành công với ID: {}", id);
        return digestiveTestMapper.toResponse(updatedDigestiveTest);
    }

    @Override
    @Transactional
    public void deleteDigestiveTest(Long id) {
        log.info("Xóa xét nghiệm tiêu hóa với ID: {}", id);
        DigestiveTest digestiveTest = digestiveTestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy xét nghiệm tiêu hóa với ID: " + id));
        digestiveTestRepository.delete(digestiveTest);
        log.info("Xóa xét nghiệm tiêu hóa thành công với ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DigestiveTestResponse> getAllLabTestByDateAndStatus(LocalDate date, String status) {
        log.info("Truy xuất danh sách xét nghiệm tiêu hóa theo ngày: {} và trạng thái: {}", date, status);
        try {
            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime endOfDay = date.atTime(23, 59, 59);
            EMedicalTest statusEnum = EMedicalTest.valueOf(status);
            return digestiveTestRepository.findByCreateDateBetweenAndStatus(startOfDay, endOfDay, statusEnum)
                    .stream()
                    .map(digestiveTestMapper::toResponse)
                    .toList();
        } catch (IllegalArgumentException e) {
            throw new ResourceNotFoundException("Trạng thái xét nghiệm không hợp lệ: " + status);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<DigestiveTestResponse> getEncounterIdAndDate(Long encounterId, LocalDate date) {
        log.info("Truy xuất danh sách xét nghiệm tiêu hóa theo cuộc gặp ID: {} và ngày: {}", encounterId, date);
        LocalDate queryDate = (date != null) ? date : LocalDate.now();
        LocalDateTime startOfDay = queryDate.atStartOfDay();
        LocalDateTime endOfDay = queryDate.atTime(23, 59, 59);
        return digestiveTestRepository.findByEncounterIdAndCreateDateBetween(encounterId, startOfDay, endOfDay)
                .stream()
                .map(digestiveTestMapper::toResponse)
                .toList();
    }
}
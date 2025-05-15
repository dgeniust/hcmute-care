package vn.edu.hcmute.utecare.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.utecare.dto.request.LaboratoryTestsRequest;
import vn.edu.hcmute.utecare.dto.response.LaboratoryTestsResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.exception.ResourceNotFoundException;
import vn.edu.hcmute.utecare.mapper.LaboratoryTestsMapper;
import vn.edu.hcmute.utecare.model.Encounter;
import vn.edu.hcmute.utecare.model.LaboratoryTests;
import vn.edu.hcmute.utecare.repository.EncounterRepository;
import vn.edu.hcmute.utecare.repository.LaboratoryTestsRepository;
import vn.edu.hcmute.utecare.service.LaboratoryTestsService;
import vn.edu.hcmute.utecare.util.PaginationUtil;
import vn.edu.hcmute.utecare.util.enumeration.EMedicalTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LaboratoryTestsServiceImpl implements LaboratoryTestsService {

    private final LaboratoryTestsRepository laboratoryTestsRepository;
    private final EncounterRepository encounterRepository;
    private final LaboratoryTestsMapper laboratoryTestsMapper;

    @Override
    @Transactional
    public LaboratoryTestsResponse createLaboratoryTests(LaboratoryTestsRequest request) {
        log.info("Tạo xét nghiệm phòng thí nghiệm mới: {}", request);
        Encounter encounter = encounterRepository.findById(request.getEncounterId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy cuộc gặp với ID: " + request.getEncounterId()));
        LaboratoryTests laboratoryTests = laboratoryTestsMapper.toEntity(request);
        laboratoryTests.setEncounter(encounter);
        laboratoryTests.setStatus(EMedicalTest.PENDING);
        LaboratoryTests saved = laboratoryTestsRepository.save(laboratoryTests);
        log.info("Tạo xét nghiệm phòng thí nghiệm thành công với ID: {}", saved.getId());
        return laboratoryTestsMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public LaboratoryTestsResponse getLaboratoryTestsById(Long id) {
        log.info("Truy xuất xét nghiệm phòng thí nghiệm với ID: {}", id);
        LaboratoryTests laboratoryTests = laboratoryTestsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy xét nghiệm phòng thí nghiệm với ID: " + id));
        log.info("Truy xuất xét nghiệm phòng thí nghiệm thành công với ID: {}", id);
        return laboratoryTestsMapper.toResponse(laboratoryTests);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LaboratoryTestsResponse> getAll() {
        log.info("Truy xuất danh sách tất cả xét nghiệm phòng thí nghiệm");
        return laboratoryTestsRepository.findAll().stream()
                .map(laboratoryTestsMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<LaboratoryTestsResponse> getAll(int page, int size, String sort, String direction) {
        log.info("Truy xuất danh sách xét nghiệm phòng thí nghiệm: trang={}, kích thước={}, sắp xếp={}, hướng={}", page, size, sort, direction);
        Pageable pageable = PaginationUtil.createPageable(page, size, sort, direction);
        Page<LaboratoryTests> laboratoryTestsPage = laboratoryTestsRepository.findAll(pageable);
        return PageResponse.<LaboratoryTestsResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(laboratoryTestsPage.getTotalPages())
                .totalElements(laboratoryTestsPage.getTotalElements())
                .content(laboratoryTestsPage.getContent().stream()
                        .map(laboratoryTestsMapper::toResponse)
                        .toList())
                .build();
    }

    @Override
    @Transactional
    public LaboratoryTestsResponse updateLaboratoryTests(Long id, LaboratoryTestsRequest request) {
        log.info("Cập nhật xét nghiệm phòng thí nghiệm với ID: {} và thông tin: {}", id, request);
        LaboratoryTests laboratoryTests = laboratoryTestsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy xét nghiệm phòng thí nghiệm với ID: " + id));
        Encounter encounter = encounterRepository.findById(request.getEncounterId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy cuộc gặp với ID: " + request.getEncounterId()));
        laboratoryTestsMapper.updateEntity(laboratoryTests, request);
        laboratoryTests.setEncounter(encounter);
        LaboratoryTests updated = laboratoryTestsRepository.save(laboratoryTests);
        log.info("Cập nhật xét nghiệm phòng thí nghiệm thành công với ID: {}", id);
        return laboratoryTestsMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void deleteLaboratoryTests(Long id) {
        log.info("Xóa xét nghiệm phòng thí nghiệm với ID: {}", id);
        LaboratoryTests laboratoryTests = laboratoryTestsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy xét nghiệm phòng thí nghiệm với ID: " + id));
        laboratoryTestsRepository.delete(laboratoryTests);
        log.info("Xóa xét nghiệm phòng thí nghiệm thành công với ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LaboratoryTestsResponse> getAllLabTestByDateAndStatus(LocalDate date, String status) {
        log.info("Truy xuất danh sách xét nghiệm phòng thí nghiệm theo ngày: {} và trạng thái: {}", date, status);
        try {
            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime endOfDay = date.atTime(23, 59, 59);
            EMedicalTest statusEnum = EMedicalTest.valueOf(status);
            return laboratoryTestsRepository.findByCreateDateBetweenAndStatus(startOfDay, endOfDay, statusEnum)
                    .stream()
                    .map(laboratoryTestsMapper::toResponse)
                    .toList();
        } catch (IllegalArgumentException e) {
            throw new ResourceNotFoundException("Trạng thái xét nghiệm không hợp lệ: " + status);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<LaboratoryTestsResponse> getEncounterIdAndDate(Long encounterId, LocalDate date) {
        log.info("Truy xuất danh sách xét nghiệm phòng thí nghiệm theo cuộc gặp ID: {} và ngày: {}", encounterId, date);
        LocalDate queryDate = (date != null) ? date : LocalDate.now();
        LocalDateTime startOfDay = queryDate.atStartOfDay();
        LocalDateTime endOfDay = queryDate.atTime(23, 59, 59);
        return laboratoryTestsRepository.findByEncounterIdAndCreateDateBetween(encounterId, startOfDay, endOfDay)
                .stream()
                .map(laboratoryTestsMapper::toResponse)
                .toList();
    }
}
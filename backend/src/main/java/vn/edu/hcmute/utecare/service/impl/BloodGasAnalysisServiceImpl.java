package vn.edu.hcmute.utecare.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.utecare.dto.request.BloodGasAnalysisRequest;
import vn.edu.hcmute.utecare.dto.response.BloodGasAnalysisResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.exception.ResourceNotFoundException;
import vn.edu.hcmute.utecare.mapper.BloodGasAnalysisMapper;
import vn.edu.hcmute.utecare.model.BloodGasAnalysis;
import vn.edu.hcmute.utecare.model.Encounter;
import vn.edu.hcmute.utecare.repository.BloodGasAnalysisRepository;
import vn.edu.hcmute.utecare.repository.EncounterRepository;
import vn.edu.hcmute.utecare.service.BloodGasAnalysisService;
import vn.edu.hcmute.utecare.util.PaginationUtil;
import vn.edu.hcmute.utecare.util.enumeration.EMedicalTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BloodGasAnalysisServiceImpl implements BloodGasAnalysisService {

    private final BloodGasAnalysisRepository bloodGasAnalysisRepository;
    private final EncounterRepository encounterRepository;
    private final BloodGasAnalysisMapper bloodGasAnalysisMapper;

    @Override
    @Transactional
    public BloodGasAnalysisResponse createBloodGasAnalysis(BloodGasAnalysisRequest request) {
        log.info("Tạo xét nghiệm phân tích khí máu mới: {}", request);
        Encounter encounter = encounterRepository.findById(request.getEncounterId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy cuộc gặp với ID: " + request.getEncounterId()));
        BloodGasAnalysis bloodGasAnalysis = bloodGasAnalysisMapper.toEntity(request);
        bloodGasAnalysis.setEncounter(encounter);
        bloodGasAnalysis.setStatus(EMedicalTest.PENDING);
        BloodGasAnalysis saved = bloodGasAnalysisRepository.save(bloodGasAnalysis);
        log.info("Tạo xét nghiệm phân tích khí máu thành công với ID: {}", saved.getId());
        return bloodGasAnalysisMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public BloodGasAnalysisResponse getBloodGasAnalysisById(Long id) {
        log.info("Truy xuất xét nghiệm phân tích khí máu với ID: {}", id);
        BloodGasAnalysis bloodGasAnalysis = bloodGasAnalysisRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy xét nghiệm phân tích khí máu với ID: " + id));
        log.info("Truy xuất xét nghiệm phân tích khí máu thành công với ID: {}", id);
        return bloodGasAnalysisMapper.toResponse(bloodGasAnalysis);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BloodGasAnalysisResponse> getAll() {
        log.info("Truy xuất danh sách tất cả xét nghiệm phân tích khí máu");
        return bloodGasAnalysisRepository.findAll().stream()
                .map(bloodGasAnalysisMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<BloodGasAnalysisResponse> getAll(int page, int size, String sort, String direction) {
        log.info("Truy xuất danh sách xét nghiệm phân tích khí máu: trang={}, kích thước={}, sắp xếp={}, hướng={}", page, size, sort, direction);
        Pageable pageable = PaginationUtil.createPageable(page, size, sort, direction);
        Page<BloodGasAnalysis> bloodGasAnalysisPage = bloodGasAnalysisRepository.findAll(pageable);
        return PageResponse.<BloodGasAnalysisResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(bloodGasAnalysisPage.getTotalPages())
                .totalElements(bloodGasAnalysisPage.getTotalElements())
                .content(bloodGasAnalysisPage.getContent().stream()
                        .map(bloodGasAnalysisMapper::toResponse)
                        .toList())
                .build();
    }

    @Override
    @Transactional
    public BloodGasAnalysisResponse updateBloodGasAnalysis(Long id, BloodGasAnalysisRequest request) {
        log.info("Cập nhật xét nghiệm phân tích khí máu với ID: {} và thông tin: {}", id, request);
        BloodGasAnalysis bloodGasAnalysis = bloodGasAnalysisRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy xét nghiệm phân tích khí máu với ID: " + id));
        Encounter encounter = encounterRepository.findById(request.getEncounterId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy cuộc gặp với ID: " + request.getEncounterId()));
        bloodGasAnalysisMapper.updateEntity(bloodGasAnalysis, request);
        bloodGasAnalysis.setEncounter(encounter);
        BloodGasAnalysis updated = bloodGasAnalysisRepository.save(bloodGasAnalysis);
        log.info("Cập nhật xét nghiệm phân tích khí máu thành công với ID: {}", id);
        return bloodGasAnalysisMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void deleteBloodGasAnalysis(Long id) {
        log.info("Xóa xét nghiệm phân tích khí máu với ID: {}", id);
        BloodGasAnalysis bloodGasAnalysis = bloodGasAnalysisRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy xét nghiệm phân tích khí máu với ID: " + id));
        bloodGasAnalysisRepository.delete(bloodGasAnalysis);
        log.info("Xóa xét nghiệm phân tích khí máu thành công với ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BloodGasAnalysisResponse> getAllLabTestByDateAndStatus(LocalDate date, String status) {
        log.info("Truy xuất danh sách xét nghiệm phân tích khí máu theo ngày: {} và trạng thái: {}", date, status);
        try {
            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime endOfDay = date.atTime(23, 59, 59);
            EMedicalTest statusEnum = EMedicalTest.valueOf(status);
            return bloodGasAnalysisRepository.findByCreateDateBetweenAndStatus(startOfDay, endOfDay, statusEnum)
                    .stream()
                    .map(bloodGasAnalysisMapper::toResponse)
                    .toList();
        } catch (IllegalArgumentException e) {
            throw new ResourceNotFoundException("Trạng thái xét nghiệm không hợp lệ: " + status);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<BloodGasAnalysisResponse> getEncounterIdAndDate(Long encounterId, LocalDate date) {
        log.info("Truy xuất danh sách xét nghiệm phân tích khí máu theo cuộc gặp ID: {} và ngày: {}", encounterId, date);
        LocalDate queryDate = (date != null) ? date : LocalDate.now();
        LocalDateTime startOfDay = queryDate.atStartOfDay();
        LocalDateTime endOfDay = queryDate.atTime(23, 59, 59);
        return bloodGasAnalysisRepository.findByEncounterIdAndCreateDateBetween(encounterId, startOfDay, endOfDay)
                .stream()
                .map(bloodGasAnalysisMapper::toResponse)
                .toList();
    }
}
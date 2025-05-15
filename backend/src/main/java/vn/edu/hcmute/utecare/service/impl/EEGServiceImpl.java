package vn.edu.hcmute.utecare.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.utecare.dto.request.EEGRequest;
import vn.edu.hcmute.utecare.dto.response.EEGResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.exception.ResourceNotFoundException;
import vn.edu.hcmute.utecare.mapper.EEGMapper;
import vn.edu.hcmute.utecare.model.EEG;
import vn.edu.hcmute.utecare.model.Encounter;
import vn.edu.hcmute.utecare.repository.EEGRepository;
import vn.edu.hcmute.utecare.repository.EncounterRepository;
import vn.edu.hcmute.utecare.service.EEGService;
import vn.edu.hcmute.utecare.util.PaginationUtil;
import vn.edu.hcmute.utecare.util.enumeration.EMedicalTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EEGServiceImpl implements EEGService {

    private final EEGRepository eegRepository;
    private final EncounterRepository encounterRepository;
    private final EEGMapper eegMapper;

    @Override
    @Transactional
    public EEGResponse createEEG(EEGRequest request) {
        log.info("Tạo xét nghiệm EEG mới: {}", request);
        Encounter encounter = encounterRepository.findById(request.getEncounterId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy cuộc gặp với ID: " + request.getEncounterId()));
        EEG eeg = eegMapper.toEntity(request);
        eeg.setEncounter(encounter);
        eeg.setStatus(EMedicalTest.PENDING);
        EEG saved = eegRepository.save(eeg);
        log.info("Tạo xét nghiệm EEG thành công với ID: {}", saved.getId());
        return eegMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public EEGResponse getEEGById(Long id) {
        log.info("Truy xuất xét nghiệm EEG với ID: {}", id);
        EEG eeg = eegRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy xét nghiệm EEG với ID: " + id));
        log.info("Truy xuất xét nghiệm EEG thành công với ID: {}", id);
        return eegMapper.toResponse(eeg);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EEGResponse> getAll() {
        log.info("Truy xuất danh sách tất cả xét nghiệm EEG");
        return eegRepository.findAll().stream()
                .map(eegMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<EEGResponse> getAll(int page, int size, String sort, String direction) {
        log.info("Truy xuất danh sách EEG: trang={}, kích thước={}, sắp xếp={}, hướng={}", page, size, sort, direction);
        Pageable pageable = PaginationUtil.createPageable(page, size, sort, direction);
        Page<EEG> eegPage = eegRepository.findAll(pageable);
        return PageResponse.<EEGResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(eegPage.getTotalPages())
                .totalElements(eegPage.getTotalElements())
                .content(eegPage.getContent().stream()
                        .map(eegMapper::toResponse)
                        .toList())
                .build();
    }

    @Override
    @Transactional
    public EEGResponse updateEEG(Long id, EEGRequest request) {
        log.info("Cập nhật xét nghiệm EEG với ID: {} và thông tin: {}", id, request);
        EEG eeg = eegRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy xét nghiệm EEG với ID: " + id));
        Encounter encounter = encounterRepository.findById(request.getEncounterId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy cuộc gặp với ID: " + request.getEncounterId()));
        eegMapper.updateEntity(eeg, request);
        eeg.setEncounter(encounter);
        EEG updated = eegRepository.save(eeg);
        log.info("Cập nhật xét nghiệm EEG thành công với ID: {}", id);
        return eegMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void deleteEEG(Long id) {
        log.info("Xóa xét nghiệm EEG với ID: {}", id);
        EEG eeg = eegRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy xét nghiệm EEG với ID: " + id));
        eegRepository.delete(eeg);
        log.info("Xóa xét nghiệm EEG thành công với ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EEGResponse> getAllLabTestByDateAndStatus(LocalDate date, String status) {
        log.info("Truy xuất danh sách EEG theo ngày: {} và trạng thái: {}", date, status);
        try {
            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime endOfDay = date.atTime(23, 59, 59);
            EMedicalTest statusEnum = EMedicalTest.valueOf(status);
            return eegRepository.findByCreateDateBetweenAndStatus(startOfDay, endOfDay, statusEnum)
                    .stream()
                    .map(eegMapper::toResponse)
                    .toList();
        } catch (IllegalArgumentException e) {
            throw new ResourceNotFoundException("Trạng thái xét nghiệm không hợp lệ: " + status);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<EEGResponse> getEncounterIdAndDate(Long encounterId, LocalDate date) {
        log.info("Truy xuất danh sách EEG theo cuộc gặp ID: {} và ngày: {}", encounterId, date);
        LocalDate queryDate = (date != null) ? date : LocalDate.now();
        LocalDateTime startOfDay = queryDate.atStartOfDay();
        LocalDateTime endOfDay = queryDate.atTime(23, 59, 59);
        return eegRepository.findByEncounterIdAndCreateDateBetween(encounterId, startOfDay, endOfDay)
                .stream()
                .map(eegMapper::toResponse)
                .toList();
    }
}
package vn.edu.hcmute.utecare.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.utecare.dto.request.EMGRequest;
import vn.edu.hcmute.utecare.dto.response.EMGResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.exception.ResourceNotFoundException;
import vn.edu.hcmute.utecare.mapper.EMGMapper;
import vn.edu.hcmute.utecare.model.EMG;
import vn.edu.hcmute.utecare.model.Encounter;
import vn.edu.hcmute.utecare.repository.EMGRepository;
import vn.edu.hcmute.utecare.repository.EncounterRepository;
import vn.edu.hcmute.utecare.service.EMGService;
import vn.edu.hcmute.utecare.util.PaginationUtil;
import vn.edu.hcmute.utecare.util.enumeration.EMedicalTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EMGServiceImpl implements EMGService {

    private final EMGRepository emgRepository;
    private final EncounterRepository encounterRepository;
    private final EMGMapper emgMapper;

    @Override
    @Transactional
    public EMGResponse createEMG(EMGRequest request) {
        log.info("Tạo xét nghiệm EMG mới: {}", request);
        Encounter encounter = encounterRepository.findById(request.getEncounterId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy cuộc gặp với ID: " + request.getEncounterId()));
        EMG emg = emgMapper.toEntity(request);
        emg.setEncounter(encounter);
        emg.setStatus(EMedicalTest.PENDING);
        EMG saved = emgRepository.save(emg);
        log.info("Tạo xét nghiệm EMG thành công với ID: {}", saved.getId());
        return emgMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public EMGResponse getEMGById(Long id) {
        log.info("Truy xuất xét nghiệm EMG với ID: {}", id);
        EMG emg = emgRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy xét nghiệm EMG với ID: " + id));
        log.info("Truy xuất xét nghiệm EMG thành công với ID: {}", id);
        return emgMapper.toResponse(emg);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EMGResponse> getAll() {
        log.info("Truy xuất danh sách tất cả xét nghiệm EMG");
        return emgRepository.findAll().stream()
                .map(emgMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<EMGResponse> getAll(int page, int size, String sort, String direction) {
        log.info("Truy xuất danh sách EMG: trang={}, kích thước={}, sắp xếp={}, hướng={}", page, size, sort, direction);
        Pageable pageable = PaginationUtil.createPageable(page, size, sort, direction);
        Page<EMG> emgPage = emgRepository.findAll(pageable);
        return PageResponse.<EMGResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(emgPage.getTotalPages())
                .totalElements(emgPage.getTotalElements())
                .content(emgPage.getContent().stream()
                        .map(emgMapper::toResponse)
                        .toList())
                .build();
    }

    @Override
    @Transactional
    public EMGResponse updateEMG(Long id, EMGRequest request) {
        log.info("Cập nhật xét nghiệm EMG với ID: {} và thông tin: {}", id, request);
        EMG emg = emgRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy xét nghiệm EMG với ID: " + id));
        Encounter encounter = encounterRepository.findById(request.getEncounterId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy cuộc gặp với ID: " + request.getEncounterId()));
        emgMapper.updateEntity(emg, request);
        emg.setEncounter(encounter);
        EMG updated = emgRepository.save(emg);
        log.info("Cập nhật xét nghiệm EMG thành công với ID: {}", id);
        return emgMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void deleteEMG(Long id) {
        log.info("Xóa xét nghiệm EMG với ID: {}", id);
        EMG emg = emgRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy xét nghiệm EMG với ID: " + id));
        emgRepository.delete(emg);
        log.info("Xóa xét nghiệm EMG thành công với ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EMGResponse> getAllLabTestByDateAndStatus(LocalDate date, String status) {
        log.info("Truy xuất danh sách EMG theo ngày: {} và trạng thái: {}", date, status);
        try {
            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime endOfDay = date.atTime(23, 59, 59);
            EMedicalTest statusEnum = EMedicalTest.valueOf(status);
            return emgRepository.findByCreateDateBetweenAndStatus(startOfDay, endOfDay, statusEnum)
                    .stream()
                    .map(emgMapper::toResponse)
                    .toList();
        } catch (IllegalArgumentException e) {
            throw new ResourceNotFoundException("Trạng thái xét nghiệm không hợp lệ: " + status);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<EMGResponse> getEncounterIdAndDate(Long encounterId, LocalDate date) {
        log.info("Truy xuất danh sách EMG theo cuộc gặp ID: {} và ngày: {}", encounterId, date);
        LocalDate queryDate = (date != null) ? date : LocalDate.now();
        LocalDateTime startOfDay = queryDate.atStartOfDay();
        LocalDateTime endOfDay = queryDate.atTime(23, 59, 59);
        return emgRepository.findByEncounterIdAndCreateDateBetween(encounterId, startOfDay, endOfDay)
                .stream()
                .map(emgMapper::toResponse)
                .toList();
    }
}
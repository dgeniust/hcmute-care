package vn.edu.hcmute.utecare.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.utecare.dto.request.NerveConductionRequest;
import vn.edu.hcmute.utecare.dto.response.NerveConductionResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.exception.ResourceNotFoundException;
import vn.edu.hcmute.utecare.mapper.NerveConductionMapper;
import vn.edu.hcmute.utecare.model.Encounter;
import vn.edu.hcmute.utecare.model.NerveConduction;
import vn.edu.hcmute.utecare.repository.EncounterRepository;
import vn.edu.hcmute.utecare.repository.NerveConductionRepository;
import vn.edu.hcmute.utecare.service.NerveConductionService;
import vn.edu.hcmute.utecare.util.PaginationUtil;
import vn.edu.hcmute.utecare.util.enumeration.EMedicalTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NerveConductionServiceImpl implements NerveConductionService {

    private final NerveConductionRepository nerveConductionRepository;
    private final EncounterRepository encounterRepository;
    private final NerveConductionMapper nerveConductionMapper;

    @Override
    @Transactional
    public NerveConductionResponse createNerveConduction(NerveConductionRequest request) {
        log.info("Tạo xét nghiệm dẫn truyền thần kinh mới: {}", request);

        // Kiểm tra Encounter tồn tại
        Long encounterId = request.getEncounterId();
        Encounter encounter = encounterRepository.findById(encounterId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy cuộc gặp với ID: " + encounterId));

        // Chuyển đổi request sang entity
        NerveConduction nerveConduction = nerveConductionMapper.toEntity(request);
        nerveConduction.setEncounter(encounter);
        nerveConduction.setStatus(EMedicalTest.PENDING);

        NerveConduction saved = nerveConductionRepository.save(nerveConduction);
        log.info("Tạo xét nghiệm dẫn truyền thần kinh thành công với ID: {}", saved.getId());

        return nerveConductionMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public NerveConductionResponse getNerveConductionById(Long id) {
        log.info("Truy xuất xét nghiệm dẫn truyền thần kinh với ID: {}", id);
        NerveConduction nerveConduction = nerveConductionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy xét nghiệm dẫn truyền thần kinh với ID: " + id));
        return nerveConductionMapper.toResponse(nerveConduction);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NerveConductionResponse> getAll() {
        log.info("Truy xuất toàn bộ danh sách xét nghiệm dẫn truyền thần kinh");
        return nerveConductionRepository.findAll().stream()
                .map(nerveConductionMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<NerveConductionResponse> getAll(int page, int size, String sort, String direction) {
        log.info("Truy xuất danh sách xét nghiệm dẫn truyền thần kinh: trang={}, kích thước={}, sắp xếp={}, hướng={}", page, size, sort, direction);
        Pageable pageable = PaginationUtil.createPageable(page, size, sort, direction);
        Page<NerveConduction> nerveConductionPage = nerveConductionRepository.findAll(pageable);

        return PageResponse.<NerveConductionResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(nerveConductionPage.getTotalPages())
                .totalElements(nerveConductionPage.getTotalElements())
                .content(nerveConductionPage.getContent().stream()
                        .map(nerveConductionMapper::toResponse)
                        .toList())
                .build();
    }

    @Override
    @Transactional
    public NerveConductionResponse updateNerveConduction(Long id, NerveConductionRequest request) {
        log.info("Cập nhật xét nghiệm dẫn truyền thần kinh với ID: {}", id);

        NerveConduction nerveConduction = nerveConductionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy xét nghiệm dẫn truyền thần kinh với ID: " + id));

        // Kiểm tra Encounter tồn tại
        Long encounterId = request.getEncounterId();
        Encounter encounter = encounterRepository.findById(encounterId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy cuộc gặp với ID: " + encounterId));

        nerveConductionMapper.updateEntity(nerveConduction, request);
        nerveConduction.setEncounter(encounter);

        NerveConduction updated = nerveConductionRepository.save(nerveConduction);
        log.info("Cập nhật xét nghiệm dẫn truyền thần kinh thành công với ID: {}", id);

        return nerveConductionMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void deleteNerveConduction(Long id) {
        log.info("Xóa xét nghiệm dẫn truyền thần kinh với ID: {}", id);
        NerveConduction nerveConduction = nerveConductionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy xét nghiệm dẫn truyền thần kinh với ID: " + id));
        nerveConductionRepository.delete(nerveConduction);
        log.info("Xóa xét nghiệm dẫn truyền thần kinh thành công với ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NerveConductionResponse> getAllLabTestByDateAndStatus(LocalDate date, String status) {
        log.info("Truy xuất danh sách xét nghiệm dẫn truyền thần kinh theo ngày: {} và trạng thái: {}", date, status);
        try {
            EMedicalTest statusEnum = EMedicalTest.valueOf(status.toUpperCase());
            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime endOfDay = date.atTime(23, 59, 59);
            return nerveConductionRepository.findByCreateDateBetweenAndStatus(startOfDay, endOfDay, statusEnum)
                    .stream()
                    .map(nerveConductionMapper::toResponse)
                    .toList();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Trạng thái xét nghiệm không hợp lệ: " + status);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<NerveConductionResponse> getEncounterIdAndDate(Long encounterId, LocalDate date) {
        log.info("Truy xuất danh sách xét nghiệm dẫn truyền thần kinh theo cuộc gặp ID: {} và ngày: {}", encounterId, date);
        if (!encounterRepository.existsById(encounterId)) {
            throw new ResourceNotFoundException("Không tìm thấy cuộc gặp với ID: " + encounterId);
        }
        LocalDate queryDate = (date != null) ? date : LocalDate.now();
        LocalDateTime startOfDay = queryDate.atStartOfDay();
        LocalDateTime endOfDay = queryDate.atTime(23, 59, 59);
        return nerveConductionRepository.findByEncounterIdAndCreateDateBetween(encounterId, startOfDay, endOfDay)
                .stream()
                .map(nerveConductionMapper::toResponse)
                .toList();
    }
}
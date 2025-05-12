package vn.edu.hcmute.utecare.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.utecare.dto.request.SpirometryRequest;
import vn.edu.hcmute.utecare.dto.response.SpirometryResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.exception.ResourceNotFoundException;
import vn.edu.hcmute.utecare.mapper.SpirometryMapper;
import vn.edu.hcmute.utecare.model.Encounter;
import vn.edu.hcmute.utecare.model.Spirometry;
import vn.edu.hcmute.utecare.repository.EncounterRepository;
import vn.edu.hcmute.utecare.repository.SpirometryRepository;
import vn.edu.hcmute.utecare.service.SpirometryService;
import vn.edu.hcmute.utecare.util.PaginationUtil;
import vn.edu.hcmute.utecare.util.enumeration.EMedicalTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SpirometryServiceImpl implements SpirometryService {

    private final SpirometryRepository spirometryRepository;
    private final EncounterRepository encounterRepository;
    private final SpirometryMapper spirometryMapper;

    @Override
    @Transactional
    public SpirometryResponse createSpirometry(SpirometryRequest request) {
        log.info("Tạo xét nghiệm hô hấp mới: {}", request);
        Encounter encounter = encounterRepository.findById(request.getEncounterId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy cuộc gặp với ID: " + request.getEncounterId()));
        Spirometry spirometry = spirometryMapper.toEntity(request);
        spirometry.setEncounter(encounter);
        spirometry.setStatus(EMedicalTest.PENDING);
        Spirometry saved = spirometryRepository.save(spirometry);
        log.info("Tạo xét nghiệm hô hấp thành công với ID: {}", saved.getId());
        return spirometryMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public SpirometryResponse getSpirometryById(Long id) {
        log.info("Truy xuất xét nghiệm hô hấp với ID: {}", id);
        Spirometry spirometry = spirometryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy xét nghiệm hô hấp với ID: " + id));
        log.info("Truy xuất xét nghiệm hô hấp thành công với ID: {}", id);
        return spirometryMapper.toResponse(spirometry);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SpirometryResponse> getAll() {
        log.info("Truy xuất danh sách tất cả xét nghiệm hô hấp");
        return spirometryRepository.findAll().stream()
                .map(spirometryMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<SpirometryResponse> getAll(int page, int size, String sort, String direction) {
        log.info("Truy xuất danh sách xét nghiệm hô hấp: trang={}, kích thước={}, sắp xếp={}, hướng={}", page, size, sort, direction);
        Pageable pageable = PaginationUtil.createPageable(page, size, sort, direction);
        Page<Spirometry> spirometryPage = spirometryRepository.findAll(pageable);
        return PageResponse.<SpirometryResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(spirometryPage.getTotalPages())
                .totalElements(spirometryPage.getTotalElements())
                .content(spirometryPage.getContent().stream()
                        .map(spirometryMapper::toResponse)
                        .toList())
                .build();
    }

    @Override
    @Transactional
    public SpirometryResponse updateSpirometry(Long id, SpirometryRequest request) {
        log.info("Cập nhật xét nghiệm hô hấp với ID: {} và thông tin: {}", id, request);
        Spirometry spirometry = spirometryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy xét nghiệm hô hấp với ID: " + id));
        Encounter encounter = encounterRepository.findById(request.getEncounterId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy cuộc gặp với ID: " + request.getEncounterId()));
        spirometryMapper.updateEntity(spirometry, request);
        spirometry.setEncounter(encounter);
        Spirometry updated = spirometryRepository.save(spirometry);
        log.info("Cập nhật xét nghiệm hô hấp thành công với ID: {}", id);
        return spirometryMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void deleteSpirometry(Long id) {
        log.info("Xóa xét nghiệm hô hấp với ID: {}", id);
        Spirometry spirometry = spirometryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy xét nghiệm hô hấp với ID: " + id));
        spirometryRepository.delete(spirometry);
        log.info("Xóa xét nghiệm hô hấp thành công với ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SpirometryResponse> getAllLabTestByDateAndStatus(LocalDate date, String status) {
        log.info("Truy xuất danh sách xét nghiệm hô hấp theo ngày: {} và trạng thái: {}", date, status);
        try {
            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime endOfDay = date.atTime(23, 59, 59);
            EMedicalTest statusEnum = EMedicalTest.valueOf(status);
            return spirometryRepository.findByCreateDateBetweenAndStatus(startOfDay, endOfDay, statusEnum)
                    .stream()
                    .map(spirometryMapper::toResponse)
                    .toList();
        } catch (IllegalArgumentException e) {
            throw new ResourceNotFoundException("Trạng thái xét nghiệm không hợp lệ: " + status);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<SpirometryResponse> getEncounterIdAndDate(Long encounterId, LocalDate date) {
        log.info("Truy xuất danh sách xét nghiệm hô hấp theo cuộc gặp ID: {} và ngày: {}", encounterId, date);
        LocalDate queryDate = (date != null) ? date : LocalDate.now();
        LocalDateTime startOfDay = queryDate.atStartOfDay();
        LocalDateTime endOfDay = queryDate.atTime(23, 59, 59);
        return spirometryRepository.findByEncounterIdAndCreateDateBetween(encounterId, startOfDay, endOfDay)
                .stream()
                .map(spirometryMapper::toResponse)
                .toList();
    }
}
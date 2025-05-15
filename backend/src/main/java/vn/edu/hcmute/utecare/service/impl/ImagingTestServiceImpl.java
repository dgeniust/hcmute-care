package vn.edu.hcmute.utecare.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.utecare.dto.request.ImagingTestRequest;
import vn.edu.hcmute.utecare.dto.response.ImagingTestResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.exception.ResourceNotFoundException;
import vn.edu.hcmute.utecare.mapper.ImagingTestMapper;
import vn.edu.hcmute.utecare.model.Encounter;
import vn.edu.hcmute.utecare.model.ImagingTest;
import vn.edu.hcmute.utecare.repository.EncounterRepository;
import vn.edu.hcmute.utecare.repository.ImagingTestRepository;
import vn.edu.hcmute.utecare.service.ImagingTestService;
import vn.edu.hcmute.utecare.util.PaginationUtil;
import vn.edu.hcmute.utecare.util.enumeration.EMedicalTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImagingTestServiceImpl implements ImagingTestService {

    private final ImagingTestRepository imagingTestRepository;
    private final EncounterRepository encounterRepository;
    private final ImagingTestMapper imagingTestMapper;

    @Override
    @Transactional
    public ImagingTestResponse createImagingTest(ImagingTestRequest request) {
        log.info("Tạo xét nghiệm hình ảnh mới: {}", request);
        Encounter encounter = encounterRepository.findById(request.getEncounterId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy cuộc gặp với ID: " + request.getEncounterId()));
        ImagingTest imagingTest = imagingTestMapper.toEntity(request);
        imagingTest.setEncounter(encounter);
        imagingTest.setStatus(EMedicalTest.PENDING);
        ImagingTest saved = imagingTestRepository.save(imagingTest);
        log.info("Tạo xét nghiệm hình ảnh thành công với ID: {}", saved.getId());
        return imagingTestMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ImagingTestResponse getImagingTestById(Long id) {
        log.info("Truy xuất xét nghiệm hình ảnh với ID: {}", id);
        ImagingTest imagingTest = imagingTestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy xét nghiệm hình ảnh với ID: " + id));
        log.info("Truy xuất xét nghiệm hình ảnh thành công với ID: {}", id);
        return imagingTestMapper.toResponse(imagingTest);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ImagingTestResponse> getAll() {
        log.info("Truy xuất danh sách tất cả xét nghiệm hình ảnh");
        return imagingTestRepository.findAll().stream()
                .map(imagingTestMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ImagingTestResponse> getAll(int page, int size, String sort, String direction) {
        log.info("Truy xuất danh sách xét nghiệm hình ảnh: trang={}, kích thước={}, sắp xếp={}, hướng={}", page, size, sort, direction);
        Pageable pageable = PaginationUtil.createPageable(page, size, sort, direction);
        Page<ImagingTest> imagingTestPage = imagingTestRepository.findAll(pageable);
        return PageResponse.<ImagingTestResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(imagingTestPage.getTotalPages())
                .totalElements(imagingTestPage.getTotalElements())
                .content(imagingTestPage.getContent().stream()
                        .map(imagingTestMapper::toResponse)
                        .toList())
                .build();
    }

    @Override
    @Transactional
    public ImagingTestResponse updateImagingTest(Long id, ImagingTestRequest request) {
        log.info("Cập nhật xét nghiệm hình ảnh với ID: {} và thông tin: {}", id, request);
        ImagingTest imagingTest = imagingTestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy xét nghiệm hình ảnh với ID: " + id));
        Encounter encounter = encounterRepository.findById(request.getEncounterId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy cuộc gặp với ID: " + request.getEncounterId()));
        imagingTestMapper.updateEntity(imagingTest, request);
        imagingTest.setEncounter(encounter);
        ImagingTest updated = imagingTestRepository.save(imagingTest);
        log.info("Cập nhật xét nghiệm hình ảnh thành công với ID: {}", id);
        return imagingTestMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void deleteImagingTest(Long id) {
        log.info("Xóa xét nghiệm hình ảnh với ID: {}", id);
        ImagingTest imagingTest = imagingTestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy xét nghiệm hình ảnh với ID: " + id));
        imagingTestRepository.delete(imagingTest);
        log.info("Xóa xét nghiệm hình ảnh thành công với ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ImagingTestResponse> getAllImagingTestByDateAndStatus(LocalDate date, String status) {
        log.info("Truy xuất danh sách xét nghiệm hình ảnh theo ngày: {} và trạng thái: {}", date, status);
        try {
            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime endOfDay = date.atTime(23, 59, 59);
            EMedicalTest statusEnum = EMedicalTest.valueOf(status);
            return imagingTestRepository.findByCreateDateBetweenAndStatus(startOfDay, endOfDay, statusEnum)
                    .stream()
                    .map(imagingTestMapper::toResponse)
                    .toList();
        } catch (IllegalArgumentException e) {
            throw new ResourceNotFoundException("Trạng thái xét nghiệm không hợp lệ: " + status);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ImagingTestResponse> getEncounterIdAndDate(Long encounterId, LocalDate date) {
        log.info("Truy xuất danh sách xét nghiệm hình ảnh theo cuộc gặp ID: {} và ngày: {}", encounterId, date);
        LocalDate queryDate = (date != null) ? date : LocalDate.now();
        LocalDateTime startOfDay = queryDate.atStartOfDay();
        LocalDateTime endOfDay = queryDate.atTime(23, 59, 59);
        return imagingTestRepository.findByEncounterIdAndCreateDateBetween(encounterId, startOfDay, endOfDay)
                .stream()
                .map(imagingTestMapper::toResponse)
                .toList();
    }
}
package vn.edu.hcmute.utecare.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.edu.hcmute.utecare.dto.request.LaboratoryTestsRequest;
import vn.edu.hcmute.utecare.dto.response.LaboratoryTestsResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.exception.NotFoundException;
import vn.edu.hcmute.utecare.mapper.LaboratoryTestsMapper;
import vn.edu.hcmute.utecare.model.LaboratoryTests;
import vn.edu.hcmute.utecare.repository.LaboratoryTestsRepository;
import vn.edu.hcmute.utecare.service.LaboratoryTestsService;
import vn.edu.hcmute.utecare.util.PaginationUtil;
import vn.edu.hcmute.utecare.util.enumeration.EMedicalTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class LaboratoryTestsServiceImpl implements LaboratoryTestsService {

    private final LaboratoryTestsRepository laboratoryTestsRepository;

    @Override
    public LaboratoryTestsResponse createLaboratoryTests(LaboratoryTestsRequest request) {
        log.info("Tạo LaboratoryTests mới: {}", request);
        LaboratoryTests laboratoryTests = LaboratoryTestsMapper.INSTANCE.toEntity(request);
        laboratoryTests.setStatus(EMedicalTest.PENDING);
        LaboratoryTests savedLaboratoryTests = laboratoryTestsRepository.save(laboratoryTests);
        return LaboratoryTestsMapper.INSTANCE.toResponse(savedLaboratoryTests);
    }

    @Override
    public LaboratoryTestsResponse getLaboratoryTestsById(Long id) {
        log.info("Lấy thông tin LaboratoryTests với id: {}", id);
        LaboratoryTests laboratoryTests = laboratoryTestsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy LaboratoryTests với id: " + id));
        return LaboratoryTestsMapper.INSTANCE.toResponse(laboratoryTests);
    }

    @Override
    public List<LaboratoryTestsResponse> getAll() {
        log.info("Lấy danh sách tất cả LaboratoryTests");
        return laboratoryTestsRepository.findAll().stream()
                .map(LaboratoryTestsMapper.INSTANCE::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PageResponse<LaboratoryTestsResponse> getAll(int page, int size, String sort, String direction) {
        log.info("Lấy danh sách LaboratoryTests với phân trang: page={}, size={}, sort={}, direction={}", page, size, sort, direction);
        Pageable pageable = PaginationUtil.createPageable(page, size, sort, direction);
        Page<LaboratoryTests> laboratoryTestsPage = laboratoryTestsRepository.findAll(pageable);

        return PageResponse.<LaboratoryTestsResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(laboratoryTestsPage.getTotalPages())
                .totalElements(laboratoryTestsPage.getTotalElements())
                .content(laboratoryTestsPage.getContent().stream()
                        .map(LaboratoryTestsMapper.INSTANCE::toResponse)
                        .toList())
                .build();
    }

    @Override
    public LaboratoryTestsResponse updateLaboratoryTests(Long id, LaboratoryTestsRequest request) {
        log.info("Cập nhật LaboratoryTests với id: {}", id);
        LaboratoryTests laboratoryTests = laboratoryTestsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy LaboratoryTests với id: " + id));
        LaboratoryTestsMapper.INSTANCE.updateEntity(laboratoryTests, request);
        laboratoryTestsRepository.save(laboratoryTests);
        return LaboratoryTestsMapper.INSTANCE.toResponse(laboratoryTests);
    }

    @Override
    public void deleteLaboratoryTests(Long id) {
        log.info("Xóa LaboratoryTests với id: {}", id);
        LaboratoryTests laboratoryTests = laboratoryTestsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy LaboratoryTests với id: " + id));
        laboratoryTestsRepository.delete(laboratoryTests);
    }

    @Override
    public List<LaboratoryTestsResponse> getAllLabTestByDateAndStatus(LocalDate date, String status) {
        log.info("Lấy danh sách LaboratoryTests theo ngày {} và trạng thái PENDING", date);
        LocalDateTime startOfDay = date.atStartOfDay(); // 00:00:00
        LocalDateTime endOfDay = date.atTime(23, 59, 59); // 23:59:59
        EMedicalTest statusEnum = EMedicalTest.valueOf(String.valueOf(status)); // statusString là "PENDING", "COMPLETED",...

        return laboratoryTestsRepository.findByCreateDateBetweenAndStatus(startOfDay, endOfDay, statusEnum)
                .stream()
                .map(LaboratoryTestsMapper.INSTANCE::toResponse).toList();
    }
}
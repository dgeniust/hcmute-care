package vn.edu.hcmute.utecare.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.edu.hcmute.utecare.dto.request.MedicalTestRequest;
import vn.edu.hcmute.utecare.dto.response.MedicalTestResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.exception.NotFoundException;
import vn.edu.hcmute.utecare.mapper.MedicalTestMapper;
import vn.edu.hcmute.utecare.model.MedicalTest;
import vn.edu.hcmute.utecare.repository.MedicalTestRepository;
import vn.edu.hcmute.utecare.service.MedicalTestService;
import vn.edu.hcmute.utecare.util.PaginationUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MedicalTestServiceImpl implements MedicalTestService {

    private final MedicalTestRepository medicalTestRepository;

    @Override
    public MedicalTestResponse createMedicalTest(MedicalTestRequest request) {
        throw new UnsupportedOperationException("Không thể tạo MedicalTest trừu tượng. Sử dụng dịch vụ của lớp con cụ thể.");
    }

    @Override
    public MedicalTestResponse getMedicalTestById(Long id) {
        log.info("Lấy thông tin MedicalTest với id: {}", id);
        MedicalTest medicalTest = medicalTestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy MedicalTest với id: " + id));
        return MedicalTestMapper.INSTANCE.toResponse(medicalTest);
    }

    @Override
    public List<MedicalTestResponse> getAll() {
        log.info("Lấy danh sách tất cả MedicalTest");
        return medicalTestRepository.findAll().stream()
                .map(MedicalTestMapper.INSTANCE::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PageResponse<MedicalTestResponse> getAll(int page, int size, String sort, String direction) {
        log.info("Lấy danh sách MedicalTest với phân trang: page={}, size={}, sort={}, direction={}", page, size, sort, direction);
        Pageable pageable = PaginationUtil.createPageable(page, size, sort, direction);
        Page<MedicalTest> medicalTestPage = medicalTestRepository.findAll(pageable);

        return PageResponse.<MedicalTestResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(medicalTestPage.getTotalPages())
                .totalElements(medicalTestPage.getTotalElements())
                .content(medicalTestPage.getContent().stream()
                        .map(MedicalTestMapper.INSTANCE::toResponse)
                        .toList())
                .build();
    }

    @Override
    public MedicalTestResponse updateMedicalTest(Long id, MedicalTestRequest request) {
        log.info("Cập nhật MedicalTest với id: {}", id);
        MedicalTest medicalTest = medicalTestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy MedicalTest với id: " + id));
        MedicalTestMapper.INSTANCE.updateEntity(medicalTest, request);
        medicalTestRepository.save(medicalTest);
        return MedicalTestMapper.INSTANCE.toResponse(medicalTest);
    }

    @Override
    public void deleteMedicalTest(Long id) {
        log.info("Xóa MedicalTest với id: {}", id);
        MedicalTest medicalTest = medicalTestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy MedicalTest với id: " + id));
        medicalTestRepository.delete(medicalTest);
    }

    @Override
    public List<MedicalTestResponse> findByEncounterAndDate(Long encounterId, LocalDate date) {
        log.info("Tìm MedicalTest theo encounterId: {} và ngày: {}", encounterId, date);

        LocalDateTime startOfDay = date.atStartOfDay(); // 00:00:00
        LocalDateTime endOfDay = date.atTime(23, 59, 59); // 23:59:59

        List<MedicalTest> tests = medicalTestRepository.findByEncounter_IdAndCreateDateBetween(encounterId, startOfDay, endOfDay);
        return tests.stream()
                .map(MedicalTestMapper.INSTANCE::toResponse)
                .toList();
    }
}
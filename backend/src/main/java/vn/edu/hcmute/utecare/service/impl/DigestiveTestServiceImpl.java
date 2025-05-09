package vn.edu.hcmute.utecare.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.edu.hcmute.utecare.dto.request.DigestiveTestRequest;
import vn.edu.hcmute.utecare.dto.response.DigestiveTestResponse;
import vn.edu.hcmute.utecare.exception.NotFoundException;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DigestiveTestServiceImpl implements DigestiveTestService {

    private final DigestiveTestRepository digestiveTestRepository;
    private final EncounterRepository encounterRepository;


    @Override
    public DigestiveTestResponse createDigestiveTest(DigestiveTestRequest request) {
        log.info("Tạo DigestiveTest mới: {}", request);
        Encounter encounter = encounterRepository.findById(request.getEncounterId())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy Encounter với id: " + request.getEncounterId()));
        DigestiveTest digestiveTest = DigestiveTestMapper.INSTANCE.toEntity(request);
        digestiveTest.setEncounter(encounter);
        digestiveTest.setStatus(EMedicalTest.PENDING);
        DigestiveTest savedDigestiveTest = digestiveTestRepository.save(digestiveTest);
        return DigestiveTestMapper.INSTANCE.toResponse(savedDigestiveTest);
    }

    @Override
    public DigestiveTestResponse getDigestiveTestById(Long id) {
        log.info("Lấy thông tin DigestiveTest với id: {}", id);
        DigestiveTest digestiveTest = digestiveTestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy DigestiveTest với id: " + id));
        return DigestiveTestMapper.INSTANCE.toResponse(digestiveTest);
    }

    @Override
    public List<DigestiveTestResponse> getAll() {
        log.info("Lấy danh sách tất cả DigestiveTest");
        return digestiveTestRepository.findAll().stream()
                .map(DigestiveTestMapper.INSTANCE::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PageResponse<DigestiveTestResponse> getAll(int page, int size, String sort, String direction) {
        log.info("Lấy danh sách DigestiveTest với phân trang: page={}, size={}, sort={}, direction={}", page, size, sort, direction);
        Pageable pageable = PaginationUtil.createPageable(page, size, sort, direction);
        Page<DigestiveTest> digestiveTestPage = digestiveTestRepository.findAll(pageable);

        return PageResponse.<DigestiveTestResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(digestiveTestPage.getTotalPages())
                .totalElements(digestiveTestPage.getTotalElements())
                .content(digestiveTestPage.getContent().stream()
                        .map(DigestiveTestMapper.INSTANCE::toResponse)
                        .toList())
                .build();
    }

    @Override
    public DigestiveTestResponse updateDigestiveTest(Long id, DigestiveTestRequest request) {
        log.info("Cập nhật DigestiveTest với id: {}", id);
        DigestiveTest digestiveTest = digestiveTestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy DigestiveTest với id: " + id));
        Encounter encounter = encounterRepository.findById(request.getEncounterId())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy Encounter với id: " + request.getEncounterId()));
        DigestiveTestMapper.INSTANCE.updateEntity(digestiveTest, request);
        digestiveTest.setEncounter(encounter);
        digestiveTestRepository.save(digestiveTest);
        return DigestiveTestMapper.INSTANCE.toResponse(digestiveTest);
    }

    @Override
    public void deleteDigestiveTest(Long id) {
        log.info("Xóa DigestiveTest với id: {}", id);
        DigestiveTest digestiveTest = digestiveTestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy DigestiveTest với id: " + id));
        digestiveTestRepository.delete(digestiveTest);
    }

    @Override
    public List<DigestiveTestResponse> getAllLabTestByDateAndStatus(LocalDate date, String status) {
        log.info("Lấy danh sách DigestiveTest theo ngày {} và trạng thái PENDING", date);
        LocalDateTime startOfDay = date.atStartOfDay(); // 00:00:00
        LocalDateTime endOfDay = date.atTime(23, 59, 59); // 23:59:59
        EMedicalTest statusEnum = EMedicalTest.valueOf(String.valueOf(status)); // statusString là "PENDING", "COMPLETED",...
        return digestiveTestRepository.findByCreateDateBetweenAndStatus(startOfDay, endOfDay, statusEnum)
                .stream()
                .map(DigestiveTestMapper.INSTANCE::toResponse).toList();
    }

    @Override
    public List<DigestiveTestResponse> getEncounterIdAndDate(Long encounterId, LocalDate date) {
        log.info("Lấy danh sách DigestiveTest theo encounterId {} và ngày {}", encounterId, date);
        LocalDate queryDate = (date != null) ? date : LocalDate.now();
        LocalDateTime startOfDay = queryDate.atStartOfDay(); // 00:00:00
        LocalDateTime endOfDay = queryDate.atTime(23, 59, 59); // 23:59:59
        return digestiveTestRepository.findByEncounterIdAndCreateDateBetween(encounterId, startOfDay, endOfDay)
                .stream()
                .map(DigestiveTestMapper.INSTANCE::toResponse)
                .toList();
    }


}
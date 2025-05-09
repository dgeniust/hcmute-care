package vn.edu.hcmute.utecare.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.edu.hcmute.utecare.dto.request.ImagingTestRequest;
import vn.edu.hcmute.utecare.dto.response.ImagingTestResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.exception.NotFoundException;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImagingTestServiceImpl implements ImagingTestService {

    private final ImagingTestRepository imagingTestRepository;
    private final EncounterRepository encounterRepository;

    @Override
    public ImagingTestResponse createImagingTest(ImagingTestRequest request) {
        log.info("Tạo ImagingTest mới: {}", request);
        Encounter encounter = encounterRepository.findById(request.getEncounterId())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy Encounter với id: " + request.getEncounterId()));
        ImagingTest imagingTest = ImagingTestMapper.INSTANCE.toEntity(request);
        imagingTest.setEncounter(encounter);
        imagingTest.setStatus(EMedicalTest.PENDING);
        ImagingTest savedImagingTest = imagingTestRepository.save(imagingTest);
        return ImagingTestMapper.INSTANCE.toResponse(savedImagingTest);
    }

    @Override
    public ImagingTestResponse getImagingTestById(Long id) {
        log.info("Lấy thông tin ImagingTest với id: {}", id);
        ImagingTest imagingTest = imagingTestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy ImagingTest với id: " + id));
        return ImagingTestMapper.INSTANCE.toResponse(imagingTest);
    }

    @Override
    public List<ImagingTestResponse> getAll() {
        log.info("Lấy danh sách tất cả ImagingTest");
        return imagingTestRepository.findAll().stream()
                .map(ImagingTestMapper.INSTANCE::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PageResponse<ImagingTestResponse> getAll(int page, int size, String sort, String direction) {
        log.info("Lấy danh sách ImagingTest với phân trang: page={}, size={}, sort={}, direction={}", page, size, sort, direction);
        Pageable pageable = PaginationUtil.createPageable(page, size, sort, direction);
        Page<ImagingTest> imagingTestPage = imagingTestRepository.findAll(pageable);

        return PageResponse.<ImagingTestResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(imagingTestPage.getTotalPages())
                .totalElements(imagingTestPage.getTotalElements())
                .content(imagingTestPage.getContent().stream()
                        .map(ImagingTestMapper.INSTANCE::toResponse)
                        .toList())
                .build();
    }

    @Override
    public ImagingTestResponse updateImagingTest(Long id, ImagingTestRequest request) {
        log.info("Cập nhật ImagingTest với id: {}", id);
        ImagingTest imagingTest = imagingTestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy ImagingTest với id: " + id));
        Encounter encounter = encounterRepository.findById(request.getEncounterId())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy Encounter với id: " + request.getEncounterId()));
        ImagingTestMapper.INSTANCE.updateEntity(imagingTest, request);
        imagingTest.setEncounter(encounter);
        imagingTestRepository.save(imagingTest);
        return ImagingTestMapper.INSTANCE.toResponse(imagingTest);
    }

    @Override
    public void deleteImagingTest(Long id) {
        log.info("Xóa ImagingTest với id: {}", id);
        ImagingTest imagingTest = imagingTestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy ImagingTest với id: " + id));
        imagingTestRepository.delete(imagingTest);
    }

    @Override
    public List<ImagingTestResponse> getAllImagingTestByDateAndStatus(LocalDate date, String status) {
        log.info("Lấy danh sách ImagingTest theo ngày {} và trạng thái PENDING", date);
        LocalDateTime startOfDay = date.atStartOfDay(); // 00:00:00
        LocalDateTime endOfDay = date.atTime(23, 59, 59); // 23:59:59
        EMedicalTest statusEnum = EMedicalTest.valueOf(String.valueOf(status)); // statusString là "PENDING", "COMPLETED",...
        return imagingTestRepository.findByCreateDateBetweenAndStatus(startOfDay, endOfDay, statusEnum)
                .stream()
                .map(ImagingTestMapper.INSTANCE::toResponse).toList();
    }

    @Override
    public List<ImagingTestResponse> getEncounterIdAndDate(Long encounterId, LocalDate date) {
        log.info("Lấy danh sách ImagingTest theo encounterId {} và ngày {}", encounterId, date);
        LocalDate queryDate = (date != null) ? date : LocalDate.now();
        LocalDateTime startOfDay = queryDate.atStartOfDay(); // 00:00:00
        LocalDateTime endOfDay = queryDate.atTime(23, 59, 59); // 23:59:59
        return imagingTestRepository.findByEncounterIdAndCreateDateBetween(encounterId, startOfDay, endOfDay)
                .stream()
                .map(ImagingTestMapper.INSTANCE::toResponse)
                .toList();
    }


}
package vn.edu.hcmute.utecare.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.edu.hcmute.utecare.dto.request.EMGRequest;
import vn.edu.hcmute.utecare.dto.response.EMGResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.exception.NotFoundException;
import vn.edu.hcmute.utecare.mapper.EMGMapper;
import vn.edu.hcmute.utecare.model.EMG;
import vn.edu.hcmute.utecare.model.Encounter;
import vn.edu.hcmute.utecare.repository.EMGRepository;
import vn.edu.hcmute.utecare.service.EMGService;
import vn.edu.hcmute.utecare.util.PaginationUtil;
import vn.edu.hcmute.utecare.util.enumeration.EMedicalTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EMGServiceImpl implements EMGService {

    private final EMGRepository emgRepository;

    @Override
    public EMGResponse createEMG(EMGRequest request) {
        log.info("Tạo EMG mới: {}", request);

        EMG emg = EMGMapper.INSTANCE.toEntity(request);

        Encounter encounter = new Encounter();
        encounter.setId(request.getEncounterId());
        emg.setEncounter(encounter);
        emg.setEvaluate(request.getEvaluate());
        emg.setNotes(request.getNotes());
        emg.setTestName(request.getTestName());
        emg.setOrganSystem(request.getOrganSystem());
        emg.setIsInvasive(request.getIsInvasive());
        emg.setIsQuantitative(request.getIsQuantitative());
        emg.setRecordDuration(request.getRecordDuration());
        emg.setImage(request.getImage());

        emg.setMuscleGroup(request.getMuscleGroup());
        emg.setStatus(EMedicalTest.PENDING);
        EMG saved = emgRepository.save(emg);

        return EMGMapper.INSTANCE.toResponse(saved);
    }

    @Override
    public EMGResponse getEMGById(Long id) {
        log.info("Lấy thông tin EMG với id: {}", id);
        EMG emg = emgRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy EMG với id: " + id));
        return EMGMapper.INSTANCE.toResponse(emg);
    }

    @Override
    public List<EMGResponse> getAll() {
        log.info("Lấy danh sách tất cả EMG");
        return emgRepository.findAll().stream()
                .map(EMGMapper.INSTANCE::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PageResponse<EMGResponse> getAll(int page, int size, String sort, String direction) {
        log.info("Lấy danh sách EMG với phân trang: page={}, size={}, sort={}, direction={}", page, size, sort, direction);
        Pageable pageable = PaginationUtil.createPageable(page, size, sort, direction);
        Page<EMG> emgPage = emgRepository.findAll(pageable);

        return PageResponse.<EMGResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(emgPage.getTotalPages())
                .totalElements(emgPage.getTotalElements())
                .content(emgPage.getContent().stream()
                        .map(EMGMapper.INSTANCE::toResponse)
                        .toList())
                .build();
    }

    @Override
    public EMGResponse updateEMG(Long id, EMGRequest request) {
        log.info("Cập nhật EMG với id: {}", id);

        EMG emg = emgRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy EMG với id: " + id));

        EMGMapper.INSTANCE.updateEntity(emg, request);

        Encounter encounter = new Encounter();
        encounter.setId(request.getEncounterId());
        emg.setEncounter(encounter);

        emgRepository.save(emg);
        return EMGMapper.INSTANCE.toResponse(emg);
    }

    @Override
    public void deleteEMG(Long id) {
        log.info("Xóa EMG với id: {}", id);
        EMG emg = emgRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy EMG với id: " + id));
        emgRepository.delete(emg);
    }

    @Override
    public List<EMGResponse> getAllLabTestByDateAndStatus(LocalDate date, String status) {
        log.info("Lấy danh sách EMG theo ngày {} và trạng thái PENDING", date);
        LocalDateTime startOfDay = date.atStartOfDay(); // 00:00:00
        LocalDateTime endOfDay = date.atTime(23, 59, 59); // 23:59:59
        EMedicalTest statusEnum = EMedicalTest.valueOf(String.valueOf(status)); // statusString là "PENDING", "COMPLETED",...
        return emgRepository.findByCreateDateBetweenAndStatus(startOfDay, endOfDay, statusEnum)
                .stream()
                .map(EMGMapper.INSTANCE::toResponse).toList();
    }

    @Override
    public List<EMGResponse> getEncounterIdAndDate(Long encounterId, LocalDate date) {
        log.info("Lấy danh sách EMG theo encounterId {} và ngày {}", encounterId, date);
        LocalDate queryDate = (date != null) ? date : LocalDate.now();
        LocalDateTime startOfDay = queryDate.atStartOfDay(); // 00:00:00
        LocalDateTime endOfDay = queryDate.atTime(23, 59, 59); // 23:59:59
        return emgRepository.findByEncounterIdAndCreateDateBetween(encounterId, startOfDay, endOfDay)
                .stream()
                .map(EMGMapper.INSTANCE::toResponse)
                .toList();
    }


}

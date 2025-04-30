package vn.edu.hcmute.utecare.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.edu.hcmute.utecare.dto.request.EEGRequest;
import vn.edu.hcmute.utecare.dto.response.EEGResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.exception.NotFoundException;
import vn.edu.hcmute.utecare.mapper.EEGMapper;
import vn.edu.hcmute.utecare.model.EEG;
import vn.edu.hcmute.utecare.model.Encounter;
import vn.edu.hcmute.utecare.repository.EEGRepository;
import vn.edu.hcmute.utecare.service.EEGService;
import vn.edu.hcmute.utecare.util.PaginationUtil;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EEGServiceImpl implements EEGService {

    private final EEGRepository eegRepository;

    @Override
    public EEGResponse createEEG(EEGRequest request) {
        log.info("Tạo EEG mới: {}", request);

        EEG eeg = EEGMapper.INSTANCE.toEntity(request);

        Encounter encounter = new Encounter();
        encounter.setId(request.getEncounterId());
        eeg.setEncounter(encounter);
        eeg.setEvaluate(null);
        eeg.setNotes(null);
        eeg.setTestName(null);
        eeg.setOrganSystem(null);
        eeg.setIsInvasive(null);
        eeg.setIsQuantitative(null);
        eeg.setRecordDuration(null);
        eeg.setChannels(null);
        eeg.setImage(null);
        eeg.setDetectSeizure(false);


        EEG saved = eegRepository.save(eeg);

        return EEGMapper.INSTANCE.toResponse(saved);
    }

    @Override
    public EEGResponse getEEGById(Long id) {
        log.info("Lấy thông tin EEG với id: {}", id);
        EEG eeg = eegRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy EEG với id: " + id));
        return EEGMapper.INSTANCE.toResponse(eeg);
    }

    @Override
    public List<EEGResponse> getAll() {
        log.info("Lấy danh sách tất cả EEG");
        return eegRepository.findAll().stream()
                .map(EEGMapper.INSTANCE::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PageResponse<EEGResponse> getAll(int page, int size, String sort, String direction) {
        log.info("Lấy danh sách EEG với phân trang: page={}, size={}, sort={}, direction={}", page, size, sort, direction);
        Pageable pageable = PaginationUtil.createPageable(page, size, sort, direction);
        Page<EEG> eegPage = eegRepository.findAll(pageable);

        return PageResponse.<EEGResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(eegPage.getTotalPages())
                .totalElements(eegPage.getTotalElements())
                .content(eegPage.getContent().stream()
                        .map(EEGMapper.INSTANCE::toResponse)
                        .toList())
                .build();
    }

    @Override
    public EEGResponse updateEEG(Long id, EEGRequest request) {
        log.info("Cập nhật EEG với id: {}", id);

        EEG eeg = eegRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy EEG với id: " + id));

        EEGMapper.INSTANCE.updateEntity(eeg, request);

        Encounter encounter = new Encounter();
        encounter.setId(request.getEncounterId());
        eeg.setEncounter(encounter);

        eegRepository.save(eeg);
        return EEGMapper.INSTANCE.toResponse(eeg);
    }

    @Override
    public void deleteEEG(Long id) {
        log.info("Xóa EEG với id: {}", id);
        EEG eeg = eegRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy EEG với id: " + id));
        eegRepository.delete(eeg);
    }
}

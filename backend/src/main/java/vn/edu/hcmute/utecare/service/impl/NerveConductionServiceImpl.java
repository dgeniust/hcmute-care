package vn.edu.hcmute.utecare.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.edu.hcmute.utecare.dto.request.NerveConductionRequest;
import vn.edu.hcmute.utecare.dto.response.NerveConductionResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.exception.NotFoundException;
import vn.edu.hcmute.utecare.mapper.NerveConductionMapper;
import vn.edu.hcmute.utecare.model.*;
import vn.edu.hcmute.utecare.repository.NerveConductionRepository;
import vn.edu.hcmute.utecare.service.NerveConductionService;
import vn.edu.hcmute.utecare.util.PaginationUtil;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NerveConductionServiceImpl implements NerveConductionService {

    private final NerveConductionRepository nerveConductionRepository;
    private final NerveConductionMapper nerveConductionMapper;

    @Override
    public NerveConductionResponse createNerveConduction(NerveConductionRequest request) {
        log.info("Tạo NerveConduction mới: {}", request);

        // Chuyển đổi request sang entity
        NerveConduction nerveConduction = nerveConductionMapper.toEntity(request);

        // Thiết lập thông tin Encounter
        Encounter encounter = new Encounter();
        encounter.setId(request.getEncounterId());
        nerveConduction.setEncounter(encounter);

        nerveConduction.setEvaluate(request.getEvaluate());
        nerveConduction.setNotes(request.getNotes());
        nerveConduction.setTestName(request.getTestName());
        nerveConduction.setOrganSystem(request.getOrganSystem());
        nerveConduction.setIsInvasive(request.getIsInvasive());
        nerveConduction.setIsQuantitative(request.getIsQuantitative());
        nerveConduction.setRecordDuration(request.getRecordDuration());
        nerveConduction.setTestEnvironment(request.getTestEnvironment());
        nerveConduction.setPatientPosition(request.getPatientPosition());
        nerveConduction.setNerve(request.getNerve());
        nerveConduction.setConductionSpeed(request.getConductionSpeed());

        // Lưu entity vào cơ sở dữ liệu
        NerveConduction saved = nerveConductionRepository.save(nerveConduction);

        // Chuyển entity đã lưu sang DTO để trả về
        return nerveConductionMapper.toResponse(saved);
    }

    @Override
    public NerveConductionResponse getNerveConductionById(Long id) {
        log.info("Lấy thông tin NerveConduction với id: {}", id);
        NerveConduction nerveConduction = nerveConductionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy NerveConduction với id: " + id));
        return nerveConductionMapper.toResponse(nerveConduction);
    }

    @Override
    public List<NerveConductionResponse> getAll() {
        log.info("Lấy danh sách tất cả NerveConduction");
        return nerveConductionRepository.findAll().stream()
                .map(nerveConductionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PageResponse<NerveConductionResponse> getAll(int page, int size, String sort, String direction) {
        log.info("Lấy danh sách NerveConduction với phân trang: page={}, size={}, sort={}, direction={}", page, size, sort, direction);
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
    public NerveConductionResponse updateNerveConduction(Long id, NerveConductionRequest request) {
        log.info("Cập nhật NerveConduction với id: {}", id);

        NerveConduction nerveConduction = nerveConductionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy NerveConduction với id: " + id));

        nerveConductionMapper.updateEntity(nerveConduction, request);

        Encounter encounter = new Encounter();
        encounter.setId(request.getEncounterId());
        nerveConduction.setEncounter(encounter);

        nerveConductionRepository.save(nerveConduction);
        return nerveConductionMapper.toResponse(nerveConduction);
    }

    @Override
    public void deleteNerveConduction(Long id) {
        log.info("Xóa NerveConduction với id: {}", id);
        NerveConduction nerveConduction = nerveConductionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy NerveConduction với id: " + id));
        nerveConductionRepository.delete(nerveConduction);
    }
}

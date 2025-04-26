package vn.edu.hcmute.utecare.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.edu.hcmute.utecare.dto.request.BloodGasAnalysisRequest;
import vn.edu.hcmute.utecare.dto.response.BloodGasAnalysisResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.exception.NotFoundException;
import vn.edu.hcmute.utecare.mapper.BloodGasAnalysisMapper;
import vn.edu.hcmute.utecare.model.*;
import vn.edu.hcmute.utecare.repository.BloodGasAnalysisRepository;
import vn.edu.hcmute.utecare.service.BloodGasAnalysisService;
import vn.edu.hcmute.utecare.util.PaginationUtil;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BloodGasAnalysisServiceImpl implements BloodGasAnalysisService {

    private final BloodGasAnalysisRepository bloodGasAnalysisRepository;
    private final BloodGasAnalysisMapper bloodGasAnalysisMapper;

    @Override
    public BloodGasAnalysisResponse createBloodGasAnalysis(BloodGasAnalysisRequest request) {
        log.info("Tạo BloodGasAnalysis mới: {}", request);

        BloodGasAnalysis bloodGasAnalysis = bloodGasAnalysisMapper.toEntity(request);

        Encounter encounter = new Encounter();
        encounter.setId(request.getEncounterId());
        bloodGasAnalysis.setEncounter(encounter);

        bloodGasAnalysis.setEvaluate(request.getEvaluate());
        bloodGasAnalysis.setNotes(request.getNotes());
        bloodGasAnalysis.setTestName(request.getTestName());
        bloodGasAnalysis.setOrganSystem(request.getOrganSystem());
        bloodGasAnalysis.setIsInvasive(request.getIsInvasive());
        bloodGasAnalysis.setIsQuantitative(request.getIsQuantitative());
        bloodGasAnalysis.setRecordDuration(request.getRecordDuration());
        bloodGasAnalysis.setTestEnvironment(request.getTestEnvironment());
        bloodGasAnalysis.setPatientPosition(request.getPatientPosition());


        BloodGasAnalysis saved = bloodGasAnalysisRepository.save(bloodGasAnalysis);

        return bloodGasAnalysisMapper.toResponse(saved);
    }

    @Override
    public BloodGasAnalysisResponse getBloodGasAnalysisById(Long id) {
        log.info("Lấy thông tin BloodGasAnalysis với id: {}", id);
        BloodGasAnalysis bloodGasAnalysis = bloodGasAnalysisRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy BloodGasAnalysis với id: " + id));
        return bloodGasAnalysisMapper.toResponse(bloodGasAnalysis);
    }

    @Override
    public List<BloodGasAnalysisResponse> getAll() {
        log.info("Lấy danh sách tất cả BloodGasAnalysis");
        return bloodGasAnalysisRepository.findAll().stream()
                .map(bloodGasAnalysisMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PageResponse<BloodGasAnalysisResponse> getAll(int page, int size, String sort, String direction) {
        log.info("Lấy danh sách BloodGasAnalysis với phân trang: page={}, size={}, sort={}, direction={}", page, size, sort, direction);
        Pageable pageable = PaginationUtil.createPageable(page, size, sort, direction);
        Page<BloodGasAnalysis> bloodGasAnalysisPage = bloodGasAnalysisRepository.findAll(pageable);

        return PageResponse.<BloodGasAnalysisResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(bloodGasAnalysisPage.getTotalPages())
                .totalElements(bloodGasAnalysisPage.getTotalElements())
                .content(bloodGasAnalysisPage.getContent().stream()
                        .map(bloodGasAnalysisMapper::toResponse)
                        .toList())
                .build();
    }

    @Override
    public BloodGasAnalysisResponse updateBloodGasAnalysis(Long id, BloodGasAnalysisRequest request) {
        log.info("Cập nhật BloodGasAnalysis với id: {}", id);

        BloodGasAnalysis bloodGasAnalysis = bloodGasAnalysisRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy BloodGasAnalysis với id: " + id));

        bloodGasAnalysisMapper.updateEntity(bloodGasAnalysis, request);

        Encounter encounter = new Encounter();
        encounter.setId(request.getEncounterId());
        bloodGasAnalysis.setEncounter(encounter);

        bloodGasAnalysisRepository.save(bloodGasAnalysis);
        return bloodGasAnalysisMapper.toResponse(bloodGasAnalysis);
    }

    @Override
    public void deleteBloodGasAnalysis(Long id) {
        log.info("Xóa BloodGasAnalysis với id: {}", id);
        BloodGasAnalysis bloodGasAnalysis = bloodGasAnalysisRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy BloodGasAnalysis với id: " + id));
        bloodGasAnalysisRepository.delete(bloodGasAnalysis);
    }
}

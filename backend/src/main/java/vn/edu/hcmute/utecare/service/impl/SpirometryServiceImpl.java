package vn.edu.hcmute.utecare.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.edu.hcmute.utecare.dto.request.SpirometryRequest;
import vn.edu.hcmute.utecare.dto.response.SpirometryResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.exception.NotFoundException;
import vn.edu.hcmute.utecare.mapper.SpirometryMapper;
import vn.edu.hcmute.utecare.model.*;
import vn.edu.hcmute.utecare.repository.SpirometryRepository;
import vn.edu.hcmute.utecare.service.SpirometryService;
import vn.edu.hcmute.utecare.util.PaginationUtil;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SpirometryServiceImpl implements SpirometryService {

    private final SpirometryRepository spirometryRepository;

    @Override
    public SpirometryResponse createSpirometry(SpirometryRequest request) {
        log.info("Tạo Spirometry mới: {}", request);
        Spirometry spirometry = SpirometryMapper.INSTANCE.toEntity(request);


        Encounter encounter = new Encounter();
        encounter.setId(request.getEncounterId());
        spirometry.setEncounter(encounter);

        Spirometry saved = spirometryRepository.save(spirometry);
        return SpirometryMapper.INSTANCE.toResponse(saved);
    }

    @Override
    public SpirometryResponse getSpirometryById(Long id) {
        log.info("Lấy thông tin Spirometry với id: {}", id);
        Spirometry spirometry = spirometryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy Spirometry với id: " + id));
        return SpirometryMapper.INSTANCE.toResponse(spirometry);
    }

    @Override
    public List<SpirometryResponse> getAll() {
        log.info("Lấy danh sách tất cả Spirometry");
        return spirometryRepository.findAll().stream()
                .map(SpirometryMapper.INSTANCE::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PageResponse<SpirometryResponse> getAll(int page, int size, String sort, String direction) {
        log.info("Lấy danh sách Spirometry với phân trang: page={}, size={}, sort={}, direction={}", page, size, sort, direction);
        Pageable pageable = PaginationUtil.createPageable(page, size, sort, direction);
        Page<Spirometry> spirometryPage = spirometryRepository.findAll(pageable);

        return PageResponse.<SpirometryResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(spirometryPage.getTotalPages())
                .totalElements(spirometryPage.getTotalElements())
                .content(spirometryPage.getContent().stream()
                        .map(SpirometryMapper.INSTANCE::toResponse)
                        .toList())
                .build();
    }

    @Override
    public SpirometryResponse updateSpirometry(Long id, SpirometryRequest request) {
        log.info("Cập nhật Spirometry với id: {}", id);

        Spirometry spirometry = spirometryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy Spirometry với id: " + id));

        SpirometryMapper.INSTANCE.updateEntity(spirometry, request);

        Encounter encounter = new Encounter();
        encounter.setId(request.getEncounterId());
        spirometry.setEncounter(encounter);

        spirometryRepository.save(spirometry);
        return SpirometryMapper.INSTANCE.toResponse(spirometry);
    }
    @Override
    public void deleteSpirometry(Long id) {
        log.info("Xóa Spirometry với id: {}", id);
        Spirometry spirometry = spirometryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy Spirometry với id: " + id));
        spirometryRepository.delete(spirometry);
    }
}

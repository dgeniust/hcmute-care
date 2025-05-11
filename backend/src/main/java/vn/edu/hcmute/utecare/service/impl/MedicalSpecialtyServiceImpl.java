package vn.edu.hcmute.utecare.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.edu.hcmute.utecare.dto.request.MedicalSpecialtyRequest;
import vn.edu.hcmute.utecare.dto.response.MedicalSpecialtyResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.exception.ResourceNotFoundException;
import vn.edu.hcmute.utecare.mapper.MedicalSpecialtyMapper;
import vn.edu.hcmute.utecare.model.MedicalSpecialty;
import vn.edu.hcmute.utecare.repository.MedicalSpecialtyRepository;
import vn.edu.hcmute.utecare.service.MedicalSpecialtyService;
import vn.edu.hcmute.utecare.util.PaginationUtil;

@Service
@RequiredArgsConstructor
@Slf4j
public class MedicalSpecialtyServiceImpl implements MedicalSpecialtyService {
    private final MedicalSpecialtyRepository medicalSpecialtyRepository;
    private final MedicalSpecialtyMapper medicalSpecialtyMapper;

    @Override
    public MedicalSpecialtyResponse getMedicalSpecialtyById(Integer id) {
        log.info("Truy xuất chuyên khoa với ID: {}", id);
        MedicalSpecialty medicalSpecialty = medicalSpecialtyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy chuyên khoa với ID: " + id));
        log.info("Truy xuất chuyên khoa thành công với ID: {}", id);
        return medicalSpecialtyMapper.toResponse(medicalSpecialty);
    }

    @Override
    public MedicalSpecialtyResponse createMedicalSpecialty(MedicalSpecialtyRequest request) {
        log.info("Tạo chuyên khoa mới với thông tin: {}", request);
        MedicalSpecialty medicalSpecialty = medicalSpecialtyMapper.toEntity(request);
        MedicalSpecialty savedSpecialty = medicalSpecialtyRepository.save(medicalSpecialty);
        log.info("Tạo chuyên khoa thành công với ID: {}", savedSpecialty.getId());
        return medicalSpecialtyMapper.toResponse(savedSpecialty);
    }

    @Override
    public MedicalSpecialtyResponse updateMedicalSpecialty(Integer id, MedicalSpecialtyRequest request) {
        log.info("Cập nhật chuyên khoa với ID: {} và thông tin: {}", id, request);
        MedicalSpecialty medicalSpecialty = medicalSpecialtyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy chuyên khoa với ID: " + id));

        medicalSpecialtyMapper.update(request, medicalSpecialty);
        MedicalSpecialty updatedSpecialty = medicalSpecialtyRepository.save(medicalSpecialty);
        log.info("Cập nhật chuyên khoa thành công với ID: {}", id);
        return medicalSpecialtyMapper.toResponse(updatedSpecialty);
    }

    @Override
    public void deleteMedicalSpecialty(Integer id) {
        log.info("Xóa chuyên khoa với ID: {}", id);
        if (!medicalSpecialtyRepository.existsById(id)) {
            throw new ResourceNotFoundException("Không tìm thấy chuyên khoa với ID: " + id);
        }
        medicalSpecialtyRepository.deleteById(id);
        log.info("Xóa chuyên khoa thành công với ID: {}", id);
    }

    @Override
    public PageResponse<MedicalSpecialtyResponse> getAllMedicalSpecialties(int page, int size, String sort, String direction) {
        log.info("Truy xuất danh sách chuyên khoa: trang={}, kích thước={}, sắp xếp={}, hướng={}", page, size, sort, direction);
        Pageable pageable = PaginationUtil.createPageable(page, size, sort, direction);

        Page<MedicalSpecialty> medicalSpecialtyPage = medicalSpecialtyRepository.findAll(pageable);
        return PageResponse.<MedicalSpecialtyResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(medicalSpecialtyPage.getTotalPages())
                .totalElements(medicalSpecialtyPage.getTotalElements())
                .content(medicalSpecialtyPage.getContent().stream().map(medicalSpecialtyMapper::toResponse).toList())
                .build();
    }

    @Override
    public PageResponse<MedicalSpecialtyResponse> searchMedicalSpecialties(String keyword, int page, int size, String sort, String direction) {
        log.info("Tìm kiếm chuyên khoa với từ khóa: {}, trang={}, kích thước={}, sắp xếp={}, hướng={}", keyword, page, size, sort, direction);
        Pageable pageable = PaginationUtil.createPageable(page, size, sort, direction);

        Page<MedicalSpecialty> medicalSpecialtyPage = medicalSpecialtyRepository.searchMedicalSpecialties(keyword, pageable);
        return PageResponse.<MedicalSpecialtyResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(medicalSpecialtyPage.getTotalPages())
                .totalElements(medicalSpecialtyPage.getTotalElements())
                .content(medicalSpecialtyPage.getContent().stream().map(medicalSpecialtyMapper::toResponse).toList())
                .build();
    }
}
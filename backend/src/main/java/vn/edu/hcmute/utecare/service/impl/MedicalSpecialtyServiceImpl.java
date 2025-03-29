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
        log.info("Fetching medical specialty with id: {}", id);
        MedicalSpecialty medicalSpecialty = medicalSpecialtyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medical specialty not found with id: " + id));
        return medicalSpecialtyMapper.toResponse(medicalSpecialty);
    }

    @Override
    public MedicalSpecialtyResponse createMedicalSpecialty(MedicalSpecialtyRequest request) {
        log.info("Creating new medical specialty: {}", request);
        MedicalSpecialty medicalSpecialty = medicalSpecialtyMapper.toEntity(request);
        return medicalSpecialtyMapper.toResponse(medicalSpecialtyRepository.save(medicalSpecialty));
    }

    @Override
    public MedicalSpecialtyResponse updateMedicalSpecialty(Integer id, MedicalSpecialtyRequest request) {
        log.info("Updating medical specialty with id: {}", id);
        MedicalSpecialty medicalSpecialty = medicalSpecialtyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medical specialty not found with id: " + id));

        medicalSpecialtyMapper.update(request, medicalSpecialty);
        MedicalSpecialty updatedSpecialty = medicalSpecialtyRepository.save(medicalSpecialty);
        return medicalSpecialtyMapper.toResponse(updatedSpecialty);
    }

    @Override
    public void deleteMedicalSpecialty(Integer id) {
        log.info("Deleting medical specialty with id: {}", id);
        MedicalSpecialty medicalSpecialty = medicalSpecialtyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medical specialty not found with id: " + id));
        medicalSpecialtyRepository.delete(medicalSpecialty);
    }

    @Override
    public PageResponse<MedicalSpecialtyResponse> getAllMedicalSpecialties(int page, int size, String sort, String direction) {
        log.info("Fetching all medical specialties with pagination: page={}, size={}, sort={}, direction={}", page, size, sort, direction);
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

}

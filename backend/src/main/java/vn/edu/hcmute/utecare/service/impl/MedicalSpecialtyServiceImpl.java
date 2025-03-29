package vn.edu.hcmute.utecare.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.edu.hcmute.utecare.dto.request.MedicalSpecialtyRequest;
import vn.edu.hcmute.utecare.dto.response.DoctorResponse;
import vn.edu.hcmute.utecare.dto.response.MedicalSpecialtyResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.exception.ResourceNotFoundException;
import vn.edu.hcmute.utecare.mapper.DoctorMapper;
import vn.edu.hcmute.utecare.mapper.MedicalSpecialtyMapper;
import vn.edu.hcmute.utecare.model.Doctor;
import vn.edu.hcmute.utecare.model.MedicalSpecialty;
import vn.edu.hcmute.utecare.repository.DoctorRepository;
import vn.edu.hcmute.utecare.repository.MedicalSpecialtyRepository;
import vn.edu.hcmute.utecare.service.MedicalSpecialtyService;
import vn.edu.hcmute.utecare.util.PaginationUtil;

@Service
@RequiredArgsConstructor
@Slf4j
public class MedicalSpecialtyServiceImpl implements MedicalSpecialtyService {
    private final MedicalSpecialtyRepository medicalSpecialtyRepository;
    private final DoctorRepository doctorRepository;

    @Override
    public MedicalSpecialtyResponse getMedicalSpecialtyById(Integer id) {
        log.info("Fetching medical specialty with id: {}", id);
        MedicalSpecialty medicalSpecialty = medicalSpecialtyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medical specialty not found with id: " + id));
        return MedicalSpecialtyMapper.INSTANCE.toResponse(medicalSpecialty);
    }

    @Override
    public MedicalSpecialtyResponse createMedicalSpecialty(MedicalSpecialtyRequest request) {
        log.info("Creating new medical specialty: {}", request);
        MedicalSpecialty medicalSpecialty = MedicalSpecialtyMapper.INSTANCE.toEntity(request);
        return MedicalSpecialtyMapper.INSTANCE.toResponse(medicalSpecialtyRepository.save(medicalSpecialty));
    }

    @Override
    public MedicalSpecialtyResponse updateMedicalSpecialty(Integer id, MedicalSpecialtyRequest request) {
        log.info("Updating medical specialty with id: {}", id);
        MedicalSpecialty medicalSpecialty = medicalSpecialtyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medical specialty not found with id: " + id));

        MedicalSpecialtyMapper.INSTANCE.update(request, medicalSpecialty);
        MedicalSpecialty updatedSpecialty = medicalSpecialtyRepository.save(medicalSpecialty);
        return MedicalSpecialtyMapper.INSTANCE.toResponse(updatedSpecialty);
    }

    @Override
    public void deleteMedicalSpecialty(Integer id) {
        log.info("Deleting medical specialty with id: {}", id);
        if (!medicalSpecialtyRepository.existsById(id)) {
            throw new ResourceNotFoundException("Medical specialty not found with id: " + id);
        }
        medicalSpecialtyRepository.deleteById(id);
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
                .content(medicalSpecialtyPage.getContent().stream().map(MedicalSpecialtyMapper.INSTANCE::toResponse).toList())
                .build();
    }

    @Override
    public PageResponse<MedicalSpecialtyResponse> searchMedicalSpecialties(String keyword, int page, int size, String sort, String direction) {
        log.info("Searching medical specialties with keyword: {}", keyword);
        Pageable pageable = PaginationUtil.createPageable(page, size, sort, direction);

        Page<MedicalSpecialty> medicalSpecialtyPage = medicalSpecialtyRepository.searchMedicalSpecialties(keyword, pageable);
        return PageResponse.<MedicalSpecialtyResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(medicalSpecialtyPage.getTotalPages())
                .totalElements(medicalSpecialtyPage.getTotalElements())
                .content(medicalSpecialtyPage.getContent().stream().map(MedicalSpecialtyMapper.INSTANCE::toResponse).toList())
                .build();
    }

    @Override
    public PageResponse<DoctorResponse> getDoctorsByMedicalSpecialtyId(Integer id, int page, int size, String sort, String direction) {
        log.info("Fetching doctors for medical specialty with id: {}", id);
        Pageable pageable = PaginationUtil.createPageable(page, size, sort, direction);

        Page<Doctor> doctorPage = doctorRepository.findByMedicalSpecialty_Id(id, pageable);
        return PageResponse.<DoctorResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(doctorPage.getTotalPages())
                .totalElements(doctorPage.getTotalElements())
                .content(doctorPage.getContent().stream().map(DoctorMapper.INSTANCE::toResponse).toList())
                .build();
    }

    @Override
    public PageResponse<DoctorResponse> searchDoctorsByMedicalSpecialtyId(Integer id, String keyword, int page, int size, String sort, String direction) {
        log.info("Searching doctors for medical specialty with id: {} and keyword: {}", id, keyword);
        Pageable pageable = PaginationUtil.createPageable(page, size, sort, direction);

        Page<Doctor> doctorPage = doctorRepository.searchDoctorsByMedicalSpecialty(id, keyword, pageable);
        return PageResponse.<DoctorResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(doctorPage.getTotalPages())
                .totalElements(doctorPage.getTotalElements())
                .content(doctorPage.getContent().stream().map(DoctorMapper.INSTANCE::toResponse).toList())
                .build();
    }
}

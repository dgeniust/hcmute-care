package vn.edu.hcmute.utecare.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.edu.hcmute.utecare.dto.request.DoctorRequest;
import vn.edu.hcmute.utecare.dto.response.DoctorResponse;
import vn.edu.hcmute.utecare.dto.response.MedicalTestDetailResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.exception.ResourceNotFoundException;
import vn.edu.hcmute.utecare.mapper.*;
import vn.edu.hcmute.utecare.model.*;
import vn.edu.hcmute.utecare.repository.AccountRepository;
import vn.edu.hcmute.utecare.repository.DoctorRepository;
import vn.edu.hcmute.utecare.repository.MedicalSpecialtyRepository;
import vn.edu.hcmute.utecare.repository.MedicalTestRepository;
import vn.edu.hcmute.utecare.service.DoctorService;
import vn.edu.hcmute.utecare.util.enumeration.AccountStatus;
import vn.edu.hcmute.utecare.util.PaginationUtil;
import vn.edu.hcmute.utecare.util.enumeration.Role;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class DoctorServiceImpl implements DoctorService {
    private final AccountRepository accountRepository;
    private final DoctorRepository doctorRepository;
    private final MedicalSpecialtyRepository medicalSpecialtyRepository;
    private final PasswordEncoder passwordEncoder;
    private final MedicalTestRepository medicalTestRepository;
    @Override
    @Transactional(rollbackOn = Exception.class)
    public DoctorResponse createDoctor(DoctorRequest request){
        log.info("Creating doctor with request: {}", request);

        if (doctorRepository.existsByPhone(request.getPhone())) {
            throw new IllegalArgumentException("Phone number already exists");
        }

        Doctor doctor = DoctorMapper.INSTANCE.toEntity(request);

        if (request.getMedicalSpecialtyId() != null) {
            MedicalSpecialty specialty = medicalSpecialtyRepository.findById(request.getMedicalSpecialtyId())
                    .orElseThrow(() -> new ResourceNotFoundException("Medical specialty not found with ID: " + request.getMedicalSpecialtyId()));
            doctor.setMedicalSpecialty(specialty);
        }

        Doctor savedDoctor = doctorRepository.save(doctor);

        Account account = Account.builder()
                .password(passwordEncoder.encode(request.getPhone()))
                .user(savedDoctor)
                .role(Role.NURSE)
                .status(AccountStatus.ACTIVE)
                .build();
        accountRepository.save(account);
        log.info("Saved doctor: {}", savedDoctor);
        return DoctorMapper.INSTANCE.toResponse(savedDoctor);
    }

    @Override
    public DoctorResponse getDoctorById(Long id) {
        log.info("Getting doctor by id: {}", id);
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with ID: " + id));
        return DoctorMapper.INSTANCE.toResponse(doctor);
    }

    @Override
    @Transactional
    public DoctorResponse updateDoctor(Long id, DoctorRequest request){
        log.info("Updating doctor with id: {} and request: {}", id, request);
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with ID: " + id));
        DoctorMapper.INSTANCE.updateEntity(request, doctor);

        if (!doctor.getPhone().equals(request.getPhone()) && doctorRepository.existsByPhone(request.getPhone())) {
            throw new IllegalArgumentException("Phone number already exists");
        }

        if (request.getMedicalSpecialtyId() != null) {
            MedicalSpecialty specialty = medicalSpecialtyRepository.findById(request.getMedicalSpecialtyId())
                    .orElseThrow(() -> new ResourceNotFoundException("Medical specialty not found with ID: " + request.getMedicalSpecialtyId()));
            doctor.setMedicalSpecialty(specialty);
        }
        return DoctorMapper.INSTANCE.toResponse(doctorRepository.save(doctor));
    }

    @Transactional
    @Override
    public void deleteDoctor(Long id) {
        log.info("Deleting doctor with id: {}", id);
        Account account = accountRepository.findByUser_Id(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found for doctor with ID: " + id));
        accountRepository.delete(account);
        log.info("Successfully deleted doctor with id: {}", id);
    }

    @Override
    public PageResponse<DoctorResponse> getAllDoctors(int page, int size, String sort, String direction) {
        log.info("Fetching all doctors with pagination: page={}, size={}, sort={}, direction={}", page, size, sort, direction);
        Pageable pageable = PaginationUtil.createPageable(page, size, sort, direction);

        Page<Doctor> doctorPage = doctorRepository.findAll(pageable);
        return PageResponse.<DoctorResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(doctorPage.getTotalPages())
                .totalElements(doctorPage.getTotalElements())
                .content(doctorPage.getContent().stream().map(DoctorMapper.INSTANCE::toResponse).toList())
                .build();
    }

    @Override
    public PageResponse<DoctorResponse> searchDoctors(String keyword, int page, int size, String sort, String direction) {
        log.info("Searching doctors with keyword: {}, page={}, size={}, sort={}, direction={}", keyword, page, size, sort, direction);
        Pageable pageable = PaginationUtil.createPageable(page, size, sort, direction);

        Page<Doctor> doctorPage = doctorRepository.searchDoctors(keyword, pageable);
        return PageResponse.<DoctorResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(doctorPage.getTotalPages())
                .totalElements(doctorPage.getTotalElements())
                .content(doctorPage.getContent().stream().map(DoctorMapper.INSTANCE::toResponse).toList())
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


    @Override
    public List<MedicalTestDetailResponse> getMedicalTestsByPatientId(Long patientId, LocalDate date) {
        log.info("Lấy danh sách MedicalTest cho bệnh nhân với patientId: {} và ngày: {}", patientId, date);

        List<MedicalTest> tests;
        if (date != null) {
            LocalDateTime startOfDay = date.atStartOfDay(); // 00:00:00
            LocalDateTime endOfDay = date.atTime(23, 59, 59); // 23:59:59
            tests = medicalTestRepository.findByPatientIdAndCreateDateBetween(patientId, startOfDay, endOfDay);
        } else {
            tests = medicalTestRepository.findByPatientId(patientId);
        }

        return tests.stream()
                .map(this::mapToDetailResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<MedicalTestDetailResponse> getMedicalTestsByPatientId(Long patientId) {
        log.info("Lấy tất cả MedicalTest cho bệnh nhân với patientId: {}", patientId);

        List<MedicalTest> tests = medicalTestRepository.findByPatientId(patientId);

        return tests.stream()
                .map(this::mapToDetailResponse)
                .collect(Collectors.toList());
    }

    @Override
    public MedicalTestDetailResponse mapToDetailResponse(MedicalTest medicalTest) {
        MedicalTestDetailResponse.MedicalTestDetailResponseBuilder builder = MedicalTestDetailResponse.builder()
                .id(medicalTest.getId())
                .evaluate(medicalTest.getEvaluate())
                .notes(medicalTest.getNotes())
                .encounterId(medicalTest.getEncounter().getId())
                .createDate(medicalTest.getCreateDate())
                .status(medicalTest.getStatus());
        if (medicalTest instanceof LaboratoryTests) {
            return builder
                    .type("LaboratoryTests")
                    .details(LaboratoryTestsMapper.INSTANCE.toResponse((LaboratoryTests) medicalTest))
                    .build();
        } else if (medicalTest instanceof CardiacTest) {
            return builder
                    .type("CardiacTest")
                    .details(CardiacTestMapper.INSTANCE.toResponse((CardiacTest) medicalTest))
                    .build();
        } else if (medicalTest instanceof ImagingTest) {
            return builder
                    .type("ImagingTest")
                    .details(ImagingTestMapper.INSTANCE.toResponse((ImagingTest) medicalTest))
                    .build();
        } else if (medicalTest instanceof DigestiveTest) {
            return builder
                    .type("DigestiveTest")
                    .details(DigestiveTestMapper.INSTANCE.toResponse((DigestiveTest) medicalTest))
                    .build();
        } else if (medicalTest instanceof EEG) {
            return builder
                    .type("EEG")
                    .details(EEGMapper.INSTANCE.toResponse((EEG) medicalTest))
                    .build();
        } else if (medicalTest instanceof EMG) {
            return builder
                    .type("EMG")
                    .details(EMGMapper.INSTANCE.toResponse((EMG) medicalTest))
                    .build();
        } else if (medicalTest instanceof Spirometry) {
            return builder
                    .type("Spirometry")
                    .details(SpirometryMapper.INSTANCE.toResponse((Spirometry) medicalTest))
                    .build();
        } else if (medicalTest instanceof BloodGasAnalysis) {
            return builder
                    .type("BloodGasAnalysis")
                    .details(BloodGasAnalysisMapper.INSTANCE.toResponse((BloodGasAnalysis) medicalTest))
                    .build();
        } else if (medicalTest instanceof NerveConduction) {
            return builder
                    .type("NerveConduction")
                    .details(NerveConductionMapper.INSTANCE.toResponse((NerveConduction) medicalTest))
                    .build();
        } else {
            return builder
                    .type("MedicalTest")
                    .details(MedicalTestMapper.INSTANCE.toResponse(medicalTest))
                    .build();
        }
    }


}

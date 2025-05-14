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
import vn.edu.hcmute.utecare.repository.*;
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
    private final DoctorMapper doctorMapper;
    private final MedicalSpecialtyRepository medicalSpecialtyRepository;
    private final PasswordEncoder passwordEncoder;
    private final MedicalTestRepository medicalTestRepository;
    private final LaboratoryTestsRepository laboratoryTestsRepository;
    private final ImagingTestMapper imagingTestMapper;
    private final CardiacTestMapper cardiacTestMapper;
    private final DigestiveTestMapper digestiveTestMapper;
    private final SpirometryMapper spirometryMapper;
    private final BloodGasAnalysisMapper bloodGasAnalysisMapper;
    private final NerveConductionMapper nerveConductionMapper;
    private final EEGMapper eegMapper;
    private final EMGMapper emgMapper;
    private final LaboratoryTestsMapper laboratoryTestsMapper;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public DoctorResponse createDoctor(DoctorRequest request) {
        log.info("Tạo bác sĩ mới với thông tin: {}", request);

        if (doctorRepository.existsByPhone(request.getPhone())) {
            throw new IllegalArgumentException("Số điện thoại đã tồn tại");
        }

        Doctor doctor = doctorMapper.toEntity(request);

        if (request.getMedicalSpecialtyId() != null) {
            MedicalSpecialty specialty = medicalSpecialtyRepository.findById(request.getMedicalSpecialtyId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy chuyên khoa với ID: " + request.getMedicalSpecialtyId()));
            doctor.setMedicalSpecialty(specialty);
        }

        Doctor savedDoctor = doctorRepository.save(doctor);

        Account account = Account.builder()
                .password(passwordEncoder.encode(request.getPhone()))
                .user(savedDoctor)
                .role(Role.DOCTOR) // Sửa từ Role.NURSE thành Role.DOCTOR để phù hợp
                .status(AccountStatus.ACTIVE)
                .build();
        accountRepository.save(account);
        log.info("Tạo bác sĩ thành công với ID: {}", savedDoctor.getId());
        return doctorMapper.toResponse(savedDoctor);
    }

    @Override
    public DoctorResponse getDoctorById(Long id) {
        log.info("Truy xuất bác sĩ với ID: {}", id);
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bác sĩ với ID: " + id));
        log.info("Truy xuất bác sĩ thành công với ID: {}", id);
        return doctorMapper.toResponse(doctor);
    }

    @Override
    @Transactional
    public DoctorResponse updateDoctor(Long id, DoctorRequest request) {
        log.info("Cập nhật bác sĩ với ID: {} và thông tin: {}", id, request);
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bác sĩ với ID: " + id));
        doctorMapper.updateEntity(request, doctor);

        if (!doctor.getPhone().equals(request.getPhone()) && doctorRepository.existsByPhone(request.getPhone())) {
            throw new IllegalArgumentException("Số điện thoại đã tồn tại");
        }

        if (request.getMedicalSpecialtyId() != null) {
            MedicalSpecialty specialty = medicalSpecialtyRepository.findById(request.getMedicalSpecialtyId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy chuyên khoa với ID: " + request.getMedicalSpecialtyId()));
            doctor.setMedicalSpecialty(specialty);
        }
        Doctor updatedDoctor = doctorRepository.save(doctor);
        log.info("Cập nhật bác sĩ thành công với ID: {}", id);
        return doctorMapper.toResponse(updatedDoctor);
    }

    @Override
    @Transactional
    public void deleteDoctor(Long id) {
        log.info("Xóa bác sĩ với ID: {}", id);
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bác sĩ với ID: " + id));
        Account account = accountRepository.findByUser_Id(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tài khoản cho bác sĩ với ID: " + id));
        accountRepository.delete(account);
        doctorRepository.delete(doctor);
        log.info("Xóa bác sĩ thành công với ID: {}", id);
    }

    @Override
    public PageResponse<DoctorResponse> getAllDoctors(int page, int size, String sort, String direction) {
        log.info("Truy xuất danh sách bác sĩ: trang={}, kích thước={}, sắp xếp={}, hướng={}", page, size, sort, direction);
        Pageable pageable = PaginationUtil.createPageable(page, size, sort, direction);

        Page<Doctor> doctorPage = doctorRepository.findAll(pageable);
        return PageResponse.<DoctorResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(doctorPage.getTotalPages())
                .totalElements(doctorPage.getTotalElements())
                .content(doctorPage.getContent().stream().map(doctorMapper::toResponse).toList())
                .build();
    }

    @Override
    public PageResponse<DoctorResponse> searchDoctors(String keyword, int page, int size, String sort, String direction) {
        log.info("Tìm kiếm bác sĩ với từ khóa: {}, trang={}, kích thước={}, sắp xếp={}, hướng={}", keyword, page, size, sort, direction);
        Pageable pageable = PaginationUtil.createPageable(page, size, sort, direction);

        Page<Doctor> doctorPage = doctorRepository.searchDoctors(keyword, pageable);
        return PageResponse.<DoctorResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(doctorPage.getTotalPages())
                .totalElements(doctorPage.getTotalElements())
                .content(doctorPage.getContent().stream().map(doctorMapper::toResponse).toList())
                .build();
    }

    @Override
    public PageResponse<DoctorResponse> getDoctorsByMedicalSpecialtyId(Integer id, int page, int size, String sort, String direction) {
        log.info("Truy xuất danh sách bác sĩ theo chuyên khoa với ID: {}", id);
        Pageable pageable = PaginationUtil.createPageable(page, size, sort, direction);

        Page<Doctor> doctorPage = doctorRepository.findByMedicalSpecialty_Id(id, pageable);
        return PageResponse.<DoctorResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(doctorPage.getTotalPages())
                .totalElements(doctorPage.getTotalElements())
                .content(doctorPage.getContent().stream().map(doctorMapper::toResponse).toList())
                .build();
    }

    @Override
    public PageResponse<DoctorResponse> searchDoctorsByMedicalSpecialtyId(Integer id, String keyword, int page, int size, String sort, String direction) {
        log.info("Tìm kiếm bác sĩ theo chuyên khoa với ID: {} và từ khóa: {}", id, keyword);
        Pageable pageable = PaginationUtil.createPageable(page, size, sort, direction);

        Page<Doctor> doctorPage = doctorRepository.searchDoctorsByMedicalSpecialty(id, keyword, pageable);
        return PageResponse.<DoctorResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(doctorPage.getTotalPages())
                .totalElements(doctorPage.getTotalElements())
                .content(doctorPage.getContent().stream().map(doctorMapper::toResponse).toList())
                .build();
    }

//    @Override
//    public List<MedicalTestDetailResponse> getMedicalTestsByPatientId(Long patientId, LocalDate date) {
//        log.info("Truy xuất danh sách xét nghiệm y tế cho bệnh nhân với ID: {} và ngày: {}", patientId, date);
//
//        List<MedicalTest> tests;
//        if (date != null) {
//            LocalDateTime startOfDay = date.atStartOfDay();
//            LocalDateTime endOfDay = date.atTime(23, 59, 59);
//            tests = medicalTestRepository.findByPatientIdAndCreateDateBetween(patientId, startOfDay, endOfDay);
//        } else {
//            tests = medicalTestRepository.findByPatientId(patientId);
//        }
//
//        return tests.stream()
//                .map(this::mapToDetailResponse)
//                .collect(Collectors.toList());
//    }
//
    @Override
    public List<MedicalTestDetailResponse> getMedicalTestsByPatientId(Long patientId) {
        log.info("Truy xuất tất cả xét nghiệm y tế cho bệnh nhân với ID: {}", patientId);

        List<MedicalTest> tests = medicalTestRepository.findByPatientId(patientId);

        return tests.stream()
                .map(this::mapToDetailResponse)
                .collect(Collectors.toList());
    }


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
                    .type("Xét nghiệm máu")
                    .details(laboratoryTestsMapper.toResponse((LaboratoryTests) medicalTest))
                    .build();
        } else if (medicalTest instanceof CardiacTest) {
            return builder
                    .type("Xét nghiệm tim mạch")
                    .details(cardiacTestMapper.toResponse((CardiacTest) medicalTest))
                    .build();
        } else if (medicalTest instanceof ImagingTest) {
            return builder
                    .type("Chụp hình ảnh")
                    .details(imagingTestMapper.toResponse((ImagingTest) medicalTest))
                    .build();
        } else if (medicalTest instanceof DigestiveTest) {
            return builder
                    .type("Xét nghiệm tiêu hóa")
                    .details(digestiveTestMapper.toResponse((DigestiveTest) medicalTest))
                    .build();
        } else if (medicalTest instanceof EEG) {
            return builder
                    .type("Điện não đồ (EEG)")
                    .details(eegMapper.toResponse((EEG) medicalTest))
                    .build();
        } else if (medicalTest instanceof EMG) {
            return builder
                    .type("Điện cơ đồ (EMG)")
                    .details(emgMapper.toResponse((EMG) medicalTest))
                    .build();
        } else if (medicalTest instanceof Spirometry) {
            return builder
                    .type("Đo hô hấp")
                    .details(spirometryMapper.toResponse((Spirometry) medicalTest))
                    .build();
        } else if (medicalTest instanceof BloodGasAnalysis) {
            return builder
                    .type("Phân tích khí máu")
                    .details(bloodGasAnalysisMapper.toResponse((BloodGasAnalysis) medicalTest))
                    .build();
        } else if (medicalTest instanceof NerveConduction) {
            return builder
                    .type("Dẫn truyền thần kinh")
                    .details(nerveConductionMapper.toResponse((NerveConduction) medicalTest))
                    .build();
        }
        throw new ResourceNotFoundException("Not found");
    }
}
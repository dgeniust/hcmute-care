package vn.edu.hcmute.utecare.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.edu.hcmute.utecare.dto.request.DoctorCreationRequest;
import vn.edu.hcmute.utecare.dto.request.DoctorRequest;
import vn.edu.hcmute.utecare.dto.response.DoctorResponse;
import vn.edu.hcmute.utecare.dto.response.DoctorScheduleResponse;
import vn.edu.hcmute.utecare.dto.response.DoctorScheduleSummaryResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.exception.ResourceNotFoundException;
import vn.edu.hcmute.utecare.mapper.AccountMapper;
import vn.edu.hcmute.utecare.mapper.DoctorMapper;
import vn.edu.hcmute.utecare.mapper.DoctorScheduleMapper;
import vn.edu.hcmute.utecare.model.Account;
import vn.edu.hcmute.utecare.model.Doctor;
import vn.edu.hcmute.utecare.model.DoctorSchedule;
import vn.edu.hcmute.utecare.model.MedicalSpecialty;
import vn.edu.hcmute.utecare.repository.AccountRepository;
import vn.edu.hcmute.utecare.repository.DoctorRepository;
import vn.edu.hcmute.utecare.repository.DoctorScheduleRepository;
import vn.edu.hcmute.utecare.repository.MedicalSpecialtyRepository;
import vn.edu.hcmute.utecare.service.DoctorService;
import vn.edu.hcmute.utecare.util.enumeration.AccountStatus;
import vn.edu.hcmute.utecare.util.PaginationUtil;
import vn.edu.hcmute.utecare.util.enumeration.Role;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class DoctorServiceImpl implements DoctorService {
    private final AccountRepository accountRepository;
    private final DoctorRepository doctorRepository;
    private final MedicalSpecialtyRepository medicalSpecialtyRepository;
    private final DoctorScheduleRepository doctorScheduleRepository;
    private final PasswordEncoder passwordEncoder;

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
    public PageResponse<DoctorScheduleSummaryResponse> getDoctorAvailability(Long doctorId, LocalDate date, int page, int size, String sort, String direction) {
        log.info("Retrieving doctor availability with doctorId: {}, date: {}, page={}, size={}, sort={}, direction={}",
                doctorId, date, page, size, sort, direction);

        // Kiểm tra xem doctorId có tồn tại không (tùy chọn)
        if (!doctorRepository.existsById(doctorId)) {
            throw new ResourceNotFoundException("Doctor not found with ID: " + doctorId);
        }

        Pageable pageable = PaginationUtil.createPageable(page, size, sort, direction);

        // Gọi repository để lấy các lịch trình còn trống
        Page<DoctorSchedule> schedulePage = doctorScheduleRepository.findAvailableSchedules(doctorId, date, pageable);

        return PageResponse.<DoctorScheduleSummaryResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(schedulePage.getTotalPages())
                .totalElements(schedulePage.getTotalElements())
                .content(schedulePage.getContent().stream().map(DoctorScheduleMapper.INSTANCE::toSummaryResponse).toList())
                .build();
    }
}

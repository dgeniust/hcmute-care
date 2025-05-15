package vn.edu.hcmute.utecare.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.edu.hcmute.utecare.dto.request.MedicalRecordRequest;
import vn.edu.hcmute.utecare.dto.response.EncounterResponse;
import vn.edu.hcmute.utecare.dto.response.MedicalRecordResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.exception.NotFoundException;
import vn.edu.hcmute.utecare.exception.ResourceNotFoundException;
import vn.edu.hcmute.utecare.mapper.EncounterMapper;
import vn.edu.hcmute.utecare.mapper.MedicalRecordMapper;
import vn.edu.hcmute.utecare.model.Customer;
import vn.edu.hcmute.utecare.model.Encounter;
import vn.edu.hcmute.utecare.model.MedicalRecord;
import vn.edu.hcmute.utecare.repository.CustomerRepository;
import vn.edu.hcmute.utecare.repository.EncounterRepository;
import vn.edu.hcmute.utecare.repository.MedicalRecordRepository;
import vn.edu.hcmute.utecare.service.MedicalRecordService;
import vn.edu.hcmute.utecare.util.PaginationUtil;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MedicalRecordServiceImpl implements MedicalRecordService {
    private final MedicalRecordRepository medicalRecordRepository;
    private final CustomerRepository customerRepository;
    private final EncounterRepository encounterRepository;
    private final MedicalRecordMapper medicalRecordMapper;
    private final EncounterMapper encounterMapper;

    @Override
    public MedicalRecordResponse create(MedicalRecordRequest request) {
        log.info("Tạo hồ sơ y tế mới với thông tin: {}", request);
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy khách hàng với ID: " + request.getCustomerId()));

        MedicalRecord medicalRecord = medicalRecordMapper.toEntity(request);
        medicalRecord.setCustomer(customer);

        String barcode = generateUniqueBarcode();
        medicalRecord.setBarcode(barcode);

        MedicalRecord savedRecord = medicalRecordRepository.save(medicalRecord);
        log.info("Tạo hồ sơ y tế thành công với ID: {}", savedRecord.getId());
        return medicalRecordMapper.toResponse(savedRecord);
    }

    @Override
    public MedicalRecordResponse getById(Long id) {
        log.info("Truy xuất hồ sơ y tế với ID: {}", id);
        MedicalRecord medicalRecord = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy hồ sơ y tế với ID: " + id));
        log.info("Truy xuất hồ sơ y tế thành công với ID: {}", id);
        return medicalRecordMapper.toResponse(medicalRecord);
    }

    @Override
    public MedicalRecordResponse update(Long id, MedicalRecordRequest request) {
        log.info("Cập nhật hồ sơ y tế với ID: {} và thông tin: {}", id, request);
        MedicalRecord medicalRecord = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy hồ sơ y tế với ID: " + id));

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy khách hàng với ID: " + request.getCustomerId()));

        MedicalRecord updatedRecord = medicalRecordMapper.toEntity(request);
        medicalRecord.setPatient(updatedRecord.getPatient());
        medicalRecord.setCustomer(customer);

        MedicalRecord savedRecord = medicalRecordRepository.save(medicalRecord);
        log.info("Cập nhật hồ sơ y tế thành công với ID: {}", id);
        return medicalRecordMapper.toResponse(savedRecord);
    }

    @Override
    public void delete(Long id) {
        log.info("Xóa hồ sơ y tế với ID: {}", id);
        MedicalRecord medicalRecord = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy hồ sơ y tế với ID: " + id));
        medicalRecordRepository.delete(medicalRecord);
        log.info("Xóa hồ sơ y tế thành công với ID: {}", id);
    }

    @Override
    public List<EncounterResponse> getAllEncounterByMedicalRecordId(Long medicalRecordId) {
        return List.of();
    }


    @Override
    public List<EncounterResponse> getEncounterByMedicalRecordIdAndDate(Long medicalRecordId, LocalDate date) {
        List<Encounter> encounters = encounterRepository.findByMedicalRecord_IdAndVisitDate(medicalRecordId, date);
        return encounters.stream().map(encounterMapper::toResponse).toList();
    }

    @Override
    public MedicalRecordResponse getByBarcodeAndCustomerId(String barcode, Long customerId) {
        log.info("Truy xuất hồ sơ y tế với mã vạch: {} và ID khách hàng: {}", barcode, customerId);
        if (barcode == null || barcode.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã vạch không được để trống");
        }
        if (customerId == null || customerId <= 0) {
            throw new IllegalArgumentException("ID khách hàng phải là số dương hợp lệ");
        }
        MedicalRecord medicalRecord = medicalRecordRepository.findByBarcodeAndCustomerId(barcode, customerId);
        if (medicalRecord == null) {
            throw new ResourceNotFoundException("Không tìm thấy hồ sơ y tế với mã vạch: " + barcode + " và ID khách hàng: " + customerId);
        }
        log.info("Truy xuất hồ sơ y tế thành công với mã vạch: {}", barcode);
        return medicalRecordMapper.toResponse(medicalRecord);
    }

    @Override
    public List<MedicalRecordResponse> getAll() {
        log.info("Truy xuất danh sách tất cả hồ sơ y tế");
        return medicalRecordRepository.findAll()
                .stream()
                .map(medicalRecordMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PageResponse<MedicalRecordResponse> getAll(int page, int size, String sort, String direction) {
        log.info("Truy xuất danh sách hồ sơ y tế: trang={}, kích thước={}, sắp xếp={}, hướng={}", page, size, sort, direction);
        Pageable pageable = PaginationUtil.createPageable(page, size, sort, direction);
        Page<MedicalRecord> medicalRecordPage = medicalRecordRepository.findAll(pageable);

        return PageResponse.<MedicalRecordResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(medicalRecordPage.getTotalPages())
                .totalElements(medicalRecordPage.getTotalElements())
                .content(medicalRecordPage.getContent().stream()
                        .map(medicalRecordMapper::toResponse)
                        .toList())
                .build();
    }

    @Override
    public PageResponse<MedicalRecordResponse> getAllMedicalRecordsByCustomer(Long customerId, int page, int size, String sort, String direction) {
        log.info("Truy xuất danh sách hồ sơ y tế cho khách hàng ID: {}: trang={}, kích thước={}, sắp xếp={}, hướng={}",
                customerId, page, size, sort, direction);

        if (!customerRepository.existsById(customerId)) {
            throw new IllegalArgumentException("Khách hàng không tồn tại");
        }

        Pageable pageable = PaginationUtil.createPageable(page, size, sort, direction);
        Page<MedicalRecord> medicalRecordPage = medicalRecordRepository.findByCustomerId(customerId, pageable);

        return PageResponse.<MedicalRecordResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(medicalRecordPage.getTotalPages())
                .totalElements(medicalRecordPage.getTotalElements())
                .content(medicalRecordPage.getContent().stream()
                        .map(medicalRecordMapper::toResponse)
                        .toList())
                .build();
    }

    private String generateUniqueBarcode() {
        String year = String.valueOf(Year.now().getValue() % 100);
        String prefix = "W" + year + "_";
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        String barcode;

        do {
            StringBuilder randomStr = new StringBuilder();
            for (int i = 0; i < 6; i++) {
                int index = random.nextInt(characters.length());
                randomStr.append(characters.charAt(index));
            }
            barcode = prefix + randomStr.toString();
        } while (medicalRecordRepository.findByBarcode(barcode).isPresent());

        return barcode;
    }
}
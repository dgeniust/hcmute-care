package vn.edu.hcmute.utecare.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import vn.edu.hcmute.utecare.dto.request.MedicalRecordRequest;
import vn.edu.hcmute.utecare.dto.response.MedicalRecordResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.exception.NotFoundException;
import vn.edu.hcmute.utecare.mapper.MedicalRecordMapper;
import vn.edu.hcmute.utecare.model.Customer;
import vn.edu.hcmute.utecare.model.MedicalRecord;
import vn.edu.hcmute.utecare.repository.CustomerRepository;
import vn.edu.hcmute.utecare.repository.MedicalRecordRepository;
import vn.edu.hcmute.utecare.service.MedicalRecordService;
import vn.edu.hcmute.utecare.util.PaginationUtil;

import java.time.Year;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MedicalRecordServiceImpl implements MedicalRecordService {
    private final MedicalRecordRepository medicalRecordRepository;
    private final CustomerRepository customerRepository;


    @Override
    public MedicalRecordResponse create(MedicalRecordRequest request) {
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new NotFoundException("Customer not found with id: " + request.getCustomerId()));

        MedicalRecord medicalRecord = MedicalRecordMapper.INSTANCE.toEntity(request);
        medicalRecord.setCustomer(customer);

        String barcode = generateUniqueBarcode();
        medicalRecord.setBarcode(barcode);

        return MedicalRecordMapper.INSTANCE.toResponse(medicalRecordRepository.save(medicalRecord));
    }

    @Override
    public MedicalRecordResponse getById(Long id) {
        MedicalRecord medicalRecord = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Medical record not found with id: " + id));
        return MedicalRecordMapper.INSTANCE.toResponse(medicalRecord);
    }

    @Override
    public MedicalRecordResponse update(Long id, MedicalRecordRequest request) {
        MedicalRecord medicalRecord = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Medical record not found with id: " + id));

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new NotFoundException("Customer not found with id: " + request.getCustomerId()));

        MedicalRecord updatedRecord = MedicalRecordMapper.INSTANCE.toEntity(request);
        medicalRecord.setPatient(updatedRecord.getPatient());
        medicalRecord.setCustomer(customer);

        return MedicalRecordMapper.INSTANCE.toResponse(medicalRecordRepository.save(medicalRecord));
    }

    @Override
    public void delete(Long id) {
        MedicalRecord medicalRecord = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Medical record not found with id: " + id));
        medicalRecordRepository.delete(medicalRecord);
    }

    @Override
    public List<MedicalRecordResponse> getAll() {
        return medicalRecordRepository.findAll()
                .stream()
                .map(MedicalRecordMapper.INSTANCE::toResponse)
                .collect(Collectors.toList());
    }


    @Override
    public PageResponse<MedicalRecordResponse> getAll(int page, int size, String sort, String direction) {
        log.info("Fetching all medical records: page={}, size={}, sort={}, direction={}", page, size, sort, direction);
        Pageable pageable = PaginationUtil.createPageable(page, size, sort, direction);
        Page<MedicalRecord> medicalRecordPage = medicalRecordRepository.findAll(pageable);

        return PageResponse.<MedicalRecordResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(medicalRecordPage.getTotalPages())
                .totalElements(medicalRecordPage.getTotalElements())
                .content(medicalRecordPage.getContent().stream()
                        .map(MedicalRecordMapper.INSTANCE::toResponse)
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
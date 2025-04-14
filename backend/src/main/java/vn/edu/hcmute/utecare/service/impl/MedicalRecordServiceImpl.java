package vn.edu.hcmute.utecare.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import vn.edu.hcmute.utecare.dto.request.MedicalRecordRequest;
import vn.edu.hcmute.utecare.dto.response.MedicalRecordResponse;
import vn.edu.hcmute.utecare.exception.NotFoundException;
import vn.edu.hcmute.utecare.mapper.MedicalRecordMapper;
import vn.edu.hcmute.utecare.model.Customer;
import vn.edu.hcmute.utecare.model.MedicalRecord;
import vn.edu.hcmute.utecare.repository.CustomerRepository;
import vn.edu.hcmute.utecare.repository.MedicalRecordRepository;
import vn.edu.hcmute.utecare.service.MedicalRecordService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicalRecordServiceImpl implements MedicalRecordService {
    private final MedicalRecordRepository medicalRecordRepository;
    private final CustomerRepository customerRepository;
    private final MedicalRecordMapper medicalRecordMapper;

    @Override
    public MedicalRecordResponse create(MedicalRecordRequest request) {
        // Kiểm tra customer tồn tại
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new NotFoundException("Customer not found with id: " + request.getCustomerId()));

        // Kiểm tra barcode duy nhất
        medicalRecordRepository.findByBarcode(request.getBarcode())
                .ifPresent(record -> {
                    throw new IllegalArgumentException("Barcode already exists: " + request.getBarcode());
                });

        MedicalRecord medicalRecord = medicalRecordMapper.toEntity(request);
        medicalRecord.setCustomer(customer);

        // Patient sẽ được tạo tự động nhờ cascade = CascadeType.ALL
        return medicalRecordMapper.toResponse(medicalRecordRepository.save(medicalRecord));
    }

    @Override
    public MedicalRecordResponse getById(Long id) {
        MedicalRecord medicalRecord = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Medical record not found with id: " + id));
        return medicalRecordMapper.toResponse(medicalRecord);
    }

    @Override
    public MedicalRecordResponse update(Long id, MedicalRecordRequest request) {
        MedicalRecord medicalRecord = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Medical record not found with id: " + id));

        // Kiểm tra customer tồn tại
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new NotFoundException("Customer not found with id: " + request.getCustomerId()));

        // Kiểm tra barcode duy nhất (trừ chính nó)
        medicalRecordRepository.findByBarcode(request.getBarcode())
                .ifPresent(record -> {
                    if (!record.getId().equals(id)) {
                        throw new IllegalArgumentException("Barcode already exists: " + request.getBarcode());
                    }
                });

        // Cập nhật thông tin
        MedicalRecord updatedRecord = medicalRecordMapper.toEntity(request);
        medicalRecord.setBarcode(updatedRecord.getBarcode());
        medicalRecord.setPatient(updatedRecord.getPatient()); // Cập nhật Patient
        medicalRecord.setCustomer(customer);

        return medicalRecordMapper.toResponse(medicalRecordRepository.save(medicalRecord));
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
                .map(medicalRecordMapper::toResponse)
                .collect(Collectors.toList());
    }
}
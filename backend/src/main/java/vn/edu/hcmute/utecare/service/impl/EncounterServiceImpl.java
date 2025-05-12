package vn.edu.hcmute.utecare.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.edu.hcmute.utecare.dto.request.EncounterRequest;
import vn.edu.hcmute.utecare.dto.response.EncounterPatientSummaryResponse;
import vn.edu.hcmute.utecare.dto.response.EncounterResponse;
import vn.edu.hcmute.utecare.exception.ResourceNotFoundException;
import vn.edu.hcmute.utecare.mapper.EncounterMapper;
import vn.edu.hcmute.utecare.model.Encounter;
import vn.edu.hcmute.utecare.model.MedicalRecord;
import vn.edu.hcmute.utecare.model.Prescription;
import vn.edu.hcmute.utecare.repository.EncounterRepository;
import vn.edu.hcmute.utecare.repository.MedicalRecordRepository;
import vn.edu.hcmute.utecare.repository.PrescriptionRepository;
import vn.edu.hcmute.utecare.service.EncounterService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class EncounterServiceImpl implements EncounterService {
    private final EncounterRepository encounterRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final EncounterMapper encounterMapper;


    @Override
    @Transactional(rollbackOn = Exception.class)
    public void deleteEncounter(Long id) {
        log.info("Xóa cuộc gặp với ID: {}", id);
        if (!encounterRepository.existsById(id)) {
            throw new ResourceNotFoundException("Không tìm thấy cuộc gặp với ID: " + id);
        }
        encounterRepository.deleteById(id);
        log.info("Xóa cuộc gặp thành công với ID: {}", id);
    }

    @Override
    public List<EncounterResponse> getAllEncounter() {
        log.info("Truy xuất danh sách tất cả cuộc gặp");
        List<Encounter> encounters = encounterRepository.findAll();
        return encounters.stream().map(encounterMapper::toResponse).toList();
    }


    @Override
    public List<EncounterResponse> getAllEncounterByMedicalRecordId(Long medicalRecordId) {
        log.info("Truy xuất danh sách cuộc gặp theo hồ sơ y tế với ID: {}", medicalRecordId);
        List<Encounter> encounters = encounterRepository.findByMedicalRecord_Id(medicalRecordId);
        return encounters.stream().map(encounterMapper::toResponse).toList();
    }

    @Override
    public EncounterResponse getEncounterById(Long id) {
        log.info("Truy xuất cuộc gặp với ID: {}", id);
        Encounter encounter = encounterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy cuộc gặp với ID: " + id));
        log.info("Truy xuất cuộc gặp thành công với ID: {}", id);
        return encounterMapper.toResponse(encounter);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public EncounterResponse createEncounter(EncounterRequest request) {
        log.info("Tạo cuộc gặp mới với thông tin: {}", request);
        Encounter encounter = encounterMapper.toEntity(request);

        MedicalRecord medicalRecord = medicalRecordRepository.findById(request.getMedicalRecordId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy hồ sơ y tế với ID: " + request.getMedicalRecordId()));
        encounter.setMedicalRecord(medicalRecord);

        encounter.setPrescriptions(new HashSet<>());
        Encounter savedEncounter = encounterRepository.save(encounter);
        log.info("Tạo cuộc gặp thành công với ID: {}", savedEncounter.getId());
        return encounterMapper.toResponse(savedEncounter);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public EncounterResponse updateEncounter(Long id, EncounterRequest request) {
        log.info("Cập nhật cuộc gặp với ID: {} và thông tin: {}", id, request);
        Encounter encounter = encounterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy cuộc gặp với ID: " + id));
        encounterMapper.update(request, encounter);

        if (request.getMedicalRecordId() != null) {
            MedicalRecord medicalRecord = medicalRecordRepository.findById(request.getMedicalRecordId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy hồ sơ y tế với ID: " + request.getMedicalRecordId()));
            encounter.setMedicalRecord(medicalRecord);
        }

        Set<Prescription> prescriptions = new HashSet<>();
        if (request.getPrescriptionId() != null && !request.getPrescriptionId().isEmpty()) {
            List<Prescription> prescriptionList = prescriptionRepository.findAllById(request.getPrescriptionId());
            if (prescriptionList.size() != request.getPrescriptionId().size()) {
                throw new EntityNotFoundException("Không tìm thấy một hoặc nhiều đơn thuốc");
            }
            encounter.getPrescriptions().forEach(prescription -> prescription.setEncounter(null));
            encounter.getPrescriptions().clear();

            for (Prescription prescription : prescriptionList) {
                if (prescription.getEncounter() != null && !prescription.getEncounter().equals(encounter)) {
                    throw new IllegalStateException("Đơn thuốc " + prescription.getId() + " đã được liên kết với một cuộc gặp khác");
                }
                prescription.setEncounter(encounter);
                prescriptions.add(prescription);
            }
        } else {
            encounter.getPrescriptions().forEach(prescription -> prescription.setEncounter(null));
            encounter.getPrescriptions().clear();
        }
        encounter.setPrescriptions(prescriptions);

        Encounter updatedEncounter = encounterRepository.save(encounter);
        log.info("Cập nhật cuộc gặp thành công với ID: {}", id);
        return encounterMapper.toResponse(updatedEncounter);
    }

    @Override
    public EncounterPatientSummaryResponse getEncounterPatientSummaryById(Long id) {
        log.info("Truy xuất chi tiết cuộc gặp của bệnh nhân với ID: {}", id);
        Encounter encounter = encounterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy cuộc gặp với ID: " + id));
        MedicalRecord medicalRecord = medicalRecordRepository.findByEncountersId(encounter.getId());
        encounter.setMedicalRecord(medicalRecord);
        log.info("Truy xuất chi tiết cuộc gặp của bệnh nhân thành công với ID: {}", id);
        return encounterMapper.toEncounterPatientResponse(encounter);
    }

    @Override
    public List<EncounterPatientSummaryResponse> getAllEncounterPatientSummaryById(List<Long> id) {
        log.info("Truy xuất chi tiết nhiều cuộc gặp của bệnh nhân với ID: {}", id);

        List<Encounter> encounters = encounterRepository.findAllById(id);
        List<Long> foundIds = encounters.stream().map(Encounter::getId).toList();
        List<Long> missingIds = id.stream().filter(ids -> !foundIds.contains(ids)).toList();
        if (!missingIds.isEmpty()) {
            throw new ResourceNotFoundException("Không tìm thấy các cuộc gặp với ID: " + missingIds);
        }

        return encounters.stream().map(encounter -> {
            MedicalRecord medicalRecord = medicalRecordRepository.findByEncountersId(encounter.getId());
            encounter.setMedicalRecord(medicalRecord);
            return encounterMapper.toEncounterPatientResponse(encounter);
        }).toList();
    }
}
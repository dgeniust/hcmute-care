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
import vn.edu.hcmute.utecare.mapper.MedicalRecordMapper;
import vn.edu.hcmute.utecare.mapper.PrescriptionMapper;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EncounterServiceImpl implements EncounterService {
    private final EncounterRepository encounterRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final PrescriptionRepository prescriptionRepository;
//    @Override
//    public EncounterResponse getEncounterPrescription(Long prescriptionId) {
//        log.info("Get encounter prescription with request {}", prescriptionId);
//        Encounter encounter = encounterRepository.findByPrescription_Id(prescriptionId)
//                .orElseThrow(() -> new ResourceNotFoundException("Prescription id " + prescriptionId +" not found"));
//        return EncounterMapper.INSTANCE.toResponse(encounter);
//    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public void deleteEncounter(Long id) {
        log.info("Delete encounter with id {}", id);
        if(!encounterRepository.existsById(id))
            throw new ResourceNotFoundException("Encounter with id " + id + " not found");
        encounterRepository.deleteById(id);
    }

    @Override
    public List<EncounterResponse> getAllEncounter() {
        log.info("Get all encounters");
        List<Encounter> encounters = encounterRepository.findAll();
        return encounters.stream().map(EncounterMapper.INSTANCE::toResponse).toList();
    }

    @Override
    public List<EncounterResponse> getAllEncounterByMedicalRecordId(Long medicalRecordId) {
        log.info("Get all encounters by medical record with id {}", medicalRecordId);
        List<Encounter> encounters = encounterRepository.findByMedicalRecord_Id(medicalRecordId);
        return encounters.stream().map(EncounterMapper.INSTANCE::toResponse).toList();
    }

    @Override
    public EncounterResponse getEncounterById(Long id) {
        log.info("Get encounter with id {}", id);
        Encounter encounter = encounterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Encounter with id " + id + " not found"));
        return EncounterMapper.INSTANCE.toResponse(encounter);
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public EncounterResponse createEncounter(EncounterRequest request) {
        log.info("Create encounter with request {}", request);
        Encounter encounter = EncounterMapper.INSTANCE.toEntity(request);

        MedicalRecord medicalRecord = medicalRecordRepository.findById(request.getMedicalRecordId()).orElseThrow(() -> new ResourceNotFoundException("Medical record with id " + request.getMedicalRecordId() + " not found"));
        encounter.setMedicalRecord(medicalRecord);

        encounter.setPrescriptions(new HashSet<>());
        Encounter savedEncounter = encounterRepository.save(encounter);

        return EncounterMapper.INSTANCE.toResponse(savedEncounter);
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public EncounterResponse updateEncounter(Long id, EncounterRequest request) {
        log.info("Update encounters by request {}", request);
        Encounter encounter = encounterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Encounter not found with id " + id));
        EncounterMapper.INSTANCE.update(request, encounter);
        // Update MedicalRecord if provided
        if (request.getMedicalRecordId() != null) {
            MedicalRecord medicalRecord = medicalRecordRepository.findById(request.getMedicalRecordId())
                    .orElseThrow(() -> new ResourceNotFoundException("Medical record with id " + request.getMedicalRecordId() + " not found"));
            encounter.setMedicalRecord(medicalRecord);
        }

        // Update Prescriptions
        Set<Prescription> prescriptions = new HashSet<>();
        if (request.getPrescriptionId() != null && !request.getPrescriptionId().isEmpty()) {
            List<Prescription> prescriptionList = prescriptionRepository.findAllById(request.getPrescriptionId());
            if (prescriptionList.size() != request.getPrescriptionId().size()) {
                throw new EntityNotFoundException("One or more Prescriptions not found");
            }
            // Clear existing prescriptions (optional, depending on your requirements)
            encounter.getPrescriptions().forEach(prescription -> prescription.setEncounter(null));
            encounter.getPrescriptions().clear();

            // Set new prescriptions
            for (Prescription prescription : prescriptionList) {
                if (prescription.getEncounter() != null && !prescription.getEncounter().equals(encounter)) {
                    throw new IllegalStateException("Prescription " + prescription.getId() + " is already associated with another Encounter");
                }
                prescription.setEncounter(encounter); // Set the owning side
                prescriptions.add(prescription);
            }
        } else {
            // If prescriptionId is null or empty, clear prescriptions
            encounter.getPrescriptions().forEach(prescription -> prescription.setEncounter(null));
            encounter.getPrescriptions().clear();
        }
        encounter.setPrescriptions(prescriptions);

        Encounter updatedEncounter = encounterRepository.save(encounter);
        return EncounterMapper.INSTANCE.toResponse(updatedEncounter);
    }

    @Override
    public EncounterPatientSummaryResponse getEncounterPatientSummaryById(Long id) {
        log.info("Get detail encounter patient summary with id {}", id);
        Encounter encounter = encounterRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Encounter with id " + id + " not found"));
        MedicalRecord medicalRecord = medicalRecordRepository.findByEncountersId(encounter.getId());
        encounter.setMedicalRecord(medicalRecord);

        return EncounterMapper.INSTANCE.toEncounterPatientResponse(encounter);
    }

    @Override
    public List<EncounterPatientSummaryResponse> getAllEncounterPatientSummaryById(List<Long> id) {
        log.info("Get detail encounter patient summaries for ids {}", id);

        List<Encounter> encounters = encounterRepository.findAllById(id);
        // Check for missing Encounters
        List<Long> foundIds = encounters.stream().map(Encounter::getId).toList();
        List<Long> missingIds = id.stream().filter(ids -> !foundIds.contains(ids)).toList();
        if (!missingIds.isEmpty()) {
            throw new ResourceNotFoundException("Encounters with ids " + missingIds + " not found");
        }
        // Map Encounters to EncounterPatientSummaryResponse
        // Set MedicalRecord for mapping
        return encounters.stream().map(encounter -> {
            MedicalRecord medicalRecord = medicalRecordRepository.findByEncountersId(encounter.getId());
            encounter.setMedicalRecord(medicalRecord); // Set MedicalRecord for mapping
            return EncounterMapper.INSTANCE.toEncounterPatientResponse(encounter);
        }).toList();
    }
}

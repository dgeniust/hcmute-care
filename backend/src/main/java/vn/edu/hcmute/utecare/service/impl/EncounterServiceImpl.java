package vn.edu.hcmute.utecare.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.edu.hcmute.utecare.dto.request.EncounterRequest;
import vn.edu.hcmute.utecare.dto.response.EncounterResponse;
import vn.edu.hcmute.utecare.exception.ResourceNotFoundException;
import vn.edu.hcmute.utecare.mapper.EncounterMapper;
import vn.edu.hcmute.utecare.model.Encounter;
import vn.edu.hcmute.utecare.repository.EncounterRepository;
import vn.edu.hcmute.utecare.service.EncounterService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EncounterServiceImpl implements EncounterService {
    private final EncounterRepository encounterRepository;
    @Override
    public EncounterResponse getEncounterPrescription(Long prescriptionId) {
        log.info("Get encounter prescription with request {}", prescriptionId);
        Encounter encounter = encounterRepository.findByPrescription_Id(prescriptionId)
                .orElseThrow(() -> new ResourceNotFoundException("Prescription id " + prescriptionId +" not found"));
        return EncounterMapper.INSTANCE.toResponse(encounter);
    }

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

    @Override
    public EncounterResponse createEncounter(EncounterRequest request) {
//        log.info("Create encounter with request {}", request);
//        Encounter encounter = EncounterMapper.INSTANCE.toEntity(request);
//        return EncounterMapper.INSTANCE.toResponse(encounterRepository.save(encounter));
        return null;
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public EncounterResponse updateEncounter(Long id, EncounterRequest request) {
        log.info("Update encounters by request {}", request);
        Encounter encounter = encounterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Encounter not found with id " + id));
        EncounterMapper.INSTANCE.update(request, encounter);
        Encounter updatedEncounter = encounterRepository.save(encounter);
        return EncounterMapper.INSTANCE.toResponse(updatedEncounter);
    }
}

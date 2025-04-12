package vn.edu.hcmute.utecare.service;

import vn.edu.hcmute.utecare.dto.request.EncounterRequest;
import vn.edu.hcmute.utecare.dto.response.EncounterResponse;

import java.util.List;

public interface EncounterService {

    EncounterResponse getEncounterPrescription(Long prescriptionId);

    void deleteEncounter(Long id);

    List<EncounterResponse> getAllEncounter();

    List<EncounterResponse> getAllEncounterByMedicalRecordId(Long medicalRecordId);

    EncounterResponse getEncounterById(Long id);

    EncounterResponse createEncounter(EncounterRequest request);

    EncounterResponse updateEncounter(Long id, EncounterRequest request);
}

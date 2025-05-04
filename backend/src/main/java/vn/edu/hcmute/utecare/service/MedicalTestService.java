package vn.edu.hcmute.utecare.service;


import vn.edu.hcmute.utecare.dto.request.MedicalTestRequest;
import vn.edu.hcmute.utecare.dto.response.MedicalTestResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;

import java.time.LocalDate;
import java.util.List;

public interface MedicalTestService {

    MedicalTestResponse createMedicalTest(MedicalTestRequest request);

    MedicalTestResponse getMedicalTestById(Long id);

    List<MedicalTestResponse> getAll();

    PageResponse<MedicalTestResponse> getAll(int page, int size, String sort, String direction);

    MedicalTestResponse updateMedicalTest(Long id, MedicalTestRequest request);

    void deleteMedicalTest(Long id);

    List<MedicalTestResponse> findByEncounterAndDate(Long encounterId, LocalDate date);
}
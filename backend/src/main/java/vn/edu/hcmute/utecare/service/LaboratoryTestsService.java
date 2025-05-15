package vn.edu.hcmute.utecare.service;

import vn.edu.hcmute.utecare.dto.request.LaboratoryTestsRequest;
import vn.edu.hcmute.utecare.dto.response.LaboratoryTestsResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.util.enumeration.EMedicalTest;

import java.time.LocalDate;
import java.util.List;

public interface LaboratoryTestsService {

    LaboratoryTestsResponse createLaboratoryTests(LaboratoryTestsRequest request);

    LaboratoryTestsResponse getLaboratoryTestsById(Long id);

    List<LaboratoryTestsResponse> getAll();

    PageResponse<LaboratoryTestsResponse> getAll(int page, int size, String sort, String direction);

    LaboratoryTestsResponse updateLaboratoryTests(Long id, LaboratoryTestsRequest request);

    void deleteLaboratoryTests(Long id);

    List<LaboratoryTestsResponse> getAllLabTestByDateAndStatus(LocalDate date, String status);

    List<LaboratoryTestsResponse> getEncounterIdAndDate(Long encounterId, LocalDate date);
}
package vn.edu.hcmute.utecare.service;

import vn.edu.hcmute.utecare.dto.request.LaboratoryTestsRequest;
import vn.edu.hcmute.utecare.dto.response.LaboratoryTestsResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;

import java.util.List;

public interface LaboratoryTestsService {

    LaboratoryTestsResponse createLaboratoryTests(LaboratoryTestsRequest request);

    LaboratoryTestsResponse getLaboratoryTestsById(Long id);

    List<LaboratoryTestsResponse> getAll();

    PageResponse<LaboratoryTestsResponse> getAll(int page, int size, String sort, String direction);

    LaboratoryTestsResponse updateLaboratoryTests(Long id, LaboratoryTestsRequest request);

    void deleteLaboratoryTests(Long id);
}
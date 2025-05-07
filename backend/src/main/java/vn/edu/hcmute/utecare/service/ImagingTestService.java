package vn.edu.hcmute.utecare.service;

import vn.edu.hcmute.utecare.dto.request.ImagingTestRequest;
import vn.edu.hcmute.utecare.dto.response.ImagingTestResponse;
import vn.edu.hcmute.utecare.dto.response.LaboratoryTestsResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;

import java.time.LocalDate;
import java.util.List;

public interface ImagingTestService {

    ImagingTestResponse createImagingTest(ImagingTestRequest request);

    ImagingTestResponse getImagingTestById(Long id);

    List<ImagingTestResponse> getAll();

    PageResponse<ImagingTestResponse> getAll(int page, int size, String sort, String direction);

    ImagingTestResponse updateImagingTest(Long id, ImagingTestRequest request);

    void deleteImagingTest(Long id);

    List<ImagingTestResponse> getAllImagingTestByDateAndStatus(LocalDate date, String status);

}
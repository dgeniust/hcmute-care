package vn.edu.hcmute.utecare.service;

import vn.edu.hcmute.utecare.dto.request.NerveConductionRequest;
import vn.edu.hcmute.utecare.dto.response.DigestiveTestResponse;
import vn.edu.hcmute.utecare.dto.response.NerveConductionResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;

import java.time.LocalDate;
import java.util.List;

public interface NerveConductionService {
    NerveConductionResponse createNerveConduction(NerveConductionRequest request);

    NerveConductionResponse getNerveConductionById(Long id);

    List<NerveConductionResponse> getAll();

    PageResponse<NerveConductionResponse> getAll(int page, int size, String sort, String direction);

    NerveConductionResponse updateNerveConduction(Long id, NerveConductionRequest request);

    void deleteNerveConduction(Long id);

    List<NerveConductionResponse> getAllLabTestByDateAndStatus(LocalDate date, String status);

}
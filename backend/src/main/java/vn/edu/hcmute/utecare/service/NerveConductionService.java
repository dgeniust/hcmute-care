package vn.edu.hcmute.utecare.service;

import vn.edu.hcmute.utecare.dto.request.NerveConductionRequest;
import vn.edu.hcmute.utecare.dto.response.NerveConductionResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;

import java.util.List;

public interface NerveConductionService {
    NerveConductionResponse createNerveConduction(NerveConductionRequest request);

    NerveConductionResponse getNerveConductionById(Long id);

    List<NerveConductionResponse> getAll();

    PageResponse<NerveConductionResponse> getAll(int page, int size, String sort, String direction);

    NerveConductionResponse updateNerveConduction(Long id, NerveConductionRequest request);

    void deleteNerveConduction(Long id);
}
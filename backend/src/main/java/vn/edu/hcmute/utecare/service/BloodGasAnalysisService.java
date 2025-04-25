package vn.edu.hcmute.utecare.service;

import vn.edu.hcmute.utecare.dto.request.BloodGasAnalysisRequest;
import vn.edu.hcmute.utecare.dto.response.BloodGasAnalysisResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;

import java.util.List;

public interface BloodGasAnalysisService {
    BloodGasAnalysisResponse createBloodGasAnalysis(BloodGasAnalysisRequest request);

    BloodGasAnalysisResponse getBloodGasAnalysisById(Long id);

    List<BloodGasAnalysisResponse> getAll();

    PageResponse<BloodGasAnalysisResponse> getAll(int page, int size, String sort, String direction);

    BloodGasAnalysisResponse updateBloodGasAnalysis(Long id, BloodGasAnalysisRequest request);

    void deleteBloodGasAnalysis(Long id);
}

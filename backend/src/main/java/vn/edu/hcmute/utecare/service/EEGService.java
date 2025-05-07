package vn.edu.hcmute.utecare.service;

import vn.edu.hcmute.utecare.dto.request.EEGRequest;
import vn.edu.hcmute.utecare.dto.response.DigestiveTestResponse;
import vn.edu.hcmute.utecare.dto.response.EEGResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;

import java.time.LocalDate;
import java.util.List;

public interface EEGService {
    EEGResponse createEEG(EEGRequest request);

    EEGResponse getEEGById(Long id);

    List<EEGResponse> getAll();

    PageResponse<EEGResponse> getAll(int page, int size, String sort, String direction);

    EEGResponse updateEEG(Long id, EEGRequest request);

    void deleteEEG(Long id);

    List<EEGResponse> getAllLabTestByDateAndStatus(LocalDate date, String status);

}

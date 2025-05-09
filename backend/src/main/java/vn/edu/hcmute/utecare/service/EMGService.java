package vn.edu.hcmute.utecare.service;

import vn.edu.hcmute.utecare.dto.request.EMGRequest;
import vn.edu.hcmute.utecare.dto.response.DigestiveTestResponse;
import vn.edu.hcmute.utecare.dto.response.EMGResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;

import java.time.LocalDate;
import java.util.List;

public interface EMGService {
    EMGResponse createEMG(EMGRequest request);

    EMGResponse getEMGById(Long id);

    List<EMGResponse> getAll();

    PageResponse<EMGResponse> getAll(int page, int size, String sort, String direction);

    EMGResponse updateEMG(Long id, EMGRequest request);

    void deleteEMG(Long id);

    List<EMGResponse> getAllLabTestByDateAndStatus(LocalDate date, String status);

    List<EMGResponse> getEncounterIdAndDate(Long encounterId, LocalDate date);
}

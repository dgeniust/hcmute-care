package vn.edu.hcmute.utecare.service;


import vn.edu.hcmute.utecare.dto.request.DigestiveTestRequest;
import vn.edu.hcmute.utecare.dto.response.CardiacTestResponse;
import vn.edu.hcmute.utecare.dto.response.DigestiveTestResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;

import java.time.LocalDate;
import java.util.List;

public interface DigestiveTestService {

    DigestiveTestResponse createDigestiveTest(DigestiveTestRequest request);

    DigestiveTestResponse getDigestiveTestById(Long id);

    List<DigestiveTestResponse> getAll();

    PageResponse<DigestiveTestResponse> getAll(int page, int size, String sort, String direction);


    DigestiveTestResponse updateDigestiveTest(Long id, DigestiveTestRequest request);

    void deleteDigestiveTest(Long id);

    List<DigestiveTestResponse> getAllLabTestByDateAndStatus(LocalDate date, String status);

    List<DigestiveTestResponse> getEncounterIdAndDate(Long encounterId, LocalDate date);
}
package vn.edu.hcmute.utecare.service;

import vn.edu.hcmute.utecare.dto.request.CardiacTestRequest;
import vn.edu.hcmute.utecare.dto.response.CardiacTestResponse;
import vn.edu.hcmute.utecare.dto.response.LaboratoryTestsResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;

import java.time.LocalDate;
import java.util.List;

public interface CardiacTestService {

    CardiacTestResponse createCardiacTest(CardiacTestRequest request);

    CardiacTestResponse getCardiacTestById(Long id);

    List<CardiacTestResponse> getAll();

    PageResponse<CardiacTestResponse> getAll(int page, int size, String sort, String direction);

    CardiacTestResponse updateCardiacTest(Long id, CardiacTestRequest request);

    void deleteCardiacTest(Long id);

    List<CardiacTestResponse> getAllLabTestByDateAndStatus(LocalDate date, String status);

    List<CardiacTestResponse> getEncounterIdAndDate(Long encounterId, LocalDate date);
}
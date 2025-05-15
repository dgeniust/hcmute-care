package vn.edu.hcmute.utecare.service;

import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.utecare.dto.request.SpirometryRequest;
import vn.edu.hcmute.utecare.dto.response.DigestiveTestResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.dto.response.SpirometryResponse;

import java.time.LocalDate;
import java.util.List;

public interface SpirometryService {
    SpirometryResponse createSpirometry(SpirometryRequest request);

    SpirometryResponse getSpirometryById(Long id);

    List<SpirometryResponse> getAll();

    PageResponse<SpirometryResponse> getAll(int page, int size, String sort, String direction);

    SpirometryResponse updateSpirometry(Long id, SpirometryRequest request);

    void deleteSpirometry(Long id);

    List<SpirometryResponse> getAllLabTestByDateAndStatus(LocalDate date, String status);

    List<SpirometryResponse> getEncounterIdAndDate(Long encounterId, LocalDate date);
}

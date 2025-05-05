package vn.edu.hcmute.utecare.service;

import vn.edu.hcmute.utecare.dto.request.MedicalRecordRequest;
import vn.edu.hcmute.utecare.dto.response.EncounterResponse;
import vn.edu.hcmute.utecare.dto.response.MedicalRecordResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;

import java.util.List;

public interface MedicalRecordService {
    MedicalRecordResponse create(MedicalRecordRequest request);
    MedicalRecordResponse getById(Long id);

    MedicalRecordResponse getByBarcodeAndCustomerId(String barcode, Long customerId);

    List<MedicalRecordResponse> getAll();
    PageResponse<MedicalRecordResponse> getAll(int page, int size, String sort, String direction);
    MedicalRecordResponse update(Long id, MedicalRecordRequest request);
    void delete(Long id);
    List<EncounterResponse> getAllEncounterByMedicalRecordId(Long medicalRecordId);
}
package vn.edu.hcmute.utecare.service;

import vn.edu.hcmute.utecare.dto.request.MedicalRecordRequest;
import vn.edu.hcmute.utecare.dto.response.MedicalRecordResponse;

import java.util.List;

public interface MedicalRecordService {
    MedicalRecordResponse create(MedicalRecordRequest request);
    MedicalRecordResponse getById(Long id);
    List<MedicalRecordResponse> getAll();
    MedicalRecordResponse update(Long id, MedicalRecordRequest request);
    void delete(Long id);
}
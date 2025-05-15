package vn.edu.hcmute.utecare.service;

import vn.edu.hcmute.utecare.dto.request.MedicalSpecialtyRequest;
import vn.edu.hcmute.utecare.dto.response.DoctorResponse;
import vn.edu.hcmute.utecare.dto.response.MedicalSpecialtyResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;

public interface MedicalSpecialtyService {
    MedicalSpecialtyResponse getMedicalSpecialtyById(Integer id);

    MedicalSpecialtyResponse createMedicalSpecialty(MedicalSpecialtyRequest request);

    MedicalSpecialtyResponse updateMedicalSpecialty(Integer id, MedicalSpecialtyRequest request);

    void deleteMedicalSpecialty(Integer id);

    PageResponse<MedicalSpecialtyResponse> getAllMedicalSpecialties(int page, int size, String sort, String direction);

    PageResponse<MedicalSpecialtyResponse> searchMedicalSpecialties(String keyword, int page, int size, String sort, String direction);
}

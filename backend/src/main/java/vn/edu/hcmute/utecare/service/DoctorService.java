package vn.edu.hcmute.utecare.service;

import jakarta.transaction.Transactional;
import vn.edu.hcmute.utecare.dto.request.DoctorCreationRequest;
import vn.edu.hcmute.utecare.dto.request.DoctorRequest;
import vn.edu.hcmute.utecare.dto.response.DoctorResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;

public interface DoctorService {
    DoctorResponse createDoctor(DoctorCreationRequest request);

    DoctorResponse getDoctorById(Long id);

    DoctorResponse updateDoctor(Long id, DoctorRequest request);

    void deleteDoctor(Long id);

    PageResponse<DoctorResponse> getAllDoctors(int page, int size, String sort, String direction);

    PageResponse<DoctorResponse> searchDoctors(String keyword, int page, int size, String sort, String direction);
}

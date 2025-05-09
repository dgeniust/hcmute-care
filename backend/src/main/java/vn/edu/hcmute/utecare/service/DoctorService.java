package vn.edu.hcmute.utecare.service;

import vn.edu.hcmute.utecare.dto.request.DoctorRequest;
import vn.edu.hcmute.utecare.dto.response.DoctorResponse;
import vn.edu.hcmute.utecare.dto.response.MedicalTestDetailResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.model.MedicalTest;

import java.time.LocalDate;
import java.util.List;

public interface DoctorService {
    DoctorResponse createDoctor(DoctorRequest request);

    DoctorResponse getDoctorById(Long id);

    DoctorResponse updateDoctor(Long id, DoctorRequest request);

    void deleteDoctor(Long id);

    PageResponse<DoctorResponse> getAllDoctors(int page, int size, String sort, String direction);

    PageResponse<DoctorResponse> searchDoctors(String keyword, int page, int size, String sort, String direction);

    PageResponse<DoctorResponse> getDoctorsByMedicalSpecialtyId(Integer id, int page, int size, String sort, String direction);

    PageResponse<DoctorResponse> searchDoctorsByMedicalSpecialtyId(Integer id, String keyword, int page, int size, String sort, String direction);

    List<MedicalTestDetailResponse> getMedicalTestsByPatientId(Long patientId, LocalDate date);

    List<MedicalTestDetailResponse> getMedicalTestsByPatientId(Long patientId);

    MedicalTestDetailResponse mapToDetailResponse(MedicalTest medicalTest);
}

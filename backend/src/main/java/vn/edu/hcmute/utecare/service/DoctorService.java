package vn.edu.hcmute.utecare.service;

import vn.edu.hcmute.utecare.dto.request.DoctorRequest;
import vn.edu.hcmute.utecare.dto.response.DoctorResponse;
import vn.edu.hcmute.utecare.dto.response.ScheduleSummaryResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;

import java.time.LocalDate;

public interface DoctorService {
    DoctorResponse createDoctor(DoctorRequest request);

    DoctorResponse getDoctorById(Long id);

    DoctorResponse updateDoctor(Long id, DoctorRequest request);

    void deleteDoctor(Long id);

    PageResponse<DoctorResponse> getAllDoctors(int page, int size, String sort, String direction);

    PageResponse<DoctorResponse> searchDoctors(String keyword, int page, int size, String sort, String direction);

    PageResponse<ScheduleSummaryResponse> getDoctorAvailability(Long id, LocalDate date, int page, int size, String sort, String direction);
}

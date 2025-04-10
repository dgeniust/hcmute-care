package vn.edu.hcmute.utecare.service;

import jakarta.transaction.Transactional;
import vn.edu.hcmute.utecare.dto.request.DoctorScheduleRequest;
import vn.edu.hcmute.utecare.dto.response.DoctorScheduleResponse;
import vn.edu.hcmute.utecare.dto.response.DoctorScheduleSummaryResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;

import java.time.LocalDate;

public interface DoctorScheduleService {
    DoctorScheduleSummaryResponse createDoctorSchedule(DoctorScheduleRequest request);

    DoctorScheduleResponse getDoctorScheduleById(Long id);

    DoctorScheduleSummaryResponse updateDoctorSchedule(Long id, DoctorScheduleRequest request);

    void deleteDoctorSchedule(Long id);

    PageResponse<DoctorScheduleSummaryResponse> getAllDoctorSchedules(int page, int size, String sort, String direction);

    PageResponse<DoctorScheduleSummaryResponse> searchDoctorSchedules(Long doctorId, LocalDate date, Integer timeSlotId, int page, int size, String sort, String direction);
}

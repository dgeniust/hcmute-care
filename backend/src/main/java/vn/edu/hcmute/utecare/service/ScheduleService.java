package vn.edu.hcmute.utecare.service;

import vn.edu.hcmute.utecare.dto.request.ScheduleRequest;
import vn.edu.hcmute.utecare.dto.response.ScheduleResponse;
import vn.edu.hcmute.utecare.dto.response.ScheduleSummaryResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;

import java.time.LocalDate;

public interface ScheduleService {
    ScheduleSummaryResponse createDoctorSchedule(ScheduleRequest request);

    ScheduleResponse getDoctorScheduleById(Long id);

    ScheduleSummaryResponse updateDoctorSchedule(Long id, ScheduleRequest request);

    void deleteDoctorSchedule(Long id);

    PageResponse<ScheduleSummaryResponse> getAllDoctorSchedules(int page, int size, String sort, String direction);

    PageResponse<ScheduleSummaryResponse> searchDoctorSchedules(Long doctorId, LocalDate date, Integer timeSlotId, int page, int size, String sort, String direction);
}

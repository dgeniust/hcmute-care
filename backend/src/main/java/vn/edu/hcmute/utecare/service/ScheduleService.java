package vn.edu.hcmute.utecare.service;

import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.utecare.dto.request.ScheduleRequest;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.dto.response.ScheduleInfoResponse;
import vn.edu.hcmute.utecare.dto.response.ScheduleResponse;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleService {
    ScheduleResponse createSchedule(ScheduleRequest request);

    @Transactional
    ScheduleResponse updateSchedule(Long id, ScheduleRequest request);

    @Transactional
    void deleteSchedule(Long id);

    @Transactional(readOnly = true)
    ScheduleResponse getScheduleById(Long id);

    @Transactional(readOnly = true)
    PageResponse<ScheduleResponse> getAllSchedules(
            Long doctorId,
            Integer roomId,
            LocalDate startDate,
            LocalDate endDate,
            Integer page, Integer size, String sort, String direction);

    @Transactional(readOnly = true)
    List<ScheduleInfoResponse> getAvailableSchedules(
            Integer medicalSpecialtyId,
            LocalDate date
    );

    @Transactional(readOnly = true)
    PageResponse<ScheduleResponse> getDoctorSchedules(
            Long doctorId,
            LocalDate startDate,
            LocalDate endDate,
            Integer page, Integer size, String sort, String direction);

    ScheduleResponse getDoctorSchedule(Long id, LocalDate date);
}

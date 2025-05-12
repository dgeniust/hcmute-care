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

    ScheduleResponse updateSchedule(Long id, ScheduleRequest request);

    void deleteSchedule(Long id);

    ScheduleResponse getScheduleById(Long id);

    PageResponse<ScheduleResponse> getAllSchedules(
            Long doctorId,
            LocalDate startDate,
            LocalDate endDate,
            Integer page, Integer size, String sort, String direction);

    List<ScheduleInfoResponse> getAvailableSchedules(
            Integer medicalSpecialtyId,
            LocalDate date
    );

    PageResponse<ScheduleResponse> getDoctorSchedules(
            Long doctorId,
            LocalDate startDate,
            LocalDate endDate,
            Integer page, Integer size, String sort, String direction);

    ScheduleResponse getDoctorSchedule(Long id, LocalDate date);
}

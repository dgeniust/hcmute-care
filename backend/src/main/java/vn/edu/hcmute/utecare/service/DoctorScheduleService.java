package vn.edu.hcmute.utecare.service;

import jakarta.transaction.Transactional;
import vn.edu.hcmute.utecare.dto.request.DoctorScheduleRequest;
import vn.edu.hcmute.utecare.dto.response.DoctorScheduleResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;

import java.time.LocalDate;

public interface DoctorScheduleService {
    @Transactional(rollbackOn = Exception.class)
    DoctorScheduleResponse createDoctorSchedule(DoctorScheduleRequest request);

    DoctorScheduleResponse getDoctorScheduleById(Long id);

    @Transactional
    DoctorScheduleResponse updateDoctorSchedule(Long id, DoctorScheduleRequest request);

    @Transactional
    void deleteDoctorSchedule(Long id);

    PageResponse<DoctorScheduleResponse> getAllDoctorSchedules(int page, int size, String sort, String direction);

    PageResponse<DoctorScheduleResponse> searchDoctorSchedules(Long doctorId, LocalDate date, Integer timeSlotId, int page, int size, String sort, String direction);
}

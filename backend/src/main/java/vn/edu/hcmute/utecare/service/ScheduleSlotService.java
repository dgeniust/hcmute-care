package vn.edu.hcmute.utecare.service;

import vn.edu.hcmute.utecare.dto.response.ScheduleSlotResponse;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleSlotService {
    List<ScheduleSlotResponse> getAllScheduleSlotsByDoctorIdAndDate(Long doctorId, LocalDate date);
}

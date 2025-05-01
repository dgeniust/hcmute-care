package vn.edu.hcmute.utecare.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.edu.hcmute.utecare.dto.response.ScheduleSlotResponse;
import vn.edu.hcmute.utecare.mapper.ScheduleSlotMapper;
import vn.edu.hcmute.utecare.model.ScheduleSlot;
import vn.edu.hcmute.utecare.repository.ScheduleSlotRepository;
import vn.edu.hcmute.utecare.service.ScheduleSlotService;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleSlotServiceImpl implements ScheduleSlotService {
    private final ScheduleSlotRepository scheduleSlotRepository;

    @Override
    public List<ScheduleSlotResponse> getAllScheduleSlotsByDoctorIdAndDate(Long doctorId, LocalDate date) {
        log.info("Getting all schedule slots for doctor ID: {} on date: {}", doctorId, date);
        List<ScheduleSlot> scheduleSlots = scheduleSlotRepository.findBySchedule_Doctor_IdAndSchedule_Date(doctorId,date);

        return scheduleSlots.stream().map(ScheduleSlotMapper.INSTANCE::toResponse).toList();
    }
}

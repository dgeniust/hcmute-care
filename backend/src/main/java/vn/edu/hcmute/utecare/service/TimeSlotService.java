package vn.edu.hcmute.utecare.service;

import vn.edu.hcmute.utecare.dto.request.TimeSlotRequest;
import vn.edu.hcmute.utecare.dto.response.TimeSlotResponse;

import java.util.List;

public interface TimeSlotService {
    TimeSlotResponse createTimeSlot(TimeSlotRequest request);

    TimeSlotResponse updateTimeSlot(Integer id, TimeSlotRequest request);

    void deleteTimeSlot(Integer id);

    TimeSlotResponse getTimeSlotById(Integer id);

    List<TimeSlotResponse> getAllTimeSlots();
}

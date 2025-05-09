package vn.edu.hcmute.utecare.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.edu.hcmute.utecare.dto.request.TimeSlotRequest;
import vn.edu.hcmute.utecare.dto.response.TimeSlotResponse;
import vn.edu.hcmute.utecare.exception.ResourceNotFoundException;
import vn.edu.hcmute.utecare.mapper.TimeSlotMapper;
import vn.edu.hcmute.utecare.model.TimeSlot;
import vn.edu.hcmute.utecare.repository.TimeSlotRepository;
import vn.edu.hcmute.utecare.service.TimeSlotService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TimeSlotServiceImpl implements TimeSlotService {
    private final TimeSlotRepository timeSlotRepository;
    private final TimeSlotMapper TimeSlotMapper;

    @Override
    public TimeSlotResponse createTimeSlot(TimeSlotRequest request){
        log.info("Creating time slot with request: {}", request);
        TimeSlot timeSlot = TimeSlotMapper.toEntity(request);
        return TimeSlotMapper.toResponse(timeSlotRepository.save(timeSlot));
    }

    @Override
    public TimeSlotResponse updateTimeSlot(Integer id, TimeSlotRequest request) {
        log.info("Updating time slot with ID: {} and request: {}", id, request);
        TimeSlot timeSlot = timeSlotRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Time slot not found with ID: " + id));
        TimeSlotMapper.update(request, timeSlot);
        return TimeSlotMapper.toResponse(timeSlotRepository.save(timeSlot));
    }

    @Override
    public void deleteTimeSlot(Integer id) {
        log.info("Deleting time slot with ID: {}", id);
        TimeSlot timeSlot = timeSlotRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Time slot not found with ID: " + id));
        timeSlotRepository.delete(timeSlot);
    }

    @Override
    public TimeSlotResponse getTimeSlotById(Integer id) {
        log.info("Getting time slot with ID: {}", id);
        TimeSlot timeSlot = timeSlotRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Time slot not found with ID: " + id));
        return TimeSlotMapper.toResponse(timeSlot);
    }

    @Override
    public List<TimeSlotResponse> getAllTimeSlots() {
        log.info("Getting all time slots");
        List<TimeSlot> timeSlots = timeSlotRepository.findAll();
        return timeSlots.stream()
                .map(TimeSlotMapper::toResponse)
                .toList();
    }
}

package vn.edu.hcmute.utecare.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    private final TimeSlotMapper timeSlotMapper;

    @Override
    @Transactional
    public TimeSlotResponse createTimeSlot(TimeSlotRequest request) {
        log.info("Tạo khung giờ mới với thông tin: {}", request);
        TimeSlot timeSlot = timeSlotMapper.toEntity(request);
        TimeSlot savedTimeSlot = timeSlotRepository.save(timeSlot);
        log.info("Tạo khung giờ thành công với ID: {}", savedTimeSlot.getId());
        return timeSlotMapper.toResponse(savedTimeSlot);
    }

    @Override
    @Transactional
    public TimeSlotResponse updateTimeSlot(Integer id, TimeSlotRequest request) {
        log.info("Cập nhật khung giờ với ID: {} và thông tin: {}", id, request);
        TimeSlot timeSlot = timeSlotRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy khung giờ với ID: " + id));
        timeSlotMapper.update(request, timeSlot);
        TimeSlot updatedTimeSlot = timeSlotRepository.save(timeSlot);
        log.info("Cập nhật khung giờ thành công với ID: {}", id);
        return timeSlotMapper.toResponse(updatedTimeSlot);
    }

    @Override
    @Transactional
    public void deleteTimeSlot(Integer id) {
        log.info("Xóa khung giờ với ID: {}", id);
        TimeSlot timeSlot = timeSlotRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy khung giờ với ID: " + id));
        timeSlotRepository.delete(timeSlot);
        log.info("Xóa khung giờ thành công với ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public TimeSlotResponse getTimeSlotById(Integer id) {
        log.info("Truy xuất khung giờ với ID: {}", id);
        TimeSlot timeSlot = timeSlotRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy khung giờ với ID: " + id));
        log.info("Truy xuất khung giờ thành công với ID: {}", id);
        return timeSlotMapper.toResponse(timeSlot);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TimeSlotResponse> getAllTimeSlots() {
        log.info("Truy xuất danh sách tất cả khung giờ");
        List<TimeSlot> timeSlots = timeSlotRepository.findAll();
        return timeSlots.stream()
                .map(timeSlotMapper::toResponse)
                .toList();
    }
}
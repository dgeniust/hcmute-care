package vn.edu.hcmute.utecare.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmute.utecare.dto.request.TimeSlotRequest;
import vn.edu.hcmute.utecare.dto.response.ResponseData;
import vn.edu.hcmute.utecare.dto.response.TimeSlotResponse;
import vn.edu.hcmute.utecare.service.TimeSlotService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/time-slots")
@RequiredArgsConstructor
@Slf4j(topic = "TIME_SLOT_CONTROLLER")
@Tag(name = "Time Slot", description = "Time Slot Management API")
public class TimeSlotController {
    private final TimeSlotService timeSlotService;

    @PostMapping
    @Operation(summary = "Create time slots", description = "Create time slots for the current semester")
    public ResponseData<TimeSlotResponse> createTimeSlots(@RequestBody TimeSlotRequest request) {
        log.info("Create time slots request: {}", request);
        return ResponseData.<TimeSlotResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Time slots created successfully")
                .data(timeSlotService.createTimeSlot(request))
                .build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update time slots", description = "Update time slots for the current semester")
    public ResponseData<TimeSlotResponse> updateTimeSlots(
            @PathVariable("id") Integer id,
            @RequestBody TimeSlotRequest request) {
        log.info("Update time slots request: {}", request);
        return ResponseData.<TimeSlotResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Time slots updated successfully")
                .data(timeSlotService.updateTimeSlot(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete time slots", description = "Delete time slots for the current semester")
    public ResponseData<Void> deleteTimeSlots(@PathVariable("id") Integer id) {
        log.info("Delete time slots request for id: {}", id);
        timeSlotService.deleteTimeSlot(id);
        return ResponseData.<Void>builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message("Time slots deleted successfully")
                .build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get time slots by ID", description = "Retrieve time slots by ID")
    public ResponseData<TimeSlotResponse> getTimeSlotsById(@PathVariable("id") Integer id) {
        log.info("Get time slots request for id: {}", id);
        return ResponseData.<TimeSlotResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Time slots retrieved successfully")
                .data(timeSlotService.getTimeSlotById(id))
                .build();
    }

    @GetMapping
    @Operation(summary = "Get all time slots", description = "Retrieve all time slots")
    public ResponseData<List<TimeSlotResponse>> getAllTimeSlots() {
        log.info("Get all time slots request");
        return ResponseData.<List<TimeSlotResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("All time slots retrieved successfully")
                .data(timeSlotService.getAllTimeSlots())
                .build();
    }
}

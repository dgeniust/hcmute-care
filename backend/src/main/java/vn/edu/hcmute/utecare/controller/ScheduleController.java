package vn.edu.hcmute.utecare.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmute.utecare.dto.request.ScheduleRequest;
import vn.edu.hcmute.utecare.dto.response.ScheduleResponse;
import vn.edu.hcmute.utecare.dto.response.ScheduleSummaryResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.dto.response.ResponseData;
import vn.edu.hcmute.utecare.service.ScheduleService;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/schedules")
@RequiredArgsConstructor
@Tag(name = "Schedule", description = "Schedule API")
@Slf4j(topic = "SCHEDULE_CONTROLLER")
public class ScheduleController {
    private final ScheduleService scheduleService;

    @GetMapping("/{id}")
    @Operation(summary = "Get schedule by ID", description = "Retrieve a schedule by its ID")
    public ResponseData<ScheduleResponse> getDoctorScheduleById(@PathVariable("id") Long id) {
        log.info("Get schedule request for id: {}", id);
        return ResponseData.<ScheduleResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Schedule retrieved successfully")
                .data(scheduleService.getDoctorScheduleById(id))
                .build();
    }

    @PostMapping
    @Operation(summary = "Create a new schedule", description = "Create a new schedule with provided details")
    public ResponseData<ScheduleSummaryResponse> createDoctorSchedule(@RequestBody @Valid ScheduleRequest request) {
        log.info("Create schedule request: {}", request);
        return ResponseData.<ScheduleSummaryResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("schedule created successfully")
                .data(scheduleService.createDoctorSchedule(request))
                .build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a schedule", description = "Update an existing schedule by its ID")
    public ResponseData<ScheduleSummaryResponse> updateDoctorSchedule(
            @PathVariable("id") Long id,
            @RequestBody @Valid ScheduleRequest request) {
        log.info("Update schedule request for id: {}", id);
        return ResponseData.<ScheduleSummaryResponse>builder()
                .status(HttpStatus.OK.value())
                .message("schedule updated successfully")
                .data(scheduleService.updateDoctorSchedule(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a schedule", description = "Delete a schedule by its ID")
    public ResponseData<Void> deleteDoctorSchedule(@PathVariable("id") Long id) {
        log.info("Delete schedule request for id: {}", id);
        scheduleService.deleteDoctorSchedule(id);
        return ResponseData.<Void>builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message("schedule deleted successfully")
                .build();
    }

    @GetMapping
    @Operation(summary = "Get all schedules", description = "Retrieve a paginated list of all schedules")
    public ResponseData<PageResponse<ScheduleSummaryResponse>> getAllDoctorSchedules(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String direction) {
        log.info("Get all schedules request: page={}, size={}, sort={}, direction={}", page, size, sort, direction);
        return ResponseData.<PageResponse<ScheduleSummaryResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("schedules retrieved successfully")
                .data(scheduleService.getAllDoctorSchedules(page, size, sort, direction))
                .build();
    }

    @GetMapping("/search")
    @Operation(summary = "Search schedules", description = "Search schedules by doctor ID, date, and time slot with pagination")
    public ResponseData<PageResponse<ScheduleSummaryResponse>> searchDoctorSchedules(
            @RequestParam(required = false) Long doctorId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) Integer timeSlotId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String direction) {
        log.info("Search schedules request with doctorId: {}, date: {}, timeSlotId: {}", doctorId, date, timeSlotId);
        return ResponseData.<PageResponse<ScheduleSummaryResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("schedules search completed successfully")
                .data(scheduleService.searchDoctorSchedules(doctorId, date, timeSlotId, page, size, sort, direction))
                .build();
    }

}
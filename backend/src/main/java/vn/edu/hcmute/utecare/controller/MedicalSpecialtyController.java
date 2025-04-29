package vn.edu.hcmute.utecare.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmute.utecare.dto.request.MedicalSpecialtyRequest;
import vn.edu.hcmute.utecare.dto.response.*;
import vn.edu.hcmute.utecare.service.DoctorService;
import vn.edu.hcmute.utecare.service.MedicalSpecialtyService;
import vn.edu.hcmute.utecare.service.NurseService;
import vn.edu.hcmute.utecare.service.ScheduleService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/medical-specialties")
@RequiredArgsConstructor
@Tag(name = "Medical Specialty", description = "Medical Specialty API")
@Slf4j(topic = "MEDICAL_SPECIALTY_CONTROLLER")
public class MedicalSpecialtyController {
    private final MedicalSpecialtyService medicalSpecialtyService;
    private final ScheduleService scheduleService;
    private final DoctorService doctorService;
    private final NurseService nurseService;

    @GetMapping("/{id}")
    @Operation(summary = "Get medical specialty by ID", description = "Retrieve a medical specialty by its ID")
    public ResponseData<MedicalSpecialtyResponse> getMedicalSpecialtyById(@PathVariable("id") Integer id) {
        log.info("Get medical specialty request for id: {}", id);
        return ResponseData.<MedicalSpecialtyResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Medical specialty retrieved successfully")
                .data(medicalSpecialtyService.getMedicalSpecialtyById(id))
                .build();
    }

    @PostMapping
    @Operation(summary = "Create a new medical specialty", description = "Create a new medical specialty with provided details")
    public ResponseData<MedicalSpecialtyResponse> createMedicalSpecialty(@RequestBody @Valid MedicalSpecialtyRequest request) {
        log.info("Create medical specialty request: {}", request);
        return ResponseData.<MedicalSpecialtyResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Medical specialty created successfully")
                .data(medicalSpecialtyService.createMedicalSpecialty(request))
                .build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a medical specialty", description = "Update an existing medical specialty by its ID")
    public ResponseData<MedicalSpecialtyResponse> updateMedicalSpecialty(
            @PathVariable("id") Integer id,
            @RequestBody @Valid MedicalSpecialtyRequest request) {
        log.info("Update medical specialty request for id: {}", id);
        return ResponseData.<MedicalSpecialtyResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Medical specialty updated successfully")
                .data(medicalSpecialtyService.updateMedicalSpecialty(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a medical specialty", description = "Delete a medical specialty by its ID")
    public ResponseData<Void> deleteMedicalSpecialty(@PathVariable("id") Integer id) {
        log.info("Delete medical specialty request for id: {}", id);
        medicalSpecialtyService.deleteMedicalSpecialty(id);
        return ResponseData.<Void>builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message("Medical specialty deleted successfully")
                .build();
    }

    @GetMapping
    @Operation(summary = "Get all medical specialties", description = "Retrieve a paginated list of all medical specialties")
    public ResponseData<PageResponse<MedicalSpecialtyResponse>> getAllMedicalSpecialties(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String direction) {
        log.info("Get all medical specialties request: page={}, size={}, sort={}, direction={}", page, size, sort, direction);
        return ResponseData.<PageResponse<MedicalSpecialtyResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Medical specialties retrieved successfully")
                .data(medicalSpecialtyService.getAllMedicalSpecialties(page, size, sort, direction))
                .build();
    }

    @GetMapping("/search")
    @Operation(summary = "Search medical specialties", description = "Search medical specialties by keyword with pagination")
    public ResponseData<PageResponse<MedicalSpecialtyResponse>> searchMedicalSpecialties(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String direction) {
        log.info("Search medical specialties request with keyword: {}", keyword);
        return ResponseData.<PageResponse<MedicalSpecialtyResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Medical specialties search completed successfully")
                .data(medicalSpecialtyService.searchMedicalSpecialties(keyword, page, size, sort, direction))
                .build();
    }

    @GetMapping("/{id}/doctors")
    @Operation(summary = "Get doctors by medical specialty ID", description = "Retrieve a paginated list of doctors for a specific medical specialty")
    public ResponseData<PageResponse<DoctorResponse>> getDoctorsByMedicalSpecialtyId(
            @PathVariable("id") Integer id,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String direction) {
        log.info("Get doctors by medical specialty ID request: id={}, page={}, size={}, sort={}, direction={}", id, page, size, sort, direction);
        return ResponseData.<PageResponse<DoctorResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Doctors retrieved successfully")
                .data(doctorService.getDoctorsByMedicalSpecialtyId(id, page, size, sort, direction))
                .build();
    }

    @GetMapping("/{id}/doctors/search")
    @Operation(summary = "Search doctors by medical specialty ID", description = "Search doctors by keyword for a specific medical specialty")
    public ResponseData<PageResponse<DoctorResponse>> searchDoctorsByMedicalSpecialtyId(
            @PathVariable("id") Integer id,
            @RequestParam String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String direction) {
        log.info("Search doctors by medical specialty ID request: id={}, keyword={}, page={}, size={}, sort={}, direction={}", id, keyword, page, size, sort, direction);
        return ResponseData.<PageResponse<DoctorResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Doctors search completed successfully")
                .data(doctorService.searchDoctorsByMedicalSpecialtyId(id, keyword, page, size, sort, direction))
                .build();
    }

    @GetMapping("/{id}/nurses")
    @Operation(summary = "Get nurses by medical specialty ID", description = "Retrieve a paginated list of nurses for a specific medical specialty")
    public ResponseData<PageResponse<NurseResponse>> getNursesByMedicalSpecialtyId(
            @PathVariable("id") Integer id,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String direction) {
        log.info("Get nurses by medical specialty ID request: id={}, page={}, size={}, sort={}, direction={}", id, page, size, sort, direction);
        return ResponseData.<PageResponse<NurseResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Nurses retrieved successfully")
                .data(nurseService.getNursesByMedicalSpecialtyId(id, page, size, sort, direction))
                .build();
    }

    @GetMapping("/{id}/nurses/search")
    @Operation(summary = "Search nurses by medical specialty ID", description = "Search nurses by keyword for a specific medical specialty")
    public ResponseData<PageResponse<NurseResponse>> searchNursesByMedicalSpecialtyId(
            @PathVariable("id") Integer id,
            @RequestParam String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String direction) {
        log.info("Search nurses by medical specialty ID request: id={}, keyword={}, page={}, size={}, sort={}, direction={}", id, keyword, page, size, sort, direction);
        return ResponseData.<PageResponse<NurseResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Nurses search completed successfully")
                .data(nurseService.searchNursesByMedicalSpecialtyId(id, keyword, page, size, sort, direction))
                .build();
    }

    @GetMapping("/{id}/schedule/available")
    @Operation(summary = "Get available schedules for medical specialty", description = "Retrieve available schedules for a specific medical specialty")
    public ResponseData<List<ScheduleInfoResponse>> getAvailableSchedulesByMedicalSpecialtyId(
            @PathVariable("id") Integer id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
            log.info("Get available schedules for medical specialty request: id={}, date={}", id, date);
            return ResponseData.<List<ScheduleInfoResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Available schedules retrieved successfully")
                .data(scheduleService.getAvailableSchedulesByMedicalSpecialtyId(id, date))
                .build();
    }

}
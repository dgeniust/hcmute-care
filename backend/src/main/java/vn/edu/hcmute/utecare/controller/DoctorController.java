package vn.edu.hcmute.utecare.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmute.utecare.dto.request.DoctorRequest;
import vn.edu.hcmute.utecare.dto.response.*;
import vn.edu.hcmute.utecare.service.DoctorService;
import vn.edu.hcmute.utecare.service.ScheduleService;
import vn.edu.hcmute.utecare.service.ScheduleSlotService;
import vn.edu.hcmute.utecare.service.TicketService;
import vn.edu.hcmute.utecare.util.enumeration.TicketStatus;

import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("/api/v1/doctors")
@RequiredArgsConstructor
@Tag(name = "Doctor", description = "Doctor API")
@Slf4j(topic = "DOCTOR_CONTROLLER")
public class DoctorController {
    private final DoctorService doctorService;
    private final ScheduleService scheduleService;
    private final TicketService ticketService;
    private final ScheduleSlotService scheduleSlotService;

    @GetMapping("/{id}")
    @Operation(summary = "Get doctor by ID", description = "Retrieve a doctor by their ID")
    public ResponseData<DoctorResponse> getDoctorById(@PathVariable("id") Long id) {
        log.info("Get doctor request for id: {}", id);
        return ResponseData.<DoctorResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Doctor retrieved successfully")
                .data(doctorService.getDoctorById(id))
                .build();
    }

    @PostMapping
    @Operation(summary = "Create a new doctor", description = "Create a new doctor with provided details and associated account")
    public ResponseData<DoctorResponse> createDoctor(@RequestBody @Valid DoctorRequest request) {
        log.info("Create doctor request: {}", request);
        return ResponseData.<DoctorResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Doctor created successfully")
                .data(doctorService.createDoctor(request))
                .build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a doctor", description = "Update an existing doctor by their ID")
    public ResponseData<DoctorResponse> updateDoctor(
            @PathVariable("id") Long id,
            @RequestBody @Valid DoctorRequest request) {
        log.info("Update doctor request for id: {}", id);
        return ResponseData.<DoctorResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Doctor updated successfully")
                .data(doctorService.updateDoctor(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a doctor", description = "Delete a doctor by their ID")
    public ResponseData<Void> deleteDoctor(@PathVariable("id") Long id) {
        log.info("Delete doctor request for id: {}", id);
        doctorService.deleteDoctor(id);
        return ResponseData.<Void>builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message("Doctor deleted successfully")
                .build();
    }

    @GetMapping
    @Operation(summary = "Get all doctors", description = "Retrieve a paginated list of all doctors")
    public ResponseData<PageResponse<DoctorResponse>> getAllDoctors(
                                                                     @RequestParam(defaultValue = "1") int page,
                                                                     @RequestParam(defaultValue = "10") int size,
                                                                     @RequestParam(defaultValue = "id") String sort,
                                                                     @RequestParam(defaultValue = "asc") String direction) {
        log.info("Get all doctors request: page={}, size={}, sort={}, direction={}", page, size, sort, direction);
        return ResponseData.<PageResponse<DoctorResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Doctors retrieved successfully")
                .data(doctorService.getAllDoctors(page, size, sort, direction))
                .build();
    }

    @GetMapping("/search")
    @Operation(summary = "Search doctors", description = "Search doctors by keyword (e.g., name, specialty) with pagination")
    public ResponseData<PageResponse<DoctorResponse>> searchDoctors(
                                                                     @RequestParam String keyword,
                                                                     @RequestParam(defaultValue = "1") int page,
                                                                     @RequestParam(defaultValue = "10") int size,
                                                                     @RequestParam(defaultValue = "id") String sort,
                                                                     @RequestParam(defaultValue = "asc") String direction) {
        log.info("Search doctors request with keyword: {}", keyword);
        return ResponseData.<PageResponse<DoctorResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Doctors search completed successfully")
                .data(doctorService.searchDoctors(keyword, page, size, sort, direction))
                .build();
    }

    @GetMapping("/{id}/schedule")
    @Operation(summary = "Chi tiết về 1 lịch làm  theo bác sĩ và ngày (option1)", description = "Retrieve a doctor's schedule by their ID")
    public ResponseData<ScheduleResponse> getDoctorSchedule(@PathVariable("id") Long id,
                                                            @RequestParam LocalDate date) {
        log.info("Get doctor's schedule request for doctor ID: {}", id);
        return ResponseData.<ScheduleResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Doctor's schedule retrieved successfully")
                .data(scheduleService.getDoctorSchedule(id, date))
                .build();
    }

    @GetMapping("/{id}/schedule-slots")
    @Operation(summary = "Danh sách schedule slot theo bác sĩ và ngày (Option 2)", description = "Retrieve a list of schedule slots for a specific doctor")
    public ResponseData<List<ScheduleSlotResponse>> getDoctorScheduleSlots(@PathVariable("id") Long id,
                                                                           @RequestParam LocalDate date) {
        log.info("Get doctor's schedule slots request for doctor ID: {}", id);
        return ResponseData.<List<ScheduleSlotResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Doctor's schedule slots retrieved successfully")
                .data(scheduleSlotService.getAllScheduleSlotsByDoctorIdAndDate(id, date))
                .build();
    }

    @GetMapping("/{id}/schedules")
    @Operation(summary = "Get doctor's schedules", description = "Retrieve a paginated list of schedules for a specific doctor")
    public ResponseData<PageResponse<ScheduleResponse>> getDoctorSchedules(
            @PathVariable("id") Long id,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String direction) {
        log.info("Get doctor's schedules request for doctor ID: {}", id);
        return ResponseData.<PageResponse<ScheduleResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Doctor's schedules retrieved successfully")
                .data(scheduleService.getDoctorSchedules(id, startDate, endDate, page, size, sort, direction))
                .build();
    }

    @GetMapping("/{id}/tickets")
    public ResponseData<List<TicketResponse>> getDoctorTickets(@PathVariable("id") Long id,
                                                               @RequestParam LocalDate date,
                                                               @RequestParam(required = false)TicketStatus status){
        log.info("Get doctor's tickets request for doctor ID: {}", id);
        return ResponseData.<List<TicketResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Doctor's tickets retrieved successfully")
                .data(ticketService.getAllTicketsByDoctorId(id, date, status))
                .build();
    }

    @GetMapping("/patient/{patientId}/medical-tests-date")
    @Operation(summary = "Lấy danh sách MedicalTests của bệnh nhân", description = "Lấy danh sách MedicalTests của bệnh nhân theo patientId và ngày được chỉ định, bao gồm các loại xét nghiệm cụ thể như LaboratoryTests, CardiacTest, ImagingTests, DigestiveTest, EEG, EMG, Spirometry, BloodGasAnalysis, NerveConduction.")
    public ResponseData<List<MedicalTestDetailResponse>> getMedicalTestsByPatientId(
            @PathVariable("patientId") Long patientId,
            @RequestParam(required = false) @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("Yêu cầu lấy danh sách MedicalTests cho bệnh nhân với patientId: {} và ngày: {}", patientId, date);
        List<MedicalTestDetailResponse> responses = doctorService.getMedicalTestsByPatientId(patientId, date);
        return ResponseData.<List<MedicalTestDetailResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Danh sách MedicalTests của bệnh nhân được trả về thành công")
                .data(responses)
                .build();
    }

    @GetMapping("/patient/{patientId}/medical-tests/all")
    @Operation(summary = "Lấy tất cả MedicalTests của bệnh nhân, theo ngày và ID", description = "Lấy tất cả MedicalTests của bệnh nhân theo patientId, bao gồm các loại xét nghiệm cụ thể như LaboratoryTests, CardiacTest, ImagingTests, DigestiveTest, EEG, EMG, Spirometry, BloodGasAnalysis, NerveConduction.")
    public ResponseData<List<MedicalTestDetailResponse>> getAllMedicalTestsByPatientId(
            @PathVariable("patientId") Long patientId) {
        log.info("Yêu cầu lấy tất cả MedicalTests cho bệnh nhân với patientId: {}", patientId);
        List<MedicalTestDetailResponse> responses = doctorService.getMedicalTestsByPatientId(patientId);
        return ResponseData.<List<MedicalTestDetailResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Tất cả MedicalTests của bệnh nhân được trả về thành công")
                .data(responses)
                .build();
    }



}
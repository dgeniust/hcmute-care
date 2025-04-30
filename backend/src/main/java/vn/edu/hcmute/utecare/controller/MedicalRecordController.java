package vn.edu.hcmute.utecare.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmute.utecare.dto.request.MedicalRecordRequest;
import vn.edu.hcmute.utecare.dto.response.*;
import vn.edu.hcmute.utecare.service.AppointmentService;
import vn.edu.hcmute.utecare.service.MedicalRecordService;
import vn.edu.hcmute.utecare.service.TicketService;
import vn.edu.hcmute.utecare.util.enumeration.TicketStatus;

@RestController
@RequestMapping("/api/v1/medical-records")
@RequiredArgsConstructor
@Tag(name = "Medical Record", description = "Medical Record API")
@Slf4j(topic = "MEDICAL_RECORD_CONTROLLER")
public class MedicalRecordController {
    private final MedicalRecordService medicalRecordService;
    private final AppointmentService appointmentService;
    private final TicketService ticketService;

    @PostMapping
    @Operation(summary = "Create a new medical record", description = "Creates a new medical record along with its associated patient details")
    public ResponseData<MedicalRecordResponse> create(@RequestBody @Valid MedicalRecordRequest request) {
        log.info("Create medical record request: {}", request);
        return ResponseData.<MedicalRecordResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Medical record created successfully")
                .data(medicalRecordService.create(request))
                .build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get medical record by ID", description = "Retrieves the details of a medical record by its ID")
    public ResponseData<MedicalRecordResponse> getById(@PathVariable Long id) {
        log.info("Get medical record request for id: {}", id);
        return ResponseData.<MedicalRecordResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Medical record retrieved successfully")
                .data(medicalRecordService.getById(id))
                .build();
    }

    @GetMapping
    @Operation(summary = "Get all medical records", description = "Retrieves a paginated list of all medical records")
    public ResponseData<PageResponse<MedicalRecordResponse>> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String direction) {
        log.info("Get all medical records request: page={}, size={}, sort={}, direction={}", page, size, sort, direction);
        return ResponseData.<PageResponse<MedicalRecordResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Medical records retrieved successfully")
                .data(medicalRecordService.getAll(page, size, sort, direction))
                .build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update medical record by ID", description = "Updates a medical record by its ID")
    public ResponseData<MedicalRecordResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid MedicalRecordRequest request) {
        log.info("Update medical record request for id: {}", id);
        return ResponseData.<MedicalRecordResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Medical record updated successfully")
                .data(medicalRecordService.update(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete medical record by ID", description = "Deletes a medical record by its ID")
    public ResponseData<Void> delete(@PathVariable Long id) {
        log.info("Delete medical record request for id: {}", id);
        medicalRecordService.delete(id);
        return ResponseData.<Void>builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message("Medical record deleted successfully")
                .build();
    }

    @GetMapping("/{id}/appointments")
    @Operation(summary = "Get appointments by medical record ID", description = "Retrieves a paginated list of appointments associated with a medical record")
    public ResponseData<PageResponse<AppointmentResponse>> getAppointmentsByMedicalRecordId(
            @PathVariable("id") Long medicalRecordId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String direction) {
        log.info("Get appointments by medical record ID: {}, page: {}, size: {}, sort: {}, direction: {}",
                medicalRecordId, page, size, sort, direction);
        return ResponseData.<PageResponse<AppointmentResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Appointments retrieved successfully")
                .data(appointmentService.getAppointmentByMedicalRecordId(medicalRecordId, page, size, sort, direction))
                .build();
    }

    @GetMapping("/{id}/tickets")
    @Operation(summary = "Get tickets by medical record ID", description = "Retrieves a paginated list of tickets associated with a medical record")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'CUSTOMER')")
    public ResponseData<PageResponse<TicketResponse>> getTicketsByMedicalRecordId(
            @PathVariable("id") Long medicalRecordId,
            @RequestParam(required = false) TicketStatus status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String direction) {
        log.info("Get tickets by medical record ID: {}, status: {}, page: {}, size: {}, sort: {}, direction: {}",
                medicalRecordId, status, page, size, sort, direction);
        return ResponseData.<PageResponse<TicketResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Tickets retrieved successfully")
                .data(ticketService.getAllTicketsByMedicalRecordId(medicalRecordId, status, page, size, sort, direction))
                .build();
    }

}
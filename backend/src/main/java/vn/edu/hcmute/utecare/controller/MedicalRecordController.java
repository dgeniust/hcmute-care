package vn.edu.hcmute.utecare.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import vn.edu.hcmute.utecare.dto.request.MedicalRecordRequest;
import vn.edu.hcmute.utecare.dto.response.MedicalRecordResponse;
import vn.edu.hcmute.utecare.service.MedicalRecordService;

import java.util.List;

@RestController
@RequestMapping("/api/medical-records")
@Tag(name = "Medical Record", description = "Medical Record API")
@RequiredArgsConstructor
public class MedicalRecordController {
    private final MedicalRecordService medicalRecordService;

    @PostMapping
    @Operation(summary = "Create a new medical record",
            description = "Creates a new medical record along with its associated patient details")
    public ResponseEntity<MedicalRecordResponse> create(
            @Valid @RequestBody MedicalRecordRequest request) {
        return ResponseEntity.ok(medicalRecordService.create(request));
    }
    @GetMapping("/{id}")
    @Operation(summary = "Get medical record by ID",
            description = "Retrieves the details of a medical record by its ID")
    public ResponseEntity<MedicalRecordResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(medicalRecordService.getById(id));
    }


    @Operation(summary = "Get all medical records",
            description = "Get all medical records")
    @GetMapping
    public ResponseEntity<List<MedicalRecordResponse>> getAll() {
        return ResponseEntity.ok(medicalRecordService.getAll());
    }



    @PutMapping("/{id}")
    @Operation(summary = "Update medical record by ID",
            description = "Update a medical record by its ID")
    public ResponseEntity<MedicalRecordResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody MedicalRecordRequest request) {
        return ResponseEntity.ok(medicalRecordService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete medical record by ID",
            description = "Deletes a medical record by its ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        medicalRecordService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
package vn.edu.hcmute.utecare.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import vn.edu.hcmute.utecare.dto.request.PrescriptionRequest;
import vn.edu.hcmute.utecare.dto.response.PrescriptionItemResponse;
import vn.edu.hcmute.utecare.dto.response.PrescriptionResponse;
import vn.edu.hcmute.utecare.dto.response.ResponseData;
import vn.edu.hcmute.utecare.service.PrescriptionService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/prescription")
@RequiredArgsConstructor
@Tag(name="Prescription", description = "Prescription API")
@Slf4j(topic = "PRESCRIPTION_CONTROLLER")
public class PrescriptionController {
    private final PrescriptionService prescriptionService;

    @GetMapping("/{id}")
    @Operation(summary = "Get prescription by id", description = "Get details prescription by its id")
    public ResponseData<PrescriptionResponse> getPrescriptionById(@PathVariable("id") Long id) {
        log.info("Get prescription by id {}", id);
        try {
            return ResponseData.<PrescriptionResponse>builder()
                    .status(HttpStatus.OK.value())
                    .message("Get prescription by id successfully ")
                    .data(prescriptionService.getPrescriptionById(id))
                    .build();
        } catch (Exception e) {
            log.error("Error in get prescription by id: {}", e.getMessage());
            return ResponseData.<PrescriptionResponse>builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Failed to get prescription by id. Please try again later.")
                    .data(null)
                    .build();
        }
    }

    @GetMapping
    @Operation(summary = "Get all prescriptions", description = "Feature for admin/ staff to get all prescriptions")
    public ResponseData<List<PrescriptionResponse>> getAllPrescriptions() {
        log.info("Get all prescriptions");
        try {
            return ResponseData.<List<PrescriptionResponse>>builder()
                    .status(HttpStatus.OK.value())
                    .message("Get all prescriptions successfully ")
                    .data(prescriptionService.getAllPrescriptions())
                    .build();
        } catch (Exception e) {
            log.error("Error in get all prescription: {}", e.getMessage());
            return ResponseData.<List<PrescriptionResponse>>builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Failed to get all prescriptions. Please try again later.")
                    .data(null)
                    .build();
        }
    }

    @GetMapping("/item/{id}")
    @Operation(summary = "Get all prescriptions item by prescription id", description = "Get all details prescriptions item by prescription id")
    public ResponseData<List<PrescriptionItemResponse>> getAllPrescriptionItemsByPrescriptionItemId(@PathVariable("id") Long id) {
        log.info("Get all prescription items by prescription id {}", id);
        try {
            return ResponseData.<List<PrescriptionItemResponse>>builder()
                    .status(HttpStatus.OK.value())
                    .message("Get all prescription items by prescription id successfully ")
                    .data(prescriptionService.getAllPrescriptionItemsByPrescriptionId(id))
                    .build();
        } catch (Exception e) {
            log.error("Error in get all prescription items by prescription id : {}", e.getMessage());
            return ResponseData.<List<PrescriptionItemResponse>>builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Failed to get all prescription items. Please try again later.")
                    .data(null)
                    .build();
        }
    }
    @PostMapping
    @Operation(summary = "Create new prescription", description = "Create a new prescription with request")
    public ResponseData<PrescriptionResponse> createPrescription(@RequestBody @Valid PrescriptionRequest request) {
        log.info("Create prescription with request: {}", request);
        try{
            return ResponseData.<PrescriptionResponse>builder()
                    .status(HttpStatus.OK.value())
                    .message("Create prescription with request successfully ")
                    .data(prescriptionService.addPrescription(request))
                    .build();
        } catch (Exception e) {
            log.error("Error in create prescription: {}", e.getMessage());
            return ResponseData.<PrescriptionResponse>builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Failed to create prescription. Please try again later.")
                    .data(null)
                    .build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a prescription", description = "Update an existing prescription with its id")
    public ResponseData<PrescriptionResponse> updatePrescription(@PathVariable("id") Long id, PrescriptionRequest request) {
        log.info("Update prescription by id {} and request {}", id, request);
        try{
            return ResponseData.<PrescriptionResponse>builder()
                    .status(HttpStatus.OK.value())
                    .message("Update prescription by id successfully ")
                    .data(prescriptionService.updatePrescription(id, request))
                    .build();
        } catch (Exception e) {
            log.error("Error in update prescription by id: {}", e.getMessage());
            return ResponseData.<PrescriptionResponse>builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Failed to update prescription by id. Please try again later.")
                    .data(null)
                    .build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a prescription", description = "Delete an existing prescription with its id")
    public void deletePrescription(@PathVariable("id") Long id) {
        log.info("Delete prescription item with id: {}", id);
        prescriptionService.deletePrescription(id);
    }
}

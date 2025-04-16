package vn.edu.hcmute.utecare.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmute.utecare.dto.request.PrescriptionItemRequest;
import vn.edu.hcmute.utecare.dto.response.PrescriptionItemResponse;
import vn.edu.hcmute.utecare.dto.response.ResponseData;
import vn.edu.hcmute.utecare.service.PrescriptionItemService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/prescription-item")
@RequiredArgsConstructor
@Tag(name="PrescriptionItem", description = "Prescription Item API")
@Slf4j(topic = "PRESCRIPTION_ITEM_CONTROLLER")
public class PrescriptionItemController {
    private final PrescriptionItemService prescriptionItemService;

    @GetMapping("/{id}")
    @Operation(summary = "Get prescription item by id", description = "Get details prescription item by its id")
    public ResponseData<PrescriptionItemResponse> getPrescriptionItem(@PathVariable("id") Long id) {
        log.info("Get prescription item by id: {}", id);
        try{
            return ResponseData.<PrescriptionItemResponse>builder()
                    .status(HttpStatus.OK.value())
                    .message("Get prescription item by id successfully")
                    .data(prescriptionItemService.getPrescriptionItemById(id))
                    .build();
        } catch (Exception e) {
            log.error("Error in get prescription item by id: {}", e.getMessage());
            return ResponseData.<PrescriptionItemResponse>builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Failed to get prescription item by id. Please try again later.")
                    .data(null)
                    .build();
        }
    }

    @GetMapping
    @Operation(summary = "Get all prescription items", description = "Get all prescription items")
    public ResponseData<List<PrescriptionItemResponse>> getAllPrescriptionItems() {
        log.info("Get all prescription items");
        try{
            return ResponseData.<List<PrescriptionItemResponse>>builder()
                    .status(HttpStatus.OK.value())
                    .message("Get all prescription items successfully")
                    .data(prescriptionItemService.getAllPrescriptionItems())
                    .build();
        } catch (Exception e) {
            log.error("Error in get all prescription items: {}", e.getMessage());
            return  ResponseData.<List<PrescriptionItemResponse>>builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Failed to retrieve prescription items. Please try again later.")
                    .data(null)
                    .build();
        }
    }

    @PostMapping
    @Operation(summary = "Create new prescription item", description = "Create a new prescription item with request")
    public ResponseData<PrescriptionItemResponse> createPrescriptionItem(@RequestBody @Valid PrescriptionItemRequest request) {
        log.info("Create prescription item with request: {}", request);
        try{
            return ResponseData.<PrescriptionItemResponse>builder()
                    .status(HttpStatus.OK.value())
                    .message("Create prescription with request successfully ")
                    .data(prescriptionItemService.addPrescriptionItem(request))
                    .build();
        } catch (Exception e) {
            log.error("Error in create prescription item: {}", e.getMessage());
            return ResponseData.<PrescriptionItemResponse>builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Failed to create prescription item. Please try again later.")
                    .data(null)
                    .build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a prescription item", description = "Update an existing prescription item with its id")
    public ResponseData<PrescriptionItemResponse> updatePrescriptionItem(@PathVariable("id") Long id ,@RequestBody @Valid PrescriptionItemRequest request) {
        log.info("Update prescription item with id: {}", id);
        try{
            return ResponseData.<PrescriptionItemResponse>builder()
                    .status(HttpStatus.OK.value())
                    .message("Update prescription with request successfully ")
                    .data(prescriptionItemService.updatePrescriptionItem(id,request))
                    .build();
        } catch (Exception e) {
            log.error("Error in create update item: {}", e.getMessage());
            return ResponseData.<PrescriptionItemResponse>builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Failed to update prescription item. Please try again later.")
                    .data(null)
                    .build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a prescription item", description = "Delete an existing prescription item with its id")
    public void deletePrescriptionItem(@PathVariable("id") Long id) {
        log.info("Delete prescription item with id: {}", id);
        prescriptionItemService.deletePrescriptionItem(id);
    }

}

package vn.edu.hcmute.utecare.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmute.utecare.dto.request.MedicineRequest;
import vn.edu.hcmute.utecare.dto.response.MedicineResponse;
import vn.edu.hcmute.utecare.dto.response.ResponseData;
import vn.edu.hcmute.utecare.service.MedicineService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/medicine")
@RequiredArgsConstructor
@Tag(name="Medicine", description = "Medicine API")
@Slf4j(topic = "MEDICINE_CONTROLLER")
public class MedicineController {
    private final MedicineService medicineService;

    @GetMapping("/{id}")
    @Operation(summary = "Get medicine by id", description = "Get details medicine by its id")
    public ResponseData<MedicineResponse> getMedicineById(@PathVariable("id") Long id) {
        log.info("Get medicine by id: {}", id);
        try{
            return ResponseData.<MedicineResponse>builder()
                    .status(HttpStatus.OK.value())
                    .message("Get medicine by id successfully")
                    .data(medicineService.getMedicineById(id))
                    .build();
        } catch (Exception e) {
            log.error("Error in get medicine by id: {}", e.getMessage());
            return ResponseData.<MedicineResponse>builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Failed to get medicine by id. Please try again later.")
                    .data(null)
                    .build();
        }
    }

    @GetMapping
    @Operation(summary = "Get all medicine", description = "Get all details medicine")
    public ResponseData<List<MedicineResponse>> getAllMedicine() {
        log.info("Get all medicine");
        try{
            return ResponseData.<List<MedicineResponse>>builder()
                    .status(HttpStatus.OK.value())
                    .message("Get all medicine successfully")
                    .data(medicineService.getAllMedicine())
                    .build();
        } catch (Exception e) {
            log.error("Error in get all medicine: {}", e.getMessage());
            return ResponseData.<List<MedicineResponse>>builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Failed to get all medicine. Please try again later.")
                    .data(null)
                    .build();
        }
    }

    @PostMapping
    @Operation(summary = "Create a medicine", description = "Create a details medicine")
    public ResponseData<MedicineResponse> createMedicine(@RequestBody @Valid MedicineRequest request) {
        log.info("Create medicine: {}", request);
        try{
            return ResponseData.<MedicineResponse>builder()
                    .status(HttpStatus.OK.value())
                    .message("Create medicine successfully")
                    .data(medicineService.createMedicine(request))
                    .build();
        } catch (Exception e) {
            log.error("Error in create medicine: {}", e.getMessage());
            return ResponseData.<MedicineResponse>builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Failed to create medicine. Please try again later.")
                    .data(null)
                    .build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a medicine by its id", description = "Update a details medicine by its id")
    public ResponseData<MedicineResponse> updateMedicine(@PathVariable("id") Long id, @RequestBody @Valid MedicineRequest request) {
        log.info("Update medicine: {}", request);
        try{
            return ResponseData.<MedicineResponse>builder()
                    .status(HttpStatus.OK.value())
                    .message("Update medicine by its id successfully")
                    .data(medicineService.updateMedicine(id,request))
                    .build();
        } catch (Exception e) {
            log.error("Error in update medicine: {}", e.getMessage());
            return ResponseData.<MedicineResponse>builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Failed to update medicine. Please try again later.")
                    .data(null)
                    .build();
        }
    }
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a medicine by its id", description = "Delete a details medicine by its id")
    public void deleteMedicine(@PathVariable("id") Long id) {
        log.info("Delete medicine: {}", id);
        medicineService.deleteMedicine(id);
    }


}

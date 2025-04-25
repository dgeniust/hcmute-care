package vn.edu.hcmute.utecare.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmute.utecare.dto.request.EncounterRequest;
import vn.edu.hcmute.utecare.dto.response.EncounterResponse;
import vn.edu.hcmute.utecare.dto.response.ResponseData;
import vn.edu.hcmute.utecare.service.EncounterService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/encounters")
@RequiredArgsConstructor
@Tag(name = "Encounter", description = "Encounter API")
@Slf4j(topic = "ENCOUNTER_CONTROLLER")
public class EncounterController {
    private final EncounterService encounterService;

    @GetMapping
    @Operation(summary = "Get all encounters", description = "Get all encounters ")
    public ResponseData<List<EncounterResponse>> getAllEncounter() {
        log.info("Get all encounter");
        return ResponseData.<List<EncounterResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("All encounters retrieved successfully")
                .data(encounterService.getAllEncounter())
                .build();
    }
    @GetMapping("/{id}")
    @Operation(summary = "Get encounter by ID", description = "Get encounter by its ID")
    public ResponseData<EncounterResponse> getEncounterById(@PathVariable("id") Long id) {
        log.info("Get encounter with id {}", id);
        return ResponseData.<EncounterResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Encounter retrieved successfully")
                .data(encounterService.getEncounterById(id))
                .build();
    }

    @PostMapping
    @Operation(summary = "Create a new encounter", description = "Create a new encounter with provided details")
    public ResponseData<EncounterResponse> createEncounter(@RequestBody @Valid EncounterRequest request) {
        log.info("Create encounter request: {}", request);
        return ResponseData.<EncounterResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Encounter created successfully")
                .data(encounterService.createEncounter(request))
                .build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an encounter", description = "Update an existing encounter by its ID")
    public ResponseData<EncounterResponse> updateEncounter(
            @PathVariable("id") Long id,
            @RequestBody @Valid EncounterRequest request) {
        log.info("Update encounter request for id: {}", id);
        return ResponseData.<EncounterResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Encounter updated successfully")
                .data(encounterService.updateEncounter(id, request))
                .build();
    }
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an encounter", description = "Delete an encounter by its ID")
    public ResponseData<Void> deleteEncounter(@PathVariable("id") Long id) {
        log.info("Delete encounter request for id: {}", id);
        encounterService.deleteEncounter(id);
        return ResponseData.<Void>builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message("Encounter deleted successfully")
                .build();
    }
}

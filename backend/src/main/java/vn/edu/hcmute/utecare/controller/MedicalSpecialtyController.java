package vn.edu.hcmute.utecare.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/medical-specialties")
@RequiredArgsConstructor
@Tag(name = "Medical Specialty", description = "Medical Specialty API")
@Slf4j(topic = "MEDICAL-SPECIALTIES_CONTROLLER")
public class MedicalSpecialtyController {
}

package vn.edu.hcmute.utecare.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import vn.edu.hcmute.utecare.dto.request.PatientRequest;
import vn.edu.hcmute.utecare.dto.response.PatientResponse;
import vn.edu.hcmute.utecare.exception.NotFoundException;
import vn.edu.hcmute.utecare.mapper.PatientMapper;
import vn.edu.hcmute.utecare.model.Patient;
import vn.edu.hcmute.utecare.repository.PatientRepository;
import vn.edu.hcmute.utecare.service.PatientService;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    @Override
    public PatientResponse getById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Patient not found with id: " + id));
        return patientMapper.toResponse(patient);
    }

    @Override
    public PatientResponse update(Long id, PatientRequest request) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Patient not found with id: " + id));

        patientRepository.findByCccd(request.getCccd())
                .ifPresent(p -> {
                    if (!p.getId().equals(id)) {
                        throw new IllegalArgumentException("CCCD already exists");
                    }
                });
        patientRepository.findByEmail(request.getEmail())
                .ifPresent(p -> {
                    if (!p.getId().equals(id)) {
                        throw new IllegalArgumentException("Email already exists");
                    }
                });

        patient.setName(request.getName());
        patient.setCccd(request.getCccd());
        patient.setDob(request.getDob());
        patient.setGender(request.getGender());
        patient.setAddress(request.getAddress());
        patient.setPhone(request.getPhone());
        patient.setEmail(request.getEmail());
        patient.setNation(request.getNation());
        patient.setCareer(request.getCareer());

        return patientMapper.toResponse(patientRepository.save(patient));
    }


}
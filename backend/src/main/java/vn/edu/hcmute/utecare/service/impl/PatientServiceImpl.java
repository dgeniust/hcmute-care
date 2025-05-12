package vn.edu.hcmute.utecare.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class PatientServiceImpl implements PatientService {
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    @Override
    public PatientResponse getById(Long id) {
        log.info("Truy xuất bệnh nhân với ID: {}", id);
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy bệnh nhân với ID: " + id));
        log.info("Truy xuất bệnh nhân thành công với ID: {}", id);
        return patientMapper.toResponse(patient);
    }

    @Override
    public PatientResponse update(Long id, PatientRequest request) {
        log.info("Cập nhật bệnh nhân với ID: {} và thông tin: {}", id, request);
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy bệnh nhân với ID: " + id));

        patientRepository.findByCccd(request.getCccd())
                .ifPresent(p -> {
                    if (!p.getId().equals(id)) {
                        throw new IllegalArgumentException("CCCD đã tồn tại");
                    }
                });
        patientRepository.findByEmail(request.getEmail())
                .ifPresent(p -> {
                    if (!p.getId().equals(id)) {
                        throw new IllegalArgumentException("Email đã tồn tại");
                    }
                });

        patientMapper.updateEntity(request, patient);

        Patient updatedPatient = patientRepository.save(patient);
        log.info("Cập nhật bệnh nhân thành công với ID: {}", id);
        return patientMapper.toResponse(updatedPatient);
    }
}
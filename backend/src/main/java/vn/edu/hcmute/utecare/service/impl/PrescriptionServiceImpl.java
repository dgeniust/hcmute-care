package vn.edu.hcmute.utecare.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.hcmute.utecare.dto.request.PrescriptionRequest;
import vn.edu.hcmute.utecare.dto.response.PrescriptionItemResponse;
import vn.edu.hcmute.utecare.dto.response.PrescriptionResponse;
import vn.edu.hcmute.utecare.exception.ResourceNotFoundException;
import vn.edu.hcmute.utecare.mapper.PrescriptionItemMapper;
import vn.edu.hcmute.utecare.mapper.PrescriptionMapper;
import vn.edu.hcmute.utecare.model.Prescription;
import vn.edu.hcmute.utecare.model.PrescriptionItem;
import vn.edu.hcmute.utecare.repository.PrescriptionItemRepository;
import vn.edu.hcmute.utecare.repository.PrescriptionRepository;
import vn.edu.hcmute.utecare.service.PrescriptionService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrescriptionServiceImpl implements PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final PrescriptionItemRepository prescriptionItemRepository;
    private final PrescriptionMapper prescriptionMapper;

//    @Autowired
//    public void PrescriptionService(PrescriptionMapper prescriptionMapper) {
//        this.prescriptionMapper = prescriptionMapper;
//    }
    @Override
    public PrescriptionResponse getPrescriptionById(Long id) {
        log.info("Get prescription with id {}", id);
        Prescription prescription = prescriptionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Prescription with id " + id + " not found"));
        return prescriptionMapper.toResponse(prescription);
    }

    @Override
    public void deletePrescription(Long id) {
        log.info("Delete prescription with id {}", id);
        if(!prescriptionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Prescription with id " + id + " not found");
        }
        prescriptionRepository.deleteById(id);
    }

    @Override
    public List<PrescriptionItemResponse> getAllPrescriptionItemsByPrescriptionItemId(Long prescriptionId) {
        log.info("Get prescription items with prescription id {}", prescriptionId);
        Prescription prescription = prescriptionRepository.findById(prescriptionId).orElseThrow(()->new ResourceNotFoundException("Prescription with id " + prescriptionId + " not found"));
        Set<PrescriptionItem> listPI = prescription.getPrescriptionItems();
        log.info("-------------------------------------------------------------");
        System.out.println("Found prescription items" + listPI.stream().toList());
        List<PrescriptionItemResponse> listPIResponse = new ArrayList<>();
        listPI.forEach(item -> {
            Long id = item.getId();
            PrescriptionItem pir = prescriptionItemRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Not found prescription items with id: " + id));
            PrescriptionItemResponse response = PrescriptionItemMapper.INSTANCE.toResponse(pir);
            listPIResponse.add(response);
        });
        return listPIResponse;
    }

    @Override
    public List<PrescriptionResponse> getAllPrescriptions() {
        log.info("Get all prescription");
        List<Prescription> prescriptions = prescriptionRepository.findAll();
        return prescriptions.stream().map(prescriptionMapper::toResponse).toList();
    }

    @Override
    public PrescriptionResponse addPrescription(PrescriptionRequest request) {
        log.info("Create a new prescription");
        Prescription prescription = prescriptionMapper.toEntity(request);
        return prescriptionMapper.toResponse(prescriptionRepository.save(prescription));
    }

    @Override
    public PrescriptionResponse updatePrescription(Long id, PrescriptionRequest request) {
        log.info("Update prescription with id {}, request {}", id, request);
        Prescription prescription = prescriptionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Prescription with id " + id + " not found"));
        prescriptionMapper.update(request, prescription);
        Prescription updatedPrescription = prescriptionRepository.save(prescription);
        return prescriptionMapper.toResponse(updatedPrescription);
    }
}

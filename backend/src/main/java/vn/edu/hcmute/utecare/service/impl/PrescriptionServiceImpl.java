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
import vn.edu.hcmute.utecare.model.Encounter;
import vn.edu.hcmute.utecare.model.Medicine;
import vn.edu.hcmute.utecare.model.Prescription;
import vn.edu.hcmute.utecare.model.PrescriptionItem;
import vn.edu.hcmute.utecare.repository.EncounterRepository;
import vn.edu.hcmute.utecare.repository.MedicineRepository;
import vn.edu.hcmute.utecare.repository.PrescriptionItemRepository;
import vn.edu.hcmute.utecare.repository.PrescriptionRepository;
import vn.edu.hcmute.utecare.service.PrescriptionService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrescriptionServiceImpl implements PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final PrescriptionItemRepository prescriptionItemRepository;
    private final EncounterRepository encounterRepository;
    private final MedicineRepository medicineRepository;
    //private final PrescriptionMapper prescriptionMapper;

//    @Autowired
//    public void PrescriptionService(PrescriptionMapper prescriptionMapper) {
//        this.prescriptionMapper = prescriptionMapper;
//    }
    @Override
    public PrescriptionResponse getPrescriptionById(Long id) {
        log.info("Get prescription with id {}", id);
        Prescription prescription = prescriptionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Prescription with id " + id + " not found"));
        return PrescriptionMapper.INSTANCE.toResponse(prescription);
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
    public List<PrescriptionItemResponse> getAllPrescriptionItemsByPrescriptionId(Long prescriptionId) {
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
        return prescriptions.stream().map(PrescriptionMapper.INSTANCE::toResponse).toList();
    }

    @Override
    public PrescriptionResponse addPrescription(PrescriptionRequest request) {
        log.info("Create a new prescription");
        Prescription prescription = PrescriptionMapper.INSTANCE.toEntity(request);

        prescription.setPrescriptionItems(null);
        Encounter encounter = encounterRepository.findById(request.getEncounterId()).orElseThrow(() -> new ResourceNotFoundException("Encounter with id " + request.getEncounterId() + " not found"));
        prescription.setEncounter(encounter);
        // Lưu Prescription để tạo ID
        Prescription prescriptionSaved = prescriptionRepository.save(prescription);

        Set<PrescriptionItem> prescriptionItems = request.getPrescriptionItems().stream()
                .map(itemRequest -> {
                    PrescriptionItem item = PrescriptionItemMapper.INSTANCE.toEntity(itemRequest);
                    Medicine medicine = medicineRepository.findById(itemRequest.getMedicineId()).orElseThrow(() -> new ResourceNotFoundException("Medicine with id " + itemRequest.getMedicineId() + " not found"));
                    item.setMedicine(medicine);
                    item.setPrescription(prescriptionSaved); // Gán liên kết với Prescription
                    return item;
                })
                .collect(Collectors.toSet());

        prescriptionItemRepository.saveAll(prescriptionItems);
        prescriptionSaved.setPrescriptionItems(prescriptionItems);

        return PrescriptionMapper.INSTANCE.toResponse(prescriptionRepository.save(prescription));
    }

    @Override
    public PrescriptionResponse updatePrescription(Long id, PrescriptionRequest request) {
        log.info("Update prescription with id {}, request {}", id, request);
        Prescription prescription = prescriptionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Prescription with id " + id + " not found"));
        PrescriptionMapper.INSTANCE.update(request, prescription);
        Prescription updatedPrescription = prescriptionRepository.save(prescription);
        return PrescriptionMapper.INSTANCE.toResponse(updatedPrescription);
    }
}

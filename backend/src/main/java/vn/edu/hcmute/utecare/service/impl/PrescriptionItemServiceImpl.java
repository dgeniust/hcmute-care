package vn.edu.hcmute.utecare.service.impl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import vn.edu.hcmute.utecare.dto.request.PrescriptionItemRequest;
import vn.edu.hcmute.utecare.dto.response.PrescriptionItemResponse;
import vn.edu.hcmute.utecare.exception.ResourceNotFoundException;
import vn.edu.hcmute.utecare.mapper.PrescriptionItemMapper;
import vn.edu.hcmute.utecare.model.Medicine;
import vn.edu.hcmute.utecare.model.PrescriptionItem;
import vn.edu.hcmute.utecare.repository.MedicineRepository;
import vn.edu.hcmute.utecare.repository.PrescriptionItemRepository;
import vn.edu.hcmute.utecare.service.PrescriptionItemService;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class PrescriptionItemServiceImpl implements PrescriptionItemService {
    private final PrescriptionItemRepository prescriptionItemRepository;
    private final MedicineRepository medicineRepository;

    @Override
    public PrescriptionItemResponse addPrescriptionItem(@RequestBody @Valid PrescriptionItemRequest request) {
        log.info("Add prescription item by request{}", request);
        PrescriptionItem prescriptionItem = PrescriptionItemMapper.INSTANCE.toEntity(request);
        Medicine medicine = medicineRepository.findById(request.getMedicineId())
                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found with id " + request.getMedicineId()));
        prescriptionItem.setMedicine(medicine);
        return PrescriptionItemMapper.INSTANCE.toResponse(prescriptionItemRepository.save(prescriptionItem));
    }

    @Override
    public PrescriptionItemResponse updatePrescriptionItem(Long id, PrescriptionItemRequest request) {
        log.info("Update prescription item by id{}, request{}", id, request);
        PrescriptionItem prescriptionItem = prescriptionItemRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Can't find prescription item with id: " + id));
        PrescriptionItemMapper.INSTANCE.update(request, prescriptionItem);
        PrescriptionItem updatedPrescriptionItem = prescriptionItemRepository.save(prescriptionItem);
        return PrescriptionItemMapper.INSTANCE.toResponse(updatedPrescriptionItem);
    }

    @Override
    public void deletePrescriptionItem(Long id) {
        log.info("Delete prescription item by id{}", id);
        if(!prescriptionItemRepository.existsById(id)) {
            throw new ResourceNotFoundException("PrescriptionItem with id " + id + " not found");
        }
        prescriptionItemRepository.deleteById(id);
    }

    @Override
    public PrescriptionItemResponse getPrescriptionItemById(Long id) {
        log.info("Get prescription item by id{}", id);
        PrescriptionItem prescriptionItem = prescriptionItemRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Can't find prescription item with id: " + id));

        return PrescriptionItemMapper.INSTANCE.toResponse(prescriptionItem);
    }

    @Override
    public List<PrescriptionItemResponse> getAllPrescriptionItems() {
        log.info("Get all prescription items");
        List<PrescriptionItem> prescriptionItems = prescriptionItemRepository.findAll();
        return prescriptionItems.stream().map(PrescriptionItemMapper.INSTANCE::toResponse).toList();
    }
}

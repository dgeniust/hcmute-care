package vn.edu.hcmute.utecare.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.edu.hcmute.utecare.dto.request.MedicineRequest;
import vn.edu.hcmute.utecare.dto.response.MedicineResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.exception.ResourceNotFoundException;
import vn.edu.hcmute.utecare.mapper.MedicineMapper;
import vn.edu.hcmute.utecare.model.Medicine;
import vn.edu.hcmute.utecare.repository.MedicineRepository;
import vn.edu.hcmute.utecare.repository.PrescriptionItemRepository;
import vn.edu.hcmute.utecare.service.MedicineService;
import vn.edu.hcmute.utecare.util.PaginationUtil;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class MedicineServiceImpl implements MedicineService {

    private final MedicineRepository medicineRepository;
    private final PrescriptionItemRepository prescriptionItemRepository;
    @Override
    public MedicineResponse getMedicineById(Long id) {
        log.info("Get medicine by id: {} ", id);
        Medicine medicine = medicineRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Medicine with id: " + id + " not found"));
        return MedicineMapper.INSTANCE.toResponse(medicine);
    }

    @Override
    public MedicineResponse getMedicineByMedicineName(String medicineName) {
        log.info("Get medicine by medicineName: {} ", medicineName);
        Medicine medicine = medicineRepository.findByName(medicineName).orElseThrow(() -> new ResourceNotFoundException("Medicine with name: " + medicineName + " not found"));
        return MedicineMapper.INSTANCE.toResponse(medicine);
    }

    @Override
    public MedicineResponse createMedicine(MedicineRequest request) {
        log.info("Create medicine with request: {}", request);
        Medicine medicine = MedicineMapper.INSTANCE.toEntity(request);
        return MedicineMapper.INSTANCE.toResponse(medicineRepository.save(medicine));
    }

    @Override
    public MedicineResponse updateMedicine(Long id, MedicineRequest request) {
        log.info("Update medicine with id: {} ", id);
        Medicine medicine = medicineRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Medicine with id: " + id + " not found"));
        MedicineMapper.INSTANCE.update(request, medicine);
        Medicine updatedMedicine = medicineRepository.save(medicine);
        return MedicineMapper.INSTANCE.toResponse(updatedMedicine);
    }

    @Override
    public void deleteMedicine(Long id) {
        log.info("Delete medicine with id: {} ", id);
        if(!medicineRepository.existsById(id)){
            throw new ResourceNotFoundException("Medicine with id: " + id + " not found");
        }
        medicineRepository.deleteById(id);
    }

    @Override
    public List<MedicineResponse> getAllMedicine() {
        log.info("Get medicine list");
        List<Medicine> medicines = medicineRepository.findAll();
        return medicines.stream().map(MedicineMapper.INSTANCE::toResponse).toList();
    }

    @Override
    public PageResponse<MedicineResponse> searchByName(String name,
                                                       int page,
                                                       int size,
                                                       String sort,
                                                       String direction) {
        log.info("Search medicine by name: {} ", name);
        Pageable pageable = PaginationUtil.createPageable(page, size, sort, direction);
        Page<Medicine> medicinePage = medicineRepository.findByNameContainingIgnoreCase(name, pageable);

        return PageResponse.<MedicineResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalElements(medicinePage.getTotalElements())
                .totalPages(medicinePage.getTotalPages())
                .content(medicinePage.getContent().stream()
                        .map(MedicineMapper.INSTANCE::toResponse)
                        .toList())
                .build();
    }

}

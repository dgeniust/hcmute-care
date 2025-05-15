package vn.edu.hcmute.utecare.service.impl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.utecare.dto.request.PrescriptionItemRequest;
import vn.edu.hcmute.utecare.dto.response.PrescriptionItemResponse;
import vn.edu.hcmute.utecare.exception.ResourceNotFoundException;
import vn.edu.hcmute.utecare.mapper.PrescriptionItemMapper;
import vn.edu.hcmute.utecare.model.Medicine;
import vn.edu.hcmute.utecare.model.Prescription;
import vn.edu.hcmute.utecare.model.PrescriptionItem;
import vn.edu.hcmute.utecare.repository.MedicineRepository;
import vn.edu.hcmute.utecare.repository.PrescriptionItemRepository;
import vn.edu.hcmute.utecare.repository.PrescriptionRepository;
import vn.edu.hcmute.utecare.service.PrescriptionItemService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrescriptionItemServiceImpl implements PrescriptionItemService {
    private final PrescriptionItemRepository prescriptionItemRepository;
    private final MedicineRepository medicineRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final PrescriptionItemMapper prescriptionItemMapper;

    @Override
    @Transactional
    public PrescriptionItemResponse addPrescriptionItem(PrescriptionItemRequest request) {
        log.info("Tạo mục thuốc mới với thông tin: {}", request);
        PrescriptionItem prescriptionItem = prescriptionItemMapper.toEntity(request);

        // Kiểm tra và thiết lập thuốc
        Medicine medicine = medicineRepository.findById(request.getMedicineId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thuốc với ID: " + request.getMedicineId()));
        prescriptionItem.setMedicine(medicine);

//        // Kiểm tra và thiết lập đơn thuốc
//        Prescription prescription = prescriptionRepository.findById(request.getPrescriptionId())
//                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn thuốc với ID: " + request.getPrescriptionId()));
//        prescriptionItem.setPrescription(prescription);

        PrescriptionItem savedItem = prescriptionItemRepository.save(prescriptionItem);
        log.info("Tạo mục thuốc thành công với ID: {}", savedItem.getId());
        return prescriptionItemMapper.toResponse(savedItem);
    }

    @Override
    @Transactional
    public PrescriptionItemResponse updatePrescriptionItem(Long id, PrescriptionItemRequest request) {
        log.info("Cập nhật mục thuốc với ID: {} và thông tin: {}", id, request);
        PrescriptionItem prescriptionItem = prescriptionItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy mục thuốc với ID: " + id));

        // Cập nhật thông tin mục thuốc
        prescriptionItemMapper.update(request, prescriptionItem);

        // Kiểm tra và cập nhật thuốc nếu có
        if (request.getMedicineId() != null) {
            Medicine medicine = medicineRepository.findById(request.getMedicineId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thuốc với ID: " + request.getMedicineId()));
            prescriptionItem.setMedicine(medicine);
        }

        // Kiểm tra và cập nhật đơn thuốc nếu có
//        if (request.getPrescriptionId() != null) {
//            Prescription prescription = prescriptionRepository.findById(request.getPrescriptionId())
//                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn thuốc với ID: " + request.getPrescriptionId()));
//            prescriptionItem.setPrescription(prescription);
//        }

        PrescriptionItem updatedItem = prescriptionItemRepository.save(prescriptionItem);
        log.info("Cập nhật mục thuốc thành công với ID: {}", id);
        return prescriptionItemMapper.toResponse(updatedItem);
    }

    @Override
    @Transactional
    public void deletePrescriptionItem(Long id) {
        log.info("Xóa mục thuốc với ID: {}", id);
        if (!prescriptionItemRepository.existsById(id)) {
            throw new ResourceNotFoundException("Không tìm thấy mục thuốc với ID: " + id);
        }
        prescriptionItemRepository.deleteById(id);
        log.info("Xóa mục thuốc thành công với ID: {}", id);
    }

    @Override
    public PrescriptionItemResponse getPrescriptionItemById(Long id) {
        log.info("Truy xuất mục thuốc với ID: {}", id);
        PrescriptionItem prescriptionItem = prescriptionItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy mục thuốc với ID: " + id));
        log.info("Truy xuất mục thuốc thành công với ID: {}", id);
        return prescriptionItemMapper.toResponse(prescriptionItem);
    }
}
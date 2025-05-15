package vn.edu.hcmute.utecare.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.utecare.dto.request.PrescriptionRequest;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
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
import vn.edu.hcmute.utecare.util.PaginationUtil;

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
    private final PrescriptionMapper prescriptionMapper;
    private final PrescriptionItemMapper prescriptionItemMapper;

    @Override
    public PrescriptionResponse getPrescriptionById(Long id) {
        log.info("Truy xuất đơn thuốc với ID: {}", id);
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn thuốc với ID: " + id));
        log.info("Truy xuất đơn thuốc thành công với ID: {}", id);
        return prescriptionMapper.toResponse(prescription);
    }

    @Override
    @Transactional
    public void deletePrescription(Long id) {
        log.info("Xóa đơn thuốc với ID: {}", id);
        if (!prescriptionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Không tìm thấy đơn thuốc với ID: " + id);
        }
        prescriptionRepository.deleteById(id);
        log.info("Xóa đơn thuốc thành công với ID: {}", id);
    }

    @Override
    public List<PrescriptionItemResponse> getAllPrescriptionItemsByPrescriptionId(Long prescriptionId) {
        log.info("Truy xuất danh sách mục thuốc với ID đơn thuốc: {}", prescriptionId);
        Prescription prescription = prescriptionRepository.findById(prescriptionId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn thuốc với ID: " + prescriptionId));
        Set<PrescriptionItem> prescriptionItems = prescription.getPrescriptionItems();
        log.info("Tìm thấy {} mục thuốc cho đơn thuốc ID: {}", prescriptionItems.size(), prescriptionId);
        return prescriptionItems.stream()
                .map(prescriptionItemMapper::toResponse)
                .toList();
    }

    @Override
    public PageResponse<PrescriptionResponse> getAllPrescriptions(int page, int size, String sort, String direction) {
        log.info("Truy xuất danh sách tất cả đơn thuốc: trang={}, kích thước={}, sắp xếp={}, hướng={}", page, size, sort, direction);
        Pageable pageable = PaginationUtil.createPageable(page, size, sort, direction);
        Page<Prescription> prescriptionPage = prescriptionRepository.findAll(pageable);
        return PageResponse.<PrescriptionResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalElements(prescriptionPage.getTotalElements())
                .totalPages(prescriptionPage.getTotalPages())
                .content(prescriptionPage.getContent().stream()
                        .map(prescriptionMapper::toResponse)
                        .toList())
                .build();
    }

    @Override
    @Transactional
    public PrescriptionResponse addPrescription(PrescriptionRequest request) {
        log.info("Tạo đơn thuốc mới với thông tin: {}", request);
        Prescription prescription = prescriptionMapper.toEntity(request);

        Encounter encounter = encounterRepository.findById(request.getEncounterId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy cuộc khám với ID: " + request.getEncounterId()));
        prescription.setEncounter(encounter);


        // Xử lý các mục thuốc
        Set<PrescriptionItem> prescriptionItems = request.getPrescriptionItems().stream()
                .map(itemRequest -> {
                    PrescriptionItem item = prescriptionItemMapper.toEntity(itemRequest);
                    Medicine medicine = medicineRepository.findById(itemRequest.getMedicineId())
                            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thuốc với ID: " + itemRequest.getMedicineId()));
                    item.setMedicine(medicine);
                    item.setPrescription(prescription);
                    return item;
                })
                .collect(Collectors.toSet());

        prescription.setPrescriptionItems(prescriptionItems);

        Prescription finalPrescription = prescriptionRepository.save(prescription);
        log.info("Tạo đơn thuốc thành công với ID: {}", finalPrescription.getId());
        return prescriptionMapper.toResponse(finalPrescription);
    }

    @Override
    @Transactional
    public PrescriptionResponse updatePrescription(Long id, PrescriptionRequest request) {
        log.info("Cập nhật đơn thuốc với ID: {} và thông tin: {}", id, request);
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn thuốc với ID: " + id));

        // Cập nhật thông tin đơn thuốc
        prescriptionMapper.update(request, prescription);

        // Cập nhật các mục thuốc nếu có
        if (request.getPrescriptionItems() != null) {
            // Xóa các mục thuốc cũ
            prescriptionItemRepository.deleteAll(prescription.getPrescriptionItems());
            prescription.setPrescriptionItems(null);

            // Tạo các mục thuốc mới
            Set<PrescriptionItem> newPrescriptionItems = request.getPrescriptionItems().stream()
                    .map(itemRequest -> {
                        PrescriptionItem item = prescriptionItemMapper.toEntity(itemRequest);
                        Medicine medicine = medicineRepository.findById(itemRequest.getMedicineId())
                                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thuốc với ID: " + itemRequest.getMedicineId()));
                        item.setMedicine(medicine);
                        item.setPrescription(prescription);
                        return item;
                    })
                    .collect(Collectors.toSet());

            prescriptionItemRepository.saveAll(newPrescriptionItems);
            prescription.setPrescriptionItems(newPrescriptionItems);
        }

        Prescription updatedPrescription = prescriptionRepository.save(prescription);
        log.info("Cập nhật đơn thuốc thành công với ID: {}", id);
        return prescriptionMapper.toResponse(updatedPrescription);
    }
}
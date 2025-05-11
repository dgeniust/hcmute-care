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
import vn.edu.hcmute.utecare.service.MedicineService;
import vn.edu.hcmute.utecare.util.PaginationUtil;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MedicineServiceImpl implements MedicineService {

    private final MedicineRepository medicineRepository;
    private final MedicineMapper medicineMapper;

    @Override
    public MedicineResponse getMedicineById(Long id) {
        log.info("Truy xuất thuốc với ID: {}", id);
        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thuốc với ID: " + id));
        log.info("Truy xuất thuốc thành công với ID: {}", id);
        return medicineMapper.toResponse(medicine);
    }

    @Override
    public MedicineResponse getMedicineByMedicineName(String medicineName) {
        log.info("Truy xuất thuốc với tên: {}", medicineName);
        Medicine medicine = medicineRepository.findByName(medicineName)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thuốc với tên: " + medicineName));
        log.info("Truy xuất thuốc thành công với tên: {}", medicineName);
        return medicineMapper.toResponse(medicine);
    }

    @Override
    public MedicineResponse createMedicine(MedicineRequest request) {
        log.info("Tạo thuốc mới với thông tin: {}", request);
        Medicine medicine = medicineMapper.toEntity(request);
        Medicine savedMedicine = medicineRepository.save(medicine);
        log.info("Tạo thuốc thành công với ID: {}", savedMedicine.getId());
        return medicineMapper.toResponse(savedMedicine);
    }

    @Override
    public MedicineResponse updateMedicine(Long id, MedicineRequest request) {
        log.info("Cập nhật thuốc với ID: {} và thông tin: {}", id, request);
        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thuốc với ID: " + id));
        medicineMapper.update(request, medicine);
        Medicine updatedMedicine = medicineRepository.save(medicine);
        log.info("Cập nhật thuốc thành công với ID: {}", id);
        return medicineMapper.toResponse(updatedMedicine);
    }

    @Override
    public void deleteMedicine(Long id) {
        log.info("Xóa thuốc với ID: {}", id);
        if (!medicineRepository.existsById(id)) {
            throw new ResourceNotFoundException("Không tìm thấy thuốc với ID: " + id);
        }
        medicineRepository.deleteById(id);
        log.info("Xóa thuốc thành công với ID: {}", id);
    }

    @Override
    public List<MedicineResponse> getAllMedicine() {
        log.info("Truy xuất danh sách tất cả thuốc");
        List<Medicine> medicines = medicineRepository.findAll();
        return medicines.stream().map(medicineMapper::toResponse).toList();
    }

    @Override
    public PageResponse<MedicineResponse> searchByName(String name, int page, int size, String sort, String direction) {
        log.info("Tìm kiếm thuốc với tên: {}, trang={}, kích thước={}, sắp xếp={}, hướng={}", name, page, size, sort, direction);
        Pageable pageable = PaginationUtil.createPageable(page, size, sort, direction);
        Page<Medicine> medicinePage = medicineRepository.findByNameContainingIgnoreCase(name, pageable);

        return PageResponse.<MedicineResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalElements(medicinePage.getTotalElements())
                .totalPages(medicinePage.getTotalPages())
                .content(medicinePage.getContent().stream()
                        .map(medicineMapper::toResponse)
                        .toList())
                .build();
    }
}
package vn.edu.hcmute.utecare.service;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import vn.edu.hcmute.utecare.dto.request.PrescriptionItemRequest;
import vn.edu.hcmute.utecare.dto.response.PrescriptionItemResponse;

import java.util.List;

public interface PrescriptionItemService {

    PrescriptionItemResponse addPrescriptionItem(PrescriptionItemRequest request);

    PrescriptionItemResponse updatePrescriptionItem(Long id, PrescriptionItemRequest request);

    void deletePrescriptionItem(Long id);

    PrescriptionItemResponse getPrescriptionItemById(Long id);

    List<PrescriptionItemResponse> getAllPrescriptionItems();
}

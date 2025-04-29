package vn.edu.hcmute.utecare.service;


import vn.edu.hcmute.utecare.dto.request.PrescriptionRequest;
import vn.edu.hcmute.utecare.dto.response.PrescriptionItemResponse;
import vn.edu.hcmute.utecare.dto.response.PrescriptionResponse;

import java.util.List;

public interface PrescriptionService {

    PrescriptionResponse getPrescriptionById(Long id);

    List<PrescriptionItemResponse> getAllPrescriptionItemsByPrescriptionId(Long prescriptionId);

    List<PrescriptionResponse> getAllPrescriptions();

    PrescriptionResponse addPrescription(PrescriptionRequest request);

    PrescriptionResponse updatePrescription(Long id, PrescriptionRequest request);

    void deletePrescription(Long id);

}

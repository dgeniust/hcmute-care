package vn.edu.hcmute.utecare.service;

import vn.edu.hcmute.utecare.dto.request.MedicineRequest;
import vn.edu.hcmute.utecare.dto.request.PrescriptionItemRequest;
import vn.edu.hcmute.utecare.dto.response.MedicineResponse;
import vn.edu.hcmute.utecare.model.Medicine;

import java.util.List;

public interface MedicineService {

    MedicineResponse getMedicineById(Long id);

    MedicineResponse getMedicineByMedicineName(String medicineName);

    MedicineResponse createMedicine(MedicineRequest request);

    MedicineResponse updateMedicine(Long id,MedicineRequest request);

    void deleteMedicine(Long id);

    List<MedicineResponse> getAllMedicine();


}

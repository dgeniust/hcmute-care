package vn.edu.hcmute.utecare.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hcmute.utecare.model.MedicalRecord;

import java.util.Optional;

public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {
    Optional<MedicalRecord> findByBarcode(String barcode);
}
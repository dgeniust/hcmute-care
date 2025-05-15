package vn.edu.hcmute.utecare.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.hcmute.utecare.model.MedicalRecord;
import java.util.Optional;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {
    Optional<MedicalRecord> findByBarcode(String barcode);
    Page<MedicalRecord> findByCustomerId(Long customerId, Pageable pageable);
    MedicalRecord findByBarcodeAndCustomerId(String barcode, Long customerId);
    MedicalRecord findByEncountersId(Long encounterId);
    long count();
}
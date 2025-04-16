package vn.edu.hcmute.utecare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.hcmute.utecare.model.Encounter;

import java.util.List;
import java.util.Optional;
@Repository
public interface EncounterRepository extends JpaRepository<Encounter, Long> {
   // Optional<Encounter> findByPrescription_Id(Long prescriptionId);
    List<Encounter> findByMedicalRecord_Id(Long medicalRecordId);

}

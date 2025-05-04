package vn.edu.hcmute.utecare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hcmute.utecare.model.MedicalTest;

import java.time.LocalDateTime;
import java.util.List;

public interface MedicalTestRepository extends JpaRepository<MedicalTest, Long> {
    List<MedicalTest> findByEncounter_IdAndCreateDateBetween(Long encounterId, LocalDateTime start, LocalDateTime end);

}
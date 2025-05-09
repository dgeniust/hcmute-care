package vn.edu.hcmute.utecare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import vn.edu.hcmute.utecare.model.LaboratoryTests;
import vn.edu.hcmute.utecare.util.enumeration.EMedicalTest;

import java.time.LocalDateTime;
import java.util.List;

public interface LaboratoryTestsRepository extends JpaRepository<LaboratoryTests, Long> {

    List<LaboratoryTests> findByCreateDateBetweenAndStatus(LocalDateTime start, LocalDateTime end, EMedicalTest status);
    List<LaboratoryTests> findByEncounterIdAndCreateDateBetween(Long encounterId, LocalDateTime start, LocalDateTime end);
}
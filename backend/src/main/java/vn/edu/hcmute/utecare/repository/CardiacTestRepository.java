package vn.edu.hcmute.utecare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hcmute.utecare.model.CardiacTest;
import vn.edu.hcmute.utecare.model.LaboratoryTests;
import vn.edu.hcmute.utecare.util.enumeration.EMedicalTest;

import java.time.LocalDateTime;
import java.util.List;

public interface CardiacTestRepository extends JpaRepository<CardiacTest, Long> {
    List<CardiacTest> findByCreateDateBetweenAndStatus(LocalDateTime start, LocalDateTime end, EMedicalTest status);
    List<CardiacTest> findByEncounterIdAndCreateDateBetween(Long encounterId, LocalDateTime start, LocalDateTime end);
}
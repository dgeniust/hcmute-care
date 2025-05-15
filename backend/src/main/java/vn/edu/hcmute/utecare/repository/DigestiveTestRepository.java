package vn.edu.hcmute.utecare.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hcmute.utecare.model.CardiacTest;
import vn.edu.hcmute.utecare.model.DigestiveTest;
import vn.edu.hcmute.utecare.util.enumeration.EMedicalTest;

import java.time.LocalDateTime;
import java.util.List;

public interface DigestiveTestRepository extends JpaRepository<DigestiveTest, Long> {

    Page<DigestiveTest> findByTestNameContainingIgnoreCase(String testName, Pageable pageable);
    List<DigestiveTest> findByCreateDateBetweenAndStatus(LocalDateTime start, LocalDateTime end, EMedicalTest status);
    List<DigestiveTest> findByEncounterIdAndCreateDateBetween(Long encounterId, LocalDateTime start, LocalDateTime end);
}
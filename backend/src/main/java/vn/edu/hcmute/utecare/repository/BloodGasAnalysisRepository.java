package vn.edu.hcmute.utecare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hcmute.utecare.model.BloodGasAnalysis;
import vn.edu.hcmute.utecare.model.CardiacTest;
import vn.edu.hcmute.utecare.util.enumeration.EMedicalTest;

import java.time.LocalDateTime;
import java.util.List;

public interface BloodGasAnalysisRepository  extends JpaRepository<BloodGasAnalysis, Long> {
    List<BloodGasAnalysis> findByCreateDateBetweenAndStatus(LocalDateTime start, LocalDateTime end, EMedicalTest status);

}

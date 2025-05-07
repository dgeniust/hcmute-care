package vn.edu.hcmute.utecare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hcmute.utecare.model.DigestiveTest;
import vn.edu.hcmute.utecare.model.NerveConduction;
import vn.edu.hcmute.utecare.util.enumeration.EMedicalTest;

import java.time.LocalDateTime;
import java.util.List;

public interface NerveConductionRepository extends JpaRepository<NerveConduction, Long> {

    List<NerveConduction> findByCreateDateBetweenAndStatus(LocalDateTime start, LocalDateTime end, EMedicalTest status);

}

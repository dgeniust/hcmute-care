package vn.edu.hcmute.utecare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hcmute.utecare.model.MedicalTest;

public interface MedicalTestRepository extends JpaRepository<MedicalTest, Long> {
}
package vn.edu.hcmute.utecare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hcmute.utecare.model.LaboratoryTests;

public interface LaboratoryTestsRepository extends JpaRepository<LaboratoryTests, Long> {
}
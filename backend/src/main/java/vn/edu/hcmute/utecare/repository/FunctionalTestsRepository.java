package vn.edu.hcmute.utecare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hcmute.utecare.model.FunctionalTests;

public interface FunctionalTestsRepository extends JpaRepository<FunctionalTests, Long> {
}
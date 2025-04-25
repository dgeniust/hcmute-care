package vn.edu.hcmute.utecare.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hcmute.utecare.model.DigestiveTest;

public interface DigestiveTestRepository extends JpaRepository<DigestiveTest, Long> {

    Page<DigestiveTest> findByTestNameContainingIgnoreCase(String testName, Pageable pageable);
}
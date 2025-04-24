package vn.edu.hcmute.utecare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hcmute.utecare.model.ImagingTest;

public interface ImagingTestRepository extends JpaRepository<ImagingTest, Long> {
}
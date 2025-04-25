package vn.edu.hcmute.utecare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hcmute.utecare.model.LaboratoryTests;
import vn.edu.hcmute.utecare.model.Spirometry;

public interface SpirometryRepository extends JpaRepository<Spirometry, Long> {


}

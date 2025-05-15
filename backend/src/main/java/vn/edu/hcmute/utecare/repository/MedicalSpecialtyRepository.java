package vn.edu.hcmute.utecare.repository;


import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.edu.hcmute.utecare.model.MedicalSpecialty;

@Repository
public interface MedicalSpecialtyRepository extends JpaRepository<MedicalSpecialty, Integer> {

    @Query("SELECT m FROM MedicalSpecialty m WHERE " +
            "(:keyword IS NULL OR LOWER(m.name) LIKE LOWER('%' || :keyword || '%'))"
    )
    Page<MedicalSpecialty> searchMedicalSpecialties(
        @Param("keyword") String keyword,
        Pageable pageable
    );


}

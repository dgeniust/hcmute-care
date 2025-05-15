package vn.edu.hcmute.utecare.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.edu.hcmute.utecare.model.Nurse;

@Repository
public interface NurseRepository extends JpaRepository<Nurse, Long> {
    Page<Nurse> findByMedicalSpecialty_Id(Integer medicalSpecialtyId, Pageable pageable);

    @Query("SELECT n FROM Nurse n WHERE " +
            ":keyword IS NULL OR " +
            "LOWER(n.fullName) LIKE LOWER('%' || :keyword || '%') OR " +
            "LOWER(n.phone) LIKE LOWER('%' || :keyword || '%') OR " +
            "LOWER(n.position) LIKE LOWER('%' || :keyword || '%')")
    Page<Nurse> searchNurses(String keyword, Pageable pageable);

    @Query("SELECT d FROM Nurse d WHERE " +
            "d.medicalSpecialty.id = :medicalSpecialtyId AND " +
            "(:keyword IS NULL OR :keyword = '' OR " +
            "LOWER(d.fullName) LIKE LOWER('%' || :keyword || '%') OR " +
            "LOWER(d.phone) LIKE LOWER('%' || :keyword || '%') OR " +
            "LOWER(d.position) LIKE LOWER('%' || :keyword || '%'))")
    Page<Nurse> searchNursesByMedicalSpecialty(
            @Param("medicalSpecialtyId") Integer medicalSpecialtyId,
            @Param("keyword") String keyword,
            Pageable pageable);

    boolean existsByPhone(String phone);
    long count();
}
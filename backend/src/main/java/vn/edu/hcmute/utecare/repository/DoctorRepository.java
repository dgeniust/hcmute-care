package vn.edu.hcmute.utecare.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.edu.hcmute.utecare.model.Doctor;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Page<Doctor> findByMedicalSpecialty_Id(Integer id, Pageable pageable);

    @Query("SELECT d FROM Doctor d WHERE " +
            ":keyword IS NULL OR " +
            "LOWER(d.fullName) LIKE LOWER('%' || :keyword || '%') OR " +
            "LOWER(d.phone) LIKE LOWER('%' || :keyword || '%') OR " +
            "LOWER(d.position) LIKE LOWER('%' || :keyword || '%')")
    Page<Doctor> searchDoctors(
            @Param("keyword") String keyword,
            Pageable pageable);

    @Query("SELECT d FROM Doctor d WHERE " +
            "d.medicalSpecialty.id = :medicalSpecialtyId AND " +
            "(:keyword IS NULL OR :keyword = '' OR " +
            "LOWER(d.fullName) LIKE LOWER('%' || :keyword || '%') OR " +
            "LOWER(d.phone) LIKE LOWER('%' || :keyword || '%') OR " +
            "LOWER(d.position) LIKE LOWER('%' || :keyword || '%'))")
    Page<Doctor> searchDoctorsByMedicalSpecialty(
            @Param("medicalSpecialtyId") Integer medicalSpecialtyId,
            @Param("keyword") String keyword,
            Pageable pageable);

    boolean existsByPhone(String phone);
    long count();

}


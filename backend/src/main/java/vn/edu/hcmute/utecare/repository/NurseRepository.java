package vn.edu.hcmute.utecare.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.edu.hcmute.utecare.model.Nurse;

@Repository
public interface NurseRepository extends JpaRepository<Nurse, Long> {
    @Query("SELECT n FROM Nurse n WHERE " +
            ":keyword IS NULL OR " +
            "LOWER(n.fullName) LIKE LOWER('%' || :keyword || '%') OR " +
            "LOWER(n.phone) LIKE LOWER('%' || :keyword || '%') OR " +
            "LOWER(n.position) LIKE LOWER('%' || :keyword || '%')")
    Page<Nurse> searchNurses(String keyword, Pageable pageable);

    boolean existsByPhone(String phone);
}
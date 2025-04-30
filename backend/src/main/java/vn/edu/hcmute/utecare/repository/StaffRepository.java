package vn.edu.hcmute.utecare.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.edu.hcmute.utecare.model.Staff;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {
    @Query("SELECT s FROM Staff s WHERE " +
            ":keyword IS NULL OR " +
            "LOWER(s.fullName) LIKE LOWER('%' || :keyword || '%') OR " +
            "LOWER(s.phone) LIKE LOWER('%' || :keyword || '%') OR " +
            "LOWER(s.staffRole) LIKE LOWER('%' || :keyword || '%')")
    Page<Staff> searchStaff(String keyword, Pageable pageable);

    boolean existsByPhone(String phone);
}
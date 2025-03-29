package vn.edu.hcmute.utecare.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.edu.hcmute.utecare.model.Account;
import vn.edu.hcmute.utecare.model.User;
import vn.edu.hcmute.utecare.util.AccountStatus;
import vn.edu.hcmute.utecare.util.Role;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByUser_Phone(String phone);

    Optional<Account> findByUser_Id(Long userId);

    @Query("SELECT a FROM Account a JOIN a.user u WHERE " +
            "(:keyword IS NULL OR " +
            "   LOWER(u.phone) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "   LOWER(u.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "   LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
            "(:role IS NULL OR a.role = :role) AND " +
            "(:status IS NULL OR a.status = :status)")
    Page<Account> searchAccounts(
            @Param("keyword") String keyword,
            @Param("role") Role role,
            @Param("status") AccountStatus status,
            Pageable pageable);
}

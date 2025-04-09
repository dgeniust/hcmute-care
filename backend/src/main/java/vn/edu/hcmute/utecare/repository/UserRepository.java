package vn.edu.hcmute.utecare.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.edu.hcmute.utecare.model.User;
import vn.edu.hcmute.utecare.util.enumeration.Role;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u " +
            "LEFT JOIN FETCH u.account acc " +
            "WHERE (:role IS NULL OR acc.role = :role) " +
            "AND (:keyword IS NULL OR :keyword = '' " +
            "OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(u.phone) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%'))" +
            ")")
    Page<User> findAll(
            @Param(value = "keyword") String keyword,
            @Param(value = "role") Role role,
            Pageable pageable);
}

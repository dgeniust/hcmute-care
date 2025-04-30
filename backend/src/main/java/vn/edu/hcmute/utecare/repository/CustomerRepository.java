package vn.edu.hcmute.utecare.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.edu.hcmute.utecare.model.Customer;
import vn.edu.hcmute.utecare.util.enumeration.Membership;


public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByPhone(String phone);
    long count();

    @Query("SELECT c FROM Customer c WHERE " +
            "c.membership IS NULL OR c.membership = :membership AND " +
            "(:keyword IS NULL OR :keyword = '' OR " +
            "LOWER(c.fullName) LIKE LOWER('%' || :keyword || '%') OR " +
            "LOWER(c.email) LIKE LOWER('%' || :keyword || '%') OR " +
            "LOWER(c.phone) LIKE LOWER('%' || :keyword || '%'))")
    Page<Customer> searchCustomers(
            @Param("keyword") String keyword,
            @Param("membership") Membership membership,
            Pageable pageable);
}
package vn.edu.hcmute.utecare.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.edu.hcmute.utecare.model.Payment;
import vn.edu.hcmute.utecare.util.enumeration.PaymentStatus;

import java.time.LocalDateTime;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByTransactionId(String transactionId);

    @Query("SELECT p FROM Payment p " +
            "WHERE (:transactionId IS NULL OR p.transactionId = :transactionId) " +
            "AND (:paymentStatus IS NULL OR p.paymentStatus = :paymentStatus) " +
            "AND (:startDate IS NULL OR :endDate IS NULL OR p.paymentDate BETWEEN :startDate AND :endDate)")
    Page<Payment> findAllByTransactionIdAndPaymentStatusAndPaymentDateBetween(
            @Param("transactionId") String transactionId,
            @Param("paymentStatus") PaymentStatus paymentStatus,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable);

    Optional<Payment> findByAppointment_Id(Long id);
}

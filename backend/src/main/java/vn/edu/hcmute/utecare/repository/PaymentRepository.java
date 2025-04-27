package vn.edu.hcmute.utecare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hcmute.utecare.model.Payment;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Payment findByTransactionId(String transactionId);
}

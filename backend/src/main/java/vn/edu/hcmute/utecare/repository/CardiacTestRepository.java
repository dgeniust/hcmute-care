package vn.edu.hcmute.utecare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hcmute.utecare.model.CardiacTest;

public interface CardiacTestRepository extends JpaRepository<CardiacTest, Long> {
}
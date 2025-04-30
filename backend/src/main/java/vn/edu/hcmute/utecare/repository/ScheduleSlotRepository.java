package vn.edu.hcmute.utecare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.hcmute.utecare.model.ScheduleSlot;

@Repository
public interface ScheduleSlotRepository extends JpaRepository<ScheduleSlot, Long> {
}

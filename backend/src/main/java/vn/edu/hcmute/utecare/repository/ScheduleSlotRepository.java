package vn.edu.hcmute.utecare.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.edu.hcmute.utecare.model.ScheduleSlot;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ScheduleSlotRepository extends JpaRepository<ScheduleSlot, Long> {

    List<ScheduleSlot> findBySchedule_Doctor_IdAndSchedule_Date(Long id, LocalDate date);

    @Query("SELECT s FROM ScheduleSlot s " +
            "JOIN FETCH s.schedule sch " +
            "JOIN FETCH sch.doctor d " +
            "JOIN FETCH d.medicalSpecialty " +
            "WHERE s.id IN :ids")
    List<ScheduleSlot> findByIdsWithScheduleAndDoctor(@Param("ids") List<Long> ids);
}

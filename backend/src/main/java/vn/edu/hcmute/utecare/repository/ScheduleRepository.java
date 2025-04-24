package vn.edu.hcmute.utecare.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.edu.hcmute.utecare.model.Schedule;

import java.time.LocalDate;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    boolean existsByDoctor_IdAndDateAndTimeSlot_Id(Long doctorId, LocalDate date, Integer timeSlotId);

    @EntityGraph(attributePaths = {"doctor", "timeSlot"})
    @Query("SELECT s FROM Schedule s WHERE " +
            "(:doctorId IS NULL OR s.doctor.id = :doctorId) AND " +
            "(:date IS NULL OR s.date = :date) AND " +
            "(:timeSlotId IS NULL OR s.timeSlot.id = :timeSlotId)")
    Page<Schedule> searchDoctorSchedules(
            @Param("doctorId") Long doctorId,
            @Param("date") LocalDate date,
            @Param("timeSlotId") Integer timeSlotId,
            Pageable pageable);

    @EntityGraph(attributePaths = {"doctor", "timeSlot"})
    @Query("SELECT s FROM Schedule s WHERE " +
            "s.doctor.id = :doctorId AND " +
            "(:date IS NULL OR s.date = :date) AND " +
            "s.bookedSlots < s.maxSlots")
    Page<Schedule> findAvailableSchedules(
            @Param("doctorId") Long doctorId,
            @Param("date") LocalDate date,
            Pageable pageable);
}

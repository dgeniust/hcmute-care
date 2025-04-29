package vn.edu.hcmute.utecare.repository;

import io.lettuce.core.dynamic.annotation.Param;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;
import vn.edu.hcmute.utecare.model.Schedule;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Set;

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


    List<Schedule> findAllByIdIn(List<Long> scheduleIds);

    @Query("SELECT s FROM Schedule s " +
            "LEFT JOIN FETCH s.doctor d " +
            "LEFT JOIN FETCH s.timeSlot t " +
            "WHERE d.medicalSpecialty.id = :medicalSpecialtyId " +
            "AND (:date IS NULL OR s.date = :date) " +
            "AND s.bookedSlots < s.maxSlots " +
            "ORDER BY s.doctor.id, t.startTime")
    List<Schedule> findAvailableSchedulesByMedicalSpecialtyId(
            @Param("medicalSpecialtyId") Integer medicalSpecialtyId,
            @Param("date") LocalDate date);
}

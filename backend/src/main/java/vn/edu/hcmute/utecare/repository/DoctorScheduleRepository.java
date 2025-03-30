package vn.edu.hcmute.utecare.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.edu.hcmute.utecare.model.DoctorSchedule;

import java.time.LocalDate;

@Repository
public interface DoctorScheduleRepository extends JpaRepository<DoctorSchedule, Long> {
    boolean existsByDoctor_IdAndDateAndTimeSlot_Id(Long doctorId, LocalDate date, Integer timeSlotId);

    @EntityGraph(attributePaths = {"doctor", "timeSlot"})
    @Query("SELECT ds FROM DoctorSchedule ds WHERE " +
            "(:doctorId IS NULL OR ds.doctor.id = :doctorId) AND " +
            "(:date IS NULL OR ds.date = :date) AND " +
            "(:timeSlotId IS NULL OR ds.timeSlot.id = :timeSlotId)")
    Page<DoctorSchedule> searchDoctorSchedules(
            @Param("doctorId") Long doctorId,
            @Param("date") LocalDate date,
            @Param("timeSlotId") Integer timeSlotId,
            Pageable pageable);

    @EntityGraph(attributePaths = {"doctor", "timeSlot"})
    @Query("SELECT ds FROM DoctorSchedule ds WHERE " +
            "ds.doctor.id = :doctorId AND " +
            "(:date IS NULL OR ds.date = :date) AND " +
            "ds.bookedSlots < ds.maxSlots")
    Page<DoctorSchedule> findAvailableSchedules(
            @Param("doctorId") Long doctorId,
            @Param("date") LocalDate date,
            Pageable pageable);
}

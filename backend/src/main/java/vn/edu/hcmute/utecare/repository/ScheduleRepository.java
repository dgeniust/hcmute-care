package vn.edu.hcmute.utecare.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.edu.hcmute.utecare.model.Schedule;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    boolean existsByDoctorIdAndDate(Long doctorId, LocalDate date);

    @Query("SELECT s FROM Schedule s WHERE " +
            "(:doctorId IS NULL OR s.doctor.id = :doctorId) " +
            "AND (:roomId IS NULL OR s.roomDetail.id = :roomId) " +
            "AND (:startDate IS NULL OR s.date >= :startDate) " +
            "AND (:endDate IS NULL OR s.date <= :endDate)")
    Page<Schedule> findAllByDoctorIdAndRoomIdAndDateBetween(Long doctorId, Integer roomId, LocalDate startDate, LocalDate endDate, Pageable pageable);

    List<Schedule> findAllByDoctor_MedicalSpecialty_IdAndDate(Integer medicalSpecialtyId, LocalDate date);

    @Query("SELECT s FROM Schedule s WHERE " +
            "(:doctorId IS NULL OR s.doctor.id = :doctorId) " +
            "AND (:startDate IS NULL OR s.date >= :startDate) " +
            "AND (:endDate IS NULL OR s.date <= :endDate)")
    Page<Schedule> findAllByDoctorIdAndDateBetween(Long doctorId, LocalDate startDate, LocalDate endDate, Pageable pageable);
}

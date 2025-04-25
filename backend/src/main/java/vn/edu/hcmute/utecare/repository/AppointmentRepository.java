package vn.edu.hcmute.utecare.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.edu.hcmute.utecare.model.Appointment;
import vn.edu.hcmute.utecare.model.Schedule;
import vn.edu.hcmute.utecare.model.MedicalRecord;
import vn.edu.hcmute.utecare.util.enumeration.AppointmentStatus;

import java.time.LocalDate;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long>, JpaSpecificationExecutor<Appointment> {
    @Query("SELECT DISTINCT a FROM Appointment a " +
            "LEFT JOIN FETCH a.medicalRecord mr " +
            "LEFT JOIN FETCH mr.patient p " +
            "LEFT JOIN FETCH a.schedules asch " +
            "LEFT JOIN FETCH asch.schedule s " +
            "LEFT JOIN FETCH s.doctor d " +
            "LEFT JOIN FETCH d.medicalSpecialty ms " +
            "LEFT JOIN FETCH s.roomDetail rd " +
            "LEFT JOIN FETCH s.timeSlot ts " +
            "WHERE (:startDate IS NULL OR CAST(a.createdAt AS LocalDate) >= :startDate) " +
            "AND (:endDate IS NULL OR CAST(a.createdAt AS LocalDate) <= :endDate)")
    Page<Appointment> findAppointmentsWithDetails(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable
    );

    @Query("SELECT DISTINCT a FROM Appointment a " +
            "LEFT JOIN FETCH a.medicalRecord mr " +
            "LEFT JOIN FETCH mr.patient p " +
            "LEFT JOIN FETCH a.schedules asch " +
            "LEFT JOIN FETCH asch.schedule s " +
            "LEFT JOIN FETCH s.doctor d " +
            "LEFT JOIN FETCH d.medicalSpecialty ms " +
            "LEFT JOIN FETCH s.roomDetail rd " +
            "LEFT JOIN FETCH s.timeSlot ts " +
            "WHERE (a.medicalRecord = :medicalRecord)")
    Page<Appointment> findAppointmentsWithDetails(
            @Param("medicalRecord") MedicalRecord medicalRecord,
            Pageable pageable
    );


}

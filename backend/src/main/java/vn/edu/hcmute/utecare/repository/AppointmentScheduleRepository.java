package vn.edu.hcmute.utecare.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.edu.hcmute.utecare.model.AppointmentSchedule;
import vn.edu.hcmute.utecare.model.Schedule;
import vn.edu.hcmute.utecare.util.enumeration.AppointmentStatus;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AppointmentScheduleRepository extends JpaRepository<AppointmentSchedule, Long> {

    @Query("SELECT aps FROM AppointmentSchedule aps " +
            "JOIN aps.appointment a " +
            "JOIN aps.schedule s " +
            "JOIN s.timeSlot ts " +
            "JOIN s.doctor d " +
            "JOIN a.medicalRecord mr " +
            "JOIN mr.patient p " +
            "WHERE d.id = :doctorId " +
            "AND (:date IS NULL OR s.date = :date) " +
            "AND (:status IS NULL OR aps.status = :status)" +
            "AND (:timeSlotId IS NULL OR ts.id = :timeSlotId)")
    Page<AppointmentSchedule> findAllByDoctorIdAndStatusAndTimeSlotId(
            @Param("doctorId") Long doctorId,
            @Param("date") LocalDate date,
            @Param("status") AppointmentStatus status,
            @Param("timeSlotId") Integer timeSlotId,
            Pageable pageable);

    List<AppointmentSchedule> findByAppointmentMedicalRecordIdAndScheduleIdIn(Long medicalRecordId, List<Long> scheduleIds);

    Integer countByScheduleId(Long scheduleId);

    Integer countByScheduleAndStatus(Schedule schedule, AppointmentStatus status);
}

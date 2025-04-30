package vn.edu.hcmute.utecare.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.edu.hcmute.utecare.dto.response.DoctorTicketSummaryResponse;
import vn.edu.hcmute.utecare.model.Ticket;
import vn.edu.hcmute.utecare.util.enumeration.TicketStatus;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    @Query("SELECT t FROM Ticket t " +
            "JOIN t.scheduleSlot ss " +
            "JOIN ss.schedule s " +
            "WHERE ss.id = :scheduleSlotId " +
            "AND (:status IS NULL OR t.status = :status)")
    List<Ticket> getTicketSummaryByScheduleSlotId(
            @Param("scheduleSlotId") Long scheduleSlotId,
            @Param("status") TicketStatus status
    );

    @Query("SELECT t FROM Ticket t " +
            "JOIN t.appointment a " +
            "JOIN a.medicalRecord md " +
            "WHERE md.id = :medicalRecordId " +
            "AND (:status IS NULL OR t.status = :status)")
    Page<Ticket> findAllByMedicalRecordIdAndStatus(
            @Param("medicalRecordId") Long medicalRecordId,
            @Param("status") TicketStatus status,
            Pageable pageable);

    Integer countByScheduleSlot_IdAndStatus(Long id, TicketStatus ticketStatus);
}

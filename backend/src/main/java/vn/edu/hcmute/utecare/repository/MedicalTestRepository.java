package vn.edu.hcmute.utecare.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.edu.hcmute.utecare.model.MedicalTest;
import vn.edu.hcmute.utecare.util.enumeration.EMedicalTest;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MedicalTestRepository extends JpaRepository<MedicalTest, Long> {
    List<MedicalTest> findByEncounter_IdAndCreateDateBetween(Long encounterId, LocalDateTime start, LocalDateTime end);
    @Query("SELECT mt FROM MedicalTest mt " +
            "JOIN mt.encounter e " +
            "JOIN e.medicalRecord mr " +
            "WHERE mr.patient.id = :patientId")
    List<MedicalTest> findByPatientId(@Param("patientId") Long patientId);

    // Truy vấn MedicalTest theo patientId và khoảng thời gian
    @Query("SELECT mt FROM MedicalTest mt " +
            "JOIN mt.encounter e " +
            "JOIN e.medicalRecord mr " +
            "WHERE mr.patient.id = :patientId " +
            "AND mt.createDate BETWEEN :start AND :end")
    List<MedicalTest> findByPatientIdAndCreateDateBetween(
            @Param("patientId") Long patientId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);


    Optional<MedicalTest> findByEncounterIdAndCreateDateBetween(Long encounterId, LocalDateTime startOfDay, LocalDateTime endOfDay);

    Optional<MedicalTest> findByCreateDateBetweenAndStatus(LocalDateTime startOfDay, LocalDateTime endOfDay, EMedicalTest statusEnum);
}
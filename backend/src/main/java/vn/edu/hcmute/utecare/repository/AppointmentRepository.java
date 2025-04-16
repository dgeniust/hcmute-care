package vn.edu.hcmute.utecare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.hcmute.utecare.model.Appointment;
import vn.edu.hcmute.utecare.model.DoctorSchedule;
import vn.edu.hcmute.utecare.model.MedicalRecord;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    boolean existsByDoctorScheduleAndMedicalRecord(DoctorSchedule doctorSchedule, MedicalRecord medicalRecord);
}

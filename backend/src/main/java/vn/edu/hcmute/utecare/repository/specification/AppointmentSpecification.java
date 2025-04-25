package vn.edu.hcmute.utecare.repository.specification;

import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.jpa.domain.Specification;
import vn.edu.hcmute.utecare.model.*;
import vn.edu.hcmute.utecare.util.enumeration.AppointmentStatus;
import vn.edu.hcmute.utecare.util.enumeration.Gender;

@Getter
@AllArgsConstructor
public class AppointmentSpecification implements Specification<Appointment> {

    private SpecSearchCriteria criteria;

    @Override
    public Predicate toPredicate(Root<Appointment> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        String key = criteria.getKey();
        Object value = criteria.getValue();
        SearchOperation operation = criteria.getOperation();

        // Handle joins for related entities
        switch (key) {
            case "patientName":
            case "patientEmail":
            case "patientPhone":
            case "patientCccd":
            case "patientGender":
                // Join: Appointment -> MedicalRecord -> Patient
                Join<Appointment, MedicalRecord> medicalRecordJoin = root.join("medicalRecord");
                Join<MedicalRecord, Patient> patientJoin = medicalRecordJoin.join("patient");
                String patientField = key.replace("patient", "").toLowerCase();
                if (key.equals("patientGender")) {
                    return builder.equal(patientJoin.get(patientField), Gender.valueOf(value.toString()));
                }
                return buildPredicate(patientJoin.get(patientField), builder, operation, value);

            case "ticketCode":
            case "status":
                // Join: Appointment -> AppointmentSchedule
                Join<Appointment, AppointmentSchedule> scheduleJoin = root.join("appointmentSchedules");
                String scheduleField = key;
                if (key.equals("status")) {
                    return builder.equal(scheduleJoin.get(scheduleField), AppointmentStatus.valueOf(value.toString()));
                }
                return buildPredicate(scheduleJoin.get(scheduleField), builder, operation, value);

            case "scheduleDate":
            case "bookedSlots":
            case "maxSlots":
                // Join: Appointment -> AppointmentSchedule -> Schedule
                Join<Appointment, AppointmentSchedule> apptScheduleJoin = root.join("appointmentSchedules");
                Join<AppointmentSchedule, Schedule> scheduleDateJoin = apptScheduleJoin.join("schedule");
                return buildPredicate(scheduleDateJoin.get(key), builder, operation, value);

            case "roomName":
            case "building":
            case "floor":
                // Join: Appointment -> AppointmentSchedule -> Schedule -> RoomDetail
                Join<Appointment, AppointmentSchedule> apptScheduleRoomJoin = root.join("appointmentSchedules");
                Join<AppointmentSchedule, Schedule> scheduleRoomJoin = apptScheduleRoomJoin.join("schedule");
                Join<Schedule, RoomDetail> roomJoin = scheduleRoomJoin.join("roomDetail");
                return buildPredicate(roomJoin.get(key), builder, operation, value);

            case "startTime":
            case "endTime":
                // Join: Appointment -> AppointmentSchedule -> Schedule -> TimeSlot
                Join<Appointment, AppointmentSchedule> apptScheduleTimeJoin = root.join("appointmentSchedules");
                Join<AppointmentSchedule, Schedule> scheduleTimeJoin = apptScheduleTimeJoin.join("schedule");
                Join<Schedule, TimeSlot> timeSlotJoin = scheduleTimeJoin.join("timeSlot");
                return buildPredicate(timeSlotJoin.get(key), builder, operation, value);

            case "specialtyName":
            case "specialtyPrice":
                // Join: Appointment -> AppointmentSchedule -> Schedule -> Doctor -> MedicalSpecialty
                Join<Appointment, AppointmentSchedule> apptScheduleSpecialtyJoin = root.join("appointmentSchedules");
                Join<AppointmentSchedule, Schedule> scheduleSpecialtyJoin = apptScheduleSpecialtyJoin.join("schedule");
                Join<Schedule, Doctor> doctorSpecialtyJoin = scheduleSpecialtyJoin.join("doctor");
                Join<Doctor, MedicalSpecialty> specialtyJoin = doctorSpecialtyJoin.join("medicalSpecialty");
                String specialtyField = key.replace("specialty", "").toLowerCase();
                return buildPredicate(specialtyJoin.get(specialtyField), builder, operation, value);

            case "doctorPosition":
            case "doctorFullName":
            case "doctorEmail":
            case "doctorPhone":
                // Join: Appointment -> AppointmentSchedule -> Schedule -> Doctor
                Join<Appointment, AppointmentSchedule> apptScheduleDoctorJoin = root.join("appointmentSchedules");
                Join<AppointmentSchedule, Schedule> scheduleDoctorJoin = apptScheduleDoctorJoin.join("schedule");
                Join<Schedule, Doctor> doctorJoin = scheduleDoctorJoin.join("doctor");
                if (key.startsWith("doctor") && !key.equals("doctorPosition") && !key.equals("doctorQualification")) {
                    Join<Doctor, User> userJoin = doctorJoin.join("user");
                    String userField = key.replace("doctor", "").toLowerCase();
                    return buildPredicate(userJoin.get(userField), builder, operation, value);
                }
                return buildPredicate(doctorJoin.get(key.replace("doctor", "").toLowerCase()), builder, operation, value);

            default:
                // Direct fields in Appointment (e.g., createdAt, updatedAt)
                return buildPredicate(root.get(key), builder, operation, value);
        }
    }

    private Predicate buildPredicate(Path<?> path, CriteriaBuilder builder, SearchOperation operation, Object value) {
        return switch (operation) {
            case EQUALITY -> builder.equal(path, value);
            case NEGATION -> builder.notEqual(path, value);
            case GREATER_THAN -> builder.greaterThan(path.as(String.class), value.toString());
            case LESS_THAN -> builder.lessThan(path.as(String.class), value.toString());
            case LIKE -> builder.like(path.as(String.class), "%" + value + "%");
            case STARTS_WITH -> builder.like(path.as(String.class), value + "%");
            case ENDS_WITH -> builder.like(path.as(String.class), "%" + value);
            case CONTAINS -> builder.like(path.as(String.class), "%" + value + "%");
        };
    }
}
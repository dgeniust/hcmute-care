package vn.edu.hcmute.utecare.repository.specification;

import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.jpa.domain.Specification;
import vn.edu.hcmute.utecare.model.Appointment;
import vn.edu.hcmute.utecare.model.MedicalRecord;
import vn.edu.hcmute.utecare.model.Patient;

@Getter
@AllArgsConstructor
public class AppointmentSpecification implements Specification<Appointment> {

    private SpecSearchCriteria criteria;

    @Override
    public Predicate toPredicate(Root<Appointment> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        if (criteria.getKey().startsWith("medicalRecord.")) {
            Join<Appointment, MedicalRecord> medicalRecordJoin = root.join("medicalRecord");
            String key = criteria.getKey().substring("medicalRecord.".length());
            return buildPredicate(medicalRecordJoin, key, builder);
        } else if (criteria.getKey().startsWith("patient.")) {
            Join<Appointment, MedicalRecord> medicalRecordJoin = root.join("medicalRecord");
            Join<MedicalRecord, Patient> patientJoin = medicalRecordJoin.join("patient");
            String key = criteria.getKey().substring("patient.".length());
            return buildPredicate(patientJoin, key, builder);
        } else {
            return buildPredicate(root, criteria.getKey(), builder);
        }
    }

    private Predicate buildPredicate(Path<?> path, String key, CriteriaBuilder builder) {
        return switch (criteria.getOperation()) {
            case EQUALITY -> builder.equal(path.get(key), criteria.getValue());
            case NEGATION -> builder.notEqual(path.get(key), criteria.getValue());
            case GREATER_THAN -> builder.greaterThan(path.get(key), criteria.getValue().toString());
            case LESS_THAN -> builder.lessThan(path.get(key), criteria.getValue().toString());
        };
    }
}
package vn.edu.hcmute.utecare.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.model.Appointment;
import vn.edu.hcmute.utecare.model.MedicalRecord;
import vn.edu.hcmute.utecare.model.Patient;
import vn.edu.hcmute.utecare.repository.criteria.AppointmentSearchQueryCriteriaConsumer;
import vn.edu.hcmute.utecare.repository.criteria.SearchCriteria;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static vn.edu.hcmute.utecare.util.AppConst.*;

@Component
@Slf4j
public class SearchRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public PageResponse<Appointment> searchAppointmentByCriteria(Pageable pageable, String[] appointment, String[] medicalRecord, String[] patient) {
        log.info("Search appointment with criteria");

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Appointment> query = builder.createQuery(Appointment.class);
        Root<Appointment> appointmentRoot = query.from(Appointment.class);
        Join<Appointment, MedicalRecord> medicalRecordJoin = appointmentRoot.join("medicalRecord");
        Join<MedicalRecord, Patient> patientJoin = medicalRecordJoin.join("patient");

        List<Predicate> appointmentPredicates = new ArrayList<>();
        List<Predicate> medicalRecordPredicates = new ArrayList<>();
        List<Predicate> patientPredicates = new ArrayList<>();

        Pattern pattern = Pattern.compile(SEARCH_SPEC_OPERATOR);

        // Process appointment criteria
        if (appointment != null) {
            for (String a : appointment) {
                Matcher matcher = pattern.matcher(a);
                if (matcher.find()) {
                    SearchCriteria criteria = new SearchCriteria(matcher.group(1), matcher.group(2), matcher.group(3));
                    appointmentPredicates.add(toAppointmentPredicate(appointmentRoot, builder, criteria));
                }
            }
        }

        if (medicalRecord != null) {
            for (String m : medicalRecord) {
                Matcher matcher = pattern.matcher(m);
                if (matcher.find()) {
                    SearchCriteria criteria = new SearchCriteria(matcher.group(1), matcher.group(2), matcher.group(3));
                    medicalRecordPredicates.add(toMedicalRecordPredicate(medicalRecordJoin, builder, criteria));
                }
            }
        }

        // Process patient criteria
        if (patient != null) {
            for (String p : patient) {
                Matcher matcher = pattern.matcher(p);
                if (matcher.find()) {
                    SearchCriteria criteria = new SearchCriteria(matcher.group(1), matcher.group(2), matcher.group(3));
                    patientPredicates.add(toPatientPredicate(patientJoin, builder, criteria));
                }
            }
        }

        Predicate finalPredicate = builder.and(
                builder.or(appointmentPredicates.toArray(new Predicate[0])),
                builder.or(medicalRecordPredicates.toArray(new Predicate[0])),
                builder.or(patientPredicates.toArray(new Predicate[0]))
        );

        query.where(finalPredicate);

        List<Appointment> appointments = entityManager.createQuery(query)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        long count = countAppointments(appointment, medicalRecord, patient);

        return PageResponse.<Appointment>builder()
                .currentPage(pageable.getPageNumber())
                .pageSize(pageable.getPageSize())
                .totalPages((int) Math.ceil((double) count / pageable.getPageSize()))
                .content(appointments)
                .build();
    }

    private Predicate toAppointmentPredicate(Root<Appointment> root, CriteriaBuilder builder, SearchCriteria criteria) {
        return switch (criteria.getOperation()) {
            case ":" -> builder.equal(root.get(criteria.getKey()), criteria.getValue());
            case "!" -> builder.notEqual(root.get(criteria.getKey()), criteria.getValue());
            case ">" -> builder.greaterThan(root.get(criteria.getKey()), criteria.getValue().toString());
            case "<" -> builder.lessThan(root.get(criteria.getKey()), criteria.getValue().toString());
            case "~" -> builder.like(root.get(criteria.getKey()), "%" + criteria.getValue() + "%");
            default -> null;
        };
    }

    private Predicate toMedicalRecordPredicate(Join<Appointment, MedicalRecord> join, CriteriaBuilder builder, SearchCriteria criteria) {
        return switch (criteria.getOperation()) {
            case ":" -> builder.equal(join.get(criteria.getKey()), criteria.getValue());
            case "~" -> builder.like(join.get(criteria.getKey()), "%" + criteria.getValue() + "%");
            default -> null;
        };
    }

    private Predicate toPatientPredicate(Join<MedicalRecord, Patient> join, CriteriaBuilder builder, SearchCriteria criteria) {
        return switch (criteria.getOperation()) {
            case ":" -> builder.equal(join.get(criteria.getKey()), criteria.getValue());
            case "~" -> builder.like(join.get(criteria.getKey()), "%" + criteria.getValue() + "%");
            case ">" -> builder.greaterThan(join.get(criteria.getKey()), criteria.getValue().toString());
            case "<" -> builder.lessThan(join.get(criteria.getKey()), criteria.getValue().toString());
            default -> null;
        };
    }

    private long countAppointments(String[] appointment, String[] medicalRecord, String[] patient) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<Appointment> appointmentRoot = query.from(Appointment.class);
        Join<Appointment, MedicalRecord> medicalRecordJoin = appointmentRoot.join("medicalRecord");
        Join<MedicalRecord, Patient> patientJoin = medicalRecordJoin.join("patient");

        List<Predicate> appointmentPredicates = new ArrayList<>();
        List<Predicate> medicalRecordPredicates = new ArrayList<>();
        List<Predicate> patientPredicates = new ArrayList<>();

        Pattern pattern = Pattern.compile(SEARCH_SPEC_OPERATOR);

        if (appointment != null) {
            for (String a : appointment) {
                Matcher matcher = pattern.matcher(a);
                if (matcher.find()) {
                    SearchCriteria criteria = new SearchCriteria(matcher.group(1), matcher.group(2), matcher.group(3));
                    appointmentPredicates.add(toAppointmentPredicate(appointmentRoot, builder, criteria));
                }
            }
        }

        if (medicalRecord != null) {
            for (String m : medicalRecord) {
                Matcher matcher = pattern.matcher(m);
                if (matcher.find()) {
                    SearchCriteria criteria = new SearchCriteria(matcher.group(1), matcher.group(2), matcher.group(3));
                    medicalRecordPredicates.add(toMedicalRecordPredicate(medicalRecordJoin, builder, criteria));
                }
            }
        }

        if (patient != null) {
            for (String p : patient) {
                Matcher matcher = pattern.matcher(p);
                if (matcher.find()) {
                    SearchCriteria criteria = new SearchCriteria(matcher.group(1), matcher.group(2), matcher.group(3));
                    patientPredicates.add(toPatientPredicate(patientJoin, builder, criteria));
                }
            }
        }

        Predicate finalPredicate = builder.and(
                builder.or(appointmentPredicates.toArray(new Predicate[0])),
                builder.or(medicalRecordPredicates.toArray(new Predicate[0])),
                builder.or(patientPredicates.toArray(new Predicate[0]))
        );

        query.select(builder.count(appointmentRoot));
        query.where(finalPredicate);

        return entityManager.createQuery(query).getSingleResult();
    }
}
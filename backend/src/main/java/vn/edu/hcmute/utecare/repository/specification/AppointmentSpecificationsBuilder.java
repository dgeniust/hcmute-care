package vn.edu.hcmute.utecare.repository.specification;

import org.springframework.data.jpa.domain.Specification;
import vn.edu.hcmute.utecare.model.Appointment;

import java.util.ArrayList;
import java.util.List;

public class AppointmentSpecificationsBuilder {

    private final List<SpecSearchCriteria> params;

    public AppointmentSpecificationsBuilder() {
        params = new ArrayList<>();
    }

    public AppointmentSpecificationsBuilder with(String key, String operation, Object value, String prefix, String suffix) {
        return with(null, key, operation, value, prefix, suffix);
    }

    public AppointmentSpecificationsBuilder with(String orPredicate, String key, String operation, Object value, String prefix, String suffix) {
        SearchOperation searchOperation = SearchOperation.getSimpleOperation(operation.charAt(0));
        if (searchOperation != null) {
            if (searchOperation == SearchOperation.EQUALITY) {
                boolean startWithAsterisk = prefix != null && prefix.contains(SearchOperation.ZERO_OR_MORE_REGEX);
                boolean endWithAsterisk = suffix != null && suffix.contains(SearchOperation.ZERO_OR_MORE_REGEX);

                if (startWithAsterisk && endWithAsterisk) {
                    searchOperation = SearchOperation.CONTAINS;
                } else if (startWithAsterisk) {
                    searchOperation = SearchOperation.ENDS_WITH;
                } else if (endWithAsterisk) {
                    searchOperation = SearchOperation.STARTS_WITH;
                }
            }
            params.add(new SpecSearchCriteria(orPredicate, key, searchOperation, value));
        }
        return this;
    }

    public Specification<Appointment> build() {
        if (params.isEmpty()) {
            return null;
        }

        Specification<Appointment> result = new AppointmentSpecification(params.get(0));

        for (int i = 1; i < params.size(); i++) {
            result = params.get(i).isOrPredicate()
                    ? Specification.where(result).or(new AppointmentSpecification(params.get(i)))
                    : Specification.where(result).and(new AppointmentSpecification(params.get(i)));
        }

        return result;
    }

    public AppointmentSpecificationsBuilder with(AppointmentSpecification spec) {
        params.add(spec.getCriteria());
        return this;
    }

    public AppointmentSpecificationsBuilder with(SpecSearchCriteria criteria) {
        params.add(criteria);
        return this;
    }
}

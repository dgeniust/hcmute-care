package vn.edu.hcmute.utecare.repository.specification;

import org.springframework.data.jpa.domain.Specification;
import vn.edu.hcmute.utecare.model.Appointment;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static vn.edu.hcmute.utecare.util.AppConst.SEARCH_SPEC_OPERATOR;

public class AppointmentSpecificationsBuilder {

    private final List<SpecSearchCriteria> params;

    public AppointmentSpecificationsBuilder() {
        params = new ArrayList<>();
    }

    public AppointmentSpecificationsBuilder with(String key, String operation, Object value) {
        SearchOperation searchOperation = SearchOperation.getSimpleOperation(operation.charAt(0));
        if (searchOperation != null) {
            params.add(new SpecSearchCriteria(key, searchOperation, value));
        }
        return this;
    }

    public AppointmentSpecificationsBuilder with(String search) {
        Pattern pattern = Pattern.compile(SEARCH_SPEC_OPERATOR);
        Matcher matcher = pattern.matcher(search);
        if (matcher.find()) {
            with(matcher.group(1), matcher.group(2), matcher.group(3));
        }
        return this;
    }

    public Specification<Appointment> build() {
        if (params.isEmpty()) {
            return null;
        }

        Specification<Appointment> result = new AppointmentSpecification(params.get(0));

        for (int i = 1; i < params.size(); i++) {
            result = Specification.where(result).and(new AppointmentSpecification(params.get(i)));
        }

        return result;
    }
}
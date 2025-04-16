package vn.edu.hcmute.utecare.util;

import org.mapstruct.Named;
import org.springframework.stereotype.Component;
import vn.edu.hcmute.utecare.model.PrescriptionItem;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PrescriptionMapperHelper {
    @Named("mapPrescriptionItemIds")
    public List<Long> mapPrescriptionItemIds(Collection<PrescriptionItem> items) {
        if (items == null) return null;
        return items.stream()
                .map(PrescriptionItem::getId)
                .collect(Collectors.toList());
    }
}

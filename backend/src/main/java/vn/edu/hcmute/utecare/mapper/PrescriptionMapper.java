package vn.edu.hcmute.utecare.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import vn.edu.hcmute.utecare.dto.request.PrescriptionRequest;
import vn.edu.hcmute.utecare.dto.response.PrescriptionResponse;
import vn.edu.hcmute.utecare.model.Prescription;
import vn.edu.hcmute.utecare.model.PrescriptionItem;
import vn.edu.hcmute.utecare.util.PrescriptionMapperHelper;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PrescriptionMapper {

    PrescriptionMapper INSTANCE = Mappers.getMapper(PrescriptionMapper.class);

    Prescription toEntity(PrescriptionRequest request);

    @Mapping(target = "encounterId", source = "encounter.id")
    @Mapping(target = "prescriptionItemsId", source = "prescriptionItems", qualifiedByName = "mapPrescriptionItemIds")
    PrescriptionResponse toResponse(Prescription prescription);

    void update(PrescriptionRequest request, @MappingTarget Prescription prescription);

    @Named("mapPrescriptionItemIds")
    default List<Long> mapPrescriptionItemIds(Collection<PrescriptionItem> items) {
        if (items == null) return null;
        return items.stream()
                .map(PrescriptionItem::getId)
                .collect(Collectors.toList());
    }
}

package vn.edu.hcmute.utecare.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import vn.edu.hcmute.utecare.dto.request.PrescriptionRequest;
import vn.edu.hcmute.utecare.dto.response.PrescriptionResponse;
import vn.edu.hcmute.utecare.model.Prescription;
import vn.edu.hcmute.utecare.util.PrescriptionMapperHelper;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring"
        , uses = PrescriptionMapperHelper.class)public interface PrescriptionMapper {
    Prescription toEntity(PrescriptionRequest request);

    @Mapping(target = "encounterId", source = "encounter.id")
    @Mapping(target = "prescriptionItemsId", source = "prescriptionItems", qualifiedByName = "mapPrescriptionItemIds")
    PrescriptionResponse toResponse(Prescription prescription);

    void update(PrescriptionRequest request, @MappingTarget Prescription prescription);
}

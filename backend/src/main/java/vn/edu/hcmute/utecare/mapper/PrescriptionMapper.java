package vn.edu.hcmute.utecare.mapper;

import org.mapstruct.*;
import vn.edu.hcmute.utecare.dto.request.PrescriptionRequest;
import vn.edu.hcmute.utecare.dto.response.PrescriptionResponse;
import vn.edu.hcmute.utecare.model.Prescription;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
uses = {PrescriptionItemMapper.class})
public interface PrescriptionMapper {

    Prescription toEntity(PrescriptionRequest request);

    @Mapping(target = "encounterId", source = "encounter.id")
    PrescriptionResponse toResponse(Prescription prescription);

    void update(PrescriptionRequest request, @MappingTarget Prescription prescription);
}

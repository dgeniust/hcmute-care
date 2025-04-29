package vn.edu.hcmute.utecare.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import vn.edu.hcmute.utecare.dto.request.PrescriptionItemRequest;
import vn.edu.hcmute.utecare.dto.response.PrescriptionItemResponse;
import vn.edu.hcmute.utecare.model.PrescriptionItem;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PrescriptionItemMapper {
    PrescriptionItemMapper INSTANCE = Mappers.getMapper(PrescriptionItemMapper.class);

    PrescriptionItem toEntity(PrescriptionItemRequest request);

    @Mapping(target = "medicineId", source = "medicine.id")
    @Mapping(target = "prescriptionId", source = "prescription.id")
    @Mapping(target = "name", source = "medicine.name")
    PrescriptionItemResponse toResponse(PrescriptionItem prescriptionItem);

    void update(PrescriptionItemRequest request, @MappingTarget PrescriptionItem prescriptionItem);
}

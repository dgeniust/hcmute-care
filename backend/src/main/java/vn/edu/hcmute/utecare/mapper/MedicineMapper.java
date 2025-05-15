package vn.edu.hcmute.utecare.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import vn.edu.hcmute.utecare.dto.request.MedicineRequest;
import vn.edu.hcmute.utecare.dto.response.MedicineResponse;
import vn.edu.hcmute.utecare.model.Medicine;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MedicineMapper {

    Medicine toEntity(MedicineRequest request);

    MedicineResponse toResponse(Medicine medicine);

    void update(MedicineRequest request, @MappingTarget Medicine medicine);
}

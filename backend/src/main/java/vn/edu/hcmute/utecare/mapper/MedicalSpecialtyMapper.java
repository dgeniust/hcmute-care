package vn.edu.hcmute.utecare.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import vn.edu.hcmute.utecare.dto.request.MedicalSpecialtyRequest;
import vn.edu.hcmute.utecare.dto.response.MedicalSpecialtyResponse;
import vn.edu.hcmute.utecare.model.MedicalSpecialty;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MedicalSpecialtyMapper {
    MedicalSpecialty toEntity(MedicalSpecialtyRequest request);

    MedicalSpecialtyResponse toResponse(MedicalSpecialty medicalSpecialty);

    void update(MedicalSpecialtyRequest request, @MappingTarget MedicalSpecialty medicalSpecialty);
}

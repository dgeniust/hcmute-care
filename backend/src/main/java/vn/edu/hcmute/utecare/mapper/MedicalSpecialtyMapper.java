package vn.edu.hcmute.utecare.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import vn.edu.hcmute.utecare.dto.request.MedicalSpecialtyRequest;
import vn.edu.hcmute.utecare.dto.response.MedicalSpecialtyResponse;
import vn.edu.hcmute.utecare.model.MedicalSpecialty;

@Mapper(componentModel = "spring")
public interface MedicalSpecialtyMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "doctors", ignore = true)
    MedicalSpecialty toEntity(MedicalSpecialtyRequest request);

    MedicalSpecialtyResponse toResponse(MedicalSpecialty medicalSpecialty);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "doctors", ignore = true)
    void update(MedicalSpecialtyRequest request, @MappingTarget MedicalSpecialty medicalSpecialty);
}

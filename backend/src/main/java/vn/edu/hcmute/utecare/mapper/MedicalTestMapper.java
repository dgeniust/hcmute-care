package vn.edu.hcmute.utecare.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import vn.edu.hcmute.utecare.dto.request.MedicalTestRequest;
import vn.edu.hcmute.utecare.dto.response.MedicalTestResponse;
import vn.edu.hcmute.utecare.model.MedicalTest;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface MedicalTestMapper {

    MedicalTestMapper INSTANCE = Mappers.getMapper(MedicalTestMapper.class);

    @Mapping(source = "encounter.id", target = "encounterId")
    MedicalTestResponse toResponse(MedicalTest medicalTest);

    @Mapping(source = "encounterId", target = "encounter.id")
    void updateEntity(@MappingTarget MedicalTest medicalTest, MedicalTestRequest request);
}
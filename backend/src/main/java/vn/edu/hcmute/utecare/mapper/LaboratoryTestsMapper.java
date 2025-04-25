package vn.edu.hcmute.utecare.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import vn.edu.hcmute.utecare.dto.request.LaboratoryTestsRequest;
import vn.edu.hcmute.utecare.dto.response.LaboratoryTestsResponse;
import vn.edu.hcmute.utecare.model.LaboratoryTests;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface LaboratoryTestsMapper {

    LaboratoryTestsMapper INSTANCE = Mappers.getMapper(LaboratoryTestsMapper.class);

    @Mapping(source = "encounterId", target = "encounter.id")
    LaboratoryTests toEntity(LaboratoryTestsRequest request);

    @Mapping(source = "encounter.id", target = "encounterId")
    LaboratoryTestsResponse toResponse(LaboratoryTests laboratoryTests);

    @Mapping(source = "encounterId", target = "encounter.id")
    void updateEntity(@MappingTarget LaboratoryTests laboratoryTests, LaboratoryTestsRequest request);
}
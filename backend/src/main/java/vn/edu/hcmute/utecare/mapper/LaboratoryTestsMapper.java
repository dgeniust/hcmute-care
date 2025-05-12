package vn.edu.hcmute.utecare.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import vn.edu.hcmute.utecare.dto.request.LaboratoryTestsRequest;
import vn.edu.hcmute.utecare.dto.response.LaboratoryTestsResponse;
import vn.edu.hcmute.utecare.model.LaboratoryTests;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LaboratoryTestsMapper {
    LaboratoryTests toEntity(LaboratoryTestsRequest request);

    @Mapping(source = "encounter.id", target = "encounterId")
    LaboratoryTestsResponse toResponse(LaboratoryTests laboratoryTests);

    void updateEntity(@MappingTarget LaboratoryTests laboratoryTests, LaboratoryTestsRequest request);
}
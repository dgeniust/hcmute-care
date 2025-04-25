package vn.edu.hcmute.utecare.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import vn.edu.hcmute.utecare.dto.request.LaboratoryTestsRequest;
import vn.edu.hcmute.utecare.dto.request.SpirometryRequest;
import vn.edu.hcmute.utecare.dto.response.LaboratoryTestsResponse;
import vn.edu.hcmute.utecare.dto.response.SpirometryResponse;
import vn.edu.hcmute.utecare.model.LaboratoryTests;
import vn.edu.hcmute.utecare.model.Spirometry;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface SpirometryMapper {
    SpirometryMapper INSTANCE = Mappers.getMapper(SpirometryMapper.class);

    Spirometry toEntity(SpirometryRequest request);

    @Mapping(source = "encounter.id", target = "encounterId")
    SpirometryResponse toResponse(Spirometry spirometry);

    void updateEntity(@MappingTarget Spirometry spirometry, SpirometryRequest request);
}


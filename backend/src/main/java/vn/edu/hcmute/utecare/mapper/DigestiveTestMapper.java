package vn.edu.hcmute.utecare.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import vn.edu.hcmute.utecare.dto.request.DigestiveTestRequest;
import vn.edu.hcmute.utecare.dto.response.DigestiveTestResponse;
import vn.edu.hcmute.utecare.model.DigestiveTest;


@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DigestiveTestMapper {
    DigestiveTestMapper INSTANCE = Mappers.getMapper(DigestiveTestMapper.class);

    @Mapping(target = "encounter", ignore = true)
    DigestiveTest toEntity(DigestiveTestRequest request);

    @Mapping(source = "encounter.id", target = "encounterId")
    DigestiveTestResponse toResponse(DigestiveTest entity);

    @Mapping(target = "encounter", ignore = true)
    void updateEntity(@MappingTarget DigestiveTest entity, DigestiveTestRequest request);
}
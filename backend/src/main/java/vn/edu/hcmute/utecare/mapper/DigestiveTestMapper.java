package vn.edu.hcmute.utecare.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import vn.edu.hcmute.utecare.dto.request.DigestiveTestRequest;
import vn.edu.hcmute.utecare.dto.response.DigestiveTestResponse;
import vn.edu.hcmute.utecare.model.DigestiveTest;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DigestiveTestMapper {
    DigestiveTest toEntity(DigestiveTestRequest request);

    @Mapping(source = "encounter.id", target = "encounterId")
    DigestiveTestResponse toResponse(DigestiveTest entity);

    void updateEntity(@MappingTarget DigestiveTest entity, DigestiveTestRequest request);
}
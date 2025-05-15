package vn.edu.hcmute.utecare.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import vn.edu.hcmute.utecare.dto.request.CardiacTestRequest;
import vn.edu.hcmute.utecare.dto.response.CardiacTestResponse;
import vn.edu.hcmute.utecare.model.CardiacTest;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CardiacTestMapper {
    CardiacTest toEntity(CardiacTestRequest request);

    @Mapping(source = "encounter.id", target = "encounterId")
    CardiacTestResponse toResponse(CardiacTest entity);

    void updateEntity(@MappingTarget CardiacTest entity, CardiacTestRequest request);
}
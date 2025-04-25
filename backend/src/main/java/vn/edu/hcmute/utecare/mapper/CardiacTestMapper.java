package vn.edu.hcmute.utecare.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import vn.edu.hcmute.utecare.dto.request.CardiacTestRequest;
import vn.edu.hcmute.utecare.dto.response.CardiacTestResponse;
import vn.edu.hcmute.utecare.model.CardiacTest;


@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CardiacTestMapper {
    CardiacTestMapper INSTANCE = Mappers.getMapper(CardiacTestMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "encounter", ignore = true)
    CardiacTest toEntity(CardiacTestRequest request);

    @Mapping(source = "encounter.id", target = "encounterId")
    CardiacTestResponse toResponse(CardiacTest entity);

    @Mapping(target = "encounter", ignore = true)
    void updateEntity(@MappingTarget CardiacTest entity, CardiacTestRequest request);
}
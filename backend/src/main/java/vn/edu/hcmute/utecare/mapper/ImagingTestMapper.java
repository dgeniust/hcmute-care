package vn.edu.hcmute.utecare.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import vn.edu.hcmute.utecare.dto.request.ImagingTestRequest;
import vn.edu.hcmute.utecare.dto.response.ImagingTestResponse;
import vn.edu.hcmute.utecare.model.ImagingTest;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface ImagingTestMapper {

    ImagingTestMapper INSTANCE = Mappers.getMapper(ImagingTestMapper.class);

    ImagingTest toEntity(ImagingTestRequest request);

    @Mapping(source = "encounter.id", target = "encounterId")
    ImagingTestResponse toResponse(ImagingTest imagingTest);

    void updateEntity(@MappingTarget ImagingTest imagingTest, ImagingTestRequest request);
}
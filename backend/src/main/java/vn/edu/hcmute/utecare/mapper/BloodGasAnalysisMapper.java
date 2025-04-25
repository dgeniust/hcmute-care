package vn.edu.hcmute.utecare.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import vn.edu.hcmute.utecare.dto.request.BloodGasAnalysisRequest;
import vn.edu.hcmute.utecare.dto.response.BloodGasAnalysisResponse;
import vn.edu.hcmute.utecare.model.BloodGasAnalysis;


@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface BloodGasAnalysisMapper {
    BloodGasAnalysisMapper INSTANCE = Mappers.getMapper(BloodGasAnalysisMapper.class);

    BloodGasAnalysis toEntity(BloodGasAnalysisRequest request);

    @Mapping(source = "encounter.id", target = "encounterId")
    BloodGasAnalysisResponse toResponse(BloodGasAnalysis bloodGasAnalysis);

    void updateEntity(@MappingTarget BloodGasAnalysis bloodGasAnalysis, BloodGasAnalysisRequest request);


}

package vn.edu.hcmute.utecare.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import vn.edu.hcmute.utecare.dto.request.BloodGasAnalysisRequest;
import vn.edu.hcmute.utecare.dto.request.NerveConductionRequest;
import vn.edu.hcmute.utecare.dto.response.BloodGasAnalysisResponse;
import vn.edu.hcmute.utecare.dto.response.NerveConductionResponse;
import vn.edu.hcmute.utecare.model.BloodGasAnalysis;
import vn.edu.hcmute.utecare.model.NerveConduction;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface NerveConductionMapper {
    NerveConductionMapper INSTANCE = Mappers.getMapper(NerveConductionMapper.class);

    NerveConduction toEntity(NerveConductionRequest request);

    @Mapping(source = "encounter.id", target = "encounterId")
    NerveConductionResponse toResponse(NerveConduction nerveConduction);

    @Mapping(source = "encounterId", target = "encounter.id")
    void updateEntity(@MappingTarget NerveConduction nerveConduction, NerveConductionRequest request);

}

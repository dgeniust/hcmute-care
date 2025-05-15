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

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NerveConductionMapper {
    NerveConduction toEntity(NerveConductionRequest request);

    @Mapping(source = "encounter.id", target = "encounterId")
    NerveConductionResponse toResponse(NerveConduction nerveConduction);

    void updateEntity(@MappingTarget NerveConduction nerveConduction, NerveConductionRequest request);

}

package vn.edu.hcmute.utecare.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import vn.edu.hcmute.utecare.dto.request.NurseRequest;
import vn.edu.hcmute.utecare.dto.response.NurseResponse;
import vn.edu.hcmute.utecare.model.Nurse;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NurseMapper {
    NurseMapper INSTANCE = Mappers.getMapper(NurseMapper.class);

    Nurse toEntity(NurseRequest request);

    NurseResponse toResponse(Nurse nurse);

    void updateEntity(NurseRequest request, @MappingTarget Nurse nurse);
}
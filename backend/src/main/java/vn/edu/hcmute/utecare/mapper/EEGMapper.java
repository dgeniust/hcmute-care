package vn.edu.hcmute.utecare.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import vn.edu.hcmute.utecare.dto.request.EEGRequest;
import vn.edu.hcmute.utecare.dto.response.EEGResponse;
import vn.edu.hcmute.utecare.model.EEG;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface EEGMapper {
    EEGMapper INSTANCE = Mappers.getMapper(EEGMapper.class);

    EEG toEntity(EEGRequest request);

    @Mapping(source = "encounter.id", target = "encounterId")
    EEGResponse toResponse(EEG eeg);

    @Mapping(source = "encounterId", target = "encounter.id")
    void updateEntity(@MappingTarget EEG eeg, EEGRequest request);

}

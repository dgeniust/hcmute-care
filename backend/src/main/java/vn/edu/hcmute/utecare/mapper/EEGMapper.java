package vn.edu.hcmute.utecare.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import vn.edu.hcmute.utecare.dto.request.EEGRequest;
import vn.edu.hcmute.utecare.dto.response.EEGResponse;
import vn.edu.hcmute.utecare.model.EEG;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EEGMapper {
    EEG toEntity(EEGRequest request);

    @Mapping(source = "encounter.id", target = "encounterId")
    EEGResponse toResponse(EEG eeg);

    void updateEntity(@MappingTarget EEG eeg, EEGRequest request);

}

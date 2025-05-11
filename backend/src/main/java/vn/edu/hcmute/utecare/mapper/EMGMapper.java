package vn.edu.hcmute.utecare.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import vn.edu.hcmute.utecare.dto.request.EMGRequest;
import vn.edu.hcmute.utecare.dto.response.EMGResponse;
import vn.edu.hcmute.utecare.model.EMG;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EMGMapper {
    EMG toEntity(EMGRequest request);

    @Mapping(source = "encounter.id", target = "encounterId")
    EMGResponse toResponse(EMG emg);

    void updateEntity(@MappingTarget EMG emg, EMGRequest request);
}

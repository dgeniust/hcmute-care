package vn.edu.hcmute.utecare.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import vn.edu.hcmute.utecare.dto.request.EncounterRequest;
import vn.edu.hcmute.utecare.dto.response.EncounterResponse;
import vn.edu.hcmute.utecare.model.Encounter;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EncounterMapper {

    EncounterMapper INSTANCE = Mappers.getMapper(EncounterMapper.class);

    Encounter toEntity(EncounterRequest request);

    @Mapping(target = "medicalRecordId", source = "medicalRecord.id")
    EncounterResponse toResponse(Encounter encounter);

    void update(EncounterRequest request, @MappingTarget Encounter encounter);
}

package vn.edu.hcmute.utecare.mapper;

import org.mapstruct.*;
import vn.edu.hcmute.utecare.dto.request.EncounterRequest;
import vn.edu.hcmute.utecare.dto.response.EncounterPatientSummaryResponse;
import vn.edu.hcmute.utecare.dto.response.EncounterResponse;
import vn.edu.hcmute.utecare.model.Encounter;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
uses = {PrescriptionMapper.class})
public interface EncounterMapper {

    Encounter toEntity(EncounterRequest request);

    @Mapping(target = "medicalRecordId", source = "medicalRecord.id")
    EncounterResponse toResponse(Encounter encounter);

    void update(EncounterRequest request, @MappingTarget Encounter encounter);

    @Mapping(target = "medicalRecord", source = "medicalRecord")
    EncounterPatientSummaryResponse toEncounterPatientResponse(Encounter encounter);
    
}